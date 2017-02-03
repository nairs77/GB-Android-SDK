package com.gebros.platform.pay;

/**
 * Created by gebros.nairs77@gmail.com on 5/20/16.
 */
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app product's listing details.
 */
public class IabSkuDetails {
    private String mItemType;
    private String mSku;
    private String mType;
    private String mPrice;
    private String mTitle;
    private String mDescription;
    private String mJson;

    private int mValidity;

//    public IabSkuDetails(String jsonSkuDetails) throws JSONException {
//        this(IabHelper.ITEM_TYPE_INAPP, jsonSkuDetails);
//    }

    public IabSkuDetails(String itemType, String sku, String title, String type, String price, int validity) {
        mItemType = itemType;
        mSku = sku;
        mTitle = title;
        mType = type;
        mPrice = price;
        mValidity = validity;
    }

    public IabSkuDetails(String itemType, String jsonSkuDetails) throws JSONException {
        mItemType = itemType;
        mJson = jsonSkuDetails;
        JSONObject o = new JSONObject(mJson);
        mSku = o.optString("productId");
        mType = o.optString("type");
        mPrice = o.optString("price");
        mTitle = o.optString("title");
        mDescription = o.optString("description");
    }

    public String getItemType() {
        return mItemType;
    }

    public String getSku() {
        return mSku;
    }

    public String getType() {
        return mType;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getJson() {
        return mJson;
    }

    public int getmValidity() {
        return mValidity;
    }

    @Override
    public String toString() {
        return "IabSkuDetails:" + mJson;
    }
}
