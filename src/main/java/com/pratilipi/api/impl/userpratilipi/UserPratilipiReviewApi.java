package com.pratilipi.api.impl.userpratilipi;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.UserReviewState;
import com.pratilipi.data.client.UserPratilipiData;
import com.pratilipi.data.util.UserPratilipiDataUtil;
import com.pratilipi.filter.AccessTokenFilter;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;

@SuppressWarnings("serial")
@Bind( uri = "/userpratilipi/review" )
public class UserPratilipiReviewApi extends GenericApi {
	
	public static class PostRequest extends GenericRequest {
		
		@Validate( required = true, minLong = 1L )
		private Long pratilipiId;
		
		private Integer rating;
		private String review;
		private UserReviewState reviewState;
		
	}
	
	
	@Post
	public UserPratilipiApi.Response post( PostRequest request )
			throws InsufficientAccessException, UnexpectedServerException {

		UserPratilipiData userPratilipiData = UserPratilipiDataUtil.saveUserPratilipiReview(
				AccessTokenFilter.getAccessToken().getUserId(),
				request.pratilipiId,
				request.rating,
				request.review,
				request.reviewState );
		
		Task task = TaskQueueFactory.newTask()
				.setUrl( "/pratilipi/process" )
				.addParam( "pratilipiId", request.pratilipiId.toString() )
				.addParam( "updateReviewsDoc", "true" )
				.addParam( "updateUserPratilipiStats", "true" );
		TaskQueueFactory.getPratilipiTaskQueue().add( task );
		
		return new UserPratilipiApi.Response( userPratilipiData, UserPratilipiReviewApi.class );
		
	}		

}
