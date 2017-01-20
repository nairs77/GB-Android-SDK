package com.gebros.platform.listener;

import com.gebros.platform.auth.GBSession;
import com.gebros.platform.exception.GBException;


/**
 * Created by nairs77@joycity.com on 2016-05-04.
 */
public interface GBAuthListener {

    public void onSuccess(GBSession newSession);

    public void onFail(GBException e);

    public void onCancel(boolean isUserCancelled);

}
