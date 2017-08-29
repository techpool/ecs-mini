package com.pratilipi.api.impl.user;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.data.util.AccessTokenDataUtil;

@SuppressWarnings("serial")
@Bind( uri= "/user/accesstoken/fcmtoken" )
public class UserAccessTokenFcmTokenApi extends GenericApi {

	public static class PostRequest extends GenericRequest {
		
		@Validate( required = true )
		private String fcmToken;

	}
	
	
	@Post
	public GenericResponse post( PostRequest request ) throws InvalidArgumentException {
		AccessTokenDataUtil.setOrUpdateFcmToken( request.fcmToken );
		return new GenericResponse();
	}
	
}
