package com.gebros.platform.auth.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gebros.platform.log.GBLog;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class GBWebClient extends WebViewClient {

    private static final String TAG = "JoypleWebViewClient:";
    private static final String[] INNER_SERVICE_DOMAIN = {"joycity.com"};
    private static final String MARKET_LINK = "market://details";

    public interface WebEventListener {

        public void onPageStarted();
        public void onPageFinished();
    }

    enum LinkType {

        INNER_WEB_LINK,
        OUTER_WEB_LINK,
        APP
    }

    private Activity activity;
    private WebEventListener webEventListener;

    public GBWebClient(Activity activity) {
        this.activity = activity;
    }

    public GBWebClient(Activity activity, WebEventListener webEventListener) {

        this.activity = activity;
        this.webEventListener = webEventListener;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

        LinkType linkType = getLinkType(url);

        if (linkType == LinkType.OUTER_WEB_LINK) {

            view.stopLoading();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);

        } else
            webEventListener.onPageStarted();
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        super.onPageFinished(view, url);
        webEventListener.onPageFinished();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        LinkType linkType = getLinkType(url);

        if (linkType == LinkType.OUTER_WEB_LINK) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);

        } else if (linkType == LinkType.APP) {

            if (!isAvailableIntent(url)) {

                GBLog.i(TAG + "Not available Intent:%s", url);
                return false;

            } else {

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    activity.startActivity(intent);

                } catch (Exception e) {

                    GBLog.e(e, TAG + "Not exists Intent:%s, errorMessage:%s", url, e.getMessage());
                }

                return true;
            }

        } else
            view.loadUrl(url);

        return true;
    }

    // Get link type
    private LinkType getLinkType(String url) {

        if (isWebLink(url)) {
            if (isServiceDomain(url))
                return LinkType.INNER_WEB_LINK;
            else
                return LinkType.OUTER_WEB_LINK;
        } else
            return LinkType.APP;
    }

    // Check if is web link
    private boolean isWebLink(String url) {
        return url.startsWith("http");
    }

    // Get domain
    public boolean isServiceDomain(String url) {

        Matcher match = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(/?)").matcher(url);

        if (match.find()) {

            String parsedDomain = match.group(2);
            for(String innerDomain : INNER_SERVICE_DOMAIN) {
                if (parsedDomain.contains(innerDomain))
                    return true;
            }
        }

        return false;
    }

    // Check existing the app
    private boolean isAvailableIntent(String appLink) {

        if (appLink.contains(MARKET_LINK))
            return true;

        Uri uri = Uri.parse(appLink);
        Intent intent = new Intent(Intent.ACTION_SEND, uri);

        List<ResolveInfo> list = activity
                .getApplicationContext()
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (list == null)
            return false;

        return (list.size() > 0);
    }
}
