package com.gebros.platform.unity;

import android.widget.Toast;

import com.joycity.platform.sdk.auth.ui.view.AsyncErrorDialog;
import com.joycity.platform.sdk.util.JoypleDeviceUtils;

/**
 * Created by Joycity-Platform on 7/8/16.
 */
public class ApplicationPlugin extends BasePlugin {
    private static final class ApplicationPluginHolder {
        public static final ApplicationPlugin instance = new ApplicationPlugin();
    }

    public static ApplicationPlugin getInstance() {
        return ApplicationPluginHolder.instance;
    }

    public static void ShowToast(String message) {
        ApplicationPlugin.getInstance().showDialog(message);
    }

    public static void ShowAlert() {
        ApplicationPlugin.getInstance().showAsyncErrorDialog();
    }

    public static String getMCC() {
        return JoypleDeviceUtils.getMcc();
    }

    public static String getCurrentLanguage() {
        return JoypleDeviceUtils.getLanguage();
    }

    public static String getDeviceId() {
        return JoypleDeviceUtils.getDeviceId();
    }

    public static String getDeviceModel() {
        return JoypleDeviceUtils.DEVICE_MODEL;
    }




    private void showDialog(final String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showAsyncErrorDialog() {

        AsyncErrorDialog dialog = new AsyncErrorDialog(getActivity());
        dialog.show();
    }
}
