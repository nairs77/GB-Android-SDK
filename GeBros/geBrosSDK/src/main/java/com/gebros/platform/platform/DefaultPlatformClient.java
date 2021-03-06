package com.gebros.platform.platform;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.gebros.platform.auth.AuthType;
import com.gebros.platform.pay.GBInAppItem;

/**
 * Created by gebros.nairs77@gmail.com on 5/10/16.
 */
class DefaultPlatformClient extends BasePlatformClient {

    private static final String TAG = DefaultPlatformClient.class.getCanonicalName();

    private static final String CLIENT_SECRET_PARAMETER_KEY = "client_secret";
    private static final String USERKEY_PARAMETER_KEY = "userkey";

    public boolean isInitialized() { return isInitialized; }

    public AuthType getAuthType() { return AuthType.GOOGLE; }
    public PlatformType getType() {
        return PlatformType.DEFAULT;
    }

    @Override
    public void doPlatformActive(Activity activity, IPlatformListener.OnInitLister listener) {

    }

    @Override
    public void doPlatformInit(Activity activity, IPlatformListener.OnInitLister listener) {
        //// TODO: 5/17/16 InitAsync
        super.doPlatformInit(activity, null);
    }

    public void doPlatformLogin(Activity activity, IPlatformListener.OnAuthListener listener) {
        // Show Login UI


    }

    public void doPlatformLogout(Activity activity, IPlatformListener.OnAuthListener listener) {

    }

    public void doPlatformPay(Activity activity, GBInAppItem itemInfo, String cpId, IPlatformListener.OnPayListener listener) {

    }

    @Override
    public void doPlatformExit(Activity activity, IPlatformListener.OnGameListener listener) {

    }

    protected boolean isLandscapeMode(Activity activity) {
        int orientation = activity.getResources().getConfiguration().orientation;
        boolean isLandscape = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? true : false;
        activity.setRequestedOrientation(isLandscape ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        return isLandscape;
    }

    /*
    activity life cycle
     */
    @Override
    public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
        if (!isInitialized()) {
            doPlatformInit(activity, null);
            isInitialized = true;
        }
    }

    @Override
    public void onActivityStart(Activity activity) {

    }

    @Override
    public void onActivityStop(Activity activity) {

    }

    @Override
    public void onActivityResume(Activity activity) {

    }

    @Override
    public void onActivityPause(Activity activity) {

    }

    @Override
    public void onActivityDestroy(Activity activity) {

    }
}
