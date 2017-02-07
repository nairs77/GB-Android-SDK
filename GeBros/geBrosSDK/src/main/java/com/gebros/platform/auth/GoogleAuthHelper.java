package com.gebros.platform.auth;

import android.app.Activity;
import android.content.Intent;

import android.support.annotation.NonNull;

import com.gebros.platform.ActivityResultHelper;
import com.gebros.platform.GBSdk;
import com.gebros.platform.listener.GBAuthListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
/**
 * Created by gebros.nairs77@gmail.com on 5/24/16.
 */
class GoogleAuthHelper extends AuthHelper implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9999;
    private GoogleApiClient mApiClient;

    public GoogleAuthHelper(GBAuthImpl impl) {
        super(impl);
    }

    public AuthType getAuthType() {
        return AuthType.GOOGLE;
    }

    public void login(Activity activity, GBAuthListener listener) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mApiClient);
        ActivityResultHelper.startActivityForResult(activity, RC_SIGN_IN, signIntent, new ActivityResultHelper.ActivityResultListener() {
            @Override
            public void onActivityResult(int resultCode, Intent intent) {

            }
        });
    }

    public void logout(Activity activity, GBAuthListener listener) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
