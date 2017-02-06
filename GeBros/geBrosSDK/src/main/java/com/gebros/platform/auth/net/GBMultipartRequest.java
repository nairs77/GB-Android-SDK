package com.gebros.platform.auth.net;

import com.gebros.platform.auth.GBRequest;
import com.gebros.platform.auth.GBSession;
import com.gebros.platform.auth.ObjectCallback;
import com.gebros.platform.auth.ResponseCallback;
import com.gebros.platform.auth.model.common.GBObject;
import com.gebros.platform.exception.GBRuntimeException;
import com.gebros.platform.util.GBValidator;

import org.json.JSONException;


public class GBMultipartRequest extends GBAppRequest {

	private String url;
	private String fileName;
	
	public GBMultipartRequest(String url, String fileName) {
		
		this.url = url;
		this.fileName = fileName;
	}

	protected void request(Request.Method method, final GBAppResponse appResponse) {
		
		final ObjectCallback<GBObject> wrappedCallback = new ObjectCallback<GBObject>() {

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
		
		MultipartRequest request = new MultipartRequest(method, GBSession.getActiveSession(), url, new ResponseCallback() {
			
			@Override
			public void onError(Response response) {
				wrappedCallback.onError(response);
			}
			
			@Override
			public void onComplete(Response response) {
				wrappedCallback.onComplete(response.getGBObject(), response);
			}
		});
		
		request.setRequestType(requestType);
		request.setBodyType(getBodyType());
		request.setFileName(fileName);
		
		if(!GBValidator.isNullOrEmpty(parameters))
			request.setParams(parameters);
		
		GBRequest.requestAPI(request);
	}
}
