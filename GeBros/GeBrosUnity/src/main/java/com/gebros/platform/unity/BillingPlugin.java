package com.gebros.platform.unity;

import com.joycity.platform.sdk.JoypleSettings;
import com.joycity.platform.sdk.exception.JoypleException;
import com.joycity.platform.sdk.listener.JoypleInAppListener;
import com.joycity.platform.sdk.log.JLog;
import com.joycity.platform.sdk.pay.IabPurchase;
import com.joycity.platform.sdk.pay.IabResult;
import com.joycity.platform.sdk.pay.JoypleInAppItem;
import com.joycity.platform.sdk.pay.JoypleInAppManager;
import com.joycity.platform.sdk.platform.PlatformType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nairs77@joycity.com on 6/20/16.
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

        ArrayList<String> items = new ArrayList<String>(Arrays.asList(skus));
        //BillingPlugin.getInstance().queryInventoryItemInfo(items, gameObjectName);
    }

    //public static void BuyItem(String sku, int price, String itemInfo, String gameObjectName) {
    public static void BuyItem(String itemInfo, String gameObjectName) {

        try {
            JSONObject itemObject = new JSONObject(itemInfo);
            String sku = itemObject.optJSONObject("item").optString("product_id");
            String price = itemObject.optJSONObject("item").optString("product_price");
            String info = itemObject.optJSONObject("item").optString("product_name");
            String toUser = itemObject.optString("to");

            JLog.d(TAG + "sku = %s, price=%s, info = %s, toUser = %s", sku, price, info, toUser);


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

        JoypleInAppManager.InitInAppService(userKey, new JoypleInAppListener.OnIabSetupFinishedListener() {

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

    private void queryInventoryItemInfo(ArrayList<String> items, final String gameObjectName) throws JoypleException{

        // TODO: API 지원 여부를 어디서 판단하는게 좋을까?
        if (JoypleSettings.getMarket() != PlatformType.Market.GOOGLE)
            throw new JoypleException("Not Supported");

        //callbackObjectNames.put(gameObjectName, gameObjectName);

        //JoypleInAppManager.QueryInventory(items, );
/*
        JoycityIabService.getInstance().queryInventoryItems(true, items, new JoycityIabService.OnQueryInventoryFinishedListener() {

            @Override
            public void onQueryInventoryFinished(IabResult result, IabInventory inv) {
                // TODO Auto-generated method stub
                Logger.d(TAG + "Query inventory item info finished.");

                String callResponseName = (result.isSuccess() == true) ? ASYNC_CALL_SUCCEEDED : ASYNC_CALL_FAILED;

                JSONObject response = new JSONObject();
                JSONObject iab_response = new JSONObject();
                JSONObject error_response = new JSONObject();

                try {

                    if (!result.isSuccess()) {
                        error_response.put(API_RESPONSE_ERROR_CODE_KEY, result.getResponse());
                        error_response.put(API_RESPONSE_ERROR_MESSAGE_KEY, result.getMessage());
                        iab_response.put(ERROR_KEY, error_response);

                        response.put(RESULT_KEY, error_response);
                    } else {
                        JSONObject items_response = new JSONObject();

                        if (inv != null) {

                            List<String> purchases = inv.getAllQueriedSkus(true);

                            int count = 0;
                            int purchasesCount = purchases.size();

                            items_response.put("count", purchasesCount);

                            if (purchasesCount > 0) {
                                for (String purchase : purchases) {
                                    IabSkuDetails item = inv.getSkuDetails(purchase);
                                    JSONObject item_response = new JSONObject();

                                    if (Joycity.getMarket() == Market.ONESTORE) {
                                        item_response.put("title", item.getTitle());
                                        item_response.put("product_id", item.getSku());
                                        item_response.put("price", item.getPrice());
                                        item_response.put("type", item.getType());
                                        item_response.put("item_type", item.getItemType());
                                        item_response.put("validity", item.getmValidity());
                                    } else {
                                        item_response.put("title", item.getTitle());
                                        item_response.put("description", item.getDescription());
                                        item_response.put("product_id", item.getSku());

                                        JSONObject o = new JSONObject(item.getJson());

                                        String price_symbol = item.getPrice();
                                        item_response.put("price", price_symbol.substring(1));
                                        item_response.put("currency_symbol", price_symbol.substring(0, 1));
                                        item_response.put("currency", o.optString("price_currency_code"));
                                        items_response.put("item"+String.valueOf(count), item_response);
                                    }
                                    count++;
                                }
                            }
                        }

                        iab_response.put(DATA_KEY, items_response);
                    }

                    response.put(RESULT_KEY, iab_response);

                } catch (JSONException e) {
                    Logger.d(TAG + "JSONException = %s", e.getMessage());
                }

                UnityPlayer.UnitySendMessage(gameObjectName, callResponseName, response.toString());
            }

        });
*/
    }

    private void buyItemWithInfo(String sku, String price, String itemInfo, String toUserKey, final String gameObjectName) {

        //callbackObjectNames.add(gameObjectName);
        callbackObjectNames.put(gameObjectName, gameObjectName);

        JoypleInAppManager.BuyItem(getActivity(), toUserKey, new JoypleInAppItem(price, sku, itemInfo, "inapp"), new JoypleInAppListener.OnPurchaseFinishedListener() {

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
                    JLog.d(TAG + "JSONException = %s", e.getMessage());
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
                    JLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_FAIL, response.toString());
            }
        });
    }

    private void restoreItems(final String gameObjectName) {

        //callbackObjectNames.add(gameObjectName);
        callbackObjectNames.put(gameObjectName, gameObjectName);

        JoypleInAppManager.ReStoreItems(new JoypleInAppListener.OnRestoreItemsFinishedListener() {
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
                    JLog.d(TAG + "JSONException = %s", e.getMessage());
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
                    JLog.d(TAG + "JSONException = %s", e.getMessage());
                }

                SendUnityMessage(callbackObjectNames.remove(gameObjectName), ASYNC_RESULT_FAIL, response.toString());

            }
        });
    }

}
