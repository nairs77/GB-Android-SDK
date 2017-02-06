package com.gebros.platform.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.gebros.platform.concurrent.ISimpleAsyncTask;
import com.gebros.platform.concurrent.SimpleAsyncTask;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.log.GBLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.gebros.platform.auth.GBAuthImpl.CHANNEL_ID_KEY;

/**
 * Created by nairs77 on 2017. 2. 3..
 */

class FacebookAuthHelper extends AuthHelper {

    private static final String TAG = FacebookAuthHelper.class.getCanonicalName();

    private static final String PERMISSIONS_PUBLIC_PROFILE = "public_profile";
    private static final String PERMISSIONS_EMAIL = "email";
    private static final String PERMISSIONS_USER_FRIENDS = "user_friends";


    protected static List<String> PERMISSIONS = new ArrayList<String>();
    private int mServiceType;
    private Activity mActivity;

    public AccessToken mAccessToken;
    //public static String facebookToken;
    private GBAuthListener mListener;

    private CallbackManager mCallbackManager;

    static {
        PERMISSIONS.add(PERMISSIONS_PUBLIC_PROFILE);
        PERMISSIONS.add(PERMISSIONS_EMAIL);
        PERMISSIONS.add(PERMISSIONS_USER_FRIENDS);
    }

    public FacebookAuthHelper(GBAuthImpl authImpl) {
        super(authImpl);
    }

    @Override
    public void login(Activity activity, GBAuthListener listener) {

        if (listener != null)
            mListener = listener;

        GBSession activeSession = GBSession.getActiveSession();

        if (activeSession.getState() == GBSession.SessionState.NONE) {
            mCallbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().logInWithReadPermissions(activity, PERMISSIONS);
            LoginManager.getInstance().registerCallback(mCallbackManager, facebookCallback);

        } else if (activeSession.getState() == GBSession.SessionState.READY) {
            Map<String, Object> accountInfo = new HashMap<String, Object>();
            accountInfo.put(CHANNEL_ID_KEY, activeSession.getUserInfo());
            loginWithAccountInfo(activity, accountInfo, listener);
        }
    }

    @Override
    public void loginWithAccountInfo(Activity activity, final Map<String, Object> accountInfo, GBAuthListener listener) {
        SimpleAsyncTask.doRunUIThread(new ISimpleAsyncTask.OnUIThreadTask() {
            @Override
            public void doRunUIThread() {
                mImpl.authorize(getAuthType(), "", (String)accountInfo.get(CHANNEL_ID_KEY), mListener);
            }
        });
    }

    @Override
    public void logout(GBAuthListener listener) {
    }

    @Override
    public AuthType getAuthType() {
        return AuthType.FACEBOOK;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Facebook callback
     */
    private final FacebookCallback<com.facebook.login.LoginResult> facebookCallback = new FacebookCallback<com.facebook.login.LoginResult>() {

        @Override
        public void onSuccess(com.facebook.login.LoginResult loginResult) {
            GBLog.d(TAG + "Facebook login success.");
            mAccessToken = loginResult.getAccessToken();
            getUserInfo(mAccessToken);

        }

        @Override
        public void onError(FacebookException exception) {
            GBLog.e(exception, TAG + ". Facebook error : %s", exception.getMessage());

//            Joyple.getInstance().hideProgress();
//            joypleStatusCallback.callback(JoypleSession.getActiveSession(), State.ACCESS_FAILED, JoypleException.getJoypleExceptionTemplate(JoypleExceptionType.FACEBOOK_ERROR));

            mListener.onFail(GBException.getGBExceptionTemplate(GBExceptionType.FACEBOOK_ERROR));
        }

        @Override
        public void onCancel() {
            GBLog.d(TAG + ". Facebook User Cancel");

//            Joyple.getInstance().hideProgress();
//            joypleStatusCallback.callback(JoypleSession.getActiveSession(), State.ACCESS_FAILED, JoypleException.getJoypleExceptionTemplate(JoypleExceptionType.FACEBOOK_USER_CANCELED));
            mListener.onCancel(true);
        }
    };

    public void getUserInfo(final AccessToken token){

        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                if(object != null)
                    GBLog.d(" facebook object : %s", object.toString());

                FacebookRequestError requestError = response.getError();
                if(requestError != null){
                    GBLog.e(requestError.getException(), TAG + ". Facebook error : %s", requestError.getErrorMessage());

                    _expires();

//                    Joyple.getInstance().hideProgress();
//                    joypleStatusCallback.callback(JoypleSession.getActiveSession(), State.ACCESS_FAILED, JoypleException.getJoypleExceptionTemplate(JoypleExceptionType.FACEBOOK_ERROR));

                    return;
                }

                String email = "";
                try {
                    email = object.getString("email");
                } catch (JSONException e) {
                    email = "";
                } finally {
//                    if (mServiceType == Joyple.AUTH_CONNECT_TYPE)
//                        thirdPartyAccountLink(AuthType.FACEBOOK.getLoginType(), accessToken.getToken(), null, null);
//                    else
//                        checkAccount(mActivity, AuthType.FACEBOOK, accessToken.getUserId(), email);

                    SimpleAsyncTask.doRunUIThread(new ISimpleAsyncTask.OnUIThreadTask() {
                        @Override
                        public void doRunUIThread() {
                            mImpl.authorize(getAuthType(), token.getToken(), token.getUserId(), mListener);
                        }
                    });

                }
            }
        });

        Bundle paramters = new Bundle();
        paramters.putString("fields", "id, email");

        request.setParameters(paramters);
        request.executeAsync();

    }

    private boolean _isNeedPermission(Set<String> permissions){

        boolean isNeedPermission = true;

        int count = PERMISSIONS.size();
        for(String permission : permissions){
            for(String needPermission : PERMISSIONS){
                if(permission.equals(needPermission)){
                    count--;
                    break;
                }
            }
        }

        if(count == 0)
            isNeedPermission = false;

        return isNeedPermission;
    }

    private void _expires() {
        LoginManager.getInstance().logOut();
    }
}
