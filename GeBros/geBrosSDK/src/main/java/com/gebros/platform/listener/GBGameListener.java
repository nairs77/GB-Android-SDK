package com.gebros.platform.listener;


import com.gebros.platform.exception.GBException;

/**
 * Created by jce_platform on 2016. 7. 22..
 */
public interface GBGameListener {
    public void onSuccess();

    public void onFail(GBException e);
}
