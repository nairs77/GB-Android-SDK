package com.gebros.platform.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.gebros.platform.exception.GBException;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.pay.google.IabHelper;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by nairs77@joycity.com on 5/20/16.
 */
class GoogleIabHelper implements IIabHelper {

    private static final String TAG = "[GoogleIabService]";
    public static final int REQUEST_CODE_GOOGLE_PAYMENT = 9998;

    private IabHelper mIabHelper;
    private WeakReference<Context> contextRef;


    public GoogleIabHelper(Context context, String publicKey) {
        contextRef = new WeakReference<Context>(context);
        mPublicKey = publicKey;
    }

    public boolean isIabServiceInitialized() {
        return (mIabHelper != null);
    }

    /**
     * Starts in-app billing service and notifies the given listener upon completion.
     *
     * @param initListener the listener to notify when the startIabService process completes
     */
    public void startIabService(final IIabCallback.OnInitListener initListener) throws GBException {
        startIabHelper(new OnIabSetupFinishedListener(initListener));
    }

    /**
     * Stops in-app billing service and notifies the given listener upon completion.
     *
     * @param initListener the listener to notify when the stopIabService process completes
     */
    public void stopIabService(IIabCallback.OnInitListener initListener) {

    }

    public void queryInventoryAsync(List<String> moreSkus, IIabCallback.OnQueryInventoryListener queryInventoryListener) {
        if (moreSkus == null) {
            mIabHelper.queryInventoryAsync(new OnQueryInventoryFinishedListener(queryInventoryListener));
        } else {
            mIabHelper.queryInventoryAsync(true, moreSkus, new OnQueryInventoryFinishedListener(queryInventoryListener));
        }
    }

    public void launchPurchaseFlow(Activity activity, String userKey, GBInAppItem item, IIabCallback.OnPurchaseListener purChasesListener, String extraData) {
        mIabHelper.launchPurchaseFlow(activity, item.getSku(), item.getItemType(), REQUEST_CODE_GOOGLE_PAYMENT, new OnIabPurchaseFinishedListener(purChasesListener), extraData);
    }

    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return isIabServiceInitialized() && mIabHelper.handleActivityResult(requestCode, resultCode, data);
    }

    public void consume(List<IabPurchase> purchases, IIabCallback.OnConsumeListener consumeListener) {
        mIabHelper.consumeAsync(purchases, new OnIabConsumeFinishedListener(consumeListener));
    }


    /*====================   Private Methods   ====================*/

    /**
     * Create a new IAB helper and set it up.
     *
     * @param onIabSetupFinishedListener is a callback that lets users to add their own implementation for when the Iab is started
     */
    private void startIabHelper(OnIabSetupFinishedListener onIabSetupFinishedListener) {
        if (isIabServiceInitialized())
        {
            GBLog.d(TAG + "The helper is started. Just running the post start function.");

            if (onIabSetupFinishedListener != null && onIabSetupFinishedListener.getIabInitListener() != null) {
                //TODO: Fix Temp String
                IabResult result = new IabResult(IabResult.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED, "Already owned", getApplicationContext());
                onIabSetupFinishedListener.getIabInitListener().success(result);
            }
            return;
        }

        mIabHelper = new IabHelper(getApplicationContext(), mPublicKey);
        mIabHelper.startSetup(onIabSetupFinishedListener);
    }

    /**
     * Dispose of the helper to prevent memory leaks
     */
    private void stopIabHelper(IIabCallback.OnInitListener iabInitListener) {
        IabResult result = null;

        //FIXME: Always false -> keep IabService Open variable
        if (false) {
            String msg = "Not stopping Google Service b/c the user run 'startIabServiceInBg'. Keeping it open.";
            result = new IabResult(IabResult.BILLING_RESPONSE_RESULT_ERROR, msg, getApplicationContext());
            if (iabInitListener != null) {
                iabInitListener.fail(result);
            } else {
                GBLog.d(TAG + msg);
            }
            return;
        }

        if (mIabHelper == null) {
            String msg = "Tried to stop Google Service when it was null.";
            result = new IabResult(IabResult.BILLING_RESPONSE_RESULT_ERROR, msg, getApplicationContext());
            if (iabInitListener != null) {
                iabInitListener.fail(result);
            } else {
                GBLog.d(TAG + msg);
            }
            return;
        }

        if (!mIabHelper.isAsyncInProgress())
        {
            GBLog.d(TAG + "Stopping Google Service");
            mIabHelper.dispose();
            mIabHelper = null;
            if (iabInitListener != null) {
                iabInitListener.success(new IabResult(IabResult.BILLING_RESPONSE_RESULT_OK, "", getApplicationContext()));
            }
        }
        else
        {
            String msg = "Cannot stop Google Service during async process. Will be stopped when async operation is finished.";
            result = new IabResult(IabResult.BILLING_RESPONSE_RESULT_ERROR, msg, getApplicationContext());
            if (iabInitListener != null) {
                iabInitListener.fail(result);
            } else {
                GBLog.d(TAG + msg);
            }
        }
    }

    private class OnIabSetupFinishedListener implements IabHelper.OnIabSetupFinishedListener {

        private IIabCallback.OnInitListener mIabInitListener;

        public IIabCallback.OnInitListener getIabInitListener() {
            return mIabInitListener;
        }

        public OnIabSetupFinishedListener(IIabCallback.OnInitListener iabListener) {
            this.mIabInitListener = iabListener;
        }

        @Override
        public void onIabSetupFinished(IabResult result) {

            GBLog.d(TAG + "IAB helper Setup finished. result %s", result.mMessage);
            if (result.isFailure()) {
                if (mIabInitListener != null) mIabInitListener.fail(result);
                return;
            }
            if (mIabInitListener != null) mIabInitListener.success(result);
        }
    }

    /**
     * Handle incomplete purchase and refund after initialization
     */
    private class OnQueryInventoryFinishedListener implements IabHelper.OnQueryInventoryFinishedListener {

        private IIabCallback.OnQueryInventoryListener mQueryInventoryListener;

        public OnQueryInventoryFinishedListener(IIabCallback.OnQueryInventoryListener queryInventoryListener) {
            this.mQueryInventoryListener = queryInventoryListener;
        }

        public void onQueryInventoryFinished(IabResult result, IabInventory inventory) {
            GBLog.d(TAG + "Query inventory succeeded");

            if (result.getResponse() == IabResult.BILLING_RESPONSE_RESULT_OK && mQueryInventoryListener != null) {
                this.mQueryInventoryListener.success(inventory);
            } else {
                GBLog.d(TAG + "Wither mQueryInventoryListener==null OR Query inventory error: " + result.getMessage());
                if (this.mQueryInventoryListener != null)
                    this.mQueryInventoryListener.fail(new IabResult(IabResult.BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE, result.getMessage(), getApplicationContext()));
            }

            //stopIabHelper(null);
        }
    }


    private class OnIabPurchaseFinishedListener implements IabHelper.OnIabPurchaseFinishedListener {

        private IIabCallback.OnPurchaseListener mPurchaseListener;

        public OnIabPurchaseFinishedListener(IIabCallback.OnPurchaseListener purChaseListenter) {
            this.mPurchaseListener = purChaseListenter;
        }

        public void onIabPurchaseFinished(IabResult result, IabPurchase info) {
            GBLog.d(TAG + "Purchase Finished");

            int response = result.getResponse();

            if (response == IabResult.BILLING_RESPONSE_RESULT_USER_CANCELED) {
                this.mPurchaseListener.cancelled(null);
            } else if (response == IabResult.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
                this.mPurchaseListener.alreadyOwned(info);
            } else if (response == IabResult.BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE) {

            } else if (response == IabResult.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE
                    || response <= IabResult.IABHELPER_ERROR_BASE) {
                this.mPurchaseListener.fail(result);
            }
            else {
                this.mPurchaseListener.success(info);
            }


        }
    }

    private class OnIabConsumeFinishedListener implements IabHelper.OnConsumeMultiFinishedListener {

        private IIabCallback.OnConsumeListener mConsumeListener;

        public OnIabConsumeFinishedListener(IIabCallback.OnConsumeListener consumeListener) {
            this.mConsumeListener = consumeListener;
        }

        public void onConsumeMultiFinished(List<IabPurchase> purchases, List<IabResult> results) {
            GBLog.d(TAG + "onConsumeMultiFinished");

            this.mConsumeListener.OnConsumeFinishedListener(purchases, results);
        }
    }

    private Context getApplicationContext() {
        return contextRef.get();
    }

    public void callThirdpartyAPI(final Activity activity) {}

    private String mPublicKey;

    private IIabCallback.OnPurchaseListener mPurchaseListener;
    private IIabCallback.OnConsumeListener mConsumeListener;
}
