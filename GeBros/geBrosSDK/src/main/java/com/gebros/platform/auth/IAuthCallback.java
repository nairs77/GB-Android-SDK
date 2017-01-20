package com.gebros.platform.auth;

/**
 * Created by Joycity-Platform on 5/24/16.
 */
interface IAuthCallback {

    public void onSuccess(LoginResult result);

    public void onFail(LoginResult result);

    public void onCancel(boolean isUserCancelled);
}
