package com.u17.net.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.text.TextUtils;

import com.u17.net.Request;
import com.u17.net.model.U17request;
import com.u17.net.model.VisitResult;


/**
 * @Package com.u17.core.util 
 * @user: pengpan
 * @time: 2014-4-14,下午2:45:24
 * @Description: TODO 执行网络请求 
 * @version
 */
public class HttpUtils {
	private static String TAG = HttpUtils.class.getSimpleName();
	private static int BUFFER_SIZE = 4096;
	private static int SHORT_TIME = 2000;
	private static int NORMAL_TIME = 5000;
	private static HttpParams params;
	private static HttpClient client;
	private static int currTime = NORMAL_TIME;
	public static final int NETVISITOR_ERROR_NONET=-21;
	public static final int NETVISITOR_ERROR_TIMEOUT=-22;
	public static final int NETVISITOR_ERROR_OTHER=-24;
	public static final int NETVISITOR_ERROR_SERVER=-20;
	
	public static final int NETVISITOR_ERROR_DATAPARSER=-23;
	public static final int NETVISITOR_CANCEL=-25;
	public static final int NETVISITOR_NORMAL=1;
	// 从网络加载数据
	public static VisitResult loadDataFromUrl(String requestUrl,
				Request visitor, HashMap<String, String> postData,
				int maxRetryTime, Context context, boolean isUseShortTimeOut,
				U17request u17Request) {
			boolean isDownNormal = false;
			boolean useGizp = false;
			int curRetryTime = 0;
			// 重试机制
			VisitResult visitResult = null;
			HttpRequestBase request = null;
			InputStream input = null;
			BufferedReader bufferedReader=null;
			client = new DefaultHttpClient();
			long start = System.currentTimeMillis();
			String requestResult="";
			ULog.record(TAG+" loadDataFromUrl", "request:"+requestUrl+",start");
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
				System.setProperty("http.keepAlive", "false");
			}
			StringBuffer sb=new StringBuffer();
			while (curRetryTime < maxRetryTime && !isDownNormal) {
				visitResult = new VisitResult();
				if (visitor.isCancel())
					break;
				if (!ContextUtil.isNetWorking(context)) {
					visitResult.setCode(NETVISITOR_ERROR_NONET);
					visitResult.setMessage("当前无网络");
					break;
				}
				int code = 0;
				// 判断是否是电信wap接入点，如果是需要使用10.0.0.72代理,暂时不支持https
				String proxyStr = NetUtil.getNetProxyStr(context);
				if (!TextUtils.isEmpty(proxyStr)) {
					useGizp = false;
				}
				if (visitor.isCancel()) {
					visitResult.setCode(NETVISITOR_CANCEL);
					visitResult.setMessage("the request is cancelled");
					break;
				}
				
				try {
					if (postData != null) {
						request = new HttpPost(requestUrl);
						// 加入多种数据类型的网络交互
						MultipartEntity multipartEntity = new MultipartEntity();
						Set<Entry<String, String>> entryParams = postData
								.entrySet();
						for (Entry<String, String> entryParam : entryParams) {
							if (entryParam.getKey().startsWith("file")) {
								File uploadFile = new File(entryParam.getValue());
								multipartEntity.addPart(entryParam.getKey(),
										new FileBody(uploadFile));
							} else {
								String val = entryParam.getValue();
								multipartEntity.addPart(
										entryParam.getKey(),
										new StringBody(val, Charset
												.forName(HTTP.UTF_8)));
							}
						}
						((HttpPost) request).setEntity(multipartEntity);
					} else {
						request = new HttpGet(requestUrl);
					}
					if (useGizp) {
						request.addHeader("Accept-Encoding", "gzip");
					}
					if(isUseShortTimeOut){
						currTime = SHORT_TIME;
					}else
						currTime = NORMAL_TIME;
					//ULog.record(TAG+" ", "当前超时设置:"+currTime);
					params=client.getParams();
					HttpConnectionParams.setConnectionTimeout(params, currTime);
			        HttpConnectionParams.setSoTimeout(params, currTime);
					if (!TextUtils.isEmpty(proxyStr)) {

						HttpHost host = new HttpHost(android.net.Proxy.getDefaultHost(),
								android.net.Proxy.getDefaultPort());
						client.getParams().setParameter("http.route.default-proxy",
								host);
						// if(isDebug){ULog.record( TAG+" loadDataFromUrl",
						// "current net is wap:"+proxyStr);}
					}
					ULog.record("httputils", "now start execute request:"+requestUrl);
					HttpResponse response = client.execute(request);
					ULog.record("httputils", "now end execute request:"+requestUrl+"\r\n");
					code = response.getStatusLine().getStatusCode();
					//ULog.record(TAG + "loadDataFromUrl","code:"+code);
					if (code == HttpStatus.SC_OK) {
						HttpEntity entity = response.getEntity();
						long length = entity.getContentLength();
						u17Request.setRequestGetDataBeginTime(System.currentTimeMillis());
						ULog.record("httputils","data length:"+length);
						long curByteNum = 0;
						if (useGizp
								&& entity.getContentEncoding() != null
								&& entity.getContentEncoding().getValue()
										.toLowerCase().contains("gzip")) {
							HeaderElement[] responseHeaders = entity
									.getContentEncoding().getElements();
							String header = "";
							for (int i = 0; i < responseHeaders.length; i++) {
								header += responseHeaders[i].getValue();
								header += ",";
							}
							// if(isDebug){ULog.record(TAG+ "loadDataFromUrl",
							// header);}
							input = new GZIPInputStream(input);
						} else {
							input = entity.getContent();
						}
						bufferedReader=new BufferedReader(new InputStreamReader(input,"UTF-8"));
						while ((requestResult=bufferedReader.readLine())!=null) {
							sb.append(requestResult);
						}
						u17Request.setRequestGetDataEndTime(System.currentTimeMillis());
						requestResult=sb.toString();
						if (!TextUtils.isEmpty(requestResult)) {
							visitResult.setCode(1);
							visitResult.setResult(requestResult);
							isDownNormal = true;
							u17Request.setRetryCount(curRetryTime);
							u17Request.setDataLength(requestResult.length());
							ULog.record("httputils", "request setcode and setRetryCount:"+curRetryTime+","+requestResult.length());
						}
					} else {
						isDownNormal = false;
						visitResult.setCode(NETVISITOR_ERROR_SERVER);
						visitResult.setMessage("错误的HTTP状态" + code);
						u17Request.setRetryCount(curRetryTime);
						u17Request.setRequestGetDataEndTime(System.currentTimeMillis());
						u17Request.setException("错误的HTTP状态" + code);
					}

				} catch (Exception ex) {
					isDownNormal = false;
					//ULog.record(TAG, "loadDataFromUrl error:"+ex.getMessage());
					if (ex instanceof SocketTimeoutException) {
						//ULog.record( TAG, "读取数据超时,请稍后重试");
						visitResult.setCode(NETVISITOR_ERROR_TIMEOUT);
						visitResult.setMessage(ex.getMessage());
						u17Request.setRequestGetDataEndTime(System.currentTimeMillis());
						u17Request.setRetryCount(curRetryTime);
						u17Request.setException(ex.getMessage());
					} else if (ex instanceof ConnectTimeoutException) {
					   // ULog.record( TAG, "连接超时,请稍后重试");
						visitResult.setCode(NETVISITOR_ERROR_TIMEOUT);
						visitResult.setMessage(ex.getMessage());
						u17Request.setRetryCount(curRetryTime);
						u17Request.setRequestGetDataEndTime(System.currentTimeMillis());
						u17Request.setException(ex.getMessage());
					} else {
						//ULog.record( TAG,"未知错误,请点击重试"+ex.getMessage()); 
						visitResult.setCode(NETVISITOR_ERROR_OTHER);
						visitResult.setMessage(ex.getMessage());
						u17Request.setRetryCount(curRetryTime);
						u17Request.setRequestGetDataEndTime(System.currentTimeMillis());
						u17Request.setException(ex.getMessage());
					}
					
				} finally {
					try {
						if (bufferedReader != null)
							bufferedReader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				curRetryTime++;
				if(curRetryTime>1){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// if(isDebug){ULog.record( TAG,
				// "current retry count:"+curRetryTime); }
			}
			long end = System.currentTimeMillis();
			ULog.record(TAG+" loadDataFromUrl", "request:"+requestUrl+",end"
					+",用时"+(end-start));
			return visitResult;
		}/**/
	
	
	/**
	 * @param ImageUri
	 * @return从网络下载并保存到lrudiskcache
	 */
	public static VisitResult loadDataFromNetByUrlConnection(String imageUri
			,Context context,U17request u17request){
		//long start=System.currentTimeMillis();
		VisitResult visitResult = new VisitResult();
		if(!ContextUtil.isNetWorking(context)){
			visitResult.setCode(NETVISITOR_ERROR_NONET);
			visitResult.setMessage("当前无网络");
			return visitResult;
		}
		//注意android8以前的版本，http请求头中的keep alive应当设置为false
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.FROYO){
			System.setProperty("http.keepAlive", "false");
		}
		HttpURLConnection urlConnection=null;
		InputStream in=null;
		Bitmap bitmap=null;
		ByteArrayOutputStream out = null;
		String result = "";
		long startRequestTime = System.currentTimeMillis();
		u17request.setRequestBeginTime(startRequestTime);
		int length = -1;
		byte[] cache = new byte[32*1024];
		try {
			urlConnection=getConnectionWithRetrys(imageUri,context,u17request);
			if(urlConnection==null){
				visitResult.setCode(NETVISITOR_ERROR_SERVER);
				visitResult.setMessage("错误的HTTP状态" + u17request.getExceptionCode());
				u17request.setRequestEndTime(System.currentTimeMillis());
				return visitResult;
			}
			in=urlConnection.getInputStream();
			out = new ByteArrayOutputStream();
			long startRequestGetDataTime = System.currentTimeMillis();
			u17request.setRequestGetDataBeginTime(startRequestGetDataTime);
			while((length = in.read(cache)) != -1){
				out.write(cache, 0, length);
			}
			out.flush();
			
			long endRequestGetDataTime = System.currentTimeMillis();
			result = out.toString("utf-8");
			visitResult.setCode(1);
			visitResult.setResult(result);
			int dataLength = result.length();
			u17request.setDataLength(dataLength);
			u17request.setRequestGetDataEndTime(endRequestGetDataTime);
		} catch (Exception ex) {
			//ULog.record(TAG, "loadDataFromUrl error:"+ex.getMessage());
			if (ex instanceof SocketTimeoutException) {
				//ULog.record( TAG, "读取数据超时,请稍后重试");
				visitResult.setCode(NETVISITOR_ERROR_TIMEOUT);
				visitResult.setMessage(ex.getMessage());
				u17request.setRequestGetDataEndTime(System.currentTimeMillis());
				u17request.setException(ex.getMessage());
				ULog.record( TAG,"未知错误,请点击重试"+ex.getMessage());
			} else if (ex instanceof ConnectTimeoutException) {
			   // ULog.record( TAG, "连接超时,请稍后重试");
				visitResult.setCode(NETVISITOR_ERROR_TIMEOUT);
				visitResult.setMessage(ex.getMessage());
				u17request.setRequestGetDataEndTime(System.currentTimeMillis());
				u17request.setException(ex.getMessage());
				ULog.record( TAG,"未知错误,请点击重试"+ex.getMessage());
			} else {
				//ULog.record( TAG,"未知错误,请点击重试"+ex.getMessage()); 
				visitResult.setCode(NETVISITOR_ERROR_OTHER);
				visitResult.setMessage(ex.getMessage());
				u17request.setRequestGetDataEndTime(System.currentTimeMillis());
				u17request.setException(ex.getMessage());
				ULog.record( TAG,"未知错误,请点击重试"+ex.getMessage());
			}
			
		} 
		finally{
			//关闭网络连接
			if(urlConnection!=null){
				urlConnection.disconnect();
				urlConnection=null;
			}
		}
		return visitResult;
	}
	
	private static HttpURLConnection getConnectionWithRetrys(String imageUri,Context context,U17request u17request) throws IOException{
		//long start=System.currentTimeMillis();
				if(!ContextUtil.isNetWorking(context)){
					return null;
				}
				//注意android8以前的版本，http请求头中的keep alive应当设置为false
				if(Build.VERSION.SDK_INT<Build.VERSION_CODES.FROYO){
					System.setProperty("http.keepAlive", "false");
				}
				HttpURLConnection urlConnection=null;
				int retryCount=0;
					final URL url=new URL(imageUri);
					urlConnection=getHttpUrlConnection(url,context);
					while(urlConnection.getResponseCode()/100==3&&retryCount<=3){
							urlConnection=getHttpUrlConnection(url,context);
							retryCount++;
					}
					if(urlConnection.getResponseCode()!=HttpURLConnection.HTTP_OK){
						u17request.setExceptionCode(NETVISITOR_ERROR_SERVER);
						u17request.setException("错误的HTTP状态" + urlConnection.getResponseCode());
							return null;
					}
					u17request.setRetryCount(retryCount);
					
				return urlConnection;	
				
	}
	
	private static HttpURLConnection getHttpUrlConnection(URL url,Context context) throws IOException{
		String proxyStr=NetUtil.getNetProxyStr(context);
		HttpURLConnection  urlConnection=null;
		if(!TextUtils.isEmpty(proxyStr)){
			urlConnection = (HttpURLConnection)url.openConnection(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(android.net.Proxy.getDefaultHost(), android.net.Proxy.getDefaultPort())));
		}else{
			urlConnection=(HttpURLConnection)url.openConnection();
		}
		urlConnection.setConnectTimeout(3000);
		urlConnection.setReadTimeout(3000);
		urlConnection.setRequestMethod("GET");
		urlConnection.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		urlConnection.setRequestProperty("Accept-Language", "zh-CN");
		urlConnection.setRequestProperty("Charset", "UTF-8");
		urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
		urlConnection.setRequestProperty("Connection", "Keep-Alive");
		return urlConnection;
	}
	
}
