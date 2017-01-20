package com.gebros.platform.net;

import com.gebros.platform.net.AbstractRequest.Builder;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AsyncRequestRunner extends RequestRunner<String> {

	private ExecutorService executor = Executors.newCachedThreadPool();
	
	public AsyncRequestRunner(Builder builder) {
		super(builder);
	}

	@Override
	public void call(ResponseListener<String> listener) {
		AsyncHttpClient asyncRequest = new AsyncHttpClient(request, null);
		executor.execute(asyncRequest);
	}

	@Override
	public String get() {
		return null;
	}

	private class AsyncHttpClient implements Runnable {

		private String url;
		private HttpMethod method;
		private Map<String, Object> parameters;
		private boolean isHttpPostBodyJson;
		private ResponseListener<String> listener;
		
		AsyncHttpClient(AbstractRequest request, ResponseListener<String> listener) {
			this.url = request.getUrl();
			this.method = request.getMethod();
			this.parameters = request.getParameters();
			this.isHttpPostBodyJson = request.enableJsonBody();
			this.listener = listener;
		}
		
		@Override
		public void run() {
			OKHttpTemplate template = OkHttpFactory.create(method, url);;
			
			Set<String> keys = parameters.keySet();
			for(String key : keys) {
				template.addParameter(key, parameters.get(key));
			}
			
			if(isHttpPostBodyJson)
				template.enableJsonBody();
			
			try {
				String response = template.getResponse();
				listener.onComplete(response);
			} catch (IOException e) {
				listener.onException(new OkHttpException(e.getMessage()));
			}
		}
	}
}
