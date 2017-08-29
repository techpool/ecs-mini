package com.pratilipi.api.impl.user;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.FacebookApi;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.util.UserDataUtil;

@SuppressWarnings("serial")
@Bind( uri= "/user/facebook/validation" )
public class UserFacebookValidationApi extends GenericApi {
	
	public static class PostRequest extends GenericRequest {
		
		@Validate( required = true )
		private String fbUserAccessToken;
		
		@Validate( required = true )
		private String accessToken;
		
	}
	
	
	@Post
	public GenericResponse post( PostRequest request )
			throws UnexpectedServerException {
		
		if( ! FacebookApi.validateUserAccessToken( request.fbUserAccessToken ) ) 
			UserDataUtil.logoutUser( DataAccessorFactory.getDataAccessor().getAccessToken( request.accessToken ) );
		
		return new GenericResponse();
	}
	
}
