package com.gebros.platform.unity;

import android.app.Activity;

import com.gebros.platform.log.GBLog;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by nairs77@joycity.com on 6/15/16.
 */
public abstract class BasePlugin {

    private static final String TAG = BasePlugin.class.getCanonicalName();

    protected static final String ASYNC_RESULT_SUCCESS = "asyncCallSucceeded";
    protected static final String ASYNC_RESULT_FAIL = "asyncCallFailed";
    protected static final String DATA_KEY = "data";
    protected static final String RESULT_KEY = "result";
    protected static final String ERROR_KEY = "error";
    protected static final String EVENT_KEY = "event";
    static final String API_RESPONSE_ERROR_CODE_KEY = "error_code";
    static final String API_RESPONSE_ERROR_MESSAGE_KEY = "error_message";

    //protected List<String> callbackObjectNames = new ArrayList<String>();
    protected Dictionary<String, String> callbackObjectNames = new Hashtable<String, String>();

    static Activity getActivity() {
        return UnityPlayer.currentActivity;
    }

    static void SendUnityMessage(String gameObjectName, String methodName, String response) {
        UnityPlayer.UnitySendMessage(gameObjectName, methodName, response);
    }

    static final void sendMessage(String methodName, String message) {
        JSONObject response = new JSONObject();
        JSONObject event_response = new JSONObject();

        try {
            event_response.put("eventKey", message);
            response.put(RESULT_KEY, event_response);
        } catch (JSONException e) {
            GBLog.d(TAG + "JSONException = %s", e.getMessage());
        }

        UnityPlayer.UnitySendMessage("GBManager", methodName, response.toString());
        GBLog.d(TAG + " = sendMessage() To Unity = %s %s", methodName, message);
    }

    static String MakeErrorResponse(int errorCode, String errorMessage) {

        JSONObject response = new JSONObject();
        JSONObject event_response = new JSONObject();
        JSONObject error_response = new JSONObject();

        try {
            error_response.put("error_code", errorCode);
            error_response.put("error_message", errorMessage);
            event_response.put("error", error_response);
            response.put("result", event_response);
        } catch (JSONException e) {
            GBLog.d(TAG + "JSONException = %s", e.getMessage());
        }

        return response.toString();
    }
}
