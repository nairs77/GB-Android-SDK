package com.gebros.platform.pay;

import java.util.List;

/**
 * Created by gebros.nairs77@gmail.com on 5/20/16.
 */

public interface IIabCallback {

    /**
     * Listens for in-app billing service initialization
     */
    public interface OnInitListener {

        /**
         *
         * @param result
         */
        public void success(IabResult result);

        /**
         *
         * @param result
         */
        public void fail(IabResult result);
    }

    /**
     * Listens for in-app purchases being made
     */
    public interface OnPurchaseListener {

        /**
         * The user has successfully completed a purchase of the desired product Id.
         *
         * @param purchase
         */
        public void success(IabPurchase purchase);

        /**
         *
         * @param purchase
         */
        public void cancelled(IabPurchase purchase);

        /**
         * The user has successfully completed a purchase of an item that he already owns.
         *
         * @param purchase
         */
        public void alreadyOwned(IabPurchase purchase);

        /**
         *
         * @param result
         */
        public void fail(IabResult result);
    }

    /**
     * Listens for inventory queries
     */
    public interface OnQueryInventoryListener {
        /**
         *
         * @param inventory (Purchases Container)
         * @param skuDetails
         */
        //public void success(List<IabPurchase> purchases, List<IabSkuDetails> skuDetails);
        public void success(IabInventory inventory);

        /**
         *
         * @param message
         */
        public void fail(IabResult result);
    }


    /**
     * Listens for consumptions of purchases
     */
    public interface OnConsumeListener {

        /**
         *
         * @param puchases
         * @param results
         */

        public void OnConsumeFinishedListener(List<IabPurchase> purchaseList, List<IabResult> results);
    }
}

