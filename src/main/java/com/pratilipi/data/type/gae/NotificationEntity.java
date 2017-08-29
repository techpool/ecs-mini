package com.pratilipi.data.type.gae;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfTrue;
import com.pratilipi.common.type.NotificationState;
import com.pratilipi.common.type.NotificationType;
import com.pratilipi.data.type.Notification;

@Cache
@Entity( name = "NOTIFICATION" )
public class NotificationEntity implements Notification {

	@Id
	private Long NOTIFICATION_ID;
	
	
	@Index
	private Long USER_ID;
	
	@Index
	private NotificationType TYPE;
	
	@Index
	private String SOURCE_ID;

	@Index
	private List<Long> DATA_IDS;
	
	@Index
	private List<Long> AUDIT_LOG_IDS;
	
	@Index
	private NotificationState STATE;

	
	@Index( IfTrue.class )
	private Boolean FCM_PENDING;
	
	private String FCM_RESPONSE;
	
	
	@Index
	private String CREATED_BY;
	
	@Index
	private Date CREATION_DATE;
	
	@Index
	private Date LAST_UPDATED;



	public NotificationEntity() {}

	public NotificationEntity( Long id ) {
		this.NOTIFICATION_ID = id;
	}

	
	@Override
	public Long getId() {
		return NOTIFICATION_ID;
	}
	
	public void setId( Long id ) {
		this.NOTIFICATION_ID = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.NOTIFICATION_ID = key.getId();
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
	public NotificationType getType() {
		return TYPE;
	}

	@Override
	public void setType( NotificationType type ) {
		this.TYPE = type;
	}

	@Override
	public String getSourceId() {
		return SOURCE_ID;
	}
	
	@Override
	public Long getSourceIdLong() {
		return SOURCE_ID == null ? null : Long.parseLong( SOURCE_ID );
	}
	
	@Override
	public void setSourceId( Long sourceId ) {
		this.SOURCE_ID = sourceId == null ? null : sourceId.toString();
	}
	
	@Override
	public void setSourceId( String sourceId ) {
		this.SOURCE_ID = sourceId;
	}
	
	@Override
	public List<Long> getDataIds() {
		return DATA_IDS == null ? new ArrayList<Long>( 0 ) : DATA_IDS;
	}

	@Override
	public boolean addDataId( Long dataId ) {
		
		if( this.DATA_IDS == null )
			this.DATA_IDS = new ArrayList<>( 1 );
		
		if( this.DATA_IDS.contains( dataId ) )
			return false;
		
		this.DATA_IDS.add( dataId );
		
		return true;
		
	}
	
	@Override
	public boolean removeDataId( Long dataId ) {
		
		if( this.DATA_IDS == null || ! this.DATA_IDS.contains( dataId ) )
			return false;
		
		this.DATA_IDS.remove( dataId );
		
		return true;
		
	}
	
	@Override
	public NotificationState getState() {
		return STATE;
	}

	@Override
	public void setState( NotificationState state ) {
		this.STATE = state;
	}

	@Override
	public Boolean isFcmPending() {
		return FCM_PENDING == null ? false : FCM_PENDING;
	}
	
	@Override
	public void setFcmPending( boolean fcmPending ) {
		this.FCM_PENDING = fcmPending;
	}
	
	@Override
	public String getFcmResponse() {
		return FCM_RESPONSE;
	}
	
	@Override
	public void setFcmResponse( String fcmResponse ) {
		this.FCM_RESPONSE = fcmResponse;
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
