package com.gebros.platform.auth.ui.common;

/**
 * Created by jce_platform on 2016. 5. 30..
 */
public interface BaseFragmentSupport {
    /**
     * This method show new fragment that not supported back-step
     */

    void showFragment(BaseFragment fragment);

    /**
     * This method show new fragment that supported back-step
     * - back-step supported by popFragment()
     */

    void switchFragments(BaseFragment currentFragment, BaseFragment newFragment);

    /**
     * This method remove current fragment
     */

    void removeFragment(BaseFragment fragment);

    /**
     * This method push back previous fragment
     */

    void popFragment();
}
