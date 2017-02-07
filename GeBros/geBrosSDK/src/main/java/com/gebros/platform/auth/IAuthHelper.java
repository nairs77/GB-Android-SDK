package com.gebros.platform.auth;


import android.app.Activity;
import android.content.Intent;

import com.gebros.platform.listener.GBAuthListener;

import java.util.Map;

/**
 * Created by gebros.nairs77@gmail.com on 5/23/16.
 */

interface IAuthHelper {

    /**
     *
     * @param activity      The activity which is starting the login process
     * @param listener
     */
    public void login(Activity activity, GBAuthListener listener);


    /**
     *
     * @param activity      The activity which is starting the login process
     * @param accountInfo
     * @param listener
     */
    public void loginWithAccountInfo(Activity activity, Map<String, Object> accountInfo, GBAuthListener listener);

    /**
     *
     *
     * @param listener
     */
    public void logout(Activity activity, GBAuthListener listener);

    /**
     *
     *
     * @return
     */
    public AuthType getAuthType();


    public void onActivityResult(int requestCode, int resultCode, Intent data);
}
