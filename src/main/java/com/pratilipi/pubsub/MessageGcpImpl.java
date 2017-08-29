package com.pratilipi.pubsub;

import java.util.Date;

import com.google.cloud.pubsub.ReceivedMessage;
import com.google.gson.Gson;

public class MessageGcpImpl implements Message {
	
	private ReceivedMessage message;

	
	MessageGcpImpl( ReceivedMessage message ) {
		this.message = message;
	}
	
	
	@Override
	public <T extends Payload> T getPayload( Class<T> clazz ) {
		return new Gson().fromJson( message.payloadAsString(), clazz );
	}

	@Override
	public Date getPublishTime() {
		return new Date( message.publishTime() );
	}

	@Override
	public void acknowledge() {
		message.ack();
	}
	
}
