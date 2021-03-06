package com.gebros.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
    }

    public void configureSDKWithGameInfo(Activity activity, int gameCode, String clientSecretkey, Platform platform, GBLog.LogLevel logLevel) {
        GBConfig config = GBSettingsProxy.getInstance().getConfig();
        String gameVersion = GBDeviceUtils.getGameVersion();

        config.setSDKInfo("", gameCode, gameVersion, platform.getPlatformType());

        if (logLevel == GBLog.LogLevel.RELEASE)
            GBLog.disableLog();
        else
            GBLog.enableLog();

        mPlatformClient = PlatformFactory.create(platform);
        mPlatformClient.doPlatformInit(activity, null);
    }

    public void configureSDKWithGameInfo(Activity activity, int gameCode, String clientSecretkey, GBLog.LogLevel logLevel) {
        Platform platform = new Platform.Builder(PlatformType.DEFAULT).build();
        configureSDKWithGameInfo(activity, gameCode, clientSecretkey, platform, logLevel);
    }

    public IPlatformClient getPlatformClient() {
        return mPlatformClient;
    }

    public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
        //GB.instance.getPlatformClient().onActivityCreate(activity, savedInstanceState);

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
