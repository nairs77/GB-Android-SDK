package com.gebros.platform.net;

import com.gebros.platform.internal.CodecUtils;
import com.gebros.platform.log.GBLog;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author nairs77@jocytidy.com
 *
 */
class HttpPost extends OKHttpTemplate {

	HttpPost(String url) {
		super(url);
	}
	
	HttpPost(String url, Map<String, Object> parameters) {
		super(url, parameters);
	}
	
	@Override
	String getResponse() throws IOException {
				
		HttpURLConnection connection = client.open(new URL(url));
		connection.setRequestProperty("Content-Type", CONTENT_TYPE);
		
		OutputStream out = null;
		InputStream in = null;

		try {

			connection.setRequestMethod(HttpMethod.POST.name());
			out = connection.getOutputStream();
			out.write(parametersToByteValue());
			out.close();

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException("Unexpected HTTP response: "
						+ connection.getResponseCode() + " "
						+ connection.getResponseMessage());
			}

			in = connection.getInputStream();
			String response = CodecUtils.readStreamToString(in);
			
			GBLog.d("------------------------- HTTP %s -------------------------------------------------------", "Request");
			GBLog.d("URL: %s", url);
			GBLog.d("Method: %s", HttpMethod.POST.name());
			GBLog.d("[RequestBody]\n%s", new String(parametersToByteValue(), "UTF-8"));
			GBLog.d("------------------------- HTTP %s -------------------------------------------------------", "Response");
			GBLog.d("responseCode: %d", connection.getResponseCode());
			GBLog.d("[ResponseBody]\n%s", response);
			GBLog.d("----------------------------------------------------------------------------------------------");
			return response;

		} finally {
			CodecUtils.closeQuietly(out);
			CodecUtils.closeQuietly(in);
		}
	}
	
	private byte[] parametersToByteValue() throws UnsupportedEncodingException {
		String params = null;
		if(bodyType.equals(BodyType.NORMAL))
			params = CodecUtils.encodeURLParameters(parameters);
		else 
			params = new JSONObject(parameters).toString();
		return params.getBytes();
	}
}
