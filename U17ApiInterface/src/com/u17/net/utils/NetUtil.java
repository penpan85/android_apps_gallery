package com.u17.net.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.text.TextUtils;

public class NetUtil {
	/*public static boolean isCMWAP(Context context){  
		String currentAPN = ""; 
		ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo info = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
		if(info==null){
			return false;///TODO：modified by lirui 2012-01-29 based xoom error null pointer
		}
		currentAPN = info.getExtraInfo(); 
		if(currentAPN == null || currentAPN == ""){  
			return false;  
		}else{  
			if(currentAPN.contains("cmwap") || currentAPN.equals("uniwap") || currentAPN.equals("3gwap")){  
				return true;  
			}else{  
				return false;  
			} 
		}
	 }
	*/
	public static String getNetProxyStr(Context context){
		String proxyStr="";
		boolean isWIFIActive=false;
		boolean isMobileActive=false;
		int netType=-1;
		if(context==null){
			return "";
		}
		ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager==null){
			return "";
		}
		NetworkInfo networkinfo=connectivityManager.getActiveNetworkInfo();
		if(networkinfo!=null)
		  netType=networkinfo.getType();
		if(netType==ConnectivityManager.TYPE_WIFI){
			isWIFIActive=true;
		}
		boolean isMobileNetType = (networkinfo !=null 
				&& networkinfo.getType() == ConnectivityManager.TYPE_MOBILE);
		if(!isWIFIActive && isMobileNetType){
			/*Uri uri=Uri.parse("content://telephony/carriers/preferapn");
			Cursor localCursor= null;//context.getContentResolver().query(uri, null, null, null, null);
			if(localCursor==null||!localCursor.moveToFirst()){
				return "";
			}
			proxyStr=localCursor.getString(localCursor.getColumnIndex("proxy"));*/
			String proxy=Proxy.getDefaultHost();
			String port=Proxy.getDefaultPort()==-1?"":Proxy.getDefaultPort()+"";
			if(!TextUtils.isEmpty(proxy)){
				proxyStr=proxy+":"+port;
			}
			
		}
		return proxyStr;
	}
	@Deprecated
	public static String getProxyStr(Context context){
		String proxyStr="";
		try {
			if(context==null){
				return null;
			}
			ConnectivityManager localConnectivityManager=(ConnectivityManager)context.getSystemService("connectivity");
			if(localConnectivityManager==null){
				return null;
			}
			boolean isWifiNet=false;
			NetworkInfo networkInfo=localConnectivityManager.getActiveNetworkInfo();
			if(networkInfo!=null){
				int type=networkInfo.getType();
				if(type==ConnectivityManager.TYPE_WIFI){
					isWifiNet=true;
				}
			}
			//获取代理设置信息
			if(!isWifiNet&&localConnectivityManager.getNetworkInfo(0)!=null){
				Uri localUri = Uri.parse("content://telephony/carriers/preferapn");
			    Cursor localCursor = context.getContentResolver().query(localUri, null, null, null, null);
			    if(localCursor==null||!localCursor.moveToFirst()){
			    	return null;
			    }
			    proxyStr=localCursor.getString(localCursor.getColumnIndex("proxy"));
			    if(localCursor!=null){
			    	localCursor.close();
			    }
			}
			//4.0以上会抛出错误
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return proxyStr;
	}

	public static String getHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}

		} catch (Exception e) {
			return "";
		}
		return "";
	}
}
