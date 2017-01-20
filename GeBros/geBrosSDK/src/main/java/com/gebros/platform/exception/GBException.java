package com.gebros.platform.exception;

import com.gebros.platform.GBSdk;
import com.gebros.platform.auth.model.common.GBAPIError;
import com.gebros.platform.auth.model.common.GBObject;
import com.gebros.platform.auth.net.Response;
import com.gebros.platform.internal.JR;
import com.gebros.platform.util.GBValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by nairs77@joycity.com on 2016-05-12.
 */
public class GBException extends Exception implements BaseException {


    /**
     * Accounts Login API response type
     */
    public static final int LOGIN_UNREGISTERD_USER_CODE = -100;
    public static final int LOGIN_WRONG_PASSWORD_CODE = -101;
    public static final int LOGIN_ADD_GAME_INFO_FAILED_CODE = -102;
    public static final int LOGIN_STORE_SESSION_FAILED_CODE = -103;
    public static final int LOGIN_WITHDRAW_USER_CODE = -104;
    public static final int LOGIN_BLOCKED_USER_CODE = -105;
    public static final int LOGIN_FAILED_CODE = -106;
    public static final int LOGIN_ADD_USER_FAILED_CODE = -107;
    public static final int LOGIN_UNREGISTERD_GUEST_USER_CODE = -108;

    public static final int LOGIN_FACEBOOK_USER_DENIED_CODE = -109;
    public static final int LOGIN_INVALID_FACEBOOK_ACCESS_TOKEN_CODE = -110;
    public static final int LOGIN_NOT_EXIST_LOGIN_TYPE_CODE = -111;
    public static final int LOGIN_FACEBOOK_TOKEN_UPDATE_FAILED_CODE = -112;
    public static final int LOGIN_GOOGLE_AUTH_ERROR_CODE = -129;
    public static final int LOGIN_GOOGLE_TOKEN_UPDATE_FAILED_CODE = -130;
    public static final int LOGIN_TWITTER_AUTH_ERROR_CODE = -131;
    public static final int LOGIN_TWITTER_TOKEN_UPDATE_FAILED_CODE = -132;
    public static final int LOGIN_NAVER_AUTH_ERROR_CODE = -133;
    public static final int LOGIN_NAVER_TOKEN_UPDATE_FAILED_CODE = -134;
    public static final int LOGIN_CN360_AUTH_ERROR_CODE = -143;
    public static final int LOGIN_CN360_TOKEN_UPDATE_FAILED = -144;
    public static final int LOGIN_THIRD_PARTY_AUTH_ERROR_CODE = -145;
    public static final int LOGIN_THIRD_PARTY_TOKEN_UPDATE_FAILED = -146;

    public static final int JOIN_DUPLICATED_EMAIL = -113;
    public static final int ACCOUNTS_CONNECT_EXISTS_GAMEINFO = -121;
    public static final int ACCOUNTS_CONNECT_SERVICED_ON = -122;
    public static final int NETWORK_UNSTABLE_ERROR_CODE = -400;

    private GBExceptionType exceptionType;
    private GBAPIError apiError;
    private String errorStr;

    public GBException(String message) {
        super(message);
    }

    public GBException(String message, Exception e) {
        super(message, e);
    }

    public GBException(GBExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public GBException(final int errorCode, final String detailError) {
        setAPIError(new GBAPIError() {
            @Override
            public String getErrorType() {
                return null;
            }

            @Override
            public void setErrorType(String errorType) {

            }

            @Override
            public int getErrorCode() {
                return errorCode;
            }

            @Override
            public void setErrorCode(int errorCode) {

            }

            @Override
            public String getDetailError() {
                return detailError;
            }

            @Override
            public void setDetailError(String detailError) {

            }

            @Override
            public <T extends GBObject> T cast(Class<T> joypleObjectClass) {
                return null;
            }

            @Override
            public <T extends GBObject> T getInnerObject(Class<T> joypleObjectClass) {
                return null;
            }

            @Override
            public Map<String, Object> asMap() {
                return null;
            }

            @Override
            public JSONObject getInnerJSONObject() {
                return null;
            }

            @Override
            public GBAPIError getAPIError() {
                return null;
            }

            @Override
            public String getString(String propertyName) {
                return null;
            }

            @Override
            public int getInt(String propertyName) {
                return 0;
            }

            @Override
            public long getLong(String propertyName) {
                return 0;
            }

            @Override
            public Object getProperty(String propertyName) {
                return null;
            }

            @Override
            public void setProperty(String propertyName, Object propertyValue) {

            }

            @Override
            public void removeProperty(String propertyName) {

            }
        });
    }

    public void setErrorType(GBExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public int getErrorCode() {
        return getAPIError().getErrorCode();
    }

    @Override
    public String getDetailError() {
        return getAPIError().getDetailError();
    }

    @Override
    public String getMessage() {
        switch (getErrorCode()) {
            case LOGIN_UNREGISTERD_USER_CODE:
                errorStr = GBSdk.getApplicationContext().getResources().getString(JR.string("errorui_findpw_notexist_label_title"));
                break;
            case LOGIN_WRONG_PASSWORD_CODE:
                errorStr = GBSdk.getApplicationContext().getResources().getString(JR.string("errorui_login_differpw_label_title"));
                break;
            case LOGIN_WITHDRAW_USER_CODE:
                errorStr = GBSdk.getApplicationContext().getResources().getString(JR.string("errorui_common_withdraw_label_title"));
                break;
            case LOGIN_BLOCKED_USER_CODE:
                errorStr = GBSdk.getApplicationContext().getResources().getString(JR.string("errorui_login_block_label_title"));
                break;
            case NETWORK_UNSTABLE_ERROR_CODE:
                errorStr = GBSdk.getApplicationContext().getResources().getString(JR.string("joyple_alert_network_status"));
                break;
            case LOGIN_GOOGLE_AUTH_ERROR_CODE:
            case LOGIN_CN360_AUTH_ERROR_CODE:
            case LOGIN_THIRD_PARTY_AUTH_ERROR_CODE:
                errorStr = GBSdk.getApplicationContext().getResources().getString(JR.string("alert_login_longwait_label_title"));
                break;
            case ACCOUNTS_CONNECT_EXISTS_GAMEINFO:
            case ACCOUNTS_CONNECT_SERVICED_ON:
                errorStr = GBSdk.getApplicationContext().getResources().getString(JR.string("errorui_account_connected_label_title"));
                break;
            default:
                errorStr = GBSdk.getApplicationContext().getResources().getString(JR.string("ui_main_default_error"));
        }
        if(GBValidator.isNullOrEmpty(errorStr)) {
            return exceptionType == null ? super.getMessage() : exceptionType.getErrorMessage();
        }
        return errorStr;
    }

    public void setMessage(String message) {
        this.errorStr = message;
    }

    public GBAPIError getAPIError() {
        return apiError;
    }

    public void setAPIError(GBAPIError apiError) {
        this.apiError = apiError;
    }

    public GBExceptionType getExceptionType() {
        return exceptionType;
    }

    public int getApiErrResId() {
        int messageResId = 0;
        if(apiError == null) {
            return messageResId;
        }
        int errorCode = apiError.getErrorCode();
        switch(errorCode) {
            case LOGIN_UNREGISTERD_USER_CODE:
                messageResId = JR.string("errorui_findpw_notexist_label_title");
                break;
            case LOGIN_WRONG_PASSWORD_CODE:
                messageResId = JR.string("errorui_login_differpw_label_title");
                break;
            case LOGIN_WITHDRAW_USER_CODE:
                messageResId = JR.string("errorui_common_withdraw_label_title");
                break;
            case LOGIN_BLOCKED_USER_CODE:
                messageResId = JR.string("errorui_login_block_label_title");
                break;
            case NETWORK_UNSTABLE_ERROR_CODE:
                messageResId = JR.string("joyple_alert_network_status");
                break;
            case LOGIN_GOOGLE_AUTH_ERROR_CODE:
            case LOGIN_CN360_AUTH_ERROR_CODE:
            case LOGIN_THIRD_PARTY_AUTH_ERROR_CODE:
                messageResId = JR.string("alert_login_longwait_label_title");
                break;
            case ACCOUNTS_CONNECT_EXISTS_GAMEINFO:
            case ACCOUNTS_CONNECT_SERVICED_ON:
                messageResId = JR.string("errorui_account_connected_label_title");
                break;
            default:
                messageResId = JR.string("joyple_alert_server_status");
        }
        return messageResId;
    }

    public static GBAPIError getApiErrorTemplateGBObject(GBExceptionType exceptionType) {

        JSONObject json = new JSONObject();
        JSONObject errorJson = new JSONObject();

        try {

            errorJson.put(Response.ERROR_CODE_KEY, exceptionType.getErrorCode());
            errorJson.put(Response.ERROR_MESSAGE_KEY, exceptionType.getErrorMessage());
            json.put(Response.API_ERROR_KEY, errorJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GBObject gbObject = GBObject.Factory.create(json);
        GBAPIError errorObject = gbObject.getAPIError();

        return errorObject;
    }

    public static GBAPIError getApiErrorTemplateGBObject(JSONObject state) {

        JSONObject json = new JSONObject();
        JSONObject errorJson = new JSONObject();

        try {

            errorJson.put(Response.ERROR_CODE_KEY, state.optInt(Response.ERROR_CODE_KEY));
            errorJson.put(Response.ERROR_MESSAGE_KEY, state.optString(Response.ERROR_MESSAGE_KEY));
            json.put(Response.API_ERROR_KEY, errorJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GBObject gbObject = GBObject.Factory.create(json);
        GBAPIError errorObject = gbObject.getAPIError();

        return errorObject;
    }

    public static GBException getGBExceptionTemplate(GBExceptionType exceptionType) {

        GBException GBException = new GBException(exceptionType);
        GBAPIError apiError = getApiErrorTemplateGBObject(exceptionType);
        GBException.setAPIError(apiError);

        return GBException;
    }

    @Override
    public String toString() {
        return exceptionType.getErrorMessage();
    }
}
