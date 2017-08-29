package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
import com.pratilipi.common.type.ReferenceType;
import com.pratilipi.common.type.VoteParentType;
import com.pratilipi.common.type.VoteType;
import com.pratilipi.data.type.Vote;

@Cache
@Entity( name = "VOTE" )
public class VoteEntity implements Vote {

	@Id
	private String VOTE_ID;
	
	
	@Index
	private Long USER_ID;
	
	@Index
	private VoteParentType PARENT_TYPE;
	
	@Index
	private String PARENT_ID;
	
	@Index
	private ReferenceType REFERENCE_TYPE;
	
	@Index
	private String REFERENCE_ID;
	
	
	@Index( IfNotNull.class )
	private VoteType TYPE;
	
	
	@Index
	private Date CREATION_DATE;

	@Index( IfNotNull.class )
	private Date LAST_UPDATED;

	

	public VoteEntity() {}

	public VoteEntity( String id ) {
		this.VOTE_ID = id;
	}

	
	@Override
	public String getId() {
		return VOTE_ID;
	}
	
	public void setId( String id ) {
		this.VOTE_ID = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.VOTE_ID = key.getName();
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
	public VoteParentType getParentType() {
		return PARENT_TYPE;
	}
	
	@Override
	public void setParentType( VoteParentType parentType ) {
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
	public VoteType getType() {
		return TYPE;
	}
	
	@Override
	public void setType( VoteType type ) {
		this.TYPE = type;
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
		return LAST_UPDATED == null ? CREATION_DATE : LAST_UPDATED;
	}

	@Override
	public void setLastUpdated( Date lastUpdated ) {
		this.LAST_UPDATED = lastUpdated;
	}

}
