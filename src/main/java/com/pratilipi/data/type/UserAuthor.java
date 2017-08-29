package com.pratilipi.data.type;

import java.util.Date;

import com.pratilipi.common.type.UserFollowState;

public interface UserAuthor extends GenericOfyType {

	String getId();
	
	
	Long getUserId();
	
	void setUserId( Long userId );
	
	Long getAuthorId();
	
	void setAuthorId( Long authorId );


	UserFollowState getFollowState();
	
	void setFollowState( UserFollowState state );

	Date getFollowDate();
	
	void setFollowDate( Date date );
	
	Date getTimestamp();
	
	void setTimestamp( Date timestamp );	
	
}
