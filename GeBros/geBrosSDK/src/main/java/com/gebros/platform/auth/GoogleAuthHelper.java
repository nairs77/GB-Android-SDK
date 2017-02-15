package com.gebros.platform.auth;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.gebros.platform.ActivityResultHelper;
import com.gebros.platform.GBSdk;
import com.gebros.platform.concurrent.ISimpleAsyncTask;
import com.gebros.platform.concurrent.SimpleAsyncTask;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.log.GBLog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
/**
 * Created by gebros.nairs77@gmail.com on 5/24/16.
 */
class GoogleAuthHelper extends AuthHelper implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = GoogleAuthHelper.class.getSimpleName();

    public final static int REQUEST_CODE_GOOGLE_SIGNIN_ID_TOKEN = 9005;
    public static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 3001;

    private GoogleApiClient mApiClient;

    public GoogleAuthHelper(GBAuthImpl impl) {
        super(impl);
    }

    public AuthType getAuthType() {
        return AuthType.GOOGLE;
    }

    public void login(final Activity activity, final GBAuthListener listener) {

        if (!_isGooglePlayServicesAvailableState(activity)) {
            _checkGooglePlayServicesAvailable(activity, listener);
            return;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mApiClient);
        ActivityResultHelper.startActivityForResult(activity, REQUEST_CODE_GOOGLE_SIGNIN_ID_TOKEN, signIntent, new ActivityResultHelper.ActivityResultListener() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                    if (result != null)
                        mImpl.authorize(getAuthType(), result.getSignInAccount().getIdToken(), result.getSignInAccount().getEmail(), listener);
                } else {
                    listener.onFail(GBException.getGBExceptionTemplate(GBExceptionType.GOOGLE_ERROR));
                }
            }
        });
    }

    public void logout(Activity activity, GBAuthListener listener) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //- Private Methods

    private boolean _isGooglePlayServicesAvailableState(Activity activity){
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);

        return (resultCode == ConnectionResult.SUCCESS) ? true : false;
    }

    private void _checkGooglePlayServicesAvailable(final Activity activity, final GBAuthListener listener) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, REQUEST_CODE_RECOVER_PLAY_SERVICES, new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(activity, "Google Play Services must be installed.", Toast.LENGTH_SHORT).show();
                        if (listener != null)
                            listener.onFail(GBException.getGBExceptionTemplate(GBExceptionType.GOOGLE_ERROR));
                    }
                }).show();
            } else {
                Toast.makeText(activity, "This device is not supported", Toast.LENGTH_LONG).show();
                activity.finish();
            }
        }
    }
}
