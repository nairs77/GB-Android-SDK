package com.gebros.platform.auth.model.local;

import com.gebros.platform.auth.model.GBFriends;
import com.gebros.platform.auth.model.GBFriendsInfo;
import com.gebros.platform.auth.model.GBProfile;
import com.gebros.platform.auth.model.GBUsers;
import com.gebros.platform.auth.model.common.GBObject;

import java.util.List;

public class GBData {

	private GBProfile profile;
	private GBFriends friends;
	private List<GBFriendsInfo> searchedUsers;
	
	private static final class GBDataHolder {
		public static final GBData instance = new GBData();
	}
	
	public static GBData getInstance() {
		return GBDataHolder.instance;
	}
	
	public <T extends GBObject> void castAndStoreGBObject(T GBObject) {
		if(GBObject instanceof GBProfile)
			profile = (GBProfile) GBObject;
		else if(GBObject instanceof GBFriends)
			friends = (GBFriends) GBObject;
		else if(GBObject instanceof GBUsers)
			searchedUsers = ((GBUsers) GBObject).getSearchedUsers();
	}
	
	public GBProfile getProfile() {
		return profile;
	}
	
	public GBFriends getFriends() {
		return friends;
	}
	
	public List<GBFriendsInfo> getSearchedUsers() {
		return searchedUsers;
	}
}
