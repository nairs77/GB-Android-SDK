package com.gebros.platform.pay;


import com.gebros.platform.GBSettings;

/**
 * Created by gebros.nairs77@gmail.com on 5/18/16.
 */
public class GBInAppApi {

    private static final String CLIENT_SECRET_PARAMETER_KEY = "client_secret";
    private static final String USERKEY_PARAMETER_KEY = "userkey";

    private static final String INIT = "/Initialize";
    private static final String BUY_INTENT = "/BuyIntent";
    private static final String SAVE_RECEIPT = "/SaveReceipt";

    public static final String JOYCITY_BILL_MARKETINFO_API = GBSettings.getIabServer() + INIT;
    public static final String JOYCITY_BILL_TOKEN_API = GBSettings.getIabServer() + BUY_INTENT;
    public static final String JOYCITY_BILL_RECEIPT_API = GBSettings.getIabServer() + SAVE_RECEIPT;
    public static final String JOYCITY_BILL_RESTORE_API = GBSettings.getIabServer() + "/pay/fail/restore";

}
