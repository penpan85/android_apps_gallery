package com.u17.net.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.u17.net.globe.Constant;


public class ContextUtil {
	private static String Tag=ContextUtil.class.getSimpleName();
	 public static final String SD_CARD = "sdCard";
	 private static boolean isDebug=true;
	 public static final String EXTERNAL_SD_CARD = "externalSdCard";

	public static boolean isInternalSDCardExists(){
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	

	public static int getDensityDpi(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics(); 
		return dm.densityDpi; 
	}
	
	public static String getU17MetaData(Context context)
	{
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			String cid=appInfo.metaData.getString("UMENG_CHANNEL");
			return cid;
		} catch (NameNotFoundException e) {
			return null;
		}
	}
	

	public static String getDataBasePath()
	{
		String path = getSDPath()+"/u17phone/data/";
		return path;
	}
	
	public static boolean isDataBasePath(String path)
	{
		if(path == null || "".equals(path) || "null".contains(path))
		{
			return true;
		}
		return false;
	}

	public static boolean IsHasKey(JSONObject jsObject, String name) {
		return jsObject.has(name) && (!jsObject.isNull(name));
	}
	/**
	 * 网络是否连通
	 * @param context
	 * @return
	 */
	public static boolean isNetWorking(Context context) {
		if(context==null){
			return false;
		}
		boolean result;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm==null){
			return false;
		}
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	
	public static boolean getWifiConnectionStat(Context context)
	{
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
	}
	
	public static LayoutInflater getLayoutInflater(Context context){
		return (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
//	public static int getSDCardSpace() {
//		String state = Environment.getExternalStorageState();
//		if (Environment.MEDIA_MOUNTED.equals(state)) {
//			File sdcardDir = Environment.getExternalStorageDirectory();
//			StatFs sf = new StatFs(sdcardDir.getPath());
//			long blockSize = sf.getBlockSize();
//			long availCount = sf.getAvailableBlocks();
//			return (int) (availCount * blockSize);
//		}
//		return 0;
//	}
	
	
	
	/**
	 * 
	 * @return 返回以Byte为单位
	 */
	public static long getLocalSdcardSpace() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            long availableSpare = blocks * blockSize;
          //  ULog.e("剩余空间", "availableSpare = " + availableSpare);
//            if (availableSpare > sizeMb) {
//                ishasSpace = true;
//            }
            return availableSpare;
        }
        return 0;
    }
	

	
	/*public static boolean isSdcardNospace(int stateCode){
		//if(isDebug){ULog.record(Tag+" isSdcardNospace", "stateCode:"+stateCode);}
		if(stateCode==0){
			return !getInternalSDCardEnoughspace();
		}else{
			return !getOutsideSdcardEnoughSpace();
		}
	}*/
	/*public static boolean getOutsideSdcardEnoughSpace(){
		if(getOutSideSdcardSpace()<10){
			return false;
		}
		return true;
	}*/
	public static boolean getInternalSDCardEnoughspace()
	{
		if(getLocalSdcardSpace() < 10)
			return false;
		return true;
	}
	/**
	 * SD卡根目录
	 * @return
	 */
	public static String getSDPath(){
		if(!isInternalSDCardExists()){
			return "";
		}
		return  Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	public static int dip2px(Context context,float dip){
		int dpi = ContextUtil.getDensityDpi(context);
		return (int)((dip/160.0f)*dpi);
	}
	
	public static float dip2PxExactly(Context context,float dip){
		int dpi = ContextUtil.getDensityDpi(context);
		return (dip/160.0f)*dpi;
	}
	
	public static int getDipValue(Context context,int dp){
		final Resources resources=context.getResources();
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, resources.getDisplayMetrics());
	}
	public static String getStringFromAttrs(Context context,TypedArray typeArray,int attr){
		int id = typeArray.getResourceId(attr, 0);
		String text = null;
		if(id==0){
			text = typeArray.getString(attr);
		}else{
			text = context.getResources().getString(id);
		}
		return text;
	}
	public static int getViewHeight(View view){
		 view.measure(0, 0);  //计算子项View 的宽高
       return  view.getMeasuredHeight();  //统计所有子项的总高度
	}
	public static int getViewWidth(View view){
		view.measure(0, 0);
		return view.getWidth();
	}
	public static int getTextViewHeight(Context context,int width,String text,float dipSize){
		TextView view = new TextView(context);
		view.setWidth(width);
		view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,dipSize);
		view.setText(text);
		return getViewHeight(view);
	}
	public static float getTextViewHeight(String text,TextView view){
		CharSequence oldText = view.getText();
		view.setText(text);
		float height = getViewHeight(view);
		view.setText(oldText);
		return height;
	 }

	public static boolean closeInputWindow(Activity activity){
		InputMethodManager inputMethodManager = (InputMethodManager)activity.               
				getSystemService(Context.INPUT_METHOD_SERVICE);
		if(activity.getCurrentFocus()!=null){
			return inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		return false;
	}
	public static String getNetWorkTypeSimpleName(Context context){
		ConnectivityManager connectManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		if(networkInfo == null){
			return "UNKOWN";
		}
		int connectType = networkInfo.getType();
		if(connectType==ConnectivityManager.TYPE_WIFI){
			return "WIFI";
		}else{
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			int mobileNetType = telephonyManager.getNetworkType();
			String mobileNetTypeName;
			switch(mobileNetType){
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				mobileNetTypeName="1X";
				break;
			case TelephonyManager.NETWORK_TYPE_CDMA:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				mobileNetTypeName = "EDGE";
				break;
			case TelephonyManager.NETWORK_TYPE_EHRPD://
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_B://
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				mobileNetTypeName = "GPRS";
				break;
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_HSPA:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_HSPAP://
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_IDEN://
				mobileNetTypeName = "GPRS";
				break;
			case TelephonyManager.NETWORK_TYPE_LTE://
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				mobileNetTypeName = "UNKOWN";
				break;
			default:
				mobileNetTypeName = "UNKOWN";
				break;	
			}
			return mobileNetTypeName;
		}
	}

	public static String GetHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements();) 
				{
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) 
					{
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
		} catch (Exception e) {
		}
		return "";
	}
	
	 /**
     * @return A map of all storage locations available
     */
    public static Map<String, File> getAllStorageLocations() {
        Map<String, File> map = new HashMap<String, File>(10);

        List<String> mMounts = new ArrayList<String>(10);
        List<String> mVold = new ArrayList<String>(10);
        mMounts.add("/mnt/sdcard");
        mVold.add("/mnt/sdcard");

        try {
            File mountFile = new File("/proc/mounts");
            if(mountFile.exists()){
                Scanner scanner = new Scanner(mountFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("/dev/block/vold/")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[1];

                        // don't add the default mount path
                        // it's already in the list.
                        if (!element.equals("/mnt/sdcard"))
                            mMounts.add(element);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File voldFile = new File("/system/etc/vold.fstab");
            if(voldFile.exists()){
                Scanner scanner = new Scanner(voldFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("dev_mount")) {
                        String[] lineElements = line.split(" ");
                        String element = lineElements[2];

                        if (element.contains(":"))
                            element = element.substring(0, element.indexOf(":"));
                        if (!element.equals("/mnt/sdcard"))
                            mVold.add(element);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < mMounts.size(); i++) {
            String mount = mMounts.get(i);
            if (!mVold.contains(mount))
                mMounts.remove(i--);
        }
        mVold.clear();

        List<String> mountHash = new ArrayList<String>(10);

        for(String mount : mMounts){
            File root = new File(mount);
            if (root.exists() && root.isDirectory() && root.canWrite()) {
                File[] list = root.listFiles();
                String hash = "[";
                if(list!=null){
                    for(File f : list){
                        hash += f.getName().hashCode()+":"+f.length()+", ";
                    }
                }
                hash += "]";
                if(!mountHash.contains(hash)){
                    String key = SD_CARD + "_" + map.size();
                    if (map.size() == 0) {
                        key = SD_CARD;
                    } else if (map.size() == 1) {
                        key = EXTERNAL_SD_CARD;
                    }
                    mountHash.add(hash);
                    map.put(key, root);
                }
            }
        }

        mMounts.clear();

        if(map.isEmpty()){
                 map.put(SD_CARD, Environment.getExternalStorageDirectory());
        }
        return map;
    }
    @Deprecated
    public static String getOutSideSdcardRootDir3(){
    	HashMap<String, File> map=(HashMap<String, File>) getAllStorageLocations();
    	if(map.containsKey(EXTERNAL_SD_CARD)){
    		return map.get(EXTERNAL_SD_CARD).getAbsolutePath();
    	}
    	return "";
    }
    
    /**
     * @user: 1
     * @time: 2014-2-27,上午11:30:49
     * @Title: getStorageDirs 
     * @Description: TODO
     * @return String[]，所有的存储器路径
     * @version
     * 读取系统目录 /proc/mounts文件信息
     * 一行一行读取，获取每行文本信息
     * 如果某行文本包含了"vfat"或者"/mnt",则可能包含了一个存在的存储器路径，要做进一步判断
     * 将上一步得到的路径做分割，得到各个字符串，进一步判断这些字符串是否是代表了可能存在的存储器路径
     * 不包含/dev/block/vold 包含/mnt/secure  /mnt/asec /mnt/obb  /dev/mapper tmpfs，这5种情况
     */
    public static String[] getOutSideDirs(){
    	String[] storagePaths=null;
    	BufferedReader br=null;
    	ArrayList<String> list=new ArrayList<String>();
    	String line=null;
    	try{
    		br=new BufferedReader(new FileReader("/proc/mounts"));
    		while((line=br.readLine())!=null){
    			if(line.contains("vfat")||line.contains("/mnt")){
    				StringTokenizer stringTokenizer=new StringTokenizer(line," ");
    				stringTokenizer.nextToken();
    				String str=stringTokenizer.nextToken();
    				if(str.equals(Environment.getExternalStorageDirectory().getPath())){
    					continue;
    				}else if(line.contains("/dev/block/vold")
        					&&!line.contains("/mnt/secure")
        					&&!line.contains("/mnt/asec")
        					&&!line.contains("/mnt/obbb")
        					&&!line.contains("/dev/mapper")
        					&&!line.contains("tmpfs")){
        				list.add(str);
        			}else{
        				continue;
        			}
    			}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		try{
    			br.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    	if(list!=null&&list.size()>0){
    		storagePaths=new String[list.size()];
    		list.toArray(storagePaths);
    	}
    	return storagePaths;
    }
	
  
    
    protected static HashSet<File> getExtStorageDirByProbabilisticName() {
		String[] sfiles = { "/sdcard-ext","sdcard","/sdcard0", "/sdcard1", "/sdcard2", "/external_sd", "/flash",
				"/internal","/mnt/sdcard2","/mnt/sdcard-ext","/mnt/ext_sdcard","/mnt/sdcard/SD_CARD"
				,"/mnt/sdcard/extra_sd","/mnt/extrasd_bind","/mnt/sdcard/ext_sd"
				,"/mnt/sdcard/external_SD","/storage/sdcard1","/storage/extSdCard"};
		HashSet<File> sets = new HashSet<File>();
		for (String sfile : sfiles) {
			final File file = new File(sfile);
			if ((file != null) && file.isDirectory() && file.exists() && file.canWrite()) {
				sets.add(file);
			}
		}
		return sets;
	}

	/*@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Deprecated
	public static String getOutSideSdCardRootDir(Context context) {
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			return "";
		}
		StorageManager storageManager = (StorageManager) context
				.getSystemService(Context.STORAGE_SERVICE);
		try {
			Class<?>[] paramClasses = {};
			Method getVolumePathsMethod = StorageManager.class.getMethod(
					"getVolumePaths", paramClasses);
			getVolumePathsMethod.setAccessible(true);
			String internalSdcardPath=getSDPath();
			Object[] params = {};
			Object invoke = getVolumePathsMethod.invoke(storageManager, params);
			for (int i = 0; i < ((String[]) invoke).length&&i<2; i++) {
				String path=((String[]) invoke)[i];
				String sdcardStr="";
				if(!TextUtils.isEmpty(path)&&(!TextUtils.isEmpty(internalSdcardPath))){
					sdcardStr=path.substring(path.lastIndexOf("/")+1);
					if(internalSdcardPath.contains(sdcardStr)){
						continue;
					}else{
						return path;
					}
				}
				
				ULog.record(Tag+" getOutSideSdCardRootDir2", ((String[]) invoke)[i]);
			}
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();

		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return "";
	}*/
	public static String getOutSideSdcardRootDir1() {
		String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        BufferedInputStream in =null;
        BufferedReader inBr=null;
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            in= new BufferedInputStream(p.getInputStream());
            inBr= new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
            
                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        //ULog.record(Tag+" getOutSideSdcardRootDir()", " out sdcard root dir:"+result);
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                    //ULog.record("CommonUtil:getSDCardPath", "命令执行失败!");
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			//ULog.record(Tag+" getOutSideSdcardRootDir() exception", e.getMessage());
		} finally{
			if(inBr!=null){
				try {
					inBr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				inBr=null;
			}
		}
		
		return "";

	}

	

	
	public static String getAvailableMem(Context context){
		  ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		  MemoryInfo mi=new MemoryInfo();
		  am.getMemoryInfo(mi);
		  return Formatter.formatFileSize(context, mi.availMem);
	}
	
	public static int getMaxMemory(Context context){
		ActivityManager am=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getMemoryClass();
	}
	
	public static Bitmap getBitmapFromRes(int resId,Context mContext){
		return BitmapFactory.decodeResource(mContext.getResources(), resId);
	}
	
	public final static <T> T[] copy(T[] source) {
		   Class type = source.getClass().getComponentType();
		   T[] target = (T[])Array.newInstance(type, source.length);
		   System.arraycopy(source, 0, target, 0, source.length);
		   return target;
		}


	
	public static long getSpecialFileSize(File dir,Context context){
		long totalSize = 0;
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			for(File temp:files){
				if(temp.isDirectory()){
					totalSize+=getSpecialFileSize(dir, context);
				}else{
					totalSize+=getFileSize(temp);
				}
			}
		}
		return totalSize;
	}
	
	private static long getFileSize(File file){
		if(file == null || !file.exists()
				||file.isDirectory()
				){
			return 0;
		}
		return file.length();
	}
	/**
	 * @param context
	 * @return 手机内部存储的剩余空间
	 */
	public static String getInternalSDAvailableSize(Context context){
		long available = 0;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			
			available = blockSize*availableBlocks;
		}
		
		return Formatter.formatFileSize(context, available);
	}

	

	
	public static int getStatusBarHeight(Context context){
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		 int x = 0, sbar = 0;
		 try {
		       c = Class.forName("com.android.internal.R$dimen");
		       obj = c.newInstance();
		       field = c.getField("status_bar_height");
		       x = Integer.parseInt(field.get(obj).toString());
		       sbar = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
		      // ULog.record(Tag+" getStatusBarHeight","get status bar height fail");
		       e1.printStackTrace();
		       sbar = 0;
		} 
		return sbar;

	}
//	public static CharSequence getUpdateState(String SeriesStatus){
//		CharSequence result = "";
//		try {
//			int ss = Integer.valueOf(SeriesStatus);
//			switch (ss) {
//			case 0:
////				result = "连载";
//				result = "";
//				break;
//
//			case 1:
//				result = setTextSizeColor(" (完)");
//				break;
//			case 2:
////				result = "暂停更新";
//				result = "";
//				break;
//			}
//		} catch (Exception e) {
//			ULog.d("contextUtil getUpdateState",e.getMessage());
//		}
//		return result;
//	}

	
	
	public static CharSequence getUpdateState(String SeriesStatus) {
		CharSequence result = null;
		if(result == null)
			result = setTextSizeColor(" (完)");
		try {
			int ss = Integer.valueOf(SeriesStatus);
			if (ss == 1) {
				return result;
			}else{
				return "";
			}
		} catch (Exception e) {
			//ULog.d("contextUtil getUpdateState", e.getMessage());
		}
		return "";
	}
	
	private static SpannableString setTextSizeColor(String str){
		SpannableString result = new SpannableString(str);
		result.setSpan(new AbsoluteSizeSpan(16, true), 0, result.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		result.setSpan(new ForegroundColorSpan(Color.rgb(169, 3, 3)), 0, result.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return result;
	}
	
	
	public static String getListUpdateState(String SeriesStatus){
		String result = "";
		try {
			int ss = Integer.valueOf(SeriesStatus);
			if(ss==1) {
				result = "(完)";
			}
		} catch (Exception e) {
			//ULog.d("contextUtil getUpdateState",e.getMessage());
		}
		return result;
	}
	/**
	 * 通过三方浏览器打开网址
	 * @param context
	 * @param url
	 */
	public static void openWebUrl(Context context,String url){
		Intent intent = new Intent(); 
		intent.setAction(Intent.ACTION_VIEW);    
		Uri content_url = Uri.parse(url);   
		intent.setData(content_url);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	/**获取手机CPU信息分为3类、ARM、ARMv7、x86*/
	public static String getCpuProcessor() {  
		String result = "";
		 InputStream is =null;  
	     InputStreamReader ir = null;
	     BufferedReader br = null;
	    try {  
	        is = new FileInputStream("/proc/cpuinfo");  
	        ir = new InputStreamReader(is);  
	        br = new BufferedReader(ir);  
		    String nameProcessor = "Processor";
				while (true) {
					String line = br.readLine();
					String[] pair = null;
					if (line == null) {
						break;
					}
					pair = line.split(":");
					if (pair.length != 2)
						continue;
					String key = pair[0].trim();
					String val = pair[1].trim();
					if (key.compareTo(nameProcessor) == 0 && val.length() > 2) {
						if(val.contains("ARM"))
							result = "ARM";
						if(val.contains("ARMv7"))
							result = "ARMv7";
						if(val.contains("x86"))
							result = "x86";
						break;
					}
				}
			
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally{
	    	if(br!=null){
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	}if(ir!=null){
					try {
						ir.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	}if(is!=null){
	    		try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
					
	    }
	    return result;  
	}  

	public static boolean isStringEquals(String a,String b){
		if(TextUtils.isEmpty(a) || TextUtils.isEmpty(b)){
			return false;
		}else{
			return a.trim().equalsIgnoreCase(b.trim());
		}
	}
	
	public static void sendBroadCast(LocalBroadcastManager localBroadcastManager, Context context
			,String action){
		Intent intent = new Intent(action);
		localBroadcastManager.sendBroadcast(intent);
	}
	
	public static void sendBroadCast(LocalBroadcastManager localBroadcastManager, Context context
			,String action,Bundle bundle){
		if(bundle == null){
			return;
		}
		Intent intent = new Intent(action);
		intent.putExtra(Constant.BUNDLE_KEY_COUNT,bundle);
		localBroadcastManager.sendBroadcast(intent);
	}
	
	public static void registerLoaderReceiver(
			LocalBroadcastManager localBroadcastManager, Context context,
			BroadcastReceiver receiver) {

		try {
			localBroadcastManager.registerReceiver(
					receiver,
					new IntentFilter(Constant.ACTION_SERVICE_EXCEPTION));
			localBroadcastManager.registerReceiver(
					receiver,
					new IntentFilter(Constant.ACTION_SERVICE_START));
			localBroadcastManager.registerReceiver(
					receiver,
					new IntentFilter(Constant.ACTION_SERVICE_STOP));
			localBroadcastManager.registerReceiver(
					receiver,
					new IntentFilter(Constant.ACTION_SERVICE_NEWSTART));
			localBroadcastManager.registerReceiver(
					receiver,
					new IntentFilter(Constant.ACTION_SERVICE_TIMEOUT));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void unRegisterLoaderReceiver(
			LocalBroadcastManager localBroadcastManager, Context context,
			BroadcastReceiver receiver) {
		if (receiver != null) {
			localBroadcastManager.unregisterReceiver(receiver);
		}
	}
}
