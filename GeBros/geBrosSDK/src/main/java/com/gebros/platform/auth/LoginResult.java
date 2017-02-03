package com.gebros.platform.auth;

import org.json.JSONObject;

/**
 *
 *
 * @author by gebros.nairs77@gmail.com on 5/12/16.
 */
public class LoginResult { //implements IResult {

    boolean success;
    int errorCode;
    String mAccessToken;
    String mUID;
    JSONObject originObject;
    int detailErrorCode;

    public static int SUCCESS = 0;
    public static int COMMON_LOGIN_FAILED = -8887;
    public static int COMMON_REALNAME_AUTH_FAILED = -8888;
    public static int COMMON_LOGOUT_FAILED = -8889;
    public static final String PLATFORM_ACCESS_TOKEN_KEY = "access_token";
    public static final String PLATFORM_UNIQUE_ID_KEY = "unique_id";
    public static final String PLATFORM_EXTRA_KEY = "extra";
    public static final String PALTFORM_ERROR_CODE_KEY = "error_code";
    public static final String EMPTY_VALUE = "";

    public static final int CHINA360_AUTH_DEFAULT_ERROR = -15000000;
    public static final int BAIDU_AUTH_DEFAULT_ERROR = -25000000;
    public static final int UC_AUTH_DEFAULT_ERROR = -35000000;
    public static final int WANDOUJIA_AUTH_DEFAULT_ERROR = -45000000;
    public static final int MI_AUTH_DEFAULT_ERROR = -55000000;
    public static final int HUAWEI_AUTH_DEFAULT_ERROR = -65000000;

    // China Auth Common error code
    public static final int LOGOUT_FAIL = -1105;    // UC, Wandoujia 전용 : 로그아웃 요청은 했으나 실패한 상태
    public static final int LOGIN_ACCESSTOKEN_NULL = -1101; // Sid(또는 accessToken) 가 없어서 예외
    public static final int ORDER_EXCEPTION = -1200;    // JSONObj 처리중 예외발생. 같은 exception 에러

    // Huawei Auth error code
    public static final int HUAWEI_LOGIN_SDK_INTERAL_ERROR = 3;
    public static final int HUAWEI_LOGIN_ALREADY_USING = 2;
    public static final int HUAWEI_LOGIN_SUCCESS = 1;
    public static final int HUAWEI_LOGIN_CANCEL = 0;
    public static final int HUAWEI_LOGIN_USERINFO_NULL = -100;
    public static final int HUAWEI_LOGIN_AUTH_RESULT_UNKNOWN = -13;
    public static final int HUAWEI_LOGIN_AUTH_RESULT_FAILED = -12;
    public static final int HUAWEI_LOGIN_AUTH_PARAMETER_BLANK = 10001;
    public static final int HUAWEI_LOGIN_AUTH_UPDATE_RESULT_FAILED = -16;
    public static final int HUAWEI_LOGIN_ACCESSTOKEN_NULL = -17;


    // china360 Auth error code
    public static final int CHINA360_LOGIN_AUTH_RESULT_UNKNOWN = -9999;
    public static final int CHINA360_REAL_NAME_REGISTER_UNKNOWN_USER = -9998;
    public static final int CHINA360_REAL_NAME_REGISTER_DATA_NULL = -9997;

    // Baidu Auth error code
    public static final int BAIDU_RESULT_NET_ERROR = -1;
    public static final int BAIDU_RESULT_PARSE_ERROR = -2;
    public static final int BAIDU_INIT_FAIL = -10;
    public static final int BAIDU_LOGIN_CANCEL = -20;
    public static final int BAIDU_LOGIN_FAIL = -21;
    public static final int BAIDU_SESSION_INVALID = 0;
    public static final int BAIDU_LOGIN_AUTH_RESULT_UNKNOWN = -13;
    public static final int BAIDU_LOGIN_AUTH_RESULT_QUERY_FAILED = -14;

    // UC Auth error code
    public static final int UC_CALLBACK_LISTENER_NULL = -1100;  // UC 전용 : callback listener 를 설정하지 않아 발생한 예외
    public static final int LOGOUT_NO_LOGIN = -1103;    // UC 전용 : 로그아웃 요청을 했지만, 로그인이 되어 있지 않음.
    public static final int LOGOUT_NO_INIT = -1104; // UC 전용 : 로그아웃 요청을 했지만, 초기화가 되어 있지 않은 상태.
    public static final int UC_FAIL = -2;
    public static final int UC_NO_INIT = -10;
    public static final int UC_NO_LOGIN = -11;
    public static final int UC_NO_NETWORK = -12;
    public static final int UC_INIT_FAIL = -100;
    public static final int UC_LOGIN_GAME_USER_AUTH_FAIL = -201;
    public static final int UC_LOGIN_GAME_USER_NETWORK_FAIL = -202;
    public static final int UC_LOGIN_GAME_USER_OTHER_FAIL = -203;
    public static final int UC_GETFRINDS_FAIL = -300;
    public static final int UC_VIP_FAIL = -400;
    public static final int UC_VIPINFO_FAIL = -401;
    public static final int UC_PAY_USER_EXIT = -500;
    public static final int UC_LOGIN_EXIT = -600;
    public static final int UC_SDK_OPEN = -700;
    public static final int UC_SDK_CLOSE = -701;
    public static final int UC_SDK_EXIT = -702;
    public static final int UC_SDK_EXIT_CONTINUE = -703;
    public static final int UC_GUEST = -800;
    public static final int UC_UC_ACCOUNT = -801;
    public static final int UC_BIND_EXIT = -900;

    // Xiaomi Auth error code
    public static final int MI_AUTH_MIACCOUNTINFO_NULL = -1102;
    public static final int MI_AUTH_ACCESSTOKEN_NULL = -1103;
    public static final int MI_AUTH_USERKEY_NULL = -1104;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CANCEL = -12;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CANCEL = -12;
    public static final int MI_XIAOMI_PAYMENT_ERROR_API_FAILED_TO_EXECUTE = -107;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_API_FAILED_TO_EXECUTE = -107;
    public static final int MI_XIAOMI_PAYMENT_ERROR_MITALK_API_EXCEPTION = -108;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CALL_API_REPEATED = -109;
    public static final int MI_XIAOMI_PAYMENT_ERROR_PAY_FAILURE = -18003;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE = -18003;
    public static final int MI_XIAOMI_PAYMENT_ERROR_PAY_CANCEL = -18004;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_PAY_CANCEL = -18004;
    public static final int MI_XIAOMI_PAYMENT_ERROR_PAY_REPEAT = -18005;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_PAY_REPEAT = -18005;
    public static final int MI_XIAOMI_PAYMENT_ERROR_ACTION_EXECUTED = -18006;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED = -18006;
    public static final int MI_XIAOMI_PAYMENT_ERROR_PAY_ASYN_SMS_SENT = -6004;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_PAY_ASYN_SMS_SENT = -6004;
    public static final int MI_XIAOMI_PAYMENT_ERROR_PAY_REQUEST_SUBMITTED = -4004;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_PAY_REQUEST_SUBMITTED = -4004;
    public static final int MI_XIAOMI_PAYMENT_ERROR_UNEXIST_ORDER = -19032;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_UNEXIST_ORDER = -19032;
    public static final int MI_XIAOMI_PAYMENT_ERROR_USER_SWITCH_ACCOUNT = -50;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_USER_SWITCH_ACCOUNT = -50;
    public static final int MI_XIAOMI_PAYMENT_ERROR_USER_RESTART = -51;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_USER_RESTART = -51;
    public static final int MI_XIAOMI_PAYMENT_ERROR_ACCOUNT_NOT_EXIST = -103;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_ACCOUNT_NOT_EXIST = -103;
    public static final int MI_XIAOMI_PAYMENT_ERROR_ACCOUNT_PASSWORD_ERROR = -104;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_ACCOUNT_PASSWORD_ERROR = -104;
    public static final int MI_XIAOMI_PAYMENT_ERROR_ACCOUNT_UNACTIVE_ERROR = -105;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_ACCOUNT_UNACTIVE_ERROR = -105;
    public static final int MI_XIAOMI_PAYMENT_ERROR_ACCOUNT_USE_GAME_ACCOUNT = -106;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_ACCOUNT_USE_GAME_ACCOUNT = -106;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CLIENT_APP_ID_INVALID = -6;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CLIENT_APP_ID_INVALID = -6;
    public static final int MI_XIAOMI_PAYMENT_ERROR_APP_ID_INVALID = -13001;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_APP_ID_INVALID = -13001;
    public static final int MI_XIAOMI_PAYMENT_ERROR_APP_KEY_INVALID = -8;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_APP_KEY_INVALID = -8;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CLIENT_INVALID = -20;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CLIENT_INVALID = -20;
    public static final int MI_XIAOMI_PAYMENT_ERROR_GOODS_ID_INVALID = -23004;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_GOODS_ID_INVALID = -23004;
    public static final int MI_XIAOMI_PAYMENT_ERROR_HAS_NOT_LOGIN = -11;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_HAS_NOT_LOGIN = -11;
    public static final int MI_XIAOMI_PAYMENT_ERROR_LOGIN_FAIL = -102;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_LOGIN_FAIL = -102;
    public static final int MI_XIAOMI_PAYMENT_ERROR_LOGINOUT_FAIL = -103;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_LOGINOUT_FAIL = -103;
    public static final int MI_XIAOMI_PAYMENT_ERROR_LOGINOUT_SUCCESS = -104;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_LOGINOUT_SUCCESS = -104;
    public static final int MI_XIAOMI_PAYMENT_ERROR_NO_SIM = -9;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_NO_SIM = -9;
    public static final int MI_XIAOMI_PAYMENT_ERROR_SERVER_RETURN_ERROR = -10;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_SERVER_RETURN_ERROR = -10;
    public static final int MI_XIAOMI_PAYMENT_ERROR_PAYFORCARD_FAILE = -20000;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_PAYFORCARD_FAILE = -20000;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CARDNUMERROR = -20001;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CARDNUMERROR = -20001;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CARDPASSERROR = -20002;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CARDPASSERROR = -20002;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CARDCARRISNULL = -20003;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CARDCARRISNULL = -20003;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CARDNUMORPASSERROR = -20004;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CARDNUMORPASSERROR = -20004;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CARDMONEYERROR = -20005;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CARDMONEYERROR = -20005;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CREATEORDERFAIL = -20006;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CREATEORDERFAIL = -20006;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CHARGE_FAIL = -20010;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CHARGE_FAIL = -20010;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CHARGE_CANCEL = -20011;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CHARGE_CANCEL = -20011;
    public static final int MI_XIAOMI_PAYMENT_ERROR_CARDNOENOUGHMONY = 20012;
    public static final int MI_XIAOMI_GAMECENTER_ERROR_CARDNOENOUGHMONY = 20012;

    public LoginResult(boolean isSuccess, int errorCode, String accessToken, String uID) {
        this.success = isSuccess;
        this.errorCode = errorCode;
        this.mAccessToken = accessToken;
        this.mUID = uID;
    }

    public LoginResult(boolean isSuccess, int errorCode, int detailErrorCode, JSONObject object) {

        this.success = isSuccess;
        this.errorCode = errorCode;
        this.detailErrorCode = detailErrorCode;
        this.originObject = object;

        if (success && (object != null)) {
            this.mAccessToken = object.optString(PLATFORM_ACCESS_TOKEN_KEY);
            this.mUID = object.optString(PLATFORM_UNIQUE_ID_KEY);
        }

/*
        String accessToken =  null;
        String uId = null;

        if (success) {
            accessToken = object.optString(PLATFORM_ACCESS_TOKEN_KEY);
            uId = object.optString(PLATFORM_UNIQUE_ID_KEY);
        }

        this(isSuccess, errorCode, accessToken, uId);
*/
    }

    public boolean isSuccess() {
        return success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int code) {
        this.errorCode = code;
    }

    public int getDetailErrorCode() { return detailErrorCode; }

    public void setErrorMsg(int detailCode) { this.detailErrorCode = detailCode; }

    public String toString() {
        return originObject.toString();
    }

    public String getAccessToekn() {
        return this.mAccessToken;
    }

    public String getUID() {
        return this.mUID;
    }

    public JSONObject toJSONObject() {
        return originObject;
    }
}
