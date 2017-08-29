package com.pratilipi.api.impl.user;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.util.AuthorDataUtil;
import com.pratilipi.data.util.UserDataUtil;
import com.pratilipi.filter.AccessTokenFilter;
import com.pratilipi.filter.UxModeFilter;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;

@SuppressWarnings("serial")
@Bind( uri= "/user/login/facebook" )  
public class UserLoginFacebookApi extends GenericApi {
	
	public static class PostRequest extends GenericRequest {
		
		@Validate( required = true )
		private String fbUserAccessToken;
		
		private Language language; // Used for SignUp only. Ignored for SingIn.

	}
	
	@Post
	public UserV1Api.Response post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {
		
		UserData userData = UserDataUtil.loginFacebookUser(
				request.fbUserAccessToken,
				UserDataUtil.getUserSignUpSource( true, false ) );

		
		List<Task> taskList = new LinkedList<>();
		

		Task fbValidationTask = TaskQueueFactory.newTask()
				.setUrl( "/user/facebook/validation" )
				.addParam( "accessToken", AccessTokenFilter.getAccessToken().getId() )
				.addParam( "fbUserAccessToken", request.fbUserAccessToken );
		taskList.add( fbValidationTask );

		
		if( new Date().getTime() - userData.getSignUpDate().getTime() <= 60000 ) {
			
			Long authorId = AuthorDataUtil.createAuthorProfile(
					userData,
					request.language == null ? UxModeFilter.getFilterLanguage() : request.language );
			userData = UserDataUtil.getCurrentUser(); // Fetching updated UserData

			if( userData.getEmail() != null ) {
				// Send welcome mail to the user
				Task welcomeMailTask = TaskQueueFactory.newTask()
						.setUrl( "/user/email" )
						.addParam( "userId", userData.getId().toString() )
						.addParam( "language", ( request.language == null ? ( UxModeFilter.getDisplayLanguage() == null ? Language.ENGLISH : UxModeFilter.getDisplayLanguage() ) : request.language ).toString() )
						.addParam( "sendWelcomeMail", "true" );
				taskList.add( welcomeMailTask );
			}
			
			// Process Author data
			Task task = TaskQueueFactory.newTask()
					.setUrl( "/author/process" )
					.addParam( "authorId", authorId.toString() )
					.addParam( "processData", "true" );
			TaskQueueFactory.getAuthorTaskQueue().add( task );
			
		}

		
		TaskQueueFactory.getUserTaskQueue().addAll( taskList );

		
		return new UserV1Api.Response( userData, UserLoginFacebookApi.class );
	
	}

}
