package com.pratilipi.api.impl.blogpost;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.event.EventApi;
import com.pratilipi.api.impl.user.UserV1Api;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.BlogPostState;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.HtmlUtil;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.BlogPostData;
import com.pratilipi.data.type.BlogPost;
import com.pratilipi.data.util.BlogPostDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/blogpost" )
public class BlogPostApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		@Validate( required = true, minLong = 1L )
		private Long blogPostId;

		public void setBlogPostId( Long blogPostId ) {
			this.blogPostId = blogPostId;
		}

	}

	public static class PostRequest extends GenericRequest {

		@Validate( minLong = 1L )
		private Long blogPostId;
		
		@Validate( minLong = 1L )
		private Long blogId;
		
		private String title;
		private String titleEn;
		private String content;

		private Language language;
		private BlogPostState state;
		
	}

	public static class Response extends GenericResponse {
		
		private Long blogPostId;
		private Long blogId;
		
		private String title;
		private String titleEn;
		private String content;
		
		private BlogPostState state;
		private UserV1Api.Response createdBy;
		private Long creationDateMillis;
		private Long lastUpdatedMillis;
		
		private String pageUrl;
		private Boolean hasAccessToUpdate;

		
		@SuppressWarnings("unused")
		private Response() {}
		
		private Response( BlogPostData blogPostData ) {
			this.blogPostId = blogPostData.getId();
			this.blogId = blogPostData.getBlogId();
			this.title = blogPostData.getTitle();
			this.titleEn = blogPostData.getTitleEn();
			this.content = blogPostData.getContent();
			this.state = blogPostData.getState();
			if( blogPostData.getCreatedBy() != null )
				this.createdBy = new UserV1Api.Response( blogPostData.getCreatedBy(), BlogPostApi.class );
			this.creationDateMillis = blogPostData.getCreationDate().getTime();
			this.lastUpdatedMillis = blogPostData.getLastUpdated().getTime();
			this.pageUrl = blogPostData.getPageUrl();
			this.hasAccessToUpdate = blogPostData.hasAccessToUpdate();
		}
		
		public Response( BlogPostData blogPostData, Class<? extends GenericApi> clazz ) {
			if( clazz == BlogPostListApi.class ) {
				this.blogPostId = blogPostData.getId();
				this.blogId = blogPostData.getBlogId();
				this.title = blogPostData.getTitle();
				this.titleEn = blogPostData.getTitleEn();
				if( blogPostData.getContent() != null )
					this.content = HtmlUtil.toPlainText( blogPostData.getContent() );
				this.state = blogPostData.getState();
				if( blogPostData.getCreatedBy() != null )
					this.createdBy = new UserV1Api.Response( blogPostData.getCreatedBy(), clazz );
				this.creationDateMillis = blogPostData.getCreationDate().getTime();
				this.lastUpdatedMillis = blogPostData.getLastUpdated().getTime();
				this.pageUrl = blogPostData.getPageUrl();
				this.hasAccessToUpdate = blogPostData.hasAccessToUpdate();
			}
		}
		
		
		public Long getId() {
			return blogPostId;
		}

		public Long getBlogId() {
			return blogId;
		}

		
		public String getTitle() {
			return title;
		}

		public String getTitleEn() {
			return titleEn;
		}

		public String getContent() {
			return content;
		}

		
		public BlogPostState getState() {
			return state;
		}
		
		public UserV1Api.Response getCreatedBy() {
			return createdBy;
		}
		
		public Date getCreationDate() {
			return new Date( creationDateMillis );
		}
		
		public Date getLastUpdated() {
			return new Date( lastUpdatedMillis );
		}
		
		
		public String getPageUrl() {
			return pageUrl;
		}

		public boolean hasAccessToUpdate() {
			return hasAccessToUpdate == null ? false : hasAccessToUpdate;
		}
		
	}

	
	@Get
	public Response get( GetRequest request ) throws UnexpectedServerException {
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "blogPostId", request.blogPostId.toString() );
		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/blogpost", paramsMap );
		return new Gson().fromJson( responseString, Response.class );
	}

	@Post
	public Response post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException {
		
		BlogPostData blogPostData = new BlogPostData( request.blogPostId );
		blogPostData.setBlogId( request.blogId );
		blogPostData.setTitle( request.title );
		blogPostData.setTitleEn( request.titleEn );
		blogPostData.setContent( request.content );
		blogPostData.setLanguage( request.language );
		blogPostData.setState( request.state );
		
		blogPostData = BlogPostDataUtil.saveBlogPostData( blogPostData );
		
		return new Response( blogPostData );
		
	}

}
