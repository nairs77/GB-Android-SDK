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
        //Joyple.instance.getPlatformClient().onActivityCreate(activity, savedInstanceState);
        Joyple.instance.onActivityCreate(activity, savedInstanceState);
    }

    public static void onActivityStart(final Activity activity) {
        Joyple.instance.onActivityStart(activity);
    }

    public static void onActivityStop(final Activity activity) {
        Joyple.instance.onActivityStop(activity);
    }

    public static void onActivityRestart(final Activity activity) {
        Joyple.instance.onActivityRestart(activity);
    }

    public static void onActivityPause(final Activity activity) {
        Joyple.instance.onActivityPause(activity);
    }

    public static void onActivityResume(final Activity activity) {
        Joyple.instance.onActivityResume(activity);
    }

    public static void onActivityDestroy(final Activity activity) {
        Joyple.instance.onActivityDestroy(activity);
    }

    public static void onActivityNewIntent(final Activity activity, final Intent intent) {
        Joyple.instance.onActivityNewIntent(activity, intent);
    }

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        Joyple.instance.onActivityResult(activity, requestCode, resultCode, intent);
    }
}
