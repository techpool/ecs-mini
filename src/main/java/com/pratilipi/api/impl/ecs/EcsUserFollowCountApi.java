package com.pratilipi.api.impl.ecs;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.User;

@SuppressWarnings( "serial" )
@Bind( uri = "/ecs/user/followcount" )
public class EcsUserFollowCountApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		@Validate( required = true )
		private Long userId;

	}

	public static class Response extends GenericResponse {

		Long userId;
		Long followCount;

		public Response( Long userId, Long followCount ) {
			this.userId = userId;
			this.followCount = followCount;
		}

	}

	@Get
	public Response get( GetRequest request ) {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		User user = dataAccessor.getUser( request.userId );
		return new Response( user.getId(), user.getFollowCount() );

	}

}
