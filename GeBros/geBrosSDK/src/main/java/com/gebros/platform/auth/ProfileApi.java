package com.gebros.platform.auth;


import com.gebros.platform.auth.model.GBDevice;
import com.gebros.platform.auth.model.GBProfile;
import com.gebros.platform.auth.model.GBService;
import com.gebros.platform.auth.model.GBUser;
import com.gebros.platform.auth.model.local.GBData;
import com.gebros.platform.listener.GBProfileListener;
import com.gebros.platform.util.GBValidator;

import java.util.List;

/**
 * Created by nairs77@joycity.com on 5/19/16.
 */
public class ProfileApi {

    /**
     * Request User Information
     *
     * @param listener  {@link GBProfileListener}
     */

    public static void RequestProfile(GBProfileListener listener) {
        GBAuthManager.getAuthIml().requestProfile(listener);
    }

    public static GBUser getLocalUser() {

        if (GBValidator.isNullOrEmpty(getProfile()))
            return null;

        return getProfile().getUserInfo();
    }

    public static List<GBService> getService() {
        if (!GBValidator.isNullOrEmpty(getProfile()))
            return null;

        return getProfile().getServices();
    }

    public static List<GBDevice> getDevice() {
        if (!GBValidator.isNullOrEmpty(getProfile()))
            return null;

        return getProfile().getDevices();
    }

    private static GBProfile getProfile() {
        return GBData.getInstance().getProfile();
    }

}
