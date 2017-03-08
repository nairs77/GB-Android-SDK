package com.gebros.platform.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gebros.platform.GBSettings;
import com.gebros.platform.auth.AuthType;
import com.gebros.platform.auth.GBAuthManager;
import com.gebros.platform.auth.GBSession;
import com.gebros.platform.auth.ProfileApi;
import com.gebros.platform.auth.ui.GBProfileViewType;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.internal.JR;
import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.listener.GBInAppListener;
import com.gebros.platform.listener.GBProfileListener;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.pay.GBInAppManager;
import com.gebros.platform.util.GBMessageUtils;

import org.json.JSONObject;

/**
 * Created by gebros.nairs77@gmail.com on 5/30/16.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = LoginFragment.class.getSimpleName();


    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View loginView = inflater.inflate(R.layout.activity_login, container, false);

        mPlatformTitle = (TextView)loginView.findViewById(R.id.login_platform_title);
        mPlatformTitle.setText(GBSettings.getPlatformType().getName());
        mBtnLogin = (Button)loginView.findViewById(R.id.btn_login);
        mBtnProfile = (Button)loginView.findViewById(R.id.btn_connect);
        mTvUserkey = (TextView) loginView.findViewById(R.id.login_user_key_info_tv);
        mBtnLogin.setOnClickListener(this);
        mBtnProfile.setOnClickListener(this);

        //setSessionState(GBSession.getActiveSession().getState());
        return loginView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {

        int clickedViewId = view.getId();

        if (clickedViewId == mBtnLogin.getId()) {
            //showProgress();

            if (!isLogin)
                Login();
            else
                Logout();

        } else if (clickedViewId == mBtnProfile.getId()) {

            GBAuthManager.ConnectChannel(getActivity(), AuthType.FACEBOOK, new GBAuthListener() {
                @Override
                public void onSuccess(GBSession newSession) {
                    setSessionState(newSession.getState());
                    //hideProgress();
                    mBtnProfile.setVisibility(View.GONE);
                    //GetProfile();
                    mTvUserkey.setText(newSession.getUserKey());
                }

                @Override
                public void onFail(GBException e) {

                }

                @Override
                public void onCancel(boolean isUserCancelled) {

                }
            });
/*
            GBGameManager.GameExitService(getActivity(), new GBGameListener() {
                @Override
                public void onSuccess() {
                    JLog.d(TAG + " MAIN GameExitService onSuccess:::::");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Game Exit Suc", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onFail(GBException e) {
                    JLog.d(TAG + " MAIN GameExitService onFail:::::");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Game Exit fail", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

           // ShowProfile();
*/
        }
    }

    private void Login() {

        boolean isReady = GBSession.getActiveSession().getState() == GBSession.SessionState.READY ? true : false;

        if (isReady)
            GBLog.d("isReady");

        GBAuthManager.LoginWithAuthType(getActivity(), AuthType.GOOGLE, new GBAuthListener() {
            @Override
            public void onSuccess(final GBSession newSession) {
                if (newSession.isOpened()) {
                    GBLog.d(TAG + "Session Open.. token = %s", newSession.getUserKey());
                //    Toast.makeText(getActivity(), "[Login onSuccess]Session Open.. token = "+newSession.getAccessToken(), Toast.LENGTH_LONG).show();
                    setSessionState(newSession.getState());
                    hideProgress();
                    mBtnProfile.setVisibility(View.VISIBLE);
                    //GetProfile();
                    mTvUserkey.setText(newSession.getUserKey());

                    GBInAppManager.InitInAppService(newSession.getUserKey(), new GBInAppListener.OnIabSetupFinishedListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getActivity(), "[InitInAppService onSuccess]", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(getActivity(), "[InitInAppService onFail]", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "[Login onSuccess]Session not open.. state = "+newSession.getState(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFail(GBException e) {
                hideProgress();
                int errorCode = e.getErrorCode();
                Toast.makeText(getActivity(), "[Login onFail] errorCode = "+errorCode+" , detailError = "+e.getDetailError(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(boolean isUserCancelled) {
                hideProgress();
                Toast.makeText(getActivity(), "[Login onCancel]isUserCancelled:::"+isUserCancelled, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void Logout() {
        GBAuthManager.Logout(getActivity(), new GBAuthListener() {
            @Override
            public void onSuccess(GBSession newSession) {
                setSessionState(newSession.getState());
                hideProgress();
                Toast.makeText(getActivity(), "[Logout onSuccess]Session state = "+newSession.getState(), Toast.LENGTH_LONG).show();
                mBtnProfile.setVisibility(View.GONE);
                mTvUserkey.setText(newSession.getUserKey());
            }

            @Override
            public void onFail(GBException e) {
                Toast.makeText(getActivity(), "[Logout onFail]errorCode = "+e.getErrorCode()+" , detailError = "+e.getDetailError(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(boolean isUserCancelled) {
                Toast.makeText(getActivity(), "[Logout onCancel]isLogoutCancelled = "+isUserCancelled, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void LinkAccount() {

    }

    private void GetProfile() {
        showProgress();

        ProfileApi.RequestProfile(new GBProfileListener() {
            @Override
            public void onSuccess(JSONObject object) {
                hideProgress();

                String userKey = ProfileApi.getLocalUser().getUserKey();
                mTvUserkey.setText(userKey);

                GBInAppManager.InitInAppService(userKey, null);
            }

            @Override
            public void onFail(GBException e) {
                hideProgress();

                if (e.getExceptionType() == GBExceptionType.SESSION_INVALID)
                    GBMessageUtils.alert(getActivity(), "재로그인이 필요합니다. !!!");
                else
                    GBMessageUtils.alert(getActivity(), e.getMessage());
            }
        });
    }

    private void ShowProfile() {
        GBAuthManager.showProfile(getActivity(), GBProfileViewType.GBProfileUserInfo);
    }

    private void setSessionState(GBSession.SessionState state) {
        if (state == GBSession.SessionState.OPEN) {
            mBtnLogin.setText("로그아웃");
            isLogin = true;

            //mBtnProfile.setVisibility(View.VISIBLE);
        } else {
            mBtnLogin.setText("로그인");
            isLogin = false;
            //mBtnProfile.setVisibility(View.INVISIBLE);
        }
    }

    private void showProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            return;

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgressDialog.show();
        mProgressDialog.setContentView(JR.layout("custom_progress"));
    }

    private void hideProgress() {
        if (mProgressDialog == null)
            return;

        try {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isLogin = false;
    private ProgressDialog mProgressDialog;

    private TextView mPlatformTitle;
    private Button mBtnLogin;
    private Button mBtnProfile;
    private TextView mTvUserkey;

}
