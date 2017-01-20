/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gebros.platform.pay;

import android.content.Context;

import com.gebros.platform.internal.JR;

/**
 * Represents the result of an in-app billing operation.
 * A result is composed of a response code (an integer) and possibly a
 * message (String). You can get those by calling
 * {@link #getResponse} and {@link #getMessage()}, respectively. You
 * can also inquire whether a result is a success or a failure by
 * calling {@link #isSuccess()} and {@link #isFailure()}.
 */
public class IabResult {

    // Billing response codes
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;

    // China Common Error codes
    public static final int ORDER_EXCEPTION = -1200;    // JSONObj 처리중 예외발생. 같은 exception 에러
    public static int COMMON_PAY_FAILED = -8890;
    // China360 Error codes
    public static final int CHINA360_PAY_DEFAULT_ERROR = -10000000;
    public static final int CHINA360_PAY_ACCESSTOKEN_INVAILD = 4010201; // 360 전용 : 결제시도시 accessToken invaild 에러
    public static final int CHINA360_PAY_QT_INVAILD = 4009911;  // 360 전용 : 결제시도시 QT invaild 에러
    public static final int CHINA360_PAY_USER_CANCELED = -1;    // 360 전용 : 결제 진행중 사용자가 결제 취소
    public static final int CHINA360_PAY_BILLING_FAIL = -2;     // 360 전용 : 결제 진행 중 에러 발

    // Baidu Error codes
    public static final int BAIDU_PAY_DEFAULT_ERROR = -20000000;
    public static final int BAIDU_PAY_CANCEL = -30;
    public static final int BAIDU_PAY_FAIL = -31;
    public static final int BAIDU_PAY_SUBMIT_ORDER = -32;
    public static final int BAIDU_PAY_LOGOUT_CANCEL_ERROR = -33;
    public static final int BAIDU_PAY_LOGIN_CANCEL_ERROR = -34;

    // UC Error codes
    public static final int UC_PAY_DEFAULT_ERROR = -30000000;
    public static final int UC_CALLBACK_LISTENER_NULL = -1100;  // UC 전용 : callback listener 를 설정하지 않아 발생한 예외
    public static final int PAY_ORDERINFO_NULL = -1102; // UC 전용 : 결제는 성공했다는 코드가 왔지만, 제결정보가 담긴 orderInfo 객체가 Null 이라 accessToken을 받지 못하는 경우의 에러

    // Wandoujia Error codes
    public static final int WANDOUJIA_PAY_DEFAULT_ERROR = -40000000;
    public static final int WANDOUJIA_PAY_FAIL = -1201; // wandoujia 전용 : Pay Fail 에러
    public static final int WANDOUJIA_PAY_LOGOUT_CANCEL_ERROR = -1202;
    public static final int WANDOUJIA_PAY_LOGIN_CANCEL_ERROR = -1203;

    // Huawei Error codes
    public static final int HUAWEI_PAY_DEFAULT_ERROR = -60000000;
    public static final int HUAWEI_PAY_FAILED_INITIALIZE_PAYMENT = 200100;
    public static final int HUAWEI_PAY_CANCELED = 200001;
    public static final int HUAWEI_PAY_INCORRECT_PARAM = 30001;
    public static final int HUAWEI_PAY_TIME_OUT = 30002;
    public static final int HUAWEI_PAY_INVALID_REQUEST = 30004;
    public static final int HUAWEI_PAY_NETWORK_CONNECTION_ERROR = 30005;
    public static final int HUAWEI_PAY_SYSTEM_UPGRADE = 30006;
    public static final int HUAWEI_PAY_ORDER_EXPIRED = 30007;
    public static final int HUAWEI_PAY_LOGIN_FAILED = 30008;
    public static final int HUAWEI_PAY_SYSTEM_ERROR = 30099;
    public static final int HUAWEI_PAY_FAIL = -1;
    public static final int HUAWEI_PAY_RETURN_CODE_NULL = -2;
    public static final int HUAWEI_PAY_ORDER_ID_NULL = -3;

    // Xaomi Error codes
    public static final int MI_PAY_DEFAULT_ERROR = -50000000;
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

    // UC Error codes
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

    /*
     * Google, OneStore 에러코드 모음
     */
    // Failed to initialize
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;

    // Invalid item
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;

    // Invalid Argument, Incorrect Setting
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;

    // General Error
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;

    // Duplicate Error (by Google)
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;

    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;
    public static final int BILLING_NOT_SUPPORTED_MARKET = 9;
    public static final int BILLING_NETWORK_NOT_CONNECTED = 10;

    // IAB(Google) Helper error codes
    public static final int IABHELPER_ERROR_BASE = -1000;
    public static final int IABHELPER_REMOTE_EXCEPTION = -1001;
    public static final int IABHELPER_BAD_RESPONSE = -1002;
    public static final int IABHELPER_VERIFICATION_FAILED = -1003;
    public static final int IABHELPER_SEND_INTENT_FAILED = -1004;
    public static final int IABHELPER_USER_CANCELLED = -1005;
    public static final int IABHELPER_UNKNOWN_PURCHASE_RESPONSE = -1006;
    public static final int IABHELPER_MISSING_TOKEN = -1007;
    public static final int IABHELPER_UNKNOWN_ERROR = -1008;
    public static final int IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE = -1009;
    public static final int IABHELPER_INVALID_CONSUMPTION = -1010;
    // TSTORE error codes
    public static final int TSTORE_DEFAULT_ERROR = -2000;
    public static final int TSTORE_UNUSUAL_ERROR_CASE = -2011;
    public static final int TSTORE_INVAILD_RESPONSE_DATA = -2012;

    public static final int TSTORE_ONERROR_INTERNAL_ERROR = -2001;
    public static final int TSTORE_ONERROR_WEBVIEW_ERROR = -2100;
    public static final int TSTORE_ONERROR_USER_REQUEST_CANCELED = -2101;

    public static final int TSTORE_REQUEST_FAILED_BUNDLE_NULL = -2013;
    public static final int TSTORE_REQUEST_FAILED_REQUEST_ID_NULL = -2014;
    public static final int TSTORE_REQUEST_FAILED_INTERNAL_ERROR = -2201;
    public static final int TSTORE_REQUEST_FAILED_IAPWEB_ACTIVITY = -2202;
    public static final int TSTORE_REQUEST_FAILED_ADD_MATA_DATA = -2203;
    public static final int TSTORE_REQUEST_FAILED_NETWORK_UNAVAILABLE = -2204;
    public static final int TSTORE_REQUEST_FAILED_VAILDATION_ERROR = -2205;
    public static final int TSTORE_REQUEST_FAILED_CHECK_PERMISSIONS = -2206;
    
    
    int mResponse;
    String mMessage;

    public IabResult(int response, String message, Context context) {
        mResponse = response;
        mMessage = message;
        /* Google 결를 위한 전용 코드라 주석 처리 함
        if (message == null || message.trim().length() == 0) {
            mMessage = getResponseDesc(response, context);
        }
        else {
        	if (response <= TSTORE_DEFAULT_ERROR) {
        		GBLog.d("[Billing], Message = %s", message);
        		mMessage = message; 
        	} else {
                mMessage = message + " (response: " + getResponseDesc(response, context) + ")";
        	}
        }
        */
    }

    public IabResult(int response, String message) {
        mResponse = response;
        mMessage = message;
    }

    public int getResponse() { return mResponse; }
    public String getMessage() { return mMessage; }
    public boolean isSuccess() { return mResponse == BILLING_RESPONSE_RESULT_OK; }
    public boolean isFailure() { return !isSuccess(); }
    public String toString() { return "IabResult: " + getMessage(); }


    /**
     * Returns a human-readable description for the given response code.
     *
     * @param code The response code
     * @return A human-readable string explaining the result code.
     *     It also includes the result code numerically.
     */
    public static String getResponseDesc(int code, Context context) {

        String[] iab_msgs = ("0:OK/"+
        		context.getResources().getString(JR.string("errorui_iab_cancelbuy_item_table_title"))+"/"+
        		"2:Unknown/" +
                "3:"+context.getResources().getString(JR.string("errorui_iab_purchase_restart_label_title")) + "/" +
                "4:"+context.getResources().getString(JR.string("errorui_iab_cannotbuy_item_label_title"))+"/" +
                "5:Developer Error/"+
                "6:"+context.getResources().getString(JR.string("errorui_iab_purchase_restart_label_title")) + "/" +
                context.getResources().getString(JR.string("errorui_iab_existbuy_item_label_title"))+"/" +
                "8:"+context.getResources().getString(JR.string("errorui_iab_cannotbuy_item_label_title"))+"/" +
                context.getResources().getString(JR.string("errorui_iab_notsupport_market_label_title"))+"/" +
                "10:"+context.getResources().getString(JR.string("errorui_iab_purchase_restart_label_title"))).split("/");
        String[] iabhelper_msgs = ("0:OK/-1001:Remote exception during initialization/" +
                "-1002:Bad response received/" +
                "-1003:IabPurchase signature verification failed/" +
                "-1004:Send intent failed/" +
                "-1005:"+context.getResources().getString(JR.string("errorui_iab_cancelbuy_item_table_title"))+"/"+
                "-1006:Unknown purchase response/" +
                "-1007:Missing token/" +
                "-1008:Unknown error/" +
                "-1009:Subscriptions not available/" +
                "-1010:Invalid consumption attempt").split("/");

        if (code <= IABHELPER_ERROR_BASE) {
            int index = IABHELPER_ERROR_BASE - code;
            if (index >= 0 && index < iabhelper_msgs.length) return iabhelper_msgs[index];
            else return String.valueOf(code) + ":Unknown IAB Helper Error";
        } else if (code < 0 || code >= iab_msgs.length)
            return String.valueOf(code) + ":Unknown";
        else
            return iab_msgs[code];
    }
}
