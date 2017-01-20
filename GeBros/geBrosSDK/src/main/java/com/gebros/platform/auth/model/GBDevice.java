package com.gebros.platform.auth.model;


import com.gebros.platform.auth.model.annotation.PropertyName;
import com.gebros.platform.auth.model.common.GBObject;

public interface GBDevice extends GBObject {

	@PropertyName(value="idx")
	public int getIdx();
	
	/**
	 * 1: Android
	 * 2: iOS
	 * 3: iPad
	 */
	
	@PropertyName(value="device_type")
	public int getDeviceType();
	
	@PropertyName(value="phone_number")
	public String getPhoneNumber();
	
	@PropertyName(value="uid")
	public String getUDID();
}
