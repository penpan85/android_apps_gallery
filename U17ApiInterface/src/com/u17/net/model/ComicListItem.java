package com.u17.net.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.json.JSONArray;

import com.u17.net.utils.DataTypeUtils;

import android.os.Parcel;
import android.os.Parcelable;


public class ComicListItem implements Parcelable{
	private String cover;
	private String name;
	private int comicId;
	private long updateTime;
	private String updateTips;
	//0代表签约，1代表独家，2代表不显示,3,代表既是签约又是独家
	//2014-05-15，2.0新增字段值3
	private int accreditDisplayCode;
	//""搞笑，动作等标签，
	//2014-05-15，2.0新增此字段
	private String[] tags;
	//2014-05-15，2.0新增此字段
	private String extraValue;

	//2签约
	private int accredit;

	private String description;
	//某种排行榜的说明
	//2014-05-15，2.0新增此字段
	private String latestUpdateChapter;
	//1独家
	private int exclusive;
	private String nickname;
	
	private String series_status;
	
	/*漫画点击量*/
	private String click_total;
	
	
	public String getClick_total() {
		return click_total;
	}
	public void setClick_total(String click_total) {
		this.click_total = click_total;
	}
	public String getSeries_status() {
		return series_status;
	}
	public void setSeries_status(String series_status) {
		this.series_status = series_status;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	
	public String getExtraValue() {
		return extraValue;
	}
	public void setExtraValue(String extraValue) {
		this.extraValue = extraValue;
	}
	public int getAccreditDisplayCode() {
		return accreditDisplayCode;
	}
	public void setAccreditDisplayCode(int accreditDisplayCode) {
		this.accreditDisplayCode = accreditDisplayCode;
	}
	public String getUpdateTips() {
		return updateTips;
	}
	public void setUpdateTips(String updateTips) {
		this.updateTips = updateTips;
	}
	public String getNickname()
	{
		return nickname;
	}
	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	public int getExclusive()
	{
		return exclusive;
	}
	public void setExclusive(int exclusive)
	{
		this.exclusive = exclusive;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getComicId() {
		return comicId;
	}
	public void setComicId(int comicId) {
		this.comicId = comicId;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public int getAccredit() {
		return accredit;
	}
	public void setAccredit(int accredit) {
		this.accredit = accredit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

	
	public String getLatestUpdateChapter() {
		return latestUpdateChapter;
	}
	public void setLatestUpdateChapter(String latestUpdateChapter) {
		this.latestUpdateChapter = latestUpdateChapter;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.cover);
		dest.writeString(this.description);
		dest.writeInt(this.accredit);
		dest.writeInt(this.comicId);
		dest.writeLong(this.updateTime);
		dest.writeString(this.nickname);
		dest.writeString(this.updateTips);
		dest.writeInt(this.accreditDisplayCode);
		if(DataTypeUtils.isEmpty(tags)){
			dest.writeInt(0);
		}else{
			dest.writeInt(tags.length);
			dest.writeStringArray(tags);
		}
		dest.writeString(this.extraValue);
		dest.writeString(this.latestUpdateChapter);
		dest.writeString(this.series_status);
		dest.writeString(this.click_total);
	}
	public static final Parcelable.Creator<ComicListItem> CREATOR=new Parcelable.Creator<ComicListItem>() {

		@Override
		public ComicListItem createFromParcel(Parcel source) {
			ComicListItem item=new ComicListItem();
			item.setName(source.readString());
			item.setCover(source.readString());
			item.setDescription(source.readString());
			item.setAccredit(source.readInt());
			item.setComicId(source.readInt());
			item.setUpdateTime(source.readLong());
			item.setNickname(source.readString());
			item.setUpdateTips(source.readString());
			item.setAccreditDisplayCode(source.readInt());
			int tagsLength = source.readInt();
			String[] tagsFromParcelable = null;
			if(tagsLength>0){
				tagsFromParcelable = new String[tagsLength];
				source.readStringArray(tagsFromParcelable);
			}
			item.setTags(tagsFromParcelable);
			item.setExtraValue(source.readString());
			item.setLatestUpdateChapter(source.readString());
			item.setSeries_status(source.readString());
			item.setClick_total(source.readString());
			return item;
		}

		@Override
		public ComicListItem[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ComicListItem[size];
		}
	};
}
