package com.gebros.platform.auth.model;

import com.gebros.platform.auth.model.annotation.PropertyName;
import com.gebros.platform.auth.model.common.GBObject;

import java.util.List;

public interface GBProfile extends GBObject {

	@PropertyName("user_info")
	public GBUser getUserInfo();
	
	@PropertyName("services")
	public List<GBService> getServices();
	
	@PropertyName("devices")
	public List<GBDevice> getDevices();
	
	@PropertyName("games")
	public List<GBGame> getGames();
}
