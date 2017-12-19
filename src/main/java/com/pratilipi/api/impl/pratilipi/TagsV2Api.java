package com.pratilipi.api.impl.pratilipi;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.client.CategoryData;
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

		CategoryData STORY;
		CategoryData ARTICLE;
		CategoryData POEM;

		public Response() {};

		public Response( CategoryData STORY, CategoryData ARTICLE, CategoryData POEM ) {
			this.STORY = STORY;
			this.ARTICLE = ARTICLE;
			this.POEM = POEM;
		}

		public CategoryData getTagDataList( PratilipiType type ) {
			if( type == PratilipiType.STORY )
				return STORY;
			else if( type == PratilipiType.ARTICLE )
				return ARTICLE;
			else if( type == PratilipiType.POEM )
				return POEM;
			else
				return null;
		}

		public CategoryData getStoryTagDataList() {
			return STORY;
		}

		public CategoryData getArticleTagDataList() {
			return ARTICLE;
		}

		public CategoryData getPoemTagDataList() {
			return POEM;
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
