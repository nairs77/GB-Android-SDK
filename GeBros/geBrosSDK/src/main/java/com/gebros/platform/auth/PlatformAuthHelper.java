package com.gebros.platform.auth;

import android.app.Activity;

import com.gebros.platform.concurrent.ISimpleAsyncTask;
import com.gebros.platform.concurrent.SimpleAsyncTask;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.platform.IPlatformClient;
import com.gebros.platform.platform.IPlatformListener;
import com.gebros.platform.platform.PlatformType;

/**
 * Created by gebros.nairs77@gmail.com on 5/24/16.
 */

class PlatformAuthHelper extends AuthHelper {

    protected IPlatformClient mClient;

    public PlatformAuthHelper(GBAuthImpl impl) {
        super(impl);

        mClient = impl.getPlatformClient();
    }

    @Override
    public AuthType getAuthType() {
        return mClient.getAuthType();
    }

    @Override
    public void login(Activity activity, final GBAuthListener listener) {
        mClient.doPlatformLogin(activity, new IPlatformListener.OnAuthListener() {

            @Override
            public void onSuccess(final LoginResult result) {

                // Because of Handler (ResponseCallback...)
                SimpleAsyncTask.doRunUIThread(new ISimpleAsyncTask.OnUIThreadTask() {
                    @Override
                    public void doRunUIThread() {
                        mImpl.authorize(getAuthType(), result.getAccessToekn(), result.getUID(), listener);
                    }
                });
            }

            @Override
            public void onFail(LoginResult result) {
                listener.onFail(new GBException(result.getErrorCode(), result.getDetailErrorCode()+""));
            }

            @Override
            public void onCancel(boolean isUserCancel) {
                listener.onCancel(isUserCancel);
            }
        });
    }

    @Override
    public void logout(Activity activity, final GBAuthListener listener) {
        mClient.doPlatformLogout(null, new IPlatformListener.OnAuthListener() {
            @Override
            public void onSuccess(LoginResult result) {

                mImpl.requestLogout(listener);
            }

            @Override
            public void onFail(LoginResult result) {
                listener.onFail(new GBException(result.getErrorCode(), result.getDetailErrorCode()+""));
            }

            @Override
            public void onCancel(boolean isUserCancel) {
                listener.onCancel(isUserCancel);
            }
        });
    }
}
