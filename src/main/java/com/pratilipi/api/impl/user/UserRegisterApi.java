package com.pratilipi.api.impl.user;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Sensitive;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.util.AuthorDataUtil;
import com.pratilipi.data.util.UserDataUtil;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;


@SuppressWarnings("serial")
@Bind( uri= "/user/register" )
public class UserRegisterApi extends GenericApi {

	public static class PostRequest extends GenericRequest {

		@Validate( required = true, requiredErrMsg = ERR_NAME_REQUIRED )
		private String name;

		@Validate( required = true, requiredErrMsg = ERR_EMAIL_REQUIRED, regEx = REGEX_EMAIL, regExErrMsg = ERR_EMAIL_INVALID )
		private String email;

		@Sensitive
		@Validate( required = true, requiredErrMsg = ERR_PASSWORD_REQUIRED, regEx = REGEX_PASSWORD, regExErrMsg = ERR_PASSWORD_INVALID )
		private String password;

		@Validate( required = true )
		private Language language;
		
	}
	
	
	@Post
	public UserV1Api.Response post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		String firstName = request.name.trim();
		String lastName = null;
		String email = request.email.trim().toLowerCase();
		
		if( firstName.lastIndexOf( ' ' ) != -1 ) {
			lastName = firstName.substring( firstName.lastIndexOf( ' ' ) + 1 );
			firstName = firstName.substring( 0, firstName.lastIndexOf( ' ' ) );
		}
		

		// Register the User.
		UserData userData = UserDataUtil.registerUser( firstName, lastName,
				email, request.password,
				UserDataUtil.getUserSignUpSource( false, false ) );
		// Create Author profile for the User.
		Long authorId = AuthorDataUtil.createAuthorProfile( userData, request.language );
		// Log-in the User and get update UserData
		userData = UserDataUtil.loginUser( email, request.password );

		
		Task task1 = TaskQueueFactory.newTask()
				.setUrl( "/user/email" )
				.addParam( "userId", userData.getId().toString() )
				.addParam( "language", request.language.toString() )
				.addParam( "sendWelcomeMail", "true" );
//		Task task2 = TaskQueueFactory.newTask()
//				.setUrl( "/user/email" )
//				.addParam( "userId", userData.getId().toString() )
//				.addParam( "language", request.language.toString() )
//				.addParam( "sendEmailVerificationMail", "true" );
		Task task3 = TaskQueueFactory.newTask()
				.setUrl( "/author/process" )
				.addParam( "authorId", authorId.toString() )
				.addParam( "processData", "true" );

		TaskQueueFactory.getUserTaskQueue().addAll( task1 );
		TaskQueueFactory.getAuthorTaskQueue().add( task3 );

		
		return new UserV1Api.Response( userData, UserRegisterApi.class );
		
	}
	
}
