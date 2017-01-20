package com.gebros.platform.listener;

import com.gebros.platform.exception.GBException;

import org.json.JSONObject;

/**
 * Created by Joycity-Platform on 6/22/16.
 */
public interface GBFriendsListener {

    public void onSuccess(JSONObject object);

    public void onFail(GBException e);
}
