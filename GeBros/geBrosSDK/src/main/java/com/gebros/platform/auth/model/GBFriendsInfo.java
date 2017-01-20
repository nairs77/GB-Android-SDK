package com.gebros.platform.auth.model;


import com.gebros.platform.auth.model.annotation.PropertyName;
import com.gebros.platform.auth.model.common.GBObject;

public interface GBFriendsInfo extends GBObject {

	@PropertyName(value="idx")
	public int getIdx();
	public void setIdx(int idx);
	
	@PropertyName(value="f_userkey")
	public int getUserKey();
	
	/**
	 * 0: 주소록 기반 친구 관계
	 * 1: 게스트 로그인을 통한 관계  *joyple
	 * 2: 네스트 친구 관계
	 * 3: 이메일 계정 친구 관계 *joyple
	 * 4: 페이스북 친구 관계
	 */
	
	@PropertyName(value="f_type")
	public int getType();
	
	@PropertyName(value="nickname")
	public String getNickName();
	
	@PropertyName(value="profile_img")
	public String getProfileImagePath();
	
	@PropertyName(value="greeting_msg")
	public String getGreetingMessage();
	
	@PropertyName(value="regdate")
	public long getRegDate();
	
	@PropertyName(value="join_type")
	public int getJoinType();
	
	@PropertyName(value="join_date")
	public long getJoinDate();
	
	@PropertyName(value="status")
	public String getStatus();
	public void setStatus(String status);
}
