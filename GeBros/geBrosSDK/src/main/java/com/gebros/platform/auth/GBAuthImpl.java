package com.gebros.platform.auth;

import com.gebros.platform.GBSettings;
import com.gebros.platform.auth.model.GBFriends;
import com.gebros.platform.auth.model.GBProfile;
import com.gebros.platform.auth.model.GBToken;
import com.gebros.platform.auth.model.GBUsers;
import com.gebros.platform.auth.model.common.GBAPIError;
import com.gebros.platform.auth.model.common.GBObject;
import com.gebros.platform.auth.net.GBAppRequest;
import com.gebros.platform.auth.net.GBAppResponse;
import com.gebros.platform.auth.net.Request;
import com.gebros.platform.auth.net.Response;
import com.gebros.platform.auth.ui.GBContentsAPI;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.listener.GBFriendsListener;
import com.gebros.platform.listener.GBProfileListener;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.platform.IPlatformClient;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBDeviceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nairs77 on 5/9/16.
 */
final class GBAuthImpl {

    private static final String TAG = GBAuthImpl.class.getCanonicalName();

    private static final String LOGIN_TYPE_PARAM_KEY = "login_type";
    private static final String MCC_PARAM_KEY = "mcc";
    private static final String LOGIN_SNS_KEY = "sns_key";
    private static final String LOGIN_SNS_ACCESS_TOKEN = "sns_access_token";
    private static final String LOGIN_ID_PARAM_KEY = "uid";
    private static final String LOGIN_BY_MARKET_TYPE = "market";
    private static final String JOIN_EMAIL_PARAM_KEY = "email";
    private static final String PW_PARAM_KEY = "pw";
    private static final String JOIN_DEVICE_COLLECT_STATE = "device_collect_state";

    private static IPlatformClient mClient;

    public void initialize(IPlatformClient client) {
        mClient = client;

        GBSessionProxy.getInstance().loadSession();
    }

    public IPlatformClient getPlatformClient() {
        return mClient;
    }

    public void requestWithAuthType(final PlatformType.AuthType authType, String accessToken, String uID, final GBAuthListener listener) {
        authorize(authType, accessToken, uID, listener);
    }

    public void requestLogout(final GBAuthListener listener) {
        Request request = GBRequest.getAbstractRequest(Request.Method.GET,
                GBSession.getActiveSession(),
                GBAccountAPI.DISCONNECT_URI,
                new ObjectCallback<GBObject>() {
                    @Override
                    public void onComplete(GBObject joypleObject, Response response) {
                        GBSession newSession = GBSession.clearSession();

                        GBSession.getActiveSession().setCurrentActiveSession(newSession);
                        listener.onSuccess(newSession);

                    }

                    @Override
                    public void onError(Response response) {
                        listener.onFail(handleException(response));
                    }
                });

        GBRequest.requestAPI(request);

    }

    public void requestDeleteAccount(final GBAuthListener listener) {
        Request request = GBRequest.getAbstractRequest(Request.Method.GET,
                GBSession.getActiveSession(),
                GBAccountAPI.WITHDRAW_URI,
                new ObjectCallback<GBObject>() {
                    @Override
                    public void onComplete(GBObject joypleObject, Response response) {

                        GBSession.getActiveSession().setCurrentActiveSession(GBSession.clearSession());
                        listener.onSuccess(GBSession.clearSession());
                        GBLog.d("GB DeleteAccount onComplete");
                    }

                    @Override
                    public void onError(Response response) {
                        listener.onFail(handleException(response));
                    }
                });

        GBRequest.requestAPI(request);
    }

    public void autoLogin(final GBAuthListener listener) {
        Map<String, Object> accountInfo = new HashMap<String, Object>();
        accountInfo.put(MCC_PARAM_KEY, GBDeviceUtils.getMcc());

        final SessionJoinSource source = GBSession.getActiveSession().getSource();
        accountInfo.put(LOGIN_TYPE_PARAM_KEY, GBSession.getActiveSession().getSource());
        accountInfo.put(LOGIN_BY_MARKET_TYPE, GBSettings.getMarket().getMarketType());

        final Request request = GBRequest.getAuthorizationRequest(GBAccountAPI.REISSUED_URI, accountInfo, new ObjectCallback<GBToken>() {

            @Override
            public void onComplete(GBToken tokens, Response response) {
                GBSession newSession = doUpdateSession(tokens, source);
                listener.onSuccess(newSession);
                GBLog.d("token = %s", newSession.getAccessToken());
            }

            @Override
            public void onError(Response response) {
                doExpireSession();
                listener.onFail(handleException(response));
            }

        });

        GBLog.i(TAG + " authorizationCallback !!!!!!!!!!!!!!!!!!!!!!!!!!"+GBSession.getActiveSession().getSource());
        GBRequest.requestAPI(request);
    }

    public void authorize(final PlatformType.AuthType authType, String accessToken, String uID, final GBAuthListener listener) {
        Map<String, Object> accountInfo = new HashMap<String, Object>();
        accountInfo.put(LOGIN_SNS_ACCESS_TOKEN, accessToken);
        accountInfo.put(MCC_PARAM_KEY, GBDeviceUtils.getMcc());
        accountInfo.put(LOGIN_ID_PARAM_KEY, uID);
        accountInfo.put(LOGIN_TYPE_PARAM_KEY, authType.getLoginType());
        accountInfo.put(LOGIN_BY_MARKET_TYPE, GBSettings.getMarket().getMarketType());

        authorize(authType, accountInfo, listener);
    }

    public void authorize(final PlatformType.AuthType authType, Map<String, Object> accountInfo, final GBAuthListener listener) {
        Request request = GBRequest.getAuthorizationRequest(GBAccountAPI.AUTHENTICATION_URI, accountInfo, new ObjectCallback<GBToken>() {

            @Override
            public void onComplete(GBToken tokens, Response response) {
                GBSession newSession = doUpdateSession(tokens, SessionJoinSource.valueOf(authType.getLoginType()));
                listener.onSuccess(newSession);
                GBLog.d("token = %s", newSession.getAccessToken());
            }

            @Override
            public void onError(Response response) {
                //handleException(response);
                //GBLog.d("error code = %d", response.getAPIError().getErrorCode());
                doExpireSession();
                listener.onFail(handleException(response));
            }

        });
        GBLog.i(TAG + " authorizationCallback !!!!!!!!!!!!!!!!!!!!!!!!!!"+GBSession.getActiveSession().getSource());
        GBLog.i(TAG + " authorizationCallback authType!!!!!!!!!!!!!!!!!!!!!!!!!!"+authType.toString());
        GBRequest.requestAPI(request);
    }

    //- ProfileApi
    public void requestProfile(final GBProfileListener listener) {

        Request request = GBRequest.getAbstractRequest(Request.Method.GET,
                GBSession.getActiveSession(),
                GBContentsAPI.PROFILE_URI,
                new ObjectCallback<GBProfile>() {
                    @Override
                    public void onComplete(GBProfile joypleObject, Response response) {
                        listener.onSuccess(joypleObject.getInnerJSONObject());
                    }

                    @Override
                    public void onError(Response response) {
                        listener.onFail(handleException(response));
                    }
                }, GBProfile.class);

        request.appendParam("scope", "user_info, devices");
        request.appendParam("udid", GBDeviceUtils.getDeviceId());

        GBRequest.requestAPI(request);
    }

    // - Friends
    public void requestFriends(final GBFriendsListener listener) {
        Request request = GBRequest.getAbstractRequest(Request.Method.GET,
                GBSession.getActiveSession(),
                GBContentsAPI.FRIENDS_URI,
                new ObjectCallback<GBFriends>() {

                    @Override
                    public void onComplete(GBFriends joypleObject, Response response) {
                        //receiver.onSuccessEvent(GBEvent.FRIENDS, joypleObject.getInnerJSONObject());
                        listener.onSuccess(joypleObject.getInnerJSONObject());
                    }

                    @Override
                    public void onError(Response response) {

                        GBAPIError apiError = response.getAPIError();
                        int errorCode = apiError.getErrorCode();
                        String errorType = apiError.getErrorType();

                        //receiver.onFailedEvent(GBEvent.FRIENDS_FAILED, errorCode, errorType, 0);
                    }

                }, GBFriends.class);

        GBRequest.requestAPI(request);
    }

    public void requestAddFriends(String friendUserKey, final GBFriendsListener listener) {
        GBAppRequest request = new GBAppRequest(GBContentsAPI.ADD_FRIENDS_URI);
        request.addParameter("f_userkey", friendUserKey);
        request.post(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response) throws JSONException {
                //receiver.onSuccessEvent(GBEvent.ADD_FRIEND, null);
                listener.onSuccess(json);
            }

            @Override
            public void onError(Response response) {

                GBAPIError apiError = response.getAPIError();
                int errorCode = apiError.getErrorCode();
                String errorType = apiError.getErrorType();

                //receiver.onFailedEvent(GBEvent.ADD_FRIEND_FAILED, errorCode, errorType, 0);
                //TODO : Error Handling
                listener.onFail(null);
            }
        });
    }

    public void requestUpdateFriendsStatus(String friendsUserKey, int status, final GBFriendsListener listener) {

        GBAppRequest request = new GBAppRequest(GBContentsAPI.UPDATE_FRIENDS_STATUS_URI);
        request.addParameter("f_userkey", friendsUserKey);
        request.addParameter("f_status", status);
        request.post(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response) throws JSONException {
                //receiver.onSuccessEvent(GBEvent.UPDATE_FRIEND_STATUS, null);
                listener.onSuccess(json);
            }

            @Override
            public void onError(Response response) {

                GBAPIError apiError = response.getAPIError();
                int errorCode = apiError.getErrorCode();
                String errorType = apiError.getErrorType();

                //receiver.onFailedEvent(GBEvent.UPDATE_FRIEND_STATUS_FAILED, errorCode, errorType, 0);
                //TODO : Error Handling
                listener.onFail(null);
            }
        });
    }

    public void requestInvitedUserCount(final GBFriendsListener listener) {

        GBAppRequest request = new GBAppRequest(GBContentsAPI.INVITED_USER_COUNT);
        request.get(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response) throws JSONException {
                //receiver.onSuccessEvent(GBEvent.INVITED_USERS_COUNT, json);
                listener.onSuccess(json);
            }

            @Override
            public void onError(Response response) {

                GBAPIError apiError = response.getAPIError();
                int errorCode = apiError.getErrorCode();
                String errorType = apiError.getErrorType();

                //receiver.onFailedEvent(GBEvent.INVITED_USERS_COUNT_FAILED, errorCode, errorType, 0);
                //TODO : Error Handling
                listener.onFail(null);
            }
        });
    }

    public void requestSearchedUsers(String nickName, final GBFriendsListener listener) {

        Request request = GBRequest.getAbstractRequest(Request.Method.GET, GBSession.getActiveSession(), GBContentsAPI.SEARCH_FRIENDS_URI,
                new ObjectCallback<GBUsers>() {

                    @Override
                    public void onComplete(GBUsers joypleObject, Response response) {
                        //receiver.onSuccessEvent(GBEvent.SEARCH_USERS, joypleObject.getInnerJSONObject());
                        listener.onSuccess(joypleObject.getInnerJSONObject());
                    }

                    @Override
                    public void onError(Response response) {

                        GBAPIError apiError = response.getAPIError();
                        int errorCode = apiError.getErrorCode();
                        String errorType = apiError.getErrorType();

                        //receiver.onFailedEvent(GBEvent.SEARCH_USERS_FAILED, errorCode, errorType, 0);
                        //TODO : Error Handling
                        listener.onFail(null);
                    }

                }, GBUsers.class);

        request.appendParam("nickname", nickName);

        GBRequest.requestAPI(request);
    }



    private GBSession doUpdateSession(GBToken tokens, SessionJoinSource source) {
        GBSession newSession = new GBSession(tokens.getAccessToken(),
                tokens.getRefreshToken(),
                source,
                new Date(),
                GBSession.SessionState.OPEN);

        GBSession.getActiveSession().setCurrentActiveSession(newSession);

        return newSession;
    }

    private void doExpireSession() {
        GBSession.getActiveSession().setCurrentActiveSession(GBSession.clearSession());
    }

    private GBException handleException(Response response) {
        GBAPIError apiError = response.getAPIError();
        GBException GBException = response.getException();

        if(apiError == null) {

            /**
             * ClientError : IOException / SocketTimeoutException
             */

            apiError = GBException.getApiErrorTemplateGBObject(GBExceptionType.BAD_REQUEST);
            GBException.setAPIError(apiError);

        } else {

            /**
             * Authorization Error
             */

            GBException.setAPIError(apiError);
        }

        return GBException;
    }
}
