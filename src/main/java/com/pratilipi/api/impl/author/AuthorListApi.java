package com.pratilipi.api.impl.author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.impl.userauthor.UserAuthorFollowListApi;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.AuthorFilter;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.util.AuthorDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/author/list" )
public class AuthorListApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		private String searchQuery;
		private Language language;

		private Boolean orderByContentPublished;

		private String cursor;
		private Integer resultCount;


		public String getSearchQuery() {
			return searchQuery;
		}

		public void setSearchQuery( String searchQuery ) {
			this.searchQuery = searchQuery;
		}

		public Language getLanguage() {
			return language;
		}

		public void setLanguage( Language language ) {
			this.language = language;
		}

		public Boolean getOrderByContentPublished() {
			return orderByContentPublished;
		}

		public void setOrderByContentPublished( Boolean orderByContentPublished ) {
			this.orderByContentPublished = orderByContentPublished;
		}

		public String getCursor() {
			return cursor;
		}

		public void setCursor( String cursor ) {
			this.cursor = cursor;
		}

		public Integer getResultCount() {
			return resultCount;
		}

		public void setResultCount( Integer resultCount ) {
			this.resultCount = resultCount;
		}

	}

	public class Response extends GenericResponse {

		private List<AuthorApi.Response> authorList;
		private String cursor;


		private Response() {}

		private Response( List<AuthorData> authorList, String cursor ) {
			this.authorList = new ArrayList<>( authorList.size() );
			for( AuthorData authorData : authorList )
				this.authorList.add( new AuthorApi.Response( authorData, AuthorListApi.class ) );
			this.cursor = cursor;
		}
		

		public List<AuthorApi.Response> getAuthorList() {
			return authorList;
		}

		public String getCursor() {
			return cursor;
					
		}

	}
	
	
	@Get
	public Response get( GetRequest request )
			throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();

		if( request.searchQuery != null )
			paramsMap.put( "searchQuery", request.searchQuery );
		if( request.language != null )
			paramsMap.put( "language", request.language.toString() );
		if( request.orderByContentPublished != null )
			paramsMap.put( "orderByContentPublished", request.orderByContentPublished.toString() );

		if( request.cursor != null )
			paramsMap.put( "cursor", request.cursor );
		if( request.resultCount != null )
			paramsMap.put( "resultCount", request.resultCount + "" );

		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/author/list", paramsMap );
		return new Gson().fromJson( responseString, Response.class );

	}

}
