package com.pratilipi.data.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.pratilipi.common.type.NotificationState;
import com.pratilipi.common.type.NotificationType;
import com.pratilipi.data.type.Notification;
import com.pratilipi.data.type.gae.NotificationEntity;

public class NotificationMock {

	public static final List<Notification> NOTIFICATION_TABLE = new LinkedList<>();

	public static final Notification notification_1 = new NotificationEntity( 1L );
	public static final Notification notification_2 = new NotificationEntity( 2L );
	public static final Notification notification_3 = new NotificationEntity( 3L );

	public static final Notification notification_4 = new NotificationEntity( 4L );
	public static final Notification notification_5 = new NotificationEntity( 5L );
	public static final Notification notification_6 = new NotificationEntity( 6L );
	
	public static final Notification notification_7 = new NotificationEntity( 7L );
	public static final Notification notification_8 = new NotificationEntity( 8L );
	public static final Notification notification_9 = new NotificationEntity( 9L );


	static {
		NOTIFICATION_TABLE.add( notification_1 );
		NOTIFICATION_TABLE.add( notification_2 );
		NOTIFICATION_TABLE.add( notification_3 );

		NOTIFICATION_TABLE.add( notification_4 );
		NOTIFICATION_TABLE.add( notification_5 );
		NOTIFICATION_TABLE.add( notification_6 );

		NOTIFICATION_TABLE.add( notification_7 );
		NOTIFICATION_TABLE.add( notification_8 );
		NOTIFICATION_TABLE.add( notification_9 );

		notification_1.setCreationDate( new Date() );
		notification_1.setLastUpdated( new Date() );
		notification_1.setSourceId( UserMock.user_2.getId() );
		notification_1.setState( NotificationState.UNREAD );
		notification_1.setType( NotificationType.AUTHOR_FOLLOW );
		notification_1.setUserId( UserMock.user_1.getId() );

		notification_2.setCreationDate( new Date() );
		notification_2.setLastUpdated( new Date() );
		notification_2.setSourceId( UserMock.user_2.getId() );
		notification_2.setState( NotificationState.UNREAD );
		notification_2.setType( NotificationType.AUTHOR_FOLLOW );
		notification_2.setUserId( UserMock.user_1.getId() );

		notification_3.setCreationDate( new Date() );
		notification_3.setLastUpdated( new Date() );
		notification_3.setSourceId( UserMock.user_2.getId() );
		notification_3.setState( NotificationState.UNREAD );
		notification_3.setType( NotificationType.AUTHOR_FOLLOW );
		notification_3.setUserId( UserMock.user_1.getId() );

		notification_4.setCreationDate( new Date() );
		notification_4.setLastUpdated( new Date() );
		notification_4.setSourceId( UserMock.user_2.getId() );
		notification_4.setState( NotificationState.UNREAD );
		notification_4.setType( NotificationType.AUTHOR_FOLLOW );
		notification_4.setUserId( UserMock.user_1.getId() );

		notification_5.setCreationDate( new Date() );
		notification_5.setLastUpdated( new Date() );
		notification_5.setSourceId( UserMock.user_2.getId() );
		notification_5.setState( NotificationState.UNREAD );
		notification_5.setType( NotificationType.AUTHOR_FOLLOW );
		notification_5.setUserId( UserMock.user_1.getId() );

		notification_6.setCreationDate( new Date() );
		notification_6.setLastUpdated( new Date() );
		notification_6.setSourceId( UserMock.user_2.getId() );
		notification_6.setState( NotificationState.UNREAD );
		notification_6.setType( NotificationType.AUTHOR_FOLLOW );
		notification_6.setUserId( UserMock.user_1.getId() );

		notification_7.setCreationDate( new Date() );
		notification_7.setLastUpdated( new Date() );
		notification_7.setSourceId( UserMock.user_2.getId() );
		notification_7.setState( NotificationState.UNREAD );
		notification_7.setType( NotificationType.AUTHOR_FOLLOW );
		notification_7.setUserId( UserMock.user_1.getId() );

		notification_8.setCreationDate( new Date() );
		notification_8.setLastUpdated( new Date() );
		notification_8.setSourceId( UserMock.user_2.getId() );
		notification_8.setState( NotificationState.UNREAD );
		notification_8.setType( NotificationType.AUTHOR_FOLLOW );
		notification_8.setUserId( UserMock.user_1.getId() );

		notification_9.setCreationDate( new Date() );
		notification_9.setLastUpdated( new Date() );
		notification_9.setSourceId( UserMock.user_2.getId() );
		notification_9.setState( NotificationState.UNREAD );
		notification_9.setType( NotificationType.AUTHOR_FOLLOW );
		notification_9.setUserId( UserMock.user_1.getId() );

	}

}