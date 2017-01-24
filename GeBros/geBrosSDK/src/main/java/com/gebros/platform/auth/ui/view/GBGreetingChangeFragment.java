package com.gebros.platform.auth.ui.view;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gebros.platform.auth.GBAuthManager;
import com.gebros.platform.auth.model.common.GBAPIError;
import com.gebros.platform.auth.net.GBAppRequest;
import com.gebros.platform.auth.net.GBAppResponse;
import com.gebros.platform.auth.net.Request;
import com.gebros.platform.auth.net.Response;
import com.gebros.platform.auth.ui.ByteLengthFilter;
import com.gebros.platform.auth.ui.GBContentsAPI;
import com.gebros.platform.auth.ui.common.BaseFragment;
import com.gebros.platform.auth.ui.common.FragmentAware;
import com.gebros.platform.auth.ui.common.FragmentType;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBServerErrorCode;
import com.gebros.platform.internal.JR;
import com.gebros.platform.listener.GBProfileListener;
import com.gebros.platform.log.GBLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class GBGreetingChangeFragment extends BaseFragment {

    private RelativeLayout greetingChangeTopArea;
    private RelativeLayout closeBtn;
    private RelativeLayout backBtn;

    private RelativeLayout contentLayout;
    private RelativeLayout contentTextLayout;
    private EditText contentText;
    private RelativeLayout confirmBtn;
    private TextView inputCountText;
    private TextView maxCountText;
    private TextView errorText;

    private final int MAX_GREETING_MESSAGE_BYTES = 180;
    private final int MAX_GREETING_TEXTLINE_LENGTH = 5;

    private static final String ENCODING_TYPE = "utf-8";
    private String preInfo;

    public GBGreetingChangeFragment() {
        fragmentType = FragmentType.GREETING_CHANGE_FRAGMENT;
        layoutId = JR.layout("GB_profile_info_change");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

        Bundle args = GBGreetingChangeFragment.this.getArguments();
        preInfo = args.getString(FragmentAware.DATA);
        contentText.setText(preInfo);
        contentText.requestFocus();

        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(contentText, 0);

        int inputLength = 0;
        try {

            String greetingStr = contentText.getText().toString();
            inputLength = greetingStr.getBytes(ENCODING_TYPE).length;

        } catch (UnsupportedEncodingException e) {

        }

        String length = String.valueOf(inputLength);
        inputCountText.setText(length);

        contentText.addTextChangedListener(textWatcherInput);
    }

    private void initialLayout(View view) {
        greetingChangeTopArea = (RelativeLayout) view.findViewById(JR.id("GB_profile_info_change_top_ly"));
        closeBtn = (RelativeLayout) view.findViewById(JR.id("GB_profile_info_change_close_btn"));
        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        backBtn = (RelativeLayout) view.findViewById(JR.id("GB_profile_info_change_back_btn"));
        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
                back();
            }
        });
        contentLayout = (RelativeLayout) view.findViewById(JR.id("GB_profile_info_change_fragment"));
        contentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
            }
        });

        contentTextLayout = (RelativeLayout) view.findViewById(JR.id("GB_profile_info_change_content_ly"));

        RelativeLayout.LayoutParams contentTextLy =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        contentTextLy.topMargin = (int) getResources().getDimension(JR.dimen("profile_info_change_top_margin"));
        if( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ) {
            contentTextLy.rightMargin = (int) getResources().getDimension(JR.dimen("base_left_right_margin"));
            contentTextLy.leftMargin = (int) getResources().getDimension(JR.dimen("base_left_right_margin"));
        } else if( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            contentTextLy.rightMargin = (int) getResources().getDimension(JR.dimen("land_left_right_margin"));
            contentTextLy.leftMargin = (int) getResources().getDimension(JR.dimen("land_left_right_margin"));
        }
        contentTextLy.addRule(RelativeLayout.BELOW, greetingChangeTopArea.getId());
        contentTextLayout.setLayoutParams(contentTextLy);

        contentText = (EditText) view.findViewById(JR.id("GB_profile_info_change_content_et"));
        contentText.setSingleLine(false);
        contentText.setMaxLines(MAX_GREETING_TEXTLINE_LENGTH);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new ByteLengthFilter(MAX_GREETING_MESSAGE_BYTES, ENCODING_TYPE);
        contentText.setFilters(filters);

        inputCountText = (TextView) view.findViewById(JR.id("GB_profile_info_change_current_count_tv"));
        maxCountText = (TextView) view.findViewById(JR.id("GB_profile_info_change_max_count_tv"));
        maxCountText.setText(MAX_GREETING_MESSAGE_BYTES+"");
        errorText = (TextView) view.findViewById(JR.id("GB_profile_info_error_tv"));

        confirmBtn = (RelativeLayout) view.findViewById(JR.id("GB_profile_info_change_confirm_ly"));
        confirmBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String changeInfo = contentText.getText().toString();
                if(changeInfo.equals(preInfo)) {
                    hideSoftKeyboard(contentText);
                    fragmentAware.fragmentSwitch(GBGreetingChangeFragment.this, new GBProfileFragment());
                    return;
                }
                vaildGreeting(contentText.getText().toString());
            }
        });
    }

    TextWatcher textWatcherInput = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {
            int inputLength = 0;
            try {

                String nicknameStr = s.toString();
                inputLength = nicknameStr.getBytes(ENCODING_TYPE).length;

            } catch (UnsupportedEncodingException e) {

            }

            String length = String.valueOf(inputLength);
            inputCountText.setText(length);
        }

    };

    private void callGreetingChangeAPI(String greeting) {
        GBAppRequest appResources = new GBAppRequest(GBContentsAPI.GREETING_CHANGE_URI);
        appResources.addParameter("msg", greeting);
        appResources.setRequestType(Request.RequestType.API);

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
                GBAPIError responseError = response.getAPIError();
                if(responseError.getErrorCode() == GBServerErrorCode.LETTER_EXCEED_MAX_LENGTH) {
                    errorText.setText(JR.string("errorui_greeting_max_label_title"));
                    contentText.requestFocus();
                } else if(responseError.getErrorCode() == GBServerErrorCode.LETTER_LACK_MIN_LENGTH) {
                    errorText.setText(JR.string("errorui_greeting_max_label_title"));
                    contentText.requestFocus();
                } else if(responseError.getErrorCode() == GBServerErrorCode.INCLUDE_PROHIBITED_WORDS) {
                    errorText.setText(JR.string("errorui_greeting_prohibited_label_title"));
                    contentText.setText("");
                    contentText.requestFocus();
                } else {
                    AsyncErrorDialog dialog = new AsyncErrorDialog(activity, JR.string("ui_main_default_error"));
                    dialog.show();
                    contentText.requestFocus();
                }
                GBLog.d(TAG + "callNicknameChangeAPI onError: %s", response.toString());
            }
        });
    }

    private void vaildGreeting(String greeting) {
        hideSoftKeyboard(contentText);
        showProgress();
        callGreetingChangeAPI(greeting);
    }
}
