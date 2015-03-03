package com.u17.net.model;

import java.io.Serializable;

public class JsonResult implements Serializable{

	private static final long serialVersionUID = 5177022944707502292L;

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
	
	public void setSubmit_res(String submit_res)
	{
		this.submit_res = submit_res;
	}
	
	public String getSubmit_res()
	{
		return submit_res;
	}

	private int code = 0;
	
	private String message = "";
	
	private String submit_res = "";

}
