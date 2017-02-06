package com.gebros.platform.auth;


import android.app.Activity;
import android.content.Intent;

import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.platform.PlatformType;

import java.util.Map;

/**
 * Created by gebros.nairs77@gmail.com on 5/24/16.
 */
class AuthHelper implements IAuthHelper {

    protected GBAuthImpl mImpl;

    public AuthHelper(GBAuthImpl authImpl) {
        mImpl = authImpl;
    }

    public void login(Activity activity, GBAuthListener listener) {
        mImpl.authorize(getAuthType(), null, null, listener);
    }

    public void loginWithAccountInfo(Activity activity, Map<String, Object> accountInfo, GBAuthListener listener) {
        mImpl.authorize(getAuthType(), accountInfo, listener);
    }

    public void logout(GBAuthListener listener) {
        mImpl.requestLogout(listener);
    }

    public AuthType getAuthType() {
        return AuthType.GUEST;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
