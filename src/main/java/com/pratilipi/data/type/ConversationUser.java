package com.pratilipi.data.type;

public interface ConversationUser extends GenericOfyType {

	String getId();
	
	String getConversationId();
	
	void setConversationId( String coversationId );
	
	Long getUserId();
	
	void setUserId( Long userId );

}