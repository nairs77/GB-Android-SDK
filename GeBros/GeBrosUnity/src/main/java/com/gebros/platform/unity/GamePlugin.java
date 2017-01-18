package com.gebros.platform.unity;

import com.joycity.platform.sdk.exception.JoypleException;
import com.joycity.platform.sdk.game.JoypleGameManager;
import com.joycity.platform.sdk.listener.JoypleGameListener;
import com.joycity.platform.sdk.log.JLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jce_platform on 2016. 7. 22..
 */
public final class GamePlugin extends BasePlugin {

    private static final String TAG = "[GamePlugin]";

    private static final class GamePluginHolder {
        public static final GamePlugin instance = new GamePlugin();
    }

    public static GamePlugin getInstance() {
        return GamePluginHolder.instance;
    }

    public static void SubmitExtendData(String extraData, String gameObjectName) {
        JoypleGameManager.SubmitExtendDataGameService(extraData, null);
    }

    public static void GameExit(String gameObjectName) {
        GamePlugin.getInstance().GameExitCallback(gameObjectName);
    }

    private void GameExitCallback(final String callbackObjectName) {
        callbackObjectNames.put(callbackObjectName, callbackObjectName);

        JoypleGameManager.GameExitService(getActivity(), new JoypleGameListener() {
                    @Override
                    public void onSuccess() {
                        JSONObject response = new JSONObject();
                        JSONObject event_response = new JSONObject();
                        try {
                            event_response.put(DATA_KEY, "gameExitSuccess");
                            response.put(RESULT_KEY, event_response);
                        } catch (JSONException e) {
                            JLog.d(TAG + "JSONException = %s", e.getMessage());
                        }

                        SendUnityMessage(callbackObjectNames.remove(callbackObjectName), ASYNC_RESULT_SUCCESS, response.toString());
                    }

                    @Override
                    public void onFail(JoypleException exception) {
                        JSONObject response = new JSONObject();
                        JSONObject error_response = new JSONObject();

                        try {
                            error_response.put(DATA_KEY, -8891);

                            response.put(RESULT_KEY, error_response);
                        } catch (JSONException e) {
                            JLog.d(TAG + "JSONException = %s", e.getMessage());
                        }

                        SendUnityMessage(callbackObjectNames.remove(callbackObjectName), ASYNC_RESULT_FAIL, response.toString());
                    }
                }

        );
    }


}
