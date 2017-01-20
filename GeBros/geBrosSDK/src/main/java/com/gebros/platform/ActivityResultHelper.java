package com.gebros.platform;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nairs77@joycity.com on 5/23/16.
 */
public class ActivityResultHelper {

    public interface ActivityResultListener {
        void onActivityResult(int resultCode, Intent intent);
    }

    public static ConcurrentHashMap<Integer, ActivityResultListener> mActivityResultListeners = new ConcurrentHashMap<Integer, ActivityResultListener>();

    public static void listenForActivityResult(int requestCode, ActivityResultListener resultListener)
    {
        mActivityResultListeners.put(requestCode, resultListener);
    }

    public static void stopListeningForActivityResult(int requestCode, ActivityResultListener resultListener) {
        mActivityResultListeners.remove(requestCode);
    }

    //public static void handleOnActivityResult(int requestCode, int resultCode, Intent intent) {
    public static boolean handleOnActivityResult(int requestCode, int resultCode, Intent intent) {
        ActivityResultListener listener = mActivityResultListeners.remove(requestCode);
        if (listener != null) {
            mActivityResultListeners.remove(listener);
            listener.onActivityResult(resultCode, intent);

            return true;
        }

        return false;
    }

    public static void registerActivityResult(int requestCode, ActivityResultListener resultListener) {
        listenForActivityResult(requestCode, resultListener);
    }

    public static void unRegisterActivityResult(int requestCode) {
        mActivityResultListeners.remove(requestCode);
    }

    public static void startActivityForResult(@NonNull Activity parentActivity, int requestCode,
                                              Intent intent, ActivityResultListener resultListener) {
        listenForActivityResult(requestCode, resultListener);
        parentActivity.startActivityForResult(intent, requestCode);
    }
/*
    public static void on() {
        ActivityResultHelper.registerActivityResult(1234, new ActivityResultListener() {
            @Override
            public void onActivityResult(int resultCode, Intent intent) {

            }
        });
    }
*/
}
