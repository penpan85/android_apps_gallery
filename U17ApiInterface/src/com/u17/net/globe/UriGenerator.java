package com.u17.net.globe;

import java.net.URLEncoder;

import com.u17.net.utils.DataTypeUtils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;
import android.text.TextUtils;


public class UriGenerator {
	private static UriGenerator config;
	private int versionCode;
	private String versionName;
	private String androidId;
	private String market;
	private Context context;
	public static String NEW_SERVER_URL = "http://app.u17.com/v3/app/android/phone/";
	public UriGenerator(Context context){
		this.context = context;
		initVersionParams(context);
		getAndroidId();
		getMarket();
	}
	
	public static UriGenerator getInstance(Context context){
		if(config == null){
			config = new UriGenerator(context);
		}
		return config;
	}
	private String format(String url, Object... args) {

		if (url.indexOf("?") > 0) {
			url = url + "&";
		} else {
			url = url + "?";
		}

		url = url + "t=%d&v=%d&android_id=%s&key=%s&come_from=%s&model=%s";
		int count = 0;
		if (args != null) {
			count = args.length;
		} else {
			count = 0;
		}
		Object[] array = new Object[count + 6];
		for (int i = 0; i < count; i++) {
			array[i] = args[i];
		}
		array[count] = (long) (System.currentTimeMillis() / 1000);
		array[count + 1] = versionCode;
		array[count + 2] = androidId;
		String loginKey = "fd";
		String kk = URLEncoder.encode(loginKey);
		String model = android.os.Build.MODEL;
		if (DataTypeUtils.isEmpty(kk)) {
			kk = null;
		}
		if(TextUtils.isEmpty(android.os.Build.MODEL)){
			model = "unknown_model";
		}
		array[count + 3] = kk;
		array[count + 4] = market;
		array[count + 5] = URLEncoder.encode(model);
		return String.format(url, array);
	}
	
	private void initVersionParams(Context context) {// 初始化版本变量
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(),PackageManager.GET_CONFIGURATIONS);
			versionCode = pinfo.versionCode;
			versionName = pinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void getAndroidId() {
		if (androidId == null) {
			androidId = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
			if (androidId == null) {
				androidId = String.valueOf("".hashCode());
			}
		}
	}
	
	public void getMarket(){
		/* ApplicationInfo info=null;
	        try {
				info=context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
				if(info!=null){
					market=info.metaData.getString("UMENG_CHANNEL");
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			} catch (NullPointerException e){
				e.printStackTrace();
			}*/
		market = "u17";
	}
	
	public String getMangaListUrl(int pageNo){
		return getMangaListUrl(10,pageNo,"sort",String.valueOf(0),null);
	}
	
	public String getMangaListUrl(int pageSize,
			int pageNo, String type, String value, Integer pageCondition){
		if (pageCondition == null) {
			return format(NEW_SERVER_URL
					+ "list/index?size=%d&page=%d&argName=%s&argValue=%s",
					pageSize, pageNo, type, value);
		} else {
			return format(
					NEW_SERVER_URL
							+ "list/index?size=%d&page=%d&argName=%s&argValue=%s&con=%d",
					pageSize, pageNo, type, value, pageCondition);
		}
	}
	
	public String getMangaDetailUrl(){
		int index = (int)(Math.random()*200);
		return format(NEW_SERVER_URL
				+ "comic/detail?comicid=%d",Integer.parseInt(Constant.comicIds[index]) );
	}
	public String getMangaImageUrl(){
		int index = (int)(Math.random()*200);
		return Constant.comicImageUrls[index];
	}
	
}
