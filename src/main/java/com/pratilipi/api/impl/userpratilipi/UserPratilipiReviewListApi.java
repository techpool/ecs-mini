package com.pratilipi.api.impl.userpratilipi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.UserPratilipiData;
import com.pratilipi.data.util.UserPratilipiDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/userpratilipi/review/list" )
public class UserPratilipiReviewListApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		@Validate( required = true, minLong = 1L )
		private Long pratilipiId;
		
		private String cursor;
		private Integer offset;
		private Integer resultCount;

		public void setPratilipiId( Long pratilipiId ) {
			this.pratilipiId = pratilipiId;
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
		
		private List<UserPratilipiApi.Response> reviewList;
		private String cursor;
		private Long numberFound;

		
		Response() {}
		
		Response( List<UserPratilipiData> reviewList, String cursor, Long numberFound ) {
			this.reviewList = new ArrayList<>( reviewList.size() );
			for( UserPratilipiData review : reviewList )
				this.reviewList.add( new UserPratilipiApi.Response( review, UserPratilipiReviewListApi.class ) );
			this.cursor = cursor;
			this.numberFound = numberFound;
		}

		public List<UserPratilipiApi.Response> getReviewList() {
			return reviewList;
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
		paramsMap.put( "pratilipiId", request.pratilipiId.toString() );
		if( request.cursor != null )
			paramsMap.put( "cursor", request.cursor );
		if( request.offset != null )
			paramsMap.put( "offset", request.offset + "" );
		if( request.resultCount != null )
			paramsMap.put( "resultCount", request.resultCount + "" );

		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/userpratilipi/review/list", paramsMap );
		return new Gson().fromJson( responseString, Response.class );

	}

}
