package com.pratilipi.api.impl.user;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
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
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.UserState;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.common.util.UserAccessUtil;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.util.UserDataUtil;
import com.pratilipi.filter.UxModeFilter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
@Bind( uri= "/user", ver = "2" )
public class UserV2Api extends UserV1Api {

	public static class GetRequest extends GenericRequest {
		private Long userId;
	}

	
	@SuppressWarnings("unused")
	public static class Response extends GenericResponse {
		
		private Long userId;
		private AuthorApi.Response author;
		private String displayName;
		private Boolean password;
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
		
		public Response( UserData userData, Class<? extends GenericApi> clazz ) {
			
			if( clazz == UserV2Api.class
					|| clazz == UserLoginApi.class || clazz == UserLoginFacebookApi.class || clazz == UserLoginGoogleApi.class
					|| clazz == UserLogoutApi.class
					|| clazz == UserRegisterApi.class
					|| clazz == UserPasswordUpdateApi.class
					|| clazz == UserVerificationApi.class ) {
				
				this.userId = userData.getId();
				this.author = new AuthorApi.Response( userData.getAuthor(), UserLoginApi.class );
				this.displayName = userData.getDisplayName();
				this.password = userData.hasPassword();
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
	public Response get( GetRequest request ) throws InsufficientAccessException, UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "_apiVer", "2" );
		String userResponse = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/user", paramsMap );
		return new Gson().fromJson( userResponse, UserV2Api.Response.class );

	}
	
}
