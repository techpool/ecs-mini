package com.pratilipi.api.impl.user;

import com.google.gson.JsonObject;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.util.UserDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri= "/user/email" )
public class UserEmailApi extends GenericApi {
	
	public static class PostRequest extends GenericRequest {
		
		@Validate( minLong = 1L )
		private Long userId;
		
		@Validate( regEx = REGEX_EMAIL, regExErrMsg = ERR_EMAIL_INVALID )
		private String email;
		
		private Language language;
		
		private Boolean sendWelcomeMail;
		
		private Boolean sendEmailVerificationMail;
		
		private Boolean sendPasswordResetMail;
		
		private Boolean sendBirthdayMail;

		
		public Long getUserId() {
			return userId;
		}
		
		public String getEmail() {
			return email;
		}
		
		public Language getLanguage() {
			return language;
		}
		
		public boolean sendWelcomeMail() {
			return sendWelcomeMail == null ? false : sendWelcomeMail;
		}
		
		public boolean sendEmailVerificationMail() {
			return sendEmailVerificationMail == null ? false : sendEmailVerificationMail;
		}
		
		public boolean sendPasswordResetMail() { 
			return sendPasswordResetMail == null ? false : sendPasswordResetMail;
		}
		
		public boolean sendBirthdayMail() {
			return sendBirthdayMail == null ? false : sendBirthdayMail;
		}
		
	}
	
	
	@Post
	public GenericResponse post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		JsonObject errorMessages = new JsonObject();

		if( ( request.sendWelcomeMail() || request.sendBirthdayMail() ) && request.getUserId() == null )
			errorMessages.addProperty( "userId", GenericRequest.ERR_USER_ID_REQUIRED );
		
		if( request.sendEmailVerificationMail() && request.getUserId() == null && ( request.getEmail() == null || request.getEmail().trim().isEmpty() ) )
			errorMessages.addProperty( "email", GenericRequest.ERR_EMAIL_REQUIRED );
		
		if( request.sendPasswordResetMail() && ( request.getEmail() == null || request.getEmail().trim().isEmpty() ) )
			errorMessages.addProperty( "email", GenericRequest.ERR_EMAIL_REQUIRED );

		if( errorMessages.entrySet().size() > 0 )
			throw new InvalidArgumentException( errorMessages );


		Language language = request.getLanguage() == null
				? UxModeFilter.getDisplayLanguage() // User facing requests
				: request.getLanguage(); // Offline requests
		
		
		if( request.sendWelcomeMail() )
			UserDataUtil.sendWelcomeMail( request.getUserId(), language );
		
		if( request.sendEmailVerificationMail() ) {
			if( request.getUserId() != null )
				UserDataUtil.sendEmailVerificationMail( request.getUserId(), language );
			else
				UserDataUtil.sendEmailVerificationMail( request.getEmail(), language );
		}

		if( request.sendPasswordResetMail() )
			UserDataUtil.sendPasswordResetMail( request.getEmail(), language );
		
		if( request.sendBirthdayMail() )
			throw new InvalidArgumentException( "Feature not yet supported !" );

		
		return new GenericResponse();
		
	}
	
}
