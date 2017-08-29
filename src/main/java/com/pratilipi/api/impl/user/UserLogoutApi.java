package com.pratilipi.api.impl.user;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.data.util.UserDataUtil;

@SuppressWarnings("serial")
@Bind( uri= "/user/logout" )
public class UserLogoutApi extends GenericApi {

	@Get
	public UserV1Api.Response post( GenericRequest request )
			throws InvalidArgumentException {
		
		return new UserV1Api.Response( UserDataUtil.logoutUser(), UserLoginApi.class );
		
	}
	
}
