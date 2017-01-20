package com.gebros.platform.net;

import com.gebros.platform.concurrent.ManagedAsyncTask;
import com.gebros.platform.log.GBLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author nairs77@joycity.com
 *
 */
public class JSONRequestRunner extends RequestRunner<JSONObject> {

	// TODO - Create Executors Factory
	private static final String TAG = JSONRequestRunner.class.getCanonicalName();


	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public JSONRequestRunner(AbstractRequest.Builder builder) {
		super(builder);
	}
	
	@Override
	public void call(ResponseListener<JSONObject> listener) {
		AsyncHttpClient asyncRequest = new AsyncHttpClient(request, (ResponseListener<JSONObject>) listener);
		executor.execute(asyncRequest);
	}

	@Override
	public JSONObject get() {
		SyncHttpClient syncRequest = new SyncHttpClient(request, null);
		try {
			return syncRequest.execute().get();
		} catch (InterruptedException e) {

		} catch (ExecutionException e) {

		}

		return null;
	}
	
	private class AsyncHttpClient implements Runnable {
		private String url;
		private HttpMethod method;
		private Map<String, Object> parameters;
		private boolean isHttpPostBodyJson;
		private ResponseListener<JSONObject> listener;
		
		AsyncHttpClient(AbstractRequest request, ResponseListener<JSONObject> listener) {
			this.url = request.getUrl();
			this.method = request.getMethod();
			this.parameters = request.getParameters();
			this.isHttpPostBodyJson = request.enableJsonBody();
			this.listener = listener;
		}
		
		@Override
		public void run() {
			OKHttpTemplate template = OkHttpFactory.create(method, url);
			Set<String> keys = parameters.keySet();
			for(String key : keys) {
				template.addParameter(key, parameters.get(key));
			}
			
			if(isHttpPostBodyJson)
				template.enableJsonBody();
			
			try {
				String response = template.getResponse();
				JSONObject object = new JSONObject(response);
				listener.onComplete(object);
			} catch (IOException e) {
				listener.onException(new OkHttpException(e.getMessage()));
			} catch (JSONException e) {
				listener.onException(new OkHttpException(e.getMessage()));
			}
		}
	}

	private class SyncHttpClient extends ManagedAsyncTask<Void, Void, JSONObject> {

		private String url;
		private HttpMethod method;
		private Map<String, Object> parameters;
		private boolean isHttpPostBodyJson;
		private ResponseListener<String> listener;

		SyncHttpClient(AbstractRequest request, ResponseListener<String> listener) {
			this.url = request.getUrl();
			this.method = request.getMethod();
			this.parameters = request.getParameters();
			this.isHttpPostBodyJson = request.enableJsonBody();
			this.listener = listener;
		}

		@Override
		public JSONObject doInBackground(Void... params) {
			OKHttpTemplate template = OkHttpFactory.create(method, url);;

			Set<String> keys = parameters.keySet();
			for(String key : keys) {
				template.addParameter(key, parameters.get(key));
			}

			if(isHttpPostBodyJson)
				template.enableJsonBody();

			try {
				String response = template.getResponse();
				return new JSONObject(response);
			} catch (IOException e) {
				return null;
			} catch (JSONException e) {
				return null;
			}
		}

		@Override
		public void onPostExecuteInternal(JSONObject object) {
			// run UI Thread
			GBLog.d(TAG + "ManagedAsync Internal!!");
		}
	}
}
