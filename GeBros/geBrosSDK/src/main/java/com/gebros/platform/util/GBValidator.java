package com.gebros.platform.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.gebros.platform.GBSdk;
import com.gebros.platform.log.GBLog;

import java.util.List;
import java.util.Map;

/**
 * Created by nairs77@joycity.com on 5/2/16.
 */

// Thank you for Facebook. we com.facebook.internal/Validate.java

public final class GBValidator {

    private static final String NO_INTERNET_PERMISSION_REASON =
            "No internet permissions granted for the app, please add " +
                    "<uses-permission android:name=\"android.permission.INTERNET\" /> " +
                    "to your AndroidManifest.xml.";

    public static void notNull(Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException("Argument '" + name + "' cannot be null");
        }
    }

    public static boolean stringsEqualOrEmpty(String a, String b) {
        boolean aEmpty = TextUtils.isEmpty(a);
        boolean bEmpty = TextUtils.isEmpty(b);

        if (aEmpty && bEmpty) {
            // Both null or empty, they match.
            return true;
        }
        if (!aEmpty && !bEmpty) {
            // Both non-empty, check equality.
            return a.equals(b);
        }
        // One empty, one non-empty, can't match.
        return false;
    }

    public static <T> boolean areObjectsEqual(T a, T b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    public static boolean isNullOrEmpty(Object s) {
        if (s == null)
            return true;

        if ((s instanceof String) &&
                (((String) s).equals("null") ||
                (s == null) ||
                ((String) s).length() == 0))
            return true;

        if ((s instanceof String) && (((String) s).trim().length() == 0))
            return true;

        if (s instanceof Map)
            return ((Map<?, ?>) s).isEmpty();

        if (s instanceof List)
            return ((List<?>) s).isEmpty();

        if (s instanceof Object[])
            return (((Object[]) s).length == 0);

        return false;
    }

    public static void sdkInitialized() {
//        if (!GBSdk.Initialized()) {
//            // TODO : throw Exception
//        }
    }

    public static boolean VaidateApp() {
        // Check ClientSecretKey, GameCode

        if (false) {
            throw new IllegalStateException("No AppKey(ClientSecretKey) or GameCode found, please set the AppID / GameCode.");
        }

        return true;
    }


    public static boolean isValidPermission(Context context, String permissionName) {
        return GBValidator.isValidPermission(context, permissionName, true);
    }

    public static boolean isValidPermission(Context context, String permissionName, boolean shouldThrow) {
        GBValidator.notNull(context, "context");

        if (context.checkCallingOrSelfPermission(permissionName) ==
                PackageManager.PERMISSION_DENIED) {
            if (shouldThrow) {
                /* TODO : App crash....*/
                throw new IllegalStateException(permissionName);
            } else {
                GBLog.d("[permission = %s]", permissionName);
            }

            return false;
        }

        return true;
    }


}
