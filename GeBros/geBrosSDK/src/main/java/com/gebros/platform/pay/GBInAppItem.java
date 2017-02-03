package com.gebros.platform.pay;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gebros.nairs77@gmail.com on 5/17/16.
 */
public class GBInAppItem {

    public static final String INAPP_ITEM_PRICE = "product_price";
    public static final String INAPP_ITEM_ID = "product_id";
    public static final String INAPP_ITEM_NAME = "product_name";
    public static final String INAPP_ITEM_TYPE = "type";
    public static final String INAPP_ITEM_MONEY_TYPE = "money_type";

    private String mPrice;
    private String mSku;
    private String mName;
    private String mType;

    public GBInAppItem(String price, String sku, String name, String type) {
        mPrice = price;
        mSku = sku;
        mName = name;
        mType = type;
    }

    public static GBInAppItem createFromJSONObject(JSONObject jsonObject) throws JSONException {

        String price = jsonObject.getString(INAPP_ITEM_PRICE);
        String sku = jsonObject.getString(INAPP_ITEM_ID);
        String name = jsonObject.getString(INAPP_ITEM_NAME);
        String type = jsonObject.getString(INAPP_ITEM_TYPE);

        return new GBInAppItem(price, sku, name, type);
    }

    public String getPrice() {
        return mPrice;
    }

    public String getSku() {
        return mSku;
    }

    public String getItemName() {
        return mName;
    }

    public String getItemType() {
        return mType;
    }

    public String toJSONString() {
        try {
            JSONObject object = new JSONObject();
            object.put(INAPP_ITEM_NAME, mName);
            object.put(INAPP_ITEM_PRICE, mPrice);
            object.put(INAPP_ITEM_ID, mSku);
            object.put(INAPP_ITEM_TYPE, mType);
            // requestPaymentIabToken 호출 때 서버측으로 extraData 값으로 "money_type" 을 보내줘야 해서 따로 추가하게 됨
            object.put(INAPP_ITEM_MONEY_TYPE, 4);

            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
