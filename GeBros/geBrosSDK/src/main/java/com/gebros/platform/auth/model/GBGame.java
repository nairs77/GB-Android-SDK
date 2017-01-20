package com.gebros.platform.auth.model;

import com.gebros.platform.auth.model.annotation.PropertyName;
import com.gebros.platform.auth.model.common.GBObject;

public interface GBGame extends GBObject {

	@PropertyName(value="blocked")
	public int getBlockType();
	
	@PropertyName(value="blocked_date")
	public long getBlockDate();
	
	@PropertyName(value="game_code")
	public int getGameCode();
	
	@PropertyName(value="game_quit")
	public int isQuitGame();
	
	@PropertyName(value="game_quit_date")
	public long getQuitDate();
	
	@PropertyName(value="last_logintime")
	public long getLastLoginTime();
	
	@PropertyName(value="device_type")
	public int getDeviceType();
}
