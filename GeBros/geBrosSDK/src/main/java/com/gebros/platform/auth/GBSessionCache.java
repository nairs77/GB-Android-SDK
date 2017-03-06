package com.gebros.platform.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.gebros.platform.GBSdk;
import com.gebros.platform.util.GBValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by gebros.nairs77@gmail.com on 5/10/16.
 */
class GBSessionCache {

    private SharedPreferences sharedPreferences;

    GBSessionCache(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public GBSessionCache() {
        this(
                GBSdk.getApplicationContext().getSharedPreferences(
                        GBSessionProxy.CACHED_SESSION_KEY,
                        Context.MODE_PRIVATE)
        );
    }

    public GBSession load() {

        GBSession session = getCachedSession();

        if (session == null || !isInstalled()) {
            // TODO : 처음 앱을 실행(?)
            // 현재 시간을 installed 시간으로 처리
            return getDefaultSession();
        }

        GBValidator.notNull(session, "Session Load Failed!!!");

        return session;
    }

    public void save(GBSession session) {
        GBValidator.notNull(session, "accessToken");

        JSONObject jsonObject = null;
        try {
            jsonObject = session.toJSONObject();
            sharedPreferences.edit().putString(GBSessionProxy.CACHED_SESSION_KEY, jsonObject.toString())
                    .apply();
        } catch (JSONException e) {
            // Can't recover
        }
    }

    public void clear() {
        sharedPreferences.edit().remove(GBSessionProxy.CACHED_SESSION_KEY).apply();
    }

    private boolean isInstalled() {
        return sharedPreferences.contains(GBSessionProxy.CACHED_SESSION_KEY);
    }

    private GBSession getDefaultSession() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GBSessionProxy.ACCOUNT_SEQ_KEY, "");
            jsonObject.put(GBSessionProxy.CHANNEL_USER_ID, "");
            jsonObject.put(GBSessionProxy.CHANNEL_TYPE_KEY, AuthType.NONE.getLoginType());
//            jsonObject.put(GBSessionProxy.SESSION_ACCESS_KEY, new Date(0).getTime());
            jsonObject.put(GBSessionProxy.SESSION_STATE_KEY, GBSession.SessionState.NONE);
            return GBSession.createFromJSONObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    private GBSession getCachedSession() {

        String jsonString = sharedPreferences.getString(GBSessionProxy.CACHED_SESSION_KEY, null);
        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                return GBSession.createFromJSONObject(jsonObject);
            } catch (JSONException e) {
                return null;
            }
        }
        return null;

    }
}
