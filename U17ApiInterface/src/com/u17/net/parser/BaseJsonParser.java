package com.u17.net.parser;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.u17.net.error.U17JsonParserException;
import com.u17.net.error.U17ServerFail;
import com.u17.net.model.JsonResult;



public abstract class BaseJsonParser<T> implements BaseParser<T>{
	private static String TAG=BaseJsonParser.class.getSimpleName();
	protected abstract T parserData(String s) throws JSONException,U17ServerFail;
	
	public T parser(String jsonStr) throws JSONException,U17ServerFail{
		//ULog.record(TAG+" parser", jsonStr);
		String dataStr="";
		int code=-1;
		String rootMes ="";
		if(!TextUtils.isEmpty(jsonStr)){
			JSONObject root=new JSONObject(jsonStr);
			if(root.has("code")){
				code=getIntNodeValue(root, "code");
			}if(root.has("data")){
				dataStr=getStringNodeValue(root, "data");
			}if(root.has("msg")){
				rootMes =getStringNodeValue(root, "msg");
			}
		}if(code<=0){
			throw new U17ServerFail(rootMes);
		}if(code==1&&TextUtils.isEmpty(jsonStr)){
			throw new U17ServerFail("服务器忙");
		}
		return parserData(dataStr);
	}
	

	public T parser(byte[] bytes) throws UnsupportedEncodingException, JSONException,U17ServerFail
	,StringIndexOutOfBoundsException,U17JsonParserException{
		return parser(convert(bytes));
	}
	/**
	 * @user: pengpan
	 * @time: 2014-4-14,下午2:02:35
	 * @Title: toJsonString 
	 * @Description: TODO 判断某个string是否符合json数据格式
	 * @return String
	 * @version
	 */
	
	private boolean isJsonStart(String value) throws StringIndexOutOfBoundsException{
		if(value.substring(0, 1).equals("{")){
			return true;
		}
		if(value.substring(0, 1).equals("[")){
			return true;
		}
		return false;
	}
	
	public JsonResult checkDataState(JSONObject root)throws JSONException,U17ServerFail{
		JsonResult result = parseJsonResult(root);
		if(result.getCode()<0){
			throw new U17ServerFail(result.getMessage());
		}
		return result;
	}

	public JsonResult parseJsonResult(JSONObject root) throws JSONException{
		JsonResult result = new JsonResult();
		result.setCode(getIntNodeValue(root, "stateCode"));
		result.setMessage(getStringNodeValue(root, "message"));
		return result;
	}
	/*protected String convert(byte[] bytes) throws UnsupportedEncodingException, JSONException,StringIndexOutOfBoundsException{
			String data=new String(bytes,"utf-8");
			data=data.trim();
			while(!isJsonStart(data)){
				data = data.substring(1);
			}
			if(!data.substring(0, 1).equals("[")){
				data="["+data+"]";
			}
		return data;
	}*/
	protected String convert(byte[] bytes) throws UnsupportedEncodingException, JSONException,StringIndexOutOfBoundsException
	,U17JsonParserException{
		String data=new String(bytes,"utf-8");
		data=data.trim();
		while(!isJsonStart(data)){
			data = data.substring(1);
		}
		if(!data.substring(0, 1).equals("{")){
			throw new U17JsonParserException("数据格式异常");
		}
		return data;
	}
	protected String toJsonString(T t){ return "";}
	/**
	 * @user: pengpan
	 * @time: 2014-4-14,下午2:14:03
	 * @Title: getIntNodeValue 
	 * @Description: TODO得到某个json结点对应的int类型的值
	 * @param jsonobject
	 * @param jsonKey
	 * @return int
	 * @version
	 * @throws JSONException 
	 */
	protected int getIntNodeValue(JSONObject jsonobject, String jsonKey) throws JSONException{
		boolean isHas = jsonobject.has(jsonKey) && (!jsonobject.isNull(jsonKey));
		return isHas ? jsonobject.getInt(jsonKey) : -1;
		
	}
	
	protected long getLongNodeValue(JSONObject jsonobject, String jsonKey) throws JSONException {
		boolean isHas = jsonobject.has(jsonKey) && (!jsonobject.isNull(jsonKey));
		return isHas ? jsonobject.getLong(jsonKey) : -1;
	}
	
	protected Double getDoubleNodeValue(JSONObject jsonobject, String jsonKey) throws JSONException {
		boolean isHas = jsonobject.has(jsonKey) && (!jsonobject.isNull(jsonKey));
		return isHas ? jsonobject.getDouble(jsonKey) : -1;
	}
	/**
	 * @user: pengpan
	 * @time: 2014-4-14,下午2:14:51
	 * @Title: getStringNodeValue 
	 * @Description: TODO得到某个json结点对应的string类型的值
	 * @param jsonobject
	 * @param jsonkey
	 * @return String
	 * @version
	 * @throws JSONException 
	 */
	protected String getStringNodeValue(JSONObject jsonobject,String jsonkey) throws JSONException{
		boolean isHas = jsonobject.has(jsonkey) && (!jsonobject.isNull(jsonkey));
	    return isHas ? jsonobject.getString(jsonkey) : "";
	
	}

}
