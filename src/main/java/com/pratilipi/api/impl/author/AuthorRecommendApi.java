package com.pratilipi.api.impl.author;

import java.util.ArrayList;
import java.util.List;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.util.AuthorDataUtil;
import com.pratilipi.filter.AccessTokenFilter;

@SuppressWarnings( "serial" )
@Bind( uri = "/author/recommend" )
public class AuthorRecommendApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		@Validate( required = true )
		private Language language;

		private String cursor;

		private Integer resultCount;


		public void setLanguage( Language language ) {
			this.language = language;
		}

		public void setCursor( String cursor ) {
			this.cursor = cursor;
		}

		public void setResultCount( Integer resultCount ) {
			this.resultCount = resultCount;
		}

	}

	public static class Response extends GenericResponse {

		private List<AuthorApi.Response> authorList;
		private String cursor;

		
		private Response() {}

		private Response( List<AuthorData> authorList, String cursor ) {
			this.authorList = new ArrayList<>( authorList.size() );
			for( AuthorData authorData : authorList )
				this.authorList.add( new AuthorApi.Response( authorData, AuthorRecommendApi.class ) );
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
	public GenericResponse get( GetRequest request ) throws UnexpectedServerException {

		DataListCursorTuple<AuthorData> recommendedAuthorList =
				AuthorDataUtil.getRecommendedAuthorList(
						AccessTokenFilter.getAccessToken().getUserId(),
						request.language,
						request.cursor,
						request.resultCount != null ? request.resultCount : 20 );

		return new Response(
				recommendedAuthorList.getDataList(),
				recommendedAuthorList.getCursor() );

	}

}