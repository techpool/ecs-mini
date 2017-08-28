package com.pratilipi.api.impl.page;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.user.UserV2Api;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.PageType;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Page;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings( "serial" )
@Bind( uri = "/page" )
public class PageApi extends GenericApi {

	private static final Logger logger = Logger.getLogger( PageApi.class.getName() );
	
	
	public static class GetRequest extends GenericRequest {
		
		@Validate( required = true )
		private String uri;

		public void setUri( String uri ) {
			this.uri = uri;
		}

	}
	
	@SuppressWarnings("unused")
	public static class Response extends GenericResponse {

		private PageType pageType;

		private Long primaryContentId;
		
		private String primaryContentName;

		private String uri;

		private String uriAlias;

		
		private Response() {}
		
		private Response( PageType pageType, Long primaryContentId ) {
			this.pageType = pageType;
			this.primaryContentId = primaryContentId;
		}
		
		private Response( PageType pageType, String primaryContentName ) {
			this.pageType = pageType;
			this.primaryContentName = primaryContentName;
		}


		public PageType getPageType() {
			return pageType;
		}

		public Long getPrimaryContentId() {
			return primaryContentId;
		}

		public String getPrimaryContentName() {
			return primaryContentName;
		}

		public String getUri() {
			return uri;
		}

		public String getUriAlias() {
			return uriAlias;
		}

	}
	
	
	@Get
	public Response get( GetRequest request ) throws InvalidArgumentException, UnexpectedServerException {

		if( request.uri.startsWith( "/read?id=" ) ) { // TODO: Remove this as soon as READ uri(s) are added to Page table.
			String pratilipiId = request.uri.substring( "/read?id=".length() );
			if( pratilipiId.indexOf( '&' ) != -1 )
				pratilipiId = pratilipiId.substring( 0, pratilipiId.indexOf( '&' ) );
			return new Response( PageType.READ, Long.parseLong( pratilipiId ) );
		}

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "uri", request.uri );
		try {
			String pageResponse = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/page", paramsMap );
			System.out.println( "Reached here" );
			System.out.println( "My theory sucks!!" );
			return new Gson().fromJson( pageResponse, PageApi.Response.class );
		} catch( UnexpectedServerException e ) {
			return null;
		}

	}

}
