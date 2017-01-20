package com.gebros.platform.net;


public interface ResponseListener<T> {
	
	static final int SUCCESS_EVENT = 0;
	static final int FAILED_EVENT = 1;
	static final int API_SUCCESS = 1;
	static final int API_FAILED = 0;
	
	public void onComplete(T result);
	public void onException(OkHttpException exception);
}