package com.pratilipi.api.impl.user;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.util.FirebaseApi;
import com.pratilipi.filter.AccessTokenFilter;

@SuppressWarnings("serial")
@Bind( uri= "/user/firebase-token" )
public class UserFirebaseTokenApi extends GenericApi {

	public static class Response extends GenericResponse {
		
		@SuppressWarnings("unused")
		private String firebaseToken;

		
		private Response() {}
		
		private Response( String firebaseToken ) {
			this.firebaseToken = firebaseToken;
		}
		
	}

	
	@Get
	public GenericResponse post( GenericRequest request ) throws InvalidArgumentException {
		Long userId = AccessTokenFilter.getAccessToken().getUserId();
		if( userId == 0 )
			return new Response();
		else
			return new Response( FirebaseApi.getCustomTokenForUser( userId ) );
	}
	
}
