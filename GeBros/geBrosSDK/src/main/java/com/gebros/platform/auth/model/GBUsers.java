package com.gebros.platform.auth.model;


import com.gebros.platform.auth.model.annotation.PropertyName;
import com.gebros.platform.auth.model.common.GBObject;

import java.util.List;

public interface GBUsers extends GBObject {

	@PropertyName("user_list")
	public List<GBFriendsInfo> getSearchedUsers();
}
