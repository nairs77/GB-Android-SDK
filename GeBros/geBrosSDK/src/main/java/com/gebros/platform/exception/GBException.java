package com.gebros.platform.exception;

import com.joycity.platform.sdk.Joyple;
import com.joycity.platform.sdk.auth.model.common.JoypleAPIError;
import com.joycity.platform.sdk.auth.model.common.JoypleObject;
import com.joycity.platform.sdk.auth.net.Response;
import com.joycity.platform.sdk.internal.JR;
import com.joycity.platform.sdk.util.JoypleValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by nairs77@joycity.com on 2016-05-12.
 */
public class JoypleException extends Exception implements BaseException {


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

    private JoypleExceptionType exceptionType;
    private JoypleAPIError apiError;
    private String errorStr;

    public JoypleException(String message) {
        super(message);
    }

    public JoypleException(String message, Exception e) {
        super(message, e);
    }

    public JoypleException(JoypleExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public JoypleException(final int errorCode, final String detailError) {
        setAPIError(new JoypleAPIError() {
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
            public <T extends JoypleObject> T cast(Class<T> joypleObjectClass) {
                return null;
            }

            @Override
            public <T extends JoypleObject> T getInnerObject(Class<T> joypleObjectClass) {
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
            public JoypleAPIError getAPIError() {
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

    public void setErrorType(JoypleExceptionType exceptionType) {
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
                errorStr = Joyple.getApplicationContext().getResources().getString(JR.string("errorui_findpw_notexist_label_title"));
                break;
            case LOGIN_WRONG_PASSWORD_CODE:
                errorStr = Joyple.getApplicationContext().getResources().getString(JR.string("errorui_login_differpw_label_title"));
                break;
            case LOGIN_WITHDRAW_USER_CODE:
                errorStr = Joyple.getApplicationContext().getResources().getString(JR.string("errorui_common_withdraw_label_title"));
                break;
            case LOGIN_BLOCKED_USER_CODE:
                errorStr = Joyple.getApplicationContext().getResources().getString(JR.string("errorui_login_block_label_title"));
                break;
            case NETWORK_UNSTABLE_ERROR_CODE:
                errorStr = Joyple.getApplicationContext().getResources().getString(JR.string("joyple_alert_network_status"));
                break;
            case LOGIN_GOOGLE_AUTH_ERROR_CODE:
            case LOGIN_CN360_AUTH_ERROR_CODE:
            case LOGIN_THIRD_PARTY_AUTH_ERROR_CODE:
                errorStr = Joyple.getApplicationContext().getResources().getString(JR.string("alert_login_longwait_label_title"));
                break;
            case ACCOUNTS_CONNECT_EXISTS_GAMEINFO:
            case ACCOUNTS_CONNECT_SERVICED_ON:
                errorStr = Joyple.getApplicationContext().getResources().getString(JR.string("errorui_account_connected_label_title"));
                break;
            default:
                errorStr = Joyple.getApplicationContext().getResources().getString(JR.string("ui_main_default_error"));
        }
        if(JoypleValidator.isNullOrEmpty(errorStr)) {
            return exceptionType == null ? super.getMessage() : exceptionType.getErrorMessage();
        }
        return errorStr;
    }

    public void setMessage(String message) {
        this.errorStr = message;
    }

    public JoypleAPIError getAPIError() {
        return apiError;
    }

    public void setAPIError(JoypleAPIError apiError) {
        this.apiError = apiError;
    }

    public JoypleExceptionType getExceptionType() {
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

    public static JoypleAPIError getApiErrorTemplateJoypleObject(JoypleExceptionType exceptionType) {

        JSONObject json = new JSONObject();
        JSONObject errorJson = new JSONObject();

        try {

            errorJson.put(Response.ERROR_CODE_KEY, exceptionType.getErrorCode());
            errorJson.put(Response.ERROR_MESSAGE_KEY, exceptionType.getErrorMessage());
            json.put(Response.API_ERROR_KEY, errorJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JoypleObject joypleObject = JoypleObject.Factory.create(json);
        JoypleAPIError errorObject = joypleObject.getAPIError();

        return errorObject;
    }

    public static JoypleAPIError getApiErrorTemplateJoypleObject(JSONObject state) {

        JSONObject json = new JSONObject();
        JSONObject errorJson = new JSONObject();

        try {

            errorJson.put(Response.ERROR_CODE_KEY, state.optInt(Response.ERROR_CODE_KEY));
            errorJson.put(Response.ERROR_MESSAGE_KEY, state.optString(Response.ERROR_MESSAGE_KEY));
            json.put(Response.API_ERROR_KEY, errorJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JoypleObject joypleObject = JoypleObject.Factory.create(json);
        JoypleAPIError errorObject = joypleObject.getAPIError();

        return errorObject;
    }

    public static JoypleException getJoypleExceptionTemplate(JoypleExceptionType exceptionType) {

        JoypleException joypleException = new JoypleException(exceptionType);
        JoypleAPIError apiError = getApiErrorTemplateJoypleObject(exceptionType);
        joypleException.setAPIError(apiError);

        return joypleException;
    }

    @Override
    public String toString() {
        return exceptionType.getErrorMessage();
    }
}
