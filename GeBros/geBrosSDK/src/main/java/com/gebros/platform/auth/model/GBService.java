package com.gebros.platform.auth.model;


import com.gebros.platform.auth.model.annotation.PropertyName;
import com.gebros.platform.auth.model.common.GBObject;

public interface GBService extends GBObject {
	
	@PropertyName(value="service_type")
	public String getServiceType();
	
	@PropertyName(value="service_id")
	public String getServiceId();
		
	@PropertyName(value="status")
	public int isConnected();
}
