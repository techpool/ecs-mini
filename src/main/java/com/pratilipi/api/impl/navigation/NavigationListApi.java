package com.pratilipi.api.impl.navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.user.UserV2Api;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.client.NavigationData;
import com.pratilipi.data.type.Navigation;
import com.pratilipi.data.util.NavigationDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/navigation/list" )
public class NavigationListApi extends GenericApi {
	
	public static class GetRequest extends GenericRequest {
		
		@Validate( required = true )
		private Language language;
		
		
		public Language getLanguage() {
			return this.language;
		}

		public void setLanguage( Language language ) {
			this.language = language;
		}
		
	}
	
	public static class Response extends GenericResponse {

		@SuppressWarnings("unused")
		private List<NavigationData> navigationList;
		
		
		public Response( List<NavigationData> navigationList ) {
			this.navigationList = navigationList;
		}

		public List<NavigationData> getNavigationList() { return navigationList; }

	}

	
	@Get
	public Response get( GetRequest request ) throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "language", request.language != null ? request.language.name() : Language.ENGLISH.name() );
		String userResponse = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/navigation/list", paramsMap );
		return new Gson().fromJson( userResponse, NavigationListApi.Response.class );

	}

}
