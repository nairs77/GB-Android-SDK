package com.gebros.platform.event;

import org.json.JSONObject;

public interface GBEventReceiver {
	abstract void onSuccessEvent(GBEvent event, JSONObject json);
	abstract void onFailedEvent(GBEvent event, int errorCode, String errorMessage);
}
