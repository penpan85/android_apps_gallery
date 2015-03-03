package com.u17.net.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.u17.net.error.U17ServerFail;
import com.u17.net.model.ChapterSizeResult;
/**
 * 解析选中章节的各种图片格式对应的大小
 * @author zhaoningqiang
 * @time 2014-8-8上午10:08:58
 */
public class ChapterSizeParser extends BaseJsonParser<ChapterSizeResult>{

	@Override
	protected ChapterSizeResult parserData(String jsonStr) throws JSONException, U17ServerFail {
		JSONObject data = new JSONObject(jsonStr);
		int code = getIntNodeValue(data, "stateCode");
		if(code < 1){
			throw new U17ServerFail(getStringNodeValue(data, "message"));
		}
		ChapterSizeResult chapterSize = new ChapterSizeResult();
		JSONObject returnData = data.getJSONObject("returnData");
		chapterSize.setSvol(Long.parseLong(getStringNodeValue(returnData, "svol")));
		chapterSize.setMobile(Long.parseLong(getStringNodeValue(returnData, "mobile")));
		chapterSize.setWebp_05(Long.parseLong(getStringNodeValue(returnData, "webp_05")));
		chapterSize.setWebp_50(Long.parseLong(getStringNodeValue(returnData, "webp_50")));
		return chapterSize;
	}

}
