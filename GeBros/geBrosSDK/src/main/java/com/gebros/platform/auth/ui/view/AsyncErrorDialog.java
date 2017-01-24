package com.gebros.platform.auth.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.gebros.platform.auth.net.Response;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.internal.JR;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class AsyncErrorDialog {

    /**
     * Accounts API response type
     */
    private Activity activity;
    private Toast toast;

    @SuppressLint("ShowToast")
    public AsyncErrorDialog(Activity activity) {

        this.activity = activity;

        if (toast == null) {
            toast = Toast.makeText(activity, JR.string("GB_alert_server_status"), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
    }

    public AsyncErrorDialog(Activity activity, int messageResId) {

        this(activity, activity.getString(messageResId));
    }

    public AsyncErrorDialog(Activity activity, String errorMessage) {

        this(activity);
        toast.setText(errorMessage);
    }

    public void show() {

        View v = toast.getView();

        if ((v == null) || (!(v.isShown())))
            toast.show();
    }

    public void show(int errorCode) {

        int messageResId = 0;
        boolean isDefault = false;
        switch(errorCode) {

            case GBException.LOGIN_UNREGISTERD_USER_CODE:
                messageResId = JR.string("errorui_findpw_notexist_label_title");
                break;
            case GBException.LOGIN_WRONG_PASSWORD_CODE:
                messageResId = JR.string("errorui_login_differpw_label_title");
                break;
            case GBException.LOGIN_WITHDRAW_USER_CODE:
                messageResId = JR.string("errorui_common_withdraw_label_title");
                break;
            case GBException.LOGIN_BLOCKED_USER_CODE:
                messageResId = JR.string("errorui_login_block_label_title");
                break;
            case GBException.NETWORK_UNSTABLE_ERROR_CODE:
                messageResId = JR.string("GB_alert_network_status");
                break;
            case GBException.ACCOUNTS_CONNECT_EXISTS_GAMEINFO:
            case GBException.ACCOUNTS_CONNECT_SERVICED_ON:
                messageResId = JR.string("errorui_account_connected_label_title");
                break;
            case Response.CLIENT_ON_ERROR:
                return;
            case Response.CLIENT_LOGIN_CANCELED:
                return;
            case GBException.JOIN_DUPLICATED_EMAIL:
                return;
            case GBException.LOGIN_INVALID_FACEBOOK_ACCESS_TOKEN_CODE:
                return;
            default:
                messageResId = JR.string("ui_main_default_error");
                isDefault = true;
        }
        if (isDefault) {
            toast.setText(activity.getString(messageResId)+"("+errorCode+")");
        } else {
            toast.setText(activity.getString(messageResId));
        }

        show();
    }
}
