package com.pratilipi.data.client;

import java.util.Date;

import com.pratilipi.common.type.ReferenceType;
import com.pratilipi.common.type.VoteParentType;
import com.pratilipi.common.type.VoteType;

public class VoteData {

	private String voteId;
	
	private Long userId;
	private UserData user;
	
	private VoteParentType parentType;
	private String parentId;

	private ReferenceType referenceType;
	private String referenceId;
	
	private VoteType type;
	
	private Long creationDateMillis;
	private Long lastUpdatedMillis;


	public VoteData() {}

	public VoteData( String id ) {
		this.voteId = id;
	}

	
	public String getId() {
		return voteId;
	}
	
	public void setId( String id ) {
		this.voteId = id;
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

	
	public VoteParentType getParentType() {
		return parentType;
	}

	public void setParentType( VoteParentType parentType ) {
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

	
	public VoteType getType() {
		return type;
	}

	public void setType( VoteType type ) {
		this.type = type;
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

}
