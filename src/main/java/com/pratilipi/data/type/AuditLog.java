package com.pratilipi.data.type;

import java.io.Serializable;
import java.util.Date;

import com.pratilipi.common.type.AccessType;

public interface AuditLog extends GenericOfyType, Serializable { // TODO: remove Serializable
	
	Long getId();

	Long getUserId();

	void setUserId( Long userId );
	
	String getAccessId();

	void setAccessId( String accessId );
	
	
	AccessType getAccessType();
	
	void setAccessType( AccessType accessType );

	String getPrimaryContentId();

	Long getPrimaryContentIdLong();
	
	void setPrimaryContentId( String pageContentId );
	
	void setPrimaryContentId( Long pageContentId );

	
	String getEventDataOld();
	
	void setEventDataOld( Object eventDataOld );
	
	String getEventDataNew();

	void setEventDataNew(  Object eventDataNew );
	
	String getEventComment();
	
	void setEventComment( String eventComment );

	
	Date getCreationDate();

	void setCreationDate( Date date );
	
}