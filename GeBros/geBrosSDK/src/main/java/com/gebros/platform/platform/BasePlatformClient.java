package com.gebros.platform.platform;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.gebros.platform.GBConfig;
import com.gebros.platform.sdk.JoypleConfig;
import com.gebros.platform.sdk.auth.JoypleAuthManager;
import com.gebros.platform.sdk.game.JoypleGameManager;
import com.gebros.platform.sdk.pay.JoypleInAppItem;
import com.gebros.platform.sdk.pay.JoypleInAppManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joycity-Platform on 6/22/16.
 */
public abstract class BasePlatformClient implements IPlatformClient {

    private static final String TAG = BasePlatformClient.class.getCanonicalName();

    private static final String CLIENT_SECRET_PARAMETER_KEY = "client_secret";
    private static final String USERKEY_PARAMETER_KEY = "userkey";

    protected boolean isInitialized = false;
    protected GBConfig mConfig;

    private String mMarketInfo;
    protected boolean mDebugMode;

    public abstract boolean isInitialized();

    public PlatformType getType() {
        return PlatformType.DEFAULT;
    }

    public PlatformType.AuthType getAuthType() { return PlatformType.AuthType.JOYPLE; }

    public void setMarketInfo(String marketInfo) {
        mMarketInfo = marketInfo;
    }

    public boolean getDebugMode() {
        return mDebugMode;
    }

    public abstract void doPlatformActive(Activity activity, IPlatformListener.OnInitLister listener);

    public void doPlatformInit(Activity activity, IPlatformListener.OnInitLister listener) {
        //// TODO: 5/17/16 InitAsync
        JoypleAuthManager.Initialize(this);
        JoypleInAppManager.Initialize(this);
        JoypleGameManager.Initialize(this);

        try {
            JSONObject info = new JSONObject(mMarketInfo);
            int appMode = info.optJSONObject("result").optJSONObject("market_info").optInt("service_status");
            mDebugMode = (appMode == 1) ? true : false;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (mDebugMode) {}
    }

    public abstract void doPlatformLogin(Activity activity, IPlatformListener.OnAuthListener listener);

    public abstract void doPlatformLogout(Activity activity, IPlatformListener.OnAuthListener listener);

    public abstract void doPlatformPay(Activity activity, JoypleInAppItem itemInfo, String cpId, IPlatformListener.OnPayListener listener);

    public void doPlatformSubmitExtendData(String extraData, IPlatformListener.OnGameListener listener) {}

    public abstract void doPlatformExit(Activity activity, IPlatformListener.OnGameListener listener);

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

    }

    @Override
    public void onActivityStart(Activity activity) {

    }

    @Override
    public void onActivityStop(Activity activity) {

    }

    @Override
    public void onActivityRestart(Activity activity) {

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

    @Override
    public void onActivityNewIntent(Activity activity, Intent intent) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {

    }
}
