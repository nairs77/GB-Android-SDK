package com.gebros.platform.auth;

import android.app.Activity;

import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.platform.PlatformType;

/**
 * Created by gebros.nairs77@gmail.com on 5/25/16.
 */

class GBAuthHelper extends AuthHelper {

    public GBAuthHelper(GBAuthImpl authImpl) {
        super(authImpl);
    }

    public void login(Activity activity, GBAuthListener listener) {
        mImpl.authorize(getAuthType(), null, null, listener);
    }

    public void logout(GBAuthListener listener) {
        mImpl.requestLogout(listener);
    }

    public AuthType getAuthType() {
        return AuthType.GUEST;
    }
}
