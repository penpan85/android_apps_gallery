package com.u17.net.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.u17.net.error.U17ServerFail;
import com.u17.net.model.ChapterDetail;
import com.u17.net.model.Image;
/**
 * @Package com.u17.core.parsers 
 * @user: zhaoningqiang
 * @time: 2014年4月24日14:35:22
 * @Description: 解析章节详情
 * @version
 * TODO ChapterDetail的章节ID需要接口返回，当前没有设置
 */
public class ChapterDetailParser extends BaseJsonParser<ChapterDetail>{

	private int chapterId;
	public ChapterDetailParser(){}
	public ChapterDetailParser(int chapterId,boolean isDownloaded){
		this.chapterId=chapterId;
	}
	public void setChapterId(int id){
		this.chapterId=id;
	}

	@Override
	protected ChapterDetail parserData(String dataStr) throws JSONException, U17ServerFail {
		ChapterDetail chapter = new ChapterDetail();
		JSONObject jsonObject=new JSONObject(dataStr);
		checkDataState(jsonObject);
		if(jsonObject.has("returnData")){
			//int currentResolution = U17Config.getInstance().getDownLoadSpec();
			int currentResolution = 0;
			JSONArray items = jsonObject.getJSONArray("returnData");
			List<Image> imageList = new ArrayList<Image>();
			int size = items.length();
			for(int i=0;i<size;i++){
					JSONObject object = items.getJSONObject(i);
					Image image = new Image();
					image.setImageId(Integer.valueOf(getStringNodeValue(object, "image_id")));
					image.setHeight(Integer.valueOf(getStringNodeValue(object,"height")));
					image.setWidth(Integer.valueOf(getStringNodeValue(object,"width")));
					image.setTotalTucao(Integer.valueOf(getStringNodeValue(object,"total_tucao")));
					//1.4.0以后接口有所变动
//					String domain=getStringNodeValue(object, "domain");
					image.setFastUrl(getStringNodeValue(object, "img05").trim());
					image.setBalanceUrl(getStringNodeValue(object, "img50").trim());
					image.setMobileUrl(getStringNodeValue(object,"location").trim());
					//下载默认用 此地址
					image.setSvolUrl(getStringNodeValue(object,"svol").trim());
					image.setUrl(parseDefaultDownloadUrl(object, currentResolution));
					
					imageList.add(image);
				}
			chapter.setImages(imageList);
			chapter.setChapterId(chapterId);
		}
		
		return chapter;
	}

	private String parseDefaultDownloadUrl(JSONObject object,int resolutionCode) throws JSONException{
		/*if(resolutionCode == U17Config.DOWNLOAD_IMAGE_JPG_HIGH){
			return getStringNodeValue(object,"svol").trim();
		}else if(resolutionCode == U17Config.DOWNLOAD_IMAGE_JPG_NORMAL){
			return getStringNodeValue(object,"location").trim();
		}else if(resolutionCode ==  U17Config.DOWNLOAD_IMAGE_WEBP_HIGH){
			return getStringNodeValue(object, "img50").trim();
		}else
			return getStringNodeValue(object, "img05").trim();*/
		return getStringNodeValue(object, "img05").trim();
	}
}
