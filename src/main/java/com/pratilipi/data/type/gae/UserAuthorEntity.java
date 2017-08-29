package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pratilipi.common.type.UserFollowState;
import com.pratilipi.data.type.UserAuthor;

@Cache
@Entity( name = "USER_AUTHOR" )
public class UserAuthorEntity implements UserAuthor {

	@Id
	private String USER_AUTHOR_ID;

	@Index
	private Long USER_ID;
	@Index
	private Long AUTHOR_ID;

	@Index
	private UserFollowState FOLLOW_STATE;
	@Index
	private Date FOLLOW_DATE;
	
	@Index
	private Date _TIMESTAMP_;	
	
	
	public UserAuthorEntity() {}
	
	public UserAuthorEntity( Long userId, Long authorId ) {
		this.USER_AUTHOR_ID = userId + "-" + authorId;
		this.USER_ID = userId;
		this.AUTHOR_ID = authorId;
	}

	
	@Override
	public String getId() {
		return this.USER_AUTHOR_ID;
	}
	
	public void setId( String id ) {
		this.USER_AUTHOR_ID = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.USER_AUTHOR_ID = key.getName();
	}
	
	
	@Override
	public Long getUserId() {
		return USER_ID;
	}
	
	@Override
	public void setUserId( Long userId ) {
		this.USER_ID = userId;
	}
	
	@Override
	public Long getAuthorId() {
		return AUTHOR_ID;
	}
	
	@Override
	public void setAuthorId( Long authorId ) {
		this.AUTHOR_ID = authorId;
	}
	
	
	@Override
	public UserFollowState getFollowState() {
		return FOLLOW_STATE;
	}

	@Override
	public void setFollowState( UserFollowState state ) {
		this.FOLLOW_STATE = state;
	}
	
	@Override
	public Date getFollowDate() {
		return FOLLOW_DATE;
	}
	
	@Override
	public void setFollowDate( Date date ) {
		this.FOLLOW_DATE = date;
	}
	
	@Override
	public Date getTimestamp() {
		return _TIMESTAMP_;
	}

	@Override
	public void setTimestamp( Date timestamp ) {
		this._TIMESTAMP_ = timestamp;
	}	

}
