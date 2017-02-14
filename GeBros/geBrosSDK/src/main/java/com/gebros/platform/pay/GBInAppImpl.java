package com.gebros.platform.pay;

import android.os.Handler;

import com.gebros.platform.GBSdk;
import com.gebros.platform.GBSettings;
import com.gebros.platform.net.AbstractRequest;
import com.gebros.platform.event.GBEvent;
import com.gebros.platform.event.GBEventReceiver;
import com.gebros.platform.internal.JR;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.net.GBResponseHandler;
import com.gebros.platform.net.HttpMethod;
import com.gebros.platform.net.JSONRequestRunner;
import com.gebros.platform.net.RequestRunner;
import com.gebros.platform.platform.IPlatformClient;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBAppUtils;
import com.gebros.platform.util.GBDeviceUtils;

import org.json.JSONObject;

/**
 * Created by nairs on 2016-05-17.
 */
final class GBInAppImpl {

    private static final String TAG = GBInAppImpl.class.getCanonicalName();

    private static IPlatformClient mClient;

    private static final String CLIENT_SECRET_PARAMETER_KEY = "client_secret";
    private static final String USERKEY_PARAMETER_KEY = "userkey";
    private static final String TO_USERKEY_PARAMETER_KEY = "to_userkey";
    private static final String EXTRA_PARAMETER_KEY = "extra_data";
    private static final String MARKET_CODE_PARAMETER_KEY = "market_code";
    //public static final String PAYMENTKEY_PARAMETER_KEY = "payment_key";
    public static final String AUTH_CODE_PARAMETER_KEY = "auth_code";
    public static final String CALLBACK_URL_PARAMETER_KEY = "callbackurl";

    private static final String ACCOUNT_SEQ_KEY = "accountSeq";
    private static final String MARKET_CODE_KEY = "marketCode";
    private static final String GAME_CODE_KEY = "gameCode";
    private static final String PRODUT_ID_KEY = "productID";
    private static final String PRICE_KEY = "price";


    public static final String SERVICE_STATUS = "service_status";
    private static final String IP_PARAMETER_KEY = "ip";

    private static final int DELAY_RECEIPT_CALLBACK = -117;
    private static final int RETRY_PAY_RECEIPT_CALL_THREAD_TIME = 1000;

    private boolean mInitialized = false;
    private int mInAppMode;

    //- China 360, UC
    private String mNotifyCallbackURL;

    //- China Platform
    private String mAppKey;
    private String mAppId;
    private String mAppSecret;

    //- Google
    private String gPublicKey;

    //- T Store (One Store)
    private String tAppId;
    private String tApiVersion;

    //- Naver Store
    private String nAppCode;
    private String nIapKey;
    private String nPublicKey;

    //- My Card
    private String myCardFacID;
    private String myCardServiceID;
    private String myCardRequestURL;

    public boolean isInitialized() { return mInitialized; }

    public IPlatformClient getClient() { return mClient; }

    //public void setClient(IPlatformClient client) {
    //    mClient = client;
    //}

    public void initialize(IPlatformClient client) {
        mClient = client;

        initialize();
    }

    public void initialize() {
        GBLog.d(TAG + "Start Market Init (Info)");

/*
        AbstractRequest.Builder builder = new AbstractRequest.Builder(GBInAppApi.JOYCITY_BILL_MARKETINFO_API).method(HttpMethod.POST)
                .addParameters(GAME_CODE_KEY, GBSettings.getGameCode());

        AbstractRequest.Builder builder = new AbstractRequest.Builder(GBInAppApi.JOYCITY_BILL_MARKETINFO_API).method(HttpMethod.POST)
                .addParameters(CLIENT_SECRET_PARAMETER_KEY, GBSettings.getClientSecret())
                .addParameters(MARKET_CODE_PARAMETER_KEY, GBSettings.getMarketCode());

        RequestRunner<JSONObject> runner = new JSONRequestRunner(builder);
        JSONObject object = runner.get();

        if (object != null) {

            GBLog.d("result = %s", object.toString());
            // TODO : Error Parsing
            getMarketInfo(object.optJSONObject("result"));

            mClient.setMarketInfo(object.toString());
            mInitialized = true;
        } else {
            requestPaymentMarketInfo();
        }

        GBLog.d(TAG + "End Market Init (Info)");
*/
        requestPaymentMarketInfo();
    }

    public void requestPaymentMarketInfo() {

        GBEventReceiver wrappedCallback = new GBEventReceiver() {
            @Override
            public void onSuccessEvent(GBEvent event, JSONObject json) {
                getMarketInfo(json);
                mInitialized = true;
            }

            @Override
            public void onFailedEvent(GBEvent event, int errorCode, String errorMessage) {
                // TODO :
                GBLog.i(TAG + "Market Initialized Faield!!!");
            }
        };

        GBLog.d("Market Info = %s", GBInAppApi.JOYCITY_BILL_MARKETINFO_API);

        AbstractRequest.Builder builder = new AbstractRequest.Builder(GBInAppApi.JOYCITY_BILL_MARKETINFO_API).method(HttpMethod.POST)
                .addParameters(GAME_CODE_KEY, GBSettings.getGameCode());

        RequestRunner<JSONObject> runner = new JSONRequestRunner(builder);
        runner.call(new GBResponseHandler(new GBEvent[]{GBEvent.MARKET_INFO_IAB, GBEvent.MARKET_INFO_IAB_FAILED}, wrappedCallback));
    }

    /**
     * Get paymentKey and extra data from Billing server
     * in case, MyCard
     *
     * @param userKey       userkey for Buyer's
     * @param item          item info
     * @param receiver      Notify listener when Event is complete.
     */
    public void requestPaymentIabToken(String userKey, GBInAppItem item, GBEventReceiver receiver) {

        AbstractRequest.Builder builder = new AbstractRequest.Builder(GBInAppApi.JOYCITY_BILL_TOKEN_API).method(HttpMethod.POST)
                .addParameters(ACCOUNT_SEQ_KEY, userKey)
                .addParameters(MARKET_CODE_KEY, GBSettings.getMarketCode())
                .addParameters(GAME_CODE_KEY, GBSettings.getGameCode())
                .addParameters(PRODUT_ID_KEY, item.getItemName())
                .addParameters(PRICE_KEY, item.getPrice());

        RequestRunner<JSONObject> runner = new JSONRequestRunner(builder);
        runner.call(new GBResponseHandler(new GBEvent[]{GBEvent.PAYMENT_IAB_TOKEN, GBEvent.PAYMENT_IAB_TOKEN_FAILED}, receiver));
    }

    /**
     *
     * @param userKey       userKey for Buyer's
     * @param purchase
     * @param receiver      Notify listener when Event is complete.
     */

    public void requestSaveReceipt(final String userKey, final IabPurchase purchase, final GBEventReceiver receiver) {

        final Handler handler = new Handler();

        (new Thread(new Runnable() {
            @Override
            public void run() {
                int retryCount = 3;

                boolean isSuccess = false;
                int errorCode = 0;
                String errorMessage = "";
                JSONObject result = null;

                for (int index = 0; index < retryCount; index++) {
                    result = requestSaveReceiptInternal(userKey, purchase);

                    if (result == null) {
                        isSuccess = false;
                        errorCode = IabResult.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR;
                        break;
                    }

                    int apiStatus = result.optInt("status");

                    if(apiStatus == 0) {
                        errorCode = result.optJSONObject("error").optInt("errorCode");
                        errorMessage = result.optJSONObject("error").optString("errorType");

                        // Retry
                        if (errorCode == DELAY_RECEIPT_CALLBACK) {
                            final int currentIndex = index;
                            final int error = errorCode;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String msg = String.format("Save Receipt (%d) : Retry Count %d", error, currentIndex + 1);
                                    GBLog.d(TAG + msg);
                                }
                            });

                            errorMessage = GBSdk.getApplicationContext().getResources().getString(JR.string("errorui_iab_china_delay_market_callback_label_title"));
                        } else {
                            isSuccess = false;
                            break;
                        }

                        try {
                            int tryCallTime = RETRY_PAY_RECEIPT_CALL_THREAD_TIME * (index+1);
                            GBLog.d(TAG + "Save Receipt "+errorCode+" : Retry count "+index+" delayTime : "+tryCallTime );
                            Thread.sleep(tryCallTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        isSuccess = true;
                        break;
                    }
                }

                if (receiver != null) {

                    final boolean isResult = isSuccess;
                    final JSONObject apiResult = result;
                    final int error_code = errorCode;
                    final String errorMsg = errorMessage;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isResult) {
                                receiver.onSuccessEvent(GBEvent.PAYMENT_IAB_TOKEN, apiResult);
                            } else {
                                receiver.onFailedEvent(GBEvent.PAYMENT_IAB_TOKEN, error_code, errorMsg);
                            }
                        }
                    });
                }

            }
        })).start();
    }

    public void requestRestoreItems(String userKey, GBEventReceiver receiver) {
        AbstractRequest.Builder builder = new AbstractRequest.Builder(GBInAppApi.JOYCITY_BILL_RESTORE_API).method(HttpMethod.POST)
                .addParameters(CLIENT_SECRET_PARAMETER_KEY, GBSettings.getClientSecret())
                .addParameters(USERKEY_PARAMETER_KEY, userKey)
                .addParameters("market_code", GBSettings.getMarketCode());

        RequestRunner<JSONObject> runner = new JSONRequestRunner(builder);
        runner.call(new GBResponseHandler(new GBEvent[]{GBEvent.PAYMENT_FAIL_RESTORE_IAB, GBEvent.PAYMENT_FAIL_RESTORE_IAB_FAILED}, receiver));
    }

    private JSONObject requestSaveReceiptInternal(final String userKey, final IabPurchase purchase) {
        AbstractRequest.Builder builder = new AbstractRequest.Builder(GBInAppApi.JOYCITY_BILL_RECEIPT_API).method(HttpMethod.POST)
                .addParameters(ACCOUNT_SEQ_KEY, userKey)
                .addParameters("marketCode", GBSettings.getMarketCode())
                .addParameters("paymentKey", purchase.getPaymentKey())
                .addParameters("productID", purchase.getSku())
                .addParameters("marketCode", GBSettings.getMarketCode())
                .addParameters("receipt", purchase.getToken());

        RequestRunner<JSONObject> runner = new JSONRequestRunner(builder);
        return runner.get();

        //runner.call(new GBResponseHandler(new GBEvent[]{GBEvent.SAVE_RECEIPT_IAB, GBEvent.SAVE_RECEIPT_IAB_FAILED}, receiver));
    }

    private void getMarketInfo(JSONObject object) {
        JSONObject marketInfoObj = object;

        Market market = GBSettings.getMarket();

        if(marketInfoObj != null) {
            if(market == Market.GOOGLE) {
                gPublicKey = marketInfoObj.optString("PUBLIC_KEY");
            }
        }
/*
        JSONObject marketInfoObj = object.optJSONObject("market_info");
        //int marketCode = Joycity.getMarketCode();
        Market market = GBSettings.getMarket();

        if(marketInfoObj != null) {
            String key = GBSettings.getClientSecret();
            mInAppMode = marketInfoObj.optInt(SERVICE_STATUS);
            if(market == Market.GOOGLE) {
                gPublicKey = marketInfoObj.optString("public_key");
                //gPackageName = marketInfoObj.optString("package_name");
                try {
                    gPublicKey = GBAppUtils.deCrypt(gPublicKey, key);
                    //gPackageName = CryptUtil.deCrypt(gPackageName, key);
                } catch (Exception e) {
                    GBLog.d(TAG + "exception: "+e);
                }
            } else if(market == Market.ONESTORE) {
                tAppId = marketInfoObj.optString("appid");
                tApiVersion = marketInfoObj.optString("api_version");
                GBLog.d(TAG + " receiveEvent: XORtAppId: "+tAppId+" XORtAppVersion: "+tApiVersion);
                try {
                    tAppId = GBAppUtils.deCrypt(tAppId, key);
                    tApiVersion = GBAppUtils.deCrypt(tApiVersion, key);
                } catch (Exception e) {
                    GBLog.d(TAG + "exception: "+e);
                }
                GBLog.d(TAG + " receiveEvent: decryptTAppId: "+tAppId+" decryptAppVersion:"+tApiVersion);
            } else if(market == Market.NAVER) {
                nAppCode = marketInfoObj.optString("appCode");
                nIapKey = marketInfoObj.optString("iapKey");
                nPublicKey = marketInfoObj.optString("publicKey");
                try {
                    nAppCode = GBAppUtils.deCrypt(nAppCode, key);
                    nIapKey = GBAppUtils.deCrypt(nIapKey, key);
                    nPublicKey = GBAppUtils.deCrypt(nPublicKey, key);
                } catch (Exception e) {
                    GBLog.d(TAG + "exception: "+e);
                }
            } else if (market == Market.MYCARD) {
                myCardFacID = marketInfoObj.optString("factoryId");
                myCardServiceID = marketInfoObj.optString("factoryServiceId");
                try {
                    myCardFacID = GBAppUtils.deCrypt(myCardFacID, key);
                    myCardServiceID = GBAppUtils.deCrypt(myCardServiceID, key);
                } catch (Exception e) {
                    GBLog.d(TAG + "exception: " + e);
                }
                myCardRequestURL = marketInfoObj.optString("weburl");
            } else if (market == Market.CHINA360 || market == Market.UC) {
                mNotifyCallbackURL = marketInfoObj.optString(CALLBACK_URL_PARAMETER_KEY);
            } else if (market == Market.BAIDU) {

            }
        }
*/
    }

    // - Getter
    public int getInAppMode() {
        return mInAppMode;
    }

    public String getNotifyCallbackURL() {
        return mNotifyCallbackURL;
    }
    // - Google
    public String getGooglePublicKey() {
        return gPublicKey;
    }

    public String gettAppId() {
        return tAppId;
    }

    public String gettApiVersion() {
        return tApiVersion;
    }

    public String getnAppCode() {
        return nAppCode;
    }

    public String getnIapKey() {
        return nIapKey;
    }

    public String getnPublicKey() {
        return nPublicKey;
    }

    public String getMyCardFacID() {
        return myCardFacID;
    }

    public String getMyCardServiceID() {
        return myCardServiceID;
    }

    public String getMyCardRequestURL() {
        return myCardRequestURL;
    }

}
