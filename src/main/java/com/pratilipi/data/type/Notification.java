package com.pratilipi.data.type;

import java.util.Date;
import java.util.List;

import com.pratilipi.common.type.NotificationState;
import com.pratilipi.common.type.NotificationType;


public interface Notification extends GenericOfyType {

	Long getId();
	
	
	Long getUserId();
	
	void setUserId( Long userId );
	
	NotificationType getType();
	
	void setType( NotificationType notificationType );
	
	String getSourceId();
	
	Long getSourceIdLong();
	
	void setSourceId( Long sourceId );
	
	void setSourceId( String sourceId );

	
	List<Long> getDataIds();

	boolean addDataId( Long dataId );
	
	boolean removeDataId( Long dataId );
		
	NotificationState getState();
	
	void setState( NotificationState state );

	
	Boolean isFcmPending();
	
	void setFcmPending( boolean fcmPending );

	String getFcmResponse();
	
	void setFcmResponse( String fcmResponse );
	
	
	String getCreatedBy();

	void setCreatedBy( String createdBy );
	
	Date getCreationDate();
	
	void setCreationDate( Date creationDate );

	Date getLastUpdated();

	void setLastUpdated( Date lastUpdated );

}