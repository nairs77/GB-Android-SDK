package com.gebros.platform.auth.ui.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gebros.platform.auth.GBAuthManager;
import com.gebros.platform.auth.net.GBAppRequest;
import com.gebros.platform.auth.net.GBAppResponse;
import com.gebros.platform.auth.net.Response;
import com.gebros.platform.auth.ui.GBContentsAPI;
import com.gebros.platform.auth.ui.common.BaseFragment;
import com.gebros.platform.auth.ui.common.FragmentAware;
import com.gebros.platform.auth.ui.common.FragmentType;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.internal.JR;
import com.gebros.platform.listener.GBProfileListener;
import com.gebros.platform.log.GBLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jce_platform on 2016. 6. 1..
 */
public class GBAddInfoFragment extends BaseFragment implements View.OnClickListener{

    private static final int GENDER_MEN = 1;
    private static final int GENDER_WOMEN = 2;
    private static final String PRE_FRAGMENT_NAME = "GBAddInfoFragment";

    private boolean isMen = false;
    private boolean isSexChecked = false;

    private RelativeLayout closeBtn;
    private TextView titleText;
    private RelativeLayout backBtn;

    private TextView addInfoTitleText;
    private RelativeLayout birthDatePickerBtn;
    private TextView birthdayErrText;
    public static TextView birthText;
    private RelativeLayout menRadio;
    private RelativeLayout womenRadio;
    private TextView genderErrText;
    private TextView enrollBtnText;
    private RelativeLayout enrollBtn;
    private String callBirthValue;

    private String preFragmentName;
    private String gender;
    private String moveInfo;


    public GBAddInfoFragment () {
        fragmentType = FragmentType.ADDINFO_INFO_FRAGMENT;
        layoutId = JR.layout("GB_setting_myinfo_addinfo");
    }

    public static GBAddInfoFragment newInstance() {
        GBAddInfoFragment fragment = new GBAddInfoFragment();
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
        if(moveInfo != null) {
            setDataView(moveInfo);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        Bundle args = GBAddInfoFragment.this.getArguments();
        moveInfo = args.getString(FragmentAware.DATA);
        setDataView(moveInfo);
    }

    private void initialLayout(View view) {
        titleText = (TextView) view.findViewById(JR.id("addinfo_title_tv"));
        closeBtn = (RelativeLayout) view.findViewById(JR.id("addinfo_close_btn"));
        backBtn = (RelativeLayout) view.findViewById(JR.id("addinfo_back_btn"));

        addInfoTitleText = (TextView) view.findViewById(JR.id("addinfo_add_info_title_tv"));
        String birthDate = getResources().getString(JR.string("ui_common_birth_label_title")).toString();
        String genderStr = getResources().getString(JR.string("ui_common_gender_label_title")).toString();
        String addInfoExplainStr = birthDate + "/" + genderStr;
        addInfoTitleText.setText(addInfoExplainStr);
        birthDatePickerBtn = (RelativeLayout) view.findViewById(JR.id("addinfo_add_birth_ly"));
        birthdayErrText = (TextView) view.findViewById(JR.id("addinfo_add_birth_err_text"));
        birthText = (TextView) view.findViewById(JR.id("addinfo_birth_value_tv"));
        menRadio = (RelativeLayout) view.findViewById(JR.id("addinfo_add_men_radio"));
        womenRadio = (RelativeLayout) view.findViewById(JR.id("addinfo_add_women_radio"));
        genderErrText = (TextView) view.findViewById(JR.id("addinfo_add_sex_err_text"));
        enrollBtnText = (TextView) view.findViewById(JR.id("addinfo_confirm_tv"));
        enrollBtn = (RelativeLayout) view.findViewById(JR.id("addinfo_confirm_btn"));

        closeBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        birthDatePickerBtn.setOnClickListener(this);
        menRadio.setOnClickListener(this);
        womenRadio.setOnClickListener(this);
        enrollBtn.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == birthDatePickerBtn.getId()) {
            DialogFragment newDatePicker = new DatePickerFragment();
            newDatePicker.show(activity.getSupportFragmentManager() , "datePicker");
        } else if(viewId == menRadio.getId()) {
            isSexChecked = true;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                menRadio.setBackground(getResources().getDrawable(JR.drawable("GB_radio_on_orange_btn")));
                womenRadio.setBackground(getResources().getDrawable(JR.drawable("GB_radio_off_gray_btn")));
            } else {
                menRadio.setBackgroundDrawable(getResources().getDrawable(JR.drawable("GB_radio_on_orange_btn")));
                womenRadio.setBackgroundDrawable(getResources().getDrawable(JR.drawable("GB_radio_off_gray_btn")));
            }
            isMen = true;
        } else if(viewId == womenRadio.getId()) {
            isSexChecked = true;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                menRadio.setBackground(getResources().getDrawable(JR.drawable("GB_radio_off_gray_btn")));
                womenRadio.setBackground(getResources().getDrawable(JR.drawable("GB_radio_on_orange_btn")));
            } else {
                menRadio.setBackgroundDrawable(getResources().getDrawable(JR.drawable("GB_radio_off_gray_btn")));
                womenRadio.setBackgroundDrawable(getResources().getDrawable(JR.drawable("GB_radio_on_orange_btn")));
            }
            isMen = false;
        } else if(viewId == enrollBtn.getId()) {
            String birthValue = birthText.getText().toString();
            birthdayErrText.setVisibility(View.GONE);
            genderErrText.setVisibility(View.GONE);

            if(callBirthValue == null || getResources().getString(JR.string("ui_myinfo_notregist_label_title")).equals(birthValue)) {
                birthdayErrText.setText(JR.string("errorui_enroll2_selectbirth_label_title"));
                birthdayErrText.setVisibility(View.VISIBLE);
                return;
            }

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String today = dateFormat.format(date);
            if(callBirthValue.compareTo(today) > 0) {
                birthdayErrText.setText(JR.string("errorui_common_invaildbirth_label_title"));
                birthdayErrText.setVisibility(View.VISIBLE);
                return;
            }

            if(!isSexChecked) {
                genderErrText.setText(JR.string("errorui_enroll2_selectgender_label_title"));
                genderErrText.setVisibility(View.VISIBLE);
                return;
            }

            showProgress();
            if(isMen) {
                callAdditionalChangeAPI(GENDER_MEN, Integer.parseInt(callBirthValue));
            } else {
                callAdditionalChangeAPI(GENDER_WOMEN, Integer.parseInt(callBirthValue));
            }
        } else if(viewId == closeBtn.getId()) {
            activity.finish();
        } else if(viewId == backBtn.getId()) {
            back();
        }

    }

    private void callAdditionalChangeAPI(int gender, int birthday) {
        GBAppRequest appResources = new GBAppRequest(GBContentsAPI.ADDITIONAL_INFO_CHANGE_URI);
        appResources.addParameter("gender", gender);
        appResources.addParameter("birthday", birthday);

        appResources.post(new GBAppResponse() {

            @Override
            public void onComplete(JSONObject json, Response response)
                    throws JSONException {
                hideProgress();

                GBAuthManager.RequestProfile(new GBProfileListener() {
                    @Override
                    public void onSuccess(JSONObject object) {
                        back();
                    }

                    @Override
                    public void onFail(GBException e) {
                        back();
                    }
                });

            }

            @Override
            public void onError(Response response) {
                hideProgress();
                AsyncErrorDialog dialog = new AsyncErrorDialog(activity, JR.string("ui_main_default_error"));
                dialog.show();
                GBLog.d(TAG + "callAdditionalChangeAPI onError: %s", response.toString());
            }

        });
    }

    private void setDataView(String data) {
        String[] values = data.split("[|]");
        gender = "-1";
        if(values.length == 3){
            String birth = values[0];
            gender = values[1];
            preFragmentName = values[2];
            if(preFragmentName.equals("GBEmailEnrollFragment")) {
                backBtn.setVisibility(View.GONE);
                titleText.setText(JR.string("ui_enroll2_addinfo_top_title"));
                enrollBtnText.setText(JR.string("ui_enroll2_confirm_btn_title"));
            } else {
                String birthDate = getResources().getString(JR.string("ui_common_birth_label_title")).toString();
                String genderStr = getResources().getString(JR.string("ui_addmyinfo_gender_top_title")).toString();
                String addInfoExplainStr = birthDate + "/" + genderStr;
                titleText.setText(addInfoExplainStr);
                enrollBtnText.setText(JR.string("ui_common_confirm_btn_title"));
            }
            if(gender.equals("1")) {
                menRadio.setBackground(getResources().getDrawable(JR.drawable("GB_radio_on_orange_btn")));
                womenRadio.setBackground(getResources().getDrawable(JR.drawable("GB_radio_off_gray_btn")));
                isMen = true;
                isSexChecked = true;
            }
            else if(gender.equals("2")) {
                menRadio.setBackground(getResources().getDrawable(JR.drawable("GB_radio_off_gray_btn")));
                womenRadio.setBackground(getResources().getDrawable(JR.drawable("GB_radio_on_orange_btn")));
                isMen = false;
                isSexChecked = true;
            }

            if("".equals(birth) || birth == null || "null".equals(birth)) {
                birthText.setText(JR.string("ui_myinfo_notregist_label_title"));
            } else {
                callBirthValue = birth;
                String year = birth.substring(0, 4);
                String month = birth.substring(4, 6);
                String day = birth.substring(6, 8);
                birthText.setText(year+" . "+month+" . "+day);
            }
        }

    }

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(activity, this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String yearStr = year+"";
            String monthStr = "";
            String dayStr = "";
            int month = monthOfYear + 1;
            if(month < 10) {
                monthStr = "0" + month;
            } else {
                monthStr = month + "";
            }
            if(dayOfMonth < 10) {
                dayStr = "0" + dayOfMonth;
            } else {
                dayStr = dayOfMonth + "";
            }
            callBirthValue = yearStr + monthStr + dayStr;
            String birthValue = String.format("%d . %d . %d", year,monthOfYear+1, dayOfMonth);
            GBLog.d("JoycityAddInfoFragment::::::callBirthValue::::"+callBirthValue);
            GBLog.d("JoycityAddInfoFragment::::::birthValue::::"+birthValue);
            GBAddInfoFragment.birthText.setText(birthValue);
        }

    }
}
