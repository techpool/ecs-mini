package com.pratilipi.pubsub;

import java.util.Date;

public interface Message {

	<T extends Payload> T getPayload( Class<T> clazz );
	
	Date getPublishTime();

	void acknowledge();
	
}
