package com.gebros.platform.pay;


import com.gebros.platform.GBSettings;

/**
 * Created by gebros.nairs77@gmail.com on 5/18/16.
 */
public class GBInAppApi {

    private static final String CLIENT_SECRET_PARAMETER_KEY = "client_secret";
    private static final String USERKEY_PARAMETER_KEY = "userkey";

    public static final String JOYCITY_BILL_MARKETINFO_API = GBSettings.getIabServer() + "/pay/init";
    public static final String JOYCITY_BILL_TOKEN_API = GBSettings.getIabServer() + "/pay/key";
    public static final String JOYCITY_BILL_RECEIPT_API = GBSettings.getIabServer() + "/pay/receipt";
    public static final String JOYCITY_BILL_RESTORE_API = GBSettings.getIabServer() + "/pay/fail/restore";

}
