package com.gebros.platform.auth;

/**
 * Created by gebros.nairs77@gmail.com on 5/24/16.
 */
interface IAuthCallback {

    public void onSuccess(LoginResult result);

    public void onFail(LoginResult result);

    public void onCancel(boolean isUserCancelled);
}
