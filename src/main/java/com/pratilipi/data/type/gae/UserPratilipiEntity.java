package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
import com.pratilipi.common.type.UserReviewState;
import com.pratilipi.data.type.UserPratilipi;

@Cache
@Entity( name = "USER_PRATILIPI" )
public class UserPratilipiEntity implements UserPratilipi {
	
	@Id
	private String USER_PRATILIPI_ID;

	@Index
	private Long USER_ID;
	
	@Index
	private Long PRATILIPI_ID;

	
	@Index( IfNotNull.class )
	private String LAST_OPENED_PAGE;
	
	@Index( IfNotNull.class )
	private Date LAST_OPENED_DATE;

	@Index( IfNotNull.class )
	private Boolean ADDED_TO_LIB;
	
	@Index( IfNotNull.class )
	private Date ADDED_TO_LIB_DATE;

	
	@Index( IfNotNull.class )
	private Integer RATING;
	
	@Index( IfNotNull.class )
	private Date RATING_DATE;

	private String REVIEW_TITLE;

	private String REVIEW;
	
	@Index( IfNotNull.class )
	private UserReviewState REVIEW_STATE;
	
	@Index( IfNotNull.class )
	private Date REVIEW_DATE;

	
	@Index( IfNotNull.class )
	private Long COMMENT_COUNT;
	
	@Index
	private Date _TIMESTAMP_;	
	
	
	public UserPratilipiEntity() {}
	
	public UserPratilipiEntity( Long userId, Long pratilipiId ) {
		this.USER_PRATILIPI_ID = userId + "-" + pratilipiId;
		this.USER_ID = userId;
		this.PRATILIPI_ID = pratilipiId;
	}

	
	@Override
	public String getId() {
		return this.USER_PRATILIPI_ID;
	}

	public void setId( String id ) {
		this.USER_PRATILIPI_ID = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.USER_PRATILIPI_ID = key.getName();
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
	public Long getPratilipiId() {
		return PRATILIPI_ID;
	}
	
	@Override
	public void setPratilipiId( Long pratilipiId ) {
		this.PRATILIPI_ID = pratilipiId;
	}
	

	@Override
	public String getLastOpenedPage() {
		return LAST_OPENED_PAGE;
	}
	
	@Override
	public void setLastOpenedPage( String lastOpenedPage ) {
		this.LAST_OPENED_PAGE = lastOpenedPage;
	}

	@Override
	public Date getLastOpenedDate() {
		return LAST_OPENED_DATE;
	}
	
	@Override
	public void setLastOpenedDate( Date lastOpenedDate ) {
		this.LAST_OPENED_DATE = lastOpenedDate;
	}
	
	@Override
	public Boolean isAddedToLib() {
		return ADDED_TO_LIB == null ? false : ADDED_TO_LIB;
	}

	@Override
	public void setAddedToLib( Boolean addedToLib ) {
		this.ADDED_TO_LIB = addedToLib;
	}

	@Override
	public Date getAddedToLibDate() {
		return ADDED_TO_LIB_DATE;
	}
	
	@Override
	public void setAddedToLibDate( Date addedToLibDate ) {
		this.ADDED_TO_LIB_DATE = addedToLibDate;
	}


	@Override
	public Integer getRating() {
		return RATING;
	}
	
	@Override
	public void setRating( Integer rating ) {
		this.RATING = rating;
	}
	
	@Override
	public Date getRatingDate() {
		return RATING_DATE;
	}
	
	@Override
	public void setRatingDate( Date ratingDate ) {
		this.RATING_DATE = ratingDate;
	}
	
	@Override
	public String getReviewTitle() {
		return REVIEW_TITLE;
	}

	@Override
	public void setReviewTitle( String reviewTitle ) {
		this.REVIEW_TITLE = reviewTitle;
	}
	
	@Override
	public String getReview() {
		return REVIEW;
	}

	@Override
	public void setReview( String review ) {
		this.REVIEW = review;
	}
	
	@Override
	public UserReviewState getReviewState() {
		return REVIEW_STATE;
	}
	
	@Override
	public void setReviewState( UserReviewState reviewState ) {
		this.REVIEW_STATE = reviewState;
	}

	@Override
	public Date getReviewDate() {
		return REVIEW_DATE;
	}
	
	@Override
	public void setReviewDate( Date reviewDate ) {
		this.REVIEW_DATE = reviewDate;
	}
	

	@Override
	public Long getCommentCount() {
		return COMMENT_COUNT;
	}
	
	@Override
	public void setCommentCount( Long count ) {
		this.COMMENT_COUNT = count;
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
