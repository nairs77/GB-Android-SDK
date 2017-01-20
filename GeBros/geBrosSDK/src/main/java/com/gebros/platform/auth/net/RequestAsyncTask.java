package com.gebros.platform.auth.net;

import android.os.AsyncTask;

import com.gebros.platform.auth.model.common.JsonUtil;
import com.gebros.platform.exception.GBException;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.exception.GBRuntimeException;
import com.gebros.platform.log.GBLog;

import java.io.IOException;

/**
 * @author
 * 
 */

public class RequestAsyncTask extends AsyncTask<Void, Void, Response> {
	
	private String TAG = RequestAsyncTask.class.getCanonicalName() + ":";
	private Request mRequest;
	
	public RequestAsyncTask(Request request) {
		this.mRequest = request;
	}
	
	@Override
	protected Response doInBackground(Void... params) {
		Response mResponse;
		try {
			mResponse = mRequest.getResponse();
			GBLog.d(TAG + "%s", "doInBackground complete.");
		} catch (IOException e) {
			mResponse = new Response.Builder(mRequest, null)
			.responseCode(Response.HTTP_BAD_REQUEST)
			.state(JsonUtil.getResponseErrorTemplate(GBExceptionType.BAD_REQUEST))
			.exception(new GBException(GBExceptionType.CONNECTION_ERROR)).build();
		} catch (GBException e) {
			mResponse = new Response.Builder(mRequest, null)
			.state(JsonUtil.getResponseErrorTemplate(e.getExceptionType()))
			.exception(e).build();
		}
		return mResponse;
	}

	@Override
	protected void onPostExecute(Response response) {
		if(response.hasError()) {
			onFailed(response);
			return;
		}
		
		if(response.getStatus() == Response.API_ON_FAILED) {
			onFailed(response.setException(new GBException(GBExceptionType.SERVER_RESPONSE_FAILED)));
			return;
		}
		
		mRequest.onSuccess(response);
	}

	public void onFailed(Response response) {
		if(response == null)
			throw new GBRuntimeException(GBExceptionType.RESPONSE_NULL);
		
		mRequest.onFailed(response);
	}
}
