package com.gebros.platform.auth.net;

import org.json.JSONException;
import org.json.JSONObject;


public interface GBAppResponse {

	/**
	 * JoypleObject callback for app resource
	 * 
	 */
	
	abstract public void onComplete(JSONObject json, Response response) throws JSONException;
	abstract public void onError(Response response);
}
