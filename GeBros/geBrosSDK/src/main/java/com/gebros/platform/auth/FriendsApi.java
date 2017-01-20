package com.gebros.platform.auth;

import com.gebros.platform.auth.model.GBFriends;
import com.gebros.platform.auth.model.GBFriendsInfo;
import com.gebros.platform.auth.model.local.GBData;
import com.gebros.platform.listener.GBFriendsListener;

import java.util.List;

/**
 * Created by nairs77@joycity.com on 6/22/16.
 */
public class FriendsApi {

    public static void RequestFriends(GBFriendsListener listener) {
        GBAuthManager.getAuthIml().requestFriends(listener);
    }

    public static void AddFriend(String friendUserKey, final GBFriendsListener listener) {
        GBAuthManager.getAuthIml().requestAddFriends(friendUserKey, listener);
    }

    public static void UpdateFriendStatus(String friendUserKey, int status, final GBFriendsListener listener) {
        GBAuthManager.getAuthIml().requestUpdateFriendsStatus(friendUserKey, status, listener);
    }

    public static void InvitedUserCount(GBFriendsListener listener) {
        GBAuthManager.getAuthIml().requestInvitedUserCount(listener);
    }


    public static GBFriends getFriends() {
        return GBData.getInstance().getFriends();
    }

    public static List<GBFriendsInfo> getAllFriends() {
        return getFriends().getAllFriends();
    }

    public static List<GBFriendsInfo> getGameFriends() {
        return getFriends().getGameFriends();
    }

    public static List<GBFriendsInfo> getRecommendUsers() {
        return getFriends().getRecommendUsers();
    }

    public static List<GBFriendsInfo> getSearchedUsers() {
        return GBData.getInstance().getSearchedUsers();
    }

/*

    public static void requestProfile(final GBEventReceiver receiver) {

        Request request = GBAPI.getAbstractRequest(Method.GET, GBSession.getActiveSession(), GBAPI.PROFILE_URI,
                new ObjectCallback<GBProfile>() {

                    @Override
                    public void onComplete(GBProfile joypleObject, Response response) {

                        receiver.onSuccessEvent(GBEvent.PROFILE, joypleObject.getInnerJSONObject());

                        GB.getInstance().isUpdatedAdvertisingId(new JoycityAsyncResultListener(){

                            @Override
                            public void onReceiveEvent(JoycityAsyncResultEvent event) {
                                if (event.equals(JoycityAsyncResultEvent.UPDATED_ADVERTISING_INFO_YES)) {
                                    GB.getInstance().collectAdvertisingId();
                                }
                            }

                        });

                        if (GB.isIncludeGBPush()) {
                            JoycityNotificationService.getInstance().requestRegisterPushToken(ProfileApi.getLocalUser().getUserKey(), new JoycityEventReceiver() {

                                @Override
                                public void onSuccessEvent(JoycityEvent event, final JSONObject json) {
                                    Logger.d("ProfileApi [requestProfile] requestRegisterPushToken onSuccessEvent event= "+event.name());
                                }

                                @Override
                                public void onFailedEvent(final JoycityEvent event, final int errorCode, final String errorMessage) {
                                    Logger.d("ProfileApi [requestProfile] requestRegisterPushToken onFailedEvent event = "+event.name());
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Response response) {

                        GBAPIError apiError = response.getAPIError();
                        int errorCode = apiError.getErrorCode();
                        String errorType = apiError.getErrorType();

                        receiver.onFailedEvent(GBEvent.PROFILE_FAILED, errorCode, errorType, 0);
                    }

                }, GBProfile.class);

        request.appendParam("scope", "user_info,services,devices,games");
        if(!GBLogger.isTestMode())
            request.appendParam("udid", DeviceUtilsManager.getInstance().getDeviceId());

        GBAPI.requestAPI(request);
    }

    public static void requestProfileUserInfo(final GBEventReceiver receiver) {

        Request request = GBAPI.getAbstractRequest(Method.GET, GBSession.getActiveSession(), GBAPI.PROFILE_URI,
                new ObjectCallback<GBProfile>() {

                    @Override
                    public void onComplete(GBProfile joypleObject, Response response) {

                        receiver.onSuccessEvent(GBEvent.PROFILE, joypleObject.getInnerJSONObject());
                        if (GB.isIncludeGBPush()) {
                            JoycityNotificationService.getInstance().requestRegisterPushToken(ProfileApi.getLocalUser().getUserKey(), new JoycityEventReceiver() {

                                @Override
                                public void onSuccessEvent(JoycityEvent event, final JSONObject json) {
                                    Logger.d("ProfileApi [requestProfile] requestRegisterPushToken onSuccessEvent event= "+event.name());
                                }

                                @Override
                                public void onFailedEvent(final JoycityEvent event, final int errorCode, final String errorMessage) {
                                    Logger.d("ProfileApi [requestProfile] requestRegisterPushToken onFailedEvent event = "+event.name());
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Response response) {

                        GBAPIError apiError = response.getAPIError();
                        int errorCode = apiError.getErrorCode();
                        String errorType = apiError.getErrorType();

                        receiver.onFailedEvent(GBEvent.PROFILE_FAILED, errorCode, errorType, 0);
                    }

                }, GBProfile.class);

        request.appendParam("scope", "user_info");
        if(!GBLogger.isTestMode())
            request.appendParam("udid", DeviceUtilsManager.getInstance().getDeviceId());

        GBAPI.requestAPI(request);
    }

    public static void requestProfileServices(final GBEventReceiver receiver) {

        Request request = GBAPI.getAbstractRequest(Method.GET, GBSession.getActiveSession(), GBAPI.PROFILE_URI,
                new ObjectCallback<GBProfile>() {

                    @Override
                    public void onComplete(GBProfile joypleObject, Response response) {

                        receiver.onSuccessEvent(GBEvent.PROFILE, joypleObject.getInnerJSONObject());
                    }

                    @Override
                    public void onError(Response response) {

                        GBAPIError apiError = response.getAPIError();
                        int errorCode = apiError.getErrorCode();
                        String errorType = apiError.getErrorType();

                        receiver.onFailedEvent(GBEvent.PROFILE_FAILED, errorCode, errorType, 0);
                    }

                }, GBProfile.class);

        request.appendParam("scope", "services");
        if(!GBLogger.isTestMode())
            request.appendParam("udid", DeviceUtilsManager.getInstance().getDeviceId());

        GBAPI.requestAPI(request);
    }

    public static void requestProfileDevices(final GBEventReceiver receiver) {

        Request request = GBAPI.getAbstractRequest(Method.GET, GBSession.getActiveSession(), GBAPI.PROFILE_URI,
                new ObjectCallback<GBProfile>() {

                    @Override
                    public void onComplete(GBProfile joypleObject, Response response) {

                        receiver.onSuccessEvent(GBEvent.PROFILE, joypleObject.getInnerJSONObject());
                    }

                    @Override
                    public void onError(Response response) {

                        GBAPIError apiError = response.getAPIError();
                        int errorCode = apiError.getErrorCode();
                        String errorType = apiError.getErrorType();

                        receiver.onFailedEvent(GBEvent.PROFILE_FAILED, errorCode, errorType, 0);
                    }

                }, GBProfile.class);

        request.appendParam("scope", "devices");
        if(!GBLogger.isTestMode())
            request.appendParam("udid", DeviceUtilsManager.getInstance().getDeviceId());

        GBAPI.requestAPI(request);
    }

    public static void requestProfileGames(final GBEventReceiver receiver) {

        Request request = GBAPI.getAbstractRequest(Method.GET, GBSession.getActiveSession(), GBAPI.PROFILE_URI,
                new ObjectCallback<GBProfile>() {

                    @Override
                    public void onComplete(GBProfile joypleObject, Response response) {

                        receiver.onSuccessEvent(GBEvent.PROFILE, joypleObject.getInnerJSONObject());
                    }

                    @Override
                    public void onError(Response response) {

                        GBAPIError apiError = response.getAPIError();
                        int errorCode = apiError.getErrorCode();
                        String errorType = apiError.getErrorType();

                        receiver.onFailedEvent(GBEvent.PROFILE_FAILED, errorCode, errorType, 0);
                    }

                }, GBProfile.class);

        request.appendParam("scope", "games");
        if(!GBLogger.isTestMode())
            request.appendParam("udid", DeviceUtilsManager.getInstance().getDeviceId());

        GBAPI.requestAPI(request);
    }

    public static void requestProfileUserInfoGames(final GBEventReceiver receiver) {

        Request request = GBAPI.getAbstractRequest(Method.GET, GBSession.getActiveSession(), GBAPI.PROFILE_URI,
                new ObjectCallback<GBProfile>() {

                    @Override
                    public void onComplete(GBProfile joypleObject, Response response) {

                        receiver.onSuccessEvent(GBEvent.PROFILE, joypleObject.getInnerJSONObject());
                        if (GB.isIncludeGBPush()) {
                            JoycityNotificationService.getInstance().requestRegisterPushToken(ProfileApi.getLocalUser().getUserKey(), new JoycityEventReceiver() {

                                @Override
                                public void onSuccessEvent(JoycityEvent event, final JSONObject json) {
                                    Logger.d("ProfileApi [requestProfile] requestRegisterPushToken onSuccessEvent event= "+event.name());
                                }

                                @Override
                                public void onFailedEvent(final JoycityEvent event, final int errorCode, final String errorMessage) {
                                    Logger.d("ProfileApi [requestProfile] requestRegisterPushToken onFailedEvent event = "+event.name());
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Response response) {

                        GBAPIError apiError = response.getAPIError();
                        int errorCode = apiError.getErrorCode();
                        String errorType = apiError.getErrorType();

                        receiver.onFailedEvent(GBEvent.PROFILE_FAILED, errorCode, errorType, 0);
                    }

                }, GBProfile.class);

        request.appendParam("scope", "user_info,games");
        if(!GBLogger.isTestMode())
            request.appendParam("udid", DeviceUtilsManager.getInstance().getDeviceId());

        GBAPI.requestAPI(request);
    }

    public static void requestProfileUserInfoServices(final GBEventReceiver receiver) {

        Request request = GBAPI.getAbstractRequest(Method.GET, GBSession.getActiveSession(), GBAPI.PROFILE_URI,
                new ObjectCallback<GBProfile>() {

                    @Override
                    public void onComplete(GBProfile joypleObject, Response response) {

                        receiver.onSuccessEvent(GBEvent.PROFILE, joypleObject.getInnerJSONObject());
                        if (GB.isIncludeGBPush()) {
                            JoycityNotificationService.getInstance().requestRegisterPushToken(ProfileApi.getLocalUser().getUserKey(), new JoycityEventReceiver() {

                                @Override
                                public void onSuccessEvent(JoycityEvent event, final JSONObject json) {
                                    Logger.d("ProfileApi [requestProfile] requestRegisterPushToken onSuccessEvent event= "+event.name());
                                }

                                @Override
                                public void onFailedEvent(final JoycityEvent event, final int errorCode, final String errorMessage) {
                                    Logger.d("ProfileApi [requestProfile] requestRegisterPushToken onFailedEvent event = "+event.name());
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Response response) {

                        GBAPIError apiError = response.getAPIError();
                        int errorCode = apiError.getErrorCode();
                        String errorType = apiError.getErrorType();

                        receiver.onFailedEvent(GBEvent.PROFILE_FAILED, errorCode, errorType, 0);
                    }

                }, GBProfile.class);

        request.appendParam("scope", "user_info,services");
        if(!GBLogger.isTestMode())
            request.appendParam("udid", DeviceUtilsManager.getInstance().getDeviceId());

        GBAPI.requestAPI(request);
    }

    public static void requestFindPassword(String account, final GBEventReceiver receiver) {
        GBAppRequest request = new GBAppRequest(JoycityAccounts.FIND_PW_URI);
        request.addParameter("email", account);
        request.setRequestType(RequestType.GUEST);

        request.get(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response) throws JSONException {
                receiver.onSuccessEvent(GBEvent.FIND_PASSWORD, null);
            }

            @Override
            public void onError(Response response) {

                GBAPIError apiError = response.getAPIError();
                int errorCode = apiError.getErrorCode();
                String errorType = apiError.getErrorType();
                int errorMessage = 0;

                if(errorCode == JoycityServerErrorCode.UNREGISTERD_USER) {
                    errorMessage = JR.string("errorui_findpw_notexist_label_title");
                }
                else if(errorCode == JoycityServerErrorCode.BAD_EMAIL) {
                    errorMessage = JR.string("errorui_common_invalidemail_label_title");
                }
                else if(errorCode == JoycityServerErrorCode.QUIT_USER) {
                    errorMessage = JR.string("errorui_findpw_withdraw_label_title");
                }
                else {
                    errorMessage = JR.string("ui_main_default_error");
                }

                receiver.onFailedEvent(GBEvent.FIND_PASSWORD_FAILED, errorCode, errorType, errorMessage);
            }

        });
    }

    public static void requestDuplCheckAccount(String account, final GBEventReceiver receiver) {
        GBAppRequest request = new GBAppRequest(JoycityAccounts.DUPLICATED_CHECK_URI);
        request.addParameter("email", account);
        request.setRequestType(RequestType.API);

        request.get(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response) throws JSONException {
                receiver.onSuccessEvent(GBEvent.UNIQUE_ACCOUNT, null);
            }

            @Override
            public void onError(Response response) {

                GBAPIError apiError = response.getAPIError();
                int errorCode = apiError.getErrorCode();
                String errorType = apiError.getErrorType();
                int errorMessage = 0;

                if(errorCode == JoycityServerErrorCode.DUPLICATED_EMAIL) {
                    errorMessage = JR.string("errorui_common_duplemail_label_title");
                } else {
                    errorMessage = JR.string("ui_main_default_error");
                }

                receiver.onFailedEvent(GBEvent.UNIQUE_ACCOUNT_FAILED, errorCode, errorType, errorMessage);
            }

        });
    }

    public static void requestEditAccount(String account, final GBEventReceiver receiver) {
        GBAppRequest request = new GBAppRequest(GBApp.EMAIL_CHANGE_URI);
        request.addParameter("email", account);
        request.setRequestType(RequestType.API);

        request.post(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response) throws JSONException {
                receiver.onSuccessEvent(GBEvent.EDIT_ACCOUNT, null);
            }

            @Override
            public void onError(Response response) {

                GBAPIError apiError = response.getAPIError();
                int errorCode = apiError.getErrorCode();
                String errorType = apiError.getErrorType();
                int errorMessage = 0;

                if(errorCode == JoycityServerErrorCode.DUPLICATED_EMAIL) {
                    errorMessage = JR.string("errorui_common_duplemail_label_title");
                } else {
                    errorMessage = JR.string("ui_main_default_error");
                }

                receiver.onFailedEvent(GBEvent.EDIT_ACCOUNT_FAILED, errorCode, errorType, errorMessage);
            }

        });

    }

    public static void requestEditPassword(String password, final GBEventReceiver receiver) {
        GBAppRequest request = new GBAppRequest(JoycityAccounts.PASSWORD_CHANGE_URI);
        request.addParameter("pw", password);
        request.setRequestType(RequestType.API);

        request.post(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response) throws JSONException {
                receiver.onSuccessEvent(GBEvent.EDIT_PASSWORD, null);
            }

            @Override
            public void onError(Response response) {

                GBAPIError apiError = response.getAPIError();
                int errorCode = apiError.getErrorCode();
                String errorType = apiError.getErrorType();
                int errorMessage = 0;

                if(errorCode == JoycityServerErrorCode.INVALID_PWD_LENGTH) {
                    errorMessage = JR.string("errorui_common_invalidpw_label_title");
                } else {
                    errorMessage = JR.string("ui_main_default_error");
                }

                receiver.onFailedEvent(GBEvent.EDIT_PASSWORD_FAILED, errorCode, errorType, errorMessage);
            }

        });
    }

    public static void requestSendAuthEmail(final GBEventReceiver receiver) {
        GBAppRequest request = new GBAppRequest(JoycityAccounts.AUTH_EMAIL_SEND_URI);
        request.setRequestType(RequestType.API);

        request.get(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response) throws JSONException {
                receiver.onSuccessEvent(GBEvent.SEND_AUTH_EMAIL, null);
            }

            @Override
            public void onError(Response response) {
                GBAPIError apiError = response.getAPIError();
                int errorCode = apiError.getErrorCode();
                String errorType = apiError.getErrorType();
                int errorMessage = 0;

                if(errorCode == JoycityServerErrorCode.BAD_EMAIL) {
                    errorMessage = JR.string("errorui_findpw_notexist_label_title");
                } else {
                    errorMessage = JR.string("ui_main_default_error");
                }

                receiver.onFailedEvent(GBEvent.SEND_AUTH_EMAIL_FAILED, errorCode, errorType, errorMessage);
            }

        });
    }

    public static void requestEnrollAccount(String account, String password, final GBEventReceiver receiver) {
        GBAppRequest request = new GBAppRequest(JoycityAccounts.EMAIL_ADD_URI);
        request.addParameter("email", account);
        request.addParameter("pw", password);
        request.setRequestType(RequestType.API);

        request.post(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response) throws JSONException {
                receiver.onSuccessEvent(GBEvent.ENROLL_ACCOUNT, json);
            }

            @Override
            public void onError(Response response) {
                GBAPIError apiError = response.getAPIError();
                int errorCode = apiError.getErrorCode();
                String errorType = apiError.getErrorType();
                int errorMessage = 0;

                if(errorCode == JoycityServerErrorCode.DUPLICATED_EMAIL) {
                    errorMessage = JR.string("errorui_common_duplemail_label_title");
                } else if(errorCode == JoycityServerErrorCode.INVALID_PWD_LENGTH) {
                    errorMessage = JR.string("errorui_common_invalidpw_label_title");
                } else {
                    errorMessage = JR.string("ui_main_default_error");
                }

                receiver.onFailedEvent(GBEvent.ENROLL_ACCOUNT_FAILED, errorCode, errorType, errorMessage);
            }

        });
    }

    private static GBProfile getProfile() {
        return GBData.getInstance().getProfile();
    }

    public static GBUser getLocalUser() {
        if (ObjectUtils.isEmpty(getProfile())) {
            return null;
        }
        return getProfile().getUserInfo();
    }

    public static List<GBService> getServices() {
        if (ObjectUtils.isEmpty(getProfile())) {
            return null;
        }
        return getProfile().getServices();
    }

    public static List<GBDevice> getDevices() {
        if (ObjectUtils.isEmpty(getProfile())) {
            return null;
        }
        return getProfile().getDevices();
    }

    public static List<GBGame> getGames() {
        if (ObjectUtils.isEmpty(getProfile())) {
            return null;
        }
        return getProfile().getGames();
    }
*/
}
