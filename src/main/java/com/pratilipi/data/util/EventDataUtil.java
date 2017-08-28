package com.pratilipi.data.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PageType;
import com.pratilipi.common.util.ImageUtil;
import com.pratilipi.common.util.SystemProperty;
import com.pratilipi.common.util.UriAliasUtil;
import com.pratilipi.common.util.UserAccessUtil;
import com.pratilipi.data.BlobAccessor;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.EventData;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.type.AuditLog;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.type.Event;
import com.pratilipi.data.type.Page;
import com.pratilipi.filter.AccessTokenFilter;


public class EventDataUtil {
	
	private static final Logger logger =
			Logger.getLogger( EventDataUtil.class.getName() );

	
	private static final String BANNER_FOLDER = "event-banner/original";


	public static boolean hasAccessToAddEventData( EventData eventData ) {
		
		// User with EVENT_ADD access can add an Event.
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), eventData.getLanguage(), AccessType.EVENT_ADD ) )
			return true;
		
		return false;
		
	}

	public static boolean hasAccessToUpdateEventData( Event event, EventData eventData ) {

		// User with EVENT_UPDATE access can update any event.
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), event.getLanguage(), AccessType.EVENT_UPDATE ) ) {
			if( eventData == null || ! eventData.hasLanguage() || eventData.getLanguage() == event.getLanguage() )
				return true;
			else if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), eventData.getLanguage(), AccessType.EVENT_UPDATE ) )
				return true;
		}
		
		return false;
		
	}
	
	
	public static String createEventBannerUrl( Event event ) {
		String url = "/event/banner" + "?eventId=" + event.getId() + "&version=" + event.getLastUpdated().getTime();
		if( SystemProperty.CDN != null )
			url = SystemProperty.CDN.replace( "*", event.getId() % 10 + "" ) + url;
		return url;
	}

	
	public static EventData createEventData( Event event, Boolean includePratilipiList ) {
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Page eventPage = dataAccessor.getPage( PageType.EVENT, event.getId() );
		return createEventData( event, eventPage, includePratilipiList );
	}

	public static EventData createEventData( Event event, Page eventPage, Boolean includePratilipiList ) {
		EventData eventData = new EventData();
		eventData.setId( event.getId() );
		eventData.setName( event.getName() );
		eventData.setNameEn( event.getNameEn() );
		eventData.setLanguage( event.getLanguage() );
		eventData.setDescription( event.getDescription() );
		eventData.setPageUrl( eventPage.getUriAlias() == null ? eventPage.getUri() : eventPage.getUriAlias() );
		eventData.setBannerImageUrl( createEventBannerUrl( event ) );
		eventData.setAccessToUpdate( hasAccessToUpdateEventData( event, null ) );

		if( includePratilipiList ) {
			eventData.setPratilipiIdList( event.getPratilipiIdList() );
			if( event.getPratilipiIdList() == null || event.getPratilipiIdList().size() == 0 ) {
				eventData.setPratilipiUrlList( new ArrayList<String>( 0 ) );
			} else {
				Map<Long,Page> pratilipiPages = DataAccessorFactory.getDataAccessor()
						.getPages( PageType.PRATILIPI, event.getPratilipiIdList() );
				List<String> pratilipiUrlList = new ArrayList<>( event.getPratilipiIdList().size() );
				for( Long pratilipiId : event.getPratilipiIdList() ) {
					Page pratilipiPage = pratilipiPages.get( pratilipiId );
					if( pratilipiPage != null )
						pratilipiUrlList.add( pratilipiPage.getUriAlias() == null ? pratilipiPage.getUri() : pratilipiPage.getUriAlias() );
				}
				eventData.setPratilipiUrlList( pratilipiUrlList );
			}
		}

		return eventData;
	}
	
	public static List<EventData> createEventDataList( List<Event> eventList ) {
		List<Long> eventIdList = new ArrayList<>();
		for( Event event : eventList )
			eventIdList.add( event.getId() );
	
		Map<Long, Page> eventPages = DataAccessorFactory.getDataAccessor()
				.getPages( PageType.EVENT, eventIdList );
		
		List<EventData> eventDataList = new ArrayList<>();
		for( Event event : eventList )
			eventDataList.add( createEventData( event, eventPages.get( event.getId() ), false ) );
		return eventDataList;
	}
	
	
	public static List<EventData> getEventDataList( Language language ) {
		List<Event> eventList = DataAccessorFactory
				.getDataAccessor()
				.getEventList( language );
		return createEventDataList( eventList );
	}
	
	
	public static EventData saveEventData( EventData eventData )
			throws InvalidArgumentException, InsufficientAccessException {
		
		_validateEventDataForSave( eventData );
		
		boolean isNew = eventData.getId() == null;
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Event event = isNew ? dataAccessor.newEvent() : dataAccessor.getEvent( eventData.getId() );
		
		if ( isNew && ! hasAccessToAddEventData( eventData ) )
			throw new InsufficientAccessException();
		if( ! isNew && ! hasAccessToUpdateEventData( event, eventData ) )
			throw new InsufficientAccessException();
		

		AuditLog auditLog = dataAccessor.newAuditLog(
					AccessTokenFilter.getAccessToken(),
					isNew ? AccessType.EVENT_ADD : AccessType.EVENT_UPDATE,
					event );
		
		
		if( eventData.hasName() )
			event.setName( eventData.getName() );
		if( eventData.hasNameEn() )
			event.setNameEn( eventData.getNameEn() );
		if( eventData.hasLanguage() )
			event.setLanguage( eventData.getLanguage() );
		if( eventData.hasDescription() )
			event.setDescription( eventData.getDescription() );
		if( eventData.hasPratilipiUrlList() ) {
			if( eventData.getPratilipiUrlList() == null ) {
				eventData.setPratilipiIdList( null );
			} else {
				List<Long> pratilipiIdList = new LinkedList<>();
				Map<String,Page> map = dataAccessor.getPages( eventData.getPratilipiUrlList() );
				for( String pratilipiUrl : eventData.getPratilipiUrlList() ) {
					Page page = map.get( pratilipiUrl );
					if( page != null && page.getType() == PageType.PRATILIPI )
						pratilipiIdList.add( page.getPrimaryContentId() );
				}
				event.setPratilipiIdList( pratilipiIdList );
			}
		}
		if( isNew )
			event.setCreationDate( new Date() );
		event.setLastUpdated( new Date() );
		
		
		if( isNew ) {
			event = dataAccessor.createOrUpdateEvent( event, auditLog );
			dataAccessor.createOrUpdatePage( _updateEventPageUrl( event ) );
		} else {
			event = dataAccessor.createOrUpdateEvent(
					event,
					_updateEventPageUrl( event ),
					auditLog );
		}		

		return createEventData( event, true );
		
	}
	
	private static void _validateEventDataForSave( EventData eventData )
			throws InvalidArgumentException {
		
		boolean isNew = eventData.getId() == null;
		
		JsonObject errorMessages = new JsonObject();

		// New event must have language.
		if( isNew && ( ! eventData.hasLanguage() || eventData.getLanguage() == null ) )
			errorMessages.addProperty( "langauge", GenericRequest.ERR_LANGUAGE_REQUIRED );
		// Language can not be un-set or set to null.
		else if( ! isNew && eventData.hasLanguage() && eventData.getLanguage() == null )
			errorMessages.addProperty( "langauge", GenericRequest.ERR_LANGUAGE_REQUIRED );

		if( errorMessages.entrySet().size() > 0 )
			throw new InvalidArgumentException( errorMessages );

	}
	
	
	public static BlobEntry getEventBanner( Long eventId, Integer width )
			throws UnexpectedServerException {

		String fileName = BANNER_FOLDER + "/" + eventId;
		BlobEntry blobEntry = DataAccessorFactory.getBlobAccessor().getBlob( fileName );
		if( width != null )
			blobEntry.setData( ImageUtil.resize( blobEntry.getData(), width ) );
		return blobEntry;
		
	}
	
	public static void saveEventBanner( Long eventId, BlobEntry blobEntry )
			throws InsufficientAccessException, UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Event event = dataAccessor.getEvent( eventId );

		if( ! hasAccessToUpdateEventData( event, null ) )
			throw new InsufficientAccessException();

		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		blobEntry.setName( BANNER_FOLDER + "/" + eventId );
		blobAccessor.createOrUpdateBlob( blobEntry );
		
		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.EVENT_UPDATE,
				event );

		event.setLastUpdated( new Date() );

		auditLog.setEventComment( "Uploaded banner image." );
		
		event = dataAccessor.createOrUpdateEvent( event, auditLog );
		
	}
	
	
	private static Page _updateEventPageUrl( Event event ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Page page = dataAccessor.getPage( PageType.EVENT, event.getId() );

		boolean isNew = page == null;
		
		if( isNew ) {
			page = dataAccessor.newPage();
			page.setType( PageType.EVENT );
			page.setUri( PageType.EVENT.getUrlPrefix() + event.getId() );
			page.setPrimaryContentId( event.getId() );
			page.setCreationDate( new Date() );
		}
		
		String uriAlias = UriAliasUtil.generateUriAlias(
				page.getUriAlias(),
				PageType.EVENT.getUrlPrefix(),
				event.getNameEn() == null ? event.getName() : event.getNameEn() );
			
		if( isNew && uriAlias == null ) {
			// Do NOT return.
		} else if( uriAlias == page.getUriAlias()
				|| ( uriAlias != null && uriAlias.equals( page.getUriAlias() ) )
				|| ( page.getUriAlias() != null && page.getUriAlias().equals( uriAlias ) ) ) {
			return null; // Do Nothing.
		} else {
			logger.log( Level.INFO, "Updating Event Page Url: '" + page.getUriAlias() + "' -> '" + uriAlias + "'" );
			page.setUriAlias( uriAlias );
		}
		
		return page;
	
	}

	
}
