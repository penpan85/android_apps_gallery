package com.u17.net.parser;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import com.u17.net.error.U17JsonParserException;
import com.u17.net.error.U17ServerFail;


public interface BaseParser<T> {
	public T parser(String netResult) throws JSONException,U17ServerFail;
	public T parser(byte[] bytes) throws  UnsupportedEncodingException, JSONException,U17ServerFail
	,StringIndexOutOfBoundsException,U17JsonParserException;
}
