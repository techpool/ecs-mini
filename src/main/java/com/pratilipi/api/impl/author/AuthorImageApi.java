package com.pratilipi.api.impl.author;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericFileDownloadResponse;
import com.pratilipi.api.shared.GenericFileUploadRequest;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.util.AuthorDataUtil;

@SuppressWarnings("serial")
@Bind( uri = "/author/image" )
public class AuthorImageApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		private Long authorId;

		private String version;
		
		private Integer width;
		
	}
	
	public static class PostRequest extends GenericFileUploadRequest {

		@Validate( required = true, minLong = 1L )
		private Long authorId;

	}

	public static class Response extends GenericResponse {
		
		@SuppressWarnings("unused")
		private String coverImageUrl;
		
		private Response( String coverImageUrl ) {
			this.coverImageUrl = coverImageUrl;
		}
		
	}

	
	@Get
	public GenericFileDownloadResponse get( GetRequest request )
			throws UnexpectedServerException {

		BlobEntry blobEntry = (BlobEntry) AuthorDataUtil.getAuthorProfileImage(
				request.authorId,
				request.version,
				request.width );
		
		return new GenericFileDownloadResponse(
				blobEntry.getData(),
				blobEntry.getMimeType(),
				blobEntry.getETag() );
		
	}

	@Post
	public GenericResponse post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		BlobEntry blobEntry = DataAccessorFactory.getBlobAccessor().newBlob( request.getName() );
		blobEntry.setData( request.getData() );
		blobEntry.setMimeType( request.getMimeType() );
		blobEntry.setMetaName( request.getName() );
		
		String profileImageUrl = AuthorDataUtil.saveAuthorImage(
				request.authorId,
				blobEntry );

		return new Response( profileImageUrl );
		
	}		

}
