package com.gebros.platform.auth.model;

import com.gebros.platform.auth.model.annotation.InnerObject;
import com.gebros.platform.auth.model.annotation.PropertyName;
import com.gebros.platform.auth.model.common.GBObject;

@InnerObject(value="user_info")
public interface GBUser extends GBObject {

	@PropertyName(value="userkey")
	public String getUserKey();
	
	@PropertyName(value="nickname")
	public String getNickName();
	
	@PropertyName(value="email_cert")
	public int isEmailCert();
	
	@PropertyName(value="profile_img")
	public String getProfileImagePath();
	
	@PropertyName(value="profile_thumb_img")
	public String getProfileThumbImagePath();
	
	@PropertyName(value="greeting_msg")
	public String getGreetingMessage();
	
	/**
	 * 1: Guest
	 * 2: Nest
	 * 3: Email
	 * 4: Facebook
	 */
	
	@PropertyName(value="join_type")
	public int getJoinType();
	
	@PropertyName(value="join_date")
	public long getJoinDate();
	
	@PropertyName(value="quit")
	public int isQuit();
	
	@PropertyName(value="quit_date")
	public long getQuitDate();
	
	@PropertyName(value="blocked")
	public int isBlocked();	
	
	@PropertyName(value="blocked_date")
	public long getBlockDate();
	
	@PropertyName(value="policy_agree")
	public int isAgreement();
	
	@PropertyName(value="phone_cert")
	public int isPhoneCert();
	
	@PropertyName(value="birthday")
	public String getBirthday();
	
	@PropertyName(value="gender")
	public String getGender();
	
	@PropertyName(value="device_collect_state")
	public int isDeviceCollectState();
}
