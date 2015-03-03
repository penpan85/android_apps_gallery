package com.u17.net.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;


public class ComicDetail implements Parcelable{
	private Integer comicId;
	private String name;
	private String authorName;
	private String cover;
	private String bigCover;
	private String theme;
	private String readOrder;
	private String seriesStatus;//连载状态，0表示连载中，1表示已完成,2表示暂停更新
	private Long requestTime=0l;
	private Long lastUpdateTime=0l;
	private String description;
	private String totalClick;
	private String totalTucao;
	private String firstLetter;
	private List<Chapter> chapterList;
	private String totalTicket;  //总月票
	private int monthTicket;  //当月月票
	private int allImage;  //总图片数
	
	private String cateId ;
	private String groupIds;
	private Integer lastUpdateChapterId=0;
	private String lastUpdateChapterName;
	private String threadId;
	private String user_id;
	
	private Integer is_vip=0;
	private int isCanRecord;
	private LastRead lastRead;//从服务端得到的漫画最新阅读记录，真实的阅读记录需要和本地比较
	private Integer code=0;
	private String message;
	//新版本添加内容 @time 2014年6月11日09:52:40
	private String totalComment;
	private int total_hot;
	public Long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}
	private ArrayList<ComicListItem> authorOtherWorks;
	private ArrayList<ComicListItem> editorRecommended;

	public int getTotal_hot() {
		return total_hot;
	}
	public void setTotal_hot(int total_hot) {
		this.total_hot = total_hot;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LastRead getLastRead() {
		return lastRead;
	}
	public void setLastRead(LastRead lastRead) {
		this.lastRead = lastRead;
	}
	public int getIsCanRecord() {
		return isCanRecord;
	}
	public void setIsCanRecord(int isCanRecord) {
		this.isCanRecord = isCanRecord;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	public Integer getComicId() {
		return comicId;
	}
	public void setComicId(Integer comicId) {
		this.comicId = comicId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getBigCover() {
		return bigCover;
	}
	public void setBigCover(String bigCover) {
		this.bigCover = bigCover;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getReadOrder() {
		return readOrder;
	}
	public void setReadOrder(String readOrder) {
		this.readOrder = readOrder;
	}
	public String getSeriesStatus() {
		return seriesStatus;
	}
	public void setSeriesStatus(String seriesStatus) {
		this.seriesStatus = seriesStatus;
	}
	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTotalClick() {
		return totalClick;
	}
	public void setTotalClick(String totalClick) {
		this.totalClick = totalClick;
	}
	public String getTotalTucao() {
		return totalTucao;
	}
	public void setTotalTucao(String totalTucao) {
		this.totalTucao = totalTucao;
	}
    public void setChapterList(List<Chapter> chapterList){
    	this.chapterList= chapterList;
    }
    public List<Chapter> getChapterList(){
    	return chapterList;
    }
	public String getTotalComment() {
		return totalComment;
	}
	public void setTotalComment(String totalComment) {
		this.totalComment = totalComment;
	}
	
	public ArrayList<ComicListItem> getAuthorOtherWorks() {
		return authorOtherWorks;
	}
	public void setAuthorOtherWorks(ArrayList<ComicListItem> authorOtherWorks) {
		this.authorOtherWorks = authorOtherWorks;
	}
	public ArrayList<ComicListItem> getEditorRecommended() {
		return editorRecommended;
	}
	public void setEditorRecommended(ArrayList<ComicListItem> editorRecommended) {
		this.editorRecommended = editorRecommended;
	}

	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getFirstLetter() {
		return firstLetter;
	}
	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}
	public String getTotalTicket() {
		return totalTicket;
	}
	public void setTotalTicket(String totalTotalTicket) {
		this.totalTicket = totalTotalTicket;
	}
	public String getCateId() {
		return cateId;
	}
	public void setCateId(String cateId) {
		this.cateId = cateId;
	}
	public String getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}
	public Integer getLastUpdateChapterId() {
		return lastUpdateChapterId;
	}
	public void setLastUpdateChapterId(Integer lastUpdateChapterId) {
		this.lastUpdateChapterId = lastUpdateChapterId;
	}
	public String getLastUpdateChapterName() {
		return lastUpdateChapterName;
	}
	public void setLastUpdateChapterName(String lastUpdateChapterName) {
		this.lastUpdateChapterName = lastUpdateChapterName;
	}
	public Integer getIs_vip() {
		return is_vip;
	}
	public void setIs_vip(Integer is_vip) {
		this.is_vip = is_vip;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(comicId==null?0:comicId);
		dest.writeString(name);
		dest.writeString(authorName);
		dest.writeString(bigCover);
		dest.writeString(cover);
		dest.writeString(theme);
		dest.writeString(readOrder);
		dest.writeString(seriesStatus);
		dest.writeLong(lastUpdateTime==null?0:lastUpdateTime);
		dest.writeString(description);
		dest.writeString(totalClick);
		dest.writeString(totalTucao);
		dest.writeString(totalTicket);
		dest.writeInt(monthTicket);
		dest.writeInt(allImage);
		dest.writeString(cateId);
		dest.writeString(groupIds);
		dest.writeString(firstLetter);
		dest.writeInt(lastUpdateChapterId);
		dest.writeString(lastUpdateChapterName);
		dest.writeString(threadId);
		dest.writeString(user_id);
		dest.writeInt(is_vip==null?0:is_vip);
		dest.writeInt(isCanRecord);
		dest.writeTypedList(chapterList);
		if(!TextUtils.isEmpty(totalComment)){
			dest.writeString(totalComment);
		}
		dest.writeInt(total_hot);
		dest.writeTypedList(authorOtherWorks);
		dest.writeTypedList(editorRecommended);
	}
	public static final Parcelable.Creator<ComicDetail> CREATOR = new Creator<ComicDetail>() {

		@Override
		public ComicDetail createFromParcel(Parcel source) {
			ComicDetail item = new ComicDetail();
			item.setComicId(source.readInt());
			item.setName(source.readString());
			item.setAuthorName(source.readString());
			item.setBigCover(source.readString());
			item.setCover(source.readString());
			item.setTheme(source.readString());
			item.setReadOrder(source.readString());
			item.setSeriesStatus(source.readString());
			item.setLastUpdateTime(source.readLong());
			item.setDescription(source.readString());
			item.setTotalClick(source.readString());
			item.setTotalTucao(source.readString());
			item.setTotalTicket(source.readString());
			item.setMonthTicket(source.readInt());
			item.setAllImage(source.readInt());
			item.setCateId(source.readString());
			item.setGroupIds(source.readString());
			item.setFirstLetter(source.readString());
			item.setLastUpdateChapterId(source.readInt());
			item.setLastUpdateChapterName(source.readString());
			item.setThreadId(source.readString());
			item.setUser_id(source.readString());
			item.setIs_vip(source.readInt());
			item.setIsCanRecord(source.readInt());
			ArrayList<Chapter> chapterList = new ArrayList<Chapter>();
			source.readTypedList(chapterList, Chapter.CREATOR);
			item.setChapterList(chapterList);
			item.setTotalComment(source.readString());
			item.setTotal_hot(source.readInt());
			ArrayList<ComicListItem> items = new ArrayList<ComicListItem>();
			source.readTypedList(items, ComicListItem.CREATOR);
			item.setAuthorOtherWorks(items);
			ArrayList<ComicListItem> items2 = new ArrayList<ComicListItem>();
			source.readTypedList(items2, ComicListItem.CREATOR);
			item.setEditorRecommended(items2);
			return item;
		}

		@Override
		public ComicDetail[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ComicDetail[size];
		}  
		
	};


	@Override
	public String toString() {
		return "ComicDetail [comicId=" + comicId + ", name=" + name
				+ ", authorName=" + authorName + ", cover=" + cover
				+ ", bigCover=" + bigCover + ", theme=" + theme
				+ ", readOrder=" + readOrder + ", seriesStatus=" + seriesStatus
				+ ", lastUpdateTime=" + lastUpdateTime + ", description="
				+ description + ", totalClick=" + totalClick + ", totalTucao="
				+ totalTucao + ", firstLetter=" + firstLetter
				+ ", chapterList=" + chapterList + ", totalTicket="
				+ totalTicket + ", monthTicket=" + monthTicket + ", allImage="
				+ allImage + ", cateId=" + cateId + ", groupIds=" + groupIds
				+ ", lastUpdateChapterId=" + lastUpdateChapterId
				+ ", lastUpdateChapterName=" + lastUpdateChapterName
				+ ", threadId=" + threadId + ", user_id=" + user_id
				+ ", is_vip=" + is_vip + ", isCanRecord=" + isCanRecord
				+ ", lastRead=" + lastRead + ", code=" + code + ", message="
				+ message + ", totalComment=" + totalComment + ", total_hot="
				+ total_hot + ", authorOtherWorks=" + authorOtherWorks
				+ ", editorRecommended=" + editorRecommended + "]";
	}
	public int getMonthTicket() {
		return monthTicket;
	}
	public void setMonthTicket(int monthTicket) {
		this.monthTicket = monthTicket;
	}
	public int getAllImage() {
		return allImage;
	}
	public void setAllImage(int allImage) {
		this.allImage = allImage;
	}
	

}
