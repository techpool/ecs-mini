package com.pratilipi.data.client;

import java.util.Date;

import com.pratilipi.common.type.CommentParentType;
import com.pratilipi.common.type.CommentState;
import com.pratilipi.common.type.ReferenceType;

public class CommentData {

	private Long commentId;
	
	private Long userId;
	private UserData user;
	
	private CommentParentType parentType;
	private String parentId;

	private ReferenceType referenceType;
	private String referenceId;
	
	private String content;
	private boolean hasContent;
	
	private CommentState state;
	private boolean hasState;
	
	private Long creationDateMillis;
	private Long lastUpdatedMillis;

	private Long likeCount;
	
	private Boolean hasAccessToUpdate;
	
	private Boolean isLiked;
	

	public CommentData() {}

	public CommentData( Long id ) {
		this.commentId = id;
	}

	
	public Long getId() {
		return commentId;
	}
	
	public void setId( Long id ) {
		this.commentId = id;
	}
	
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId( Long userId ) {
		this.userId = userId;
	}

	public UserData getUser() {
		return user;
	}
	
	public void setUser( UserData user ) {
		this.user = user;
	}

	
	public CommentParentType getParentType() {
		return parentType;
	}

	public void setParentType( CommentParentType parentType ) {
		this.parentType = parentType;
	}

	public String getParentId() {
		return parentId;
	}

	public Long getParentIdLong() {
		return Long.parseLong( parentId );
	}

	public void setParentId( Long parentId ) {
		this.parentId = parentId.toString();
	}

	public void setParentId( String parentId ) {
		this.parentId = parentId;
	}

	
	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType( ReferenceType referenceType ) {
		this.referenceType = referenceType;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public Long getReferenceIdLong() {
		return Long.parseLong( referenceId );
	}

	public void setReferenceId( Long referenceId ) {
		this.referenceId = referenceId.toString();
	}

	public void setReferenceId( String referenceId ) {
		this.referenceId = referenceId;
	}

	
	public String getContent() {
		return content;
	}

	public void setContent( String content ) {
		this.content = content;
		this.hasContent = true;
	}
	
	public boolean hasContent() {
		return hasContent;
	}
	
	public CommentState getState() {
		return state;
	}
	
	public void setState( CommentState state ) {
		this.state = state;
		this.hasState = true;
	}
	
	public boolean hasState() {
		return hasState;
	}
	
	
	public Date getCreationDate() {
		return creationDateMillis == null ? null : new Date( creationDateMillis );
	}
	
	public void setCreationDate( Date creationDate ) {
		this.creationDateMillis = creationDate == null ? null : creationDate.getTime();
	}

	public Date getLastUpdated() {
		return lastUpdatedMillis == null ? null : new Date( lastUpdatedMillis );
	}

	public void setLastUpdated( Date lastUpdated ) {
		this.lastUpdatedMillis = lastUpdated == null ? null : lastUpdated.getTime();
	}
	

	public Long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount( Long likeCount ) {
		this.likeCount = likeCount;
	}

	
	public boolean hasAccessToUpdate() {
		return hasAccessToUpdate == null ? false : hasAccessToUpdate;
	}
	
	public void setAccessToUpdate( Boolean hasAccessToUpdate ) {
		this.hasAccessToUpdate = hasAccessToUpdate;
	}

	
	public boolean isLiked() {
		return isLiked == null ? false : isLiked;
	}

	public void setLiked( Boolean isLiked ) {
		this.isLiked = isLiked;
	}
	
}
