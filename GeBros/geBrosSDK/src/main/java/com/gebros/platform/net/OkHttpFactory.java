package com.gebros.platform.net;



class OkHttpFactory {
	
	static OKHttpTemplate create(HttpMethod method, String url) {
		
		if(method.equals(HttpMethod.GET)) {
			return new HttpGet(url);
		} else
			return new HttpPost(url);
	}
}
