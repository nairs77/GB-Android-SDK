package com.gebros.platform.net;


public abstract class RequestRunner<T> {

	AbstractRequest request;
		
	public RequestRunner(AbstractRequest.Builder builder) {
		this.request = builder.build();
	}
	
	abstract public void call(ResponseListener<T> listener);

	abstract public T get();
}
