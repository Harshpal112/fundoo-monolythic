package com.bridgeit.fundoo.response;

public class ResponseToken {
	private String token;
	private String message;
	private int code;

	@Override
	public String toString() {
		return "ResponseToken [token=" + token + ", message=" + message + ", code=" + code + "]";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	
	}

	

