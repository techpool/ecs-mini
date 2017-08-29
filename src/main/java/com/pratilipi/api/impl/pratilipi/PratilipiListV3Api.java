package com.pratilipi.api.impl.pratilipi;

import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.PratilipiFilter;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.type.Event;
import com.pratilipi.data.util.EventDataUtil;
import com.pratilipi.data.util.PratilipiDataUtil;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/pratilipi/list", ver = "3" )
public class PratilipiListV3Api extends PratilipiListV1Api {

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
		Response response = new Response(
				pratilipiListCursorTuple.getDataList(),
				pratilipiListCursorTuple.getCursor(),
				pratilipiListCursorTuple.getNumberFound(),
				PratilipiListV3Api.class );

		if( UxModeFilter.isAndroidApp() && request.eventId != null && request.cursor == null ) {
			Event event = DataAccessorFactory.getDataAccessor().getEvent( request.eventId );
			response.setBannerUrl( EventDataUtil.createEventBannerUrl( event ) );
			response.setDescription( event.getDescription() );
		}

		return response;

	}

}
