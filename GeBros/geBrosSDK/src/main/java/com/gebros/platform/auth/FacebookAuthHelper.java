package com.gebros.platform.auth;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.accountkit.AccessToken;
import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.log.GBLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by nairs77 on 2017. 2. 3..
 */

public class FacebookAuthHelper extends AuthHelper {

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

    //private CallbackManager mCallbackManager;

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

        CallbackManager callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(activity, PERMISSIONS);
        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);
    }

    @Override
    public void loginWithAccountInfo(Activity activity, Map<String, Object> accountInfo, GBAuthListener listener) {
    }

    @Override
    public void logout(GBAuthListener listener) {
    }

    @Override
    public AuthType getAuthType() {
        return AuthType.FACEBOOK;
    }

    /**
     * Facebook callback
     */
    private final FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            GBLog.d(TAG + "Facebook login success.");
            mAccessToken = loginResult.getAccessToken();
            getUserInfo(mAccessToken);

        }

        @Override
        public void onError(FacebookException exception) {
            GBLog.e(exception, TAG + ". Facebook error : %s", exception.getMessage());

//            Joyple.getInstance().hideProgress();
//            joypleStatusCallback.callback(JoypleSession.getActiveSession(), State.ACCESS_FAILED, JoypleException.getJoypleExceptionTemplate(JoypleExceptionType.FACEBOOK_ERROR));
        }

        @Override
        public void onCancel() {
            GBLog.d(TAG + ". Facebook User Cancel");

//            Joyple.getInstance().hideProgress();
//            joypleStatusCallback.callback(JoypleSession.getActiveSession(), State.ACCESS_FAILED, JoypleException.getJoypleExceptionTemplate(JoypleExceptionType.FACEBOOK_USER_CANCELED));
        }
    };

    public void getUserInfo(AccessToken token){

        GraphRequest request = GraphRequest.newMeRequest(token, new GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                if(object != null)
                    GBLog.d(" facebook object : %s", object.toString());

                FacebookRequestError requestError = response.getError();
                if(requestError != null){
                    GBLog.e(requestError.getException(), TAG + ". Facebook error : %s", requestError.getErrorMessage());

//                    expires();

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
}
