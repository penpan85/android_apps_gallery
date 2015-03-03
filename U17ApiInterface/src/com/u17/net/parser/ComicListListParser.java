package com.u17.net.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.u17.net.error.U17ServerFail;
import com.u17.net.model.ComicListDatas;
import com.u17.net.model.ComicListItem;
import com.u17.net.utils.DataTypeUtils;

/*
 * @Package com.u17.core.parsers 
 * @user: zhaoningqiang
 * @time: 2014-4-14,下午2:31:32
 * @Description: 漫画列表解析器
 * @version
 */
public class ComicListListParser extends BaseJsonParser<ComicListDatas>{


	@Override
	protected ComicListDatas parserData(String dataStr) throws JSONException,U17ServerFail {
		ComicListDatas datas = new ComicListDatas();
		ArrayList<ComicListItem> list = new ArrayList<ComicListItem>();
		datas.setListItems(list);
		if(TextUtils.isEmpty(dataStr)){
			return datas;
		}
		JSONObject obj = new JSONObject(dataStr);
		int code = getIntNodeValue(obj, "stateCode");
		String message =getStringNodeValue(obj, "message");
		if(code<0){
			throw new U17ServerFail(message);
		}else if(code==0){
			return datas;
		}
		Object object= obj.get("returnData");
		JSONArray jsonArr = null;
		//搜索结果解析
		if(object instanceof JSONObject){
			JSONObject jsonObject= (JSONObject) object;
			datas.setTotalNum(getIntNodeValue(jsonObject, "comicNum"));
			jsonArr = jsonObject.getJSONArray("comicList");
		}else {
			jsonArr = (JSONArray) object;
		}
		parseComicListItems(jsonArr, list);
		return datas;
	}
	
	public void parseComicListItems(JSONArray jsonArr,ArrayList<ComicListItem> list) throws JSONException,
	U17ServerFail{
		if(DataTypeUtils.isEmpty(jsonArr)){
			return;
		}
		ComicListItem item = null;
		int size = jsonArr.length();
		Calendar calendar=Calendar.getInstance();
		for(int i=0;i<size;i++){
			JSONObject object = jsonArr.getJSONObject(i);
			item = new ComicListItem();
			item.setComicId(Integer.valueOf(getStringNodeValue(object, "comic_id")));
			item.setName(getStringNodeValue(object,"name"));
			item.setCover(getStringNodeValue(object, "cover"));
			item.setAccredit(getIntNodeValue(object, "accredit"));
			item.setExclusive(getIntNodeValue(object, "is_dujia"));
			item.setNickname(getStringNodeValue(object, "nickname"));
			item.setUpdateTime(getIntNodeValue(object,"last_update_time"));
			item.setUpdateTips(getUpdateTipsValue(System.currentTimeMillis(),calendar,item));
			item.setDescription(getStringNodeValue(object, "description"));
			item.setAccreditDisplayCode(getAccreditDisplayCode(item.getExclusive(),item.getAccredit()));
			String[] tags =new String[0];
			if(object.has("tags")){
				JSONArray tagsArray = object.getJSONArray("tags");
				if(!DataTypeUtils.isEmpty(tagsArray)){
					int tagsSize = tagsArray.length();
					tags = new String[tagsSize];
					for(int j=0;j<tagsSize;j++){
						tags[j]=tagsArray.getString(j);
					}
				}
			}
			item.setSeries_status(getStringNodeValue(object, "series_status"));
			item.setClick_total(getStringNodeValue(object, "click_total"));
			item.setExtraValue(getStringNodeValue(object, "extraValue"));
			item.setLatestUpdateChapter(getStringNodeValue(object, "last_update_chapter_name"));
			item.setTags(tags);
			list.add(item);
		}
	}
	private static int getAccreditDisplayCode(int exclusive,int accredit){
		if(exclusive == 1 && accredit == 2)
		{
			return 0;
		}else if(exclusive == 1||accredit == 2)
		{
			return 1;
		}
		else{
			return 2;
		}
	}
	private static String getUpdateTipsValue(long nowTime,Calendar calendar,ComicListItem item){
		String updateMsg = "一周前更新";
		if (item.getUpdateTime() != 0) {
			long updateTime = item.getUpdateTime() * 1000;
			long difTime = nowTime - updateTime;
			if (difTime < 0) {
				difTime = 0;
			}
			Date date = new Date(item.getUpdateTime() * 1000);
			
			calendar.setTime(date);
			int day = calendar.get(Calendar.DAY_OF_YEAR);
			int year = calendar.get(Calendar.YEAR);
			date = new Date(nowTime);
			calendar.setTime(date);
			int nowDay = calendar.get(Calendar.DAY_OF_YEAR);
			int nowYear = calendar.get(Calendar.YEAR);

			if (difTime >= 8 * 20) {
				updateMsg = "一周前更新";
			} 
				else {
				if (year == nowYear) {
					if ((nowDay - day) >= 1) {
						updateMsg = "昨日更新";
					} else {
//						updateMsg = "今日更新";
						updateMsg = getUpdateToNow(updateTime,nowTime);
					}
				} else {
					if ((365 - day + nowDay) >= 1) {
						updateMsg = "昨日更新";
					} else {
//						updateMsg = "今日更新";
						updateMsg = getUpdateToNow(updateTime,nowTime);
					}
				}
			}
		}
		return updateMsg;
	}
	
	/**
	 * @description 得到现在到更新的时间（小时）
	 */
	private static String getUpdateToNow(long updateTime, long nowTime) {
		int hour = (int) (nowTime - updateTime) / 1000 / 60 / 60;
		if (hour == 0) {
			int min = (int) (nowTime - updateTime) / 1000 / 60;
			if (min == 0) {
				int sec = (int) (nowTime - updateTime) / 1000;
				if (sec == 0) {
					return "1秒前更新";
				}
				return sec + "秒前更新";
			}
			return min + "分钟前更新";
		}
		return hour + "小时前更新";
	}
}
