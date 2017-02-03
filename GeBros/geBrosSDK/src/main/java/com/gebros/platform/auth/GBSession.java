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

    private final String accessToken;
    private final String refreshToken;
    private final AuthType authType;
    private final Date last_access;
    private final SessionState state;

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

    /**
     * Create / Update a new Session using the supplied information from previously-obtained or updated information (in SharedPreference)
     * @param accessToken       the access token string obtained from GB (server)
     * @param refreshToken      the refresh token string obtained from GB
     * @param authType            an enum indicating how the access token was originally obtained by {@link com.gebros.platform.platform.auth.AuthType}
     *                          if NONE, {@link SessionState READY, CLOSED} or  is assumed. (First access, after Logout)
     * @param last_access       update date when the token is refreshed
     * @param state             Session State {@link SessionState}
     */
    GBSession(
            @Nullable
            final String accessToken,
            @Nullable
            final String refreshToken,
            @NonNull
            final AuthType authType,
            @Nullable
            final Date last_access,
            @Nullable
            final SessionState state
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.authType = authType;
        this.last_access = last_access != null ? last_access : new Date(0);
        this.state = state;
    }

    GBSession(Parcel parcel) {
        this.accessToken = parcel.readString();
        this.refreshToken = parcel.readString();
        this.authType = AuthType.valueOf(parcel.readString());
        this.last_access = new Date(parcel.readLong());
        this.state = SessionState.valueOf(parcel.readString());
    }

    JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(GBSessionProxy.ACCESS_TOKEN_KEY, this.accessToken);
        jsonObject.put(GBSessionProxy.REFRESH_TOKEN_KEY, this.refreshToken);
        jsonObject.put(GBSessionProxy.SESSION_ACCESS_KEY, last_access.getTime());
        jsonObject.put(GBSessionProxy.SESSION_AUTH_TYPE_KEY, authType.name());

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
        return new GBSession(null, null, AuthType.NONE, new Date(), GBSession.SessionState.CLOSED);
    }

    /**
     * Gets the string representing the access token.
     *
     * @return the string representing the access token
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Gets the string representing the refresh token.
     *
     * @return the string representing the refresh token
     */
    public String getRefreshToken() {
        return this.refreshToken;
    }


    /**
     * Gets the date at which the access token expires.
     *
     * @return the expiration date of the token
     */
//    public Date getExpires() {
//        return this.expires;
//    }

    /**
     * Gets the {@link SessionJoinSource} indicating how this access token was obtained.
     *
     * @return the enum indicating how the access token was obtained
     */
    public AuthType getAuthType() {
        return authType;
    }

    /**
     * Get the {@link SessionState} indicating how this session state was set
     * @return the enum indicating how the session state was obtained
     */
    public SessionState getState() { return state; };


    /**
     *
     *
     * @return
     */
    public boolean isOpened() {
        return (state == SessionState.OPEN) ? true : false;
    }


    static GBSession createFromJSONObject(JSONObject jsonObject) throws JSONException {
        String accessToken = jsonObject.getString(GBSessionProxy.ACCESS_TOKEN_KEY);
        String refreshToken = jsonObject.getString(GBSessionProxy.REFRESH_TOKEN_KEY);
        AuthType authType = AuthType.valueOf(jsonObject.getString(GBSessionProxy.SESSION_AUTH_TYPE_KEY));
        Date lastRefresh = new Date(jsonObject.getLong(GBSessionProxy.SESSION_ACCESS_KEY));
        SessionState state = SessionState.READY;

        return new GBSession(
                accessToken,
                refreshToken,
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
        dest.writeString(this.accessToken);
        dest.writeString(this.refreshToken);
        dest.writeString(this.authType.name());
        dest.writeLong(this.last_access.getTime());
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
