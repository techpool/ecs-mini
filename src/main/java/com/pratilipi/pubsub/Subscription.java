package com.pratilipi.pubsub;

import java.util.List;

public interface Subscription {

	List<Message> pull( int maxMessages );
	
}
