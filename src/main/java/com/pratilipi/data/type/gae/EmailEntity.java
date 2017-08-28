package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
import com.pratilipi.common.type.EmailState;
import com.pratilipi.common.type.EmailType;
import com.pratilipi.data.type.Email;


@Cache
@Entity( name = "EMAIL" )
public class EmailEntity implements Email {

	@Id
	private Long EMAIL_ID;

	@Index
	private Long USER_ID;


	@Index
	private EmailType TYPE;

	@Index
	private String PRIMARY_CONTENT_ID;


	@Index
	private EmailState STATE;


	@Index
	private String CREATED_BY;

	@Index
	private Date CREATION_DATE;

	@Index( IfNotNull.class )
	private Date SCHEDULED_DATE;

	@Index
	private Date LAST_UPDATED;



	public EmailEntity() {}

	public EmailEntity( Long emailId ) {
		this.EMAIL_ID = emailId;
	}


	@Override
	public Long getId() {
		return EMAIL_ID;
	}

	public void setId( Long emailId ) {
		this.EMAIL_ID = emailId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}

	@Override
	public <T> void setKey( Key<T> key ) {
		this.EMAIL_ID = key.getId();
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
	public EmailType getType() {
		return TYPE;
	}

	@Override
	public void setType( EmailType type ) {
		this.TYPE = type;
	}

	@Override
	public String getPrimaryContentId() {
		return PRIMARY_CONTENT_ID;
	}

	@Override
	public Long getPrimaryContentIdLong() {
		return PRIMARY_CONTENT_ID == null ? null : Long.parseLong( PRIMARY_CONTENT_ID );
	}

	@Override
	public void setPrimaryContentId( String primaryContentId ) {
		this.PRIMARY_CONTENT_ID = primaryContentId;
	}
	
	@Override
	public void setPrimaryContentId( Long primaryContentId ) {
		this.PRIMARY_CONTENT_ID = primaryContentId.toString();
	}


	@Override
	public EmailState getState() {
		return STATE;
	}

	@Override
	public void setState( EmailState state ) {
		this.STATE = state;
	}
	
	
	@Override
	public String getCreatedBy() {
		return CREATED_BY;
	}

	@Override
	public void setCreatedBy( String createdBy ) {
		this.CREATED_BY = createdBy;
	}

	@Override
	public Date getCreationDate() {
		return CREATION_DATE;
	}

	@Override
	public void setCreationDate( Date date ) {
		this.CREATION_DATE = date;
	}

	@Override
	public Date getScheduledDate() {
		return SCHEDULED_DATE;
	}

	@Override
	public void setScheduledDate( Date date ) {
		this.SCHEDULED_DATE = date;		
	}

	@Override
	public Date getLastUpdated() {
		return LAST_UPDATED;
	}

	@Override
	public void setLastUpdated( Date date ) {
		this.LAST_UPDATED = date;
	}

}
