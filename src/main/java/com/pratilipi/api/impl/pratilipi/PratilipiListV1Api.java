package com.pratilipi.api.impl.pratilipi;

import java.util.ArrayList;
import java.util.List;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.common.util.PratilipiFilter;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.type.Event;
import com.pratilipi.data.type.PratilipiContentDoc;
import com.pratilipi.data.util.EventDataUtil;
import com.pratilipi.data.util.PratilipiDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/pratilipi/list" )
public class PratilipiListV1Api extends GenericApi {

	public static class GetRequest extends GenericRequest {

		protected String searchQuery;
		protected Long authorId;
		protected Long eventId;
		protected Language language;
		protected PratilipiType type;
		protected String listName;
		protected PratilipiState state;

		protected Boolean orderByLastUpdated;
		protected Boolean orderByListingDate;

		protected String cursor;
		protected Integer offset;
		protected Integer resultCount;


		public void setSearchQuery( String searchQuery ) {
			this.searchQuery = searchQuery;
		}

		public void setAuthorId( Long authorId ) {
			this.authorId = authorId;
		}

		public void setEventId( Long eventId ) {
			this.eventId = eventId;
		}

		public void setLanguage( Language language ) {
			this.language = language;
		}

		public void setType( PratilipiType type ) {
			this.type = type;
		}

		public void setListName( String listName ) {
			this.listName = listName;
		}

		public void setState( PratilipiState state ) {
			this.state = state;
		}


		public void setOrderByLastUpdate( Boolean orderByLastUpdated ) {
			this.orderByLastUpdated = orderByLastUpdated;
		}

		public void setOrderByListingDate( Boolean orderByListingDate ) {
			this.orderByListingDate = orderByListingDate;
		}


		public void setCursor( String cursor ) {
			this.cursor = cursor;
		}

		public void setOffset( Integer offset ) {
			this.offset = offset;
		}

		public void setResultCount( Integer resultCount ) {
			this.resultCount = resultCount;
		}

	}
	
	@SuppressWarnings("unused")
	public static class Response extends GenericResponse { 

		private String bannerUrl;
		private String description;
		private List<PratilipiV1Api.Response> pratilipiList;
		private String cursor;
		private Long numberFound;

		
		private Response() {}
		
		public Response( List<PratilipiData> pratilipiList, String cursor, Long numberFound ) {
			this( pratilipiList, cursor, numberFound, PratilipiListV1Api.class );
		}

		public Response( List<PratilipiData> pratilipiList, String cursor, Long numberFound, Class<? extends GenericApi> clazz ) {
			this.pratilipiList = new ArrayList<>( pratilipiList.size() );
			for( PratilipiData pratilipi : pratilipiList )
				this.pratilipiList.add( new PratilipiV1Api.Response( pratilipi, clazz ) );
			this.cursor = cursor;
			this.numberFound = numberFound;
		}


		public void setBannerUrl( String bannerUrl ) {
			this.bannerUrl = bannerUrl;
		}
		
		public void setDescription( String description ) {
			this.description = description;
		}
		
		public List<PratilipiV1Api.Response> getPratilipiList() {
			return pratilipiList;
		}

		public String getCursor() {
			return cursor;
		}

		public Long getNumberFound() {
			return numberFound;
		}

	}

	
	@Get
	public Response get( GetRequest request )
			throws InsufficientAccessException, UnexpectedServerException {

		PratilipiFilter pratilipiFilter = new PratilipiFilter();
		pratilipiFilter.setAuthorId( request.authorId );
		pratilipiFilter.setLanguage( request.language );
		pratilipiFilter.setType( request.type );
		pratilipiFilter.setListName( request.listName );
		pratilipiFilter.setState( request.state );
		pratilipiFilter.setOrderByListingDate( request.orderByListingDate );
		pratilipiFilter.setOrderByLastUpdate( request.orderByLastUpdated );
		
		DataListCursorTuple<PratilipiData> pratilipiListCursorTuple =
				PratilipiDataUtil.getPratilipiDataList(
						request.searchQuery,
						request.eventId,
						pratilipiFilter,
						request.cursor,
						request.offset,
						request.resultCount == null ? 20 : request.resultCount );

		
		// Preparing & returning response object.
		
		if( UxModeFilter.isAndroidApp() ) {
			DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
			for( PratilipiData pratilipiData : pratilipiListCursorTuple.getDataList() ) {
				PratilipiContentDoc pcDoc = docAccessor.getPratilipiContentDoc( pratilipiData.getId() );
				if( pcDoc == null )
					continue;
				pratilipiData.setIndex( pcDoc.getIndex() );
			}
		}
		
		Response response = new Response(
				pratilipiListCursorTuple.getDataList(),
				pratilipiListCursorTuple.getCursor(),
				pratilipiListCursorTuple.getNumberFound() );
		
		if( UxModeFilter.isAndroidApp() && request.eventId != null && request.cursor == null ) {
			Event event = DataAccessorFactory.getDataAccessor().getEvent( request.eventId );
			response.setBannerUrl( EventDataUtil.createEventBannerUrl( event ) );
			response.setDescription( event.getDescription() );
		}
		
		return response;
		
	}

}
