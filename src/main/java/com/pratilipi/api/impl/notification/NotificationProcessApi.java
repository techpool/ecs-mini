package com.pratilipi.api.impl.notification;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.Async;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Notification;
import com.pratilipi.data.util.NotificationDataUtil;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;


@SuppressWarnings("serial")
@Bind( uri = "/notification/process" )
public class NotificationProcessApi extends GenericApi {

	private static final Logger logger =
			Logger.getLogger( NotificationProcessApi.class.getName() );

	
	public static class PostRequest extends GenericRequest {
		
		@Validate( required = true, minLong = 1L )
		private Long notificationId;
		
	}

	
	@Get
	public GenericResponse get( GenericRequest request ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		List<Notification> notifList = dataAccessor.getNotificationListWithFcmPending( 1000 );
		
		logger.log( Level.INFO, "Total pending notifications = " + notifList.size() );

		Map<Long, List<Notification>> userIdNotifListMap = new HashMap<>();
		for( Notification notif : notifList ) {
			List<Notification> userNotifList = userIdNotifListMap.get( notif.getUserId() );
			if( userNotifList == null ) {
				userNotifList = new LinkedList<>();
				userIdNotifListMap.put( notif.getUserId(), userNotifList );
			}
			userNotifList.add( notif );
		}

		for( final Entry<Long, List<Notification>> entry : userIdNotifListMap.entrySet() ) {
			
			Async async = new Async() {
				
				@Override
				public void exec() {
					for( Notification notif : entry.getValue() ) {
						Task task = TaskQueueFactory.newTask()
								.setUrl( "/notification/process" )
								.addParam( "notificationId", notif.getId().toString() );
						TaskQueueFactory.getNotificationTaskQueue().add( task );
						logger.log( Level.INFO, "Task created for notification id " + notif.getId() );
					}
				}
				
			};
			
			NotificationDataUtil.updateFirebaseDb( entry.getKey(), entry.getValue(), async );
			
		}
		
		return new GenericResponse();
		
	}

	@Post
	public GenericResponse post( PostRequest request ) throws UnexpectedServerException {
		
		NotificationDataUtil.sendFcm( request.notificationId );
		
		return new GenericResponse();
		
	}
	
}