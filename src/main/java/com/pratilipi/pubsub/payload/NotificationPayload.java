package com.pratilipi.pubsub.payload;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.pratilipi.pubsub.Payload;

public class NotificationPayload extends Payload {

	private List<Long> notificationIdList;

	
	public NotificationPayload() {
		this.notificationIdList = new LinkedList<>();
	}
	
	public NotificationPayload( Long notificationId ) {
		this.notificationIdList = new ArrayList<>( 0 );
		this.notificationIdList.add( notificationId );
	}
	
	public NotificationPayload( List<Long> notificationIdList ) {
		this.notificationIdList = notificationIdList;
	}
	
	
	public void addNotificationId( Long notificationId ) {
		this.notificationIdList.add( notificationId );
	}
	
	public List<Long> getNotificationIdList() {
		return notificationIdList;
	}

}
