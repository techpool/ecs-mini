package com.pratilipi.data.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.pratilipi.common.type.AuthorState;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PageType;
import com.pratilipi.common.type.PratilipiContentType;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.common.type.Website;
import com.pratilipi.common.util.*;
import com.pratilipi.data.BlobAccessor;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.SearchAccessor;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.type.AppProperty;
import com.pratilipi.data.type.AuditLog;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.type.Page;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.type.PratilipiGoogleAnalyticsDoc;
import com.pratilipi.data.type.PratilipiMetaDoc;
import com.pratilipi.data.type.PratilipiReviewsDoc;
import com.pratilipi.data.type.UserPratilipi;
import com.pratilipi.filter.AccessTokenFilter;

public class PratilipiDataUtil {
	
	private static final Logger logger =
			Logger.getLogger( PratilipiDataUtil.class.getName() );

	
	private static final String CONTENT_FOLDER 		 = "pratilipi-content/pratilipi";
	private static final String IMAGE_CONTENT_FOLDER = "pratilipi-content/image";


	public static boolean hasAccessToListPratilipiData( PratilipiFilter pratilipiFilter ) {
		
		// Case 1: User can list PUBLISHED Pratilipis.

		if( pratilipiFilter.getState() == PratilipiState.PUBLISHED )
			return true;

		
		// Case 2: User with PRATILIPI_LIST access can list Pratilipis in any State.

		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), pratilipiFilter.getLanguage(), AccessType.PRATILIPI_LIST ) )
			return true;

		
		// Case 3: User can list Pratilipis in any State, except DELETED state, for their own Author profile.

		if( pratilipiFilter.getState() == PratilipiState.DELETED )
			return false;
		
		if( pratilipiFilter.getAuthorId() == null )
			return false;
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Author author = dataAccessor.getAuthor( pratilipiFilter.getAuthorId() );
		if( author == null )
			return false;
		
		return accessToken.getUserId().equals( author.getUserId() );
		
	}
	
	public static boolean hasAccessToAddPratilipiData( PratilipiData pratilipiData ) {
		
		Author author = pratilipiData.getAuthorId() == null
				? null :
				DataAccessorFactory.getDataAccessor().getAuthor( pratilipiData.getAuthorId() );

	
		// Case 1: Content pieces can be added against ACTIVE Author profiles only.
		if( author != null && author.getState() != AuthorState.ACTIVE )
			return false;
		
		
		// Case 2: User with PRATILIPI_ADD access can add Pratilipi against any Author profile.
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), pratilipiData.getLanguage(), AccessType.PRATILIPI_ADD ) )
			return true;

		
		// Case 3: User can add Pratilipi against his/her own Author profile.
		if( author != null && accessToken.getUserId().equals( author.getUserId() ) )
			return true;
		
		
		return false;
		
	}

	public static boolean hasAccessToUpdatePratilipiData( Pratilipi pratilipi, PratilipiData pratilipiData ) {

		// Case 1: Only DRAFTED/SUBMITTED/PUBLISHED content pieces can be updated.
		if( pratilipi.getState() != PratilipiState.DRAFTED
				&& pratilipi.getState() != PratilipiState.SUBMITTED
				&& pratilipi.getState() != PratilipiState.PUBLISHED )
			return false;

		
		Author author = pratilipi.getAuthorId() == null
				? null :
				DataAccessorFactory.getDataAccessor().getAuthor( pratilipi.getAuthorId() );

	
		// Case 2: Content pieces can be updated only if Author profile is ACTIVE.
		if( author != null && author.getState() != AuthorState.ACTIVE )
			return false;
		
		
		// Case 3: User with PRATILIPI_UPDATE access can update any content piece.
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), pratilipi.getLanguage(), AccessType.PRATILIPI_UPDATE ) ) {
			if( pratilipiData == null || ! pratilipiData.hasLanguage() || pratilipiData.getLanguage() == pratilipi.getLanguage() )
				return true;
			else if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), pratilipiData.getLanguage(), AccessType.PRATILIPI_UPDATE ) )
				return true;
		}

		
		// Case 4: User can update content piece linked with his/her own Author profile.
		if( author != null && accessToken.getUserId().equals( author.getUserId() ) )
			return true;
		
		
		return false;
		
	}
	
	public static boolean hasAccessToReadPratilipiMetaData( Pratilipi pratilipi ) {
		
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), pratilipi.getLanguage(), AccessType.PRATILIPI_READ_META ) )
			return true;
		
		Author author = pratilipi.getAuthorId() == null
				? null
				: DataAccessorFactory.getDataAccessor().getAuthor( pratilipi.getAuthorId() );
		if( author != null && author.getUserId() != null && author.getUserId().equals( accessToken.getUserId() ) )
			return true;
		
		return false;
		
	}
	
	public static boolean hasAccessToUpdatePratilipiMetaData( Language language ) {
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		return UserAccessUtil.hasUserAccess( accessToken.getUserId(), language, AccessType.PRATILIPI_UPDATE_META );
	}
	
	public static boolean hasAccessToReadPratilipiContent( Pratilipi pratilipi ) {

		// Case 1: Any user can read PUBLISHED content.
		if( pratilipi.getState() == PratilipiState.PUBLISHED )
			return true;

		
		// Case 2: Nobody can read DELETED content.
		if( pratilipi.getState() == PratilipiState.DELETED )
			return false;

		
		// Case 3: User with PRATILIPI_READ_CONTENT access can read any content in any state.
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), pratilipi.getLanguage(), AccessType.PRATILIPI_READ_CONTENT ) )
			return true;

		
		// Case 4: User can read content, in any state, linked with his/her own Author profile.
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Author author = dataAccessor.getAuthor( pratilipi.getAuthorId() );
		if( author != null && accessToken.getUserId().equals( author.getUserId() ) )
			return true;
		
		
		return false;
		
	}

	public static boolean hasAccessToUpdatePratilipiContent( Pratilipi pratilipi ) {
		return hasAccessToUpdatePratilipiData( pratilipi, null );
	}
	

	public static String createPratilipiCoverUrl( Pratilipi pratilipi ) {
		return createPratilipiCoverUrl( pratilipi, null );
	}
	
	public static String createPratilipiCoverUrl( Pratilipi pratilipi, Integer width ) {
		String url = "/pratilipi/cover";
		if( pratilipi.getCoverImage() == null ) {
			if( width != null )
				url = url + "?width=" + width;
			if( SystemProperty.CDN != null )
				url = SystemProperty.CDN.replace( "*", "10" ) + url;
		} else {
			url = url + "?pratilipiId=" + pratilipi.getId() + "&version=" + pratilipi.getCoverImage();
			if( width != null )
				url = url + "&width=" + width;
			if( SystemProperty.CDN != null )
				url = SystemProperty.CDN.replace( "*", pratilipi.getId() % 5 + "" ) + url;
		}
		return url;
	}

	private static double calculateRelevance( Pratilipi pratilipi, Author author ) {
		double relevance = pratilipi.getReadCount();
		if( author != null && author.getContentPublished() > 1L )
			relevance = relevance + (double) author.getTotalReadCount() / (double) author.getContentPublished();
		return relevance;
	}
	

	public static PratilipiData createPratilipiData( Pratilipi pratilipi ) throws UnexpectedServerException {
		return createPratilipiData( pratilipi, null, DataAccessorFactory.getDataAccessor().getAuthor( pratilipi.getAuthorId() ), false );
	}

	public static PratilipiData createPratilipiData( Pratilipi pratilipi, Author author ) throws UnexpectedServerException {
		return createPratilipiData( pratilipi, null, author, false );
	}
	
	public static PratilipiData createPratilipiData( Pratilipi pratilipi, Author author, boolean includeMetaData ) throws UnexpectedServerException {
		return createPratilipiData( pratilipi, null, author, includeMetaData );
	}
	
	@SuppressWarnings("unused")
	public static PratilipiData createPratilipiData( Pratilipi pratilipi, Page pratilipiPage, Author author, boolean includeMetaData ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		if( pratilipiPage == null ) // will still be null for DELETED Pratilipis
			pratilipiPage = dataAccessor.getPage( PageType.PRATILIPI, pratilipi.getId() );
		Page readPage = null; // dataAccessor.getPage( PageType.READ, pratilipi.getId() ); TODO: Uncomment this once data in DataStore is fixed.
		Page writePage = null; // dataAccessor.getPage( PageType.WRITE, pratilipi.getId() ); TODO: Uncomment this once data in DataStore is fixed.
		
		PratilipiData pratilipiData = new PratilipiData();

		pratilipiData.setId( pratilipi.getId() );
		pratilipiData.setTitle( pratilipi.getTitle() );
		pratilipiData.setTitleEn( pratilipi.getTitleEn() );
		pratilipiData.setLanguage( pratilipi.getLanguage() );
		pratilipiData.setAuthorId( pratilipi.getAuthorId() );
		pratilipiData.setAuthor( AuthorDataUtil.createAuthorData( author ) );
		pratilipiData.setSummary( HtmlUtil.toPlainText( pratilipi.getSummary() ) );

		pratilipiData.setPageUrl( pratilipiPage == null ? null :
				( pratilipiPage.getUriAlias() == null ? pratilipiPage.getUri() : pratilipiPage.getUriAlias() ) );
		pratilipiData.setCoverImageUrl( createPratilipiCoverUrl( pratilipi ) );
		pratilipiData.setReadPageUrl( readPage == null || readPage.getUriAlias() == null
				? PageType.READ.getUrlPrefix() + pratilipi.getId()
				: readPage.getUriAlias() );

		if( writePage != null && writePage.getUriAlias() != null ) {
			pratilipiData.setWritePageUrl( writePage.getUriAlias() );
		} else {
			if( pratilipi.isOldContent() )
				pratilipiData.setWritePageUrl( "/write?id=" + pratilipi.getId() );
			else
				pratilipiData.setWritePageUrl( PageType.WRITE.getUrlPrefix() + pratilipi.getId() );
		}

		pratilipiData.setOldContent( pratilipi.isOldContent() );
		pratilipiData.setType( pratilipi.getType() );
		pratilipiData.setContentType( pratilipi.getContentType() );
		pratilipiData.setState( pratilipi.getState() );
		pratilipiData.setListingDate( pratilipi.getListingDate() );
		if( includeMetaData )
			pratilipiData.setLastUpdated( pratilipi.getLastUpdated() );

		pratilipiData.setIndex( pratilipi.getIndex() );
		pratilipiData.setPageCount( pratilipi.getPageCount() );

		pratilipiData.setReviewCount( pratilipi.getReviewCount() );
		pratilipiData.setRatingCount( pratilipi.getRatingCount() );
		pratilipiData.setAverageRating( pratilipi.getRatingCount() == 0L
				? 0F
				: (float) ( (double) pratilipi.getTotalRating() / pratilipi.getRatingCount() ) );
		pratilipiData.setRelevance( calculateRelevance( pratilipi, author ) );
		pratilipiData.setReadCount( pratilipi.getReadCountOffset() + pratilipi.getReadCount() );
		pratilipiData.setFbLikeShareCount( pratilipi.getFbLikeShareCountOffset() + pratilipi.getFbLikeShareCount() );
		pratilipiData.setWordCount( pratilipi.getWordCount() );

		pratilipiData.setTags(TagDataUtil.createTagDataList(pratilipi.getTagIds()));
		pratilipiData.setSuggestedTags(pratilipi.getSuggestedTags());

		pratilipiData.setAccessToUpdate( hasAccessToUpdatePratilipiData( pratilipi, null ) );
		
		return pratilipiData;
		
	}
	
	
	public static List<PratilipiData> createPratilipiDataList( List<Long> pratilipiIdList, boolean includeAuthorData ) throws UnexpectedServerException {
		return createPratilipiDataList( pratilipiIdList, includeAuthorData, false );
	}

	public static List<PratilipiData> createPratilipiDataList( List<Long> pratilipiIdList, boolean includeAuthorData, boolean includeMetaData ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		List<Pratilipi> pratilipiList = dataAccessor.getPratilipiList( pratilipiIdList );
		Map<Long, Page> pratilipiPages = dataAccessor.getPages( PageType.PRATILIPI, pratilipiIdList );
		
		Map<Long, AuthorData> authorIdToDataMap = null;
		if( includeAuthorData ) {
			List<Long> authorIdList = new LinkedList<>();
			for( Pratilipi pratilipi : pratilipiList )
				if( pratilipi.getAuthorId() != null && ! authorIdList.contains( pratilipi.getAuthorId() ) )
					authorIdList.add( pratilipi.getAuthorId() );
			
			List<AuthorData> authorDataList = AuthorDataUtil.createAuthorDataList( authorIdList, false );
			authorIdToDataMap = new HashMap<>( authorDataList.size() );
			for( AuthorData authorData : authorDataList )
				authorIdToDataMap.put( authorData.getId(), authorData );	
		}

		List<PratilipiData> pratilipiDataList = new ArrayList<>( pratilipiList.size() );
		for( Pratilipi pratilipi : pratilipiList ) {
			PratilipiData pratilipiData = createPratilipiData( pratilipi, pratilipiPages.get( pratilipi.getId() ), null, includeMetaData );
			if( includeAuthorData && pratilipi.getAuthorId() != null )
				pratilipiData.setAuthor( authorIdToDataMap.get( pratilipi.getAuthorId() ) );
			pratilipiData.setRelevance( calculateRelevance( pratilipi, dataAccessor.getAuthor( pratilipi.getAuthorId() ) ) );
			pratilipiDataList.add( pratilipiData );
		}
		
		// Fetching UserPratilipi list from DataStore
		List<UserPratilipi> userPratilipiList = dataAccessor.getUserPratilipiList(
				AccessTokenFilter.getAccessToken().getUserId(),
				pratilipiIdList );

		// Setting isAddedToLib flag for for each PratilipiData in the list
		for( int i = 0; i < userPratilipiList.size(); i++ ) {
			UserPratilipi userPratilipi = userPratilipiList.get( i );
			pratilipiDataList.get( i ).setAddedToLib( userPratilipi != null && userPratilipi.isAddedToLib() );
		}
		
		return pratilipiDataList;
		
	}
	

	@Deprecated
	public static DataListCursorTuple<PratilipiData> getPratilipiDataList(
			PratilipiFilter pratilipiFilter, String cursor, Integer resultCount )
			throws InsufficientAccessException, UnexpectedServerException {

		return getPratilipiDataList( null, null, pratilipiFilter, cursor, null, resultCount);
	}

	@Deprecated
	public static DataListCursorTuple<PratilipiData> getPratilipiDataList(
			PratilipiFilter pratilipiFilter, String cursor, Integer offset, Integer resultCount )
			throws InsufficientAccessException, UnexpectedServerException {

		return getPratilipiDataList( null, null, pratilipiFilter, cursor, offset, resultCount);
	}

	@Deprecated
	public static DataListCursorTuple<PratilipiData> getPratilipiDataList(
			String searchQuery, PratilipiFilter pratilipiFilter,
			String cursor, Integer resultCount )
			throws InsufficientAccessException, UnexpectedServerException {
		
		return getPratilipiDataList( searchQuery, null, pratilipiFilter, cursor, null, resultCount );
	}

	public static DataListCursorTuple<PratilipiData> getPratilipiDataList(
			String searchQuery, PratilipiFilter pratilipiFilter,
			String cursor, Integer offset, Integer resultCount )
			throws InsufficientAccessException, UnexpectedServerException {
		
		return getPratilipiDataList( searchQuery, null, pratilipiFilter, cursor, null, resultCount );
	}

	public static DataListCursorTuple<PratilipiData> getPratilipiDataList(
			String searchQuery, Long eventId, PratilipiFilter pratilipiFilter,
			String cursor, Integer offset, Integer resultCount )
			throws InsufficientAccessException, UnexpectedServerException {
		
		if( ! hasAccessToListPratilipiData( pratilipiFilter ) )
			throw new InsufficientAccessException();

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		// Processing search query
		if( searchQuery != null )
			searchQuery = searchQuery.toLowerCase().trim()
					.replaceAll( "\"|\\\\|\\(|\\)|:|,|\\sor\\s", " " ).trim()
					.replaceAll( "[\\s]+", " OR " );

		// Fetching Pratilipi id list from DataStore/SearchIndex/Event
		DataListCursorTuple<Long> pratilipiIdListCursorTuple = null; 

		if( eventId != null ) {

			List<Long> pratilipiIdList = dataAccessor.getEvent(eventId).getPratilipiIdList();
			// move read content to end of the list.
			List<Long> contentsReadList = UserPratilipiDataUtil.getContentsReadList(AccessTokenFilter.getAccessToken().getUserId());
			// move all read contents to bottom of the list.
			if (contentsReadList != null) {
				contentsReadList.retainAll(pratilipiIdList);        // Find all read contents in the list
				pratilipiIdList.removeAll(contentsReadList);        // Remove already read content from the list
				pratilipiIdList.addAll(contentsReadList);        // Append read content at end of the list
			}

			offset = (cursor == null ? 0 : Integer.parseInt(cursor))
					+ (offset == null || offset < 0 ? 0 : offset);
			offset = Math.min(offset, pratilipiIdList.size());
			resultCount = resultCount == null || resultCount > pratilipiIdList.size() - offset
					? pratilipiIdList.size() - offset
					: resultCount;

			pratilipiIdListCursorTuple = new DataListCursorTuple<Long>(
					pratilipiIdList.subList(offset, offset + resultCount),
					offset + resultCount + "",
					(long) pratilipiIdList.size());

		} else if( searchQuery != null && pratilipiFilter.getState() == PratilipiState.PUBLISHED ) {

			pratilipiIdListCursorTuple = DataAccessorFactory.getSearchAccessor()
					.searchPratilipi( searchQuery, pratilipiFilter, cursor, offset, resultCount );

		} else if( pratilipiFilter.getAuthorId() != null ) {

			DataListCursorTuple<Long> authorsPratilipis = dataAccessor.getPratilipiIdList( pratilipiFilter, cursor, offset, resultCount );
			Author author = dataAccessor.getAuthor( pratilipiFilter.getAuthorId() );

			if( pratilipiFilter.getState() == PratilipiState.PUBLISHED )
				pratilipiIdListCursorTuple =
						new DataListCursorTuple<Long>( authorsPratilipis.getDataList(), authorsPratilipis.getCursor(), (long) author.getContentPublished() );
			else if( pratilipiFilter.getState() == PratilipiState.DRAFTED )
				pratilipiIdListCursorTuple =
						new DataListCursorTuple<Long>( authorsPratilipis.getDataList(), authorsPratilipis.getCursor(), (long) author.getContentDrafted() );
			else
				pratilipiIdListCursorTuple = authorsPratilipis;

		} else {
			pratilipiIdListCursorTuple = dataAccessor.getPratilipiIdList( pratilipiFilter, cursor, offset, resultCount );

		}


		// Creating PratilipiData list from Pratilipi id list
		List<PratilipiData> pratilipiDataList = createPratilipiDataList(
				pratilipiIdListCursorTuple.getDataList(),
				pratilipiFilter.getAuthorId() == null,
				false );
		
		// Creating response object
		return new DataListCursorTuple<PratilipiData>(
				pratilipiDataList,
				pratilipiIdListCursorTuple.getCursor(),
				pratilipiIdListCursorTuple.getNumberFound() );
		
	}
	
	public static PratilipiData savePratilipiData( PratilipiData pratilipiData )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		_validatePratilipiDataForSave( pratilipiData );
		
		boolean isNew = pratilipiData.getId() == null;
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = isNew ? dataAccessor.newPratilipi() : dataAccessor.getPratilipi( pratilipiData.getId() );

		if ( isNew && ! hasAccessToAddPratilipiData( pratilipiData ) )
			throw new InsufficientAccessException();
		if( ! isNew && ! hasAccessToUpdatePratilipiData( pratilipi, pratilipiData ) )
			throw new InsufficientAccessException();
		

		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				isNew ? AccessType.PRATILIPI_ADD : AccessType.PRATILIPI_UPDATE,
				pratilipi
		);

		
		if( pratilipiData.hasTitle() )
			pratilipi.setTitle( pratilipiData.getTitle() );
		if( pratilipiData.hasTitleEn() )
			pratilipi.setTitleEn( pratilipiData.getTitleEn() );
		if( pratilipiData.hasLanguage() )
			pratilipi.setLanguage( pratilipiData.getLanguage() );
		// Do NOT update Author for existing content pieces.
		if( isNew && pratilipiData.hasAuthorId() )
			pratilipi.setAuthorId( pratilipiData.getAuthorId() );
		
		if( pratilipiData.hasSummary() )
			pratilipi.setSummary( pratilipiData.getSummary() );
		
		if( pratilipiData.hasType() )
			pratilipi.setType( pratilipiData.getType() );
		if( pratilipiData.hasContentType() )
			pratilipi.setContentType( pratilipiData.getContentType() );
		else if( isNew )
			pratilipi.setContentType( PratilipiContentType.PRATILIPI );
		if( pratilipiData.hasState() )
			pratilipi.setState( pratilipiData.getState() );
		else if( isNew )
			pratilipi.setState( PratilipiState.DRAFTED );

		if( pratilipiData.hasOldContentFlag() )
			pratilipi.setOldContent( pratilipiData.isOldContent() );
		
//		if(pratilipiData.hasTagIds())
//			pratilipi.setTagIds(pratilipiData.getTagIds());
//		
//		if(pratilipiData.hasSuggestedTags())
//			pratilipi.setSuggestedTags(pratilipiData.getSuggestedTags());


		if( isNew )
			pratilipi.setListingDate( new Date() );
		pratilipi.setLastUpdated( new Date() );

		
		if( isNew ) {
			pratilipi = dataAccessor.createOrUpdatePratilipi( pratilipi, auditLog );
			dataAccessor.createOrUpdatePage( _updatePratilipiPageUrl( pratilipi ) );
		} else {
			pratilipi = dataAccessor.createOrUpdatePratilipi(
					pratilipi,
					_updatePratilipiPageUrl( pratilipi ),
					auditLog );
		}		

		
		return createPratilipiData(
				pratilipi,
				dataAccessor.getAuthor( pratilipi.getAuthorId() ),
				hasAccessToReadPratilipiMetaData( pratilipi ) );
		
	}

	private static void _validatePratilipiDataForSave( PratilipiData pratilipiData )
			throws InvalidArgumentException {
		
		boolean isNew = pratilipiData.getId() == null;
		
		JsonObject errorMessages = new JsonObject();

		// New content piece must have language.
		if( isNew && ( ! pratilipiData.hasLanguage() || pratilipiData.getLanguage() == null ) )
			errorMessages.addProperty( "language", GenericRequest.ERR_LANGUAGE_REQUIRED );
		// Language can not be un-set or set to null.
		else if( ! isNew && pratilipiData.hasLanguage() && pratilipiData.getLanguage() == null )
			errorMessages.addProperty( "language", GenericRequest.ERR_LANGUAGE_REQUIRED );

		// New content piece must have type.
		if( isNew && ( ! pratilipiData.hasType() || pratilipiData.getType() == null ) )
			errorMessages.addProperty( "type", GenericRequest.ERR_PRATILIPI_TYPE_REQUIRED );
		// Type can not be un-set or set to null.
		else if( ! isNew && pratilipiData.hasType() && pratilipiData.getType() == null )
			errorMessages.addProperty( "type", GenericRequest.ERR_PRATILIPI_TYPE_REQUIRED );
		
		// Content type can not be un-set or set to null.
		if( ! isNew && pratilipiData.hasContentType() && pratilipiData.getContentType() == null )
			errorMessages.addProperty( "type", GenericRequest.ERR_PRATILIPI_CONTENT_TYPE_REQUIRED );

		// State can not be un-set or set to null.
		if( pratilipiData.hasState() && pratilipiData.getState() == null ) // isNew || ! isNew
			errorMessages.addProperty( "state", GenericRequest.ERR_PRATILIPI_STATE_REQUIRED );
		// isNew || !isNew
		else if( pratilipiData.hasState()
				&& pratilipiData.getState() != PratilipiState.DRAFTED
				&& pratilipiData.getState() != PratilipiState.SUBMITTED
				&& pratilipiData.getState() != PratilipiState.PUBLISHED
				&& pratilipiData.getState() != PratilipiState.DELETED )
			errorMessages.addProperty( "state", GenericRequest.ERR_PRATILIPI_STATE_INVALID );

		if( errorMessages.entrySet().size() > 0 )
			throw new InvalidArgumentException( errorMessages );

	}
	
	
	public static BlobEntry getPratilipiCover( Long pratilipiId, Integer width )
			throws UnexpectedServerException {

		Pratilipi pratilipi = pratilipiId == null ? null : DataAccessorFactory.getDataAccessor().getPratilipi( pratilipiId );
		
		BlobEntry blobEntry = null;
		if( pratilipi == null || pratilipi.getCoverImage() == null ) {
			blobEntry = DataAccessorFactory.getBlobAccessor().getBlob( "pratilipi/default/images/cover" );
		} else {
			blobEntry = DataAccessorFactory.getBlobAccessor().getBlob( "pratilipi/" + pratilipiId + "/images/" + pratilipi.getCoverImage() );
			if( blobEntry == null )
				blobEntry = DataAccessorFactory.getBlobAccessor().getBlob( "pratilipi-cover/original/" + pratilipiId );
		}
		
		if( width != null )
			blobEntry = ImageUtil.resize( blobEntry, width, (int) ( 1.5 * width ) );
		
		return blobEntry;
		
	}

	public static String savePratilipiCover( Long pratilipiId, BlobEntry blobEntry )
			throws InsufficientAccessException, UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! hasAccessToUpdatePratilipiData( pratilipi, null ) )
			throw new InsufficientAccessException();
		
		
		String coverImageName = new Date().getTime() + "";
		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		blobEntry.setName( "pratilipi/" + pratilipiId + "/images/" + coverImageName );
		blobAccessor.createOrUpdateBlob( blobEntry );
		
		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.PRATILIPI_UPDATE,
				pratilipi );

		pratilipi.setCoverImage( coverImageName );
		pratilipi.setLastUpdated( new Date() );

		pratilipi = dataAccessor.createOrUpdatePratilipi( pratilipi, auditLog );
		
		return createPratilipiCoverUrl( pratilipi );
		
	}
	
	
	public static void updatePratilipiIndex( Long pratilipiId ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
		
		if( pratilipi.isOldContent() && pratilipi.getContentType() == PratilipiContentType.PRATILIPI ) {
			
			BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
			BlobEntry blobEntry = blobAccessor.getBlob( CONTENT_FOLDER + "/" + pratilipiId );
			
			String index = null;
			Integer pageCount = 0;
			if( blobEntry != null ) {
				String content = new String( blobEntry.getData(), Charset.forName( "UTF-8" ) );
				PratilipiContentUtil pratilipiContentUtil = new PratilipiContentUtil( content );
				index = pratilipiContentUtil.generateIndex();
				pageCount = pratilipiContentUtil.getPageCount();
			}

			
			AuditLog auditLog = dataAccessor.newAuditLog(
					AccessTokenFilter.getAccessToken(),
					AccessType.PRATILIPI_UPDATE,
					pratilipi );
			
			boolean isChanged = false; 
			if( ( pratilipi.getIndex() == null && index != null )
					|| ( pratilipi.getIndex() != null && index == null )
					|| ( pratilipi.getIndex() != null && index != null && ! pratilipi.getIndex().equals( index ) ) ) {

				pratilipi.setIndex( index );
				isChanged = true;
				
			}

			if( pratilipi.getPageCount() != pageCount ) {
				pratilipi.setPageCount( pageCount );
				isChanged = true;
			}

			if( isChanged )
				pratilipi = dataAccessor.createOrUpdatePratilipi( pratilipi, auditLog );

		} else {
			
			// throw new InvalidArgumentException( "Index generation for " + pratilipi.getContentType() + " content type is not yet supported." );
		
		}
		
	}
	
	public static Page _updatePratilipiPageUrl( Pratilipi pratilipi ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Page pratilipiPage = dataAccessor.getPage( PageType.PRATILIPI, pratilipi.getId() );

		if( pratilipi.getState() == PratilipiState.DELETED ) {
			if( pratilipiPage != null )
				dataAccessor.deletePage( pratilipiPage );
			return null;
		}
		
		boolean isNew = pratilipiPage == null;
		
		if( isNew ) {
			pratilipiPage = dataAccessor.newPage();
			pratilipiPage.setType( PageType.PRATILIPI );
			pratilipiPage.setUri( PageType.PRATILIPI.getUrlPrefix() + pratilipi.getId() );
			pratilipiPage.setPrimaryContentId( pratilipi.getId() );
			pratilipiPage.setCreationDate( new Date() );
		}

		
		String uriPrifix = null;
		if( pratilipi.getAuthorId() != null ) {
			Page authorPage = dataAccessor.getPage( PageType.AUTHOR, pratilipi.getAuthorId() );
			if( authorPage.getUriAlias() != null )
				uriPrifix = authorPage.getUriAlias() + "/";
		}

		
		if( uriPrifix == null ) {
		
			if( isNew ) {
				// Do NOT return.
			} else if( pratilipiPage.getUriAlias() == null ) {
				return null; // Do Nothing.
			} else {
				logger.log( Level.INFO, "Clearing Pratilipi Page Url: '" + pratilipiPage.getUriAlias() + "' -> 'null'" );
				pratilipiPage.setUriAlias( null );
			}
		
		} else if( pratilipiPage.getUriAlias() == null ) {
			
			String uriAlias = UriAliasUtil.generateUriAlias(
					pratilipiPage.getUriAlias(),
					uriPrifix,
					pratilipi.getTitleEn() == null ? pratilipi.getTitle() : pratilipi.getTitleEn() );
			
			if( isNew && uriAlias == null ) {
				// Do NOT return.
//			} else if( uriAlias == pratilipiPage.getUriAlias()
//					|| ( uriAlias != null && uriAlias.equals( pratilipiPage.getUriAlias() ) )
//					|| ( pratilipiPage.getUriAlias() != null && pratilipiPage.getUriAlias().equals( uriAlias ) ) ) {
//				return null; // Do Nothing.
			} else {
				logger.log( Level.INFO, "Updating Pratilipi Page Url: '" + pratilipiPage.getUriAlias() + "' -> '" + uriAlias + "'" );
				pratilipiPage.setUriAlias( uriAlias );
			}
		
		} else { // Static Url alias for Pratilipi
			return null; // Do Nothing.
		}
	
		
		return pratilipiPage;
	
	}
	
	public static boolean createOrUpdatePratilipiReadPageUrl( Long pratilipiId ) {
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Page page = dataAccessor.getPage( PageType.PRATILIPI, pratilipiId );
		Page readPage = dataAccessor.getPage( PageType.READ, pratilipiId );

		if( readPage == null ) {
			readPage = dataAccessor.newPage();
			readPage.setType( PageType.READ );
			readPage.setUri( PageType.READ.getUrlPrefix() + pratilipiId );
			readPage.setPrimaryContentId( pratilipiId );
			readPage.setCreationDate( new Date() );
		} else if( ( page.getUriAlias() == null && readPage.getUriAlias() == null )
				|| ( page.getUriAlias() != null && readPage.getUriAlias() != null && page.getUri().equals( page.getUriAlias() ) ) ) {
			return false;
		}
		
		readPage.setUriAlias( page.getUriAlias() == null ? null : page.getUriAlias() + "/read" );
		readPage = dataAccessor.createOrUpdatePage( readPage );
		
		return true;
	}
	
	public static void updatePratilipiStats( Long pratilipiId )
			throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
		PratilipiGoogleAnalyticsDoc gaDoc = docAccessor.getPratilipiGoogleAnalyticsDoc( pratilipiId );
		Page pratilipiPage = dataAccessor.getPage( PageType.PRATILIPI, pratilipiId );
		
		long readCountOffset = 0L;
		for( int month = 1; month <= 12; month++ )
			for( int day = 1; day <= 31; day++ )
				readCountOffset += gaDoc.getPageViews( 2015, month, day );
		for( int month = 1; month <= 4; month++ )
			for( int day = 1; day <= 31; day++ )
				readCountOffset += gaDoc.getPageViews( 2016, month, day );
		
		long readCount = gaDoc.getTotalReadPageViews();
		long fbLikeShareCount = FacebookApi.getUrlShareCount( "http://" + Website.ALL_LANGUAGE.getHostName() + pratilipiPage.getUri() );
		
		
		if( pratilipi.getReadCountOffset() == readCountOffset
				&& pratilipi.getReadCount() == readCount
				&& pratilipi.getFbLikeShareCount() == fbLikeShareCount )
			return;
		
		
		updatePratilipiStats( pratilipiId, readCountOffset, readCount, null, fbLikeShareCount );
	
	}	

	public static void updatePratilipiStats( Long pratilipiId,
			Long readCountOffset, Long readCount,
			Long fbLikeShareCountOffset, Long fbLikeShareCount ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.PRATILIPI_UPDATE,
				pratilipi );
		
		if( readCountOffset != null )
			pratilipi.setReadCountOffset( readCountOffset );
		if( readCount != null )
			pratilipi.setReadCount( readCount );
		if( fbLikeShareCountOffset != null )
			pratilipi.setFbLikeShareCountOffset( fbLikeShareCountOffset );
		if( fbLikeShareCount != null )
			pratilipi.setFbLikeShareCount( fbLikeShareCount );
		
		pratilipi = dataAccessor.createOrUpdatePratilipi( pratilipi, auditLog );

	}
	
	public static void updateUserPratilipiStats( Long pratilipiId ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
		if( pratilipi.getState() != PratilipiState.PUBLISHED )
			return;

		PratilipiReviewsDoc reviewsDoc = docAccessor.getPratilipiReviewsDoc( pratilipiId );
		if( pratilipi.getRatingCount().equals( reviewsDoc.getRatingCount() )
				&& pratilipi.getTotalRating().equals( reviewsDoc.getTotalRating() )
				&& pratilipi.getReviewCount().equals( reviewsDoc.getReviewCount() ) )
			return;
		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.PRATILIPI_UPDATE,
				pratilipi );

		pratilipi.setRatingCount( reviewsDoc.getRatingCount() );
		pratilipi.setTotalRating( reviewsDoc.getTotalRating() );
		pratilipi.setReviewCount( reviewsDoc.getReviewCount() );
		
		pratilipi = dataAccessor.createOrUpdatePratilipi( pratilipi, auditLog );

		_updateUserPratilipiStats( pratilipiId, reviewsDoc.getRatingCount(), reviewsDoc.getReviewCount(), reviewsDoc.getTotalRating() );

	}

	// TODO: Remove Hack once services are built on ecs
	private static AccessToken WORKER_ACCESS_TOKEN = null;
	private static AccessToken _getWorkerAccessToken() {
		if( WORKER_ACCESS_TOKEN == null ) {
			DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
			String accessTokenId = dataAccessor.getAppProperty( AppProperty.WORKER_ACCESS_TOKEN_ID ).getValue();
			WORKER_ACCESS_TOKEN = dataAccessor.getAccessToken( accessTokenId );
		}
		return WORKER_ACCESS_TOKEN;
	}
	private static void _updateUserPratilipiStats( Long pratilipiId, Long ratingCount, Long reviewCount, Long totalRating )
				throws UnexpectedServerException {
		AccessToken accessToken = _getWorkerAccessToken();
		Map<String, String> headersMap = new HashMap<>();
		headersMap.put( "Access-Token", accessToken.getId() );
		headersMap.put( "User-Id", accessToken.getUserId().toString() );
		JsonObject body = new JsonObject();
		body.addProperty( "ratingCount", ratingCount );
		body.addProperty( "reviewCount", reviewCount );
		body.addProperty( "totalRating", totalRating );
		HttpUtil.doPost( SystemProperty.ECS_PAG_ENDPOINT + "/api/pratilipis/" + pratilipiId + "/review-data", headersMap, body );
	}

	public static void updatePratilipiSearchIndex( Long pratilipiId, Long authorId )
			throws InvalidArgumentException, UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		List<Pratilipi> pratilipiList = null;
		if( authorId != null ) {
			PratilipiFilter pratilipiFilter = new PratilipiFilter();
			pratilipiFilter.setAuthorId( authorId );
			pratilipiList = dataAccessor
					.getPratilipiList( pratilipiFilter, null, null )
					.getDataList();
			
		} else if( pratilipiId != null ) {
			pratilipiList = new ArrayList<>( 1 );
			pratilipiList.add( dataAccessor.getPratilipi( pratilipiId ) );
			
		} else {
			logger.log( Level.SEVERE, "Neither AuthorId, nor PratilipiId is provided !" );
			throw new InvalidArgumentException( "Neither AuthorId, nor PratilipiId is provided !" );
		}
		
		_updatePratilipiSearchIndex( pratilipiList );
	}

	public static void updatePratilipiSearchIndex( List<Long> pratilipiIdList )
			throws InvalidArgumentException, UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		_updatePratilipiSearchIndex( dataAccessor.getPratilipiList( pratilipiIdList ) );
		
	}
	
	private static void _updatePratilipiSearchIndex( List<Pratilipi> pratilipiList )
			throws InvalidArgumentException, UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		SearchAccessor searchAccessor = DataAccessorFactory.getSearchAccessor();

		List<PratilipiData> pratilipiDataList = new ArrayList<>( pratilipiList.size() );
		for( Pratilipi pratilipi : pratilipiList ) {
			
			if( pratilipi.getState() == PratilipiState.PUBLISHED ) {
				Author author = dataAccessor.getAuthor( pratilipi.getAuthorId() );
				pratilipiDataList.add( createPratilipiData( pratilipi, author, true ) );
				
			} else {
				searchAccessor.deletePratilipiDataIndex( pratilipi.getId() );

			}
			
		}
		
		if( pratilipiDataList.size() > 0 ) {
			Map<PratilipiData, String> pratilipiDataKeywordsMap = new HashMap<>();
			for( PratilipiData pratilipiData : pratilipiDataList )
				pratilipiDataKeywordsMap.put( pratilipiData, getPratilipiKeywords( pratilipiData.getId() ) );
			
			searchAccessor.indexPratilipiDataList( pratilipiDataKeywordsMap );
		}
	}
	
	public static void updateFacebookScrape( List<Long> pratilipiIdList ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		for( Long pratilipiId : pratilipiIdList ) {
			Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
			if( pratilipi.getState() != PratilipiState.PUBLISHED )
				continue;
			
			Page page = dataAccessor.getPage( PageType.PRATILIPI, pratilipiId );
			String uri = page.getUriAlias() == null ? page.getUri() : page.getUriAlias();
			FacebookApi.postScrapeRequest( "http://" + pratilipi.getLanguage().getHostName() + uri );
		}
		
	}

	public static String getPratilipiContent( long pratilipiId, Integer chapterNo )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! hasAccessToReadPratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		
		BlobEntry blobEntry = DataAccessorFactory.getBlobAccessor().getBlob( CONTENT_FOLDER + "/" + pratilipiId );
		if( blobEntry == null )
			return null;
		
		String contentHtml = new String( blobEntry.getData(), Charset.forName( "UTF-8" ) );
		PratilipiContentUtil pratilipiContentUtil = new PratilipiContentUtil( contentHtml );
		return pratilipiContentUtil.getContent( chapterNo );
		
	}
		
	public static int updatePratilipiContent( long pratilipiId, int pageNo,
			PratilipiContentType contentType, Object pageContent, boolean insertNew )
			throws InvalidArgumentException, InsufficientAccessException,
			UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! hasAccessToUpdatePratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.PRATILIPI_UPDATE,
				pratilipi );

		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		
		if( contentType == PratilipiContentType.PRATILIPI ) {
			BlobEntry blobEntry = blobAccessor.getBlob( CONTENT_FOLDER + "/" + pratilipiId );
			if( blobEntry == null ) {
				blobEntry = blobAccessor.newBlob( CONTENT_FOLDER + "/" + pratilipiId );
				blobEntry.setData( "&nbsp".getBytes( Charset.forName( "UTF-8" ) ) );
				blobEntry.setMimeType( "text/html" );
			}
			
			String content = new String( blobEntry.getData(), Charset.forName( "UTF-8" ) );
			PratilipiContentUtil pratilipiContentUtil = new PratilipiContentUtil( content );
			content = pratilipiContentUtil.updateContent( pageNo, (String) pageContent, insertNew );
			int pageCount = pratilipiContentUtil.getPageCount();
			if( content.isEmpty() ) {
				content = "&nbsp";
				pageCount = 1;
			}
			blobEntry.setData( content.getBytes( Charset.forName( "UTF-8" ) ) );
			blobAccessor.createOrUpdateBlob( blobEntry );
			
			pratilipi.setPageCount( pageCount );
			if( insertNew )
				auditLog.setEventComment( "Added new page " + pageNo + " in Pratilpi content." );
			else if( ! ( (String) pageContent ).isEmpty() )
				auditLog.setEventComment( "Updated page " + pageNo + " in Pratilpi content." );
			else
				auditLog.setEventComment( "Deleted page " + pageNo + " in Pratilpi content." );
			
		} else if( contentType == PratilipiContentType.IMAGE ) {
			
			BlobEntry blobEntry = (BlobEntry) pageContent;
			blobEntry.setName( IMAGE_CONTENT_FOLDER + "/" + pratilipiId + "/" + pageNo );
			blobAccessor.createOrUpdateBlob( blobEntry );
			
			if( pageNo > (int) pratilipi.getPageCount() )
				pratilipi.setPageCount( pageNo );
			
			auditLog.setEventComment( "Uploaded page " + pageNo + " in Image content." );
		
		} else {
			throw new InvalidArgumentException( contentType + " content type is not yet supported." );
		}
		
		pratilipi.setLastUpdated( new Date() );
		
		pratilipi = dataAccessor.createOrUpdatePratilipi( pratilipi, auditLog );
		
		return pratilipi.getPageCount();
	}

	public static String getPratilipiKeywords( Long pratilipiId ) throws UnexpectedServerException {
		
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		
		PratilipiMetaDoc pmDoc = docAccessor.getPratilipiMetaDoc( pratilipiId );
		if( pmDoc == null )
			return null;
		
		String keywords = "";
		for( String str : pmDoc.getWordCounts().keySet() )
			keywords += str + " ";
		
		keywords = keywords.trim();
		return keywords.isEmpty() ? null : keywords;
		
	}
	
	public static boolean updatePratilipiTags(long pratilipiId, PratilipiType pratilipiType, List<Long> tagIds,
			List<String> suggestedTags) throws InsufficientAccessException, UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi(pratilipiId);
		if (pratilipi == null)
			return false;

		AuditLog auditLog = dataAccessor.newAuditLog(AccessTokenFilter.getAccessToken(), AccessType.PRATILIPI_UPDATE,
				pratilipi);

		PratilipiData pratilipiData = createPratilipiData(pratilipi);

		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		logger.log(Level.INFO, "User Id : " + accessToken.getUserId());

		if (!hasAccessToUpdatePratilipiData(pratilipi, pratilipiData)) {
			throw new InsufficientAccessException();
		}

		if (pratilipiType != null)
			pratilipi.setType(pratilipiType);

		if (tagIds != null)
			pratilipi.setTagIds(tagIds);

		if (suggestedTags != null)
			pratilipi.setSuggestedTags(suggestedTags);

		dataAccessor.createOrUpdatePratilipi(pratilipi, auditLog);

		return true;
	}
	
}
