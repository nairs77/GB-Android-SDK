package com.gebros.platform.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gebros.platform.pay.GBInAppItem;

/**
 * Created by nairs on 2016-05-04.
 */
public interface IPlatformClient {

    abstract void setMarketInfo(String marketInfo);

    abstract boolean getDebugMode();

    abstract boolean isInitialized();

    abstract PlatformType getType();

    abstract PlatformType.AuthType getAuthType();

    abstract void doPlatformActive(Activity activity, IPlatformListener.OnInitLister listener);
    abstract void doPlatformInit(Activity activity, IPlatformListener.OnInitLister listener);

    abstract void doPlatformLogin(Activity activity, IPlatformListener.OnAuthListener listener);
    abstract void doPlatformLogout(Activity activity, IPlatformListener.OnAuthListener listener);

    //public void queryInventoryItems(ArrayList<String> items, JoypleInAppListener.OnQueryInventoryFinishedListener listener);

    abstract void doPlatformPay(Activity activity, GBInAppItem itemInfo, String cpId, IPlatformListener.OnPayListener listener);

    abstract void doPlatformSubmitExtendData(String extraData, IPlatformListener.OnGameListener listener);

    abstract void doPlatformExit(Activity activity, IPlatformListener.OnGameListener listener);

    abstract void onActivityCreate(Activity activity, Bundle savedInstanceState);

    abstract void onActivityStart(Activity activity);

    abstract void onActivityStop(Activity activity);

    abstract void onActivityRestart(Activity activity);

    abstract void onActivityResume(Activity activity);

    abstract void onActivityPause(Activity activity);

    abstract void onActivityDestroy(Activity activity);

    abstract void onActivityNewIntent(Activity activity, Intent intent);

    abstract void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent);
/*
onCreate, onResume, onPause
 */
}


