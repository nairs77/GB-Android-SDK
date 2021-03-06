package com.gebros.platform.auth;

import android.app.Activity;
import android.content.Intent;

import com.gebros.platform.concurrent.SimpleAsyncTask;
import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.platform.PlatformType;

import java.util.Map;

/**
 * Created by gebros.nairs77@gmail.com on 5/25/16.
 */

class GBAuthHelper implements IAuthHelper {//extends AuthHelper {

    protected GBAuthImpl mImpl;

    public GBAuthHelper(GBAuthImpl authImpl) {
        mImpl = authImpl;
    }

    public void login(Activity activity, GBAuthListener listener) {
        mImpl.authorize(getAuthType(), null, null, listener);
    }

    public void loginWithAccountInfo(Activity activity, Map<String, Object> accountInfo, GBAuthListener listener) {
        mImpl.authorize(getAuthType(), accountInfo, listener);
    }

    public void connectChannel(Activity activity, GBAuthListener listener) {

    }

    public void logout(Activity activity, GBAuthListener listener) {
        //mImpl.requestLogout(listener);

        GBSession newSession = GBSession.clearSession();
        GBSession.getActiveSession().setCurrentActiveSession(newSession);

        listener.onSuccess(newSession);

    }

    public AuthType getAuthType() {
        return AuthType.GUEST;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
