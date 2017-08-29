package com.pratilipi.data.type;

import java.util.Date;

import com.google.gson.JsonObject;

public interface ConversationMessage extends GenericOfyType {

	Long getId();
	
	String getConversationId();
	
	void setConversationId( String name );
	
	Long getCreatorId();
	
	void setCreatorId( Long userId );
	
	String getMessage();

	void setMessage( String message );
	
	JsonObject getData();
	
	void setData( JsonObject data );
	
	Date getCreationDate();
	
	void setCreationDate( Date creationDate );
	
}