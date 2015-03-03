package com.u17.net.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import android.os.Environment;
import android.util.Log;

public class ULog {


	/**
	 * 开发阶段
	 */
	public static final int DEVELOP = 0;
	/**
	 * 内部测试阶段
	 */
	public static final int DEBUG = 1;
	/**
	 * 公开测试
	 */
	public static final int BATE = 2;
	/**
	 * 正式版
	 */
	private static final int RELEASE = 3;
	public static int level = 0;
	
	public static final String TAG_NAME_TIMETEST = "TimeTest";
	public static final String TAG_NAME_SCERROR = "SCERROR";
	public static final String TAG_NAME_PARSER = "PARSER";
	public static final String TAG_NAME_REQUEST = "REQUEST";
	public static final String TAG_NAME_DEFAULT = "DEFAULT";
	private static boolean isRecord=true;
	/**
	 * 当前阶段标示
	 */
	public static int currentStage = DEBUG;
	private static String path;
	private static File file;
	private static FileOutputStream outputStream;
	private static String pattern = "yyyy-MM-dd HH-mm-ss";
	private static Date date;
	static {
	
		if (currentStage==DEBUG&&isRecord) {
			if (ContextUtil.isInternalSDCardExists()) {
				File externalStorageDirectory = Environment.getExternalStorageDirectory();
				path = externalStorageDirectory.getAbsolutePath() + "/u17phoneTest/log";
				File directory = new File(path);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				Date date=new Date();
				//String datestr=AppUtil.formatLogDate(date);
				String dateStr="u17-v2-test";
				file = new File(new File(path), "log"+dateStr+".txt");
				try {
					if(!file.exists())
						file.createNewFile();
					outputStream = new FileOutputStream(file, true);
				} catch (Exception e) {
					Log.i("logutil:static", "log.txt未找到"+e);
				}
			}
			else {
				Log.i("logutil:static", "未安装sd卡");
			}
		}
	}
	
	/**
	 * @param location  log的tag，一般为"类名："+"方法名"
	 * @param msg  要记录的信息内容
	 */
	public static void record(String logtag, String msg) {
		
		
		switch (currentStage) {
			case DEVELOP:
				// 控制台输出
				Log.i(logtag, msg);
				break;	
			case BATE:
			case DEBUG:	
				//写日志到sdcard
				Log.i(logtag, msg);
				if(!isRecord){
					return;
				};
				date = new Date();
			//	String time = AppUtil.format(date, pattern);
				String time="";
				if (ContextUtil.isInternalSDCardExists()) {
					if (outputStream != null) {
						try {
							outputStream.write(time.getBytes());						
							outputStream.write(("    " + logtag + "\r\n").getBytes());
							outputStream.write(msg.getBytes());
							outputStream.write("\r\n".getBytes());
							outputStream.flush();
						} catch (IOException e) {
							Log.i("Logutil", "sd卡写入log失败");
						}
					} else {
						Log.i("SDCAEDTAG", "file is null");
					}
				}
				break;
			case RELEASE:
				//一般不做日志记录
				break;
		}
	}
	/**
	 * @param logtag log的tag信息
	 * @param e 捕获得到的异常
	 * 把程序中捕获到的异常输出到控制台
	 */
	public static void record(String logtag, Exception e)
	{
			if(!isRecord)
			{
				return;
			}
			StackTraceElement[] arrayofElements=e.getStackTrace();
			StringBuffer buffer=new StringBuffer();
			Date date=new Date();
		//	String time=AppUtil.format(date, pattern);
			String time="";
			buffer.append("异常类型为:"+e.getClass().getName()+"\r\n");
			for (int i = 0; i < arrayofElements.length; i++) {
				String exclass =arrayofElements[i].getClassName();
				String method = arrayofElements[i].getMethodName();
				int linenumber=arrayofElements[i].getLineNumber();
				buffer.append("类:"+exclass+"调用"+method+"时在第"+linenumber+"行发生异常\r\n");
			}
			buffer.append(",异常发生时间："+time);
			buffer.append("\r\n");
			record(logtag, buffer.toString());
	}
	public static int d(String msg){
		return d(TAG_NAME_DEFAULT,msg);
	}
	public static int i(String msg){
		return i(TAG_NAME_DEFAULT,msg);
	}
	public static int e(String msg){
		return e(TAG_NAME_DEFAULT,msg);
	}
	public static int e(String msg,Throwable tr){
		return e(TAG_NAME_DEFAULT,msg,tr);
	}
	public static int error_d(String msg){
		return d(TAG_NAME_SCERROR,msg);
	}
	public static int error_i(String msg){
		return i(TAG_NAME_SCERROR,msg);
	}
	public static int error_e(String msg){
		return e(TAG_NAME_SCERROR,msg);
	}
	public static int error_e(String msg,Throwable tr){
		return e(TAG_NAME_SCERROR,msg,tr);
	}
	public static int parser_d(String msg){
		return d(TAG_NAME_PARSER,msg);
	}
	public static int parser_i(String msg){
		return i(TAG_NAME_PARSER,msg);
	}
	public static int parser_e(String msg){
		return e(TAG_NAME_PARSER,msg);
	}
	public static int parser_e(String msg,Throwable tr){
		return e(TAG_NAME_PARSER,msg,tr);
	}
	public static int request_d(String msg){
		return d(TAG_NAME_REQUEST,msg);
	}
	public static int request_i(String msg){
		return i(TAG_NAME_REQUEST,msg);
	}
	public static int request_e(String msg){
		return e(TAG_NAME_REQUEST,msg);
	}
	public static int request_e(String msg,Throwable tr){
		return e(TAG_NAME_REQUEST,msg,tr);
	}
	public static int time_d(String msg){
		return d(TAG_NAME_TIMETEST,msg);
	}
	public static int time_i(String msg){
		return i(TAG_NAME_TIMETEST,msg);
	}
	public static int time_e(String msg){
		return e(TAG_NAME_TIMETEST,msg);
	}
	public static int time_e(String msg,Throwable tr){
		return e(TAG_NAME_TIMETEST,msg,tr);
	}

	public static int v(String tag, String msg) {
		if(level<=Log.VERBOSE){
			return Log.d(tag, msg);
		}else{
			return 0;
		}
    }
	public static int d(String tag, String msg) {
		if(level<=Log.DEBUG){
			return Log.d(tag, msg);
		}else{
			return 0;
		}
    }
	public static int i(String tag, String msg) {
		if(level<=Log.INFO){
			return Log.i(tag, msg);
		}else{
			return 0;
		}
    }
	public static int w(String tag, String msg) {
		if(level<=Log.WARN){
			return Log.w(tag, msg);
		}else{
			return 0;
		}
    }
	public static int e(String tag, String msg) {
		if(level<=Log.ERROR){
			return Log.e(tag, msg);
		}else{
			return 0;
		}
    }
	
	public static int e(String tag, String msg, Throwable tr) {
		if(level<=Log.ERROR){
			return Log.e(tag, msg, tr);
		}else{
			return 0;
		}
    }

}
