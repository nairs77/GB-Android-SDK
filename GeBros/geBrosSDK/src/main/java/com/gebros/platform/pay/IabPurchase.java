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

package com.gebros.platform.pay;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app billing purchase.
 */
public class IabPurchase {
    private String mItemType;  // ITEM_TYPE_INAPP or ITEM_TYPE_SUBS
    private String mOrderId;
    private String mPackageName;
    private String mSku;	//productId
    private long mPurchaseTime;
    private int mPurchaseState;
    private String mDeveloperPayload;
    private String mToken;
    private String mOriginalJson;
    private String mSignature;
    private String receipt;
    private String mPaymentKey;
    private int mMoneyType;
    private String mPrice;
    
    public IabPurchase(){}

    //- Google
    public IabPurchase(String itemType, String jsonPurchaseInfo, String signature) throws JSONException {
        mItemType = itemType;
        mOriginalJson = jsonPurchaseInfo;
        this.receipt = "";
        JSONObject o = new JSONObject(mOriginalJson);
        mOrderId = o.optString("orderId");
        mPackageName = o.optString("packageName");
        mSku = o.optString("productId");
        mPurchaseTime = o.optLong("purchaseTime");
        mPurchaseState = o.optInt("purchaseState");
        mDeveloperPayload = o.optString("developerPayload");
        mPaymentKey = mDeveloperPayload.split("#")[0];
        mToken = o.optString("token", o.optString("purchaseToken"));
        mSignature = signature;
    }
    
    public IabPurchase(String jsonPurchaseInfo, String extraValue) throws JSONException {
    	mOriginalJson = jsonPurchaseInfo;
    	
    	JSONObject o = new JSONObject(mOriginalJson);
    	this.receipt = o.optString("receipt");
    	mToken = o.optString("nonce");
    	
    	JSONObject innerReceipt = new JSONObject(this.receipt);
    	mSku = innerReceipt.optString("productCode");
    	mOrderId = innerReceipt.optString("paymentSeq");
    	mDeveloperPayload = innerReceipt.optString("extra");
    	mPaymentKey = mDeveloperPayload;
    	mSignature = extraValue;
    }
    
    public IabPurchase(String productId, String orderId, String receiptNum, String receipt, String extraData) {
    	mSku = productId;
    	mOrderId = orderId;
    	mToken = receiptNum;
    	this.receipt = receipt;
    	mDeveloperPayload = extraData;
    	mPaymentKey = extraData;
    }

    // 2015. 05. 11  (for Promo code)
    public void setPaymentKey(String aPayload) {
    	mPaymentKey = aPayload;
    }
    
    public void setCustomOrderId(String orderId) {
    	mOrderId = orderId;
    }

    public void setMoneyType(int type) { mMoneyType = type; }

    public void setPrices(String price) { mPrice = price; }

    public int getMoenyType() { return mMoneyType; }

    public String getPrice() { return mPrice; }

    public String getItemType() {
        return mItemType;
    }

    public String getOrderId() {
        return mOrderId;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getSku() {
        return mSku;
    }

    public long getPurchaseTime() {
        return mPurchaseTime;
    }

    public int getPurchaseState() {
        return mPurchaseState;
    }

    public String getDeveloperPayload() {
        return mDeveloperPayload;
    }

    public String getToken() {
        return mToken;
    }

    public String getOriginalJson() {
        return mOriginalJson;
    }

    public String getSignature() {
        return mSignature;
    }
    
	public String getReceipt() {
		return receipt;
	}
	
	public String getPaymentKey() {
    	return mPaymentKey;
    }

	@Override
    public String toString() { return "PurchaseInfo(type:" + mItemType + "):" + mOriginalJson; }
}
