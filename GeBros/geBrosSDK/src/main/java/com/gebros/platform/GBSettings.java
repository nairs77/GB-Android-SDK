package com.gebros.platform;

import com.gebros.platform.pay.Market;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBValidator;

/**
 * Created by gebros.nairs77@gmail.com on 5/6/16.
 *
 * in order to operate SDK, we maintained (create, update and delete)
 */

public final class GBSettings {

    public enum GameLanguageType {
        GameLanguageEnglish,
        GameLanguageKorean,
        GameLanguageJapanese,
        GameLanguageSimplifiedChinese,
        GameLanguageTraditionalChinese,
        GameLanguageGerman,
        GameLanguageFrench;

        public static GameLanguageType valueOf(int gameLanguagetype) {
            switch (gameLanguagetype) {
                case 1:
                    return GameLanguageType.GameLanguageEnglish;
                case 2:
                    return GameLanguageType.GameLanguageKorean;
                case 3:
                    return GameLanguageType.GameLanguageJapanese;
                case 4:
                    return GameLanguageType.GameLanguageSimplifiedChinese;
                case 5:
                    return GameLanguageType.GameLanguageTraditionalChinese;
                case 6:
                    return GameLanguageType.GameLanguageGerman;
                case 7:
                    return GameLanguageType.GameLanguageFrench;
                default:
                    return GameLanguageType.GameLanguageEnglish;
            }
        }

        public static String valueOf(GameLanguageType gameLanguageType) {
            if (gameLanguageType.equals(GameLanguageType.GameLanguageEnglish)) {
                return "en";
            } else if (gameLanguageType.equals(GameLanguageType.GameLanguageKorean)) {
                return "ko";
            } else if (gameLanguageType
                    .equals(GameLanguageType.GameLanguageJapanese)) {
                return "ja";
            } else if (gameLanguageType
                    .equals(GameLanguageType.GameLanguageSimplifiedChinese)) {
                return "zh";
            } else if (gameLanguageType
                    .equals(GameLanguageType.GameLanguageTraditionalChinese)) {
                return "zt";
            } else if (gameLanguageType.equals(GameLanguageType.GameLanguageGerman)) {
                return "de";
            } else if (gameLanguageType.equals(GameLanguageType.GameLanguageFrench)) {
                return "fr";
            } else {
                return "en";
            }
        }

    }

    static String ACCOUNTS_SERVER = "http://52.192.103.46:8000";
    static String CONTENTS_SERVER = "http://52.192.103.46:8000";
    static String PUSH_SERVER = "";
    static String IAB_SERVER ="";
    static String COMMON_API_SERVER ="";
    static String GAME_SERVER = "";
    static String GAME_CDN_SERVER = "";

    private GBConfig mConfig;

    GBSettings(GBConfig config) {
        this.mConfig = config;
    }

    //- Server Info

    public static String getAccountServer() {
        return ACCOUNTS_SERVER;
    }
    public static String getContentsServer() { return CONTENTS_SERVER; }
    public static String getIabServer() { return IAB_SERVER; }

    public static String getClientSecret() {
        return getCurrentSettings().getConfig().getAppKey();
    }

    public static PlatformType getPlatformType() { return getCurrentSettings().getConfig().getPlatformType(); }

    public static Market getMarket() { return getCurrentSettings().getConfig().getMarket(); }

    public static int getGameCode() {
        return getCurrentSettings().getConfig().getGameCode();
    }

    public static String getAppVersion() {
        return getCurrentSettings().getConfig().getAppVersion();
    }

    public static boolean getAgreementStatus() {
        return getCurrentSettings().getConfig().getAgreementStatus();
    }

    public static int getMarketCode() {
        return getCurrentSettings().getConfig().getMarket().getMarketCode();
    }

    private static GBSettings getCurrentSettings() {
        return GBSettingsProxy.getInstance().getCurrentSettings();
    }

    GBConfig getConfig() {
        GBValidator.notNull(mConfig, "ConfigurationError!!");
        return mConfig;
    }

}



