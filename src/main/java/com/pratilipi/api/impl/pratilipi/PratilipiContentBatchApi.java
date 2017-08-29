package com.pratilipi.api.impl.pratilipi;

import com.google.gson.JsonObject;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.type.PratilipiContentDoc;
import com.pratilipi.data.util.PratilipiDocUtil;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;


@SuppressWarnings( "serial" )
@Bind( uri = "/pratilipi/content/batch" )
public class PratilipiContentBatchApi extends GenericApi {

	public static class PostRequest extends GenericRequest {

		@Validate( required = true, requiredErrMsg = ERR_PRATILIPI_ID_REQUIRED, minLong = 1L )
		private Long pratilipiId;

		@Validate( required = true )
		private JsonObject jsonObject;

	}

	@Post
	public static GenericResponse get( PostRequest request ) 
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		PratilipiContentDoc pcDoc = PratilipiDocUtil.saveContentPageBatch( request.pratilipiId, request.jsonObject );

		Task task = TaskQueueFactory.newTask()
				.setUrl( "/pratilipi/process" )
				.addParam( "pratilipiId", request.pratilipiId.toString() )
				.addParam( "processContentDoc", "true" );
		TaskQueueFactory.getPratilipiTaskQueue().add( task );

		// TODO: Update Asynchronously
		PratilipiDocUtil.updateFirebaseIndex( request.pratilipiId, pcDoc );

		return new GenericResponse();

	}

}
