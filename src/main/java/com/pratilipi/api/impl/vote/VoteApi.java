package com.pratilipi.api.impl.vote;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.type.ReferenceType;
import com.pratilipi.common.type.VoteParentType;
import com.pratilipi.common.type.VoteType;
import com.pratilipi.data.client.VoteData;
import com.pratilipi.data.util.VoteDataUtil;
import com.pratilipi.filter.AccessTokenFilter;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;

@SuppressWarnings("serial")
@Bind( uri = "/vote" )
public class VoteApi extends GenericApi {
	
	public static class PostRequest extends GenericRequest {
		
		@Validate( required = true )
		private VoteParentType parentType;
		
		@Validate( required = true )
		private String parentId;

		@Validate( required = true )
		private VoteType type;
		
	}
	
	
	@Post
	public GenericResponse post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException {
		
		VoteData voteData = VoteDataUtil.saveVoteData(
				AccessTokenFilter.getAccessToken().getUserId(),
				request.parentType,
				request.parentId,
				request.type );

		
		if( voteData.getReferenceType() == ReferenceType.PRATILIPI ) {
			
			Task task = TaskQueueFactory.newTask()
					.setUrl( "/pratilipi/process" )
					.addParam( "pratilipiId", voteData.getReferenceId() )
					.addParam( "updateReviewsDoc", "true" );
		
			TaskQueueFactory.getPratilipiTaskQueue().add( task );

		}
		
		
		return new GenericResponse();
		
	}
	
}
