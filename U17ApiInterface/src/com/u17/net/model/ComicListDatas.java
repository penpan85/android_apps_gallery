package com.u17.net.model;

import java.util.ArrayList;
import java.util.List;

import com.u17.net.utils.DataTypeUtils;
public class ComicListDatas {
	private int totalNum = 0;
	private long updateTime = 0;
	private int curPageNo = 1;
	private ArrayList<ComicListItem> listItems = new ArrayList<ComicListItem>();
	private int code;
	private String message;
	
	public int getCode() {
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
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public ArrayList<ComicListItem> getListItems() {
		return listItems;
	}
	
	public void clear(){
		if(!DataTypeUtils.isEmpty(listItems)){
			listItems.clear();
			listItems=null;
		}
	}
	public void setListItems(ArrayList<ComicListItem> listItems) {
		this.listItems = listItems;
	}
	public int getCurPageNo() {
		return curPageNo;
	}
	public void setCurPageNo(int curPageNo) {
		this.curPageNo = curPageNo;
	}
}
