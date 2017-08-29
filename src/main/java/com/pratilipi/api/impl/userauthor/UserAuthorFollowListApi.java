package com.pratilipi.api.impl.userauthor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.impl.author.AuthorApi;
import com.pratilipi.api.impl.pratilipi.PratilipiListV1Api;
import com.pratilipi.api.impl.user.UserV1Api;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.util.UserAuthorDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/userauthor/follow/list" )
public class UserAuthorFollowListApi extends GenericApi {
	
	public static class GetRequest extends GenericRequest {

		private Long userId;
		private Long authorId;
		private String cursor;
		private Integer offset;
		private Integer resultCount;
		
		public void setUserId( Long userId ) {
			this.userId = userId;
		}

		public void setAuthorId( Long authorId ) {
			this.authorId = authorId;
		}

		public void setCursor( String cursor ) {
			this.cursor = cursor;
		}

		public void setOffset( Integer offset ) {
			this.offset = offset;
		}

		public void setResultCount( Integer resultCount ) {
			this.resultCount = resultCount;
		}

	}
	
	public static class Response extends GenericResponse { 

		private List<UserV1Api.Response> userList;
		private List<AuthorApi.Response> authorList;
		private String cursor;
		private Long numberFound;

		
		private Response() {}
		
		public Response( List<UserData> userList, List<AuthorData> authorList, String cursor, Long numberFound ) {
			
			if( userList != null ) {
				this.userList = new ArrayList<>( userList.size() ); 
				for( UserData user : userList )
					this.userList.add( new UserV1Api.Response( user, UserAuthorFollowListApi.class ) );
				this.numberFound = numberFound;
			}
			
			if( authorList != null ) {
				this.authorList = new ArrayList<>( authorList.size() ); 
				for( AuthorData author : authorList )
					this.authorList.add( new AuthorApi.Response( author, UserAuthorFollowListApi.class ) );
				this.numberFound = numberFound;
			}
			
			this.cursor = cursor;
		
		}


		public List<UserV1Api.Response> getUserList() {
			return userList;
		}

		public List<AuthorApi.Response> getAuthorList() {
			return authorList;
		}

		public String getCursor() {
			return cursor;
		}
		
		public Long getNumberFound() {
			return numberFound;
		}
		
	}

	
	@Get
	public Response get( GetRequest request ) throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();

		if( request.authorId != null )
			paramsMap.put( "authorId", request.authorId.toString() );
		if( request.userId != null )
			paramsMap.put( "userId", request.userId.toString() );

		if( request.cursor != null )
			paramsMap.put( "cursor", request.cursor );
		if( request.offset != null )
			paramsMap.put( "offset", request.offset + "" );
		if( request.resultCount != null )
			paramsMap.put( "resultCount", request.resultCount + "" );

		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/userauthor/follow/list", paramsMap );
		return new Gson().fromJson( responseString, Response.class );
		
	}
	
}
