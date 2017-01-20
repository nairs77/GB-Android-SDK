package com.gebros.platform.auth;


import android.app.Activity;

import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.platform.PlatformType;

import java.util.Map;

/**
 * Created by nairs77@joycity.com on 5/23/16.
 */

interface IAuthHelper {

    /**
     *
     *
     * @param activity
     * @param listener
     */
    public void login(Activity activity, GBAuthListener listener);


    /**
     *
     * @param activity
     * @param accountInfo
     * @param listener
     */
    public void loginWithAccountInfo(Activity activity, Map<String, Object> accountInfo, GBAuthListener listener);

    /**
     *
     *
     * @param listener
     */
    public void logout(GBAuthListener listener);

    /**
     *
     *
     * @return
     */
    public PlatformType.AuthType getAuthType();
}
