package com.pratilipi.data.client;

import java.util.Date;

import com.pratilipi.common.type.UserFollowState;

public class UserAuthorData {

	private String userAuthorId;
	
	private Long userId;
	
	private Long authorId;
	
	private UserFollowState followState;
	private boolean hasFollowState;
	
	private Long followDateMillis;

	
	public String getId() {
		return userAuthorId;
	}
	
	public void setId( String id ) {
		this.userAuthorId = id;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId( Long userId ) {
		this.userId = userId;
	}
	
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId( Long authorId ) {
		this.authorId = authorId;
	}

	
	public UserFollowState getFollowState() {
		return followState;
	}

	public void setFollowState( UserFollowState state ) {
		this.followState = state;
		this.hasFollowState = true;
	}
	
	public boolean hasFollowState() {
		return hasFollowState;
	}


	public Date getFollowDate() {
		return followDateMillis == null ? null : new Date( followDateMillis );
	}

	public void setFollowDate( Date followDate ) {
		followDateMillis = followDate == null ? null : followDate.getTime();
	}

}
