package com.u17.net.model;

public class ChapterSizeResult {
	private long ori;//原图
	private long svol;//小体积图（高清）
	private long square;//75*75
	private long mobile;//手机端图片（普通）
	private long webp_05;
	private long webp_50;
	public long getOri() {
		return ori;
	}
	public void setOri(long ori) {
		this.ori = ori;
	}
	public long getSvol() {
		return svol;
	}
	public void setSvol(long svol) {
		this.svol = svol;
	}
	public long getSquare() {
		return square;
	}
	public void setSquare(long square) {
		this.square = square;
	}
	public long getMobile() {
		return mobile;
	}
	public void setMobile(long mobile) {
		this.mobile = mobile;
	}
	public long getWebp_05() {
		return webp_05;
	}
	public void setWebp_05(long webp_05) {
		this.webp_05 = webp_05;
	}
	public long getWebp_50() {
		return webp_50;
	}
	public void setWebp_50(long webp_50) {
		this.webp_50 = webp_50;
	}
	
}
