package com.gebros.platform.auth.model;

import com.gebros.platform.auth.model.annotation.PropertyName;
import com.gebros.platform.auth.model.common.GBObject;

import java.util.List;

public interface GBFriends extends GBObject {

	public static final int FRIENDS = 0;
	public static final int DELETE = 1;
	public static final int BLOCK = 2;
	
	@PropertyName("friends_list")
	public List<GBFriendsInfo> getAllFriends();
	
	@PropertyName("friends_game")
	public List<GBFriendsInfo> getGameFriends();
	
	@PropertyName("friends_recommend")
	public List<GBFriendsInfo> getRecommendUsers();
}
