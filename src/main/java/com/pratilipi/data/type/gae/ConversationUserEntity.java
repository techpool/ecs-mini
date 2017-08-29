package com.pratilipi.data.type.gae;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotZero;
import com.pratilipi.data.type.ConversationUser;

@Cache
@Entity( name = "CONVERSATION_USER" )
public class ConversationUserEntity implements ConversationUser {

	@Id
	private String CONVERSATION_USER_ID; // CONVERSATION_ID-USER_ID

	@Index
	private String CONVERSATION_ID;
	
	@Index( IfNotZero.class )
	private Long USER_ID;
		
	
	public ConversationUserEntity() {}
	
	public ConversationUserEntity( String conversationId, Long userId ) {
		this.CONVERSATION_USER_ID = conversationId + "-" + userId;
		this.CONVERSATION_ID = conversationId;
		this.USER_ID = userId;
	}
	
	
	@Override
	public String getId() {
		return CONVERSATION_USER_ID;	
	}
	
	public void setId( String id ) {
		this.CONVERSATION_USER_ID = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.CONVERSATION_USER_ID = key.getName();
	}

	
	@Override
	public String getConversationId() {
		return CONVERSATION_ID;
	}
	
	@Override
	public void setConversationId( String conversationId ) {
		this.CONVERSATION_ID = conversationId;
	}
	
	@Override
	public Long getUserId() {
		return USER_ID;
	}
	
	@Override
	public void setUserId( Long userId ) {
		this.USER_ID = userId;
	}
	
}