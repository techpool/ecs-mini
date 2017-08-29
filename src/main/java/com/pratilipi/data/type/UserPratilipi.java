package com.pratilipi.data.type;

import java.util.Date;

import com.pratilipi.common.type.UserReviewState;

public interface UserPratilipi extends GenericOfyType {

	String getId();
	
	Long getUserId();
	
	void setUserId( Long userId );
	
	Long getPratilipiId();
	
	void setPratilipiId( Long pratilipiId );

	
	String getLastOpenedPage();
	
	void setLastOpenedPage( String lastOpenedPage );

	Date getLastOpenedDate();
	
	void setLastOpenedDate( Date lastOpenedDate );

	
	Integer getRating();
	
	void setRating( Integer rating );
	
	Date getRatingDate();
	
	void setRatingDate( Date ratingDate );

	
	String getReviewTitle();

	void setReviewTitle( String reviewTitle );

	String getReview();
	
	void setReview( String review );
	
	UserReviewState getReviewState();
	
	void setReviewState( UserReviewState reviewState );

	Date getReviewDate();
	
	void setReviewDate( Date reviewDate );

	
	Long getCommentCount();
	
	void setCommentCount( Long count );
	
	
	Boolean isAddedToLib();
	
	void setAddedToLib( Boolean addedToLib );

	Date getAddedToLibDate();
	
	void setAddedToLibDate( Date addedToLibDate );
	
	Date getTimestamp();
	
	void setTimestamp( Date timestamp );	
	
}
