package com.pratilipi.pubsub;

import com.google.cloud.pubsub.Message;
import com.google.cloud.pubsub.PubSubOptions;

public class TopicGcpImpl implements Topic {

	private final com.google.cloud.pubsub.Topic topic;
	
	
	public TopicGcpImpl( String topicName ) {
		topic = PubSubOptions.defaultInstance().service().getTopic( topicName );
	}


	@Override
	public void publish( Payload payload ) {
		topic.publish( Message.of( payload.toString() ) );
	}
	
}
