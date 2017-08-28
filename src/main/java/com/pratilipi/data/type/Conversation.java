package com.pratilipi.data.type;

import java.util.Date;

public interface Conversation extends GenericOfyType {

	String getId();
	
	String getTitle();
	
	void setTitle( String title );
	
	Long getCreator();
	
	void setCreator( Long creator );
	
	String getCreatorName();
	
	void setCreatorName( String creatorName );
	
	String getCreatorEmail();
	
	void setCreatorEmail( String creatorEmail );
	
	String getCreatorPhone();
	
	void setCreatorPhone( String creatorPhone );
	
	Date getCreationDate();
	
	void setCreationDate( Date creationDate );

	Date getLastUpdated();
	
	void setLastUpdated( Date lastUpdated );

}