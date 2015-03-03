package com.u17.net.model;

public class VisitResult {
	private int code;
	private Object tag;
	private String message;
	private Object result;
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
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public Object getTag() {
		return tag;
	}
	public void setTag(Object tag) {
		this.tag = tag;
	}
	
}
