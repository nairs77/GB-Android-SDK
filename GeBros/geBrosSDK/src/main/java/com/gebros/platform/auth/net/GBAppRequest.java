package com.gebros.platform.auth.net;

import com.gebros.platform.auth.GBRequest;
import com.gebros.platform.auth.GBSession;
import com.gebros.platform.auth.ObjectCallback;
import com.gebros.platform.auth.model.common.GBObject;
import com.gebros.platform.exception.GBRuntimeException;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.util.GBValidator;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GBAppRequest {

	protected final String TAG = GBAppRequest.class.getCanonicalName() + ":";
	
	protected Request.RequestType requestType = Request.RequestType.API;
	protected Request.BodyType bodyType = Request.BodyType.PARAMETER;
	
	protected String url;
	protected Map<String, Object> parameters = new HashMap<String, Object>();
	
	public GBAppRequest() {}
	
	public GBAppRequest(String url) {
		this.url = url;
	}
	
	public void setParameter(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	public void addParameter(String key, Object value) {
		parameters.put(key, value);
	}
	
	public void setRequestType(Request.RequestType requestType) {
		this.requestType = requestType;
	}
	
	public void get(final GBAppResponse appResponse) {
		request(Request.Method.GET, appResponse);
		GBLog.d(TAG + "AppRequest be executed by GET method");
		
	}
	
	public void post(final GBAppResponse appResponse) {
		request(Request.Method.POST, appResponse);
		GBLog.d(TAG + "AppRequest be executed by POST method");
	}
	
	protected void request(Request.Method method, final GBAppResponse appResponse) {
		ObjectCallback<GBObject> wrappedCallback = new ObjectCallback<GBObject>() {

			@Override
			public void onComplete(GBObject jbObject, Response response) {
				
				try {
					
					appResponse.onComplete(jbObject.getInnerJSONObject().optJSONObject(Response.API_RESULT_KEY), response);

				} catch (JSONException e) {
					throw new GBRuntimeException(TAG  + "JSON Parse Error.");
				}
				
			}

			@Override
			public void onError(Response response) {
				appResponse.onError(response);
			}
		};
		
		Request request = GBRequest.getAbstractRequest(method, GBSession.getActiveSession(), url, wrappedCallback);
		request.setRequestType(requestType);
		request.setBodyType(getBodyType());
		
		if(!GBValidator.isNullOrEmpty(parameters))
			request.setParams(parameters);
		
		GBRequest.requestAPI(request);
	}

	public Request.BodyType getBodyType() {
		return bodyType;
	}

	public void setBodyType(Request.BodyType bodyType) {
		this.bodyType = bodyType;
	}	
}
