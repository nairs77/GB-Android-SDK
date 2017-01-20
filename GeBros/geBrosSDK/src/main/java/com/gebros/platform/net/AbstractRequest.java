package com.gebros.platform.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AbstractRequest {

	private String url;
	private HttpMethod method;
	private Map<String, Object> parameters;
	private boolean enableJsonBody;
	
	public String getUrl() {
		return url;
	}
	
	public HttpMethod getMethod() {
		return method;
	}
	
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	public boolean enableJsonBody() {
		return enableJsonBody;
	}
	
	public static class Builder {
		
		private final String url;
		private HttpMethod method = HttpMethod.GET;
		private Map<String, Object> parameters = new HashMap<String, Object>();
		private boolean enableJsonBody = false;
		
		public Builder(String url) {
			this.url = url;
		}
		
		public Builder method(HttpMethod method) {
			this.method = method;
			return this;
		}
		
		public Builder addParameters(String key, Object value) {
			this.parameters.put(key, value);
			return this;
		}
		
		public Builder params(JSONObject json) {
			try {
				parameters = JsonHelper.toMap(json);
			} catch (JSONException e) {}
			return this;
		}
		
		public Builder enableJsonBody(boolean enableJsonBody) {
			this.enableJsonBody = enableJsonBody;
			return this;
		}
		
		public AbstractRequest build() {
			return new AbstractRequest(this);
		}
	}
	
	protected AbstractRequest(Builder builder) {
		this.url = builder.url;
		this.method = builder.method;
		this.parameters = builder.parameters;
		this.enableJsonBody = builder.enableJsonBody;
	}
}
