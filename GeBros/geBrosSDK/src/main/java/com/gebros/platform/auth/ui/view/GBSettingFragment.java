package com.gebros.platform.auth.ui.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gebros.platform.auth.ProfileApi;
import com.gebros.platform.auth.model.GBUser;
import com.gebros.platform.auth.ui.GBContentsAPI;
import com.gebros.platform.auth.ui.common.BaseFragment;
import com.gebros.platform.auth.ui.common.FragmentType;
import com.gebros.platform.internal.JR;


/**
 * Created by jce_platform on 2016. 5. 30..
 */
public class GBSettingFragment extends BaseFragment {

    private RelativeLayout settingMyinfoLabel;
    private RelativeLayout settingCustomerLabel;

    private RelativeLayout moveAddInfoBtn;
    private TextView addInfoExplain;
    private TextView addInfoText;
    private RelativeLayout moveCustomerBtn;
    private RelativeLayout moveClickwrapBtn;
//    private RelativeLayout moveQuitBtn;

    private GBUser user;

    private String gender;
    private String birth = "";
    private String appId = "";

    public GBSettingFragment() {
        fragmentType = FragmentType.SETTING_INFO_FRAGMENT;
        layoutId = JR.layout("joycity_setting");
        titleRes = JR.string("ui_setting_setting_top_title");
    }

    public static GBSettingFragment newInstance() {
        GBSettingFragment fragment = new GBSettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        initialLayout(rootView);

        showProgress();
        getProfile();

        return rootView;
    }

    private void getProfile() {
        hideProgress();
        user = ProfileApi.getLocalUser();
        setDataView();
    }

    private void initialLayout(View view) {
        settingMyinfoLabel = (RelativeLayout) view.findViewById(JR.id("setting_myinfo_label"));
        moveAddInfoBtn = (RelativeLayout) view.findViewById(JR.id("setting_myinfo_add_btn"));
        settingCustomerLabel = (RelativeLayout) view.findViewById(JR.id("setting_help_label"));
        moveCustomerBtn = (RelativeLayout) view.findViewById(JR.id("setting_help_btn"));
        moveClickwrapBtn = (RelativeLayout) view.findViewById(JR.id("setting_clickwrap_btn"));
        if( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ) {
            settingMyinfoLabel.setPadding((int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    0,
                    (int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    0);
            moveAddInfoBtn.setPadding((int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    0,
                    (int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    0);
            settingCustomerLabel.setPadding((int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    0,
                    (int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    0);
            moveCustomerBtn.setPadding((int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    0,
                    (int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    0);
            moveClickwrapBtn.setPadding((int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    0,
                    (int) getResources().getDimension(JR.dimen("base_left_right_margin")),
                    0);
        } else if( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            settingMyinfoLabel.setPadding((int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    0,
                    (int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    0);
            moveAddInfoBtn.setPadding((int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    0,
                    (int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    0);
            settingCustomerLabel.setPadding((int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    0,
                    (int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    0);
            moveCustomerBtn.setPadding((int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    0,
                    (int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    0);
            moveClickwrapBtn.setPadding((int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    0,
                    (int) getResources().getDimension(JR.dimen("land_left_right_margin")),
                    0);
        }

        addInfoExplain = (TextView) view.findViewById(JR.id("setting_myinfo_add_explain_tv"));
        String birthDate = getResources().getString(JR.string("ui_common_birth_label_title")).toString();
        String genderStr = getResources().getString(JR.string("ui_common_gender_label_title")).toString();
        String addInfoExplainStr = birthDate + "/" + genderStr;
        addInfoExplain.setText(addInfoExplainStr);
        addInfoText = (TextView) view.findViewById(JR.id("setting_myinfo_add_value_tv"));


        moveAddInfoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fragmentAware.fragmentDataMove(GBSettingFragment.this,
                        new GBAddInfoFragment(), birth + "|" + gender + "|" + "null");
            }
        });

        moveCustomerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fragmentAware.fragmentDataMove(GBSettingFragment.this, GBCustomerWebFragment.newInstance(GBContentsAPI.GB_CUSTOMER_WEB_URL), "GBSettingFragment");
            }
        });

        moveClickwrapBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fragmentAware.fragmentDataMove(GBSettingFragment.this, GBEULAWebFragment.newInstance(GBContentsAPI.GB_CLICKWRAP_WEB_URL), "GBSettingFragment");
            }
        });

//        moveQuitBtn = (RelativeLayout) view.findViewById(JR.id("setting_quit_btn"));
//        moveQuitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentAware.fragmentDataMove(GBSettingFragment.this, new GBQuitFragment(), appId);
//            }
//        });
    }

    private void setDataView() {
        if(user == null) {
            return;
        }

        birth = user.getBirthday();
        gender = user.getGender();
        appId = user.getUserKey()+"";
        String male = getResources().getString(JR.string("ui_common_male_label_radio_title")).toString();
        String female = getResources().getString(JR.string("ui_common_female_label_radio_title")).toString();
        String notSet = getResources().getString(JR.string("ui_myinfo_notregist_label_title")).toString();
        if( "".equals(birth) || birth == null || "null".equals(birth)) {
            addInfoText.setText(notSet);
        } else {
            String year = birth.substring(0, 4);
            String month = birth.substring(4, 6);
            String day = birth.substring(6, 8);
            String birthFormat = year + "." + month + "." + day;
            if(gender == null) {
                addInfoText.setText(notSet);
            } else {
                if(gender.equals("1")) {
                    addInfoText.setText(birthFormat+"/"+male);
                } else if(gender.equals("2")) {
                    addInfoText.setText(birthFormat+"/"+female);
                } else {
                    addInfoText.setText(notSet);
                }
            }

        }
    }

}
