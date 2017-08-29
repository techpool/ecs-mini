package com.pratilipi.api.impl.user;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.util.UserDataUtil;

@SuppressWarnings("serial")
@Bind( uri= "/user/verification" )
public class UserVerificationApi extends GenericApi {
	
	public static class PostRequest extends GenericRequest {
		
		@Validate( required = true, requiredErrMsg = ERR_EMAIL_REQUIRED, regEx = REGEX_EMAIL, regExErrMsg = ERR_EMAIL_INVALID )
		private String email;
		
		@Validate( required = true )
		private String verificationToken;
		
		
		public String getEmail() {
			return email;
		}
		
		public String getVerificationToken() {
			return verificationToken;
		}
		
	}

	
	@Post
	public UserV1Api.Response post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException {
		
		UserDataUtil.verifyUserEmail(
				request.getEmail(),
				request.getVerificationToken() );
		
		UserData userData = UserDataUtil.loginUser(
				request.getEmail(),
				request.getVerificationToken() );
		
		return new UserV1Api.Response( userData, UserVerificationApi.class );
		
	}
	
}