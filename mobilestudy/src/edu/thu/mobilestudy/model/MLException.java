/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.thu.mobilestudy.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Õ®”√“Ï≥£¿‡
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @author hujiawei
 */
public class MLException extends Exception {

	private static final long serialVersionUID = -2623309261327598087L;
	private int statusCode = -1;// ◊¥Ã¨¬Î
	private int errorCode = -1;// ¥ÌŒÛ¬Î
	private String request;// «Î«Û
	private String error;// ¥ÌŒÛ

	public MLException(String msg) {
		super(msg);
	}

	public MLException(Exception cause) {
		super(cause);
	}

	public MLException(String msg, int statusCode) {
		super(msg);
		this.statusCode = statusCode;
	}

	public MLException(String msg, Exception cause) {
		super(msg, cause);
	}

	public MLException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;
	}

	public MLException(String msg, JSONObject json, int statusCode) throws JSONException {
		super(msg + "\n error:" + json.getString("error") + " error_code:" + json.getInt("error_code") + json.getString("request"));
		this.statusCode = statusCode;
		this.errorCode = json.getInt("error_code");
		this.error = json.getString("error");
		this.request = json.getString("request");
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getRequest() {
		return request;
	}

	public String getError() {
		return error;
	}

}
