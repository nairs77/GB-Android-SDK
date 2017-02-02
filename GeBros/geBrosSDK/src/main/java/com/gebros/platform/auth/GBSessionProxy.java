package com.gebros.platform.auth;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.gebros.platform.GBSdk;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.util.GBValidator;

/**
 * Created by Joycity-Platform on 5/10/16.
 */
final class GBSessionProxy {

    static final String CACHED_SESSION_KEY = "cache.com.gebros.platform.session";//"com.joycity.platform.account.sdk.PreferenceKey";

    static final String ACCESS_TOKEN_KEY = "accessToken";
    static final String REFRESH_TOKEN_KEY = "refreshToken";
    static final String SESSION_SOURCE_KEY = "session_source";
    static final String SESSION_ACCESS_KEY = "last_access";

    private static volatile GBSessionProxy instance;

    private final LocalBroadcastManager localBroadcastManager;
    private final GBSessionCache sessionCache;
    private GBSession currentSession;

    GBSessionProxy(LocalBroadcastManager localBroadcastManager,
                       GBSessionCache settingCache) {

        GBValidator.notNull(localBroadcastManager, "localBroadcastManager");
        GBValidator.notNull(settingCache, "accessTokenCache");

        this.localBroadcastManager = localBroadcastManager;
        this.sessionCache = settingCache;
    }

    static GBSessionProxy getInstance() {

        if (instance == null) {

            synchronized (GBSessionProxy.class) {
                if (instance == null) {
                    Context applicationContext = GBSdk.getApplicationContext();

                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(
                            applicationContext);
                    GBSessionCache sessionCache = new GBSessionCache();

                    instance = new GBSessionProxy(localBroadcastManager, sessionCache);
                }
            }
        }

        return instance;
    }

    boolean loadSession() {
        GBSession session = sessionCache.load();

        if (session != null) {
            setCurrentSession(session);

            return true;
        }

        GBLog.i("[DISASTER]....");

        return false;
    }

    void setCurrentSession(GBSession newSession) {

        currentSession = newSession;

        GBSession oldSession = this.currentSession;
        this.currentSession = newSession;

        if (currentSession != null) {
            sessionCache.save(currentSession);
        } else {
            sessionCache.clear();
        }

        if (!GBValidator.areObjectsEqual(oldSession, currentSession)) {
            sendCurrentAccessTokenChangedBroadcast(oldSession, currentSession);
        }
    }

    private void sendCurrentAccessTokenChangedBroadcast(GBSession oldSession,
                                                        GBSession currentSession) {
        Intent intent = new Intent(GBSession.ACTION_CURRENT_ACCESS_TOKEN_CHANGED);

        intent.putExtra(GBSession.EXTRA_OLD_ACCESS_TOKEN, oldSession);
        intent.putExtra(GBSession.EXTRA_NEW_ACCESS_TOKEN, currentSession);

        localBroadcastManager.sendBroadcast(intent);
    }

    GBSession getCurrentSession() { return currentSession; }
}
