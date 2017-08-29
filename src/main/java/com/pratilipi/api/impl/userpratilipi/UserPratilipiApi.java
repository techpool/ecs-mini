package com.pratilipi.api.impl.userpratilipi;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.pratilipi.PratilipiContentIndexApi;
import com.pratilipi.api.impl.user.UserV1Api;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.UserReviewState;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.client.UserPratilipiData;
import com.pratilipi.data.util.UserPratilipiDataUtil;
import com.pratilipi.filter.AccessTokenFilter;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/userpratilipi" )
public class UserPratilipiApi extends GenericApi {
	
	public static class GetRequest extends GenericRequest {

		@Validate( required = true, minLong = 1L )
		private Long pratilipiId;

		public void setPratilipiId( Long pratilipiId ) {
			this.pratilipiId = pratilipiId;
		}

	}
	
	public static class Response extends GenericResponse {

		private String userPratilipiId;
		
		private Long userId;
		private Long pratilipiId;
		
		@Deprecated
		private String userName;
		@Deprecated
		private String userImageUrl;
		@Deprecated
		private String userProfilePageUrl;
		
		private UserV1Api.Response user;
		
		private Integer rating;
		private String review;
		private UserReviewState reviewState;
		private Long reviewDateMillis;
		
		private Long likeCount;
		private Long commentCount;
		
		private Boolean addedToLib;
		
		private Boolean hasAccessToReview;
		
		private Boolean isLiked;

		
		private Response() {}
		
		private Response( UserPratilipiData userPratilipiData ) {
			
			this( userPratilipiData, UserPratilipiApi.class );
			
		}
		
		public Response( UserPratilipiData userPratilipiData, Class<? extends GenericApi> clazz ) {

			if( clazz == UserPratilipiApi.class || clazz == UserPratilipiReviewApi.class
					|| clazz == UserPratilipiLibraryApi.class ) {
				
				userPratilipiId = userPratilipiData.getId();
				
				userId = userPratilipiData.getUserId();
				pratilipiId = userPratilipiData.getPratilipiId();
				
				userName = userPratilipiData.getUserName();
				userImageUrl = userPratilipiData.getUserImageUrl();
				userProfilePageUrl = userPratilipiData.getUserProfilePageUrl();

				user = new UserV1Api.Response( userPratilipiData.getUser(), UserPratilipiApi.class );
				
				rating = userPratilipiData.getRating();
				review = userPratilipiData.getReview();
				reviewState = userPratilipiData.getReviewState();
				reviewDateMillis = userPratilipiData.getReviewDate() == null ? null : userPratilipiData.getReviewDate().getTime();

				likeCount = userPratilipiData.getLikeCount();
				commentCount = userPratilipiData.getCommentCount();
				
				addedToLib = userPratilipiData.isAddedToLib();
				
				hasAccessToReview = userPratilipiData.hasAccessToReview();

				isLiked = userPratilipiData.isLiked();
				
			} else if( clazz == UserPratilipiReviewListApi.class ) {
				
				userPratilipiId = userPratilipiData.getId();
				
				userName = userPratilipiData.getUserName();
				userImageUrl = userPratilipiData.getUserImageUrl();
				userProfilePageUrl = userPratilipiData.getUserProfilePageUrl();
	
				user = new UserV1Api.Response( userPratilipiData.getUser(), clazz );
				
				rating = userPratilipiData.getRating();
				review = userPratilipiData.getReview();
				reviewState = userPratilipiData.getReviewState();
				reviewDateMillis = userPratilipiData.getReviewDate() == null ? null : userPratilipiData.getReviewDate().getTime();
	
				commentCount = userPratilipiData.getCommentCount();
				likeCount = userPratilipiData.getLikeCount();
				
				isLiked = userPratilipiData.isLiked();
				
			}

		}

		
		public String getId() {
			return userPratilipiId;
		}
		
		public Long getUserId() {
			return userId;
		}
		
		public Long getPratilipiId() {
			return pratilipiId;
		}

		
		public String getUserName() {
			return userName;
		}
		
		public String getUserImageUrl() {
			return userImageUrl;
		}

		public String getUserImageUrl( int width ) {
			return userImageUrl.indexOf( '?' ) == -1
					? userImageUrl + "?width=" + width
					: userImageUrl + "&width=" + width;
		}

		public String getUserProfilePageUrl() {
			return userProfilePageUrl;
		}
		

		public UserV1Api.Response getUser() {
			return user;
		}
		

		public Integer getRating() {
			return rating;
		}
		
		public String getReview() {
			return review;
		}
		
		public UserReviewState getReviewState() {
			return reviewState;
		}
		
		public Date getReviewDate() {
			return reviewDateMillis == null ? null : new Date( reviewDateMillis );
		}
		
		public Long getReviewDateMillis() {
			return reviewDateMillis == null ? null : reviewDateMillis;
		}
		
		
		public Long getLikeCount() {
			return likeCount;
		}

		public Long getCommentCount() {
			return commentCount;
		}

		
		public Boolean isAddedtoLib() {
			return addedToLib;
		}
		

		public Boolean hasAccessToReview() {
			return hasAccessToReview;
		}

		
		public boolean isLiked() {
			return isLiked == null ? false : isLiked;
		}

	}
	
	
	@Get
	public Response getUserPratilipi( GetRequest request ) throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "pratilipiId", request.pratilipiId.toString() );
		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/userpratilipi", paramsMap );
		return new Gson().fromJson( responseString, Response.class );

	}		

}
