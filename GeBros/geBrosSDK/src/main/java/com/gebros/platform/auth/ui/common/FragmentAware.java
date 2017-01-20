package com.gebros.platform.auth.ui.common;

/**
 * Created by jce_platform on 2016. 5. 30..
 */
public interface FragmentAware {

    public static final String DATA = "DATA";

    /**
     * This method called by BaseFragment on Fragment's resume cycle
     */

    abstract void fragmentOnStart(BaseFragment fragment);

    /**
     * This method add new Fragment based BaseFragment to current fragment manager
     * - supported method showFragment(prevFragment, newFragment)/switchFragment(prevFragment, newFragment) in BaseFragment
     */

    abstract void fragmentSwitch(BaseFragment currentFragment, BaseFragment newFragment);
    abstract void fragmentSwitch(BaseFragment currentFragment, BaseFragment newFragment, boolean backStep);

    /**
     * This method add to previous Fragment based BaseFragment with data
     * - supported method showFragment(prevFragment, newFragment)/switchFragment(prevFragment, newFragment) in BaseFragment
     */

    abstract void fragmentDataMove(BaseFragment currentFragment, BaseFragment newFragment, String data);
    abstract void fragmentDataMove(BaseFragment currentFragment, BaseFragment newFragment, String data, boolean backStep);

    /**
     * This method back to previous fragment based BaseFragment
     * - supported method popFragment() in BaseFragment
     */

    abstract void fragmentPushBack();

    /**
     * This method finish to current fragment based BaseFragment
     * - supported method removeFragment(fragment) in BaseFragment
     */

    abstract void fragmentFinish(BaseFragment fragment);

}
