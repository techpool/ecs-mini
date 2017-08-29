package com.pratilipi.api.impl.user;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.author.AuthorApi;
import com.pratilipi.api.impl.author.AuthorListApi;
import com.pratilipi.api.impl.blogpost.BlogPostApi;
import com.pratilipi.api.impl.blogpost.BlogPostListApi;
import com.pratilipi.api.impl.comment.CommentApi;
import com.pratilipi.api.impl.userauthor.UserAuthorFollowListApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiReviewListApi;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.UserState;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.util.AuthorDataUtil;
import com.pratilipi.data.util.UserDataUtil;
import com.pratilipi.filter.AccessTokenFilter;
import com.pratilipi.filter.UxModeFilter;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;

@SuppressWarnings("serial")
@Bind( uri= "/user" )
public class UserV1Api extends GenericApi {

	public static class PostRequest extends GenericRequest {
		
		// userId == null, User updating his/her own data
		// userId == 0L, AEE adding new User
		// userId > 0L, AEE updating data on User's behalf
		@Validate( minLong = 0L )
		private Long userId;

		private String name;
		private boolean hasName;

		@Validate( regEx = REGEX_EMAIL, regExErrMsg = ERR_EMAIL_INVALID )
		private String email;
		private boolean hasEmail;

		@Validate( regEx = REGEX_PHONE, regExErrMsg = ERR_PHONE_INVALID )
		private String phone;
		private boolean hasPhone;

		private Language language;
		private boolean hasLanguage;

	}

	@SuppressWarnings("unused")
	public static class Response extends GenericResponse {
		
		private Long userId;
		@Deprecated
		private Long authorId;
		private AuthorApi.Response author;
		private String displayName;
		private String email;
		private String phone;
		private UserState state;

		private Boolean isGuest;
		private Boolean isEmailVerified;
		
		private String profilePageUrl;
		private String profileImageUrl;

		private Long followCount;
		private Boolean following;
		
		private String firebaseToken;

		
		private Response() {}
		
		private Response( UserData userData ) {
			this( userData, UserV1Api.class );
		}
		
		public Response( UserData userData, Class<? extends GenericApi> clazz ) {
			
			if( clazz == UserV1Api.class
					|| clazz == UserLoginApi.class || clazz == UserLoginFacebookApi.class || clazz == UserLoginGoogleApi.class
					|| clazz == UserLogoutApi.class
					|| clazz == UserRegisterApi.class
					|| clazz == UserPasswordUpdateApi.class
					|| clazz == UserVerificationApi.class ) {
				
				this.userId = userData.getId();
				this.authorId = userData.getAuthor().getId();
				this.author = new AuthorApi.Response( userData.getAuthor(), UserLoginApi.class );
				this.displayName = userData.getDisplayName();
				this.email = userData.getEmail();
				this.phone = userData.getPhone();
				this.state = userData.getState();
				this.isGuest = userData.getState() == UserState.GUEST;
				this.isEmailVerified = userData.getState() == UserState.ACTIVE;
				this.profilePageUrl = userData.getProfilePageUrl();
				this.profileImageUrl = userData.getProfileImageUrl();
				this.firebaseToken = userData.getFirebaseToken();
				
			} else if( clazz == AuthorApi.class ) {
				
				this.userId = userData.getId();
				this.followCount = userData.getFollowCount();
				
			} else if( clazz == AuthorListApi.class ) {
				
				this.userId = userData.getId();
				this.email = userData.getEmail();
				this.phone = userData.getPhone();
				this.state = userData.getState();
				
			} else if( clazz == BlogPostApi.class || clazz == BlogPostListApi.class ) {
				
				this.displayName = userData.getDisplayName();
				this.profilePageUrl = userData.getProfilePageUrl();
				this.profileImageUrl = userData.getProfileImageUrl();
				
			} else if( clazz == UserPratilipiApi.class || clazz == UserPratilipiReviewListApi.class || clazz == CommentApi.class ) {
				
				this.userId = userData.getId();
				if( UxModeFilter.isAndroidApp() )
					this.author = new AuthorApi.Response( userData.getAuthor(), clazz );
				this.displayName = userData.getDisplayName();
				this.profilePageUrl = userData.getProfilePageUrl();
				this.profileImageUrl = userData.getProfileImageUrl();
				
			} else if( clazz == UserAuthorFollowListApi.class ) {

				if( userData.getAuthor() == null ) {
					this.userId = userData.getId();
					this.followCount = userData.getFollowCount();
					this.following = userData.isFollowing();
				} else {
					this.userId = userData.getId();
					this.author = new AuthorApi.Response( userData.getAuthor(), clazz );
					this.displayName = userData.getDisplayName();
					this.profilePageUrl = userData.getProfilePageUrl();
					this.profileImageUrl = userData.getProfileImageUrl();
					this.followCount = userData.getFollowCount();
					this.following = userData.isFollowing();
				}
			
			}
			
		}
		
		
		public Long getId() {
			return userId;
		}

		public AuthorApi.Response getAuthor() {
			return author;
		}

		public String getDisplayName() {
			return displayName;
		}

		public String getEmail() {
			return email;
		}

		public String getPhone() {
			return phone;
		}

		public UserState getState() {
			return state;
		}

		public Boolean isGuest() {
			return isGuest;
		}

		public Boolean isEmailVerified() {
			return isEmailVerified;
		}
		
		public String getProfilePageUrl() {
			return profilePageUrl;
		}
		
		public String getProfileImageUrl() {
			return profileImageUrl;
		}
		
		public String getProfileImageUrl( int width ) {
			return profileImageUrl.indexOf( '?' ) == -1
					? profileImageUrl + "?width=" + width
					: profileImageUrl + "&width=" + width;
		}
		
		public Long getFollowCount() {
			return followCount;
		}
		
		public boolean isFollowing() {
			return following;
		}
		
	}


	@Get
	public Response get( GenericRequest request ) {
		return new Response( UserDataUtil.getCurrentUser(), UserV1Api.class );
	}

	@Post
	public Response post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException {

		UserData userData = request.userId == null
				? new UserData( AccessTokenFilter.getAccessToken().getUserId() )
				: new UserData( request.userId.equals( 0L ) ? null : request.userId );

		if( request.hasName ) {
			String firstName = request.name.trim();
			String lastName = null;
			if( firstName.lastIndexOf( ' ' ) != -1 ) {
				lastName = firstName.substring( firstName.lastIndexOf( ' ' ) + 1 );
				firstName = firstName.substring( 0, firstName.lastIndexOf( ' ' ) );
			}
			userData.setFirstName( firstName );
			userData.setLastName( lastName );
		}
		if( request.hasEmail )
			userData.setEmail( request.email );
		if( request.hasPhone )
			userData.setPhone( request.phone );
		if( request.hasLanguage )
			userData.setLanguage( request.language );
		
		// Save UserData
		userData = UserDataUtil.saveUserData( userData );

		
		// New User (added by AEE)
		if( request.userId != null && request.userId.equals( 0L ) ) {
			
			// Create Author profile for the User
			Long authorId = AuthorDataUtil.createAuthorProfile(
					userData,
					request.language == null ? UxModeFilter.getFilterLanguage() : request.language );
			
			userData.setAuthor( new AuthorData( authorId ) );
			userData.setProfilePageUrl( "/author/" + authorId );

			// Send welcome mail to the user
			Task task = TaskQueueFactory.newTask()
					.setUrl( "/user/email" )
					.addParam( "userId", userData.getId().toString() )
					.addParam( "language", request.language == null ? UxModeFilter.getDisplayLanguage().toString() : request.language.toString() )
					.addParam( "sendWelcomeMail", "true" );
			TaskQueueFactory.getUserTaskQueue().add( task );
			
		}


		// Send verification mail if user updates his email and state is REGISTERED
//		if( request.hasEmail && userData.getState() == UserState.REGISTERED ) {
//			Task task = TaskQueueFactory.newTask()
//					.setUrl( "/user/email" )
//					.addParam( "userId", userData.getId().toString() )
//					.addParam( "language", ( userData.getLanguage() == null ? Language.ENGLISH : userData.getLanguage() ).toString() )
//					.addParam( "sendEmailVerificationMail", "true" );
//			TaskQueueFactory.getUserTaskQueue().add( task );
//		}
		
		
		// Process Author data
		Task task = TaskQueueFactory.newTask()
				.setUrl( "/author/process" )
				.addParam( "authorId", userData.getAuthor().getId().toString() )
				.addParam( "processData", "true" );
		TaskQueueFactory.getAuthorTaskQueue().add( task );
		
		
		return new Response( userData );
		
	}
	
}
