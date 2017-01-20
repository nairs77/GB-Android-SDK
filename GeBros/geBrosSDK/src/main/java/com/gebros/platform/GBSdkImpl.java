package com.gebros.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gebros.platform.log.GBLog;
import com.gebros.platform.platform.IPlatformClient;
import com.gebros.platform.platform.IPlatformListener;
import com.gebros.platform.platform.Platform;
import com.gebros.platform.platform.PlatformFactory;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBDeviceUtils;

/**
 * Created by nairs77 on 2017. 1. 17..
 */

final class GBSdkImpl {

    private static final String TAG = GBSdkImpl.class.getCanonicalName();

    private IPlatformClient mPlatformClient;

    public void initialize(Activity activity) {
        GBSettingsProxy.getInstance().loadSettings();

//        GBAuthManager.Initialize(activity);
//        GBInAppManager.Initialize(activity);
/*
        mPlatformClient = PlatformFactory.create(new Platform.Builder("", "").PlatformType(PlatformType.DEFAULT).build());
        mPlatformClient.doPlatformActive(activity, new IPlatformListener.OnInitLister() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail() {
            }
        });
*/
    }

    public void configureSDKWithGameInfo(Activity activity, int gameCode, String clientSecretkey, GBLog.Mode logLevel) {
        //
        GBConfig config = GBSettingsProxy.getInstance().getConfig();
        String gameVersion = GBDeviceUtils.getGameVersion();

        config.setSDKInfo("", gameCode, gameVersion, null);

        if (logLevel == GBLog.Mode.RELEASE)
            GBLog.disableLog();
        else
            GBLog.enableLog();

        mPlatformClient = PlatformFactory.create(new Platform.Builder("", "").PlatformType(PlatformType.DEFAULT).build());
        mPlatformClient.doPlatformInit(activity, null);

//        isInitialized = true;
    }

    public IPlatformClient getPlatformClient() {
        return mPlatformClient;
    }

    public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
        //Joyple.instance.getPlatformClient().onActivityCreate(activity, savedInstanceState);
        if (getPlatformClient() != null)
            getPlatformClient().onActivityCreate(activity, savedInstanceState);
    }

    public void onActivityStart(Activity activity) {

        if (getPlatformClient() != null)
            getPlatformClient().onActivityStart(activity);

    }

    public void onActivityStop(Activity activity) {
        if (getPlatformClient() != null)
            getPlatformClient().onActivityStop(activity);
    }

    public void onActivityRestart(Activity activity) {
        if (getPlatformClient() != null)
            getPlatformClient().onActivityRestart(activity);
    }

    public void onActivityPause(Activity activity) {
        if (getPlatformClient() != null)
            getPlatformClient().onActivityPause(activity);
    }

    public void onActivityResume(Activity activity) {
        if (getPlatformClient() != null)
            getPlatformClient().onActivityResume(activity);
    }

    public void onActivityDestroy(Activity activity) {
        if (getPlatformClient() != null)
            getPlatformClient().onActivityDestroy(activity);
    }

    public void onActivityNewIntent(Activity activity, Intent intent) {
        if (getPlatformClient() != null)
            getPlatformClient().onActivityNewIntent(activity, intent);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        if (getPlatformClient() != null)
            getPlatformClient().onActivityResult(activity, requestCode, resultCode, intent);
    }
}
