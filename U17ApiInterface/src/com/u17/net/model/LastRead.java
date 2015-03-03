package com.u17.net.model;
/**
 * 从服务端得到的最后阅读记录，包括 章节id 和 第几张图片（从0开始）
 */
public class LastRead {
	
	private int chapterId;//章节ID
	private int page;// 第几张图片
	private long updateTime;//最后阅读时间
	
	public int getChapterId() {
		return chapterId;
	}
	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	public boolean hasLastRead(){
		return chapterId != -1 && page != -1 && updateTime!= -1;
	}
}
