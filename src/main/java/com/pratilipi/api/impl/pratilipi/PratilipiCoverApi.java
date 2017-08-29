package com.pratilipi.api.impl.pratilipi;

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
import com.pratilipi.data.util.PratilipiDataUtil;

@SuppressWarnings("serial")
@Bind( uri = "/pratilipi/cover" )
public class PratilipiCoverApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		private Long pratilipiId;

		private Integer width;
		
	}
	
	public static class PostRequest extends GenericFileUploadRequest {

		@Validate( required = true )
		private Long pratilipiId;
		
	}
	
	public static class PostResponse extends GenericResponse {
		
		@SuppressWarnings("unused")
		private String coverImageUrl;
		
		private PostResponse( String coverImageUrl ) {
			this.coverImageUrl = coverImageUrl;
		}
		
	}

	
	@Get
	public GenericFileDownloadResponse get( GetRequest request )
			throws UnexpectedServerException {

		BlobEntry blobEntry = PratilipiDataUtil.getPratilipiCover(
				request.pratilipiId,
				request.width );
		
		return new GenericFileDownloadResponse(
				blobEntry.getData(),
				blobEntry.getMimeType(),
				blobEntry.getETag() );
		
	}

	@Post
	public PostResponse post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		BlobEntry blobEntry = DataAccessorFactory.getBlobAccessor().newBlob( request.getName() );
		blobEntry.setData( request.getData() );
		blobEntry.setMimeType( request.getMimeType() );
		blobEntry.setMetaName( request.getName() );
		
		String coverImageUrl = PratilipiDataUtil.savePratilipiCover(
				request.pratilipiId,
				blobEntry );

		return new PostResponse( coverImageUrl );
		
	}		

}
