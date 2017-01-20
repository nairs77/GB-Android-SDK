package com.gebros.platform.auth;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.gebros.platform.auth.net.Response;

public abstract class ResponseCallback extends Handler {

	/**
	 * API resources callback interface that refers to JSON Object
	 */
	
	private static final int SUCCESS = 1;
	private static final int FAILED = 0;
	
	private Context context;

	public ResponseCallback() {}
	
//	public ResponseCallback(Context context) {
//		this.context = context;
//	}
//
//	public Context getContext() {
//		return this.context;
//	}

	public void handleMessage(Message msg) {
		
		switch (msg.what) {
			case FAILED:
				onError((Response) msg.obj);
				break;
			case SUCCESS:
				onComplete((Response) msg.obj);
				break;
		}
	}
	
	abstract public void onComplete(Response response);
	abstract public void onError(Response response);
}
