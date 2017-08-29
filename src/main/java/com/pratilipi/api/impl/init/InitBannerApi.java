package com.pratilipi.api.impl.init;

import com.google.gson.JsonObject;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericFileDownloadResponse;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.client.InitBannerData;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.util.InitDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/init/banner" )
public class InitBannerApi extends GenericApi {
	
	public static class GetRequest extends GenericRequest {
		
		@Validate( required = true )
		private Language language;
		
		@Validate( required = true )
		private String name;
		
		private Integer width;
		
	}
	
	@SuppressWarnings("unused")
	public static class Response extends GenericResponse {

		@Deprecated
		private String bannerId;
		
		private String title;
		
		private String imageUrl;
		
		private String miniImageUrl;
		
		private String actionUrl;
		
		private String apiName;
		
		private JsonObject apiRequest;
		
		
		public Response( InitBannerData initBannerData ) {
			
			this.title = initBannerData.getTitle();
			this.imageUrl = initBannerData.getImageUrl();
			if( UxModeFilter.isAndroidApp() ) {
				this.bannerId = "this-field-is-deprecated";
				this.apiName = initBannerData.getApiName();
				this.apiRequest = initBannerData.getApiRequest();
			} else {
				this.miniImageUrl = initBannerData.getMiniImageUrl();
				this.actionUrl= initBannerData.getActionUrl();
			}
			
		}
		
	}
	
	
	@Get
	public GenericResponse get( GetRequest request )
			throws InvalidArgumentException, UnexpectedServerException {
		
		BlobEntry blobEntry = InitDataUtil.getInitBanner(
				request.language,
				request.name,
				request.width );
		
		return new GenericFileDownloadResponse(
				blobEntry.getData(),
				blobEntry.getMimeType(),
				blobEntry.getETag()	);
		
	}
	
}
