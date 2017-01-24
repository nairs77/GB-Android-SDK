package com.gebros.platform.auth;

import com.gebros.platform.auth.net.Response;

/**
 * GBObject callback interface that refers to ResponseCallback
 *
 * @author nairs77@joycity.com
 */

public interface ObjectCallback<T> {

	/**
	 *
	 * @param GBObject
	 * @param response
     */
	abstract public void onComplete(T GBObject, Response response);

	/**
	 *
	 * @param response
     */
	abstract public void onError(Response response);
}
