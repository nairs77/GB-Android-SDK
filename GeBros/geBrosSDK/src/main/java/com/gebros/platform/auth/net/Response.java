package com.gebros.platform.auth.net;

import com.gebros.platform.auth.model.common.GBAPIError;
import com.gebros.platform.auth.model.common.GBObject;
import com.gebros.platform.auth.model.common.JsonUtil;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.internal.CodecUtils;
import com.gebros.platform.log.GBLog;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Response {

	protected static final String TAG = Response.class.getCanonicalName() + ":";
	
	public static final int HTTP_OK = 200;
	public static final int HTTP_BAD_REQUEST = 400;
	public static final int HTTP_UNAUTHORIZED = 401;
	public static final int HTTP_NOT_FOUND = 404;
	public static final int HTTP_SERVER_ERROR = 500;
	
	public static final int CLIENT_LOGIN_CANCELED = -501;
	public static final int CLIENT_ON_ERROR = -500;
	public static final int CLIENT_HTTP_BAD_REQUEST = -400;
	public static final int API_ON_FAILED = 0;
	public static final int API_ON_SUCCESS = 1;
	
	public static final int API_TOKEN_EXPIRES = -4;
	
	public static final String API_RETURN_CODE = "status";
	public static final String API_RESULT_KEY = "RESULT";
	public static final String API_ERROR_KEY = "error";
	public static final String ERROR_CODE_KEY = "errorCode";
	public static final String ERROR_MESSAGE_KEY = "errorType";
	
	/**
	 * request status
	 */
	
	protected Request request;					// HttpRequest
	protected HttpURLConnection connection; 	// HttpConnection
	
	/**
	 * response status
	 */
	
	protected int responseCode;					// HTTP response code
	protected int status;						// API ReturnCode
	protected String responseBody;				// API response body
	protected JSONObject state; 				// API body convert to JSON
	protected GBAPIError apiError;			// API error status
	protected GBObject gbObject; 		// Converted API Object
	protected GBException exception;		// Client error
	
	public static Response fromHttpConnection(Request request, HttpURLConnection connection) throws IOException {
		
		DataOutputStream outputStream = null;
		InputStream inputStream = null;
		
		int responseCode = HTTP_OK;
		String responseBody = null;
		
	    try {
	    	
	    	if(request == null || connection == null)
	    		throw new GBException(GBExceptionType.BAD_REQUEST);
	    	
    		if(request.getMethod() != Request.Method.GET) {
    			
    			outputStream = new DataOutputStream(connection.getOutputStream());
    	    	outputStream.writeBytes(request.getRequestBody());
    		}

	    	responseCode = connection.getResponseCode();
	    	if(responseCode >= HTTP_BAD_REQUEST)
	    		inputStream = connection.getErrorStream();
	    	else
	    		inputStream = connection.getInputStream();

	    	responseBody = CodecUtils.readStreamToString(inputStream);
	    	
	    	if(GBLog.isDebug()) {
	    		
		    	GBLog.d("------------------------- HTTP %s ------------------------------------------------------------", "Request");
				GBLog.d("URL: %s", request.getApiPath());
				GBLog.d("Method: %s", request.getMethod());
				GBLog.d("[Headers]\n%s", getArrangeHeaders(request.getHeaders()));
				GBLog.d("[RequestBody]\n%s", request.getRequestBody());

				GBLog.d("------------------------- HTTP %s ------------------------------------------------------------", "Response");
				GBLog.d("responseCode: %d", connection.getResponseCode());
				GBLog.d("[Headers]\n%s", getArrangeHeaders(connection.getHeaderFields()));
				GBLog.d("[ResponseBody]\n%s", responseBody);
				GBLog.d("---------------------------------------------------------------------------------------------------");
	    	}
	    	
	    	Response response = createResponseFromString(request, connection, responseBody);
	    	response.setResponseCode(responseCode);
			response.setResponseBody(responseBody);
			
			return response;
			
	    } catch (SocketTimeoutException e) {

			GBLog.e(e, TAG + "%s", e.toString());
        	return new Builder(request, connection)
        			.responseCode(Response.HTTP_BAD_REQUEST)
        			.responseBody(responseBody)
        			.exception(new GBException(GBExceptionType.CONNECTION_ERROR))
        			.state(JsonUtil.getResponseErrorTemplate(GBExceptionType.CONNECTION_ERROR)).build();
        	
	    } catch (IOException e) {

			GBLog.e(e, TAG + "%s", e.toString());
        	return new Builder(request, connection)
        			.responseCode(Response.HTTP_BAD_REQUEST)
        			.responseBody(responseBody)
        			.exception(new GBException(GBExceptionType.BAD_REQUEST))
        			.state(JsonUtil.getResponseErrorTemplate(GBExceptionType.BAD_REQUEST)).build();
        	
        } catch (GBException e) {

			GBLog.e(e, TAG + "%s", e.getMessage());
        	return new Builder(request, connection)
        			.responseCode(responseCode)
        			.state(JsonUtil.getResponseErrorTemplate(e.getExceptionType()))
        			.responseBody(responseBody)
        			.exception(e).build();
        	
        }  finally {
        	
        	CodecUtils.closeQuietly(inputStream);
        	CodecUtils.closeQuietly(outputStream);
        	
        	connection.disconnect();
        	connection = null;
        	inputStream = null;
        	outputStream = null;
        }
	}
	
	static Response createResponseFromString(Request request, HttpURLConnection connection, String responseBody) throws GBException, IOException {
		
		GBObject gbObject = GBObject.Factory.create(createJsonFromBody(responseBody)).cast(GBObject.class);
		return createReponseFromObject(request, connection, gbObject);
	}
	
	static Response createReponseFromObject(Request request, HttpURLConnection connection, GBObject GBObject) {
		
		Integer status = (Integer) GBObject.getProperty(Response.API_RESULT_KEY);
		status = (status == null ? Response.API_ON_FAILED : status);
		
		/**
		 * API JSON Values - returnCode 
		 * 1 : onSuccess
		 * 2 : onError : error:{errorType:"", errorCode:0}
		 */
		
		if(status == Response.API_ON_SUCCESS) {
			
			// API On Success
			return new Builder(request, connection)
					.GBObject(GBObject)
					.state(GBObject.getInnerJSONObject())
					.status(status).build();
			
		} else {
			
			// API On Error
			return new Builder(request, connection)
					.GBObject(GBObject)
					.status(status)
					.apiError(GBObject.getAPIError()).build();
		}
	}
	
	static JSONObject createJsonFromBody(String responseBody) throws GBException {
		
		if(responseBody == null)
			throw new GBException(GBExceptionType.NOT_EXISTS_BODY);
		
		JSONObject json = null;
		
		try {
			json = new JSONObject(responseBody);
		} catch (Exception e) {
			throw new GBException(GBExceptionType.JSON_PARSE_ERROR);
		}
		
		if((!json.has(API_RESULT_KEY) || !json.has(API_RESULT_KEY)))
			throw new GBException(GBExceptionType.NOT_EXISTS_RETURN);
		
		return json;
	}
	
	public boolean hasError() {
		return (exception == null ? false : true);
	}

	public static class Builder {
		
		private final Request request;
		private final HttpURLConnection connection;
		
		private int responseCode = API_ON_FAILED;
		private int status = API_ON_FAILED;
		private String responseBody;
		private JSONObject state;
		private GBObject GBObject;
		private GBException exception = null;
		private GBAPIError apiError = null;

		public Builder(Request request, HttpURLConnection connection) {
			this.request = request;
			this.connection = connection;
		}
		
		public Builder responseCode(int val) {
			responseCode = val; return this;
		}
		
		public Builder responseBody(String val) {
			responseBody = val; return this;
		}
		
		public Builder status(int val) {
			status = val; return this;
		}
		
		public Builder state(JSONObject val) {
			state = val; return this;
		}
		
		public Builder GBObject(GBObject val) {
			GBObject = val; return this;
		}
		
		public Builder exception(GBException e) {
			exception = e; return this;
		}
		
		public Builder apiError(GBAPIError val) {
			apiError = val; return this;
		}
		
		public Response build() {
			return new Response(this);
		}
	}
	
	protected Response(Builder builder) {
		
		request = builder.request;
		connection = builder.connection;
		responseCode = builder.responseCode;
		responseBody = builder.responseBody;
		status = builder.status;
		state = builder.state;
		gbObject = builder.GBObject;
		exception = builder.exception;
		apiError = builder.apiError;		
	}
	
	public int getResponseCode() {
		return responseCode;
	}
	
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
	public String getResponseBody() {
		return responseBody;
	}
	
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public Request getRequest() {
		return request;
	}

	public GBObject getGBObject() {
		return gbObject;
	}

	public void setGBObject(GBObject gbObject) {
		this.gbObject = gbObject;
	}

	public HttpURLConnection getConnection() {
		return connection;
	}

	public GBException getException() {
		return exception;
	}
	
	public Response setException(GBException e) {
		this.exception = e;
		return this;
	}

	public JSONObject getState() {
		return state;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public GBAPIError getAPIError() {
		return apiError;
	}

	public void setAPIError(GBAPIError apiError) {
		this.apiError = apiError;
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP statusCode:").append(responseCode);
		
		if(responseCode == Response.HTTP_OK) {
			sb.append("\nAPI returnCode:").append(status);
			sb.append("\nGBObject state:").append((gbObject == null ? "NULL" : gbObject.toString()));
		}
		
		if(apiError != null) {		
			sb.append("\nErrorType:").append(apiError.getErrorType());
			sb.append("\nErrorCode:").append(apiError.getErrorCode());
		}
		
		if(exception != null)
			sb.append("\nException:").append(exception);
		
		return sb.toString();
	}
	
	public static String getArrangeHeaders(Map<String, List<String>> headers) {
		
		StringBuilder sb = new StringBuilder();
		Set<String> keys = headers.keySet();
		
		for(String key : keys) {
			if(key == null) continue;
			
			sb.append(key).append(":");	
			List<String> values = headers.get(key);
			for(String value : values) {
				if(value == null) continue;
				sb.append(value);
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
