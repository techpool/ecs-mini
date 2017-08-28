package com.pratilipi.data.type;

import java.util.Date;

import com.pratilipi.common.type.UserFollowState;

public interface UserAuthorDoc {

	String getId();

	void setId( String userAuthorId );

	Long getUserId();

	void setUserId( Long userId );

	Long getAuthorId();

	void setAuthorId( Long authorId );


	UserFollowState getFollowState();
	
	void setFollowState( UserFollowState state );

	Date getFollowDate();
	
	void setFollowDate( Date date );

}
