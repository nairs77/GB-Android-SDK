package com.gebros.platform.auth.ui.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.gebros.platform.exception.GBRuntimeException;
import com.gebros.platform.internal.JR;
import com.gebros.platform.log.GBLog;

/**
 * Created by jce_platform on 2016. 5. 30..
 */
public class BaseFragment extends Fragment {

    protected String TAG = this.getClass().getName() + ":";
    protected FragmentActivity activity;
    protected Resources res;

    protected FragmentAware fragmentAware;
    protected FragmentType fragmentType;

    protected int titleRes;
    protected int layoutId;
    protected View rootView;

    private ProgressDialog pd;

    protected void initialize() {

        if(activity == null)
            activity = getActivity();

        if(res == null)
            res = getResources();
    }

    public FragmentType getFragmentType() {
        return fragmentType;
    }

    public int getTitleRes() {
        return titleRes;
    }

    protected void showProgress() {
        pd = new ProgressDialog(activity);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pd.show();
        pd.setContentView(JR.layout("custom_progress"));
    }

    protected void hideProgress() {
        if(pd == null) {
            return;
        }
        try {
            if(pd.isShowing()) {
                pd.dismiss();
            }

            return;
        } catch(Exception e) {
            GBLog.d(TAG + " dismiss error : "+e);
        }
    }

    protected void finish() {
        fragmentAware.fragmentFinish(this);
    }

    protected void switchFragment(BaseFragment currentFragment, BaseFragment newFragment) {
        fragmentAware.fragmentSwitch(currentFragment, newFragment);
    }

    protected void back() {
        fragmentAware.fragmentPushBack();
    }

    protected void hideSoftKeyboard(View view) {

        InputMethodManager mgr = (InputMethodManager) (activity.getSystemService(Context.INPUT_METHOD_SERVICE));
        mgr.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void showSoftkeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager) (activity.getSystemService(Context.INPUT_METHOD_SERVICE));
        mgr.showSoftInput(view, 0);
    }

    /**
     *	Activity cycle
     */

    @Override
    @SuppressWarnings("deprecation")
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        try {

            fragmentAware = (FragmentAware) activity;

        } catch (ClassCastException e) {
            GBLog.e(e, "%s", e.getMessage());
        }

        GBLog.v(TAG + "%s", "onAttach");
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = null;

        if (context instanceof Activity) {
            activity = (Activity)context;
        }

        try {

            fragmentAware = (FragmentAware) activity;
        } catch (NullPointerException e) {
            GBLog.e(e, "%s", e.getMessage());
        } catch (ClassCastException e) {
            GBLog.e(e, "%s", e.getMessage());
        }

        GBLog.v(TAG + "%s", "onAttach");
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        initialize();

        GBLog.v(TAG + "%s", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(layoutId == 0)
            throw new GBRuntimeException("BaseFragment's layout resource not exists.");

        rootView = inflater.inflate(layoutId, container, false);
        GBLog.v(TAG + "%s", "onCreateView");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GBLog.v(TAG + "%s", "onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        GBLog.v(TAG + "%s", "onActivityCreated:saveInstanceState:" + saveInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        GBLog.v(TAG + "%s", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        fragmentAware.fragmentOnStart(this);
        GBLog.v(TAG + "%s", "onResume");
    }

    /**
     * Destroy cycle
     */

    @Override
    public void onPause() {
        super.onPause();
        GBLog.v(TAG + "%s", "onPause");
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        GBLog.v(TAG + "%s", "onSaveInstanceState:" + saveInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        GBLog.v(TAG + "%s", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GBLog.v(TAG + "%s", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GBLog.v(TAG + "%s", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        GBLog.v(TAG + "%s", "onDetach");
    }
}