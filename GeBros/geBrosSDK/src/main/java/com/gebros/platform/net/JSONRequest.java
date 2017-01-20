package com.gebros.platform.net;



public class JSONRequest extends AbstractRequest {
	
	public static class Builder extends AbstractRequest.Builder {
		
		public Builder(String url) {
			super(url);
		}
		
		public JSONRequest build() {
			return new JSONRequest(this);
		}
	}
	
	private JSONRequest(Builder builder) {
		super(builder);
	}
}
