package com.gebros.platform.pay;

import android.app.Activity;
import android.content.Intent;

import com.gebros.platform.exception.GBException;

import java.util.List;

/**
 * Created by nairs77@joycity.com on 5/20/16.
 */
public interface IIabHelper {

    /**
     * Check if In-app billing service is Initialized
     * @return true if initialized, false otherwise
     */
    public boolean isIabServiceInitialized();

    /**
     * Starts in-app billing service and notifies the given listener upon completion.
     *
     * @param initListener the listener to notify when the startIabService process completes
     */
    public void startIabService(IIabCallback.OnInitListener initListener) throws GBException;

    /**
     * Stops in-app billing service and notifies the given listener upon completion.
     *
     * @param initListener the listener to notify when the stopIabService process completes
     */
    public void stopIabService(IIabCallback.OnInitListener initListener) throws GBException;

    /**
     *
     * @param moreSkus
     * @param queryInventoryListener
     */
    public void queryInventoryAsync(List<String> moreSkus, IIabCallback.OnQueryInventoryListener queryInventoryListener) throws GBException;


    /**
     *
     * @param activity
     * @param item
     * @param purChasesListener
     * @param extraData
     */
    public void launchPurchaseFlow(Activity activity, String userKey, GBInAppItem item, IIabCallback.OnPurchaseListener purChasesListener, String extraData) throws GBException;


    /**
     *
     * @param client
     * @param activity
     * @param item
     * @param purChasesListener
     * @param extraData
     */
//    public void launchPurchaseFlow(IPlatformClient client, Activity activity, GBInAppItem item, IIabCallback.OnPurchaseListener purChasesListener, String extraData);

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */

    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) throws GBException;

    /**
     *
     * @param skus
     * @param consumeListener
     */
    public void consume(List<IabPurchase> skus, IIabCallback.OnConsumeListener consumeListener) throws GBException;

}

