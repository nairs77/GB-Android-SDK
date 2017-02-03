package com.gebros.platform.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.gebros.platform.exception.GBException;
import com.gebros.platform.listener.GBInAppListener;
import com.gebros.platform.platform.IPlatformClient;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by gebros.nairs77@gmail.com on 5/20/16.
 */
class PlatformIabHelper implements IIabHelper {

    protected GBInAppImpl mInAppImpl;
    private WeakReference<Context> contextRef;

    //public PlatformIabHelper(Context context, IPlatformClient client) {
    public PlatformIabHelper(Context context, GBInAppImpl inAppImpl) {
        contextRef = new WeakReference<Context>(context);
        mInAppImpl = inAppImpl;
    }

    public boolean isIabServiceInitialized() {
        return (mInAppImpl != null) ? true : false;
    };

    private IPlatformClient getClient() {
        return mInAppImpl.getClient();
    }

    /**
     * Starts in-app billing service and notifies the given listener upon completion.
     *
     * @param initListener the listener to notify when the startIabService process completes
     */
    public void startIabService(IIabCallback.OnInitListener initListener) throws GBException {
        //if (mClient == null || !mClient.isInitialized()) {
        if (getClient() == null) {
            throw new GBException("Platform not initialized!!!");
        }

        if (initListener != null)
            initListener.success(new IabResult(IabResult.BILLING_RESPONSE_RESULT_OK, "", contextRef.get()));
    }

    /**
     * Stops in-app billing service and notifies the given listener upon completion.
     *
     * @param initListener the listener to notify when the stopIabService process completes
     */
    public void stopIabService(IIabCallback.OnInitListener initListener) throws GBException {
        throw new GBException("Not Supported");
    }

    /**
     *
     * @param moreSkus
     * @param queryInventoryListener
     */
    public void queryInventoryAsync(List<String> moreSkus, IIabCallback.OnQueryInventoryListener queryInventoryListener) throws GBException {
        throw new GBException("Not Support");
    }


    /**
     *
     * @param activity
     * @param item
     * @param purChasesListener
     * @param extraData
     */
    public void launchPurchaseFlow(Activity activity, String userKey, GBInAppItem item, IIabCallback.OnPurchaseListener purChasesListener, String extraData) throws GBException {

    }

    /**
     *
     * @param activity
     * @param item
     * @param purChasesListener
     * @param extraData
     */
    public void launchPurchaseFlow(Activity activity, String userKey, GBInAppItem item, GBInAppListener.OnPurchaseFinishedListener purChasesListener, String extraData) throws GBException {

    }


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

    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) throws GBException {
        throw new GBException("Not Support");
    }

    /**
     *
     * @param skus
     * @param consumeListener
     */
    public void consume(List<IabPurchase> skus, IIabCallback.OnConsumeListener consumeListener) throws GBException {
        throw new GBException("Not Support");
    }

}
