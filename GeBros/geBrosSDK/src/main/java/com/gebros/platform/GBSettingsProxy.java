package com.gebros.platform;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.gebros.platform.log.GBLog;
import com.gebros.platform.util.GBValidator;

/**
 * Created by gebros.nairs77@gmail.com on 5/6/16.
 */
final class GBSettingsProxy {

    static final String DEVICE_DATE_KEY = "datePrefKey";
    static final String DATE_NAME = "date";

    private static volatile GBSettingsProxy instance;

    private final LocalBroadcastManager localBroadcastManager;
    private final GBConfig mSettingCache;
    private GBSettings currentSettings;

    GBSettingsProxy(LocalBroadcastManager localBroadcastManager,
                    GBConfig settingCache) {

        GBValidator.notNull(localBroadcastManager, "localBroadcastManager");
        GBValidator.notNull(settingCache, "accessTokenCache");

        this.localBroadcastManager = localBroadcastManager;
        this.mSettingCache = settingCache;
    }

    static GBSettingsProxy getInstance() {

        if (instance == null) {
            synchronized (GBSettingsProxy.class) {
                if (instance == null) {
                    Context applicationContext = GBSdk.getApplicationContext();

                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(
                            applicationContext);
                    GBConfig settingCache = new GBConfig();

                    instance = new GBSettingsProxy(localBroadcastManager, settingCache);
                }
            }
        }

        return instance;
    }

    boolean loadSettings() {
        GBSettings settings = mSettingCache.load();

        if (settings != null) {
            setCurrentSettings(settings);

            return true;
        }

        GBLog.d("[DISASTER....");

        return false;
    }

    void setCurrentSettings(GBSettings settings) {
        currentSettings = settings;
    }

    GBSettings getCurrentSettings() {
        return currentSettings;
    }

    GBConfig getConfig() { return mSettingCache; }
}
