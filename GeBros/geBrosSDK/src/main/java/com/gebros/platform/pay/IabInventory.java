package com.gebros.platform.pay;

/**
 * Created by gebros.nairs77@gmail.com on 5/20/16.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a block of information about in-app items.
 * An IabInventory is returned by such methods as {@link queryInventory}.
 */
public class IabInventory {
    private Map<String,IabSkuDetails> mSkuMap = new HashMap<String,IabSkuDetails>();
    private Map<String,IabPurchase> mPurchaseMap = new HashMap<String,IabPurchase>();

    public IabInventory() { }

    /** Returns the listing details for an in-app product. */
    public IabSkuDetails getSkuDetails(String sku) {
        return mSkuMap.get(sku);
    }

    /** Returns purchase information for a given product, or null if there is no purchase. */
    public IabPurchase getPurchase(String sku) {
        return mPurchaseMap.get(sku);
    }

    /** Returns whether or not there exists a purchase of the given product. */
    public boolean hasPurchase(String sku) {
        return mPurchaseMap.containsKey(sku);
    }

    /** Return whether or not details about the given product are available. */
    public boolean hasDetails(String sku) {
        return mSkuMap.containsKey(sku);
    }

    /**
     * Erase a purchase (locally) from the inventory, given its product ID. This just
     * modifies the IabInventory object locally and has no effect on the server! This is
     * useful when you have an existing IabInventory object which you know to be up to date,
     * and you have just consumed an item successfully, which means that erasing its
     * purchase data from the IabInventory you already have is quicker than querying for
     * a new IabInventory.
     */
    public void erasePurchase(String sku) {
        if (mPurchaseMap.containsKey(sku)) mPurchaseMap.remove(sku);
    }

    /** Returns a list of all owned product IDs. */
    public List<String> getAllOwnedSkus() {
        return new ArrayList<String>(mPurchaseMap.keySet());
    }

    /** Returns a list of all owned product IDs of a given type */
    public List<String> getAllOwnedSkus(String itemType) {
        List<String> result = new ArrayList<String>();
        for (IabPurchase p : mPurchaseMap.values()) {
            if (p.getItemType().equals(itemType)) result.add(p.getSku());
        }
        return result;
    }

    /**
     * Returns a list of all past-queried product IDs (sku)
     * in the current session
     * @param excludeOwned - if true, filters ids also present in
     *                     purchases
     * @return list of skus
     */
    public List<String> getAllQueriedSkus(boolean excludeOwned) {
        List<String> result = new ArrayList<String>();
        for (IabSkuDetails p : mSkuMap.values()) {
            if (! (excludeOwned && mPurchaseMap.containsKey(p.getSku())) )
                result.add(p.getSku());
        }
        return result;
    }

    /** Returns a list of all purchases. */
    public List<IabPurchase> getAllPurchases() {
        return new ArrayList<IabPurchase>(mPurchaseMap.values());
    }

    public void addSkuDetails(IabSkuDetails d) {
        mSkuMap.put(d.getSku(), d);
    }

    public void addPurchase(IabPurchase p) {
        mPurchaseMap.put(p.getSku(), p);
    }
}
