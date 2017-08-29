package com.pratilipi.pubsub;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.cloud.pubsub.PubSubOptions;
import com.google.cloud.pubsub.ReceivedMessage;

public class SubscriptionGcpImpl implements Subscription {

	private final com.google.cloud.pubsub.Subscription subscription;
	
	
	public SubscriptionGcpImpl( String subscriptionName ) {
		this.subscription = PubSubOptions.defaultInstance().service().getSubscription( subscriptionName );
	}


	@Override
	public List<Message> pull( int maxMessages ) {
		Iterator<ReceivedMessage> messageItr = subscription.pull( maxMessages );
		List<Message> messageList = new LinkedList<>();
		while( messageItr.hasNext() )
			messageList.add( new MessageGcpImpl( messageItr.next() ) );
		return messageList;
	}
	
}
