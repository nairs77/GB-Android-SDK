package com.gebros.platform.net;

import com.gebros.platform.internal.CodecUtils;
import com.gebros.platform.log.GBLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


class HttpGet extends OKHttpTemplate {

	private static final String AMPERSAND_SEPARATED_STRING = "%s?%s";
	
	HttpGet(String url) {
		super(url);
	}
	
	HttpGet(String url, Map<String, Object> parameters) {
		super(url, parameters);
	}

	@Override
	String getResponse() throws IOException {
		
		HttpURLConnection connection = client.open(new URL(getExtractedParameterUrl()));
		connection.setRequestProperty("Content-Type", CONTENT_TYPE);
        
		InputStream in = null;
		try {
			
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException("Unexpected HTTP response: "
						+ connection.getResponseCode() + " "
						+ connection.getResponseMessage());
			}
			
			in = connection.getInputStream();
			String response = CodecUtils.readStreamToString(in);

			GBLog.d("------------------------- HTTP %s -------------------------------------------------------", "Request");
			GBLog.d("URL: %s", getExtractedParameterUrl());
			GBLog.d("Method: %s", HttpMethod.GET.name());
			GBLog.d("------------------------- HTTP %s -------------------------------------------------------", "Response");
			GBLog.d("responseCode: %d", connection.getResponseCode());
			GBLog.d("[ResponseBody]\n%s", response);
			GBLog.d("----------------------------------------------------------------------------------------------");

			return response;
			
		} finally {
			CodecUtils.closeQuietly(in);
		}
	}
	
	private String getExtractedParameterUrl() throws UnsupportedEncodingException {
		
		if(parameters.isEmpty())
			return url;
		else 
			return extractParameters();
	}
	
	private String extractParameters() throws UnsupportedEncodingException {
		String params = CodecUtils.encodeURLParameters(parameters);
		return String.format(AMPERSAND_SEPARATED_STRING, url, params);
	}
}
