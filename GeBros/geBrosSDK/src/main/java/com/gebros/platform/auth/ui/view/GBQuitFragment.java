package com.gebros.platform.auth.ui.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gebros.platform.auth.GBAuthManager;
import com.gebros.platform.auth.GBSession;
import com.gebros.platform.auth.ui.common.BaseFragment;
import com.gebros.platform.auth.ui.common.FragmentAware;
import com.gebros.platform.auth.ui.common.FragmentType;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.internal.JR;
import com.gebros.platform.listener.GBAuthListener;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.util.GBMessageUtils;


/**
 * Created by jce_platform on 2016. 6. 1..
 */
public class GBQuitFragment extends BaseFragment {

    private RelativeLayout closeBtn;
    private RelativeLayout backBtn;

    private RelativeLayout gameQuitContentArea;
    private TextView appIdText;
    private RelativeLayout gameQuitBtn;

    public GBQuitFragment() {
        fragmentType = FragmentType.GAME_QUIT_FRAGMENT;
        layoutId = JR.layout("GB_profile_game_quit");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        initialLayout(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        Bundle args = GBQuitFragment.this.getArguments();
        String info = args.getString(FragmentAware.DATA);
        appIdText.setText(info);
    }

    private void initialLayout(View view) {
        closeBtn = (RelativeLayout) view.findViewById(JR.id("game_quit_close_btn"));
        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        backBtn = (RelativeLayout) view.findViewById(JR.id("game_quit_back_btn"));
        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });
        gameQuitContentArea = (RelativeLayout) view.findViewById(JR.id("game_quit_content_ly"));
        appIdText = (TextView) view.findViewById(JR.id("game_quit_appid_value_tv"));
        gameQuitBtn = (RelativeLayout) view.findViewById(JR.id("game_quit_btn"));

        if( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ) {
            gameQuitContentArea.setPadding((int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    (int) getResources().getDimension(JR.dimen("base_top_bottom_margin")),
                    (int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    (int) getResources().getDimension(JR.dimen("base_top_bottom_margin")));
        } else if( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            gameQuitContentArea.setPadding((int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    (int) getResources().getDimension(JR.dimen("base_top_bottom_margin")),
                    (int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    (int) getResources().getDimension(JR.dimen("base_top_bottom_margin")));
        }

        gameQuitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                GBMessageUtils.alertAvailableCancel(activity, JR.string("alert_withdraw_confirm_label_title"), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress();
                        callGBQuitAPI(activity);
                    }
                });
            }
        });
    }

    public void callGBQuitAPI(final Activity activity) {
        GBAuthManager.DeleteAccount(activity, new GBAuthListener(){

            @Override
            public void onSuccess(GBSession newSession) {
                hideProgress();
                if(newSession.getState().equals(GBSession.SessionState.CLOSED)) {
                    //activity.setResult(GBActivityHelper.QUIT_COMPLETE_CODE);
                    GBMessageUtils.toast(activity, JR.string("alert_withdraw_end_label_title"));
                    activity.finish();
                }
            }

            @Override
            public void onFail(GBException e) {
                hideProgress();
                GBLog.d("callGBQuitAPI exception:::"+e.toString());
                AsyncErrorDialog dialog = new AsyncErrorDialog(getActivity(), JR.string("GB_alert_server_status"));
                dialog.show();
            }

            @Override
            public void onCancel(boolean isUserCancelled) {
                hideProgress();
                AsyncErrorDialog dialog = new AsyncErrorDialog(getActivity(), JR.string("GB_alert_server_status"));
                dialog.show();
            }
        });

    }

}
