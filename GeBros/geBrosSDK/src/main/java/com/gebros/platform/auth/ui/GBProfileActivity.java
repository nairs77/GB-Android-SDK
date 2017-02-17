package com.gebros.platform.auth.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gebros.platform.auth.ui.common.BaseFragment;
import com.gebros.platform.auth.ui.common.BaseFragmentPagerActivity;
import com.gebros.platform.auth.ui.common.FragmentAware;
import com.gebros.platform.auth.ui.common.FragmentType;
import com.gebros.platform.auth.ui.view.GBCustomerWebFragment;
import com.gebros.platform.auth.ui.view.GBEULAWebFragment;
import com.gebros.platform.auth.ui.view.GBProfileFragment;
import com.gebros.platform.auth.ui.view.GBSettingFragment;
import com.gebros.platform.internal.JR;
import com.gebros.platform.log.GBLog;

/**
 * Created by jce_platform on 2016. 5. 30..
 */
public class GBProfileActivity extends BaseFragmentPagerActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, FragmentAware {

    public final static String GB_PROFILE_VIEW_TYPE_KEY = "VIEW_TYPE";

    public final static int FRAGMENT_PAGE_1_INDEX = 0;
    public final static int FRAGMENT_PAGE_2_INDEX = 1;

    private int NUM_PAGES = 2;

    private RelativeLayout tabProfileBtn;
    private ImageView tabProfileOffIv;
    private RelativeLayout tabProfileOnLayout;
    private RelativeLayout tabSettingBtn;
    private ImageView tabSettingOffIv;
    private RelativeLayout tabSettingOnLayout;

    private BaseFragment profileFragment;
    private BaseFragment settingFragment;

    private BaseFragment currentFragment;
    private BaseFragment prevFragment;

    private RelativeLayout closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager.setAdapter(new pagerAdapter(pagerFragmentManager));
        mViewPager.setCurrentItem(FRAGMENT_PAGE_1_INDEX);
        mViewPager.setOffscreenPageLimit(NUM_PAGES);

        mViewPager.setOnPageChangeListener(this);

        closeBtn = (RelativeLayout) findViewById(JR.id("GB_main_close_btn"));
        closeBtn.setOnClickListener(this);

        tabProfileBtn = (RelativeLayout) findViewById(JR.id("GB_main_tab_profile_btn"));
        tabProfileOffIv = (ImageView) findViewById(JR.id("GB_main_tab_profile_off_iv"));
        tabProfileOnLayout = (RelativeLayout) findViewById(JR.id("GB_main_tab_profile_on_ly"));
        tabSettingBtn = (RelativeLayout) findViewById(JR.id("GB_main_tab_setting_btn"));
        tabSettingOffIv = (ImageView) findViewById(JR.id("GB_main_tab_setting_off_iv"));
        tabSettingOnLayout = (RelativeLayout) findViewById(JR.id("GB_main_tab_setting_on_ly"));

        tabProfileBtn.setOnClickListener(this);
        tabSettingBtn.setOnClickListener(this);

        // Set the above fragment
        currentFragment = profileFragment;
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            String extrasData = extras.getString(GB_PROFILE_VIEW_TYPE_KEY);
            if (GBProfileViewType.GBProfileEULA.name().equals(extrasData)) {
                currentFragment = settingFragment;
                mViewPager.setCurrentItem(FRAGMENT_PAGE_2_INDEX);
                fragmentDataMove(currentFragment, GBEULAWebFragment.newInstance(GBContentsAPI.GB_CLICKWRAP_WEB_URL), "GBSettingFragment");
            } else if (GBProfileViewType.GBProfileSettings.name().equals(extrasData)) {
                currentFragment = settingFragment;
                mViewPager.setCurrentItem(FRAGMENT_PAGE_2_INDEX);
            } else if (GBProfileViewType.GBProfileSupportCenter.name().equals(extrasData)) {
                currentFragment = settingFragment;
                mViewPager.setCurrentItem(FRAGMENT_PAGE_2_INDEX);

                fragmentDataMove(currentFragment, GBCustomerWebFragment.newInstance(GBContentsAPI.GB_CUSTOMER_WEB_URL), "JoycitySettingFragment");
            }  else if (GBProfileViewType.GBProfileUserInfo.name().equals(extrasData)) {
                currentFragment = profileFragment;
                mViewPager.setCurrentItem(FRAGMENT_PAGE_1_INDEX);
            }
        }

        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if(resId == tabProfileBtn.getId()) {
            mViewPager.setCurrentItem(FRAGMENT_PAGE_1_INDEX);
            profileFragment = GBProfileFragment.newInstance();
            initTopMenu(profileFragment);
            currentFragment = profileFragment;

            tabProfileOffIv.setVisibility(View.GONE);
            tabProfileOnLayout.setVisibility(View.VISIBLE);
            tabSettingOffIv.setVisibility(View.VISIBLE);
            tabSettingOnLayout.setVisibility(View.GONE);
        } else if(resId == tabSettingBtn.getId()) {
            mViewPager.setCurrentItem(FRAGMENT_PAGE_2_INDEX);
            settingFragment = GBProfileFragment.newInstance();
            initTopMenu(settingFragment);
            currentFragment = settingFragment;

            tabProfileOffIv.setVisibility(View.VISIBLE);
            tabProfileOnLayout.setVisibility(View.GONE);
            tabSettingOffIv.setVisibility(View.GONE);
            tabSettingOnLayout.setVisibility(View.VISIBLE);
        } else if (resId == closeBtn.getId()) {
            finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        switch(position) {
            case FRAGMENT_PAGE_1_INDEX:
                mViewPager.setCurrentItem(FRAGMENT_PAGE_1_INDEX);
                profileFragment = GBProfileFragment.newInstance();
                initTopMenu(profileFragment);
                currentFragment = profileFragment;

                tabProfileOffIv.setVisibility(View.GONE);
                tabProfileOnLayout.setVisibility(View.VISIBLE);
                tabSettingOffIv.setVisibility(View.VISIBLE);
                tabSettingOnLayout.setVisibility(View.GONE);
                break;
            case FRAGMENT_PAGE_2_INDEX:
                mViewPager.setCurrentItem(FRAGMENT_PAGE_2_INDEX);
                settingFragment = GBProfileFragment.newInstance();
                initTopMenu(settingFragment);
                currentFragment = settingFragment;

                tabProfileOffIv.setVisibility(View.VISIBLE);
                tabProfileOnLayout.setVisibility(View.GONE);
                tabSettingOffIv.setVisibility(View.GONE);
                tabSettingOnLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void fragmentOnStart(BaseFragment fragment) {
    }

    @Override
    public void fragmentFinish(BaseFragment fragment) {
        if(fragment.getFragmentType().getMenuType().equals(FragmentType.MenuType.LAYER))
            removeLayerFragment(fragment);
        else
            removeFragment(fragment);

        GBLog.d(TAG + "fragmentFinish(), fragment:%s", fragment.getFragmentType().name());
    }

    @Override
    public void fragmentPushBack() {
        popFragment();
    }

    @Override
    public void fragmentSwitch(BaseFragment currentFragment, BaseFragment newFragment) {
        moveFragment(currentFragment, newFragment, true);
    }

    @Override
    public void fragmentSwitch(BaseFragment currentFragment, BaseFragment newFragment, boolean backStep) {
        moveFragment(currentFragment, newFragment, backStep);
    }

    @Override
    public void fragmentDataMove(BaseFragment currentFragment, BaseFragment newFragment, String data) {
        GBLog.d(TAG + ",fragmentDataMove newFragment::::::::::::" + newFragment.getClass().getCanonicalName());

        Bundle arguments = new Bundle();
        arguments.putString(DATA, data);
        newFragment.setArguments(arguments);

        fragmentSwitch(currentFragment, newFragment);
    }

    @Override
    public void fragmentDataMove(BaseFragment currentFragment,
                                 BaseFragment newFragment, String data, boolean backStep) {
    }

    @Override
    public void popFragment() {

        super.popFragment();

        if(prevFragment == null)
            return;

        removeFragment(currentFragment);

        currentFragment = prevFragment;

        if(currentFragment.getFragmentType().getMenuType().equals(FragmentType.MenuType.TOP_MENU)) {
            pagerRefresh(currentFragment);
        }

        GBLog.d(TAG + ", popFragment(), removeFragment:%s, initTopMenu:%s", currentFragment.getFragmentType(), prevFragment.getFragmentType());
    }

    private void moveFragment(BaseFragment mFragment, BaseFragment newFragment, boolean backStep) {
        GBLog.d(TAG + ",moveFragment newFragment::::::::::::" + newFragment.getClass().getCanonicalName());
        FragmentType.MenuType type = newFragment.getFragmentType().menuType;

        if(type.equals(FragmentType.MenuType.TOP_MENU)) {

            switchFragments(mFragment, newFragment);

            pagerRefresh(newFragment);

            currentFragment = newFragment;
            if(backStep)
                prevFragment = mFragment;

        } else if(type.equals(FragmentType.MenuType.SUB_MENU)) {

            if(mFragment.getFragmentType().equals(FragmentType.EMAIL_ENROLL_FRAGMENT) ||
                    mFragment.getFragmentType().equals(FragmentType.ADDINFO_INFO_FRAGMENT)) {
                removeFragment(mFragment);
                showFragment(newFragment);
            } else {
                showFragment(newFragment, backStep);
            }

            currentFragment = newFragment;
            if(backStep)
                prevFragment = mFragment;

        } else if(type.equals(FragmentType.MenuType.LAYER)) {

            showLayerFragment(newFragment);

        }

        GBLog.d(TAG + ", moveFragment(), fragmentType:%s, menuType:%s, backstep:%s", newFragment.getFragmentType(), newFragment.getFragmentType().getMenuType(), "" + backStep);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popFragment();
    }

    private void pagerRefresh(BaseFragment fragment) {

        FragmentType type = fragment.getFragmentType();
        pagerAdapter adapter = new pagerAdapter(pagerFragmentManager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);

        if(type.equals(FragmentType.PROFILE_INFO_FRAGMENT)) {
            mViewPager.setCurrentItem(FRAGMENT_PAGE_1_INDEX);
        } else if(type.equals(FragmentType.SETTING_INFO_FRAGMENT)) {
            mViewPager.setCurrentItem(FRAGMENT_PAGE_2_INDEX);
        }

    }

    private void initTopMenu(BaseFragment fragment) {

        if(fragment.getFragmentType().menuType.equals(FragmentType.MenuType.LAYER))
            return;

        GBLog.d(TAG + ", initTopMenu():%s", fragment.getFragmentType());
    }

    private class pagerAdapter extends FragmentPagerAdapter {

        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch(position) {
                case FRAGMENT_PAGE_1_INDEX:
                    profileFragment = GBProfileFragment.newInstance();
                    return profileFragment;
                case FRAGMENT_PAGE_2_INDEX:
                    settingFragment = GBSettingFragment.newInstance();
                    return settingFragment;
                default:
                    profileFragment = GBProfileFragment.newInstance();
                    return profileFragment;
            }

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

}
