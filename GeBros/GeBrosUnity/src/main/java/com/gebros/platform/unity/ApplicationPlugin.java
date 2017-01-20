package com.gebros.platform.unity;

import android.widget.Toast;

import com.gebros.platform.auth.ui.view.AsyncErrorDialog;
import com.gebros.platform.util.GBDeviceUtils;

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
        return GBDeviceUtils.getMcc();
    }

    public static String getCurrentLanguage() {
        return GBDeviceUtils.getLanguage();
    }

    public static String getDeviceId() {
        return GBDeviceUtils.getDeviceId();
    }

    public static String getDeviceModel() {
        return GBDeviceUtils.DEVICE_MODEL;
    }




    private void showDialog(final String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showAsyncErrorDialog() {

        AsyncErrorDialog dialog = new AsyncErrorDialog(getActivity());
        dialog.show();
    }
}
