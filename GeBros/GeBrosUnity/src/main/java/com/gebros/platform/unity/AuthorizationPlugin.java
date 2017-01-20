package com.gebros.platform.unity;

import com.gebros.platform.Joyple;
import com.gebros.platform.JoypleSettings;
import com.gebros.platform.auth.JoypleAuthManager;
import com.gebros.platform.auth.GBSession;
import com.gebros.platform.auth.ProfileApi;
import com.gebros.platform.auth.ui.JoypleProfileViewType;
import com.gebros.platform.auth.ui.common.JoypleViewEventListener;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.listener.JoypleAuthListener;
import com.gebros.platform.listener.JoypleProfileListener;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBValidator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nairs77@joycity.com on 6/15/16.
 */

public final class AuthorizationPlugin extends BasePlugin {

    private static final String TAG = "[AuthorizePlugin]";

    static final String SESSION_KEY = "state";
    private String SessionGameObject;

    private static final class AuthorizationPluginHolder {
        public static final AuthorizationPlugin instance = new AuthorizationPlugin();
    }

    public static AuthorizationPlugin getInstance() {
        return AuthorizationPluginHolder.instance;
    }

    public static boolean hasToken() {
        return !GBValidator.isNullOrEmpty(GBSession.getActiveSession().getAccessToken());
    }

    public static void setAllowedEULA(boolean isAllowed) {
        //Joyple.getInstance().setAllowedEULA(isAllowed);
    }

    public static boolean isOpened() {
        if (GBSession.getActiveSession() != null)
            return GBSession.getActiveSession().isOpened();
        else
            return false;
    }

    public static boolean isAllowedEULA() {
        //return Joyple.getInstance().isAllowedEULA();
        return true;
    }

    public static boolean isAlreadyLogin() {
        //return Joyple.getInstance().isAlreadyLogin();
        return true;
    }

    public static String getAccessToken() {
        if (GBSession.getActiveSession() != null)
            return GBSession.getActiveSession().getAccessToken();
        else
            return "";
    }

    public static String getRefreshToken() {
        if (GBSession.getActiveSession() != null)
            return GBSession.getActiveSession().getRefreshToken();
        else
            return "";
    }

    public static void Login(String gameObjectName) {
        GBLog.d(TAG + "Login API - %s", gameObjectName);
        AuthorizationPlugin.getInstance().loginWithCallback(gameObjectName);
    }

    public static void setGameLanguage(int languageType) {
        JoypleSettings.GameLanguageType type = JoypleSettings.GameLanguageType.valueOf(languageType);
        Joyple.SetGameLanguage(type);
    }

    public static void LoginWithType(int authType, String gameObjectName) {
        PlatformType.AuthType type = PlatformType.AuthType.valueOf(authType);
        AuthorizationPlugin.getInstance().loginWithCallback(type, gameObjectName);
    }
/*
    public static void LoginByUI(String gameObjectName) {
        AuthorizationPlugin.getInstance().loginByUI(gameObjectName);
    }

    public static void LinkServiceWithAuthType(int authType, String gameObjectName) {
        AuthType type = AuthType.valueOf(authType);
        AuthorizationPlugin.getInstance().linkServiceWithCallback(type, gameObjectName);
    }
*/
    public static void Logout(String gameObjectName) {
        AuthorizationPlugin.getInstance().logoutWithCallback(gameObjectName);
    }

//    public static void DeleteAccount(String gameObjectName) {
//        AuthorizationPlugin.getInstance().withdrawWithCallback(gameObjectName);
//    }
//
    public static void RequestProfile(String gameObjectName) {
        AuthorizationPlugin.getInstance().requestProfileWithCallback(gameObjectName);
    }
//
//    public static void ShowJoypleMain() {
//        AuthorizationPlugin.getInstance().showMain();
//    }
//
    public static void ShowClickWrap(String gameObjectName) {
        AuthorizationPlugin.getInstance().showClickWrap(gameObjectName);
    }
/*
    public static void HideJoypleStart() {
        Joyple.getInstance().hideJoypleStart();
    }

    public static void ShowEULA() {
        AuthorizationPlugin.getInstance().showEULA();
    }
*/
    public static void ShowViewByType(int viewType) {
        //AuthorizationPlugin.getInstance().
        JoypleProfileViewType type = JoypleProfileViewType.valueOf(viewType);

        AuthorizationPlugin.getInstance().showProfile(type);
    }
/*
    public static void RequestMergeAccount(String userkey, String gameObjectName) {
        AuthorizationPlugin.getInstance().RequestMergeAccountWithCallback(userkey, gameObjectName);
    }
*/
    private void loginWithCallback(String callbackObjectName) {
        //callbackObjectNames.add(callbackObjectName);
        SessionGameObject = callbackObjectName;
        callbackObjectNames.put(callbackObjectName, callbackObjectName);

        JoypleAuthManager.Login(getActivity(), mAuthListener);
    }


    private void loginWithCallback(PlatformType.AuthType authType, String callbackObjectName) {
        //callbackObjectNames.add(callbackObjectName);
        SessionGameObject = callbackObjectName;
        callbackObjectNames.put(callbackObjectName, callbackObjectName);

        try {
            JoypleAuthManager.LoginWithAuthType(authType, getActivity(), mAuthListener);
        } catch (GBException e) {
            //TODO : Processing Exception
            e.printStackTrace();
        }
    }
/*
    private void linkServiceWithCallback(AuthType authType, String callbackObjectName) {
        this.callbackObjectName = callbackObjectName;
        Joyple.getInstance().linkServiceWithAuthType(authType, thirdConnectServiceCallback);
    }
    private void loginByUI(String callbackObjectName) {
        this.callbackObjectName = callbackObjectName;
        Joyple.getInstance().loginByUI(UnityPlayer.currentActivity, joypleStatusCallback);
    }
*/
    private void logoutWithCallback(String callbackObjectName) {
        //callbackObjectNames.add(callbackObjectName);
        SessionGameObject = callbackObjectName;
        callbackObjectNames.put(callbackObjectName, callbackObjectName);

        JoypleAuthManager.Logout(getActivity(), mAuthListener);
    }
/*
    private void withdrawWithCallback(String callbackObjectName) {
        this.callbackObjectName = callbackObjectName;
        Joyple.getInstance().withdraw();

    }
*/
    private void requestProfileWithCallback(final String callbackObjectName) {
        //callbackObjectNames.add(callbackObjectName);
        callbackObjectNames.put(callbackObjectName, callbackObjectName);

        ProfileApi.RequestProfile(new JoypleProfileListener() {
            @Override
            public void onSuccess(JSONObject result) {
                JSONObject response = new JSONObject();
                JSONObject event_response = new JSONObject();
                try {
                    event_response.put(DATA_KEY, result);
                    response.put(RESULT_KEY, event_response);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(callbackObjectName), ASYNC_RESULT_SUCCESS, response.toString());
            }

            @Override
            public void onFail(GBException ex) {
                JSONObject response = new JSONObject();
                JSONObject event_response = new JSONObject();
                JSONObject error_response = new JSONObject();

                try {
                    error_response.put(API_RESPONSE_ERROR_CODE_KEY, ex.getErrorCode());
                    error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, ex.getMessage());

                    event_response.put(ERROR_KEY, error_response);
                    response.put(RESULT_KEY, event_response);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(callbackObjectName), ASYNC_RESULT_FAIL, response.toString());
            }
        });
    }
/*
    private void RequestMergeAccountWithCallback(String userkey, String callbackObjectName) {
        this.callbackObjectName = callbackObjectName;
        Joyple.getInstance().requestMergeAccount(userkey, new JoycityEventReceiver() {

            @Override
            public void onSuccessEvent(JoycityEvent event, JSONObject json) {
                Logger.d(TAG + "requestMergeAccount onSuccessEvent event = %s, json = %s", event, json);

                JSONObject response = new JSONObject();
                JSONObject result_response = new JSONObject();

                try {
                    response.put("status", 1);
                    response.put("eventKey", "OnDuplicateAccountFinished");
                    response.put("data",  json);
                    result_response.put("result", response);
                } catch (JSONException e) {
                    Logger.d(TAG + "JSONException = %s", e.getMessage());
                }

                UnityPlayer.UnitySendMessage(getCallbackObjectName(), ASYNC_CALL_SUCCEEDED, result_response.toString());
            }

            @Override
            public void onFailedEvent(JoycityEvent event, int errorCode,
                                      String errorMessage) {
                Logger.d(TAG + "requestMergeAccount onFailedEvent event = %s, errorCode = %d, errorMessage = %s",
                        event, errorCode, errorMessage);
                JSONObject error_response = new JSONObject();
                JSONObject response = new JSONObject();
                JSONObject result_response = new JSONObject();

                try {
                    response.put("status", 0);
                    response.put("eventKey", "OnDuplicateAccountFinished");
                    error_response.put("error_code", errorCode);
                    error_response.put("error_message", errorMessage);
                    response.put("error",  error_response);
                    result_response.put("result", response);
                } catch (JSONException e) {
                    Logger.d(TAG + "JSONException = %s", e.getMessage());
                }

                UnityPlayer.UnitySendMessage(getCallbackObjectName(), ASYNC_CALL_FAILED, result_response.toString());
            }
        });
    }

    private void showMain() {
        Application.main(getActivity());
    }
*/
    private void showClickWrap(final String callbackObjectName) {
        //callbackObjectNames.add(callbackObjectName);
        callbackObjectNames.put(callbackObjectName, callbackObjectName);

        GBLog.d(TAG + "Call ShowClickWrap!!!");

        JoypleAuthManager.ShowClickWrap(getActivity(), new JoypleViewEventListener() {
            @Override
            public void onReceiveEvent(JoycityViewEvent event) {
                GBLog.d(TAG + ", showClickWrap event = %s", event);///
                JSONObject eventResult = new JSONObject();

                try {
                    eventResult.put(EVENT_KEY, JoycityViewEvent.SUCCESS_AGREEMENT);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(callbackObjectName), ASYNC_RESULT_SUCCESS, eventResult.toString());
            }
        });
    }
/*
    private void showEULA() {
        Joyple.getInstance().showEULA(getActivity());
    }
*/
    private void showProfile(JoypleProfileViewType aViewType) {
        JoypleAuthManager.showProfile(getActivity(), aViewType);
    }

    private JoypleAuthListener mAuthListener = new JoypleAuthListener() {
        @Override
        public void onSuccess(GBSession newSession) {

            GBLog.d(TAG + "onSuccess - Auth Listener !!!!");
            JSONObject response = new JSONObject();
            JSONObject session_response = new JSONObject();

            try {
                session_response.put(SESSION_KEY, newSession.getState().name());
                response.put(RESULT_KEY, session_response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SendUnityMessage(callbackObjectNames.remove(SessionGameObject), ASYNC_RESULT_SUCCESS, response.toString());
        }

        @Override
        public void onFail(GBException ex) {

            GBLog.d(TAG + "onFail - Auth Listener !!!!");


            sessionErrorHandling(ex.getAPIError().getErrorCode(), ex.getAPIError().getDetailError());
/*
            JSONObject response = new JSONObject();
            JSONObject session_response = new JSONObject();
            JSONObject error_response = new JSONObject();

            try {
                error_response.put(API_RESPONSE_ERROR_CODE_KEY, ex.getAPIError().getErrorCode());
                error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, ex.getAPIError().getErrorType());
                session_response.put(ERROR_KEY, error_response);
                response.put(RESULT_KEY, session_response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SendUnityMessage(callbackObjectNames.remove(0), ASYNC_RESULT_FAIL, response.toString());
*/
        }

        @Override
        public void onCancel(boolean isUserCancelled) {
            GBLog.d(TAG + "onCancel - Auth Listener !!!!");

            sessionErrorHandling(GBExceptionType.USER_LOGIN_CANCELED.getErrorCode(), GBExceptionType.USER_LOGIN_CANCELED.getErrorMessage());
/*
            JSONObject response = new JSONObject();
            JSONObject session_response = new JSONObject();
            JSONObject error_response = new JSONObject();

            try {
                error_response.put(API_RESPONSE_ERROR_CODE_KEY, GBExceptionType.USER_LOGIN_CANCELED.getErrorCode());
                error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, GBExceptionType.USER_LOGIN_CANCELED.getErrorMessage());
                session_response.put(ERROR_KEY, error_response);
                response.put(RESULT_KEY, session_response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SendUnityMessage(callbackObjectNames.remove(0), ASYNC_RESULT_FAIL, response.toString());
*/
        }
    };

    private void sessionErrorHandling(int errorCode, String error_msg) {

        JSONObject response = new JSONObject();
        JSONObject session_response = new JSONObject();
        JSONObject error_response = new JSONObject();

        try {
            session_response.put(SESSION_KEY, GBSession.SessionState.ACCESS_FAILED.name());
            error_response.put(API_RESPONSE_ERROR_CODE_KEY, errorCode);
            error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, error_msg);
            session_response.put(ERROR_KEY, error_response);
            response.put(RESULT_KEY, session_response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SendUnityMessage(callbackObjectNames.remove(SessionGameObject), ASYNC_RESULT_FAIL, response.toString());
    }

}