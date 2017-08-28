package com.pratilipi.api.impl.pratilipi;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.event.EventApi;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.PratilipiContentType;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.type.PratilipiContentDoc;
import com.pratilipi.data.util.PratilipiDataUtil;
import com.pratilipi.data.util.PratilipiDocUtil;
import com.pratilipi.filter.UxModeFilter;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( "serial" )
@Bind( uri = "/pratilipi/content", ver = "1" )
public class PratilipiContentV1Api extends GenericApi {

	public static class GetRequest extends GenericRequest {

		@Validate( required = true, requiredErrMsg = ERR_PRATILIPI_ID_REQUIRED, minLong = 1L )
		protected Long pratilipiId;

		protected String chapterId;

		@Validate( minInt = 1 )
		protected Integer chapterNo;

		@Validate( minInt = 1 )
		protected Integer pageNo;


 		public void setPratilipiId( Long pratilipiId ) {
			this.pratilipiId = pratilipiId;
		}

		public void setChapterId( String chapterId ) { this.chapterId = chapterId; }

		public void setChapterNo( Integer chapterNo ) {
			this.chapterNo = chapterNo;
		}

		public void setPageNo( Integer pageNo ) {
			this.pageNo = pageNo;
		}

	}

	public static class PostRequest extends GenericRequest {

		@Validate( required = true, requiredErrMsg = ERR_PRATILIPI_ID_REQUIRED, minLong = 1L )
		private Long pratilipiId;

		private String chapterId;

		@Validate( minInt = 1 )
		private Integer chapterNo;

		@Validate( minInt = 1 )
		private Integer pageNo;

		private String chapterTitle;

		private String content;

	}

	@SuppressWarnings("unused")
	public static class GetResponse extends GenericResponse {

		private Long pratilipiId;
		private String chapterId;
		private Integer chapterNo;
		private String chapterTitle;
		private Integer pageNo;
		private Object content;


		private GetResponse() {}

		protected GetResponse( Long pratilipiId, String chapterId, Integer chapterNo, String chapterTitle, Integer pageNo, Object content ) {
			this.pratilipiId = pratilipiId;
			this.chapterId = chapterId;
			this.chapterNo = chapterNo;
			this.chapterTitle = chapterTitle;
			this.pageNo = pageNo;
			this.content = content;
		}

		@Deprecated
		protected GetResponse( Long pratilipiId, Integer chapterNo, String chapterTitle, Integer pageNo, Object content ) {
			this( pratilipiId, null, chapterNo, chapterTitle, pageNo, content );
		}
		
		public String getChapterTitle() {
			return chapterTitle;
		}

		public Object getContent() {
			return content;
		}

	}


	@Get
	public GetResponse get( GetRequest request )
			throws InvalidArgumentException, InsufficientAccessException,
			UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "pratilipiId", request.pratilipiId.toString() );
		paramsMap.put( "_apiVer", "1" );
		if( request.chapterId != null )
			paramsMap.put( "chapterId", request.chapterId );
		if( request.chapterNo != null )
			paramsMap.put( "chapterNo", request.chapterNo.toString() );
		if( request.pageNo != null )
			paramsMap.put( "pageNo", request.pageNo.toString() );
		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/pratilipi/content", paramsMap );
		return new Gson().fromJson( responseString, GetResponse.class );

	}

	@Post
	public GenericResponse post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		if( request.chapterId == null  && request.chapterNo == null )
			throw new InvalidArgumentException( "chapterId or chapterNo is missing" );

		PratilipiContentDoc pcDoc = PratilipiDocUtil.saveContentPage(
				request.pratilipiId,
				request.chapterId,
				request.chapterNo,
				request.chapterTitle,
				request.pageNo == null ? 1 : request.pageNo,
				request.content );

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
