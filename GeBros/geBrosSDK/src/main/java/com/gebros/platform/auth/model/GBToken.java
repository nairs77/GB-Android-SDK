package com.gebros.platform.auth.model;


import com.gebros.platform.auth.model.annotation.InnerObject;
import com.gebros.platform.auth.model.annotation.PropertyName;
import com.gebros.platform.auth.model.common.GBObject;

@InnerObject("token_info")
public interface GBToken extends GBObject {
	
	@PropertyName(value="token")
	abstract public String getAccessToken();
	
	@PropertyName(value="refresh_token")
	abstract public String getRefreshToken();

	@PropertyName(value="token_reissued")
	abstract public int isReissued();
	
	@PropertyName(value="expiredtime")
	abstract public long getExpiresTime();
}
