package com.pratilipi.api.impl.event;

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
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.util.EventDataUtil;

@SuppressWarnings("serial")
@Bind( uri = "/event/banner" )
public class EventBannerApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		private Long eventId;
		private Integer width;
		
	}
	
	public static class PostRequest extends GenericFileUploadRequest {
	
		@Validate( required = true )
		private Long eventId;
	
	}

	
	
	@Get
	public GenericFileDownloadResponse get( GetRequest request )
			throws UnexpectedServerException {

		BlobEntry blobEntry = EventDataUtil.getEventBanner(
				request.eventId,
				request.width );
		
		return new GenericFileDownloadResponse(
				blobEntry.getData(),
				blobEntry.getMimeType(),
				blobEntry.getETag() );
		
	}

	@Post
	public GenericResponse post( PostRequest request )
			throws InsufficientAccessException, UnexpectedServerException {

		BlobEntry blobEntry = DataAccessorFactory.getBlobAccessor().newBlob( request.getName() );
		blobEntry.setData( request.getData() );
		blobEntry.setMimeType( request.getMimeType() );
		
		EventDataUtil.saveEventBanner(
				request.eventId,
				blobEntry );

		return new GenericResponse();
		
	}
	
}
