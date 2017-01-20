package com.gebros.platform.unity;

import com.gebros.platform.GBSdk;
import com.gebros.platform.event.JoypleEvent;
import com.gebros.platform.event.JoypleEventReceiver;
import com.gebros.platform.exception.JoypleRuntimeException;
import com.gebros.platform.listener.JoypleInitListener;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.platform.Platform;
import com.gebros.platform.platform.PlatformType;
import com.gebros.platform.util.GBValidator;

import org.json.JSONException;
import org.json.JSONObject;

//import com.gebros.platform.permission.JoyplePermissionHelper;

/**
 * Created by nairs77@joycity.com on 6/15/16.
 */
public class GBUnityPlugin extends BasePlugin {

    private static final String TAG = GBUnityPlugin.class.getCanonicalName();

    public static void setActiveMarket(String platformInfo, final String gameObjectName) {
        JSONObject platform = null;
        try {
            platform = new JSONObject(platformInfo);
        } catch (JSONException e) {
            throw new JoypleRuntimeException("Configure information is empty!!!");
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

        Joyple.SetActiveMarket(getActivity(), builder.build(), new JoypleInitListener() {
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
    /**
     *
     * @param clientSecretKey
     * @param gameCode
     * @param marketCode
     * @param logLevel
     */
    public static void configureWithGameInfo(String clientSecretKey, int gameCode, int marketCode, int logLevel) {
        if(GBValidator.isNullOrEmpty(clientSecretKey))
            throw new JoypleRuntimeException("Configure information is empty!!!");

        Platform.Builder builder = new Platform.Builder("", "");

        if (marketCode == PlatformType.Market.GOOGLE.getMarketCode())
            builder.PlatformType(PlatformType.DEFAULT);
        else if (marketCode == PlatformType.Market.MYCARD.getMarketCode()) {
            builder.PlatformType(PlatformType.JOYPLE_MYCARD);
        }

        Joyple.ConfigureSDKInfo(getActivity(), builder.build(), clientSecretKey, gameCode, Joyple.LogLevel.fromInt(logLevel));
    }

    /**
     *
     * @param clientSecretKey
     * @param gameCode
     * @param platformInfo
     * @param logLevel
     */
    public static void configureWithGameInfo(String clientSecretKey, int gameCode, String platformInfo, int logLevel) {
        if(GBValidator.isNullOrEmpty(clientSecretKey) || GBValidator.isNullOrEmpty(platformInfo))
            throw new JoypleRuntimeException("Configure information is empty!!!");

        JSONObject platform = null;
        try {
            platform = new JSONObject(platformInfo);
        } catch (JSONException e) {
            throw new JoypleRuntimeException("Configure information is empty!!!");
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

        Joyple.ConfigureSDKInfo(getActivity(), builder.build(), clientSecretKey, gameCode, Joyple.LogLevel.values()[logLevel]);
    }

    public static void requestGlobalServerInfo(String branchURL, int gameCode, final String gameObjectName) {

        Joyple.RequestGlobalServerInfo(branchURL, gameCode, new JoypleEventReceiver() {

            @Override
            public void onSuccessEvent(JoypleEvent event, JSONObject json) {
                GBLog.d(TAG + "onSuccessEvent =%s, response = %s", event.name(), json.toString());
                SendUnityMessage(gameObjectName, ASYNC_RESULT_SUCCESS, json.toString());
            }

            @Override
            public void onFailedEvent(JoypleEvent event, int errorCode, String errorMessage) {
                GBLog.d(TAG + "onFailedEvent =%s, code = %d, response = %s", event.name(), errorCode, errorMessage);

                String errorResponse = MakeErrorResponse(errorCode, errorMessage);
                SendUnityMessage(gameObjectName, ASYNC_RESULT_SUCCESS, errorResponse);
            }
        });
    }

    public static boolean CheckRuntimePermission(String permission) {
        //return JoyplePermissionHelper.hasPermission(getActivity(), permission);
        return true;
    }

    public static void AskForPermission(String permission, String gameObjectName) {

    }

}
