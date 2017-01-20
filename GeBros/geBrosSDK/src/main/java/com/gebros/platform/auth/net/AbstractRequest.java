package com.gebros.platform.auth.net;


public interface AbstractRequest {

	abstract public void onSuccess(Response response);
	abstract public void onFailed(Response response);
	
}
