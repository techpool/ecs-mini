package com.pratilipi.api.impl.userpratilipi;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.impl.pratilipi.PratilipiListV1Api;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.util.UserPratilipiDataUtil;
import com.pratilipi.filter.AccessTokenFilter;
import com.pratilipi.filter.UxModeFilter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
@Bind( uri = "/userpratilipi/library/list" )
public class UserPratilipiLibraryListApi extends GenericApi {
	
	public static class GetRequest extends GenericRequest {

		private String cursor;
		private Integer resultCount;

		public void setCursor( String cursor ) {
			this.cursor = cursor;
		}

		public void setResultCount( Integer resultCount ) {
			this.resultCount = resultCount;
		}

	}
	
	
	@Get
	public static PratilipiListV1Api.Response get( GetRequest request ) 
			throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		if( request.cursor != null )
			paramsMap.put( "cursor", request.cursor );
		if( request.resultCount != null )
			paramsMap.put( "resultCount", request.resultCount.toString() );

		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/userpratilipi/library/list", paramsMap );
		return new Gson().fromJson( responseString, PratilipiListV1Api.Response.class );

	}

}
