package com.gebros.platform.pay;

import android.app.Activity;

import com.gebros.platform.GBSdk;
import com.gebros.platform.GBSettings;
import com.gebros.platform.concurrent.ISimpleAsyncTask;
import com.gebros.platform.concurrent.SimpleAsyncTask;
import com.gebros.platform.event.GBEvent;
import com.gebros.platform.event.GBEventReceiver;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.listener.GBInAppListener;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.platform.IPlatformClient;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBValidator;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gebros.nairs77@gmail.com on 5/12/16.
 */
public class GBInAppManager {

    private static final String TAG = String.format("[" + GBInAppManager.class.getSimpleName() + "]");
    static final GBInAppImpl inAppImpl = new GBInAppImpl();
    static PlatformIabHelper mIabHelper = null;
    static String sUserKey;

    /**
     *
     * @param client
     */
    public static void Initialize(IPlatformClient client) {
//        inAppImpl.initialize();
//    //    inAppImpl = new GBInAppImpl(client);
//    //    inAppImpl.initialize();
//        inAppImpl.setClient(client);

        inAppImpl.initialize(client);

    }

    /**
     *
     * @param userKey
     * @param listener
     */
    public static void InitInAppService(String userKey, GBInAppListener.OnIabSetupFinishedListener listener) {
        sUserKey = userKey;
        inAppImpl.initialize();
        initBillingService(listener);
    }

    /**
     *
     * @param skus          SKU(Store Keepting Unity) Array (product id)
     * @param listener      Notify listener when QueryInventory is complete.
     */
    public static void QueryInventory(List<String> skus, GBInAppListener.OnQueryInventoryFinishedListener listener) {
        GBInAppManager.QueryInventory(skus, new GBInAppListener.OnQueryInventoryFinishedListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail() {

            }
        });
    }

    /**
     *
     * @param activity      Activity
     * @param toUserKey     UserKey for Person who will receive a present
     * @param item          Purchase item info
     * @param listener      Notify listener when Purchase is complete.
     */
    public static void BuyItem(final Activity activity, String toUserKey, final GBInAppItem item, final GBInAppListener.OnPurchaseFinishedListener listener) {

        GBLog.d(TAG + "Request BuyItem = %s", sUserKey);

        inAppImpl.requestPaymentIabToken(sUserKey, toUserKey, item, new GBEventReceiver() {

            @Override
            public void onSuccessEvent(GBEvent event, JSONObject json) {

                GBLog.d(TAG + "json = %s", json.toString());
                String extraData = json.optString(GBInAppImpl.PAYMENTKEY_PARAMETER_KEY);

                StringBuilder sb = new StringBuilder(extraData);

                if (GBSettings.getMarket() == Market.MYCARD) {
                    sb.append(",");
                    sb.append(json.optString(GBInAppImpl.AUTH_CODE_PARAMETER_KEY));
                }
                extraData = sb.toString();

                GBLog.d(TAG + "payment Key = %s", extraData);
                launchPurchaseFlow(activity, sUserKey, item, extraData, listener);
            }

            @Override
            public void onFailedEvent(GBEvent event, int errorCode, String errorMessage) {
                GBLog.d(TAG + "error code = %d", errorCode);
                listener.onFail(new IabResult(errorCode, errorMessage, GBSdk.getApplicationContext()));
            }
        });
    }

    public static void ReStoreItems(final GBInAppListener.OnRestoreItemsFinishedListener listener) {
        inAppImpl.requestRestoreItems(sUserKey, new GBEventReceiver() {
            @Override
            public void onSuccessEvent(GBEvent event, JSONObject json) {
                String keyStr = json.optString("payment_key");
                List<String> keyList = getPaymentKeys(keyStr);
                listener.onSuccess(keyList);
            }

            @Override
            public void onFailedEvent(GBEvent event, int errorCode, String errorMessage) {
                listener.onFail(new IabResult(errorCode, errorMessage, GBSdk.getApplicationContext()));
            }
        });
    }

    static void initBillingService(final GBInAppListener.OnIabSetupFinishedListener listener) {

        if (mIabHelper == null) {
            mIabHelper = PlatformIabHelperFactory.create(GBSettings.getMarket());
        }

        try {
            mIabHelper.startIabService(new IIabCallback.OnInitListener() {

                @Override
                public void success(IabResult result) {
                    if (listener != null)
                        listener.onSuccess();

                }

                @Override
                public void fail(IabResult result) {
                    if (listener != null)
                        listener.onFail();
                }
            });
        } catch (GBException e) {
            e.printStackTrace();
            if (listener != null)
                listener.onFail();
        }
    }

    final static void launchPurchaseFlow(final Activity activity, final String userKey, GBInAppItem item, final String extraData, final GBInAppListener.OnPurchaseFinishedListener listener) {

        try {
            mIabHelper.launchPurchaseFlow(activity, userKey, item, new GBInAppListener.OnPurchaseFinishedListener() {
                @Override
                public void onCancel(boolean isUserCancelled) {
                    GBLog.d(TAG + "User Cancelled!!!");
                    listener.onCancel(isUserCancelled);
                }

                @Override
                public void onSuccess(IabPurchase purchase) {
                    String payload = purchase.getDeveloperPayload();
                    String orderId = purchase.getOrderId();

                    final IabPurchase purchaseInfo = purchase;


                    // - 2015. 5.12 for promo code
                    GBLog.d(TAG + "For Promo code payload:"+payload+" , orderId:"+orderId);
                    if (GBValidator.isNullOrEmpty(payload) && GBValidator.isNullOrEmpty(orderId)) {

                        GBLog.d(TAG + "For Promo code");
                        purchaseInfo.setPaymentKey(extraData);

                        String customOrderId = extraData + "." + userKey;
                        purchaseInfo.setCustomOrderId(customOrderId);
                    }

                    saveReceipt(userKey, purchaseInfo, listener);
                }

                @Override
                public void onFail(IabResult result) {
                    GBLog.d(TAG + "result =%s", result.getMessage());
                    listener.onFail(result);
                }
            }, extraData);
        } catch (GBException e) {
            e.printStackTrace();
        }
        /*
        try {
            mIabHelper.launchPurchaseFlow(activity, userKey, item, new GBInAppListener.OnPurchaseFinishedListener() {



                @Override
                public void success(IabPurchase purchase) {
                    String payload = purchase.getDeveloperPayload();
                    String orderId = purchase.getOrderId();

                    final IabPurchase purchaseInfo = purchase;


                    // - 2015. 5.12 for promo code
                    GBLog.d(TAG + "For Promo code payload:"+payload+" , orderId:"+orderId);
                    if (GBValidator.isNullOrEmpty(payload) && GBValidator.isNullOrEmpty(orderId)) {

                        GBLog.d(TAG + "For Promo code");
                        purchaseInfo.setPaymentKey(extraData);

                        String customOrderId = extraData + "." + userKey;
                        purchaseInfo.setCustomOrderId(customOrderId);
                    }

                    saveReceipt(userKey, purchaseInfo, listener);

                }

                @Override
                public void cancelled(IabPurchase purchase) {

                }

                @Override
                public void alreadyOwned(IabPurchase purchase) {

                }

                @Override
                public void fail(IabResult result) {

                }
            }, extraData);

        } catch (GBException e) {

        }
        */

    }

    final static void saveReceipt(final String userKey, final IabPurchase purchase, final GBInAppListener.OnPurchaseFinishedListener listener) {

        SimpleAsyncTask.doRunUIThread(new ISimpleAsyncTask.OnUIThreadTask() {
                                          @Override
                                          public void doRunUIThread() {
                                              inAppImpl.requestSaveReceipt(userKey, purchase, new GBEventReceiver() {
                                                  @Override
                                                  public void onSuccessEvent(GBEvent event, JSONObject json) {
                                                      listener.onSuccess(purchase);
                                                  }

                                                  @Override
                                                  public void onFailedEvent(GBEvent event, int errorCode, String errorMessage) {
                                                      listener.onFail(new IabResult(errorCode, errorMessage, GBSdk.getApplicationContext()));

                                                  }
                                              });
                                          }
                                      });
    }

    static private List<String> getPaymentKeys(String keyStr) {
        String[] paymentKeyArr = keyStr.split(",");
        List<String> keyList = new LinkedList<String>();
        for(int i=0; i < paymentKeyArr.length; i++) {
            String keyOrigin = "";
            if(i == 0) {
                keyOrigin = paymentKeyArr[i].substring(2, 22);
            }else {
                keyOrigin = paymentKeyArr[i].substring(1, 21);
            }
            keyList.add(keyOrigin);
        }
        return keyList;
    }

    private static class PlatformIabHelperFactory {

        static PlatformIabHelper create(Market market) {

/*
            if (market == PlatformType.Market.CHINA360) {
                return new China360IabHelper(GBSdk.getApplicationContext(), inAppImpl);
            } else if (market == PlatformType.Market.BAIDU) {
                return new BaiduIabHelper(GBSdk.getApplicationContext(), inAppImpl);
            } else if (market == PlatformType.Market.XIAOMI) {
                return new XiaomiIabHelper(GBSdk.getApplicationContext(), inAppImpl);
            } else if (market == PlatformType.Market.UC) {
                return new UCIabHelper(GBSdk.getApplicationContext(), inAppImpl);
            } else if (market == PlatformType.Market.WANDOUJIA) {
                return new WandoujiaIabHelper(GBSdk.getApplicationContext(), inAppImpl);
            } else if (market == PlatformType.Market.HUAWEI) {
                return new HuaweiIabHelper(GBSdk.getApplicationContext(), inAppImpl);
            }
*/
            if (market == Market.GOOGLE) {
                return null;
               //return new GoogleIabHelper(GBSdk.getApplicationContext(), inAppImpl.getGooglePublicKey());
            } else if (market == Market.ONESTORE) {

            } else if (market == Market.MYCARD) {

            }

            return null;
        }
    }
}
