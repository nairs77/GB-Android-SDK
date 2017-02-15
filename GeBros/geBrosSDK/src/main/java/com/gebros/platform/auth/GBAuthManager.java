package com.gebros.platform.auth;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.gebros.platform.ActivityResultHelper;
import com.gebros.platform.auth.ui.GBProfileViewType;
import com.gebros.platform.auth.ui.common.GBViewEventListener;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.listener.GBProfileListener;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.platform.IPlatformClient;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBDeviceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gebros.nairs77@gmail.com on 7/1/17.
 */
public class GBAuthManager {

    static GBAuthImpl mAuthClient;
    static GBAuthHelper mAuthHelper;

    public static final String TAG = GBAuthManager.class.getCanonicalName();

    /**
     * This function initializes Auth Package in GB SDK
     *
     * @param client {@link IPlatformClient} object
     */
    public static void Initialize(IPlatformClient client) {
        mAuthClient = new GBAuthImpl();
        mAuthClient.initialize(client);
    }


    /**
     * Log the user in from Authentication system (supported UI)
     *
     * @param activity  The activity which is starting the login process
     * @param listener  {@link GBAuthListener}
     */

    public static void Login(@NonNull Activity activity, final GBAuthListener listener) {

        AuthType authType = GBSession.getActiveSession().getAuthType();

        if (authType == AuthType.NONE ||
                authType == AuthType.CHINA360 ||
                authType == AuthType.BAIDU ||
                authType == AuthType.XIAOMI ||
                authType == AuthType.UC ||
                authType == AuthType.WANDOUJIA ||
                authType == AuthType.HUAWEI) {
            PlatformLogin(activity, listener);
        } else {
            AutoLogin(activity, listener);
        }
    }

    /**
     *
     * @param activity
     * @param accountInfo
     * @param listener
     * @throws GBException
     */
    public static void LoginWithAccountInfo(Activity activity, Map<String, Object> accountInfo, final GBAuthListener listener) throws GBException {
        if (mAuthHelper == null)
            mAuthHelper = IAuthHelperFactory.create(AuthType.GOOGLE, mAuthClient);

        mAuthHelper.loginWithAccountInfo(activity, accountInfo, listener);
    }


    /**
     * try to log the user in with Special Authentication Type.
     *
     * @param authType  {@link PlatformType}
     * @param activity  The activity which is starting the login process
     * @param listener  {@link GBAuthListener}
     * @throws GBException
     */

    public static void LoginWithAuthType(Activity activity, AuthType authType, final GBAuthListener listener) {

        mAuthHelper = IAuthHelperFactory.create(authType, mAuthClient);
        mAuthHelper.login(activity, listener);
    }


    /**
     * Log the user in with SNS Token obtained from Authentication system.
     *
     * @param authType      {@link PlatformType}
     * @param accessToken   the access token string obtained from Authentication system (china platform, facebook, google)
     * @param uID           the user id string obtained from Authentication system (etc. Xiaomi)
     * @param listener      {@link GBAuthListener}
     */
    public static void LoginWithAuthType(final AuthType authType, String accessToken, String uID, final GBAuthListener listener) {
        mAuthClient.requestWithAuthType(authType, accessToken, uID, listener);
    }

    /**
     * Log out the user from a session
     *
     * @param activity  The activity which is starting the login process (Not used.)
     * @param listener  {@link GBAuthListener}
     */

    public static void Logout(Activity activity, GBAuthListener listener) {
        mAuthHelper.logout(activity, listener);
    }

    /**
     * DeleteAccount the user from a session
     *
     * @param activity  The activity which is starting the login process (Not used.)
     * @param listener  {@link GBAuthListener}
     */

    public static void DeleteAccount(Activity activity, GBAuthListener listener) {
        // TODO: Request Delete Account
        //mAuthClient.requestWithdraw(listener);
        mAuthHelper.logout(activity, listener);
    }

    public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mAuthHelper != null)
            return mAuthHelper.onActivityResult(requestCode, resultCode, data);

        return false;
    }

    /**
     * Log the user in with the access token. (already obtained from GB)
     *
     * @param activity  The activity which is starting the login process (Not used.)
     * @param listener  {@link GBAuthListener}
     */

    static void AutoLogin(Activity activity, GBAuthListener listener) {
        //mAuthClient.autoLogin(listener);
    }

    /**
     * L
     * @param activity  The activity which is starting the login process (Not used.)
     * @param listener  {@link GBAuthListener}
     */

    static void PlatformLogin(Activity activity, final GBAuthListener listener) {
        mAuthHelper.login(activity, listener);
    }

    /**
     * Request User Information
     *
     * @param listener  {@link GBProfileListener}
     */

    public static void RequestProfile(GBProfileListener listener) {
        mAuthClient.requestProfile(listener);
    }

    public static void ShowClickWrap(Activity activity, GBViewEventListener listener) {
        if (GBDeviceUtils.isServiceLocaleKorea()) {
            // TODO : 약관동의 화면
            GBLog.d(TAG + "KOREA!!!");
            listener.onReceiveEvent(GBViewEventListener.JoycityViewEvent.SUCCESS_AGREEMENT);
        } else {
            GBLog.d(TAG + "NOT KOREA!!!");
            listener.onReceiveEvent(GBViewEventListener.JoycityViewEvent.SUCCESS_AGREEMENT);
        }
    }

    static GBAuthImpl getAuthIml() {
        return mAuthClient;
    }


    public static void showProfile(Activity activity, GBProfileViewType viewType) {

        if (GBProfileViewType.GBProfileUserInfo.equals(viewType))
            ShowProfileView(activity);
        else {
            GBLog.d("===========================================");
            GBLog.d("============ Not Supported API ============");
            GBLog.d("===========================================");
        }
    }

    private static void ShowProfileView(Activity activity) {
        try {
            Intent intent = new Intent(activity, Class.forName("com.gebros.platform.auth.ui.GBProfileActivity"));
//            activity.startActivityForResult(intent, 15002);
            ActivityResultHelper.startActivityForResult(activity, 15002, intent, new ActivityResultHelper.ActivityResultListener() {
                @Override
                public void onActivityResult(int resultCode, Intent intent) {
                    GBLog.d(TAG + "result Code = %d", resultCode);
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class IAuthHelperFactory {

        static GBAuthHelper create(AuthType authType, GBAuthImpl impl) {
            if (authType == AuthType.CHINA360 ||
                    authType == AuthType.BAIDU ||
                    authType == AuthType.XIAOMI ||
                    authType == AuthType.UC ||
                    authType == AuthType.WANDOUJIA ||
                    authType == AuthType.HUAWEI) {
                return new PlatformAuthHelper(impl);
            } else if (authType == AuthType.GOOGLE) {
                return new GoogleAuthHelper(impl);
            } else if (authType == AuthType.GUEST) {
                return new GBAuthHelper(impl);
            } else if (authType == AuthType.FACEBOOK) {
                return new FacebookAuthHelper(impl);
            }

            return new PlatformAuthHelper(impl);
        }
    }
}
