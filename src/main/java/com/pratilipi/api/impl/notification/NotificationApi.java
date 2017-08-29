package com.pratilipi.api.impl.notification;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.type.NotificationState;
import com.pratilipi.data.client.NotificationData;
import com.pratilipi.data.util.NotificationDataUtil;

@SuppressWarnings("serial")
@Bind( uri = "/notification" )
public class NotificationApi extends GenericApi {

	public static class PostRequest extends GenericRequest {

		@Validate( required = true, minLong = 1L )
		private Long notificationId;
		
		@Validate( required = true )
		private NotificationState state;

	}

	@SuppressWarnings("unused")
	public static class Response extends GenericResponse { 

		private Long notificationId;

		private String androidHandler;
		
		private String message;

		private String sourceId;
		private String sourceUrl;
		private String sourceImageUrl;
		private String displayImageUrl;

		private NotificationState state;
		private Long lastUpdatedMillis;


		private Response() {}

		public Response( NotificationData notification, Class<? extends GenericApi> clazz ) {
			this.notificationId = notification.getId();
			this.androidHandler = notification.getNotificationType().getAndroidHandler();
			this.message = notification.getMessage();
			this.sourceId = notification.getSourceId();
			this.sourceUrl = notification.getSourceUrl();
			this.sourceImageUrl = notification.getSourceImageUrl();
			this.displayImageUrl = notification.getDisplayImageUrl();
			this.state = notification.getState();
			this.lastUpdatedMillis = notification.getLastUpdatedDate().getTime();
		}

		
		public Long getNotificationId() {
			return notificationId;
		}


		public String getMessage() {
			return message;
		}

		public String getDisplayImageUrl() {
			return displayImageUrl;
		}


		public String getSourceUrl() {
			return sourceUrl;
		}

		public String getSourceImageUrl() {
			return sourceImageUrl;
		}


		public NotificationState getState() {
			return state;
		}


		public Long getLastUpdatedMillis() {
			return lastUpdatedMillis;
		}

	}


	@Post
	public GenericResponse post( PostRequest request ) throws InsufficientAccessException {

		NotificationDataUtil.saveNotificationState(
				request.notificationId,
				request.state );
		
		return new GenericResponse();

	}
	
}