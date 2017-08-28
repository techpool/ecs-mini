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
import com.pratilipi.data.util.PratilipiDocUtil;

@SuppressWarnings("serial")
@Bind( uri = "/pratilipi/content/image" )
public class PratilipiContentImageApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		@Validate( required = true, requiredErrMsg = ERR_PRATILIPI_ID_REQUIRED, minLong = 1L )
		private Long pratilipiId;

		@Deprecated
		@Validate( minInt = 1 )
		private Integer pageNo;

		private String name;

		private Integer width;

	}

	public static class PostRequest extends GenericFileUploadRequest {

		@Validate( required = true, minLong = 1L, requiredErrMsg = ERR_PRATILIPI_ID_REQUIRED )
		private Long pratilipiId;

	}

	@SuppressWarnings("unused")
	public static class PostResponse extends GenericResponse {

		private String name;


		private PostResponse() {}

		public PostResponse( String name ) {
			this.name = name;
		}

	}


	@Get
	public GenericFileDownloadResponse get( GetRequest request )
			throws InsufficientAccessException, UnexpectedServerException {

		BlobEntry blobEntry = PratilipiDocUtil.getContentImage(
					request.pratilipiId,
					request.name == null ? request.pageNo.toString() : request.name,
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

		String imageName = PratilipiDocUtil.saveContentImage(
								request.pratilipiId, 
								blobEntry );

		return new PostResponse( imageName );

	}		

}
