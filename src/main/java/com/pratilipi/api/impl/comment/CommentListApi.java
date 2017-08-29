package com.pratilipi.api.impl.comment;

import java.util.ArrayList;
import java.util.List;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.CommentParentType;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.CommentData;
import com.pratilipi.data.util.CommentDataUtil;

@SuppressWarnings("serial")
@Bind( uri = "/comment/list" )
public class CommentListApi extends GenericApi {
	
	public static class GetRequest extends GenericRequest {
		
		@Validate( required = true )
		private CommentParentType parentType;
		
		@Validate( required = true, minLong = 1L )
		private String parentId;

		private String cursor;
		private Integer resultCount;

	}
	
	@SuppressWarnings("unused")
	public static class Response extends GenericResponse {
		
		private List<CommentApi.Response> commentList;
		private String cursor;
		
		Response( List<CommentData> commentDataList, String cursor ) {
			this.commentList = new ArrayList<>( commentDataList.size() );
			for( CommentData commentData : commentDataList )
				this.commentList.add( new CommentApi.Response( commentData ) );
			this.cursor = cursor;
		}
		
	}
	
	
	@Get
	public GenericResponse get( GetRequest request )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {
		
		DataListCursorTuple<CommentData> commentDataListCursorTuple = CommentDataUtil.getCommentList(
				request.parentType,
				request.parentId,
				request.cursor,
				null,
				request.resultCount );
		
		return new Response(
				commentDataListCursorTuple.getDataList(),
				commentDataListCursorTuple.getCursor() );
		
	}
	
}
