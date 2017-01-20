package com.gebros.platform.auth;


import com.gebros.platform.auth.model.GBToken;
import com.gebros.platform.auth.model.annotation.InnerObject;
import com.gebros.platform.auth.model.common.GBAPIError;
import com.gebros.platform.auth.model.common.GBObject;
import com.gebros.platform.auth.model.local.GBData;
import com.gebros.platform.auth.net.Request;
import com.gebros.platform.auth.net.RequestAsyncTask;
import com.gebros.platform.auth.net.Response;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.log.GBLog;

import java.util.Map;

/**
 * Created by nairs77@joycity.com on 5/13/16.
 */
public class GBRequest {

    private static final String TAG = GBRequest.class.getCanonicalName();

/*
    public static final String FRIENDS_URI = JoycityConfig.getContentsServer() + "/users/friends/list";
    public static final String ADD_FRIENDS_URI = JoycityConfig.getContentsServer() + "/users/friends/add-relation";
    public static final String UPDATE_FRIENDS_STATUS_URI = JoycityConfig.getContentsServer() + "/users/friends/update-type";
    public static final String SEARCH_FRIENDS_URI = JoycityConfig.getContentsServer() + "/users/friends/search";
    public static final String INVITED_USER_COUNT = JoycityConfig.getContentsServer() + "/invitation/count_list";
*/
    private static final int REQUEST_RETRY_MAX_COUNT = 5;
    private static int retryAPICount = 0;

    /**
     * Request for authorization
     */

    public static Request getAuthorizationRequest(String path, Map<String, Object> accountInfo, final ObjectCallback<GBToken> callback) {
        return getAuthorizationRequest(Request.Method.POST, path, accountInfo, callback);
    }

    public static Request getAuthorizationRequest(Request.Method method, String path, Map<String, Object> accountInfo, final ObjectCallback<GBToken> callback) {

        Request request = getAbstractRequest(method, null, path, callback, GBToken.class);
        request.setRequestType(Request.RequestType.AUTHORIZATION);
        request.setParams(accountInfo);
        return request;
    }

    /**
     * Request for Object extends GBObject mapped to @InnerObject Annotation
     */

    public static <T extends GBObject> Request getAbstractRequest(GBSession GBSession, String path, final ObjectCallback<T> callback, final Class<T> GBObjectClass) {
        return getAbstractRequest(Request.Method.POST, GBSession, path, callback, GBObjectClass);
    }

    public static <T extends GBObject> Request getAbstractRequest(Request.Method method, GBSession GBSession, String path, final ObjectCallback<T> callback, final Class<T> GBObjectClass) {

        ResponseCallback wrapper = new ResponseCallback() {

            @Override
            public void onComplete(Response response) {

                /**
                 * When the response is completed, If Response has the @InnerObject, return object that refers to the @InnerObject Annotation value
                 * While, has not @InnerObject Annotation, return object that be cast normally.
                 */

                if (callback != null) {

                    /**
                     * JSON Cast to GBObject
                     */

                    T GBModel = null;

                    if(GBObjectClass.getAnnotation(InnerObject.class) == null)
                        GBModel = response.getGBObject().cast(GBObjectClass);
                    else
                        GBModel = response.getGBObject().getInnerObject(GBObjectClass);

                    /**
                     * Store to local memory
                     */

                    GBData.getInstance().castAndStoreGBObject(GBModel);

                    /**
                     * Notify API complete event
                     */

                    callback.onComplete(GBModel, response);
                }
            }

            @Override
            public void onError(final Response response) {

                GBAPIError responseError = response.getAPIError();
                if(responseError == null) {

                    if (callback != null) {

                        GBAPIError apiError = response.getAPIError();
                        if(apiError == null) {
                            apiError = GBException.getApiErrorTemplateGBObject(response.getState());
                            response.setAPIError(apiError);
                        }

                        callback.onError(response);
                    }

                } else {

                    if (callback != null)
                        callback.onError(response);

/*
                    if(responseError.getErrorCode() == GBExceptionType.SERVER_EXPIRED_ACCESS_TOKEN.getErrorCode()) {

                        //refreshTokenAndRetryAPI(response);

                    } else {

                        if (callback != null)
                            callback.onError(response);
                    }
*/
                }
            }
        };

        if(GBSession == null)
            GBLog.d(TAG + "getAuthorizationRequest, method:%s, API:%s", method, path);
        else
            GBLog.d(TAG + "getAbstractRequest, method:%s, API:%s", method, path);

        Request request = new Request(method, GBSession, path, wrapper);
        return request;
    }

    /**
     * Request for GBObject
     */

    public static Request getAbstractRequest(GBSession GBSession, String path, final ObjectCallback<GBObject> callback) {
        return getAbstractRequest(Request.Method.POST, GBSession, path, callback, GBObject.class);
    }

    public static Request getAbstractRequest(Request.Method method, GBSession GBSession, String path, final ObjectCallback<GBObject> callback) {
        return getAbstractRequest(method, GBSession, path, callback, GBObject.class);
    }

    /**
     * If access token was expired, update tokens & Retry the API request
     */
/*
    private static final void refreshTokenAndRetryAPI(final Response response) {

        // Refresh tokens
        if(retryAPICount >= REQUEST_RETRY_MAX_COUNT) {
            retryAPICount = 0;
            return;
        }

        GB.getInstance().authorize(AuthType.RETRY_API, new GBStatusCallback() {

            @Override
            public void callback(GBSession session, State state, GBException exception) {

                if(state == State.TOKEN_REISSUED) {

                    String accessToken = GB.getInstance().getAccessToken();
                    String refreshToken = GB.getInstance().getRefreshToken();

                    Logger.d(TAG + "%s", "refreshTokenAndRetryAPI() - TOKEN_REISSUED");
                    Logger.d(TAG + "accessToken:%s", accessToken);
                    Logger.d(TAG + "refreshToken:%s", refreshToken);

                    // Retry API request

                    retryAPICount++;

                    Request request = response.getRequest();
                    final ResponseCallback callback = request.getCallbackListener();
                    ResponseCallback wrappedCallback = new ResponseCallback() {

                        @Override
                        public void onError(Response response) {
                            if(callback != null)
                                callback.onError(response);
                        }

                        @Override
                        public void onComplete(Response response) {
                            retryAPICount = 0;
                            if(callback != null)
                                callback.onComplete(response);
                            else
                                Logger.d(TAG + ", refreshTokenAndRetryAPI() = %s", "callback listener is NULL.");
                        }
                    };

                    request.setCallbackListener(wrappedCallback);
                    requestAPI(request);
                }
            }
        });
    }
*/

    public static void requestAPI(Request request) {

//        if(GBLogger.isTestMode()) {
//            request.execute();
//        } else {
//            RequestAsyncTask asyncTask = new RequestAsyncTask(request);
//            asyncTask.execute();
//        }

        RequestAsyncTask asyncTask = new RequestAsyncTask(request);
        asyncTask.execute();
    }
}
