package com.gebros.platform.pay;

import android.app.Activity;
import android.content.Intent;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gebros.nairs77@gmail.com on 5/12/16.
 */
public class GBInAppManager {

    private static final String TAG = String.format("[" + GBInAppManager.class.getSimpleName() + "]");
    static final GBInAppImpl inAppImpl = new GBInAppImpl();
    //static PlatformIabHelper mIabHelper = null;
    static IIabHelper mIabHelper = null;
    static String sUserKey;

    /**
     *
     * @param client
     */
    public static void Initialize(IPlatformClient client) {
//        inAppImpl.initialißze();
//    //    inAppImpl = new GBInAppImpl(client);
//    //    inAppImpl.initialize();
//        inAppImpl.setClient(client);

        inAppImpl.initialize(client);
        //initBillingService(null);
    }

    /**
     *
     * @param userKey
     * @param listener
     */
    public static void InitInAppService(String userKey, GBInAppListener.OnIabSetupFinishedListener listener) {
        sUserKey = userKey;
        //inAppImpl.initialize();
        initBillingService(listener);
    }

    public static void InitInAppService(GBInAppListener.OnIabSetupFinishedListener listener) {
        inAppImpl.initialize();
        initBillingService(listener);
    }

    /**
     *
     * @param skus          SKU(Store Keepting Unity) Array (product id)
     * @param listener      Notify listener when QueryInventory is complete.
     */
    public static void QueryInventory(List<String> skus, final GBInAppListener.OnQueryInventoryFinishedListener listener) {
        try {
            mIabHelper.queryInventoryAsync(skus, new IIabCallback.OnQueryInventoryListener() {
                @Override
                public void success(IabInventory inventory) {

                    listener.onSuccess(inventory);
                }

                @Override
                public void fail(IabResult result) {

                    listener.onFail(result);
                }
            });
        } catch (GBException e) {

        }
    }

    /**
     * @param activity      Activity
     * @param item          Purchase item info
     * @param listener      Notify listener when Purchase is complete.
     */
    public static void BuyItem(final Activity activity, final GBInAppItem item, final GBInAppListener.OnPurchaseFinishedListener listener) {

        GBLog.d(TAG + "Request BuyItem = %s", sUserKey);

        inAppImpl.requestPaymentIabToken(sUserKey, item, new GBEventReceiver() {

            @Override
            public void onSuccessEvent(GBEvent event, JSONObject json) {
                String devPayload = json.optString("PAYMENT_KEY");
                launchPurchaseFlow(activity, sUserKey, item, devPayload, listener);
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

                GBLog.d(TAG + "RestoreItems --- onSuccessEvent");

                ArrayList<String> paymentKeys = new ArrayList<String>();

                try {

                    JSONArray jsonArray = json.getJSONArray("paymentKeys");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        paymentKeys.add(object.getString("PAYMENT_KEY"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GBLog.d(TAG + "RestoreItems --- " + paymentKeys.toString());
                listener.onSuccess(paymentKeys);
            }

            @Override
            public void onFailedEvent(GBEvent event, int errorCode, String errorMessage) {
                listener.onFail(new IabResult(errorCode, errorMessage, GBSdk.getApplicationContext()));
            }
        });
    }

    public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mIabHelper != null) {
            try {
                return mIabHelper.handleActivityResult(requestCode, resultCode, data);
            } catch (GBException e) {

            }
        }

        return false;
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

                    _restore_items();

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
            mIabHelper.launchPurchaseFlow(activity, userKey, item, new IIabCallback.OnPurchaseListener() {
                @Override
                public void success(IabPurchase purchase) {
                    String payload = purchase.getDeveloperPayload();
                    String orderId = purchase.getOrderId();

                    final IabPurchase purchaseInfo = purchase;

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
            e.printStackTrace();
        }
    }

    final static void saveReceipt(final String userKey, final IabPurchase purchase, final GBInAppListener.OnPurchaseFinishedListener listener) {
        inAppImpl.requestSaveReceipt(userKey, purchase, new GBEventReceiver() {
            @Override
            public void onSuccessEvent(GBEvent event, JSONObject json) {
                listener.onSuccess(purchase);

                SimpleAsyncTask.doRunUIThread(new ISimpleAsyncTask.OnUIThreadTask() {
                    @Override
                    public void doRunUIThread() {
                        ArrayList<IabPurchase> purchaseList = new ArrayList<IabPurchase>();
                        purchaseList.add(purchase);
                        consumePurchaseItem(purchaseList);
                    }
                });
            }

            @Override
            public void onFailedEvent(GBEvent event, int errorCode, String errorMessage) {
                listener.onFail(new IabResult(errorCode, errorMessage, GBSdk.getApplicationContext()));

            }
        });
    }

    final static void _restore_items() {
        GBInAppManager.QueryInventory(null, new GBInAppListener.OnQueryInventoryFinishedListener() {
            @Override
            public void onSuccess(IabInventory inv) {
                if (inv != null && inv.getAllPurchases() != null) {

                    for (IabPurchase purchase : inv.getAllPurchases()) {

                        saveReceipt(sUserKey, purchase, new GBInAppListener.OnPurchaseFinishedListener() {

                            @Override
                            public void onSuccess(final IabPurchase purchaseInfo) {
                                //GBLog.d(TAG + "test");
                                SimpleAsyncTask.doRunUIThread(new ISimpleAsyncTask.OnUIThreadTask() {
                                    @Override
                                    public void doRunUIThread() {
                                        ArrayList<IabPurchase> purchaseList = new ArrayList<IabPurchase>();
                                        purchaseList.add(purchaseInfo);
                                        consumePurchaseItem(purchaseList);
                                    }
                                });
                            }

                            @Override
                            public void onFail(IabResult result) {
                                GBLog.d(TAG + "test");
                            }

                            @Override
                            public void onCancel(boolean isUserCancelled) {

                                GBLog.d(TAG + "test");
                            }
                        });

                    }
                }
            }

            @Override
            public void onFail(IabResult result) {
                GBLog.d(TAG + "test");
            }
        });
    }

    final static void consumePurchaseItem(ArrayList<IabPurchase> purchaseList) {
        try {
            mIabHelper.consume(purchaseList, new IIabCallback.OnConsumeListener() {
                @Override
                public void OnConsumeFinishedListener(List<IabPurchase> purchaseList, List<IabResult> results) {

                }
            });
        } catch (GBException e) {

        }
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

        //static PlatformIabHelper create(Market market) {
        static IIabHelper create(Market market) {

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
               return new GoogleIabHelper(GBSdk.getApplicationContext(), inAppImpl);//inAppImpl.getGooglePublicKey());
            } else if (market == Market.ONESTORE) {

            } else if (market == Market.MYCARD) {

            }

            return null;
        }
    }
}
