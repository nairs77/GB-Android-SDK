package com.gebros.platform.auth.net;

import android.os.Message;

import com.gebros.platform.GBSettings;
import com.gebros.platform.auth.GBSession;
import com.gebros.platform.auth.ResponseCallback;
import com.gebros.platform.auth.model.common.JsonUtil;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.exception.GBRuntimeException;
import com.gebros.platform.internal.CodecUtils;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.util.GBDeviceUtils;
import com.gebros.platform.util.GBValidator;


import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

//import android.support.annotation.RequiresPermission;
//import com.joycity.platform.account.internal.JoypleLogger;
//import com.joycity.platform.account.internal.Logger;


public class Request implements AbstractRequest {

	protected final static String TAG = Request.class.getCanonicalName() + ":";

	public enum Method {
		POST,
		GET,
		PUT,
		DELETE
	}

	public enum BodyType {
		PARAMETER,
		JSON
	}

	public enum RequestType {
		GUEST,					// None authorization header
		AUTHORIZATION,			// Has header for authentication, clientSecret
		API						// Has API authorization header, accessToken
	}

	public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	protected static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	protected static final String DEVICE_HEADER_KEY = "Device-Info";
	protected static final String AUTHORIZATION_TYPE = "Bearer";
	protected static final String DEFAULT_ACCEPT = "application/json";
	protected static final String USER_AGENT = "joyple/0.8";
	protected static final String DEFAULT_ACCEPT_ENCODING = "gzip";

	/**
	 * @author : win_win
	 * @date : 2014.11.17
	 * @desc : http 통신 시 url connection error 를 방지하기위해 시간을 12초에서 60초로 늘림
	 */
//	protected static final int READ_TIMEOUT = 12000;
//	protected static final int CONNECT_TIMEOUT = 12000;
	protected static final int READ_TIMEOUT = 60000;
	protected static final int CONNECT_TIMEOUT = 60000;

	/**
	 * Authorization header for API Authentication.
	 */

	protected Method method = Method.POST;
	protected BodyType bodyType = BodyType.PARAMETER;
	protected RequestType requestType = RequestType.API;

	protected String apiPath;
	protected Map<String, Object> params = new HashMap<String, Object>();
	protected Map<String, List<String>> headers;

	protected GBSession gbSession;
	protected ResponseCallback callback;
	protected Response response;

	public Request() {
		this(null, null, null);
	}

	public Request(GBSession gbSession, String apiPath, ResponseCallback callback) {
		this.gbSession = gbSession;
		this.apiPath = apiPath;
		this.callback = callback;
	}

	public Request(Method method, GBSession gbSession, String apiPath, ResponseCallback callback) {
		this.method = method;
		this.gbSession = gbSession;
		this.apiPath = apiPath;
		this.callback = callback;
	}

	public Request(GBSession gbSession, String apiPath, Map<String, Object> params, ResponseCallback callback) {
		this.gbSession = gbSession;
		this.apiPath = apiPath;
		this.params = params;
		this.callback = callback;
	}

	public Request(Method method, GBSession gbSession, String apiPath, Map<String, Object> params, ResponseCallback callback) {
		this.method = method;
		this.gbSession = gbSession;
		this.apiPath = apiPath;
		this.params = params;
		this.callback = callback;
	}

	public void appendParam(String key, Object value) {
		params.put(key, value);
	}

	public void setBodyType(BodyType bodyType) {
		this.bodyType = bodyType;
	}

	public BodyType getBodyType() {
		return bodyType;
	}

	public void setCallbackListener(ResponseCallback callback) {
		this.callback = callback;
	}

	public ResponseCallback getCallbackListener() {
		return this.callback;
	}

	/**
	 * Set test data
	 * It's only used for testMode
	 */

	public void testForResult(Request request, String responseBody) throws GBException, IOException {
		response = Response.createResponseFromString(request, null, responseBody);
	}

	/**
	 * Execute request
	 * Common Request/Response Error Handle
	 */

	public Response getResponse() throws GBException, IOException {
		HttpURLConnection connection = createHttpConnection();
		headers = connection.getRequestProperties();

		return Response.fromHttpConnection(this, connection);
	}

	/**
	 * Http Sync Test
	 */

	public void execute() {
		if(response == null) {
			try {

				response = getResponse();

			} catch (IOException e) {

				throw new GBRuntimeException(e.getMessage());

			} catch (GBException e) {

				response = new Response.Builder(this, null)
    			.responseCode(Response.HTTP_UNAUTHORIZED)
    			.status(Response.API_ON_FAILED)
    			.state(JsonUtil.getResponseErrorTemplate(e.getExceptionType()))
    			.exception(e).build();
			}

		} else
			GBLog.d(TAG + "%s", "execute on localTestFile.");

		if(response.hasError()) {
			onFailed(response);
			return;
		}

		if(response.getStatus() == Response.API_ON_FAILED) {
			onFailed(response.setException(new GBException(GBExceptionType.SERVER_RESPONSE_FAILED)));
			return;
		}

		onSuccess(response);
	}

	@Override
	public void onSuccess(Response response) {
		if(callback == null)
			throw new GBRuntimeException(GBExceptionType.CALLBACK_NULL);

		Message message = new Message();
		message.what = Response.API_ON_SUCCESS;
		message.obj = response;
		callback.handleMessage(message);

		//TODO : must to do
//		if(!GBLog.isRelease())
//			GBLog.httpResponseLog(response);
	}

	@Override
	public void onFailed(Response response) {
		if(response == null)
			throw new GBRuntimeException(GBExceptionType.RESPONSE_NULL);

		if(callback == null)
			throw new GBRuntimeException(GBExceptionType.CALLBACK_NULL);

		Message message = new Message();
		message.what = Response.API_ON_FAILED;
		message.obj = response;
		callback.handleMessage(message);

//		if(!GBLog.isRelease())
//			GBLog.httpErrorLog(response);
	}

	protected void setDefaultHeaders(HttpURLConnection connection) {
		connection.setRequestProperty("Content-Type", CONTENT_TYPE);
        connection.setRequestProperty("Accept", DEFAULT_ACCEPT);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept-Encoding", DEFAULT_ACCEPT_ENCODING);

        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);

        if(method == Method.GET)
        	return;

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
	}

	protected void setAuthorizationHeaders(HttpURLConnection connection) {
		String[] headers = {
				GBSettings.getClientSecret(),
				GBSession.getActiveSession().getAccessToken(),
				GBSession.getActiveSession().getRefreshToken(),
				""
		};

		String authHeader = createAuthorizationHeaderValues(headers);
		connection.setRequestProperty(AUTHORIZATION_HEADER_KEY, authHeader);
	}

	protected void setApiResourceHeaders(HttpURLConnection connection) {

		if(GBSession.getActiveSession().getAccessToken() == null)
			throw new GBRuntimeException(GBExceptionType.NOT_FOUND_TOKEN);

		String[] headers = {
				GBSettings.getClientSecret(),
				GBSession.getActiveSession().getAccessToken(),
				"",
				""
		};

		String apiHeader = createAuthorizationHeaderValues(headers);
		connection.setRequestProperty(AUTHORIZATION_HEADER_KEY, apiHeader);
	}

//	@RequiresPermission(allOf = {
//			Manifest.permission.READ_PHONE_STATE,
//			Manifest.permission.ACCESS_WIFI_STATE})
	protected void setDeviceResourcesHeaders(HttpURLConnection connection) {
//		if(!JoypleLogger.isTestMode())
//			connection.setRequestProperty(DEVICE_HEADER_KEY, DeviceUtilsManager.getInstance().getEntireDeviceInfo());

		connection.setRequestProperty(DEVICE_HEADER_KEY, GBDeviceUtils.getEntireDeviceInfo());
	}

	public static String createAuthorizationHeaderValues(String[] headers) {
		if(headers == null)
			throw new GBRuntimeException("Authorization Header values is NULL.");

		StringBuilder sb = new StringBuilder();
		sb.append(AUTHORIZATION_TYPE);
		sb.append(" ");

		for(int i = 0 ; i < headers.length ; i++) {

			sb.append(headers[i] == null ? "" : headers[i]);
			if(i < headers.length - 1)
				sb.append(":");
		}

		return sb.toString();
	}

	protected HttpURLConnection createHttpConnection() throws GBException, IOException {
		if(apiPath == null)
			throw new GBException(GBExceptionType.BAD_REQUEST);

		URL url = null;

		try {

			String encodedParameters = CodecUtils.encodeURLParameters(getParams());
			url = new URL(method == Method.GET ? apiPath + (GBValidator.isNullOrEmpty(encodedParameters) ? "" : "?" + encodedParameters) : apiPath);

		} catch (MalformedURLException e) {
			throw new GBException(GBExceptionType.BAD_REQUEST);
		}

		URLConnection urlConnection = url.openConnection();
		HttpURLConnection connection = null;

		if(urlConnection instanceof HttpsURLConnection) {
			connection = (HttpsURLConnection) urlConnection;
		} else {
			connection = (HttpURLConnection) urlConnection;
		}

        connection.setRequestMethod(method.name());

        /**
         * set header properties
         */

        setDefaultHeaders(connection);

        if(requestType.equals(RequestType.AUTHORIZATION)) {

        	setAuthorizationHeaders(connection);
        	setDeviceResourcesHeaders(connection);

        } else if(requestType.equals(RequestType.GUEST)) {

        	setAuthorizationHeaders(connection);
        	setDeviceResourcesHeaders(connection);

        } else if(requestType.equals(RequestType.API)) {

        	if(gbSession == null)
        		throw new GBException(GBExceptionType.SESSION_NULL);

        	if(!gbSession.isOpened())
        		throw new GBException(GBExceptionType.SESSION_INVALID);

        	setApiResourceHeaders(connection);
        	setDeviceResourcesHeaders(connection);
        }

        return connection;
	}

	public Method getMethod() {
		return method;
	}

	public String getApiPath() {
		return apiPath;
	}

	public GBSession getSession() {
		return gbSession;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = (params == null ? new HashMap<String, Object>() : params);
	}

	public void addHeader(String key, List<String> values) {
		headers.put(key, values);
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public String getRequestBody() throws IOException {
		String requestBody = null;
		if(getBodyType().equals(BodyType.PARAMETER))
			requestBody = CodecUtils.encodeURLParameters(getParams());
		else
			requestBody = serializeToJsonString(getParams());

		return requestBody;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	private String serializeToJsonString(Map<String, Object> params) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject(params);
		return CodecUtils.RFC3986Encoder(json.toString());
	}
}
