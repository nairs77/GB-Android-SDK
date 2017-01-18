package com.gebros.platform;

import android.content.Context;
import android.content.SharedPreferences;

import com.joycity.platform.sdk.log.JLog;
import com.joycity.platform.sdk.platform.PlatformType;

import java.util.Date;


public final class GBConfig {

	private static final String TAG = GBConfig.class.getCanonicalName();
	private GameInfoManager mGameInfo;

	private String mAppId;
	private String mAppKey;
	private String mAppSecret;

	static final String CACHED_SETTING_KEY = "cache.com.joycity.platform.PreferenceKey";

	static final String AGREEMENT_STATUS_NAME = "agreementStatus";
	static final String AGREEMENT_PUSH_MARKETING_NAME = "agreementPushMarketing";
	static final String AGREEMENT_PRIVACY_MOVE_NAME = "agreementPrivacyMove";
	static final String ALREADY_LOGIN_NAME = "alreadyLogin";
	static final String GAME_LANGUAGE_NAME = "gameLanguage";
	static final String CONNECTED_PLAYGAMES = "connected_pgs";
	static final String HASH_ADVERTISING_ID = "hashAdvertisingId";
	static final String COLLECT_ADVERTISING_ID_STATE = "collectAdvertisingIdState";

	GBConfig(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}

	private SharedPreferences sharedPreferences;

	public GBConfig() {
		this(
				Joyple.getApplicationContext().getSharedPreferences(
						CACHED_SETTING_KEY,
						Context.MODE_PRIVATE)
		);
	}

	GBSettings load() {

		if (!isInstalled()) {
			// TODO : 처음 앱을 실행(?)
			// 현재 시간을 installed 시간으로 처리
			JLog.d("First installed !!!");
		}

		// TODO : APP START TIME
		JLog.d(TAG + "App Start Time = %s", new Date().toString());

		return new GBSettings(this);
	}

	private boolean isInstalled() {
		return sharedPreferences.contains(CACHED_SETTING_KEY);
	}

	void setSDKInfo(String appKey, int gameCode, String gameVersion, PlatformType platformType) {
		mGameInfo = new GameInfoManager(appKey, gameCode, gameVersion, platformType);
	}

	void setGameLanguage(int gameLanguageValue) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(GAME_LANGUAGE_NAME, gameLanguageValue);
		editor.commit();
	}
	// - Agreement Status
	public boolean getAgreementStatus() {
		return sharedPreferences.getBoolean(AGREEMENT_STATUS_NAME, false);
	}

	public void setAgreementStatus(boolean isEnable) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(AGREEMENT_STATUS_NAME, isEnable);
		editor.commit();
	}

	public void setThirdPartyInfo(String appId, String appKey, String appSecret) {
		this.mAppId = appId;
		this.mAppKey = appKey;
		this.mAppSecret = appSecret;
	}

	public void setAppId(String appId) {
		this.mAppId = appId;
	}

	public void setAppKey(String appKey) {
		this.mAppKey = appKey;
	}

	public void setAppSecret(String appSecret) {
		this.mAppSecret = appSecret;
	}

	// - Game Information
	public String getAppKey() {
		return mGameInfo.clientSecretKey;
	}

	public int getGameCode() {
		return mGameInfo.gameCode;
	}

	public String getAppVersion() {
		return mGameInfo.gameVersion;
	}

	public PlatformType getPlatformType() {
		return mGameInfo.platformType;
	}

	public PlatformType.Market getMarket() { return mGameInfo.platformType.getMarket(); }

	public String getPlatformName() {
		return mGameInfo.platformType.getName();
	}

	public String getThirdPartyAppkey() {
		return this.mAppKey;
	}

	public String getThirdPartyAppSecret() {
		return this.mAppSecret;
	}

	public String getThirdPartyAppId()  {
		return this.mAppId;
	}


/*
	static GBConfig createFromJSONObject(JSONObject jsonObject) throws JSONException {
		boolean agreementStatus = jsonObject.getBoolean(GBSettingsProxy.AGREEMENT_STATUS_NAME);
		boolean agreementPushMarket = jsonObject.getBoolean(GBSettingsProxy.AGREEMENT_PUSH_MARKETING_NAME);
		boolean alreadyLogin = jsonObject.getBoolean(GBSettingsProxy.ALREADY_LOGIN_NAME);
		boolean agreementPrivacyMove = jsonObject.getBoolean(GBSettingsProxy.AGREEMENT_PRIVACY_MOVE_NAME);
		String advertiseId = jsonObject.getString(GBSettingsProxy.HASH_ADVERTISING_ID);
		String gameLanguage = jsonObject.getString(GBSettingsProxy.GAME_LANGUAGE_NAME);
		int collectionAdIdState = jsonObject.getInt(GBSettingsProxy.COLLECT_ADVERTISING_ID_STATE);
		boolean allowedPGS = jsonObject.getBoolean(GBSettingsProxy.CONNECTED_PLAYGAMES);

		return new GBSettings();
	}

	private GBConfig getCachedSettings() {
		String jsonString = sharedPreferences.getString(GBSettingsProxy.CACHED_SETTING_KEY, null);

		if (jsonString != null) {
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				return GBConfig.createFromJSONObject(jsonObject);
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}
*/


	private class GameInfoManager {
		public final String clientSecretKey;
		public final int gameCode;
		public final String gameVersion;
		public final PlatformType platformType;

		GameInfoManager(String clientSecretKey, int gameCode, String gameVersion, PlatformType type) {
			this.clientSecretKey = clientSecretKey;
			this.gameCode = gameCode;
			this.gameVersion = gameVersion;
			this.platformType = type;
		}
	}
}
