package com.gebros.platform.auth;

/**
 * Created by Joycity-Platform on 5/11/16.
 */
public enum SessionJoinSource {
    GUEST(1),
    NEST(2),
    GB(3),
    FACEBOOK(4),
    GOOGLE(5),
    TWITTER(6),
    NAVER(7),
    GOOGLE_PLAY(8),
    REFRESH_TOKEN(9),
    JOIN(10),
    RETRY_API(99),
    CHINA360(11),
    BAIDU(12),
    XIAOMI(13),
    UC(14),
    WANDOUJIA(15),
    HUAWEI(17),
    NONE(100);

    private int id;

    SessionJoinSource(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static SessionJoinSource valueOf(int id) {
        SessionJoinSource[] values = SessionJoinSource.values();
        for (SessionJoinSource source : values) {
            if(source.getId() == id) {
                return source;
            }
        }

        return NONE;
    }
}
