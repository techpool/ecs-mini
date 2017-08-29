package com.pratilipi.pubsub;

import java.util.HashMap;
import java.util.Map;

public class PubSubFactory {

	private static final Map<String, Topic> topicMap = new HashMap<>();
	private static final Map<String, Subscription> subscriptionMap = new HashMap<>();
	
	private static final String FIREBASE_TOPIC			= "notification";
	private static final String FIREBASE_SUBSCRIPTION	= "notification-firebase";
	
	
	public static Topic getFirebaseTopic() {
		return getTopic( FIREBASE_TOPIC );
	}

	public static Subscription getFirebaseSubscription() {
		return getSubscription( FIREBASE_SUBSCRIPTION );
	}

	
	private static Topic getTopic( String topicName ) {
		Topic topic = topicMap.get( topicName );
		if( topic == null ) {
			topic = new TopicGcpImpl( topicName );
			topicMap.put( topicName, topic );
		}
		return topic;
	}
	
	private static Subscription getSubscription( String subscriptionName ) {
		Subscription subscription = subscriptionMap.get( subscriptionName );
		if( subscription == null ) {
			subscription = new SubscriptionGcpImpl( subscriptionName );
			subscriptionMap.put( subscriptionName, subscription );
		}
		return subscription;
	}
	
}
