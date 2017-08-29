package com.pratilipi.data.type.doc;

import java.util.Date;

import com.pratilipi.common.type.UserFollowState;
import com.pratilipi.data.type.UserAuthorDoc;

public class UserAuthorDocImpl implements UserAuthorDoc {

	private String id;
	private Long userId;
	private Long authorId;

	private UserFollowState followState;
	private Date followDate;

	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId( String userAuthorId ) {
		this.id = userAuthorId;
	}

	@Override
	public Long getUserId() {
		return userId;
	}

	@Override
	public void setUserId( Long userId ) {
		this.userId = userId;
	}

	@Override
	public Long getAuthorId() {
		return authorId;
	}

	@Override
	public void setAuthorId( Long authorId ) {
		this.authorId = authorId;
	}


	@Override
	public UserFollowState getFollowState() {
		return followState;
	}

	@Override
	public void setFollowState( UserFollowState followState ) {
		this.followState = followState;
	}

	@Override
	public Date getFollowDate() {
		return this.followDate;
	}

	@Override
	public void setFollowDate( Date date ) {
		this.followDate = date;
	}

}
