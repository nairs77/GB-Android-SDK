package com.gebros.platform.net;


public class AsyncRequest extends AbstractRequest {

	public class Builder extends AbstractRequest.Builder {

		public Builder(String url) {
			super(url);
		}
		
		public AsyncRequest build() {
			return new AsyncRequest(this);
		}
	}
	
	private AsyncRequest(Builder builder) {
		super(builder);
	}
}
