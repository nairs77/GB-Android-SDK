/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gebros.platform.pay.google;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.android.vending.billing.IInAppBillingService;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.pay.IabException;
import com.gebros.platform.pay.IabInventory;
import com.gebros.platform.pay.IabPurchase;
import com.gebros.platform.pay.IabResult;
import com.gebros.platform.pay.IabSkuDetails;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
//import android.os.Looper;


/**
 * Provides convenience methods for in-app billing. You can create one instance of this
 * class for your application and use it to process in-app billing operations.
 * It provides synchronous (blocking) and asynchronous (non-blocking) methods for
 * many common in-app billing operations, as well as automatic signature
 * verification.
 *
 * After instantiating, you must perform setup in order to start using the object.
 * To perform setup, call the {@link #startSetup} method and provide a listener;
 * that listener will be notified when setup is complete, after which (and not before)
 * you may call other methods.
 *
 * After setup is complete, you will typically want to request an inventory of owned
 * items and subscriptions. See {@link #queryInventory}, {@link #queryInventoryAsync}
 * and related methods.
 *
 * When you are done with this object, don't forget to call {@link #dispose}
 * to ensure proper cleanup. This object holds a binding to the in-app billing
 * service, which will leak unless you dispose of it correctly. If you created
 * the object on an Activity's onCreate method, then the recommended
 * place to dispose of it is the Activity's onDestroy method.
 *
 * A note about threading: When using this object from a background thread, you may
 * call the blocking versions of methods; when using from a UI thread, call
 * only the asynchronous versions and handle the results via callbacks.
 * Also, notice that you can only call one asynchronous operation at a time;
 * attempting to start a second asynchronous operation while the first one
 * has not yet completed will result in an exception being thrown.
 *
 * @author Bruno Oliveira (Google)
 *
 */
public class IabHelper {
    // uncomment to verify big chunk google bug (over 20)
//    public static final int SKU_QUERY_MAX_CHUNK_SIZE = 50;
    public static final int SKU_QUERY_MAX_CHUNK_SIZE = 19;
    private static String TAG = "[IabHelper]";

    // Is setup done?
    boolean mSetupDone = false;

    // Are subscriptions supported?
    boolean mSubscriptionsSupported = false;

    // Is an asynchronous operation in progress?
    // (only one at a time can be in progress)
    boolean mAsyncInProgress = false;

    // (for logging/debugging)
    // if mAsyncInP!?*.java;!?*.form;!?*.class;!?*.groovy;!?*.scala;!?*.flex;!?*.kt;!?*.cljrogress == true, what asynchronous operation is in progress?
    String mAsyncOperation = "";

    // Connection to the service
    IInAppBillingService mService;
    ServiceConnection mServiceConn;

    Context mContext;
    
    String mPublicKey;
    // The request code used to launch purchase flow
    int mRequestCode;

    // The item type of the current purchase flow
    String mPurchasingItemType;

    // The SKU of the item in the current purchase flow
    String mPurchasingItemSku;

    // Keys for the responses from InAppBillingService
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";

    // Item types
    public static final String ITEM_TYPE_INAPP = "inapp";
    public static final String ITEM_TYPE_SUBS = "subs";

    // some fields on the getSkuDetails response bundle
    public static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    public static final String GET_SKU_DETAILS_ITEM_TYPE_LIST = "ITEM_TYPE_LIST";

    /**
     * Creates an instance. After creation, it will not yet be ready to use. You must perform
     * setup by calling {@link #startSetup} and wait for setup to complete. This constructor does not
     * block and is safe to call from a UI thread.
     */
    public IabHelper(Context context, String publicKey) {
        GBLog.d(TAG + "IABHelper created");
        mContext = context;//context.getApplicationContext();
        mPublicKey = publicKey;
    }

    /**
     * Callback for setup process. This listener's {@link #onIabSetupFinished} method is called
     * when the setup process is complete.
     */
    public interface OnIabSetupFinishedListener {
        /**
         * Called to notify that setup is complete.
         *
         * @param result The result of the setup process.
         */
        public void onIabSetupFinished(IabResult result);
    }

    /**
     * Starts the setup process. This will start up the setup process asynchronously.
     * You will be notified through the listener when the setup process is complete.
     * This method is safe to call from a UI thread.
     *
     * @param listener The listener to notify when the setup process is complete.
     */
    public void startSetup(final OnIabSetupFinishedListener listener) {
        // If already set up, can't do it again.
        if (mSetupDone) throw new IllegalStateException("IAB helper is already set up.");

        // Connection to IAB service
        GBLog.d(TAG + "Starting in-app billing setup.");
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                GBLog.d(TAG + "Billing service disconnected.");
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                GBLog.d(TAG + "Billing service connected.");
                mService = IInAppBillingService.Stub.asInterface(service);
                String packageName = mContext.getPackageName();
                try {
                    GBLog.d(TAG + "Checking for in-app billing 3 support.");

                    // check for in-app billing v3 support
                    int response = mService.isBillingSupported(3, packageName, ITEM_TYPE_INAPP);
                    if (response != IabResult.BILLING_RESPONSE_RESULT_OK) {
                        if (listener != null) {
                            listener.onIabSetupFinished(new IabResult(response,
                                    "Error checking for billing v3 support.", mContext));
                        }

                        // if in-app purchases aren't supported, neither are subscriptions.
                        mSubscriptionsSupported = false;
                        return;
                    }
                    GBLog.d(TAG + "In-app billing version 3 supported for " + packageName);

                    // check for v3 subscriptions support
                    response = mService.isBillingSupported(3, packageName, ITEM_TYPE_SUBS);
                    if (response == IabResult.BILLING_RESPONSE_RESULT_OK) {
                        GBLog.d(TAG + "Subscriptions AVAILABLE.");
                        mSubscriptionsSupported = true;
                    }
                    else {
                        GBLog.d(TAG + "Subscriptions NOT AVAILABLE. Response: " + response);
                    }

                    mSetupDone = true;
                }
                catch (RemoteException e) {
                    if (listener != null) {
                        listener.onIabSetupFinished(new IabResult(IabResult.IABHELPER_REMOTE_EXCEPTION,
                                                    "RemoteException while setting up in-app billing.", mContext));
                    }
                    e.printStackTrace();
                    return;
                }

                if (listener != null) {
                    listener.onIabSetupFinished(new IabResult(IabResult.BILLING_RESPONSE_RESULT_OK, "Setup successful.", mContext));
                }
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
       
        if (!mContext.getPackageManager().queryIntentServices(serviceIntent, 0).isEmpty()) {
            // service available to handle that Intent
            mContext.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        }
        else {
            // no service available to handle that Intent
            if (listener != null) {
                listener.onIabSetupFinished(
                        new IabResult(IabResult.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE,
                        "Billing service unavailable on device.", mContext));
            }
        }
    }

    /**
     * Dispose of object, releasing resources. It's very important to call this
     * method when you are done with this object. It will release any resources
     * used by it such as service connections. Naturally, once the object is
     * disposed of, it can't be used again.
     */
    public void dispose() {
        GBLog.d(TAG + "Disposing.");
        mSetupDone = false;
        if (mServiceConn != null) {
            GBLog.d(TAG + "Unbinding from service.");
            if (mContext != null) mContext.unbindService(mServiceConn);
        }
        mServiceConn = null;
        mService = null;
        mPurchaseListener = null;
    }

    /** Returns whether subscriptions are supported. */
    public boolean subscriptionsSupported() {
        return mSubscriptionsSupported;
    }


    /**
     * Callback that notifies when a purchase is finished.
     */
    public interface OnIabPurchaseFinishedListener {
        /**
         * Called to notify that an in-app purchase finished. If the purchase was successful,
         * then the sku parameter specifies which item was purchased. If the purchase failed,
         * the sku and extraData parameters may or may not be null, depending on how far the purchase
         * process went.
         *
         * @param result The result of the purchase.
         * @param info The purchase information (null if purchase failed)
         */
        public void onIabPurchaseFinished(IabResult result, IabPurchase info);
    }

    // The listener registered on launchPurchaseFlow, which we have to call back when
    // the purchase finishes
    OnIabPurchaseFinishedListener mPurchaseListener;

    public void launchPurchaseFlow(Activity act, String sku, int requestCode, OnIabPurchaseFinishedListener listener) {
        launchPurchaseFlow(act, sku, requestCode, listener, "");
    }

    public void launchPurchaseFlow(Activity act, String sku, int requestCode,
                                   OnIabPurchaseFinishedListener listener, String extraData) {
        launchPurchaseFlow(act, sku, ITEM_TYPE_INAPP, requestCode, listener, extraData);
    }

    public void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode,
                                               OnIabPurchaseFinishedListener listener) {
        launchSubscriptionPurchaseFlow(act, sku, requestCode, listener, "");
    }

    public void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode,
                                               OnIabPurchaseFinishedListener listener, String extraData) {
        launchPurchaseFlow(act, sku, ITEM_TYPE_SUBS, requestCode, listener, extraData);
    }

    /**
     * Initiate the UI flow for an in-app purchase. Call this method to initiate an in-app purchase,
     * which will involve bringing up the Google Play screen. The calling activity will be paused while
     * the user interacts with Google Play, and the result will be delivered via the activity's
     * {@link Activity#onActivityResult} method, at which point you must call
     * this object's {@link #handleActivityResult} method to continue the purchase flow. This method
     * MUST be called from the UI thread of the Activity.
     *
     * @param act The calling activity.
     * @param sku The sku of the item to purchase.
     * @param itemType indicates if it's a product or a subscription (ITEM_TYPE_INAPP or ITEM_TYPE_SUBS)
     * @param requestCode A request code (to differentiate from other responses --
     *     as in {@link Activity#startActivityForResult}).
     * @param listener The listener to notify when the purchase process finishes
     * @param extraData Extra data (developer payload), which will be returned with the purchase data
     *     when the purchase completes. This extra data will be permanently bound to that purchase
     *     and will always be returned when the purchase is queried.
     */
    public void launchPurchaseFlow(Activity act, String sku, String itemType, int requestCode,
                                   OnIabPurchaseFinishedListener listener, String extraData) {
    	IabResult result;
        boolean isDone = checkSetupDone("launchPurchaseFlow");
        
        if (isDone) {
        	GBLog.d(TAG + "Purchase error. Please try again later.");
        	result = new IabResult(IabResult.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE, "An error occurred when attempting to purchase. Please restart the game.", mContext);
        	listener.onIabPurchaseFinished(result, null);
        	return;
        }
        
        boolean isBeforeBillingStarted = flagStartAsync("launchPurchaseFlow");
        
        if (isBeforeBillingStarted) {
        	GBLog.d(TAG + "Purchase error. Please try again later.");
        	result = new IabResult(IabResult.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE, "An error occurred when attempting to purchase. Please restart the game.", mContext);
        	listener.onIabPurchaseFinished(result, null);
        	return;
        }

        if (itemType.equals(ITEM_TYPE_SUBS) && !mSubscriptionsSupported) {
            IabResult r = new IabResult(IabResult.IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE,
                    "Subscriptions are not available.", mContext);
            if (listener != null) listener.onIabPurchaseFinished(r, null);
            return;
        }

        try {
            GBLog.d(TAG + "Constructing buy intent for " + sku + ", item type: " + itemType);
            String developerPayload = extraData+"#"+sku;
            Bundle buyIntentBundle = mService.getBuyIntent(3, mContext.getPackageName(), sku, itemType, developerPayload);
            buyIntentBundle.putString("PURCHASE_SKU", sku);
            int response = getResponseCodeFromBundle(buyIntentBundle);
            if (response != IabResult.BILLING_RESPONSE_RESULT_OK) {
                GBLog.d(TAG + "Unable to buy item, Error response: " + IabResult.getResponseDesc(response, mContext));
                
                IabPurchase failPurchase = new IabPurchase(itemType, "{\"productId\":" + sku + "}", null);
                result = new IabResult(response, "Unable to buy item", mContext);
                if (listener != null) listener.onIabPurchaseFinished(result, failPurchase);
                // make sure to end the async operation...
                flagEndAsync();
                //act.finish();
                return;
            }

            PendingIntent pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
            GBLog.d(TAG + "Launching buy intent for " + sku + ". Request code: " + requestCode);
            mRequestCode = requestCode;
            mPurchaseListener = listener;
            mPurchasingItemSku = sku;
            mPurchasingItemType = itemType;

            act.startIntentSenderForResult(pendingIntent.getIntentSender(),
                                           requestCode, new Intent(),
                                           Integer.valueOf(0), Integer.valueOf(0),
                                           Integer.valueOf(0));
        } catch (SendIntentException e) {
            GBLog.d(TAG + "SendIntentException while launching purchase flow for sku " + sku);
            e.printStackTrace();

            result = new IabResult(IabResult.IABHELPER_SEND_INTENT_FAILED, "Failed to send intent.", mContext);
            if (listener != null) listener.onIabPurchaseFinished(result, null);
        } catch (RemoteException e) {
        	GBLog.d(TAG + "RemoteException while launching purchase flow for sku " + sku);
            e.printStackTrace();

            result = new IabResult(IabResult.IABHELPER_REMOTE_EXCEPTION, "Remote exception while starting purchase flow", mContext);
            if (listener != null) listener.onIabPurchaseFinished(result, null);
        } catch (JSONException e) {
        	GBLog.d(TAG + "Failed to generate failing purchase.");
            e.printStackTrace();
            
            result = new IabResult(IabResult.IABHELPER_BAD_RESPONSE, "Failed to generate failing purchase.", mContext);
            if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(result, null);
        }
    }

    /**
     * Handles an activity result that's part of the purchase flow in in-app billing. If you
     * are calling {@link #launchPurchaseFlow}, then you must call this method from your
     * Activity's {@link Activity @onActivityResult} method. This method
     * MUST be called from the UI thread of the Activity.
     *
     * @param requestCode The requestCode as you received it.
     * @param resultCode The resultCode as you received it.
     * @param data The data (Intent) as you received it.
     * @return Returns true if the result was related to a purchase flow and was handled;
     *     false if the result was not related to a purchase, in which case you should
     *     handle it normally.
     */
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        IabResult result;
        if (requestCode != mRequestCode) return false;

        checkSetupDone("handleActivityResult");

        // end of async purchase operation
        flagEndAsync();

        if (data == null) {
        	GBLog.d(TAG + "Null data in IAB activity result.");
            result = new IabResult(IabResult.IABHELPER_BAD_RESPONSE, "Null data in IAB result", mContext);
            if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(result, null);
            return true;
        }

        int responseCode = getResponseCodeFromIntent(data);
        String purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
        String dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);

        if (resultCode == Activity.RESULT_OK && responseCode == IabResult.BILLING_RESPONSE_RESULT_OK) {
            GBLog.d(TAG + "Successful resultcode from purchase activity.");
            GBLog.d(TAG + "IabPurchase data: " + purchaseData);
            GBLog.d(TAG + "Data signature: " + dataSignature);
            GBLog.d(TAG + "Extras: " + data.getExtras());
            GBLog.d(TAG + "Expected item type: " + mPurchasingItemType);

            if (purchaseData == null || dataSignature == null) {
            	GBLog.d(TAG + "BUG: either purchaseData or dataSignature is null.");
                GBLog.d(TAG + "Extras: " + data.getExtras().toString());
                result = new IabResult(IabResult.IABHELPER_UNKNOWN_ERROR, "IAB returned null purchaseData or dataSignature", mContext);
                if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(result, null);
                return true;
            }

            IabPurchase purchase = null;
            try {
                purchase = new IabPurchase(mPurchasingItemType, purchaseData, dataSignature);
                String sku = purchase.getSku();

                // Verify signature
                if (!Security.verifyPurchase(mPublicKey, purchaseData, dataSignature)) {
                    GBLog.d(TAG + "IabPurchase signature verification FAILED for sku " + sku);
                    result = new IabResult(IabResult.IABHELPER_VERIFICATION_FAILED, "Signature verification failed for sku " + sku, mContext);
                    if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(result, purchase);
                    return true;
                }
                GBLog.d(TAG + "IabPurchase signature successfully verified.");
            }
            catch (JSONException e) {
                GBLog.d(TAG + "Failed to parse purchase data.");
                e.printStackTrace();
                result = new IabResult(IabResult.IABHELPER_BAD_RESPONSE, "Failed to parse purchase data.", mContext);
                if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(result, null);
                return true;
            }

            if (mPurchaseListener != null) {
                mPurchaseListener.onIabPurchaseFinished(new IabResult(IabResult.BILLING_RESPONSE_RESULT_OK, "Success", mContext), purchase);
            }
        }
        else if (resultCode == Activity.RESULT_OK) {
            // result code was OK, but in-app billing response was not OK.
            GBLog.d(TAG + "Result code was OK but in-app billing response was not OK: " + IabResult.getResponseDesc(responseCode, mContext));
            if (mPurchaseListener != null) {
                result = new IabResult(responseCode, "Problem purchashing item.", mContext);
                mPurchaseListener.onIabPurchaseFinished(result, null);
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            GBLog.d(TAG + "IabPurchase canceled. Response: " + IabResult.getResponseDesc(responseCode, mContext));
            try {
                IabPurchase purchase = new IabPurchase(mPurchasingItemType, "{\"productId\":" + mPurchasingItemSku + "}", null);
                result = new IabResult(IabResult.BILLING_RESPONSE_RESULT_USER_CANCELED, "User canceled.", mContext);
                if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(result, purchase);
            } catch (JSONException e) {
                GBLog.d(TAG + "Failed to generate canceled purchase.");
                e.printStackTrace();
                result = new IabResult(IabResult.IABHELPER_BAD_RESPONSE, "Failed to generate canceled purchase.", mContext);
                if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(result, null);
                return true;
            }
        }
        else {
        	GBLog.d(TAG + "IabPurchase failed. Result code: " + Integer.toString(resultCode)
                    + ". Response: " + IabResult.getResponseDesc(responseCode, mContext));
            result = new IabResult(IabResult.IABHELPER_UNKNOWN_PURCHASE_RESPONSE, "Unknown purchase response.", mContext);
            if (mPurchaseListener != null) mPurchaseListener.onIabPurchaseFinished(result, null);
        }
        return true;
    }

    public IabInventory queryInventory(boolean querySkuDetails, List<String> moreSkus) throws IabException {
        return queryInventory(querySkuDetails, moreSkus, null);
    }

    /**
     * Queries the inventory. This will query all owned items from the server, as well as
     * information on additional skus, if specified. This method may block or take long to execute.
     * Do not call from a UI thread. For that, use the non-blocking version {@link #queryInventoryAsync}.
     *
     * @param querySkuDetails if true, SKU details (price, description, etc) will be queried as well
     *     as purchase information.
     * @param moreItemSkus additional PRODUCT skus to query information on, regardless of ownership.
     *     Ignored if null or if querySkuDetails is false.
     * @param moreSubsSkus additional SUBSCRIPTIONS skus to query information on, regardless of ownership.
     *     Ignored if null or if querySkuDetails is false.
     * @throws IabException if a problem occurs while refreshing the inventory.
     */
    public IabInventory queryInventory(boolean querySkuDetails, List<String> moreItemSkus,
                                        List<String> moreSubsSkus) throws IabException {
        checkSetupDone("queryInventory");
        try {
            IabInventory inv = new IabInventory();
            int r = queryPurchases(inv, ITEM_TYPE_INAPP);
            if (r != IabResult.BILLING_RESPONSE_RESULT_OK) {
                throw new IabException(r, "Error refreshing inventory (querying owned items).", mContext);
            }
            //
            if (querySkuDetails) {
                r = querySkuDetails(ITEM_TYPE_INAPP, inv, moreItemSkus);
                if (r != IabResult.BILLING_RESPONSE_RESULT_OK) {
                    throw new IabException(r, "Error refreshing inventory (querying prices of items).", mContext);
                }
            }

            // if subscriptions are supported, then also query for subscriptions
            if (mSubscriptionsSupported) {
                r = queryPurchases(inv, ITEM_TYPE_SUBS);
                if (r != IabResult.BILLING_RESPONSE_RESULT_OK) {
                    throw new IabException(r, "Error refreshing inventory (querying owned subscriptions).", mContext);
                }

                if (querySkuDetails) {
                    r = querySkuDetails(ITEM_TYPE_SUBS, inv, moreItemSkus);
                    if (r != IabResult.BILLING_RESPONSE_RESULT_OK) {
                        throw new IabException(r, "Error refreshing inventory (querying prices of subscriptions).", mContext);
                    }
                }
            }

            return inv;
        }
        catch (RemoteException e) {
            throw new IabException(IabResult.IABHELPER_REMOTE_EXCEPTION, "Remote exception while refreshing inventory.", e, mContext);
        }
        catch (JSONException e) {
            throw new IabException(IabResult.IABHELPER_BAD_RESPONSE, "Error parsing JSON response while refreshing inventory.", e, mContext);
        }
    }

    /**
     * Listener that notifies when an inventory query operation completes.
     */
    public interface OnQueryInventoryFinishedListener {
        /**
         * Called to notify that an inventory query operation completed.
         *
         * @param result The result of the operation.
         * @param inv The inventory.
         */
        public void onQueryInventoryFinished(IabResult result, IabInventory inv);
    }


    /**
     * Asynchronous wrapper for inventory query. This will perform an inventory
     * query as described in {@link #queryInventory}, but will do so asynchronously
     * and call back the specified listener upon completion. This method is safe to
     * call from a UI thread.
     *
     * @param querySkuDetails as in {@link #queryInventory}
     * @param moreSkus as in {@link #queryInventory}
     * @param listener The listener to notify when the refresh operation completes.
     */
    public void queryInventoryAsync(final boolean querySkuDetails,
                               final List<String> moreSkus,
                               final OnQueryInventoryFinishedListener listener) {
        final Handler handler = new Handler();
        checkSetupDone("queryInventory");
        flagStartAsync("refresh inventory");
        (new Thread(new Runnable() {
            public void run() {
                IabResult result = new IabResult(IabResult.BILLING_RESPONSE_RESULT_OK, "IabInventory refresh successful.", mContext);
                IabInventory inv = null;
                try {
                    inv = queryInventory(querySkuDetails, moreSkus);
                }
                catch (IabException ex) {
                    result = ex.getResult();
                }

                flagEndAsync();

                final IabResult result_f = result;
                final IabInventory inv_f = inv;
                handler.post(new Runnable() {
                    public void run() {
                        listener.onQueryInventoryFinished(result_f, inv_f);
                    }
                });
            }
        })).start();
    }

    public void queryInventoryAsync(OnQueryInventoryFinishedListener listener) {
        queryInventoryAsync(true, null, listener);
    }

    public void queryInventoryAsync(boolean querySkuDetails, OnQueryInventoryFinishedListener listener) {
        queryInventoryAsync(true, null, listener);
    }


    /**
     * Consumes a given in-app product. Consuming can only be done on an item
     * that's owned, and as a result of consumption, the user will no longer own it.
     * This method may block or take long to return. Do not call from the UI thread.
     * For that, see {@link #consumeAsync}.
     *
     * @param itemInfo The PurchaseInfo that represents the item to consume.
     * @throws IabException if there is a problem during consumption.
     */
     public void consume(IabPurchase itemInfo) throws IabException {
        checkSetupDone("consume");

        if (!itemInfo.getItemType().equals(ITEM_TYPE_INAPP)) {
            throw new IabException(IabResult.IABHELPER_INVALID_CONSUMPTION,
                    "Items of type '" + itemInfo.getItemType() + "' can't be consumed.", mContext);
        }

        try {
            String token = itemInfo.getToken();
            String sku = itemInfo.getSku();
            if (token == null || token.equals("")) {
            	GBLog.d(TAG + "Can't consume "+ sku + ". No token.");
               throw new IabException(IabResult.IABHELPER_MISSING_TOKEN, "PurchaseInfo is missing token for sku: "
                   + sku + " " + itemInfo, mContext);
            }

            GBLog.d(TAG + "Consuming sku: " + sku + ", token: " + token);
            int response = mService.consumePurchase(3, mContext.getPackageName(), token);
            if (response == IabResult.BILLING_RESPONSE_RESULT_OK) {
               GBLog.d(TAG + "Successfully consumed sku: " + sku);
            }
            else {
               GBLog.d(TAG + "Error consuming consuming sku " + sku + ". " + IabResult.getResponseDesc(response, mContext));
               throw new IabException(response, "Error consuming sku " + sku, mContext);
            }
        }
        catch (RemoteException e) {
            throw new IabException(IabResult.IABHELPER_REMOTE_EXCEPTION, "Remote exception while consuming. PurchaseInfo: " + itemInfo, e, mContext);
        }
    }

    /**
     * Callback that notifies when a consumption operation finishes.
     */
    public interface OnConsumeFinishedListener {
        /**
         * Called to notify that a consumption has finished.
         *
         * @param purchase The purchase that was (or was to be) consumed.
         * @param result The result of the consumption operation.
         */
        public void onConsumeFinished(IabPurchase purchase, IabResult result);
    }

    /**
     * Callback that notifies when a multi-item consumption operation finishes.
     */
    public interface OnConsumeMultiFinishedListener {
        /**
         * Called to notify that a consumption of multiple items has finished.
         *
         * @param purchases The purchases that were (or were to be) consumed.
         * @param results The results of each consumption operation, corresponding to each
         *     sku.
         */
        public void onConsumeMultiFinished(List<IabPurchase> purchases, List<IabResult> results);
    }

    /**
     * Asynchronous wrapper to item consumption. Works like {@link #consume}, but
     * performs the consumption in the background and notifies completion through
     * the provided listener. This method is safe to call from a UI thread.
     *
     * @param purchase The purchase to be consumed.
     * @param listener The listener to notify when the consumption operation finishes.
     */
    public void consumeAsync(IabPurchase purchase, OnConsumeFinishedListener listener) {
        checkSetupDone("consume");
        List<IabPurchase> purchases = new ArrayList<IabPurchase>();
        purchases.add(purchase);
        consumeAsyncInternal(purchases, listener, null);
    }

    /**
     * Same as {@link #consumeAsync}, but for multiple items at once.
     * @param purchases The list of PurchaseInfo objects representing the purchases to consume.
     * @param listener The listener to notify when the consumption operation finishes.
     */
    public void consumeAsync(List<IabPurchase> purchases, OnConsumeMultiFinishedListener listener) {
        checkSetupDone("consume");
        consumeAsyncInternal(purchases, null, listener);
    }




    // Checks that setup was done; if not, throws an exception.
    boolean checkSetupDone(String operation) {
        if (!mSetupDone) {
        	GBLog.d(TAG + "Illegal state for operation (" + operation + "): IAB helper is not set up.");
//            throw new IllegalStateException("IAB helper is not set up. Can't perform operation: " + operation);
        	return true;
        }
        return false;
    }
    
//    void checkSetupDone(String operation) {
//        if (!mSetupDone) {
//        	GBLog.d(TAG + "Illegal state for operation (" + operation + "): IAB helper is not set up.");
//            throw new IllegalStateException("IAB helper is not set up. Can't perform operation: " + operation);
//        }
//    }

    // Workaround to bug where sometimes response codes come as Long instead of Integer
    int getResponseCodeFromBundle(Bundle b) {
        Object o = b.get(RESPONSE_CODE);
        if (o == null) {
            GBLog.d(TAG + "Bundle with null response code, assuming OK (known issue)");
            return IabResult.BILLING_RESPONSE_RESULT_OK;
        }
        else if (o instanceof Integer) return ((Integer)o).intValue();
        else if (o instanceof Long) return (int)((Long)o).longValue();
        else {
        	GBLog.d(TAG + "Unexpected type for bundle response code.");
        	GBLog.d(TAG + o.getClass().getName());
            throw new RuntimeException("Unexpected type for bundle response code: " + o.getClass().getName());
        }
    }

    // Workaround to bug where sometimes response codes come as Long instead of Integer
    int getResponseCodeFromIntent(Intent i) {
        Object o = i.getExtras().get(RESPONSE_CODE);
        if (o == null) {
        	GBLog.d(TAG + "Intent with no response code, assuming OK (known issue)");
            return IabResult.BILLING_RESPONSE_RESULT_OK;
        }
        else if (o instanceof Integer) return ((Integer)o).intValue();
        else if (o instanceof Long) return (int)((Long)o).longValue();
        else {
        	GBLog.d(TAG + "Unexpected type for intent response code.");
        	GBLog.d(TAG + o.getClass().getName());
            throw new RuntimeException("Unexpected type for intent response code: " + o.getClass().getName());
        }
    }

//    void flagStartAsync(String operation) {
//        if (mAsyncInProgress) throw new IllegalStateException("Can't start async operation (" +
//                operation + ") because another async operation(" + mAsyncOperation + ") is in progress.");
//        mAsyncOperation = operation;
//        mAsyncInProgress = true;
//        GBLog.d(TAG + "Starting async operation: " + operation);
//    }
    
    boolean flagStartAsync(String operation) {
        if (mAsyncInProgress) {
//        	throw new IllegalStateException("Can't start async operation (" +
//                    operation + ") because another async operation(" + mAsyncOperation + ") is in progress.");
        	return true;
        }
        mAsyncOperation = operation;
        mAsyncInProgress = true;
        GBLog.d(TAG + "Starting async operation: " + operation);
        return false;
    }

    void flagEndAsync() {
        GBLog.d(TAG + "Ending async operation: " + mAsyncOperation);
        mAsyncOperation = "";
        mAsyncInProgress = false;
    }

    int queryPurchases(IabInventory inv, String itemType) throws JSONException, RemoteException {
        // Query purchases
        GBLog.d(TAG + "Querying owned items, item type: " + itemType);
        GBLog.d(TAG + "Package name: " + mContext.getPackageName());
        boolean verificationFailed = false;
        String continueToken = null;

        do {
            GBLog.d(TAG + "Calling getPurchases with continuation token: " + continueToken);
            Bundle ownedItems = mService.getPurchases(3, mContext.getPackageName(),
                    itemType, continueToken);

            int response = getResponseCodeFromBundle(ownedItems);
            GBLog.d(TAG + "Owned items response: " + String.valueOf(response));
            if (response != IabResult.BILLING_RESPONSE_RESULT_OK) {
                GBLog.d(TAG + "getPurchases() failed: " + IabResult.getResponseDesc(response, mContext));
                return response;
            }
            if (!ownedItems.containsKey(RESPONSE_INAPP_ITEM_LIST)
                    || !ownedItems.containsKey(RESPONSE_INAPP_PURCHASE_DATA_LIST)
                    || !ownedItems.containsKey(RESPONSE_INAPP_SIGNATURE_LIST)) {
            	GBLog.d(TAG + "Bundle returned from getPurchases() doesn't contain required fields.");
                return IabResult.IABHELPER_BAD_RESPONSE;
            }

//            ArrayList<String> ownedSkus = ownedItems.getStringArrayList(
//                        RESPONSE_INAPP_ITEM_LIST);
            ArrayList<String> purchaseDataList = ownedItems.getStringArrayList(
                        RESPONSE_INAPP_PURCHASE_DATA_LIST);
            ArrayList<String> signatureList = ownedItems.getStringArrayList(
                        RESPONSE_INAPP_SIGNATURE_LIST);

            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
//                String sku = ownedSkus.get(i);
                if (Security.verifyPurchase(mPublicKey, purchaseData, signature)) {
                    GBLog.d(TAG + "Sku is owned: " + purchaseData);
                    IabPurchase purchase = new IabPurchase(itemType, purchaseData, signature);

                    if (TextUtils.isEmpty(purchase.getToken())) {
                    	GBLog.d(TAG + "BUG: empty/null token!");
                        GBLog.d(TAG + "IabPurchase data: " + purchaseData);
                    }

                    // Record ownership and token
                    inv.addPurchase(purchase);
                }
                else {
                	GBLog.d(TAG + "IabPurchase signature verification **FAILED**. Not adding item.");
                    GBLog.d(TAG + "   IabPurchase data: " + purchaseData);
                    GBLog.d(TAG + "   Signature: " + signature);
                    verificationFailed = true;
                }
            }

            continueToken = ownedItems.getString(INAPP_CONTINUATION_TOKEN);
            GBLog.d(TAG + "Continuation token: " + continueToken);
        } while (!TextUtils.isEmpty(continueToken));

        return verificationFailed ? IabResult.IABHELPER_VERIFICATION_FAILED : IabResult.BILLING_RESPONSE_RESULT_OK;
    }

    int querySkuDetails(String itemType, IabInventory inv, List<String> moreSkus)
                                throws RemoteException, JSONException {
    	
        GBLog.d(TAG + "Querying SKU details.");
/*
        // a list here is a bug no matter what, there is no point in
        // querying duplicates, and it can only create other bugs
        // on top of degrading performance
        // however, we need a subList later for chunks, so just
        // make the list through a Set 'filter'
        Set<String> skuSet = new HashSet<String>(moreSkus);
        skuSet.addAll(inv.getAllOwnedSkus());
        //if (moreSkus != null) skuSet.addAll(moreSkus);
        ArrayList<String> skuList = new ArrayList<String>(skuSet);

        if (skuList.size() == 0) {
            GBLog.d(TAG + "queryPrices: nothing to do because there are no SKUs.");
            return IabResult.BILLING_RESPONSE_RESULT_OK;
        }

        // see: http://stackoverflow.com/a/21080893/1469004
        int chunkIndex = 1;
        while (skuList.size() > 0) {
            ArrayList<String> skuSubList = new ArrayList<String>(
                    skuList.subList(0, Math.min(SKU_QUERY_MAX_CHUNK_SIZE, skuList.size())));
            skuList.removeAll(skuSubList);
            final int chunkResponse = querySkuDetailsChunk(itemType, inv, skuSubList);
            if (chunkResponse != IabResult.BILLING_RESPONSE_RESULT_OK) {
                // todo: TBD skip chunk or abort?
                // for now aborting at that point
                GBLog.d(TAG + String.format("querySkuDetails[chunk=%d] failed: %s",
                        chunkIndex, IabResult.getResponseDesc(chunkResponse, mContext)));
                return chunkResponse; // ABORT
            }
            chunkIndex++;
        }

        return IabResult.BILLING_RESPONSE_RESULT_OK;
*/        
        ArrayList<String> skuList = new ArrayList<String>();
        skuList.addAll(inv.getAllOwnedSkus(itemType));
        if (moreSkus != null) {
            for (String sku : moreSkus) {
                if (!skuList.contains(sku)) {
                    skuList.add(sku);
                } 
            } 
        } 
     
        if (skuList.size() == 0) {
        	GBLog.d(TAG + "queryPrices: nothing to do because there are no SKUs.");
            return IabResult.BILLING_RESPONSE_RESULT_OK; 
        } 
     
        while (skuList.size() > 0) {
            ArrayList<String> skuSubList = new ArrayList<String>(
                    skuList.subList(0, Math.min(SKU_QUERY_MAX_CHUNK_SIZE, skuList.size())));
            skuList.removeAll(skuSubList);
     
            Bundle querySkus = new Bundle();
            querySkus.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, skuSubList);
            Bundle skuDetails = mService.getSkuDetails(3,
                    mContext.getPackageName(), itemType, querySkus);
     
            if (!skuDetails.containsKey(RESPONSE_GET_SKU_DETAILS_LIST)) {
                int response = getResponseCodeFromBundle(skuDetails);
                if (response != IabResult.BILLING_RESPONSE_RESULT_OK) {
                    //logDebug("getSkuDetails() failed: " + getResponseDesc(response));
                	GBLog.d(TAG + "getSkuDetails() failed: " + IabResult.getResponseDesc(response, mContext));
                    return response;
                } else { 
                    //logError("getSkuDetails() returned a bundle with neither an error nor a detail list.");
                	GBLog.d(TAG + "getSkuDetails() returned a bundle with neither an error nor a detail list.");
                    return IabResult.IABHELPER_BAD_RESPONSE;// IABHELPER_BAD_RESPONSE; 
                } 
            } 
     
            ArrayList<String> responseList = skuDetails
                    .getStringArrayList(RESPONSE_GET_SKU_DETAILS_LIST);
     
            
            if (responseList.size() > 0) {
                for (String thisResponse : responseList) {
                	//GBLog.d(TAG + "Got sku details:" + thisResponse);
                	IabSkuDetails d = new IabSkuDetails(itemType, thisResponse);
                    //GBLog.d(TAG + "Got sku details: " + d.toString());
                    inv.addSkuDetails(d);            	
                }            	
            } else {
            	GBLog.d(TAG + "Empty sku details");
            }

        } 
        
        return IabResult.BILLING_RESPONSE_RESULT_OK;
             	
    }

    private int querySkuDetailsChunk(String itemType, IabInventory inv, ArrayList<String> chunkSkuList) throws RemoteException, JSONException {
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, chunkSkuList);
        Bundle skuDetails = mService.getSkuDetails(3, mContext.getPackageName(),
                itemType, querySkus);

        if (!skuDetails.containsKey(RESPONSE_GET_SKU_DETAILS_LIST)) {
        	int response = getResponseCodeFromBundle(skuDetails);
            if (response != IabResult.BILLING_RESPONSE_RESULT_OK) {
                GBLog.d(TAG + "querySkuDetailsChunk() failed: " + IabResult.getResponseDesc(response, mContext));
                return response;
            }
            else {
            	GBLog.d(TAG +  "querySkuDetailsChunk() returned a bundle with neither an error nor a detail list.");
                return IabResult.IABHELPER_BAD_RESPONSE;
            }
        }

        ArrayList<String> responseList = skuDetails.getStringArrayList(
                RESPONSE_GET_SKU_DETAILS_LIST);

        for (String thisResponse : responseList) {
            IabSkuDetails d = new IabSkuDetails(itemType, thisResponse);
            GBLog.d(TAG + "Got sku details: " + d.toString());
            inv.addSkuDetails(d);
        }

        return IabResult.BILLING_RESPONSE_RESULT_OK;
    }

    void consumeAsyncInternal(final List<IabPurchase> purchases,
                              final OnConsumeFinishedListener singleListener,
                              final OnConsumeMultiFinishedListener multiListener) {
        final Handler handler = new Handler();
        flagStartAsync("consume");
        (new Thread(new Runnable() {
            public void run() {
                final List<IabResult> results = new ArrayList<IabResult>();
                for (IabPurchase purchase : purchases) {
                    try {
                        consume(purchase);
                        results.add(new IabResult(IabResult.BILLING_RESPONSE_RESULT_OK, "Successful consume of sku " + purchase.getSku(), mContext));
                    }
                    catch (IabException ex) {
                        results.add(ex.getResult());
                    }
                }

                flagEndAsync();
                if (singleListener != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            singleListener.onConsumeFinished(purchases.get(0), results.get(0));
                        }
                    });
                }
                if (multiListener != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            multiListener.onConsumeMultiFinished(purchases, results);
                        }
                    });
                }
            }
        })).start();
    }

    public boolean isAsyncInProgress() {
        return mAsyncInProgress;
    }
}
