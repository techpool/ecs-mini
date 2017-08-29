package com.pratilipi.api.impl.init;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.pratilipi.PratilipiV1Api;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.util.PratilipiFilter;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.type.PratilipiContentDoc;
import com.pratilipi.data.util.PratilipiDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/init" )
public class InitV1Api extends GenericApi {
	
	public static class GetRequest extends GenericRequest {
		
		@Validate( required = true )
		protected Language language;

		public void setLanguage( Language language ) {
			this.language = language;
		}

	}
	

	public static class Response extends GenericResponse {
		
		public static class Section {
		
			private String title;
			private String listPageUrl;
			private List<PratilipiV1Api.Response> pratilipiList;
			
			@SuppressWarnings("unused")
			private Section() {}
		
			protected Section( String title, String listPageUrl ) {
				this.title = title;
				this.listPageUrl = listPageUrl;
			}
			
			public void setPratilipiList( List<PratilipiData> pratilipiList ) {
				this.pratilipiList = new ArrayList<PratilipiV1Api.Response>( pratilipiList.size() );
				for( PratilipiData pratilipiData : pratilipiList )
					this.pratilipiList.add( new PratilipiV1Api.Response( pratilipiData, InitV1Api.class ) );
			}

			public String getTitle() {
				return title;
			}

			public String getListPageUrl() {
				return listPageUrl;
			}

			public List<PratilipiV1Api.Response> getPratilipiList() {
				return pratilipiList;
			}
		
		}
		
		
		private List<Section> sections;
	
		
		@SuppressWarnings("unused")
		private Response() {};
		
		protected Response( List<Section> sections ) {
			this.sections = sections;
		};

		
		public List<Section> getSectionList() {
			return sections;
		}
		
	}
	
	
	@Get
	public Response get( GetRequest request ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		List<Response.Section> sectionList = new LinkedList<>();
		List<Long> pratilipiIdMasterList = new LinkedList<>();
		
		for( String listName : dataAccessor.getHomeSectionList( request.language ) ) {
			
			String title = dataAccessor.getPratilipiListTitle( listName, request.language );
			if( title == null )
				continue;
			
			if( title.indexOf( '|' ) != -1 )
				title = title.substring( 0, title.indexOf( '|' ) ).trim();
			
			PratilipiFilter pratilipiFilter = new PratilipiFilter();
			pratilipiFilter.setLanguage( request.language );
			pratilipiFilter.setListName( listName );
			pratilipiFilter.setState( PratilipiState.PUBLISHED );
			
			List<Long> pratilipiIdList = dataAccessor.getPratilipiIdList( pratilipiFilter, null, null, 6 ).getDataList();
			if( pratilipiIdList.size() < 6 )
				continue;
			
			sectionList.add( new Response.Section( title, "/" + listName ) );
			pratilipiIdMasterList.addAll( pratilipiIdList );

		}
		
		List<PratilipiData> pratilipiDataMasterList = PratilipiDataUtil.createPratilipiDataList( pratilipiIdMasterList, true );
		
		if( UxModeFilter.isAndroidApp() ) {
			DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
			for( PratilipiData pratilipiData : pratilipiDataMasterList ) {
				PratilipiContentDoc pcDoc = docAccessor.getPratilipiContentDoc( pratilipiData.getId() );
				if( pcDoc == null )
					continue;
				pratilipiData.setIndex( pcDoc.getIndex() );
			}
		}

		for( int i = 0; i < sectionList.size(); i++ )
			sectionList.get( i ).setPratilipiList( pratilipiDataMasterList.subList( i * 6, i * 6 + 6 ) );
		
		return new Response( sectionList );
		
	}
	
}
