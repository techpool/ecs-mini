package com.pratilipi.api.impl.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.batchprocess.BatchProcessListApi;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.client.EventData;
import com.pratilipi.data.util.EventDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/event/list" )
public class EventListApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		private Language language;
		
		public void setLanguage( Language language ) {
			this.language = language;
		}

	}
	
	public static class GetResponse extends GenericResponse {

		private List<EventApi.Response> eventList;

		
		private GetResponse() {}
		
		private GetResponse( List<EventData> eventList ) {
			List<EventApi.Response> eventListResponse = new ArrayList<EventApi.Response>( eventList.size() );
			for( EventData eventData : eventList )
				eventListResponse.add( new EventApi.Response( eventData, EventListApi.class ) );
			this.eventList = eventListResponse;
		}
		
		
		public List<EventApi.Response> getEventList() {
			return eventList;
		}

	}

	
	@Get
	public GetResponse get( GetRequest request ) throws UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();

		if( request.language != null )
			paramsMap.put( "language", request.language.name() );

		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/event/list", paramsMap );
		return new Gson().fromJson( responseString, GetResponse.class );

	}

}
