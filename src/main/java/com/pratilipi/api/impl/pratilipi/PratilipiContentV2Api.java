package com.pratilipi.api.impl.pratilipi;

import com.google.gson.Gson;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.PratilipiContentType;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.type.PratilipiContentDoc;
import com.pratilipi.data.util.PratilipiDocUtil;
import com.pratilipi.filter.UxModeFilter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( "serial" )
@Bind( uri = "/pratilipi/content", ver = "2" )
public class PratilipiContentV2Api extends PratilipiContentV1Api {

	@Get
	public GetResponse get( GetRequest request )
			throws InvalidArgumentException, InsufficientAccessException,
			UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "pratilipiId", request.pratilipiId.toString() );
		paramsMap.put( "_apiVer", "2" );
		if( request.chapterId != null )
			paramsMap.put( "chapterId", request.chapterId );
		if( request.chapterNo != null )
			paramsMap.put( "chapterNo", request.chapterNo.toString() );
		if( request.pageNo != null )
			paramsMap.put( "pageNo", request.pageNo.toString() );
		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/pratilipi/content", paramsMap );
		return new Gson().fromJson( responseString, GetResponse.class );

	}

}
