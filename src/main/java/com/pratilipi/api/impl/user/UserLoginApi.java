package com.pratilipi.api.impl.user;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Sensitive;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.util.UserDataUtil;

@SuppressWarnings("serial")
@Bind( uri= "/user/login" )
public class UserLoginApi extends GenericApi {

	public static class Request extends GenericRequest {

		@Validate( required = true, requiredErrMsg = ERR_EMAIL_REQUIRED, regEx = REGEX_EMAIL, regExErrMsg = ERR_EMAIL_INVALID )
		private String email;

		@Sensitive
		@Validate( required = true, requiredErrMsg = ERR_PASSWORD_REQUIRED )
		private String password;

		
		public String getEmail() {
			return email;
		}

		public String getPassword() {
			return password;
		}
		
	}
	
	
	@Post
	public UserV1Api.Response post( Request request )
			throws InvalidArgumentException, InsufficientAccessException {
		
		UserData userData = UserDataUtil.loginUser(
				request.getEmail(),
				request.getPassword() );
		
		return new UserV1Api.Response( userData, UserLoginApi.class );

	}
	
}