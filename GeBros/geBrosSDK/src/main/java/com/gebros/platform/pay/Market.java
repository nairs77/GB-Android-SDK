package com.gebros.platform.pay;

/**
 * Created by gebros.nairs77@gmail.com on 2017. 2. 3..
 */

public enum Market {
    GOOGLE(1, 1),
    APPLE(2, 2),
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
