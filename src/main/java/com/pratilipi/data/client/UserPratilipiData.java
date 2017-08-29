package com.pratilipi.data.client;

import java.util.Date;

import com.pratilipi.common.type.UserReviewState;

public class UserPratilipiData {

	private String userPratilipiId;
	
	private Long userId;

	@Deprecated
	private String userName;
	@Deprecated
	private String userImageUrl;
	@Deprecated
	private String userProfilePageUrl;
	
	private UserData user;
	
	private Long pratilipiId;
	
	
	private Integer rating;
	private boolean hasRating;
	
	private String review;
	private boolean hasReview;
	
	private UserReviewState reviewState;
	private boolean hasReviewState;
	
	private Long reviewDateMillis;

	
	private Long likeCount;
	private Long commentCount;
	
	
	private Boolean addedToLib;
	private boolean hasAddedToLib;

	private Boolean hasAccessToReview;
	
	private Boolean isLiked;


	
	public String getId() {
		return userPratilipiId;
	}
	
	public void setId( String id ) {
		this.userPratilipiId = id;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId( Long userId ) {
		this.userId = userId;
	}
	
	@Deprecated
	public String getUserName() {
		return userName;
	}

	@Deprecated
	public void setUserName( String userName ) {
		this.userName = userName;
	}

	@Deprecated
	public String getUserImageUrl() {
		return userImageUrl;
	}

	@Deprecated
	public void setUserImageUrl( String userImageUrl ) {
		this.userImageUrl = userImageUrl;
	}

	@Deprecated
	public String getUserProfilePageUrl() {
		return userProfilePageUrl;
	}

	@Deprecated
	public void setUserProfilePageUrl( String userProfilePageUrl ) {
		this.userProfilePageUrl = userProfilePageUrl;
	}

	public UserData getUser() {
		return user;
	}

	public void setUser( UserData user ) {
		this.user = user;
	}

	public Long getPratilipiId() {
		return pratilipiId;
	}

	public void setPratilipiId( Long pratilipiId ) {
		this.pratilipiId = pratilipiId;
	}

	
	public Integer getRating() {
		return rating;
	}

	public void setRating( Integer rating ) {
		this.rating = rating;
		this.hasRating = true;
	}
	
	public boolean hasRating() {
		return hasRating;
	}

	public String getReview() {
		return review;
	}

	public void setReview( String review ) {
		this.review = review;
		this.hasReview = true;
	}
	
	public boolean hasReview() {
		return hasReview;
	}

	public UserReviewState getReviewState() {
		return reviewState;
	}

	public void setReviewState( UserReviewState reviewState ) {
		this.reviewState = reviewState;
		this.hasReviewState = true;
	}
	
	public boolean hasReviewState() {
		return hasReviewState;
	}

	public Date getReviewDate() {
		return reviewDateMillis == null ? null : new Date( reviewDateMillis );
	}

	public void setReviewDate( Date reviewDate ) {
		this.reviewDateMillis = reviewDate == null ? null : reviewDate.getTime();
	}

	
	public Long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount( Long likeCount ) {
		this.likeCount = likeCount;
	}

	public Long getCommentCount() {
		return commentCount;
	}
	
	public void setCommentCount( Long count ) {
		this.commentCount = count;
	}
	
	
	public Boolean isAddedToLib() {
		return addedToLib == null ? false : addedToLib;
	}

	public void setAddedToLib( Boolean addedToLib ) {
		this.addedToLib = addedToLib;
		this.hasAddedToLib = true;
	}

	public boolean hasAddedToLib() {
		return hasAddedToLib;
	}
	
	public boolean hasAccessToReview() {
		return hasAccessToReview == null ? false : hasAccessToReview;
	}
	
	public void setAccessToReview( Boolean hasAccessToReview ) {
		this.hasAccessToReview = hasAccessToReview;
	}

	
	public boolean isLiked() {
		return isLiked == null ? false : isLiked;
	}

	public void setLiked( Boolean isLiked ) {
		this.isLiked = isLiked;
	}

}
