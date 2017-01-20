package com.gebros.platform.auth.ui;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public enum GBProfileViewType {
    GBProfileUserInfo,
    GBProfileSettings,
    GBProfileSupportCenter,
    GBProfileEULA;

    public static GBProfileViewType valueOf (int viewType) {

        switch (viewType) {
            case 1 :
                return GBProfileViewType.GBProfileUserInfo;
            case 2 :
            case 3 :
            case 4 :
                return GBProfileViewType.GBProfileSettings;
            case 5 :
                return GBProfileViewType.GBProfileSupportCenter;
            case 6 :
                return GBProfileViewType.GBProfileEULA;
            default :
                return GBProfileViewType.GBProfileUserInfo;
        }
    }
}
