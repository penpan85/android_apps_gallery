package com.u17.net.error;

/**
 * @Package com.u17.core.error 
 * @user: pengpan
 * @time: 2014-6-4,上午10:22:16
 * @Description: TODO
 * @version
 */
public class U17ServerFail extends U17Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6485575731520202667L;
	public U17ServerFail(){
		this("");
	}
	public U17ServerFail(String s) {
		super(s);
	}
	
}
