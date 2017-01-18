package com.gebros.platform.exception;

public class JoycityServerErrorCode {


	public static final int NOT_EXISTS_ACCESS_TOKEN = -11;
	public static final int NOT_EXISTS_REFRESH_TOKEN = -12;
	public static final int INVALID_ACCESS_TOKEN = -15;
	public static final int INVALID_REFRESH_TOKEN = -16;

	/**
	 * Account server error code
	 */

	public static final int UNREGISTERD_USER = -100;
	public static final int WRONG_PWD = -101;
	public static final int ADD_GAME_INFO_FAILED = -102;
	public static final int ADD_SESSION_FAILED = -103;
	public static final int QUIT_LOGIN_FAILED = -104;
	public static final int BLOCKED_LOGIN_FAILED = -105;
	public static final int LOGIN_FAILED = -106;
	public static final int USER_ADD_FAILED = -107;
	public static final int UNREGISTERD_GUEST_USER = -108;
	public static final int FACEBOOK_USER_DENIED = -109;
	public static final int INVALID_FACEBOOK_ACCESS_TOKEN = -110;
	public static final int NOT_EXISTS_LOGIN_TYPE = -111;
	public static final int FACEBOOK_TOKEN_UPDATE_FAILED = -112;	
	public static final int DUPLICATED_EMAIL = -113;
	public static final int NOT_MATCHED_PWD = -114;
	public static final int BAD_EMAIL = -115;
	public static final int UNRECOGNIZED_PHONE = -116;
	public static final int SEND_FAILED = -117;
	public static final int NOT_EXISTS_CERTKEY = -118;
	public static final int ERROR_CERTKEY = -119;
	public static final int ERROR_CERTKEY_EXPIRED = -120;
	public static final int ALREADY_EXISTS_GAMEINFO = -121;
	public static final int ALREADY_SERVICED_ON = -122;
	public static final int REQUIRE_EMAIL_CERT = -123;
	public static final int INVALID_PWD_LENGTH = -124;
	public static final int OVER_MAX_TIMES = -125;
	public static final int UNREGISTERD_UDID = -126;
	public static final int QUIT_USER = -127;
	public static final int UNAUTHENTICATED_PHONE = -128;
	public static final int GOOGLE_AUTH_ERROR = -129;
	public static final int GOOGLE_TOKEN_UPDATE_FAILED = -130;
	public static final int TWITTER_AUTH_ERROR = -131;
	public static final int TWITTER_TOKEN_UPDATE_FAILED = -132;
	public static final int NAVER_AUTH_ERROR = -133;
	public static final int NAVER_TOKEN_UPDATE_FAILED = -134;
	public static final int DUPLICATE_TRANSACTION = -135;
	public static final int INVALID_NICKNAME = -136;
	public static final int ALEADY_EXISTS_UID = -137;
	public static final int ALEADY_EXISTS_NICKNAME = -138;
	public static final int ADD_USER_VERIFY_INFO_FAILED = -139;
	public static final int NETWORK_ERROR = -140;
	public static final int NON_CERT_EMAIL = -141;
	
	/**
	 * Contents server error code
	 */
	
	public static final int LETTER_EXCEED_MAX_LENGTH = -200;
	public static final int LETTER_LACK_MIN_LENGTH = -201;
	public static final int INCLUDE_PROHIBITED_WORDS = -202;
	public static final int DUPLICATED_MDN_ERROR = -203;
	public static final int DEVICE_COLLECT_STATE_INVALID = -218;

}
