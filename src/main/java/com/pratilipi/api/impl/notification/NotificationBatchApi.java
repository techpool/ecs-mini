package com.pratilipi.api.impl.notification;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.type.NotificationState;
import com.pratilipi.data.util.NotificationDataUtil;

import java.util.Map;

@SuppressWarnings("serial")
@Bind( uri = "/notification/batch" )
public class NotificationBatchApi extends GenericApi {

	public static class PostRequest extends GenericRequest {

		@Validate( required = true )
		private Map<Long, NotificationState> notificationStates;

	}

	@Post
	public GenericResponse post( PostRequest request ) throws InsufficientAccessException {

		NotificationDataUtil.saveNotificationState( request.notificationStates );

		return new GenericResponse();

	}

}
