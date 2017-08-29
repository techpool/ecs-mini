package com.pratilipi.data.type.doc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pratilipi.data.type.CommentDoc;
import com.pratilipi.data.type.UserPratilipiDoc;

public class UserPratilipiDocImpl implements UserPratilipiDoc {
	
	private String id;
	
	private Long userId;
	private Long pratilipiId;
	
	private Integer rating;
	private String reviewTitle;
	private String review;
	private Long reviewDate;
	
	private List<Long> likedByUserIds;
	private List<CommentDocImpl> comments;
	
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId( String id ) {
		this.id = id;
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
	public Long getPratilipiId() {
		return pratilipiId;
	}

	@Override
	public void setPratilipiId( Long pratilipiId ) {
		this.pratilipiId = pratilipiId;
	}
	
	
	@Override
	public Integer getRating() {
		return rating;
	}
	
	@Override
	public void setRating( Integer rating ) {
		this.rating = rating;
	}
	
	@Override
	public String getReviewTitle() {
		return reviewTitle;
	}

	@Override
	public void setReviewTitle( String reviewTitle ) {
		this.reviewTitle = reviewTitle;
	}
	
	@Override
	public String getReview() {
		return review;
	}

	@Override
	public void setReview( String review ) {
		this.review = review;
	}
	
	@Override
	public Date getReviewDate() {
		return reviewDate == null ? null : new Date( reviewDate );
	}

	@Override
	public void setReviewDate( Date reviewDate ) {
		this.reviewDate = reviewDate == null ? null : reviewDate.getTime();
	}

	
	@Override
	public Long getLikeCount() {
		return likedByUserIds == null ? 0L : (long) likedByUserIds.size();
	}

	@Override
	public List<Long> getLikedByUserIds() {
		return likedByUserIds == null ? new ArrayList<Long>( 0 ) : likedByUserIds;
	}
	
	@Override
	public void setLikedByUserIds( List<Long> likedByUserIds ) {
		this.likedByUserIds = likedByUserIds == null || likedByUserIds.size() == 0 ? null : likedByUserIds;
	}

	@Override
	public Long getCommentCount() {
		return comments == null ? 0L : (long) comments.size();
	}

	@Override
	public List<CommentDoc> getComments() {
		return comments == null
				? new ArrayList<CommentDoc>( 0 )
				: new ArrayList<CommentDoc>( comments );
	}
	
	@Override
	public void setComments( List<CommentDoc> comments ) {
		if( comments == null || comments.size() == 0 ) {
			this.comments = null;
		} else {
			this.comments = new ArrayList<>( comments.size() );
			for( CommentDoc commentDoc : comments )
				this.comments.add( (CommentDocImpl) commentDoc );
		}
	}
	
}