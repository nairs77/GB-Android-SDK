package com.gebros.platform.listener;

import com.gebros.platform.exception.GBException;

/**
 * Created by Joycity-Platform on 5/3/16.
 */
interface IListener {
    // TODO : Error Param
    public void onFail(GBException e);
}
