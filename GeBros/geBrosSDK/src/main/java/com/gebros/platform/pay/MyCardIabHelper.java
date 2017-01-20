package com.gebros.platform.pay;

import android.app.Activity;
import android.content.Intent;

import com.gebros.platform.exception.GBException;

import java.util.List;

/**
 * Created by Joycity-Platform on 6/7/16.
 */
public class MyCardIabHelper implements IIabHelper {

    public MyCardIabHelper() {

    }
    /**
     * Check if In-app billing service is Initialized
     *
     * @return true if initialized, false otherwise
     */
    @Override
    public boolean isIabServiceInitialized() {
        return false;
    }

    /**
     * Starts in-app billing service and notifies the given listener upon completion.
     *
     * @param initListener the listener to notify when the startIabService process completes
     */
    @Override
    public void startIabService(IIabCallback.OnInitListener initListener) throws GBException {

    }

    /**
     * Stops in-app billing service and notifies the given listener upon completion.
     *
     * @param initListener the listener to notify when the stopIabService process completes
     */
    @Override
    public void stopIabService(IIabCallback.OnInitListener initListener) throws GBException {

    }

    /**
     * @param moreSkus
     * @param queryInventoryListener
     */
    @Override
    public void queryInventoryAsync(List<String> moreSkus, IIabCallback.OnQueryInventoryListener queryInventoryListener) throws GBException {

    }

    /**
     * @param activity
     * @param userKey
     * @param item
     * @param purChasesListener
     * @param extraData
     */
    @Override
    public void launchPurchaseFlow(Activity activity, String userKey, GBInAppItem item, IIabCallback.OnPurchaseListener purChasesListener, String extraData) throws GBException {

    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) throws GBException {
        return false;
    }

    /**
     * @param skus
     * @param consumeListener
     */
    @Override
    public void consume(List<IabPurchase> skus, IIabCallback.OnConsumeListener consumeListener) throws GBException {

    }
}
