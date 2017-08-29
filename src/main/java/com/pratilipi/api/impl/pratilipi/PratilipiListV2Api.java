package com.pratilipi.api.impl.pratilipi;

import com.google.gson.Gson;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiReviewListApi;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.common.util.PratilipiFilter;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.type.Event;
import com.pratilipi.data.util.EventDataUtil;
import com.pratilipi.data.util.PratilipiDataUtil;
import com.pratilipi.filter.UxModeFilter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
@Bind( uri = "/pratilipi/list", ver = "2" )
public class PratilipiListV2Api extends PratilipiListV1Api {

	@Get
	public Response get( GetRequest request )
			throws InsufficientAccessException, UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();

		if( request.searchQuery != null )
			paramsMap.put( "searchQuery", request.searchQuery );
		if( request.authorId != null )
			paramsMap.put( "authorId", request.authorId.toString() );
		if( request.eventId != null )
			paramsMap.put( "eventId", request.eventId.toString() );
		if( request.language != null )
			paramsMap.put( "language", request.language.name() );
		if( request.type != null )
			paramsMap.put( "type", request.type.name() );
		if( request.listName != null )
			paramsMap.put( "listName", request.listName );
		if( request.state != null )
			paramsMap.put( "state", request.state.name() );
		if( request.orderByLastUpdated != null )
			paramsMap.put( "orderByLastUpdated", request.orderByLastUpdated.toString() );
		if( request.orderByListingDate != null )
			paramsMap.put( "orderByListingDate", request.orderByListingDate.toString() );

		if( request.cursor != null )
			paramsMap.put( "cursor", request.cursor );
		if( request.offset != null )
			paramsMap.put( "offset", request.offset + "" );
		if( request.resultCount != null )
			paramsMap.put( "resultCount", request.resultCount + "" );

		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/pratilipi/list", paramsMap );
		return new Gson().fromJson( responseString, Response.class );

	}

}
