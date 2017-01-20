package com.gebros.platform.auth;

import com.gebros.platform.auth.net.Response;

/**
 * JoypleObject callback interface that refers to ResponseCallback
 *
 * @author nairs77@joycity.com
 */

public interface ObjectCallback<T> {

	/**
	 *
	 * @param joypleObject
	 * @param response
     */
	abstract public void onComplete(T joypleObject, Response response);

	/**
	 *
	 * @param response
     */
	abstract public void onError(Response response);
}
