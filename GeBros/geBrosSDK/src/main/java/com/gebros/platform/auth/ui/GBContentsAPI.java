package com.gebros.platform.auth.ui;

import com.gebros.platform.GBSettings;

/**
 * Created by jce_platform on 2016. 5. 30..
 */
public class GBContentsAPI {
    /**
     * Application API resource paths
     */
    /**
     * TODO : 고객센터 URI 변경 필요
     */
    public static final String JOYPLE_CUSTOMER_WEB_URL = "http://fw.hogacn.com/support/faq";
    public static final String JOYPLE_CLICKWRAP_WEB_URL = "http://fw.hogacn.com/policy/stipulation";

    public static final String PROFILE_URI = GBSettings.getContentsServer() + "/users/info";

    public static final String FRIENDS_URI = GBSettings.getContentsServer() + "/users/friends/list";
    public static final String ADD_FRIENDS_URI = GBSettings.getContentsServer() + "/users/friends/add-relation";
    public static final String UPDATE_FRIENDS_STATUS_URI = GBSettings.getContentsServer() + "/users/friends/update-type";
    public static final String SEARCH_FRIENDS_URI = GBSettings.getContentsServer() + "/users/friends/search";
    public static final String INVITED_USER_COUNT = GBSettings.getContentsServer() + "/invitation/count_list";

    public static final String NICKNAME_CHANGE_URI = GBSettings.getContentsServer() + "/users/nickname/change";
    public static final String GREETING_CHANGE_URI = GBSettings.getContentsServer() + "/users/greetingMsg/change";
    public static final String PROFILE_IMG_CHANGE_URI = GBSettings.getContentsServer() + "/users/profileImage/change";
    public static final String ADDITIONAL_INFO_CHANGE_URI = GBSettings.getContentsServer() + "/users/additional-info/change";
}
