package com.pratilipi.api.impl.blogpost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.author.AuthorListApi;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.BlogPostState;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.BlogPostFilter;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.BlogPostData;
import com.pratilipi.data.util.BlogPostDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/blogpost/list" )
public class BlogPostListApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		@Validate( required = true )
		private Long blogId;
		private Language language;
		private BlogPostState state;

		private String cursor;
		private Integer resultCount;

		
		public void setBlogId( Long blogId ) {
			this.blogId = blogId;
		}

		public void setLangugage( Language language ) {
			this.language = language;
		}

		public void setState( BlogPostState state ) {
			this.state = state;
		}

		public void setCursor( String cursor ) {
			this.cursor = cursor;
		}

		public void setResultCount( Integer resultCount ) {
			this.resultCount = resultCount;
		}

	}

	@SuppressWarnings("unused")
	public static class Response extends GenericResponse {

		private List<BlogPostApi.Response> blogPostList;
		private String cursor;


		private Response() {}

		public Response( List<BlogPostData> blogPostList, String cursor ) {
			List<BlogPostApi.Response> genericBlogPostResponseList = new ArrayList<>( blogPostList.size() );
			for( BlogPostData blogPostData : blogPostList )
				genericBlogPostResponseList.add( new BlogPostApi.Response( blogPostData, BlogPostListApi.class ) );
			this.blogPostList = genericBlogPostResponseList;
			this.cursor = cursor;
		}

		
		public List<BlogPostApi.Response> getBlogPostList() {
			return blogPostList;
		}

		public String getCursor() {
			return cursor;
		}
		
	}
	
	
	@Get
	public Response get( GetRequest request )
			throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();

		if( request.blogId != null )
			paramsMap.put( "blogId", request.blogId.toString() );
		if( request.language != null )
			paramsMap.put( "language", request.language.toString() );
		if( request.state != null )
			paramsMap.put( "state", request.state.toString() );

		if( request.cursor != null )
			paramsMap.put( "cursor", request.cursor );
		if( request.resultCount != null )
			paramsMap.put( "resultCount", request.resultCount + "" );

		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/blogpost/list", paramsMap );
		return new Gson().fromJson( responseString, Response.class );

		
	}

}
