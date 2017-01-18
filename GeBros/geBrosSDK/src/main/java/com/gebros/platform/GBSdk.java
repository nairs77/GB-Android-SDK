package com.gebros.platform;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.gebros.platform.account.GBSession;
import com.gebros.platform.log.GBLog;

/**
 * Created by nairs77 on 2017. 1. 16..
 */

public final class GBSdk {

    private static final String TAG = GBSdk.class.getCanonicalName();
    public static final String VERSION = "0.1.0";

    private static Context applicationContext;

    static final GBSdkImpl instance = new GBSdkImpl();

    public static void initGBSdk(int gameCode, String apiKey, GBLog.LogLevel logLevel) {
        instance.initialize(gameCode, apiKey, logLevel);
    }

}
