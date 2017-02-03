package com.gebros.platform.auth;

import com.gebros.platform.GBSettings;

/**
 * Created by gebros.nairs77@gmail.com on 5/13/16.
 */
public class GBAccountAPI {
    /**
     * Authentication resources API paths
     */

    public static final String AUTHENTICATION_URI = GBSettings.getAccountServer() + "/session/login";
    public static final String REISSUED_URI = GBSettings.getAccountServer() + "/session/auto-login";
    public static final String DISCONNECT_URI = GBSettings.getAccountServer() + "/session/logout";
    public static final String JOIN_URI = GBSettings.getAccountServer() + "/users/join";
    public static final String WITHDRAW_URI = GBSettings.getAccountServer() + "/users/quit";
    public static final String GAME_WITHDRAW_URI = GBSettings.getAccountServer() + "/users/game-quit";

    /**
     * Accounts resources API paths
     */

    public static final String EMAIL_ADD_URI = GBSettings.getAccountServer() + "/users/email/add";
    public static final String FIND_ID_URI = GBSettings.getAccountServer() + "/users/id/find";
    public static final String FIND_PW_URI = GBSettings.getAccountServer() + "/users/pw/find";
    public static final String FIND_PW_SMS_CERTKEY_SEND_URI = GBSettings.getAccountServer() + "/sms/password/certkey/send";
    public static final String FIND_PW_SMS_CERTKEY_VERIFY_URI = GBSettings.getAccountServer() + "/sms/password/certkey/verify";
    public static final String DUPLICATED_CHECK_URI = GBSettings.getAccountServer() + "/users/email/check/duplicates";
    public static final String PASSWORD_CHECK_URI = GBSettings.getAccountServer() + "/users/pw/check";
    public static final String AUTH_EMAIL_SEND_URI = GBSettings.getAccountServer() + "/email/certkey/send";
    public static final String SMS_CERTKEY_SEND_URI = GBSettings.getAccountServer() + "/sms/certkey/send";
    public static final String SMS_CERTKEY_VERIFY_URI = GBSettings.getAccountServer() + "/sms/certkey/verify";
    public static final String PASSWORD_CHANGE_URI = GBSettings.getAccountServer() + "/users/pw/change";
    public static final String FACEBOOK_SERVICE_VERIFY_URI = GBSettings.getAccountServer() + "/services/fb/verify";
    public static final String NEST_SERVICE_VERIFY_URI = GBSettings.getAccountServer() + "/services/nest/verify";
    public static final String GOOGLE_SERVICE_VERIFY_URI = GBSettings.getAccountServer() + "/services/google/verify";
    public static final String TWITTER_SERVICE_VERIFY_URI = GBSettings.getAccountServer() + "/services/twitter/verify";
    public static final String NAVER_SERVICE_VERIFY_URI = GBSettings.getAccountServer() + "/services/naver/verify";
    public static final String VERIFY_ACCOUNT_SEARCH_URI = GBSettings.getAccountServer() + "/users/account/verify";
    public static final String MERGE_ACCOUNT_URI = GBSettings.getAccountServer() + "/users/account/change";
    public static final String ADVERTISINGID_COLLECT_URI = GBSettings.getAccountServer() + "/collect/ad-id";
}
