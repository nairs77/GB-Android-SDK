package com.gebros.platform.platform;

import java.util.EnumSet;
import java.util.Set;

/**
 * Created by nairs77@joycity.com on 5/6/16.
 */
public enum PlatformType {
    DEFAULT("Joyple", EnumSet.of(AuthType.FACEBOOK, AuthType.GOOGLE, AuthType.GUEST), Market.GOOGLE, 0),
    JOYPLE_ONESTORE("Joyple", EnumSet.of(AuthType.FACEBOOK, AuthType.GOOGLE, AuthType.GUEST ), Market.ONESTORE, Market.ONESTORE.getMarketCode()),
    CHINA360 ("China360", EnumSet.of(AuthType.CHINA360), Market.CHINA360, Market.CHINA360.getMarketCode()),
    BAIDU ("Baidu", EnumSet.of(AuthType.BAIDU), Market.BAIDU, Market.BAIDU.getMarketCode()),
    XIAOMI ("Xiaomi", EnumSet.of(AuthType.XIAOMI), Market.XIAOMI, Market.XIAOMI.getMarketCode()),
    UC ("UC", EnumSet.of(AuthType.UC), Market.UC, Market.UC.getMarketCode()),
    WANDOUJIA ("Wandoujia", EnumSet.of(AuthType.WANDOUJIA), Market.WANDOUJIA, Market.WANDOUJIA.getMarketCode()),
    HUAWEI ("Huawei", EnumSet.of(AuthType.HUAWEI), Market.HUAWEI, Market.HUAWEI.getMarketCode()),
    JOYPLE_MYCARD ("Joyple", EnumSet.of(AuthType.FACEBOOK, AuthType.GOOGLE, AuthType.GUEST ), Market.MYCARD, Market.MYCARD.getMarketCode());

    private final String name;
    private final Set<AuthType> authTypeSet;
    private final Market market;
    private final int id;

    PlatformType(String name, Set<AuthType> authTypeSet, Market market, int id) {
        this.name = name;
        this.authTypeSet = authTypeSet;
        this.market = market;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Set<AuthType> getAuthTypes() {
        return this.authTypeSet;
    }

    public int getId() { return id; }

    public Market getMarket() { return market; }

    @Override
    public String toString() {
        return name;
    }

    public enum AuthType {
        GUEST(1),
        NEST(2),
        JOYPLE(3),
        FACEBOOK(4),
        GOOGLE(5),
        TWITTER(6),
        NAVER(7),
        GOOGLE_PLAY(8),
        REFRESH_TOKEN(9),
        JOIN(10),
        CHINA360(11),
        BAIDU(12),
        XIAOMI(13),
        UC(14),
        WANDOUJIA(15),
        CHINA4399(16),
        HUAWEI(17),
        LENOVO(18),
        MYCARD(21),
        RETRY_API(99),
        NONE(100);

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
                    return AuthType.NEST;
                case 3:
                    return AuthType.JOYPLE;
                case 4:
                    return AuthType.FACEBOOK;
                case 5:
                    return AuthType.GOOGLE;
                case 6:
                    return AuthType.TWITTER;
                case 7:
                    return AuthType.NAVER;
                case 8:
                    return AuthType.GOOGLE_PLAY;
                case 9:
                    return AuthType.REFRESH_TOKEN;
                case 10:
                    return AuthType.JOIN;
                case 99:
                    return AuthType.RETRY_API;
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

    public enum Market {
        GOOGLE(1, 1),
        APPLE(2, 0),
        NAVER(4, 3),
        ONESTORE(5, 3),
        LG_UPLUS(6, 4),
        CHINA360(11, 11),
        BAIDU(12, 12),
        XIAOMI(13, 13),
        UC(14, 14),
        WANDOUJIA(15, 15),
        CHINA4399(16, 16),
        HUAWEI(17, 17),
        LENOVA(18, 18),
        MYCARD(21, 21);

        private final int marketCode;
        private final int marketType;

        Market(int marketType, int marketCode) {
            this.marketType = marketType;
            this.marketCode = marketCode;
        }

        public int getMarketCode() {
            return marketCode;
        }

        public int getMarketType() {
            return marketType;
        }
    };

    public static PlatformType getPlatformType(String platformName) {
        PlatformType[] values = PlatformType.values();
        for (PlatformType authProviderType : values) {
            if(authProviderType.getName().equals(platformName)) {
                return authProviderType;
            }
        }

        return DEFAULT;
    }

    public static PlatformType valueOf(int id) {
        PlatformType[] values = PlatformType.values();
        for (PlatformType authProviderType : values) {
            if(authProviderType.getId() == id) {
                return authProviderType;
            }
        }
        return DEFAULT;
    }
}
