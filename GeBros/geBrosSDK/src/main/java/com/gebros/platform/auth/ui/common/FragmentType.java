package com.gebros.platform.auth.ui.common;

/**
 * Created by jce_platform on 2016. 5. 30..
 */
public enum FragmentType {
    /**
     *  GB UI for Accounts
     */

    CLICK_WRAP_FRAGMENT(MenuType.LAYER),
    LOGIN_ACCOUNT_FRAGMENT(MenuType.LAYER),
    GB_LOGIN_FRAGMENT(MenuType.LAYER),
    GB_JOIN_FRAGMENT(MenuType.LAYER),
    FIND_PASSWORD_FRAGMENT(MenuType.LAYER),
    FIND_END_PASSWORD_FRAGMENT(MenuType.LAYER),

    /**
     * GB UI for contents
     */

    PROGRESS_BAR(MenuType.LAYER),
    PROFILE_INFO_FRAGMENT(MenuType.TOP_MENU),
    SETTING_INFO_FRAGMENT(MenuType.TOP_MENU),
    ACCOUNT_MANAGE_FRAGMENT(MenuType.TOP_MENU),
    FRIENDS(MenuType.TOP_MENU),

    EMAIL_ENROLL_FRAGMENT(MenuType.SUB_MENU),
    NICKNAME_CHANGE_FRAGMENT(MenuType.SUB_MENU),
    GREETING_CHANGE_FRAGMENT(MenuType.SUB_MENU),
    EMAIL_SETUP_FRAGMENT(MenuType.SUB_MENU),
    MYINFO_INFO_FRAGMENT(MenuType.SUB_MENU),
    ADDINFO_INFO_FRAGMENT(MenuType.SUB_MENU),
    PHONEINFO_INFO_FRAGMENT(MenuType.SUB_MENU),
    GAME_QUIT_FRAGMENT(MenuType.SUB_MENU),

    GB_GAMES_WEBVIEW(MenuType.TOP_MENU),
    GB_CUSTOMER_WEBVIEW(MenuType.SUB_MENU),
    GB_CLICKWRAP_WEBVIEW(MenuType.SUB_MENU);

    public MenuType menuType;

    FragmentType(MenuType menuType) {
        this.menuType = menuType;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public enum MenuType {
        LAYER, TOP_MENU, SUB_MENU
    }
}
