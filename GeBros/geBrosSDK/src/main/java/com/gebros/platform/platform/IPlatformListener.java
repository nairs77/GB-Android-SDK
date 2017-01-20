package com.gebros.platform.platform;

import com.gebros.platform.auth.LoginResult;

import org.json.JSONObject;

/**
 * Created by Joycity-Platform on 5/10/16.
 */
public interface IPlatformListener {
    /**
     * Listens for Platform initialization
     */

    public interface OnInitLister {
        /**
         *
         */
        public void onSuccess();

        /**
         *
         */
        public void onFail();
    }

    /**
     * Listen for Platform Authentication
     */
    public interface OnAuthListener {

        public void onSuccess(LoginResult result);

        public void onFail(LoginResult result);

        public void onCancel(boolean isUserCancel);
    }

    /**
     * Listen for Platform Pay
     */
    public interface OnPayListener {

        public void onPaySuccess(JSONObject object);

        public void onPayFail(int errorCode, String errorMsg);//GBException e);

        public void onPayCancel(boolean isUserCancelled);
    }

    public interface OnGameListener {
        /**
         *
         */
        public void onSuccess();

        /**
         *
         */
        public void onFail();
    }
}

