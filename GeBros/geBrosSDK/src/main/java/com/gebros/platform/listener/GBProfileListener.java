package com.gebros.platform.listener;

import com.gebros.platform.exception.GBException;

import org.json.JSONObject;

/**
 * Created by gebros.nairs77@gmail.com on 2016-05-15.
 */

public interface GBProfileListener {

    public void onSuccess(JSONObject object);

    public void onFail(GBException e);
}
