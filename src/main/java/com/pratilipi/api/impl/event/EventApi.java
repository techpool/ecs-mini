package com.pratilipi.api.impl.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.author.AuthorApi;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.HtmlUtil;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.EventData;
import com.pratilipi.data.type.Event;
import com.pratilipi.data.util.EventDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/event" )
public class EventApi extends GenericApi {
	
	public static class GetRequest extends GenericRequest {

		@Validate( required = true, minLong = 1L )
		private Long eventId;

		
		public void setEventId( Long eventId ) {
			this.eventId = eventId;
		}

	}
	
	public static class PostRequest extends GenericRequest {

		@Validate( minLong = 1L )
		private Long eventId;

		private String name;
		private boolean hasName;
		
		private String nameEn;
		private boolean hasNameEn;
		
		private Language language;
		private boolean hasLanguage;

		private String description;
		private boolean hasDescription;
		
		private List<String> pratilipiUrlList;
		private boolean hasPratilipiUrlList;
		
		
		public Long getId() {
			return eventId;
		}

		public String getName() {
			return name;
		}

		public boolean hasName() {
			return hasName;
		}

		public String getNameEn() {
			return nameEn;
		}

		public boolean hasNameEn() {
			return hasNameEn;
		}

		public Language getLanguage() {
			return language;
		}

		public boolean hasLanguage() {
			return hasLanguage;
		}
		
		public String getDescription() {
			return description;
		}
		
		public boolean hasDescription() {
			return hasDescription;
		}
		
		public List<String> getPratilipiUrlList() {
			return pratilipiUrlList;
		}
		
		public boolean hasPratilipiUrlList() {
			return hasPratilipiUrlList;
		}
		
	}
	
	public static class Response extends GenericResponse {
		
		private Long eventId;
		
		private String name;
		private String nameEn;
		
		private Language language;
		private String description;
		private List<Long> pratilipiIdList;
		private List<String> pratilipiUrlList;
		
		private String pageUrl;
		private String bannerImageUrl;
		
		private Boolean hasAccessToUpdate;

		
		@SuppressWarnings("unused")
		private Response() {}
		
		private Response( EventData eventData ) {
			this.eventId = eventData.getId();
			this.name = eventData.getName();
			this.nameEn = eventData.getNameEn();
			this.language = eventData.getLanguage();
			this.description = eventData.getDescription();
			this.pratilipiIdList = eventData.getPratilipiIdList();
			this.pratilipiUrlList = eventData.getPratilipiUrlList();
			this.pageUrl = eventData.getPageUrl();
			this.bannerImageUrl = eventData.getBannerImageUrl();
			this.hasAccessToUpdate = eventData.hasAccessToUpdate();
		}
		
		public Response( EventData eventData, Class<? extends GenericApi> clazz ) {
			if( clazz == EventListApi.class ) {
				this.eventId = eventData.getId();
				this.name = eventData.getName();
				this.nameEn = eventData.getNameEn();
				this.language = eventData.getLanguage();
				if( eventData.getDescription() != null )
					this.description = HtmlUtil.toPlainText( eventData.getDescription() );
				this.pratilipiUrlList = eventData.getPratilipiUrlList();
				this.pageUrl = eventData.getPageUrl();
				this.bannerImageUrl = eventData.getBannerImageUrl();
				this.hasAccessToUpdate = eventData.hasAccessToUpdate();
			}
		}
		
		
		public Long getId() {
			return eventId;
		}
		
		
		public String getName() {
			return name;
		}
		
		public String getNameEn() {
			return nameEn;
		}
		
		
		public Language getLanguage() {
			return language;
		}
		
		public String getDescription() {
			return description;
		}
		
		public List<Long> getPratilipiIdList() { 
			return pratilipiIdList;
		}

		public List<String> getPratilipiUrlList() {
			return pratilipiUrlList;
		}

		
		public String getPageUrl() {
			return pageUrl;
		}

		public String getBannerImageUrl() {
			return bannerImageUrl;
		}

		public String getBannerImageUrl( Integer width ) {
			return bannerImageUrl + ( bannerImageUrl.indexOf( '?' ) == -1 ? "?" : "&" ) + "width=" + width;
		}
		
		public Boolean hasAccessToUpdate() {
			return hasAccessToUpdate;
		}
		
	}
	
	
	@Get
	public Response get( GetRequest request ) throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put( "eventId", request.eventId.toString() );
		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/event", paramsMap );
		return new Gson().fromJson( responseString, Response.class );

	}

	@Post
	public Response post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException {

		Gson gson = new Gson();

		EventData eventData = gson.fromJson( gson.toJson( request ), EventData.class );
		
		eventData = EventDataUtil.saveEventData( eventData );

		return new Response( eventData );
		
	}		

}
