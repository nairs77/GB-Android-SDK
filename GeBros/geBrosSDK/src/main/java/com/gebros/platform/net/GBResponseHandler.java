package com.gebros.platform.net;

import com.gebros.platform.event.GBEvent;
import com.gebros.platform.event.GBEventReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gebros.nairs77@gmail.com on 5/13/16.
 */
public class GBResponseHandler implements ResponseListener<JSONObject> {


    private GBEvent[] pairEvent;
    private GBEventReceiver receiver;

    public GBResponseHandler(GBEvent[] pairEvent, GBEventReceiver receiver) {
        this.pairEvent = pairEvent;
        this.receiver = receiver;
    }

    @Override
    public void onComplete(JSONObject result) {
        int apiStatus = result.optInt("status");
        if(apiStatus == API_FAILED) {
            int errorCode = result.optJSONObject("error").optInt("errorCode");
            String errorMessage = result.optJSONObject("error").optString("errorType");
            receiver.onFailedEvent(pairEvent[FAILED_EVENT], errorCode, errorMessage);
        } else if(apiStatus == API_SUCCESS) {
            try {
                receiver.onSuccessEvent(pairEvent[SUCCESS_EVENT], result.getJSONObject("result"));
                // Broadcast event
                //JoycityServiceHandler.getInstance().broadcastEvent(pairEvent[SUCCESS_EVENT], result.getJSONObject("result"));
            } catch (JSONException e) {
                receiver.onFailedEvent(pairEvent[FAILED_EVENT], 0, e.getMessage());
            }
        }
/*
        int apiStatus = result.optInt("status");

        if (apiStatus == API_SUCCESS) {
            receiver.onEventResult(new GBEventResult(result.getJSONObject("result"));
        }
*/

    }

    @Override
    public void onException(OkHttpException e) {
        receiver.onFailedEvent(pairEvent[FAILED_EVENT], API_FAILED, e.getMessage());
    }

}
