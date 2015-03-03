package com.u17.net.error;

public class U17NoSDCardException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7551108646641161120L;

	@Override
	public String getMessage() {
		return "没有插入SD卡";
	}
}
