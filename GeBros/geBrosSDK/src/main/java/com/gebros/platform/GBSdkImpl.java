package com.gebros.platform;

import android.app.Activity;

import com.gebros.platform.log.GBLog;
import com.gebros.platform.util.GBDeviceUtils;

/**
 * Created by nairs77 on 2017. 1. 17..
 */

final class GBSdkImpl {

    private static final String TAG = GBSdkImpl.class.getCanonicalName();

    private IPlatformClient mPlatformClient;

    public void initialize(int gameCode, String clientSecretkey, GBLog.LogLevel logLevel) {
        //
        GBConfig config = GBSettingsProxy.getInstance().getConfig();
        String gameVersion = GBDeviceUtils.getGameVersion();

        config.setSDKInfo("", gameCode, gameVersion, platform.getPlatformType());
    }
}
