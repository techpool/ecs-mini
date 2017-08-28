package com.pratilipi.api.impl.pratilipi;

import com.google.gson.Gson;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.impl.user.UserV2Api;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.util.PratilipiDataUtil;
import com.pratilipi.filter.UxModeFilter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
@Bind( uri = "/pratilipi", ver = "2" )
public class PratilipiV2Api extends PratilipiV1Api {
	
	@Get
	public Response get( GetRequest request ) throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "_apiVer", "2" );
		paramsMap.put( "pratilipiId", request.pratilipiId.toString() );
		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/pratilipi", paramsMap );
		return new Gson().fromJson( responseString, PratilipiV1Api.Response.class );

	}

}
