package com.gebros.sample;

import android.app.ProgressDialog;
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

import com.joycity.platform.sdk.JoypleSettings;
import com.joycity.platform.sdk.auth.JoypleAuthManager;
import com.joycity.platform.sdk.auth.JoypleSession;
import com.joycity.platform.sdk.auth.ProfileApi;
import com.joycity.platform.sdk.auth.ui.JoypleProfileViewType;
import com.joycity.platform.sdk.exception.JoypleException;
import com.joycity.platform.sdk.exception.JoypleExceptionType;
import com.joycity.platform.sdk.game.JoypleGameManager;
import com.joycity.platform.sdk.internal.JR;
import com.joycity.platform.sdk.listener.JoypleAuthListener;
import com.joycity.platform.sdk.listener.JoypleGameListener;
import com.joycity.platform.sdk.listener.JoypleProfileListener;
import com.joycity.platform.sdk.log.JLog;
import com.joycity.platform.sdk.pay.JoypleInAppManager;
import com.joycity.platform.sdk.util.JoypleMessageUtils;

import org.json.JSONObject;

/**
 * Created by nairs77@joycity.com on 5/30/16.
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
        mPlatformTitle.setText(JoypleSettings.getPlatformType().getName());
        mBtnLogin = (Button)loginView.findViewById(R.id.btn_login);
        mBtnProfile = (Button)loginView.findViewById(R.id.btn_profile);
        mTvUserkey = (TextView) loginView.findViewById(R.id.login_user_key_info_tv);
        mBtnLogin.setOnClickListener(this);
        mBtnProfile.setOnClickListener(this);

        setSessionState(JoypleSession.getActiveSession().getState());
        return loginView;
    }


    @Override
    public void onClick(View view) {

        int clickedViewId = view.getId();

        if (clickedViewId == mBtnLogin.getId()) {
            showProgress();

            if (!isLogin)
                Login();
            else
                Logout();

        } else if (clickedViewId == mBtnProfile.getId()) {

            JoypleGameManager.GameExitService(getActivity(), new JoypleGameListener() {
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
                public void onFail(JoypleException e) {
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
        }
    }

    private void Login() {
        JoypleAuthManager.Login(getActivity(), new JoypleAuthListener() {
            @Override
            public void onSuccess(final JoypleSession newSession) {
                if (newSession.isOpened()) {
                    JLog.d(TAG + "Session Open.. token = %s", newSession.getAccessToken());
                //    Toast.makeText(getActivity(), "[Login onSuccess]Session Open.. token = "+newSession.getAccessToken(), Toast.LENGTH_LONG).show();
                    setSessionState(newSession.getState());
                    hideProgress();
                    mBtnProfile.setVisibility(View.VISIBLE);
                    GetProfile();
                } else {
                    Toast.makeText(getActivity(), "[Login onSuccess]Session not open.. state = "+newSession.getState(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFail(JoypleException e) {
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
        JoypleAuthManager.Logout(getActivity(), new JoypleAuthListener() {
            @Override
            public void onSuccess(JoypleSession newSession) {
                setSessionState(newSession.getState());
                hideProgress();
                Toast.makeText(getActivity(), "[Logout onSuccess]Session state = "+newSession.getState(), Toast.LENGTH_LONG).show();
                mBtnProfile.setVisibility(View.GONE);

            }

            @Override
            public void onFail(JoypleException e) {
                Toast.makeText(getActivity(), "[Logout onFail]errorCode = "+e.getErrorCode()+" , detailError = "+e.getDetailError(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(boolean isUserCancelled) {
                Toast.makeText(getActivity(), "[Logout onCancel]isLogoutCancelled = "+isUserCancelled, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetProfile() {
        showProgress();

        ProfileApi.RequestProfile(new JoypleProfileListener() {
            @Override
            public void onSuccess(JSONObject object) {
                hideProgress();

                String userKey = ProfileApi.getLocalUser().getUserKey();
                mTvUserkey.setText(userKey);

                JoypleInAppManager.InitInAppService(userKey, null);
            }

            @Override
            public void onFail(JoypleException e) {
                hideProgress();

                if (e.getExceptionType() == JoypleExceptionType.SESSION_INVALID)
                    JoypleMessageUtils.alert(getActivity(), "재로그인이 필요합니다. !!!");
                else
                    JoypleMessageUtils.alert(getActivity(), e.getMessage());
            }
        });
    }

    private void ShowProfile() {
        JoypleAuthManager.showProfile(getActivity(), JoypleProfileViewType.JoypleProfileUserInfo);
    }

    private void setSessionState(JoypleSession.SessionState state) {
        if (state == JoypleSession.SessionState.OPEN) {
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
