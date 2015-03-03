package com.u17.net.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.u17.net.error.U17ServerFail;
import com.u17.net.model.Chapter;
import com.u17.net.model.ComicDetail;
import com.u17.net.model.ComicListItem;
import com.u17.net.model.LastRead;
import com.u17.net.utils.DataTypeUtils;
/**
 * @Package com.u17.core.parsers 
 * @user: pengpan
 * @time: 2014-4-14,下午2:31:32
 * @Description: TODO 解析漫画详情
 * @version 还没动
 */
public class ComicDetailParser extends BaseJsonParser<ComicDetail>{

	private int comicId;
	public ComicDetailParser(int comicId){
		this.comicId=comicId;
	}
	@Override
	public ComicDetail parserData(String dataJson) throws JSONException,U17ServerFail{
		ComicDetail comicDetail = new ComicDetail();
		List<Chapter> list = new ArrayList<Chapter>();
		// 基本信息
		JSONObject root = new JSONObject(dataJson);
		checkDataState(root);
		JSONObject o = root.optJSONObject("returnData");
		if(o==null){
			return new ComicDetail();
		}
		JSONObject comic = null;
		comic = o.getJSONObject("comic");
		comicDetail.setComicId(comicId);
		comicDetail.setAuthorName(getStringNodeValue(comic, "author_name"));
		comicDetail.setName(getStringNodeValue(comic, "name"));
		comicDetail.setCover(getStringNodeValue(comic, "cover"));
		comicDetail.setBigCover(getStringNodeValue(comic, "ori"));
		comicDetail.setRequestTime(getLongNodeValue(comic, "server_time"));
		comicDetail.setTheme(getStringNodeValue(comic, "theme_ids"));
		comicDetail.setReadOrder(getStringNodeValue(comic, "read_order"));
		comicDetail.setSeriesStatus(getStringNodeValue(comic, "series_status").trim());
		comicDetail.setLastUpdateTime(getLongNodeValue(comic,
				"last_update_time"));
		comicDetail.setDescription(getStringNodeValue(comic, "description"));
		comicDetail.setTotalClick(getStringNodeValue(comic, "click_total"));
		comicDetail.setTotalTucao(getStringNodeValue(comic, "total_tucao"));
		comicDetail.setFirstLetter(getStringNodeValue(comic, "first_letter"));
		comicDetail.setTotalTicket(getStringNodeValue(comic, "total_ticket"));
		comicDetail.setMonthTicket(getIntNodeValue(comic, "month_ticket"));
		comicDetail.setAllImage(getIntNodeValue(comic, "image_all"));
		comicDetail.setTotalComment(getStringNodeValue(comic, "comment_total"));
		comicDetail.setCateId(getStringNodeValue(comic, "cate_id"));
		comicDetail.setGroupIds(getStringNodeValue(comic, "group_ids"));
		comicDetail.setLastUpdateChapterId(getIntNodeValue(comic,
				"last_update_chapter_id"));
		comicDetail.setLastUpdateChapterName(getStringNodeValue(comic,
				"last_update_chapter_name"));
		if(comic.has("thread")){
			JSONObject thread = comic.getJSONObject("thread");
			comicDetail.setThreadId(getStringNodeValue(thread, "thread_id"));
		}
		comicDetail.setIsCanRecord(getIntNodeValue(comic, "is_dub"));
		comicDetail.setUser_id(getStringNodeValue(comic, "user_id"));
		comicDetail.setIs_vip(getIntNodeValue(comic,
				"is_vip"));
		comicDetail.setTotal_hot(getIntNodeValue(comic, "total_hot"));
		// 章节列表
		JSONArray items=null;
		
		if(o.has("chapter_list"))
			items= o.getJSONArray("chapter_list");
		if(items!=null){
			long time = System.currentTimeMillis();
			int size = items.length();
			for (int i = 0; i < size; i++) {
				Chapter item = new Chapter();
				JSONObject chapterJson = items.getJSONObject(i);
				item.setImage05Size(getIntNodeValue(chapterJson, "webp_05"));
				item.setImage50Size(getIntNodeValue(chapterJson, "webp_50"));
				item.setImageMobileSize(getIntNodeValue(chapterJson, "mobile"));
				item.setImageSvolSize(getIntNodeValue(chapterJson, "svol"));
				item.setName(getStringNodeValue(chapterJson, "name"));
				item.setImageTotal(getIntNodeValue(chapterJson, "image_total"));
				item.setChapterId(getIntNodeValue(chapterJson, "chapter_id"));
				item.setSize(getIntNodeValue(chapterJson, "size"));
				item.setBuyed(getIntNodeValue(chapterJson, "buyed"));
				item.setIs_view(getIntNodeValue(chapterJson, "is_view"));
				item.setTime(time);
				item.setPrice(getDoubleNodeValue(chapterJson, "price"));
				item.setType(getIntNodeValue(chapterJson, "type"));
				item.setPass_time(getLongNodeValue(chapterJson, "pass_time"));
				item.setRelease_time(getLongNodeValue(chapterJson, "release_time"));
				item.setIs_new(getIntNodeValue(chapterJson, "is_new"));
				item.setRead_state(getIntNodeValue(chapterJson, "read_state"));
				list.add(item);
			}
		}
		
		comicDetail.setChapterList(list);
		
		JSONObject lastReadJson = o.optJSONObject("last_read");
		if(lastReadJson != null){
			LastRead lastRead = new LastRead();
			lastRead.setChapterId(getIntNodeValue(lastReadJson, "chapter_id"));
			lastRead.setPage(getIntNodeValue(lastReadJson, "page"));
			lastRead.setUpdateTime(getLongNodeValue(lastReadJson, "update_time"));
			comicDetail.setLastRead(lastRead);
		}
		JSONArray authorOthersArray = o.optJSONArray("otherWorks");
		comicDetail.setAuthorOtherWorks(parseRecommendItems(authorOthersArray));
		JSONArray editorRecommendArray = o.optJSONArray("recommend");
		comicDetail.setEditorRecommended(parseRecommendItems(editorRecommendArray));
		return comicDetail;
	}
	
	private ArrayList<ComicListItem> parseRecommendItems(JSONArray array) throws JSONException{
		ArrayList<ComicListItem> items = new ArrayList<ComicListItem>();
		if(!DataTypeUtils.isEmpty(array)){
			int size = array.length();
			JSONObject obj=null;
			ComicListItem item =null;
			for(int i=0;i<size;i++){
				obj = array.optJSONObject(i);
				if(obj ==null){
					continue;
				}
				item = new ComicListItem();
				item.setComicId(getIntNodeValue(obj, "comicId"));
				item.setCover(getStringNodeValue(obj, "coverUrl"));
				item.setName(getStringNodeValue(obj, "name"));
				items.add(item);
			}
		}
		return items;
	}

}
