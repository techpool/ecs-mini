package com.pratilipi.api.impl.init;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.impl.user.UserV2Api;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.common.util.PratilipiFilter;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.util.PratilipiDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/init", ver = "2" )
public class InitV2Api extends InitV1Api {
	
	@Get
	public Response get( GetRequest request ) throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "_apiVer", "2" );
		paramsMap.put( "language", request.language.name() );
		String userResponse = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/init", paramsMap );
		return new Gson().fromJson( userResponse, InitV1Api.Response.class );

	}
	
}
