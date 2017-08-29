package com.pratilipi.api.batchprocess;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AuthorState;
import com.pratilipi.common.type.BatchProcessState;
import com.pratilipi.common.type.BatchProcessType;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.NotificationType;
import com.pratilipi.common.type.PageType;
import com.pratilipi.common.util.AuthorFilter;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.BatchProcessData;
import com.pratilipi.data.type.BatchProcess;
import com.pratilipi.data.type.Page;
import com.pratilipi.data.util.BatchProcessDataUtil;


@SuppressWarnings( "serial" )
@Bind( uri = "/batch-process" )
public class BatchProcessApi extends GenericApi {
	
	public static class PostRequest extends GenericRequest {

		@Validate( required = true )
		private BatchProcessType type;
		
		private Language language;

		private String message;

		private String sourceUri;
		
	}
	
	@SuppressWarnings("unused")
	public static class Response extends GenericResponse {
		
		private Long batchProcessId;
		
		private BatchProcessType type;
		
		private String initDoc;
		
		private String execDoc;

		private BatchProcessState stateInProgress;
		
		private BatchProcessState stateCompleted;

		private Long creationDate;
		
		private Long lastUpdated;

		
		public Response( BatchProcessData batchProcessData ) {
			this.batchProcessId = batchProcessData.getId();
			this.type = batchProcessData.getType();
			this.initDoc = batchProcessData.getInitDoc();
			this.execDoc = batchProcessData.getExecDoc();
			this.stateInProgress = batchProcessData.getStateInProgress();
			this.stateCompleted = batchProcessData.getStateCompleted();
			this.creationDate = batchProcessData.getCreationDate() == null ? null : batchProcessData.getCreationDate().getTime();
			this.lastUpdated = batchProcessData.getLastUpdated() == null ? null : batchProcessData.getLastUpdated().getTime();
		}
		
	}
	
	
	@Get
	public GenericResponse get( GenericRequest request ) throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		List<BatchProcess> batchProcessList = dataAccessor.getIncompleteBatchProcessList();
		for( BatchProcess batchProcess : batchProcessList )
			if( BatchProcessDataUtil.exec( batchProcess.getId() ) )
				break; // Only one execution per iteration as next BatchProcess' state might change by the time it is picked up.

		return new GenericResponse();
		
	}

	@Post
	public GenericResponse post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException  {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();


		if( request.type == BatchProcessType.NOTIFACTION_BY_AUTHOR_FILTER ) {

			Page page = null;
			
			
			JsonObject errorMessages = new JsonObject();
			
			if( request.message == null || request.message.trim().isEmpty() )
				errorMessages.addProperty( "message", "message is required !" );
			
			if( request.sourceUri == null || request.sourceUri.trim().isEmpty() )
				errorMessages.addProperty( "sourceUri", "sourceUri is required !" );
			else if( ( page = dataAccessor.getPage( request.sourceUri ) ) == null )
				errorMessages.addProperty( "sourceUri", "Invalid url !" );
			else if( page.getType() != PageType.PRATILIPI )
				errorMessages.addProperty( "sourceUri", "Must be a content url !" );
			
			if( errorMessages.size() != 0 )
				throw new InvalidArgumentException( errorMessages );
			
			
			AuthorFilter authorFilter = new AuthorFilter();
			authorFilter.setLanguage( request.language );
			authorFilter.setState( AuthorState.ACTIVE );

			
			// Creating Init Doc
			JsonObject initDoc = new JsonObject();
			initDoc.add( "authorFilter", new Gson().toJsonTree( authorFilter ) );
	 		

			// Creating Exec Doc
			JsonObject execDoc = new JsonObject();
			execDoc.addProperty( "message", request.message );
			execDoc.addProperty( "sourceId", page.getPrimaryContentId().toString() );
			execDoc.addProperty( "type", NotificationType.PRATILIPI.toString() );

	
			BatchProcessDataUtil.createBatchProcess(
					request.type,
					initDoc.toString(),
					execDoc.toString(),
					request.language );
			
		}
		
		return new GenericResponse();

	}

}
