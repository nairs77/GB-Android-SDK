package com.gebros.platform.unity;

import com.gebros.platform.GBSdk;
import com.gebros.platform.GBSettings;
import com.gebros.platform.auth.GBAuthManager;
import com.gebros.platform.auth.GBSession;
import com.gebros.platform.auth.ProfileApi;
import com.gebros.platform.auth.ui.GBProfileViewType;
import com.gebros.platform.auth.ui.common.GBViewEventListener;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.listener.GBProfileListener;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBValidator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gebros.nairs77@gmail.com on 6/15/16.
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
        //GB.getInstance().setAllowedEULA(isAllowed);
    }

    public static boolean isOpened() {
        if (GBSession.getActiveSession() != null)
            return GBSession.getActiveSession().isOpened();
        else
            return false;
    }

    public static boolean isAllowedEULA() {
        //return GB.getInstance().isAllowedEULA();
        return true;
    }

    public static boolean isAlreadyLogin() {
        //return GB.getInstance().isAlreadyLogin();
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

//    public static void setGameLanguage(int languageType) {
//        GBSettings.GameLanguageType type = GBSettings.GameLanguageType.valueOf(languageType);
//        GBSdk.SetGameLanguage(type);
//    }

    public static void LoginWithType(int authType, String gameObjectName) {
        AuthType type = AuthType.valueOf(authType);
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
//    public static void ShowGBMain() {
//        AuthorizationPlugin.getInstance().showMain();
//    }
//
    public static void ShowClickWrap(String gameObjectName) {
        AuthorizationPlugin.getInstance().showClickWrap(gameObjectName);
    }
/*
    public static void HideGBStart() {
        GB.getInstance().hideGBStart();
    }

    public static void ShowEULA() {
        AuthorizationPlugin.getInstance().showEULA();
    }
*/
    public static void ShowViewByType(int viewType) {
        //AuthorizationPlugin.getInstance().
        GBProfileViewType type = GBProfileViewType.valueOf(viewType);

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

        GBAuthManager.Login(getActivity(), mAuthListener);
    }


    private void loginWithCallback(AuthType authType, String callbackObjectName) {
        //callbackObjectNames.add(callbackObjectName);
        SessionGameObject = callbackObjectName;
        callbackObjectNames.put(callbackObjectName, callbackObjectName);

        try {
            GBAuthManager.LoginWithAuthType(authType, getActivity(), mAuthListener);
        } catch (GBException e) {
            //TODO : Processing Exception
            e.printStackTrace();
        }
    }
/*
    private void linkServiceWithCallback(AuthType authType, String callbackObjectName) {
        this.callbackObjectName = callbackObjectName;
        GB.getInstance().linkServiceWithAuthType(authType, thirdConnectServiceCallback);
    }
    private void loginByUI(String callbackObjectName) {
        this.callbackObjectName = callbackObjectName;
        GB.getInstance().loginByUI(UnityPlayer.currentActivity, GBStatusCallback);
    }
*/
    private void logoutWithCallback(String callbackObjectName) {
        //callbackObjectNames.add(callbackObjectName);
        SessionGameObject = callbackObjectName;
        callbackObjectNames.put(callbackObjectName, callbackObjectName);

        GBAuthManager.Logout(getActivity(), mAuthListener);
    }
/*
    private void withdrawWithCallback(String callbackObjectName) {
        this.callbackObjectName = callbackObjectName;
        GB.getInstance().withdraw();

    }
*/
    private void requestProfileWithCallback(final String callbackObjectName) {
        //callbackObjectNames.add(callbackObjectName);
        callbackObjectNames.put(callbackObjectName, callbackObjectName);

        ProfileApi.RequestProfile(new GBProfileListener() {
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

    private void showClickWrap(final String callbackObjectName) {
        //callbackObjectNames.add(callbackObjectName);
        callbackObjectNames.put(callbackObjectName, callbackObjectName);

        GBLog.d(TAG + "Call ShowClickWrap!!!");

        GBAuthManager.ShowClickWrap(getActivity(), new GBViewEventListener() {
            @Override
            public void onReceiveEvent(GBViewEvent event) {
                GBLog.d(TAG + ", showClickWrap event = %s", event);///
                JSONObject eventResult = new JSONObject();

                try {
                    eventResult.put(EVENT_KEY, GBViewEvent.SUCCESS_AGREEMENT);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(callbackObjectName), ASYNC_RESULT_SUCCESS, eventResult.toString());
            }
        });
    }
/*
    private void showEULA() {
        GB.getInstance().showEULA(getActivity());
    }
*/
    private void showProfile(GBProfileViewType aViewType) {
        GBAuthManager.showProfile(getActivity(), aViewType);
    }

    private GBAuthListener mAuthListener = new GBAuthListener() {
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