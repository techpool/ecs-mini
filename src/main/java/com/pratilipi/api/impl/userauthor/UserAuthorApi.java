package com.pratilipi.api.impl.userauthor;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.UserFollowState;
import com.pratilipi.data.client.UserAuthorData;
import com.pratilipi.data.util.UserAuthorDataUtil;
import com.pratilipi.filter.AccessTokenFilter;

@SuppressWarnings("serial")
@Bind( uri = "/userauthor" )
public class UserAuthorApi extends GenericApi {
	
	public static class GetRequest extends GenericRequest {

		@Validate( required = true )
		private Long authorId;

	}
	
	@SuppressWarnings("unused")
	public static class Response extends GenericResponse {

		private Boolean following;

		private Long followingSinceMillis;
		
		
		private Response() {}
		
		private Response( UserAuthorData userAuthorData ) {
			this.following = userAuthorData.getFollowState() == UserFollowState.FOLLOWING;
			if( this.following )
				this.followingSinceMillis = userAuthorData.getFollowDate().getTime();
		}
		
	}
	
	
	@Get
	public Response get( GetRequest request ) throws UnexpectedServerException {

		UserAuthorData userAuthorData = UserAuthorDataUtil.getUserAuthor(
				AccessTokenFilter.getAccessToken().getUserId(),
				request.authorId );

		return new Response( userAuthorData );
		
	}		

}
