package com.pratilipi.data.type;

import java.util.List;

public interface PratilipiReviewsDoc {

	Long getRatingCount();
	void setRatingCount( Long count );
	
	Long getTotalRating();
	void setTotalRating( Long totalRating );
	
	Long getReviewCount();
	
	List<UserPratilipiDoc> getReviews();
	void setReviews( List<UserPratilipiDoc> reviews );

}
