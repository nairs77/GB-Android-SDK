package com.gebros.platform.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.joycity.platform.sdk.internal.JR;

import org.json.JSONObject;

/**
 * Created by nairs77@joycity.com on 5/24/16.
 */
public class GBMessageUtils {

//	private static final String JOYPLE_ALERT_TITLE = "joyple_alert_title";

    public static void alert(Activity activity, int resourceId) {

        new AlertDialog.Builder(activity)
//			.setTitle(JR.string(JOYPLE_ALERT_TITLE))
                .setMessage("" + activity.getString(resourceId))
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false)
                .create().show();
    }

    public static void alert(Activity activity, String message) {

        new AlertDialog.Builder(activity)
//			.setTitle(JR.string(JOYPLE_ALERT_TITLE))
                .setMessage("" + message)
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false)
                .create().show();
    }

    public static void alert(Activity activity, JSONObject json) {

        new AlertDialog.Builder(activity)
//			.setTitle(JR.string(JOYPLE_ALERT_TITLE))
                .setMessage("" + json.toString())
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false)
                .create().show();
    }

    public static void alert(Activity activity, int resourceId, final DialogInterface.OnClickListener listener) {

        new AlertDialog.Builder(activity)
//			.setTitle(JR.string(JOYPLE_ALERT_TITLE))
                .setMessage("" + activity.getString(resourceId))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClick(dialog, which);
                    }
                })
                .create().show();
    }

    public static void alert(Activity activity, String message, final DialogInterface.OnClickListener listener) {

        new AlertDialog.Builder(activity)
//			.setTitle(JR.string(JOYPLE_ALERT_TITLE))
                .setMessage("" + message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClick(dialog, which);
                    }
                })
                .create().show();
    }

    public static void alertAvailableCancel(Activity activity, int resourceId, final DialogInterface.OnClickListener listener) {

        new AlertDialog.Builder(activity)
//			.setTitle(JR.string(JOYPLE_ALERT_TITLE))
                .setMessage("" + activity.getString(resourceId))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClick(dialog, which);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .create().show();
    }

    public static void alertBadResponse(Activity activity) {
        alert(activity, JR.string("joyple_alert_server_status"));
    }

    public static void toast(Context context, int resourceId) {
        Toast.makeText(context, resourceId, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, JSONObject json) {
        Toast.makeText(context, json.toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * This toast method be used by action for message to debug
     */

    public static void toastForDebug(Context context, String message) {

        toast(context, message);
    }

    public static void toastForDebug(Context context, JSONObject json) {

        toast(context, json);
    }
}


