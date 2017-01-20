package com.gebros.platform.net;


public class OkHttpException extends Exception {
	private static final long serialVersionUID = 1L;
	
	OkHttpException() {}
	OkHttpException(String message) {
		super(message);
	}
}