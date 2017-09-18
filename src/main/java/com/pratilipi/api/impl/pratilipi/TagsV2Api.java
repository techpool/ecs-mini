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
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind(uri = "/pratilipi/tags", ver = "2")
public class TagsV2Api extends GenericApi {

	private static Logger logger = Logger.getLogger(TagsV2Api.class.getSimpleName());

	public static class Request extends GenericRequest {

		@Validate(required=true)
		Language language;

		public Language getLanguage() {
			return language;
		}

		public void setLanguage(Language language) {
			this.language = language;
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
			List<TagData> STORY;
			List<TagData> ARTICLE;
			List<TagData> POEM;

			public ResponseObject(PratilipiType pratilipiType, String title) {
				this.pratilipiType = pratilipiType;
				this.title = title;
			}

			public List<TagData> getTagDataList( String type ) {
				if( type.equals( "STORY" ) )
					return STORY;
				else if( type.equals( "ARTICLE" ) )
					return ARTICLE;
				else if( type.equals( "POEM" ) )
					return POEM;
				return null;
			}

			public List<TagData> getStoryTagDataList() {
				return STORY;
			}

			public List<TagData> getArticleTagDataList() {
				return ARTICLE;
			}

			public List<TagData> getPoemTagDataList() {
				return POEM;
			}

		}

		public List<ResponseObject> getResponseObject() {
			return response;
		}
	}

	@Get
	public Response getTags(Request request) throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "language", request.language.name() );
		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/pratilipi/v2/categories/system", paramsMap );
		return new Gson().fromJson( responseString, Response.class );

	}

}
