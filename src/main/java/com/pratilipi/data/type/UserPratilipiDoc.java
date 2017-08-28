package com.pratilipi.data.type;

import java.util.Date;
import java.util.List;

public interface UserPratilipiDoc {

	String getId();

	void setId( String userPratilipiId );
	
	Long getUserId();
	
	void setUserId( Long userId );
	
	Long getPratilipiId();
	
	void setPratilipiId( Long pratilipiId );

	
	Integer getRating();
	
	void setRating( Integer rating );
	
	String getReviewTitle();
	
	void setReviewTitle( String reviewTitle );
	
	String getReview();
	
	void setReview( String review );
	
	Date getReviewDate();
	
	void setReviewDate( Date reviewDate );

	
	Long getLikeCount();
		
	List<Long> getLikedByUserIds();
	
	void setLikedByUserIds( List<Long> likedByUserIds );
	
	Long getCommentCount();
	
	List<CommentDoc> getComments();
	
	void setComments( List<CommentDoc> comments );
	
}
