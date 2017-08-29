package com.pratilipi.api.impl.pratilipi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.client.TagData;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.util.TagDataUtil;
import com.pratilipi.filter.AccessTokenFilter;

@SuppressWarnings("serial")
@Bind(uri = "/pratilipi/tags")
public class TagsApi extends GenericApi {

	private static Logger logger = Logger.getLogger(TagsApi.class.getSimpleName());

	public static class Request extends GenericRequest {

		@Validate(required=true)
		Language language;

		PratilipiType type;

		public Language getLanguage() {
			return language;
		}

		public void setLanguage(Language language) {
			this.language = language;
		}

		public PratilipiType getType() {
			return type;
		}

		public void setType(PratilipiType type) {
			this.type = type;
		}


	}

	public static class Response extends GenericResponse {
		List<ResponseObject> response;

		public Response() {}

		public Response(List<ResponseObject> response) {
			this.response = response;
		}

		public static class ResponseObject {
			PratilipiType pratilipiType;
			String title;
			List<TagData> tags;

			public ResponseObject(PratilipiType pratilipiType, String title, List<TagData> tags) {
				this.pratilipiType = pratilipiType;
				this.title = title;
				this.tags = tags;
			}

			public List<TagData> getTagDataList() {
				return tags;
			}
		}

		public List<ResponseObject> getResponseObject() {
			return response;
		}
	}

	@SuppressWarnings("unused")
	public static class PostRequest extends GenericRequest {

		private String id;

		private String name;
		private boolean hasName;

		private String nameEn;
		private boolean hasNameEn;

		private Language language;
		private boolean hasLanguage;

		private PratilipiType type;
		private boolean hasType;
	}

	@Get
	public Response getTags(Request request) throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "language", request.language.name() );
		if( request.type != null )
			paramsMap.put( "type", request.type.name() );
		String responseString = HttpUtil.doGet( "http://hindi.pratilipi.com" + "/api/pratilipi/tags", paramsMap );
		return new Gson().fromJson( responseString, Response.class );

	}

	@Post
	public GenericResponse addTags(PostRequest request) throws InsufficientAccessException {
		// Security Hack
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if(accessToken.getUserId() != 5073076857339904L) {
			Logger.getLogger(TagsApi.class.getSimpleName())
					.log(Level.SEVERE, "AccessToken : " + accessToken.getId());
			Logger.getLogger(TagsApi.class.getSimpleName())
					.log(Level.SEVERE, "User Id : " + accessToken.getUserId());
			throw new InsufficientAccessException();
		}

		Gson gson = new Gson();
		// Creating TagData object.
		TagData tagData = gson.fromJson( gson.toJson( request ), TagData.class );

		tagData = TagDataUtil.saveTag(tagData);


		return new GenericResponse();
	}

}
