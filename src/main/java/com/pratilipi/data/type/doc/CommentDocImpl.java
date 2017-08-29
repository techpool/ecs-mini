package com.pratilipi.data.type.doc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pratilipi.data.type.CommentDoc;

public class CommentDocImpl implements CommentDoc {
	
	private Long id;
	private Long userId;
	
	private String content;
	private Long creationDate;
	private Long lastUpdated;
	
	private List<Long> likedByUserIds;
	
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId( Long commentId ) {
		this.id = commentId;
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
	public String getContent() {
		return content;
	}

	@Override
	public void setContent( String content ) {
		this.content = content;
	}
	
	@Override
	public Date getCreationDate() {
		return creationDate == null ? null : new Date( creationDate );
	}

	@Override
	public void setCreationDate( Date date ) {
		this.creationDate = date == null ? null : date.getTime();
	}

	@Override
	public Date getLastUpdated() {
		return lastUpdated == null ? null : new Date( lastUpdated );
	}

	@Override
	public void setLastUpdated( Date date ) {
		this.lastUpdated = date == null ? null : date.getTime();
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
	
}