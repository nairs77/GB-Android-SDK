package com.gebros.platform.unity;

import com.gebros.platform.GBSettings;
import com.gebros.platform.auth.GBSession;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.listener.GBInAppListener;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.pay.IabInventory;
import com.gebros.platform.pay.IabPurchase;
import com.gebros.platform.pay.IabResult;
import com.gebros.platform.pay.GBInAppItem;
import com.gebros.platform.pay.GBInAppManager;
import com.gebros.platform.pay.IabSkuDetails;
import com.gebros.platform.platform.PlatformType;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gebros.nairs77@gmail.com on 6/20/16.
 */
public class BillingPlugin extends BasePlugin {

    private static final String TAG = BillingPlugin.class.getCanonicalName();

    private static final class BillingPluginHolder {
        public static final BillingPlugin instance = new BillingPlugin();
    }

    public static BillingPlugin getInstance() {
        return BillingPluginHolder.instance;
    }

    public static void StartSetup(String userKey, String gameObjectName) {
        InitBillingService(userKey, gameObjectName);
    }

    public static void InitBillingService(String userKey, String gameObjectName) {
        //TODO : Check Permission

        BillingPlugin.getInstance().initBillingService(userKey, gameObjectName);
    }

    public static void QueryInventoryItemInfo(String sku_array, final String gameObjectName) {
        String[] skus = sku_array.split(",");

        GBLog.d(TAG + "sku = " + sku_array);

        ArrayList<String> items = new ArrayList<String>(Arrays.asList(skus));
        BillingPlugin.getInstance().queryInventoryItemInfo(items, gameObjectName);
    }

    public static void BuyItem(String sku, int price, String gameObjectName) {
        BillingPlugin.getInstance().buyItemWithInfo(sku, price, gameObjectName);
    }


    public static void BuyItem(String itemInfo, String gameObjectName) {

        try {
            JSONObject itemObject = new JSONObject(itemInfo);
            String sku = itemObject.optJSONObject("item").optString("product_id");
            String price = itemObject.optJSONObject("item").optString("product_price");
            String info = itemObject.optJSONObject("item").optString("product_name");
            String toUser = itemObject.optString("to");

            GBLog.d(TAG + "sku = %s, price=%s, info = %s, toUser = %s", sku, price, info, toUser);


            BillingPlugin.getInstance().buyItemWithInfo(sku, price, info, toUser, gameObjectName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void RestoreItems(final String gameObjectName) {
        BillingPlugin.getInstance().restoreItems(gameObjectName);
    }



    private void initBillingService(String userKey, final String gameObjectName) {
        //callbackObjectNames.add(gameObjectName);
        callbackObjectNames.put(gameObjectName, gameObjectName);

        GBInAppManager.InitInAppService(userKey, new GBInAppListener.OnIabSetupFinishedListener() {

            @Override
            public void onSuccess() {
                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_SUCCESS, "");
            }

            @Override
            public void onFail() {
                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_FAIL, "");
            }
        });
    }

    private void queryInventoryItemInfo(ArrayList<String> items, final String gameObjectName) {

        callbackObjectNames.put(gameObjectName, gameObjectName);

        GBLog.d(TAG + "items = " + items.get(0));

        GBInAppManager.QueryInventory(items, new GBInAppListener.OnQueryInventoryFinishedListener() {
            @Override
            public void onSuccess(IabInventory inv) {
                JSONObject response = new JSONObject();
                JSONObject items_response = new JSONObject();
                JSONObject iab_response = new JSONObject();

                try {
                    if (inv != null) {

                        List<String> purchases = inv.getAllQueriedSkus(true);

                        int count = 0;
                        int purchasesCount = purchases.size();

                        items_response.put("count", purchasesCount);

                        if (purchasesCount > 0) {
                            for (String purchase : purchases) {
                                IabSkuDetails item = inv.getSkuDetails(purchase);
                                JSONObject item_response = new JSONObject();

                                item_response.put("title", item.getTitle());
                                item_response.put("description", item.getDescription());
                                item_response.put("product_id", item.getSku());

                                JSONObject o = new JSONObject(item.getJson());

                                String price_symbol = item.getPrice();
                                item_response.put("price", price_symbol.substring(1));
                                item_response.put("currency_symbol", price_symbol.substring(0, 1));
                                item_response.put("currency", o.optString("price_currency_code"));
                                items_response.put("item" + String.valueOf(count), item_response);

                            }
                            count++;
                        }
                    }
                    iab_response.put(DATA_KEY, items_response);
                    response.put(RESULT_KEY, iab_response);

                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                GBLog.d(TAG + "response = " + response.toString());
                UnityPlayer.UnitySendMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_SUCCESS, response.toString());
            }

            @Override
            public void onFail(IabResult result) {
                JSONObject response = new JSONObject();
                JSONObject iab_response = new JSONObject();
                JSONObject error_response = new JSONObject();

                try {
                    error_response.put(API_RESPONSE_ERROR_CODE_KEY, result.getResponse());
                    error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, result.getMessage());
                    iab_response.put(ERROR_KEY, error_response);
                    response.put(RESULT_KEY, error_response);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());

                }

                GBLog.d(TAG + "response = " + response.toString());
                UnityPlayer.UnitySendMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_FAIL, response.toString());
            }
        });
    }

    private void buyItemWithInfo(String sku, int price, final String gameObjectName) {
        callbackObjectNames.put(gameObjectName, gameObjectName);

        GBInAppManager.BuyItem(getActivity(), new GBInAppItem(String.valueOf(price), sku, "", "inapp"), new GBInAppListener.OnPurchaseFinishedListener() {
            @Override
            public void onSuccess(IabPurchase purchaseInfo) {
                JSONObject response = new JSONObject();
                JSONObject iab_response = new JSONObject();
                try {
                    String paymentKey = purchaseInfo.getPaymentKey();//getDeveloperPayload();
                    JSONObject payment = new JSONObject();
                    payment.put("payment_key", paymentKey);
                    iab_response.put(DATA_KEY, payment);
                    response.put(RESULT_KEY, iab_response);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_SUCCESS, response.toString());
            }

            @Override
            public void onFail(IabResult result) {
                JSONObject response = new JSONObject();
                JSONObject iab_response = new JSONObject();
                JSONObject error_response = new JSONObject();

                try {
                    error_response.put(API_RESPONSE_ERROR_CODE_KEY, result.getResponse());
                    error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, result.getMessage());
                    iab_response.put(ERROR_KEY, error_response);
                    response.put(RESULT_KEY, iab_response);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_FAIL, response.toString());
            }

            @Override
            public void onCancel(boolean isUserCancelled) {
                JSONObject response = new JSONObject();
                JSONObject iab_response = new JSONObject();
                JSONObject error_response = new JSONObject();

                // TODO : User Cancelled!!!
                try {
                    error_response.put(API_RESPONSE_ERROR_CODE_KEY, IabResult.BILLING_RESPONSE_RESULT_USER_CANCELED);
                    error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, "User canceled");
                    iab_response.put(ERROR_KEY, error_response);
                    response.put(RESULT_KEY, iab_response);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_FAIL, response.toString());
            }
        });
    }

    private void buyItemWithInfo(String sku, String price, String itemInfo, String toUserKey, final String gameObjectName) {

        callbackObjectNames.put(gameObjectName, gameObjectName);

        GBInAppManager.BuyItem(getActivity(), new GBInAppItem(price, sku, itemInfo, "inapp"), new GBInAppListener.OnPurchaseFinishedListener() {

            @Override
            public void onSuccess(IabPurchase purchaseInfo) {
                JSONObject response = new JSONObject();
                JSONObject iab_response = new JSONObject();
                try {
                    String paymentKey = purchaseInfo.getPaymentKey();//getDeveloperPayload();
                    JSONObject payment = new JSONObject();
                    payment.put("payment_key", paymentKey);
                    iab_response.put(DATA_KEY, payment);
                    response.put(RESULT_KEY, iab_response);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_SUCCESS, response.toString());
            }

            @Override
            public void onFail(IabResult result) {
                JSONObject response = new JSONObject();
                JSONObject iab_response = new JSONObject();
                JSONObject error_response = new JSONObject();

                try {
                    error_response.put(API_RESPONSE_ERROR_CODE_KEY, result.getResponse());
                    error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, result.getMessage());
                    iab_response.put(ERROR_KEY, error_response);
                    response.put(RESULT_KEY, iab_response);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_FAIL, response.toString());
            }

            @Override
            public void onCancel(boolean isUserCancelled) {
                JSONObject response = new JSONObject();
                JSONObject iab_response = new JSONObject();
                JSONObject error_response = new JSONObject();

                // TODO : User Cancelled!!!
                try {
                    error_response.put(API_RESPONSE_ERROR_CODE_KEY, IabResult.BILLING_RESPONSE_RESULT_USER_CANCELED);
                    error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, "User canceled");
                    iab_response.put(ERROR_KEY, error_response);
                    response.put(RESULT_KEY, iab_response);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_FAIL, response.toString());
            }
        });
    }

    private void restoreItems(final String gameObjectName) {

        //callbackObjectNames.add(gameObjectName);
        callbackObjectNames.put(gameObjectName, gameObjectName);

        GBInAppManager.ReStoreItems(new GBInAppListener.OnRestoreItemsFinishedListener() {
            @Override
            public void onSuccess(List<String> paymentKeys) {
                JSONObject response = new JSONObject();
                JSONObject iab_response = new JSONObject();

                StringBuilder validateIDs = new StringBuilder("");

                int count = 0;
                int restoreItemCount = paymentKeys.size();

                for (String paymentKey : paymentKeys) {
                    validateIDs.append(paymentKey);

                    if (count != restoreItemCount - 1) {
                        validateIDs.append(',');
                        count++;
                    }
                }

                try {
                    JSONObject restoreItems = new JSONObject();
                    restoreItems.put("restore_keys", validateIDs.toString());
                    iab_response.put(DATA_KEY, restoreItems);
                    response.put(RESULT_KEY, iab_response);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_SUCCESS, response.toString());
            }

            @Override
            public void onFail(IabResult result) {
                JSONObject response = new JSONObject();
                JSONObject iab_response = new JSONObject();
                JSONObject error_response = new JSONObject();

                try {
                    error_response.put(API_RESPONSE_ERROR_CODE_KEY, result.getResponse());
                    error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, result.getMessage());
                    iab_response.put(ERROR_KEY, error_response);
                } catch (JSONException e) {
                    GBLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_FAIL, response.toString());

            }
        });
    }

}
