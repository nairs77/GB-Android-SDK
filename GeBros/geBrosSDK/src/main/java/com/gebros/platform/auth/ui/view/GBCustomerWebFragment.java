package com.gebros.platform.auth.ui.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gebros.platform.GBSettings;
import com.gebros.platform.auth.GBSession;
import com.gebros.platform.auth.net.Request;
import com.gebros.platform.auth.ui.common.BaseFragment;
import com.gebros.platform.auth.ui.common.FragmentAware;
import com.gebros.platform.auth.ui.common.FragmentType;
import com.gebros.platform.auth.ui.common.GBWebClient;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.internal.JR;
import com.gebros.platform.log.GBLog;
import com.gebros.platform.util.GBDeviceUtils;
import com.gebros.platform.util.GBValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class GBCustomerWebFragment extends BaseFragment {

    private static final int UI_WEBVIEW_FILEUPLOAD_REQUEST_CODE = 15003;

    private RelativeLayout topArea;
    private RelativeLayout closeBtn;
    private TextView titleText;
    private RelativeLayout backBtn;

    private WebView gbWebView;
    private String url;
    private ValueCallback<Uri> fileUploadMessage;

    private String moveData = "";

    public GBCustomerWebFragment() {

        fragmentType = FragmentType.JOYPLE_CUSTOMER_WEBVIEW;
        layoutId = JR.layout("joyple_webview");
    }

    public static GBCustomerWebFragment newInstance(String url) {

        GBCustomerWebFragment fragment = new GBCustomerWebFragment();
        String language = GBDeviceUtils.getLanguage();
        /*
        if("ko".equals(language)) {
            url += "&lang=ko";
        } else if("en".equals(language)) {
            url += "&lang=en";
        } else if("ja".equals(language)) {
            url += "&lang=ja";
        } else if("zh_CN".equals(language)) {
            url += "&lang=zh";
        } else if("zh_TW".equals(language)) {
            url += "&lang=zt";
        } else {
            url += "&lang=ko";
        }
        */
        fragment.setUrl(url);

        return fragment;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        Bundle args = GBCustomerWebFragment.this.getArguments();
        moveData = args.getString(FragmentAware.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        topArea = (RelativeLayout) rootView.findViewById(JR.id("joyple_wv_top_ly"));
        topArea.setVisibility(View.VISIBLE);
        titleText = (TextView) rootView.findViewById(JR.id("joyple_wv_title_tv"));
        titleText.setText(JR.string("ui_setting_customer_label_title"));
        closeBtn = (RelativeLayout) rootView.findViewById(JR.id("joyple_wv_close_btn"));
        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(moveData.equals("JoyplePasswordFindFragment")) {
                    back();
                    return;
                }
                activity.finish();
            }
        });
        backBtn = (RelativeLayout) rootView.findViewById(JR.id("joyple_wv_back_btn"));
        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();

            }
        });

        if(GBValidator.isNullOrEmpty(url)) {

            GBException exception = new GBException("Webview url is NULL.");
            GBLog.e(exception, "%s", exception.getMessage());
            return rootView;
        }

        String[] authorizationValues = {
                GBSettings.getClientSecret(),
                GBSession.getActiveSession().getAccessToken(),
                "",
                ""
        };

        Map<String, String> headerInfo = new HashMap<String, String>();
        headerInfo.put(Request.AUTHORIZATION_HEADER_KEY, Request.createAuthorizationHeaderValues(authorizationValues));

        gbWebView = (WebView) rootView.findViewById(JR.id("joyple_wv"));
        gbWebView.loadUrl(url, headerInfo);
        gbWebView.clearCache(true);
        gbWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = gbWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        gbWebView.setWebViewClient(new GBWebClient(activity, new GBWebClient.WebEventListener() {

            @Override
            public void onPageStarted() {
                showProgress();
            }

            @Override
            public void onPageFinished() {
                hideProgress();
                gbWebView.scrollTo(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics()));
            }
        }));

        gbWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(final WebView view, String url, String message, final android.webkit.JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMessage, String acceptType, String capture) {
                this.openFileChooser(uploadMessage);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMessage, String acceptType) {
                this.openFileChooser(uploadMessage);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                fileUploadMessage = uploadMsg;
                activity.startActivityForResult(Intent.createChooser(intent, "Joycity"), UI_WEBVIEW_FILEUPLOAD_REQUEST_CODE);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == UI_WEBVIEW_FILEUPLOAD_REQUEST_CODE) {

            if(fileUploadMessage == null)
                return;

            Uri result = ((data == null || resultCode != Activity.RESULT_OK) ? null : data.getData());

            fileUploadMessage.onReceiveValue(result);
            fileUploadMessage = null;
        }
    }
}
