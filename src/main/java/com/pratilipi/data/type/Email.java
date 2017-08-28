package com.pratilipi.data.type;

import java.util.Date;

import com.pratilipi.common.type.EmailState;
import com.pratilipi.common.type.EmailType;

public interface Email extends GenericOfyType {

	Long getId();

	Long getUserId();

	void setUserId( Long userId );


	EmailType getType();

	void setType( EmailType type );

	String getPrimaryContentId();

	Long getPrimaryContentIdLong();
	
	void setPrimaryContentId( String primaryContentId );
	
	void setPrimaryContentId( Long primaryContentId );


	EmailState getState();

	void setState( EmailState state );


	String getCreatedBy();

	void setCreatedBy( String createdBy );

	Date getCreationDate();

	void setCreationDate( Date date );

	Date getScheduledDate();

	void setScheduledDate( Date date );

	Date getLastUpdated();

	void setLastUpdated( Date date );

}
