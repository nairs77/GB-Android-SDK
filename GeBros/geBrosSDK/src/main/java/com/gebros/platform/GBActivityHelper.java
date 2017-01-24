package com.gebros.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by nairs77 on 2017. 1. 17..
 */

public class GBActivityHelper {

    private static final String TAG = GBActivityHelper.class.getCanonicalName();

    public static void onActivityCreate(final Activity activity, Bundle savedInstanceState) {
        GBSdk.Initialize(activity);
        GBSdk.instance.onActivityCreate(activity, savedInstanceState);

        // Session Tracking
    }

    public static void onActivityStart(final Activity activity) {
        GBSdk.instance.onActivityStart(activity);
    }

    public static void onActivityStop(final Activity activity) {
        GBSdk.instance.onActivityStop(activity);
    }

    public static void onActivityRestart(final Activity activity) {
        GBSdk.instance.onActivityRestart(activity);
    }

    public static void onActivityPause(final Activity activity) {
        GBSdk.instance.onActivityPause(activity);
    }

    public static void onActivityResume(final Activity activity) {
        GBSdk.instance.onActivityResume(activity);
    }

    public static void onActivityDestroy(final Activity activity) {
        GBSdk.instance.onActivityDestroy(activity);
    }

    public static void onActivityNewIntent(final Activity activity, final Intent intent) {
        GBSdk.instance.onActivityNewIntent(activity, intent);
    }

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        GBSdk.instance.onActivityResult(activity, requestCode, resultCode, intent);
    }
}
