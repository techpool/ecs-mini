package com.pratilipi.api.impl.user;

import java.util.Date;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InvalidArgumentException;

@SuppressWarnings("serial")
@Bind( uri= "/user/accesstoken" )
public class UserAccessTokenApi extends GenericApi {

	@SuppressWarnings("unused")
	public static class Response extends GenericResponse {
		
		private String accessToken;
		private Long expiryMills;

		
		public Response( String accessToken, Date expiry ) {
			this.accessToken = accessToken;
			this.expiryMills = expiry.getTime();
		}
		
	}

	
	@Get
	public GenericResponse get( GenericRequest request ) throws InvalidArgumentException {
		
		// See AccessTokenFilter for implementation.
		
		return new GenericResponse();
		
	}
	
}
