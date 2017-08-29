package com.pratilipi.data.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.UserFollowState;
import com.pratilipi.common.util.*;
import com.pratilipi.data.BlobAccessor;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.Memcache;
import com.pratilipi.data.SearchAccessor;
import com.pratilipi.data.client.AuthorByReadCountData;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.type.*;
import com.pratilipi.filter.AccessTokenFilter;

public class AuthorDataUtil {
	
	private static final Logger logger =
			Logger.getLogger( AuthorDataUtil.class.getName() );

	
	public static boolean hasAccessToListAuthorData( Language language ) {
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		return UserAccessUtil.hasUserAccess( accessToken.getUserId(), language, AccessType.AUTHOR_LIST );
	}

	public static boolean hasAccessToAddAuthorData( AuthorData authorData ) {
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		return UserAccessUtil.hasUserAccess( accessToken.getUserId(), authorData.getLanguage(), AccessType.AUTHOR_ADD );
	}

	public static boolean hasAccessToUpdateAuthorData( Author author, AuthorData authorData ) {

		// Case 1: Only ACTIVE Author Profiles can be updated.
		if( author.getState() != AuthorState.ACTIVE )
			return false;

		// Case 2: User with AUTHOR_UPDATE access can update any Author Profile.
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), author.getLanguage(), AccessType.AUTHOR_UPDATE ) ) {
			if( authorData == null || ! authorData.hasLanguage() || authorData.getLanguage() == author.getLanguage() )
				return true;
			else if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), authorData.getLanguage(), AccessType.AUTHOR_UPDATE ) )
				return true;
		}

		// Case 3: User can update their own Author Profile.
		if( author.getUserId() != null )
			return accessToken.getUserId().equals( author.getUserId() );
			
		return false;
		
	}
	
	
	public static String createAuthorProfileImageUrl( Author author ) {
		return createAuthorProfileImageUrl( author, null );
	}
	
	public static String createAuthorProfileImageUrl( Author author, Integer width ) {
		String url = "/author/image";
		if( author.getProfileImage() == null ) {
			url = url + "?version=2";
			if( width != null )
				url = url + "&width=" + width;
			if( SystemProperty.CDN != null )
				url = SystemProperty.CDN.replace( "*", "0" ) + url;
		} else {
			url = url + "?authorId=" + author.getId() + "&version=" + author.getProfileImage();
			if( width != null )
				url = url + "&width=" + width;
			if( SystemProperty.CDN != null )
				url = SystemProperty.CDN.replace( "*", author.getId() % 5 + 1 + "" ) + url;
		}
		return url;
	}
	
	
	public static String createAuthorCoverImageUrl( Author author ) {
		return createAuthorCoverImageUrl( author, null );
	}
	
	public static String createAuthorCoverImageUrl( Author author, Integer width ) {
		String url = "/author/cover";
		if( author.getCoverImage() == null ) {
			if( width != null )
				url = url + "?width=" + width;
			if( SystemProperty.CDN != null )
				url = SystemProperty.CDN.replace( "*", "0" ) + url;
		} else {
			url = url + "?authorId=" + author.getId() + "&version=" + author.getCoverImage();
			if( width != null )
				url = url + "&width=" + width;
			if( SystemProperty.CDN != null )
				url = SystemProperty.CDN.replace( "*", author.getId() % 5 + 1 + "" ) + url;
		}
		return url;
	}
	
	
	public static AuthorData createAuthorData( Author author ) {

		if( author == null )
			return null;
		
		return createAuthorData(
				author,
				DataAccessorFactory.getDataAccessor().getPage( PageType.AUTHOR, author.getId() ) );
	
	}
	
	public static AuthorData createAuthorData( Author author, Page authorPage ) {
		
		AuthorData authorData = new AuthorData();
		
		authorData.setId( author.getId() );
		
		authorData.setFirstName( author.getFirstName() );
		authorData.setLastName( author.getLastName() );
		authorData.setPenName( author.getPenName() );

		if( author.getFirstName() != null && author.getLastName() == null )
			authorData.setName( author.getFirstName() );
		else if( author.getFirstName() == null && author.getLastName() != null )
			authorData.setName( author.getLastName() );
		else if( author.getFirstName() != null && author.getLastName() != null )
			authorData.setName( author.getFirstName() + " " + author.getLastName() );
		
		if( authorData.getName() != null && author.getPenName() == null )
			authorData.setFullName( authorData.getName() );
		else if( authorData.getName() == null && author.getPenName() != null )
			authorData.setFullName( author.getPenName() );
		else if( authorData.getName() != null && author.getPenName() != null )
			authorData.setFullName( authorData.getName() + " '" + author.getPenName() + "'" );
		
		authorData.setFirstNameEn( author.getFirstNameEn() );
		authorData.setLastNameEn( author.getLastNameEn() );
		authorData.setPenNameEn( author.getPenNameEn() );
		
		if( author.getFirstNameEn() != null && author.getLastNameEn() == null )
			authorData.setNameEn( author.getFirstNameEn() );
		else if( author.getFirstNameEn() == null && author.getLastNameEn() != null )
			authorData.setNameEn( author.getLastNameEn() );
		else if( author.getFirstNameEn() != null && author.getLastNameEn() != null )
			authorData.setNameEn( author.getFirstNameEn() + " " + author.getLastNameEn() );
		
		if( authorData.getNameEn() != null && author.getPenNameEn() == null )
			authorData.setFullNameEn( authorData.getNameEn() );
		else if( authorData.getNameEn() == null && author.getPenNameEn() != null )
			authorData.setFullNameEn( author.getPenNameEn() );
		else if( authorData.getNameEn() != null && author.getPenNameEn() != null )
			authorData.setFullNameEn( authorData.getNameEn() + " '" + author.getPenNameEn() + "'" );

		authorData.setLanguage( author.getLanguage() );
		authorData.setLocation( author.getLocation() );
		authorData.setSummary( HtmlUtil.toPlainText( author.getSummary() ) );
		
		authorData.setPageUrl( authorPage.getUriAlias() == null ? authorPage.getUri() : authorPage.getUriAlias() );
		authorData.setImageUrl( createAuthorProfileImageUrl( author ) );
		authorData.setHasCoverImage( author.getCoverImage() != null );
		authorData.setCoverImageUrl( createAuthorCoverImageUrl( author ) );
		authorData.setHasProfileImage( author.getProfileImage() != null );
		authorData.setProfileImageUrl( createAuthorProfileImageUrl( author ) );

		authorData.setRegistrationDate( author.getRegistrationDate() );
		authorData.setFollowCount( author.getFollowCount() );
		authorData.setContentDrafted( author.getContentDrafted() );
		authorData.setContentPublished( author.getContentPublished() );
		authorData.setTotalReadCount( author.getTotalReadCount() );
		authorData.setTotalFbLikeShareCount( author.getTotalFbLikeShareCount() );
		
		authorData.setAccessToUpdate( hasAccessToUpdateAuthorData( author, null ) );
		
		// Add meta-data
		if( hasAccessToUpdateAuthorData( author, null ) ) {
			authorData.setDateOfBirth( author.getDateOfBirth() );
			authorData.setGender( author.getGender() );
		}

		return authorData;
		
	}
	
	public static AuthorData createAuthorData( Author author, Page authorPage, User user ) {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		if( authorPage == null )
			authorPage = dataAccessor.getPage( PageType.AUTHOR, author.getId() );

		AuthorData authorData = createAuthorData( author, authorPage );
		if( user == null )
			authorData.setUser( UserDataUtil.createUserData( dataAccessor.getUser( author.getUserId() ), null ) );
		else
			authorData.setUser( UserDataUtil.createUserData( user, null ) );

		return authorData;

	}
	
	public static List<AuthorData> createAuthorDataList( List<Long> authorIdList, boolean includeUserData ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		List<Author> authorList = dataAccessor.getAuthorList( authorIdList );
		Map<Long, Page> authorPages = dataAccessor.getPages( PageType.AUTHOR, authorIdList );
		
		List<AuthorData> authorDataList = new ArrayList<>( authorIdList.size() );
		
		if( includeUserData ) {
			
			List<Long> userIdList = new ArrayList<>( authorIdList.size() );
			for( Author author : authorList )
				if( author.getUserId() != null )
					userIdList.add( author.getUserId() );
			
			List<User> userList = dataAccessor.getUserList( userIdList );
			
			Map<Long, User> users = new HashMap<>( userIdList.size() );
			for( User user : userList )
				users.put( user.getId(), user );
			
			for( Author author : authorList )
				authorDataList.add( createAuthorData(
						author,
						authorPages.get( author.getId() ),
						users.get( author.getUserId() ) ) );
			
		} else {
			
			for( Author author : authorList )
				authorDataList.add( createAuthorData(
						author,
						authorPages.get( author.getId() ) ) );
			
		}
		
		return authorDataList;
		
	}
	

	public static DataListCursorTuple<AuthorData> getAuthorDataList(
			String searchQuery, AuthorFilter authorFilter,
			String cursor, Integer resultCount )
			throws InsufficientAccessException {
		
		if( ! hasAccessToListAuthorData( authorFilter.getLanguage() ) )
			throw new InsufficientAccessException();
		
		// Processing search query
		if( searchQuery != null )
			searchQuery = searchQuery.toLowerCase().trim()
					.replaceAll( ",|\\sor\\s", " " )
					.replaceAll( "[\\s]+", " OR " );

		DataListCursorTuple<Long> authorIdListCursorTuple = DataAccessorFactory
				.getSearchAccessor()
				.searchAuthor( searchQuery, authorFilter, cursor, null, resultCount );
		
		List<AuthorData> authorDataList = createAuthorDataList( authorIdListCursorTuple.getDataList(), true );
		
		return new DataListCursorTuple<AuthorData>( authorDataList, authorIdListCursorTuple.getCursor() );
		
	}
	
	
	public static Long createAuthorProfile( UserData userData, Language language ) {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		Author author = dataAccessor.getAuthorByUserId( userData.getId() );
		if( author != null && author.getState() != AuthorState.DELETED )
			return author.getId();
		else
			author = dataAccessor.newAuthor();
		
		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.AUTHOR_ADD,
				author
		);
			
		author.setUserId( userData.getId() );
		author.setFirstName( userData.getFirstName() );
		author.setLastName( userData.getLastName() );
		author.setGender( userData.getGender() );
		author.setDateOfBirth( userData.getDateOfBirth() );
		author.setLanguage( language );
		author.setState( AuthorState.ACTIVE );
		author.setRegistrationDate( userData.getSignUpDate() );
		author.setLastUpdated( userData.getSignUpDate() );
		
		author = dataAccessor.createOrUpdateAuthor( author, auditLog );
		
		createOrUpdateAuthorPageUrl( author.getId() );
		
		return author.getId();
		
	}
	
	public static AuthorData saveAuthorData( AuthorData authorData )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {
		
		_validateAuthorDataForSave( authorData );

		boolean isNew = authorData.getId() == null;
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Author author = isNew ? dataAccessor.newAuthor() : dataAccessor.getAuthor( authorData.getId() );
		
		if( isNew && ! hasAccessToAddAuthorData( authorData ) )
			throw new InsufficientAccessException();
		if( ! isNew && ! hasAccessToUpdateAuthorData( author, authorData ) )
			throw new InsufficientAccessException();
		
		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				isNew ? AccessType.AUTHOR_ADD : AccessType.AUTHOR_UPDATE,
				author
		);
		
		
		if( authorData.hasFirstName() )
			author.setFirstName( authorData.getFirstName() );
		if( authorData.hasLastName() )
			author.setLastName( authorData.getLastName() );
		if( authorData.hasPenName() )
			author.setPenName( authorData.getPenName() );

		if( authorData.hasFirstNameEn() )
			author.setFirstNameEn( authorData.getFirstNameEn() );
		if( authorData.hasLastNameEn() )
			author.setLastNameEn( authorData.getLastNameEn() );
		if( authorData.hasPenNameEn() )
			author.setPenNameEn( authorData.getPenNameEn() );
		
		if( authorData.hasGender() )
			author.setGender( authorData.getGender() );
		if( authorData.hasDateOfBirth() )
			author.setDateOfBirth( authorData.getDateOfBirth() );
		
		if( authorData.hasLanguage() )
			author.setLanguage( authorData.getLanguage() );
		if( authorData.hasLocation() )
			author.setLocation( authorData.getLocation() );
		if( authorData.hasSummary() )
			author.setSummary( authorData.getSummary() );

		if( authorData.hasState() )
			author.setState( authorData.getState() );
		if( isNew )
			author.setRegistrationDate( new Date() );
		author.setLastUpdated( new Date() );

		
		author = dataAccessor.createOrUpdateAuthor( author, auditLog );

		if( isNew )
			createOrUpdateAuthorPageUrl( author.getId() );

		return createAuthorData( author );
		
	}
	
	private static void _validateAuthorDataForSave( AuthorData authorData ) throws InvalidArgumentException {
		
		boolean isNew = authorData.getId() == null;
		JsonObject errorMessages = new JsonObject();

		
		// Language is mandatory.
		if( isNew && ( ! authorData.hasLanguage() || authorData.getLanguage() == null ) )
			errorMessages.addProperty( "langauge", GenericRequest.ERR_LANGUAGE_REQUIRED );
		// Language can not be un-set or set to null.
		if( ! isNew && authorData.hasLanguage() && authorData.getLanguage() == null )
			errorMessages.addProperty( "langauge", GenericRequest.ERR_LANGUAGE_REQUIRED );

		
		// State must be ACTIVE for new profile.
		if( isNew && ( ! authorData.hasState() || authorData.getState() != AuthorState.ACTIVE ) )
			errorMessages.addProperty( "state", GenericRequest.ERR_AUTHOR_STATE_INVALID );
		// State can not be un-set or set to null.
		if( ! isNew && authorData.hasState() && authorData.getState() == null )
			errorMessages.addProperty( "state", GenericRequest.ERR_AUTHOR_STATE_REQUIRED );


		if( errorMessages.entrySet().size() > 0 )
			throw new InvalidArgumentException( errorMessages );
		
	}
	
	
	public static BlobEntry getAuthorProfileImage( Long authorId, String version, Integer width )
			throws UnexpectedServerException {

		String coverImagePath = null;
		
		if( authorId != null && version != null )
			coverImagePath = "author/" + authorId + "/images/profile/" + version;
		else
			coverImagePath = "author/default/images/profile";
			
		BlobEntry blobEntry = DataAccessorFactory.getBlobAccessor().getBlob( coverImagePath );
		
		if( width != null )
			blobEntry = ImageUtil.resize( blobEntry, width, width );
		
		return blobEntry;
		
	}
	
	public static String saveAuthorImage( Long authorId, BlobEntry blobEntry )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		
		if( blobEntry.getData() == null || blobEntry.getData().length == 0 )
			throw new InvalidArgumentException( "Image data is missing." );
			
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Author author = dataAccessor.getAuthor( authorId );

		if( ! hasAccessToUpdateAuthorData( author, null ) )
			throw new InsufficientAccessException();

		
		String profileImageName = new Date().getTime() + "";
		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		blobEntry.setName( "author/" + authorId + "/images/profile/" + profileImageName );
		blobAccessor.createOrUpdateBlob( blobEntry );
		
		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.AUTHOR_UPDATE,
				author );

		author.setProfileImage( profileImageName );
		author.setLastUpdated( new Date() );

		author = dataAccessor.createOrUpdateAuthor( author, auditLog );
		
		return createAuthorProfileImageUrl( author );
		
	}

	public static BlobEntry getAuthorCoverImage( Long authorId, String version, Integer width )
			throws UnexpectedServerException {
		
		String coverImagePath = null;
		
		if( authorId != null && version != null )
			coverImagePath = "author/" + authorId + "/images/cover/" + version;
		else
			coverImagePath = "author/default/images/cover";
		
		BlobEntry blobEntry = DataAccessorFactory.getBlobAccessor().getBlob( coverImagePath );
		
		if( width != null )
			blobEntry = ImageUtil.resize( blobEntry, width, width );
		
		return blobEntry;
		
	}
	
	public static String saveAuthorCoverImage( Long authorId, BlobEntry blobEntry )
			throws InsufficientAccessException, UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Author author = dataAccessor.getAuthor( authorId );

		if( ! hasAccessToUpdateAuthorData( author, null ) )
			throw new InsufficientAccessException();

		
		String coverImageName = new Date().getTime() + "";
		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		blobEntry.setName( "author/" + authorId + "/images/cover/" + coverImageName );
		blobAccessor.createOrUpdateBlob( blobEntry );
		

		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.AUTHOR_UPDATE,
				author );

		author.setCoverImage( coverImageName );
		author.setLastUpdated( new Date() );
		
		author = dataAccessor.createOrUpdateAuthor( author, auditLog );
		
		return createAuthorCoverImageUrl( author );
		
	}
	
	public static void removeAuthorImage( Long authorId, boolean coverImage, boolean profileImage )
			throws InsufficientAccessException {
		
		if( ! coverImage && ! profileImage )
			return;
		
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Author author = dataAccessor.getAuthor( authorId );

		if( ! hasAccessToUpdateAuthorData( author, null ) )
			throw new InsufficientAccessException();


		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.AUTHOR_UPDATE,
				author
		);

		
		if( coverImage )
			author.setCoverImage( null );
		if( profileImage )
			author.setProfileImage( null );
		author.setLastUpdated( new Date() );
		
		
		author = dataAccessor.createOrUpdateAuthor( author, auditLog );
		
	}
	
	
	public static boolean createOrUpdateAuthorPageUrl( Long authorId ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Author author = dataAccessor.getAuthor( authorId );
		Page page = dataAccessor.getPage( PageType.AUTHOR, authorId );

		boolean isNew = page == null;
		boolean isChanged = false;

		if( isNew ) {
			page = dataAccessor.newPage();
			page.setType( PageType.AUTHOR );
			page.setUri( PageType.AUTHOR.getUrlPrefix() + authorId );
			page.setPrimaryContentId( authorId );
			page.setCreationDate( new Date() );
		}

		
		if( author.getState() == AuthorState.DELETED ) {
			
			if( page.getUriAlias() == null ) {
				if( ! isNew )
					return false;
			} else {
				page.setUriAlias( null );
				isChanged = true;
			}
			
		} else if( page.getUriAlias() == null ) { // Static Author page urls
		
			String uriAlias = UriAliasUtil.generateUriAlias(
					page.getUriAlias(),
					"/", author.getFirstNameEn(), author.getLastNameEn(), author.getPenNameEn() );
	
//			if( ! isNew ) {
//				if( uriAlias == page.getUriAlias()
//						|| ( uriAlias != null && uriAlias.equals( page.getUriAlias() ) )
//						|| ( page.getUriAlias() != null && page.getUriAlias().equals( uriAlias ) ) )
//					return false;
//			}
			
			logger.log( Level.INFO, "Updating Author Page Url: '" + page.getUriAlias() + "' -> '" + uriAlias + "'" );
		
			page.setUriAlias( uriAlias );
			isChanged = true;
		
		}

		if( isNew || isChanged )
			page = dataAccessor.createOrUpdatePage( page );

		return true;

	}
	
	public static boolean createOrUpdateAuthorDashboardPageUrl( Long authorId ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Page page = dataAccessor.getPage( PageType.AUTHOR, authorId );
		
		Page dashboardPage = dataAccessor.getPage( PageType.AUTHOR_DASHBOARD, authorId );
		if( dashboardPage == null ) {
			dashboardPage = dataAccessor.newPage();
			dashboardPage.setType( PageType.AUTHOR_DASHBOARD );
			dashboardPage.setUri( page.getUri() + "/dashboard" );
			dashboardPage.setPrimaryContentId( authorId );
			dashboardPage.setCreationDate( new Date() );
			if( page.getUriAlias() == null )
				dashboardPage = dataAccessor.createOrUpdatePage( dashboardPage );
		}
		
		if( page.getUriAlias() == null ) {
		
			if( dashboardPage.getUriAlias() == null )
				return false;
			
			dashboardPage.setUriAlias( null );
		
		} else {
			
			String uriAlias = page.getUriAlias() + "/dashboard";
			if( uriAlias.equals( dashboardPage.getUriAlias() ) )
				return false;
			
			dashboardPage.setUriAlias( uriAlias );
			
		}

		dashboardPage = dataAccessor.createOrUpdatePage( dashboardPage );

		return true;
		
	}
	
	public static boolean updateAuthorStats( Long authorId ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		PratilipiFilter pratilipiFilter = new PratilipiFilter();
		pratilipiFilter.setAuthorId( authorId );
		// This call is not consistent due to some issue with Google AppEngine
		// Relying on Memcahe until the issue is fixed
//		List<Pratilipi> pratilipiList = dataAccessor.getPratilipiList( pratilipiFilter, null, null ).getDataList();
		List<Long> pratilipiIdList = dataAccessor.getPratilipiIdList( pratilipiFilter, null, null, null ).getDataList();
		List<Pratilipi> pratilipiList = dataAccessor.getPratilipiList( pratilipiIdList );
		
		int contentDrafted = 0;
		int contentPublished = 0;
		long totalReadCount = 0;
		long totalFbLikeShareCount = 0;
		for( Pratilipi pratilipi : pratilipiList ) {
			if( pratilipi.getState() == PratilipiState.DRAFTED ) {
				contentDrafted++;
			} else if( pratilipi.getState() == PratilipiState.PUBLISHED ) {
				contentPublished++;
				totalReadCount = totalReadCount + pratilipi.getReadCountOffset() + pratilipi.getReadCount();
				totalFbLikeShareCount = totalFbLikeShareCount + pratilipi.getFbLikeShareCount();
			}
		}
		
		Author author = dataAccessor.getAuthor( authorId );
		if( (int) author.getContentPublished() == contentPublished
				&& (long) author.getTotalReadCount() == totalReadCount
				&& (long) author.getTotalFbLikeShareCount() == totalFbLikeShareCount )
			return false;

		author.setContentDrafted( contentDrafted );
		author.setContentPublished( contentPublished );
		author.setTotalReadCount( totalReadCount );
		author.setTotalFbLikeShareCount( totalFbLikeShareCount );
		author = dataAccessor.createOrUpdateAuthor( author );
		return true;

	}
	
	public static void updateUserAuthorStats( Long authorId ) throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		Author author = dataAccessor.getAuthor( authorId );
		if( author.getState() != AuthorState.ACTIVE )
			return;

		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.AUTHOR_UPDATE,
				author );

		Long followCount = (long) dataAccessor.getUserAuthorFollowList( null, authorId, null, null, null ).getDataList().size();
		author.setFollowCount( followCount );
		
		author = dataAccessor.createOrUpdateAuthor( author, auditLog );

		_updateUserAuthorStats( authorId, followCount );

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
	private static void _updateUserAuthorStats( Long authorId, Long followCount )
			throws UnexpectedServerException {
		AccessToken accessToken = _getWorkerAccessToken();
		Map<String, String> headersMap = new HashMap<>();
		headersMap.put( "Access-Token", accessToken.getId() );
		headersMap.put( "User-Id", accessToken.getUserId().toString() );
		JsonObject body = new JsonObject();
		body.addProperty( "followCount", followCount );
		HttpUtil.doPost( SystemProperty.ECS_PAG_ENDPOINT + "/api/authors/" + authorId + "/follow-count", headersMap, body );
	}

	public static void updateAuthorSearchIndex( Long authorId )
			throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		SearchAccessor searchAccessor = DataAccessorFactory.getSearchAccessor();

		Author author = dataAccessor.getAuthor( authorId );
		if( author.getState() == AuthorState.ACTIVE ) {
			User user = author.getUserId() == null ? null : dataAccessor.getUser( author.getUserId() );
			searchAccessor.indexAuthorData(
					createAuthorData( author ),
					UserDataUtil.createUserData( user )
			);
		} else {
			searchAccessor.deleteAuthorDataIndex( authorId );
		}
		
	}

	public static DataListCursorTuple<AuthorData> getRecommendedAuthorList(
			Long userId, Language language, String cursorStr, Integer resultCount )
			throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		UserFollowsDoc followsDoc = docAccessor.getUserFollowsDoc( userId );

		
		// Authors to ignore = Authors Following + Authors Ignored
		List<Long> authorIdsToIgnore = new ArrayList<Long>();
		if( followsDoc != null ) {
			for( UserAuthorDoc userAuthorDoc : followsDoc.getFollows( UserFollowState.FOLLOWING ) )
				authorIdsToIgnore.add( userAuthorDoc.getAuthorId() );
			for( UserAuthorDoc userAuthorDoc : followsDoc.getFollows( UserFollowState.IGNORED ) )
				if( new Date().getTime() - userAuthorDoc.getFollowDate().getTime() >= TimeUnit.DAYS.toMillis( 30 ) )
					authorIdsToIgnore.add( userAuthorDoc.getAuthorId() );
		}


		// Get global list of recommended authors
		List<Long> recommendedList = _getRecommendAuthorGlobalList( language );

		
		// If cursor is passed, drop all items up till cursor
		Long cursor = cursorStr == null ? null : Long.parseLong( cursorStr );
		if( cursor != null && recommendedList.contains( cursor ) )
			while( ! recommendedList.remove( 0 ).equals( cursor ) )
				continue;

		// Remove Author ids to be ignored
		recommendedList.removeAll( authorIdsToIgnore );

		// Drop items if recommendedList size is requested count
		if( resultCount != null )
			recommendedList = recommendedList.subList( 0, Math.min( resultCount, recommendedList.size() ) );

		Map<Long, Author> authors = dataAccessor.getAuthors( recommendedList );
		Map<Long, Page> authorPages = dataAccessor.getPages( PageType.AUTHOR, recommendedList );

		List<AuthorData> recommendAuthorData = new ArrayList<>( recommendedList.size() );
		for( Long authorId : recommendedList )
			recommendAuthorData.add( createAuthorData( authors.get( authorId ), authorPages.get( authorId ) ) );

		Collections.shuffle( recommendAuthorData );

		return new DataListCursorTuple<AuthorData>(
				recommendAuthorData,
				recommendedList.isEmpty() ? null : recommendedList.get( recommendedList.size() - 1 ).toString() );

	}

	private static List<Long> _getRecommendAuthorGlobalList( Language language ) {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Memcache memcache = DataAccessorFactory.getL2CacheAccessor();

		String memcacheId = "AuthorDataUtil.RecommendAuthorGlobalList-" + language.getCode();

		List<Long> authorList = memcache.get( memcacheId );

		if( authorList != null )
			return authorList;

		Long minReadCount;
		switch( language ) {
			case BENGALI:
				minReadCount = 3000L; break;
			case GUJARATI:
				minReadCount = 2000L; break;
			case HINDI:
				minReadCount = 7500L; break;
			case KANNADA:
				minReadCount = 200L; break;
			case MALAYALAM:
				minReadCount = 4000L; break;
			case MARATHI:
				minReadCount = 2000L; break;
			case TAMIL:
				minReadCount = 2200L; break;
			case TELUGU:
				minReadCount = 500L; break;
			default: 
				minReadCount = 2000L;
		}

		authorList = dataAccessor.getAuthorIdListWithMaxReadCount( language, minReadCount, 1000 );

		// Algorithm - Shuffling the list in order
		int[] order = { 1, 3, 2, 4, 6, 5, 7, 9, 8, 10, 12, 11, 13, 15, 14, 16, 18, 17, 19, 21, 20, 22, 24, 23 };
		int orderSize = order.length;

		ArrayList<Long> resultList = new ArrayList<>( authorList.size() );

		int chunkSize = authorList.size() / orderSize;

		for( int i = 0; i < order.length; i++ ) {
			int beginIndex = ( order[i] - 1 ) * chunkSize;
			List<Long> subList = authorList.subList( beginIndex, beginIndex + chunkSize );
			Collections.shuffle( subList );
			resultList.addAll( subList );
		}

		resultList.addAll( authorList.subList( authorList.size() - ( authorList.size() % orderSize ), authorList.size() ) );

		// Caching the result in memcache for 3 hours
		memcache.put( memcacheId, resultList, 180 );

		return resultList;

	}

	/**
	 * Developer : Rahul Ranjan
	 * Function : Following functions are used for Author leaders board.
	 */

	public static AuthorByReadCountData createAuthorByReadCountData(AuthorByReadCount authorByReadCount) {
		AuthorByReadCountData authorByReadCountData = new AuthorByReadCountData();

		authorByReadCountData.setId(authorByReadCount.getId());
		authorByReadCountData.setAuthorId(authorByReadCount.getAuthorId());
		authorByReadCountData.setName(authorByReadCount.getName());
		authorByReadCountData.setUserId(authorByReadCount.getUserId());
		authorByReadCountData.setImageUrl(authorByReadCount.getCoverImageUrl());
		authorByReadCountData.setPageUrl(authorByReadCount.getProfilePageUrl());
		authorByReadCountData.setReadCount(authorByReadCount.getReadCount());

		return authorByReadCountData;
	}

	public static List<AuthorByReadCountData> createAuthorByReadCountDataList(List<AuthorByReadCount> authorByReadCountList) {
		List<AuthorByReadCountData> authorByReadCountDataList = new ArrayList<>(authorByReadCountList.size());

		for (AuthorByReadCount authorByReadCount : authorByReadCountList) {
			logger.log(Level.INFO, "Author Name : " + authorByReadCount.getName());
			AuthorByReadCountData data = createAuthorByReadCountData(authorByReadCount);
			authorByReadCountDataList.add(data);
		}

		return authorByReadCountDataList;
	}

	public static DataListCursorTuple<AuthorByReadCountData> getAuthorListByReadCount(Language language, Integer resultCount, String cursor) {
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		AppProperty appProperty = dataAccessor.getAppProperty(AppProperty.TOP_AUTHORS_DATE);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String dateString;
		if (appProperty == null) {
			// IF AppProperty is not present return day before yesterday's date.
			// Same is present in AppPropertyUtil.getTopAuthorLoadDate function, line 52
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			dateString = dateFormat.format(cal.getTime());
		} else {
            logger.log(Level.INFO, "AppProperty value : " + appProperty.getValue());
            dateString = appProperty.getValue();
        }

		try {
			Date date = dateFormat.parse(dateString);
            logger.log(Level.INFO, "Filter : " + date);

            DataListCursorTuple<AuthorByReadCount> cursorTuple = dataAccessor.getAuthorsByReadCount(date, language, resultCount, cursor);
            List<AuthorByReadCountData> authorsData = createAuthorByReadCountDataList(cursorTuple.getDataList());

            logger.log(Level.INFO, "Number of authors returned : " + authorsData.size());

            return new DataListCursorTuple<>(
					authorsData, cursorTuple.getCursor(), cursorTuple.getNumberFound()
			);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Error while parsing date.");
			return null;
		}

	}

	public static boolean updateAuthorByReadCount(Long pratilipiId, Long webReadCount, Long androidReadCount, String dateString) {


		DataAccessor  dataAccessor = DataAccessorFactory.getDataAccessor();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {

			Date date;
			if (dateString != null) {
				date = dateFormat.parse(dateString);
			} else {
				// If date is not present in API request, update yesterday's date.
				Calendar cal = Calendar.getInstance();
				cal.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
				cal.add(Calendar.DATE, -1);
				date = dateFormat.parse(dateFormat.format(cal.getTime()));
			}
			logger.log(Level.INFO, "Date : " + date);
			Pratilipi pratilipi = dataAccessor.getPratilipi(pratilipiId);
			String authorId = pratilipi.getAuthorId().toString();
			String id = authorId + "-" + date;
			AuthorByReadCount authorByReadCount = dataAccessor.getAuthorByReadCountById(id);

			if (authorByReadCount == null) {
				logger.log(Level.INFO, "authorByReadCount not present");
				Author author = dataAccessor.getAuthor(pratilipi.getAuthorId());
				AuthorData authorData = createAuthorData(author);

				authorByReadCount = dataAccessor.newAuthorByReadCount();

				authorByReadCount.setAuthorId(authorId);
				// Update user id to "0" if its null or 0
				if (author.getUserId() == null
						|| author.getId() == 5113251712991232L  // excluding unknown
						|| author.getUserId() == 0L) {
				    authorByReadCount.setHasUserId(false);
                    authorByReadCount.setUserId("0");
                } else {
                    authorByReadCount.setHasUserId(true);
                    authorByReadCount.setUserId(author.getUserId().toString());
                }
				authorByReadCount.setName(authorData.getName() != null ? authorData.getName() : authorData.getNameEn());
				authorByReadCount.setCoverImageUrl(createAuthorProfileImageUrl(author));
				authorByReadCount.setProfilePageUrl(authorData.getPageUrl());
				authorByReadCount.setLanguage(author.getLanguage());
				authorByReadCount.setDate(date);

                authorByReadCount.setReadCountWeb(webReadCount);
                authorByReadCount.setReadCountAndroid(androidReadCount);
                authorByReadCount.setReadCount(webReadCount + androidReadCount);
            } else {
				logger.log(Level.INFO, "authorByReadCount present already");
			    Long currentWebRc = authorByReadCount.getReadCountWeb();
			    Long currentAndroidRc = authorByReadCount.getReadCountAndroid();
			    Long currentTotalRC = authorByReadCount.getReadCount();

                authorByReadCount.setReadCountWeb(currentWebRc + webReadCount);
                authorByReadCount.setReadCountAndroid(currentAndroidRc + androidReadCount);
                authorByReadCount.setReadCount(currentTotalRC + webReadCount + androidReadCount);
            }


			dataAccessor.createOrUpdateAuthorByReadCount(authorByReadCount, date);
			return true;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static Boolean deleteAuthorsByReadCount(List<String> ids) {
		DataAccessorFactory.getDataAccessor().deleteAuthorByReadCount(ids);
		return true;
	}

}
