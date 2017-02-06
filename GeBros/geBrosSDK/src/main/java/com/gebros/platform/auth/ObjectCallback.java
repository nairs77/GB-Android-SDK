package com.gebros.platform.auth;

import com.gebros.platform.auth.net.Response;

/**
 * GBObject callback interface that refers to ResponseCallback
 *
 * @author gebros.nairs77@gmail.com
 */

public interface ObjectCallback<T> {

	/**
	 *
	 * @param gbObject
	 * @param response
     */
	abstract public void onComplete(T gbObject, Response response);

	/**
	 *
	 * @param response
     */
	abstract public void onError(Response response);
}
