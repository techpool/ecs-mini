package com.pratilipi.data.type;

import java.util.Date;
import java.util.List;

public interface CommentDoc {
	
	Long getId();
	
	void setId( Long commentId );
	
	
	Long getUserId();
	
	void setUserId( Long userId );

	
	String getContent();
	
	void setContent( String content );
	
	Date getCreationDate();
	
	void setCreationDate( Date date );

	Date getLastUpdated();
	
	void setLastUpdated( Date date );

	
	Long getLikeCount();

	List<Long> getLikedByUserIds();
	
	void setLikedByUserIds( List<Long> likedByUserIds );
	
}
