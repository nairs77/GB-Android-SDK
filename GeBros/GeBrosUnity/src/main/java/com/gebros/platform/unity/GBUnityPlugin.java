package com.gebros.platform.unity;

import com.gebros.platform.GBSdk;
import com.gebros.platform.exception.GBRuntimeException;

import com.gebros.platform.log.GBLog;
import com.gebros.platform.pay.Market;
import com.gebros.platform.platform.Platform;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBValidator;

//import com.gebros.platform.permission.GBPermissionHelper;

/**
 * Created by gebros.nairs77@gmail.com on 6/15/16.
 */
public class GBUnityPlugin extends BasePlugin {

    private static final String TAG = GBUnityPlugin.class.getCanonicalName();
/*
    public static void setActiveMarket(String platformInfo, final String gameObjectName) {
        JSONObject platform = null;
        try {
            platform = new JSONObject(platformInfo);
        } catch (JSONException e) {
            throw new GBRuntimeException("Configure information is empty!!!");
        }

        GBLog.d(TAG + "platform Info platformInfo = " + platformInfo);
        Platform.Builder builder = null;
        if(PlatformType.valueOf(platform.optInt("platformType")).equals(PlatformType.HUAWEI)) {
            builder = new Platform.Builder(platform.optString("appId"), platform.optString("appKey"))
                    .PlatformType(PlatformType.valueOf(platform.optInt("platformType")))
                    .AppSecret(platform.optString("appSecret"))
                    .CpId(platform.optString("cpId"))
                    .BuoSecret(platform.optString("buoSecret"))
                    .PayId(platform.optString("payId"))
                    .PayRsaPrivate(platform.optString("payRsaPrivate"))
                    .PayRsaPublic(platform.optString("payRsaPublic"));
        } else {
            builder = new Platform.Builder(platform.optString("appId"), platform.optString("appKey"))
                    .PlatformType(PlatformType.valueOf(platform.optInt("platformType")))
                    .AppSecret(platform.optString("appSecret"));
        }

        GB.SetActiveMarket(getActivity(), builder.build(), new GBInitListener() {
            @Override
            public void onSuccess() {
                SendUnityMessage(gameObjectName, ASYNC_RESULT_SUCCESS, "");
            }

            @Override
            public void onFail() {
                SendUnityMessage(gameObjectName, ASYNC_RESULT_FAIL, "");
            }
        });
    }
*/
    /**
     *
     * @param clientSecretKey
     * @param gameCode
     * @param marketCode
     * @param logLevel
     */
    public static void configureWithGameInfo(String clientSecretKey, int gameCode, int marketCode, int logLevel) {
        GBSdk.ConfigureSdkWithInfo(getActivity(), gameCode, clientSecretKey, GBLog.LogLevel.fromInt(logLevel));
    }

    public static boolean CheckRuntimePermission(String permission) {
        //return GBPermissionHelper.hasPermission(getActivity(), permission);
        return true;
    }

    public static void AskForPermission(String permission, String gameObjectName) {

    }

}
