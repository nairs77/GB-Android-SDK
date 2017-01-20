package com.gebros.platform.auth.ui.common;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.gebros.platform.internal.JR;
import com.gebros.platform.log.GBLog;

/**
 * Created by jce_platform on 2016. 5. 30..
 */
public class BaseFragmentPagerActivity extends FragmentActivity implements BaseFragmentSupport{

    protected String TAG = getClass().getCanonicalName() + ":";

    protected static final String MAIN_LAYOUT_ID = "joycity_main";
    protected static final String MAIN_CONTENTS_PAGER_ID = "joycity_main_content_pager";
    protected static final String MAIN_CONTENT_ID = "joycity_main_content";

    protected FragmentManager fragmentManager;
    protected FragmentManager pagerFragmentManager;
    protected FragmentManager layerFragmentManager;
    protected static Resources res;
    protected Context context;

    protected int mainViewPagerId;
    protected int mainFragmentViewId;
    protected int layerFragmentViewId;

    protected ViewPager mViewPager;

    protected FrameLayout layerFragmentLayout;

    protected void initialize() {

        fragmentManager = getSupportFragmentManager();
        pagerFragmentManager = getSupportFragmentManager();
        layerFragmentManager = getSupportFragmentManager();

        if(context == null)
            context = getApplicationContext();

        if(res == null)
            res = getResources();

        mainFragmentViewId = JR.id(MAIN_CONTENTS_PAGER_ID);
        layerFragmentViewId = JR.id(MAIN_CONTENT_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();

        // Set main area view
        setContentView(JR.layout(MAIN_LAYOUT_ID));

        mViewPager = (ViewPager) findViewById(JR.id(MAIN_CONTENTS_PAGER_ID));

        layerFragmentLayout = (FrameLayout) findViewById(JR.id(MAIN_CONTENT_ID));

        GBLog.v(TAG + "%s", "onCreate, bundle:" + savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mainViewPagerId = View.inflate(context, layoutResID, null).getId();
    }

    /**
     * Fragment UI handle
     *
     */

    @Override
    public void showFragment(BaseFragment fragment) {
        showFragment(fragment, false);
    }

    protected void showFragment(BaseFragment fragment, boolean addBackStep) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if(addBackStep)
            transaction.addToBackStack(null);

        if(fragment.getFragmentType().menuType.equals(FragmentType.MenuType.SUB_MENU)) {
            transaction.replace(layerFragmentViewId, fragment, fragment.getFragmentType().name());
            transaction.commit();
            fragmentManager.executePendingTransactions();
            layerFragmentLayout.setVisibility(View.VISIBLE);
        } else if(fragment.getFragmentType().menuType.equals(FragmentType.MenuType.TOP_MENU)) {
            transaction.commit();
            fragmentManager.executePendingTransactions();
            layerFragmentLayout.setVisibility(View.GONE);
        }

        GBLog.d(TAG + "showFragment, fragmentId:%s", fragment.getFragmentType());
    }

    @Override
    public void switchFragments(BaseFragment currentFragment,
                                BaseFragment newFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.remove(currentFragment);
        transaction.replace(layerFragmentViewId, newFragment, newFragment.getFragmentType().name());
        transaction.commit();

        GBLog.d(TAG + "switchFragments, currentFragment:%s - newFragment:%s", currentFragment.getFragmentType(), newFragment.getFragmentType());
    }

    @Override
    public void removeFragment(BaseFragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();

        GBLog.d(TAG + "removeFragment, fragmentId:%s", fragment.getFragmentType());
    }

    @Override
    public void popFragment() {
        fragmentManager.popBackStack();
    }

    protected void showLayerFragment(BaseFragment fragment) {

        FragmentTransaction transaction = layerFragmentManager.beginTransaction();
        transaction.replace(layerFragmentViewId, fragment, fragment.getFragmentType().name());
        transaction.commit();
    }

    protected void removeLayerFragment(BaseFragment fragment) {

        FragmentTransaction transaction = layerFragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }


    /**
     * Activity life cycle
     */

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    @Override
    protected void onResumeFragments() {
        // TODO Auto-generated method stub
        super.onResumeFragments();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent,
                                          int requestCode) {
        // TODO Auto-generated method stub
        super.startActivityFromFragment(fragment, intent, requestCode);
    }

    @Override
    public void onAttachFragment(android.app.Fragment fragment) {
        // TODO Auto-generated method stub
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }

}
