package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
import com.pratilipi.common.type.CommentParentType;
import com.pratilipi.common.type.CommentState;
import com.pratilipi.common.type.ReferenceType;
import com.pratilipi.data.type.Comment;

@Cache
@Entity( name = "COMMENT" )
public class CommentEntity implements Comment {

	@Id
	private Long COMMENT_ID;
	
	
	@Index
	private Long USER_ID;
	
	@Index
	private CommentParentType PARENT_TYPE;
	
	@Index
	private String PARENT_ID;
	
	@Index
	private ReferenceType REFERENCE_TYPE;
	
	@Index
	private String REFERENCE_ID;
	
	
	private String CONTENT;
	
	@Index
	private CommentState STATE;
	
	
	@Index
	private Date CREATION_DATE;

	@Index( IfNotNull.class )
	private Date LAST_UPDATED;

	

	public CommentEntity() {}

	public CommentEntity( Long id ) {
		this.COMMENT_ID = id;
	}

	
	@Override
	public Long getId() {
		return COMMENT_ID;
	}
	
	public void setId( Long id ) {
		this.COMMENT_ID = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.COMMENT_ID = key.getId();
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
	public CommentParentType getParentType() {
		return PARENT_TYPE;
	}

	@Override
	public void setParentType( CommentParentType parentType ) {
		this.PARENT_TYPE = parentType;
	}

	@Override
	public String getParentId() {
		return PARENT_ID;
	}

	@Override
	public Long getParentIdLong() {
		return Long.parseLong( PARENT_ID );
	}

	@Override
	public void setParentId( Long parentId ) {
		this.PARENT_ID = parentId.toString();
	}

	@Override
	public void setParentId( String parentId ) {
		this.PARENT_ID = parentId;
	}

	@Override
	public ReferenceType getReferenceType() {
		return REFERENCE_TYPE;
	}

	@Override
	public void setReferenceType( ReferenceType referenceType ) {
		this.REFERENCE_TYPE = referenceType;
	}

	@Override
	public String getReferenceId() {
		return REFERENCE_ID;
	}

	@Override
	public Long getReferenceIdLong() {
		return Long.parseLong( REFERENCE_ID );
	}

	@Override
	public void setReferenceId( Long parentId ) {
		this.REFERENCE_ID = parentId.toString();
	}

	@Override
	public void setReferenceId( String parentId ) {
		this.REFERENCE_ID = parentId;
	}

	
	@Override
	public String getContent() {
		return CONTENT;
	}

	@Override
	public void setContent( String content ) {
		this.CONTENT = content;
	}
	
	@Override
	public CommentState getState() {
		return STATE;
	}
	
	@Override
	public void setState( CommentState state ) {
		this.STATE = state;
	}
	
	
	@Override
	public Date getCreationDate() {
		return CREATION_DATE;
	}
	
	@Override
	public void setCreationDate( Date creationDate ) {
		this.CREATION_DATE = creationDate;
	}

	@Override
	public Date getLastUpdated() {
		return LAST_UPDATED;
	}

	@Override
	public void setLastUpdated( Date lastUpdated ) {
		this.LAST_UPDATED = lastUpdated;
	}

}
