package com.gebros.platform.auth;

import com.gebros.platform.GBSettings;

/**
 * Created by gebros.nairs77@gmail.com on 5/13/16.
 */
public class GBAccountAPI {
    /**
     * Authentication resources API paths
     */

    public static final String AUTHENTICATION_URI = GBSettings.getAccountServer() + "/Login";
    public static final String REISSUED_URI = GBSettings.getAccountServer() + "/session/auto-login";
    public static final String DISCONNECT_URI = GBSettings.getAccountServer() + "/session/logout";
    public static final String JOIN_URI = GBSettings.getAccountServer() + "/users/join";
    public static final String WITHDRAW_URI = GBSettings.getAccountServer() + "/users/quit";
    public static final String GAME_WITHDRAW_URI = GBSettings.getAccountServer() + "/users/game-quit";
}
