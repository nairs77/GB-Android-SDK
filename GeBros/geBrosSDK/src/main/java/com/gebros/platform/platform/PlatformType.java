package com.gebros.platform.platform;

import java.util.EnumSet;
import com.gebros.platform.auth.AuthType;
import com.gebros.platform.pay.Market;
import java.util.Set;

/**
 * Created by gebros.nairs77@gmail.com on 5/6/16.
 */
public enum PlatformType {
    DEFAULT("GB", EnumSet.of(AuthType.FACEBOOK, AuthType.GOOGLE, AuthType.GUEST), Market.GOOGLE, 0),
    GB_ONESTORE("GB", EnumSet.of(AuthType.FACEBOOK, AuthType.GOOGLE, AuthType.GUEST ), Market.ONESTORE, Market.ONESTORE.getMarketCode()),
    CHINA360 ("China360", EnumSet.of(AuthType.CHINA360), Market.CHINA360, Market.CHINA360.getMarketCode()),
    BAIDU ("Baidu", EnumSet.of(AuthType.BAIDU), Market.BAIDU, Market.BAIDU.getMarketCode()),
    XIAOMI ("Xiaomi", EnumSet.of(AuthType.XIAOMI), Market.XIAOMI, Market.XIAOMI.getMarketCode()),
    UC ("UC", EnumSet.of(AuthType.UC), Market.UC, Market.UC.getMarketCode()),
    WANDOUJIA ("Wandoujia", EnumSet.of(AuthType.WANDOUJIA), Market.WANDOUJIA, Market.WANDOUJIA.getMarketCode()),
    HUAWEI ("Huawei", EnumSet.of(AuthType.HUAWEI), Market.HUAWEI, Market.HUAWEI.getMarketCode()),
    GB_MYCARD ("GB", EnumSet.of(AuthType.FACEBOOK, AuthType.GOOGLE, AuthType.GUEST ), Market.MYCARD, Market.MYCARD.getMarketCode());

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
/*
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
        MYCARD(21);

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
*/
/*
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
*/
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
