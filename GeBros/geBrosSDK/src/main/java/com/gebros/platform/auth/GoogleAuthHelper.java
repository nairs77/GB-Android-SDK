package com.gebros.platform.auth;

import android.app.Activity;

import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.platform.PlatformType;

/**
 * Created by Joycity-Platform on 5/24/16.
 */
class GoogleAuthHelper extends AuthHelper {

    public GoogleAuthHelper(GBAuthImpl impl) {
        super(impl);
    }

    public PlatformType.AuthType getAuthType() {
        return PlatformType.AuthType.GOOGLE;
    }

    public void login(Activity activity, GBAuthListener listener) {
        
    }

    public void logout(GBAuthListener listener) {

    }
}
