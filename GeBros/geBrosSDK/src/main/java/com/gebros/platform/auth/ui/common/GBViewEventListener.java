package com.gebros.platform.auth.ui.common;

/**
 * Created by gebros.nairs77@gmail.com on 7/20/16.
 */
public interface GBViewEventListener {

    public enum JoycityViewEvent {
        SUCCESS_AGREEMENT,
        QUICK_LOGIN,
        ACCOUNT_LOGIN,
        NEXT_AGREEMENT,
        DUPLICATION_ACCOUNT_SELECT,
        DUPLICATION_ACCOUNT_CANCEL,
        CLOSE_VIEW,
        DETAIL_PRIVACY_MOVE_AGREEMENT;
    }

    void onReceiveEvent(JoycityViewEvent event);
}
