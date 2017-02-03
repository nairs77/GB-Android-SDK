package com.gebros.platform.platform;

/**
 * Created by gebros.nairs77@gmail.com on 5/17/16.
 */


public class Platform {

    private PlatformType platformType;
    private String pAppId;
    private String pAppKey;
    private String pAppSecret;

    //TODO : 마켓 서버 테스트 모드 셋팅
    private boolean pMarketTestMode;

    // TODO : xaomi 에서만 쓰이는 정보들
    private String pCpId;
    private String pBuoSecret;
    private String pPayId;
    private String pPayRsaPrivate;
    private String pPayRsaPublic;

    public PlatformType getPlatformType() {
        return platformType;
    }

    public String getAppId() {
        return pAppId;
    }

    public String getAppKey() {
        return pAppKey;
    }

    public String getAppSecret() {
        return pAppSecret;
    }

    public String getCpId() { return pCpId; }

    public String getBuoSecret() { return pBuoSecret; }

    public String getPayId() { return pPayId; }

    public String getPayRsaPrivate() { return pPayRsaPrivate; }

    public String getPayRsaPublic() { return pPayRsaPublic; }

    public boolean getMarketTestMode() {return pMarketTestMode; }

    public static class Builder {
        private PlatformType type;
        private String appKey;
        private String appId;
        private String appSecret;

        private boolean marketTestMode;

        private String cpId;
        private String buoSecret;
        private String payId;
        private String payRsaPrivate;
        private String payRsaPublic;

        public Builder(String appId, String appKey) {
            this.appId = appId;
            this.appKey = appKey;
        }

        public Builder(PlatformType type) {
            this.type = type;
        }

        public Builder PlatformType(PlatformType type) {
            this.type = type;
            return this;
        }

        public Builder AppId(String appId) {
            this.appId = appId;

            return this;
        }

        public Builder AppKey(String appKey) {
            this.appKey = appKey;
            return this;
        }

        public Builder AppSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }

        public Builder MarketTestMode(boolean marketTestMode) {
            this.marketTestMode = marketTestMode;
            return this;
        }

        public Builder CpId(String cpId) {
            this.cpId = cpId;
            return this;
        }

        public Builder BuoSecret(String buoSecret) {
            this.buoSecret = buoSecret;
            return this;
        }

        public Builder PayId(String payId) {
            this.payId = payId;
            return this;
        }

        public Builder PayRsaPrivate(String payRsaPrivate) {
            this.payRsaPrivate = payRsaPrivate;
            return this;
        }

        public Builder PayRsaPublic(String payRsaPublic) {
            this.payRsaPublic = payRsaPublic;
            return this;
        }

        public Platform build() {
            return new Platform(this);
        }
    }

    protected Platform(Builder builder) {
        this.platformType = builder.type;
        this.pAppKey = builder.appKey;
        this.pAppId = builder.appId;
        this.pAppSecret = builder.appSecret;

        this.pMarketTestMode = builder.marketTestMode;

        this.pCpId = builder.cpId;
        this.pBuoSecret = builder.buoSecret;
        this.pPayId = builder.payId;
        this.pPayRsaPrivate = builder.payRsaPrivate;
        this.pPayRsaPublic = builder.payRsaPublic;
    }
}
