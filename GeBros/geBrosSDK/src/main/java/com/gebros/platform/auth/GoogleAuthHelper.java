package com.gebros.platform.auth;

import android.app.Activity;
import com.gebros.platform.listener.GBAuthListener;

/**
 * Created by gebros.nairs77@gmail.com on 5/24/16.
 */
class GoogleAuthHelper extends AuthHelper {

    public GoogleAuthHelper(GBAuthImpl impl) {
        super(impl);
    }

    public AuthType getAuthType() {
        return AuthType.GOOGLE;
    }

    public void login(Activity activity, GBAuthListener listener) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN);

    }

    public void logout(GBAuthListener listener) {

    }
}
