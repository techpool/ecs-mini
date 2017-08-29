package com.pratilipi.api.impl.author;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.data.util.AuthorDataUtil;

@SuppressWarnings("serial")
@Bind( uri = "/author/cover/remove" )
public class AuthorCoverRemoveApi extends GenericApi {

	public static class PostRequest extends GenericRequest {

		@Validate( required = true, minLong = 1L )
		private Long authorId;

	}

	
	@Post
	public GenericResponse post( PostRequest request ) throws InsufficientAccessException {

		AuthorDataUtil.removeAuthorImage( request.authorId, true, false );
		return new GenericResponse();
		
	}		

}
