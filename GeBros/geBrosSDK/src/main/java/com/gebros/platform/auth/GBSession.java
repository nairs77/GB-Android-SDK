package com.gebros.platform.auth;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by nairs77 on 5/10/16.
 *
 */
public final class GBSession implements Parcelable {

    public static final String ACTION_CURRENT_ACCESS_TOKEN_CHANGED =
            "com.gebros.platform.ACTION_CURRENT_ACCESS_TOKEN_CHANGED";
    public static final String EXTRA_OLD_ACCESS_TOKEN = "com.gebros.platform.EXTRA_OLD_ACCESS_TOKEN";
    public static final String EXTRA_NEW_ACCESS_TOKEN = "com.gebros.platform.EXTRA_NEW_ACCESS_TOKEN";

//    private final String accessToken;
//    private final String refreshToken;
    private final AuthType mAuthType;
    private final Date mLastAccessTime;
    private final SessionState mState;

    private final String mUserKey;
    private final String mUserInfo;

    public enum SessionState {
        NONE,
        READY,
        OPEN,
        CLOSED,
        ACCESS_FAILED;
    }

//    public interface SessionStatusCallback {
//        abstract public void onStatusCallback(GBSession session, SessionState status, GBException exception);
//    }

    // TODO : Check Annotation library

    GBSession(
            @Nullable
            final String userKey,
            @Nullable
            final String userInfo,
            @NonNull
            final AuthType authType,
            @Nullable
            final Date last_access,
            @Nullable
            final SessionState state
    ) {
        this.mUserKey = userKey;
        this.mUserInfo = userInfo;
        this.mAuthType = authType;
        this.mLastAccessTime = last_access != null ? last_access : new Date(0);
        this.mState = state;
    }

    GBSession(Parcel parcel) {
        this.mUserKey = parcel.readString();
        this.mUserInfo = parcel.readString();
        this.mAuthType = AuthType.valueOf(parcel.readString());
        this.mLastAccessTime = new Date(parcel.readLong());
        this.mState = SessionState.valueOf(parcel.readString());
    }

    JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(GBSessionProxy.ACCOUNT_SEQ_KEY, this.mUserKey);
        jsonObject.put(GBSessionProxy.SESSION_USER_INFO, this.mUserInfo);
        jsonObject.put(GBSessionProxy.SESSION_ACCESS_KEY, this.mLastAccessTime.getTime());
        jsonObject.put(GBSessionProxy.SESSION_AUTH_TYPE_KEY, mAuthType.getLoginType());

        if (mAuthType == AuthType.NONE)
            jsonObject.put(GBSessionProxy.SESSION_STATE_KEY, SessionState.NONE);
        else
            jsonObject.put(GBSessionProxy.SESSION_STATE_KEY, SessionState.READY);

        return jsonObject;
    }

    /**
     * Getter for the Session that is current for the application.
     *
     * @return The Session that is current for the application.
     */

    public static GBSession getActiveSession() {
        return GBSessionProxy.getInstance().getCurrentSession();
    }

    public static void setCurrentActiveSession(GBSession newSession) {
        GBSessionProxy.getInstance().setCurrentSession(newSession);
    }

    public static GBSession clearSession() {
        return new GBSession(null, null, AuthType.NONE, new Date(), GBSession.SessionState.NONE);
    }

    public String getUserKey() {
        return this.mUserKey;
    }

    public String getUserInfo() { return this.mUserInfo; }
    /**
     * Gets the {@link SessionJoinSource} indicating how this access token was obtained.
     *
     * @return the enum indicating how the access token was obtained
     */
    public AuthType getAuthType() {
        return mAuthType;
    }

    /**
     * Get the {@link SessionState} indicating how this session state was set
     * @return the enum indicating how the session state was obtained
     */
    public SessionState getState() { return mState; };


    /**
     *
     *
     * @return
     */
    public boolean isOpened() {
        return (mState == SessionState.OPEN) ? true : false;
    }


    static GBSession createFromJSONObject(JSONObject jsonObject) throws JSONException {
        String userKey = jsonObject.getString(GBSessionProxy.ACCOUNT_SEQ_KEY);
        String userInfo = jsonObject.getString(GBSessionProxy.SESSION_USER_INFO);
        AuthType authType = AuthType.valueOf(jsonObject.getInt(GBSessionProxy.SESSION_AUTH_TYPE_KEY));
        Date lastRefresh = new Date(jsonObject.getLong(GBSessionProxy.SESSION_ACCESS_KEY));
        SessionState state = SessionState.valueOf(jsonObject.getString(GBSessionProxy.SESSION_STATE_KEY));

        return new GBSession(
                userKey,
                userInfo,
                authType,
                lastRefresh,
                state);
    }

    // - Parceable Interface

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUserKey);
        dest.writeString(this.mAuthType.name());
        dest.writeLong(this.mLastAccessTime.getTime());
    }

    public static final Parcelable.Creator<GBSession> CREATOR = new Parcelable.Creator() {

        @Override
        public GBSession createFromParcel(Parcel source) {
            return new GBSession(source);
        }

        @Override
        public GBSession[] newArray(int size) {
            return new GBSession[size];
        }
    };

}
