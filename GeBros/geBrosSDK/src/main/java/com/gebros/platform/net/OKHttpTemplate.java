package com.gebros.platform.net;

import com.gebros.platform.net.okhttp.OkHttpClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

/**
 * @author nairs77@joycity.com
 *
 */
abstract class OKHttpTemplate {

	static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	static final String DEFAULT_ACCEPT = "application/json";
	static final String DEFAULT_ACCEPT_ENCODING = "gzip";
	
	OkHttpClient client;
	String url;
	Map<String, Object> parameters;
	enum BodyType { NORMAL, JSON }
	BodyType bodyType = BodyType.NORMAL;
	
	OKHttpTemplate() {
		client = new OkHttpClient();
		parameters = new HashMap<String, Object>();
		
		// https://github.com/square/okhttp/issues/184
		SSLContext sslContext;
		try {
		    sslContext = SSLContext.getInstance("TLS");
		    sslContext.init(null, null, null);
		} catch (GeneralSecurityException e) {
			throw new AssertionError(); // The system has no TLS. Just give up.
		}
		
		client.setSslSocketFactory(sslContext.getSocketFactory());		

	}
	
	OKHttpTemplate(String url) {
		this();
		this.url = url;
	}
	
	OKHttpTemplate(String url, Map<String, Object> parameters) {
		this();
		this.url = url;
		this.parameters = parameters;
	}
	
	void addParameter(String key, Object value) {
		parameters.put(key, value);
	}
	
	void enableJsonBody() {
		this.bodyType = BodyType.JSON;
	}
	
	abstract String getResponse() throws IOException;
}
