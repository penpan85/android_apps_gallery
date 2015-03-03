package com.u17.net.model;

public class U17request {
	private String id;
	private String url;
	private int type;//0 comiclist, 1,comicdetail,2,image
	private boolean isSuccess;
	private int retryCount;
	private int dataLength;
	private String exception;
	private int exceptionCode;
	private long requestBeginTime;
	private long requestEndTime;
	private long requestGetDataBeginTime;
	private long requestGetDataEndTime;
	private int requestWholeTime;
	private int requestGetDataTime;
	private String connectionType = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public int getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public long getRequestBeginTime() {
		return requestBeginTime;
	}
	public void setRequestBeginTime(long requestBeginTime) {
		this.requestBeginTime = requestBeginTime;
	}
	public long getRequestEndTime() {
		return requestEndTime;
	}
	public void setRequestEndTime(long requestEndTime) {
		this.requestEndTime = requestEndTime;
	}
	public long getRequestGetDataBeginTime() {
		return requestGetDataBeginTime;
	}
	public void setRequestGetDataBeginTime(long requestGetDataBeginTime) {
		this.requestGetDataBeginTime = requestGetDataBeginTime;
	}
	public long getRequestGetDataEndTime() {
		return requestGetDataEndTime;
	}
	public void setRequestGetDataEndTime(long requestGetDataEndTime) {
		this.requestGetDataEndTime = requestGetDataEndTime;
	}
	public int getRequestWholeTime() {
		return requestWholeTime;
	}
	public void setRequestWholeTime(int requestWholeTime) {
		this.requestWholeTime = requestWholeTime;
	}
	public int getRequestGetDataTime() {
		return requestGetDataTime;
	}
	public void setRequestGetDataTime(int requestGetDataTime) {
		this.requestGetDataTime = requestGetDataTime;
	}
	public String getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	
	
	
	
}
