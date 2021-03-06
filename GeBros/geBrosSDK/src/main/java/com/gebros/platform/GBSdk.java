package com.gebros.platform;

import android.app.Activity;
import android.content.Context;
import com.gebros.platform.log.GBLog.LogLevel;
import com.gebros.platform.platform.Platform;

/**
 * Created by nairs77 on 2017. 1. 16..
 */

public final class GBSdk {

    private static final String TAG = GBSdk.class.getCanonicalName();
    public static final String VERSION = "0.1.0";

    private static Context applicationContext;

    static final GBSdkImpl instance = new GBSdkImpl();

    static void Initialize(Activity activity) {
        applicationContext = activity.getApplicationContext();
        instance.initialize(activity);
    }

    public static void ConfigureSdkWithInfo(Activity activity, int gameCode, String apiKey, LogLevel logLevel) {
        instance.configureSDKWithGameInfo(activity, gameCode, apiKey, logLevel);
    }
    public static void ConfigureSdkWithInfo(Activity activity, int gameCode, String apiKey, Platform platform, LogLevel logLevel) {
        instance.configureSDKWithGameInfo(activity, gameCode, apiKey, platform, logLevel);
    }

    public static Context getApplicationContext() {
        return applicationContext;
    }
}
