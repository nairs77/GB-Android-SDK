package com.gebros.platform.auth;

/**
 * Created by gebros.nairs77@gmail.com on 2017. 2. 3..
 */

public enum AuthType {
    GUEST(1),
    FACEBOOK(2),
    GOOGLE(3),
    CHINA360(11),
    BAIDU(12),
    XIAOMI(13),
    UC(14),
    WANDOUJIA(15),
    CHINA4399(16),
    HUAWEI(17),
    LENOVO(18),
    NONE(99);

    private final int loginType;

    AuthType(int loginType) {
        this.loginType = loginType;
    }

    public int getLoginType() {
        return loginType;
    }

    public static AuthType valueOf(int loginType) {

        switch (loginType) {

            case 1:
                return AuthType.GUEST;
            case 2:
                return AuthType.FACEBOOK;
            case 3:
                return AuthType.GOOGLE;
            case 11:
                return AuthType.CHINA360;
            case 12:
                return AuthType.BAIDU;
            case 13:
                return AuthType.XIAOMI;
            case 14:
                return AuthType.UC;
            case 15:
                return AuthType.WANDOUJIA;
            case 17:
                return AuthType.HUAWEI;
            default:
                return AuthType.GUEST;
        }
    }
};
