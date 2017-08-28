package com.pratilipi.api.impl.pratilipi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.type.PratilipiContentDoc;
import com.pratilipi.data.util.PratilipiDocUtil;
import com.pratilipi.filter.UxModeFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("serial")
@Bind( uri = "/pratilipi/content/index" )
public class PratilipiContentIndexApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		@Validate( required = true, requiredErrMsg = ERR_PRATILIPI_ID_REQUIRED, minLong = 1L )
		private Long pratilipiId;


		public void setPratilipiId( Long pratilipiId )  {
			this.pratilipiId = pratilipiId;
		}

	}

	public static class PostRequest extends GenericRequest {

		@Validate( required = true, requiredErrMsg = ERR_PRATILIPI_ID_REQUIRED, minLong = 1L )
		private Long pratilipiId;

		@Validate( required = true )
		private List<String> chapterIdOrder;

	}

	public static class Response extends GenericResponse {

		private JsonArray index;

		
		@SuppressWarnings("unused")
		private Response() {}

		public Response( JsonArray index ) {
			this.index = index;
		}


		public JsonArray getIndex() {
			return index;
		}

	}

	
	@Get
	public Response getIndex( GetRequest request )
			throws InsufficientAccessException, UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "pratilipiId", request.pratilipiId.toString() );
		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/pratilipi/content/index", paramsMap );
		return new Gson().fromJson( responseString, Response.class );

	}

	@Post
	public Response post( PostRequest request )
			throws InsufficientAccessException, InvalidArgumentException, UnexpectedServerException {

		PratilipiContentDoc pcDoc = PratilipiDocUtil.saveContentIndex( request.pratilipiId, request.chapterIdOrder );

		// TODO: Update Asynchronously
		PratilipiDocUtil.updateFirebaseIndex( request.pratilipiId, pcDoc );

		return new Response( pcDoc.getIndex() );

	}

}
