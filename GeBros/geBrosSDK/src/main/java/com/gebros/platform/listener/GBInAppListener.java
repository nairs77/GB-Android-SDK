package com.gebros.platform.listener;

import com.gebros.platform.pay.IabPurchase;
import com.gebros.platform.pay.IabResult;

import java.util.List;

/**
 * Created by nairs on 2016-05-15.
 */
public interface GBInAppListener {

    /**
     * Notify listener when setup is complete
     */
    public interface OnIabSetupFinishedListener {

        public void onSuccess();

        public void onFail();
    }

    /**
     * Notify listener when query inventory is complete.
     */
    public interface OnQueryInventoryFinishedListener {

        public void onSuccess();

        public void onFail();

    }

    /**
     * Notify listener when a Purchase is complete.
     */
    public interface OnPurchaseFinishedListener {

        public void onSuccess(IabPurchase purchaseInfo);

        public void onFail(IabResult result);

        public void onCancel(boolean isUserCancelled);
    }

    /**
     * Notify listener when restore is complete.
     */
    public interface OnRestoreItemsFinishedListener {

        public void onSuccess(List<String> paymentKeys);

        public void onFail(IabResult result);
    }
}
