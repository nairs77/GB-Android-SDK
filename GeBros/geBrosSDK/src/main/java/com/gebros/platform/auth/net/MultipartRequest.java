package com.gebros.platform.auth.net;

import com.gebros.platform.auth.GBSession;
import com.gebros.platform.auth.ResponseCallback;
import com.gebros.platform.exception.GBException;

import java.io.IOException;
import java.net.HttpURLConnection;


public class MultipartRequest extends Request {

	public static final String BOUNDARY = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
	private static final String CONTENT_TYPE = "multipart/form-data; boundary=" + BOUNDARY;
	private static final String CONNECTION = "Keep-Alive";
	
	private String fileName;
	
	public MultipartRequest(Method method, GBSession gbSession, String apiPath, ResponseCallback callback) {
		
		this.method = method;
		this.gbSession = gbSession;
		this.apiPath = apiPath;
		this.callback = callback;
	}

	public Response getResponse() throws GBException, IOException {
		
		HttpURLConnection connection = createHttpConnection();
		headers = connection.getRequestProperties();
		
		return MultipartResponse.fromHttpConnection(this, connection);
	}

	@Override
	protected void setDefaultHeaders(HttpURLConnection connection) {
		
		connection.setRequestProperty("Content-Type", CONTENT_TYPE);
        connection.setRequestProperty("Accept", DEFAULT_ACCEPT);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Connection", CONNECTION);
        connection.setRequestProperty("Accept-Encoding", DEFAULT_ACCEPT_ENCODING);
        
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
}
