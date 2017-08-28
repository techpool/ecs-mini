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
@Bind( uri= "/user/passwordupdate" )
public class UserPasswordUpdateApi extends GenericApi {
	
	public static class PostRequest extends GenericRequest {
		
		@Validate( regEx = REGEX_EMAIL, regExErrMsg = ERR_EMAIL_INVALID )
		private String email;
		
		private String verificationToken;
		
		@Sensitive
		private String password;
		
		@Sensitive
		@Validate( required = true, requiredErrMsg = ERR_PASSWORD_REQUIRED, regEx = REGEX_PASSWORD, regExErrMsg = ERR_PASSWORD_INVALID )
		private String newPassword;
		
		
		public String getEmail() {
			return email;
		}
		
		public String getVerificationToken() {
			return verificationToken;
		}
		
		public String getPassword() {
			return password;
		}
		
		public String getNewPassword() {
			return newPassword;
		}
		
	}
	
	
	@Post
	public UserV1Api.Response post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException {
		
		UserData userData;
		if( request.getEmail() != null && request.getVerificationToken() != null ) {
			// Verifying email
			UserDataUtil.verifyUserEmail( request.getEmail(), request.getVerificationToken() );
			// Updating password
			UserDataUtil.updateUserPassword( request.getEmail(), request.getVerificationToken(), request.getNewPassword() );
			// Logging-in the user
			userData = UserDataUtil.loginUser( request.getEmail(), request.getVerificationToken() );
		} else if( request.getPassword() != null ) {
			// Updating password
			UserDataUtil.updateUserPassword( request.getPassword(), request.getNewPassword() );
			userData = UserDataUtil.getCurrentUser();
		} else {
			throw new InvalidArgumentException( GenericRequest.ERR_INSUFFICIENT_ARGS );
		}
		
		return new UserV1Api.Response( userData, UserPasswordUpdateApi.class );
		
	}
	
}