package com.pratilipi.data.type.gae;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pratilipi.data.type.ConversationMessage;

@Cache
@Entity( name = "CONVERSATION_MESSAGE" )
public class ConversationMessageEntity implements ConversationMessage {

	@Id
	private Long MESSAGE_ID;
	
	@Index
	private String CONVERSATION_ID;
	
	@Index
	private Long CREATOR_ID;
	
	private String MESSAGE;
	
	private String DATA;
	
	@Index
	private Date CREATION_DATE;
	
	
	@Override
	public Long getId() {
		return MESSAGE_ID;	
	}
	
	public void setId( Long id ) {
		this.MESSAGE_ID = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.MESSAGE_ID = key.getId();
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
	public Long getCreatorId() {
		return CREATOR_ID;
	}
	
	@Override
	public void setCreatorId( Long userId ) {
		this.CREATOR_ID = userId;
	}
	
	@Override
	public String getMessage() {
		return MESSAGE;
	}

	@Override
	public void setMessage( String message ) {
		this.MESSAGE = message;
	}
	
	@Override
	public JsonObject getData() {
		return new Gson().fromJson( DATA, JsonElement.class ).getAsJsonObject();
	}

	@Override
	public void setData( JsonObject data ) {
		this.DATA = data == null ? null : data.toString();
	}
	
	@Override
	public Date getCreationDate() {
		return CREATION_DATE;
	}
	
	@Override
	public void setCreationDate( Date creationDate ) {
		this.CREATION_DATE = creationDate;
	}
	
}