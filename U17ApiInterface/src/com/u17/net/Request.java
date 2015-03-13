package com.u17.net;

import java.util.HashMap;

import com.u17.net.parser.BaseParser;

/**
 * @author pengpan
 * 对一个网络请求的封装 
 *
 */
public class Request<T> {
   
   public static final int TIME_OUT_MILLS_LONG = 10000;
   
   public static final int TIME_OUT_MILLS_DEFAULT = 5000;
   
   public static final int TIME_OUT_MILLS_SHORT = 1000;
   
   public static final int MAX_RETRY_COUNT = 3;
   
   public static final int MIN_RETRY_COUNT = 0;
   
   public static final int DEFAULT_RETRY_COUNT = 1;
   
   public String url;
   
   public boolean isGet = true;
   
   public int retryCount = DEFAULT_RETRY_COUNT;
   
   public long timeOutMills =;
   
   public BaseParser<T> parser;
   
   public HashMap<String,Object> postParamsMap;
  
   public Request(String url, boolean isGet, BaseParser<T> parser){
	   this.url = url;
	   this.parser =  parser;
	   this.
   }
   
   
}
