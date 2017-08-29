package com.pratilipi.data;

import static com.pratilipi.data.mock.AuthorMock.AUTHOR_TABLE;
import static com.pratilipi.data.mock.PageMock.PAGE_TABLE;
import static com.pratilipi.data.mock.PratilipiMock.PRATILIPI_TABLE;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.pratilipi.data.type.*;
import com.pratilipi.data.type.gae.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.BatchProcessState;
import com.pratilipi.common.type.BatchProcessType;
import com.pratilipi.common.type.BlogPostState;
import com.pratilipi.common.type.CommentParentType;
import com.pratilipi.common.type.ContactTeam;
import com.pratilipi.common.type.EmailState;
import com.pratilipi.common.type.EmailType;
import com.pratilipi.common.type.I18nGroup;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.MailingList;
import com.pratilipi.common.type.NotificationType;
import com.pratilipi.common.type.PageType;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.common.type.ReferenceType;
import com.pratilipi.common.type.VoteParentType;
import com.pratilipi.common.util.AuthorFilter;
import com.pratilipi.common.util.BlogPostFilter;
import com.pratilipi.common.util.PratilipiFilter;
import com.pratilipi.data.mock.AccessTokenMock;
import com.pratilipi.data.mock.AppPropertyMock;
import com.pratilipi.data.mock.AuthorMock;
import com.pratilipi.data.mock.BlogMock;
import com.pratilipi.data.mock.BlogPostMock;
import com.pratilipi.data.mock.EventMock;
import com.pratilipi.data.mock.NotificationMock;
import com.pratilipi.data.mock.PageMock;
import com.pratilipi.data.mock.PratilipiMock;
import com.pratilipi.data.mock.UserMock;
import com.pratilipi.data.mock.UserPratilipiMock;

public class DataAccessorMockImpl implements DataAccessor {
	
	private static final Logger logger =
			Logger.getLogger( DataAccessorMockImpl.class.getName() );
	
	private static final String CURATED_DATA_FOLDER = "curated";

	public PersistenceManager getPersistenceManager() {
		return null;
	}

	
	// Transaction Helpers

	public void beginTx() {
		// Do nothing
	}

	public void commitTx() {
		// Do nothing
	}
	
	public void rollbackTx() {
		// Do nothing
	}

	public boolean isTxActive() {
		return false;
	}
	
	
	// APP_PROPERTY Table
	
	@Override
	public AppProperty newAppProperty( String id ) {
		return new AppPropertyEntity();
	}

	@Override
	public AppProperty getAppProperty( String id ) {
		if( id == null )
			return null;
				
		for( AppProperty appProperty : AppPropertyMock.APP_PROPERTY_TABLE )
			if( appProperty.getId().equals( id ) )
				return appProperty;
		return null;
	}

	@Override
	public AppProperty createOrUpdateAppProperty( AppProperty appProperty ) {
		// TODO: Implementation
		return null;
	}


	// USER Table
	
	@Override
	public User newUser() {
		return new UserEntity();
	}
	
	@Override
	public User getUser( Long id ) {
		if( id == null )
			return null;
		
		for( User user : UserMock.USER_TABLE )
			if( user.getId().equals( id ) )
				return user;
				
		return null;
	}
	
	@Override
	public User getUserByEmail( String email ) {
		if( email == null )
			return null;
		
		for( User user : UserMock.USER_TABLE )
			if( user.getEmail() != null && user.getEmail().equals( email ) )
				return user;
				
		return null;
	}
	
	@Override
	public User getUserByFacebookId( String facebookId ) {
		if( facebookId == null )
			return null;
		
		for( User user : UserMock.USER_TABLE )
			if( user.getFacebookId() != null && user.getFacebookId().equals( facebookId ) )
				return user;
				
		return null;
	}
	
	@Override
	public User getUserByGoogleId( String googleId ) {
		if( googleId == null )
			return null;
		
		for( User user : UserMock.USER_TABLE )
			if( user.getGoogleId() != null && user.getGoogleId().equals( googleId ) )
				return user;
				
		return null;
	}

	@Override
	public Map<Long, User> getUsers( Collection<Long> idList ) {

		Map<Long, User> userMap = new HashMap<Long, User>();

		for( Long id : idList )
			for( User user : UserMock.USER_TABLE )
				if( id == user.getId() )
					userMap.put( id, user );

		return userMap;
	}
	
	@Override
	public List<User> getUserList( List<Long> idList ) {

		List<User> userList = new ArrayList<User>();

		for( Long id : idList )
			for( User user : UserMock.USER_TABLE )
				if( id == user.getId() )
					userList.add( user );

		return userList;
	}
	
	@Override
	public DataListCursorTuple<User> getUserList( String cursor, Integer resultCount ) {
		List<User> dataList = new ArrayList<>( UserMock.USER_TABLE.size() );
		for( User user : UserMock.USER_TABLE )
			dataList.add( user );
		return new DataListCursorTuple<User>( dataList, null );
	}

	@Override
	public User createOrUpdateUser( User user ) {
		boolean newUser = true;
		for( User aUser : UserMock.USER_TABLE )
			if( aUser.getId() == user.getId() )
				newUser = false;
		
		if( newUser ) {
			Long id = 0L;
			for( User aUser : UserMock.USER_TABLE )
				if( aUser.getId() >= id )
					id = aUser.getId() + 1;
			( (UserEntity) user ).setId( id );
			UserMock.USER_TABLE.add( user );
		} else {
			int index = -1;
			for( int i = 0; i < UserMock.USER_TABLE.size(); i++ )
				if( UserMock.USER_TABLE.get( i ).getId() == user.getId() )
					index = i;
			UserMock.USER_TABLE.set( index, user );
		}

		return user;
	}

	@Override
	public User createOrUpdateUser( User user, AuditLog auditLog ) {
		return createOrUpdateUser( user );
	}
	

	// ACCESS_TOKEN Table
	
	@Override
	public AccessToken newAccessToken() {
		return new AccessTokenEntity();
	}

	@Override
	public AccessToken getAccessToken( String accessTokenId ) {
		for( AccessToken accessToken : AccessTokenMock.ACCESS_TOKEN_TABLE )
			if( accessToken.getId().equals( accessTokenId ) )
				return accessToken;
		
		return null;
	}
	
	@Override
	public List<String> getFcmTokenList( Long userId ) {
		// TODO: implementation
		return null;
	}
	
	@Override
	public AccessToken createOrUpdateAccessToken( AccessToken accessToken ) {
		AccessTokenMock.ACCESS_TOKEN_TABLE.add( accessToken );
		return accessToken;
	}

	
	// AUDIT_LOG Table

	@Override
	public AuditLog newAuditLog( AccessToken accessToken, AccessType accessType, Object eventDataOld ) {
		return new AuditLogEntity( accessToken.getUserId(), accessToken.getId(), accessType, eventDataOld );
	}
	
	@Override
	public int deleteExpiredAccessTokenList( Integer count ) {
		// TODO: Implementation
		return 0;
	}

	
	// PAGE Table
	
	@Override
	public Page newPage() {
		return new PageEntity();
	}
	
	@Override
	public Long getPageIdFirst() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Long getPageIdLast() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Page getPage( Long id ) {
		for( Page page : PAGE_TABLE )
			if( page.getId().equals( id ) )
				return page;

		return null;
	}
	
	@Override
	public Page getPage( String uri ) {
		for( Page page : PAGE_TABLE )
			if( uri.equals( page.getUri() ) )
				return page;

		for( Page page : PAGE_TABLE )
			if( uri.equals( page.getUriAlias() ) )
				return page;

		return null;
	}
	
	@Override
	public Page getPage( PageType pageType, Long primaryContentId ) {
		for( Page page : PAGE_TABLE )
			if( page.getType().equals( pageType ) && page.getPrimaryContentId().equals( primaryContentId ) )
				return page;

		return null;
	}
	
	@Override
	public List<Page> getPageList( Long startIndex, Long endIndex ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Page> getPages( List<String> uriList ) {
		Map<String, Page> pageMap = new HashMap<String, Page>();
		for( String uri : uriList )
			if( getPage( uri ) != null )
				pageMap.put( uri, getPage( uri ) );
		return pageMap;
	}
	
	@Override
	public Map<Long, Page> getPages( PageType pageType, List<Long> primaryContentIdList ) {
		Map<Long, Page> pageMap = new HashMap<Long, Page>();
		for( Long primaryContentId : primaryContentIdList )
			for( Page page : PageMock.PAGE_TABLE )
				if( page.getType() == pageType && page.getPrimaryContentId() == primaryContentId )
					pageMap.put( primaryContentId, page );
		return pageMap;
	}

	@Override
	public Page createOrUpdatePage( Page page ) {
		PAGE_TABLE.add( page );
		return page;
	}
	
	
	// PRATILIPI Table & curated/list.<list-name>.<lang>

	@Override
	public Pratilipi newPratilipi() {
		return new PratilipiEntity();
	}

	@Override
	public String getPratilipiListTitle( String listName, Language lang ) {
		String fileName = "list." + lang.getCode() + "." + listName;
		String listTitle = null;
		try {
			InputStream inputStream = DataAccessor.class.getResource( CURATED_DATA_FOLDER + "/" + fileName ).openStream();
			LineIterator it = IOUtils.lineIterator( inputStream, "UTF-8" );
			listTitle = it.nextLine().trim();
			LineIterator.closeQuietly( it );
		} catch( NullPointerException | IOException e ) {
			logger.log( Level.SEVERE, "Exception while reading from " + listName + " .", e );
		}
		return listTitle;
	}
	
	@Override
	public Pratilipi getPratilipi( Long id ) {
		for( Pratilipi pratilipi : PRATILIPI_TABLE )
			if( pratilipi.getId().equals( id ) )
				return pratilipi;
		
		return null;
	}

	@Override
	public List<Pratilipi> getPratilipiList( List<Long> idList ) {
		List<Pratilipi> pratilipiList = new ArrayList<>( idList.size() );
		for( Long id : idList )
			pratilipiList.add( getPratilipi( id ) );
		return pratilipiList;
	}
	
	@Override
	public DataListCursorTuple<Long> getPratilipiIdList(
			PratilipiFilter pratilipiFilter, String cursorStr, Integer offset, Integer resultCount ) {
		
		List<Long> pratilipiIdList = new ArrayList<Long>();
		for( Pratilipi pratilipi : PratilipiMock.PRATILIPI_TABLE ) {
			if( pratilipi.getLanguage() == pratilipiFilter.getLanguage() ) {
				pratilipiIdList.add( pratilipi.getId() );
				pratilipiIdList.add( pratilipi.getId() );
			}
		}
		return new DataListCursorTuple<>( pratilipiIdList, null );

	}
	
	@Override
	public DataListCursorTuple<Pratilipi> getPratilipiList(
			PratilipiFilter pratilipiFilter, String cursorStr, Integer resultCount ) {
		
		List<Pratilipi> dataList = new ArrayList<>(); 
		for( Pratilipi pratilipi : PratilipiMock.PRATILIPI_TABLE )
			dataList.add( pratilipi );
		return new DataListCursorTuple<Pratilipi>( dataList, null );

	}

	@Override
	public List<Long> getPratilipiIdListForStateDeleted( Long authorId ) {
		List<Long> deletedPratilipiIds = new ArrayList<>();
		for( Pratilipi pratilipi : PratilipiMock.PRATILIPI_TABLE )
			if( pratilipi.getState() == PratilipiState.DELETED )
				deletedPratilipiIds.add( pratilipi.getId() );
		return  deletedPratilipiIds;
	}

	@Override
	public Pratilipi createOrUpdatePratilipi( Pratilipi pratilipi, AuditLog auditLog ) {
		
		if( pratilipi.getId() != null )
			return pratilipi;

		long pratilipiId = 0L;
		for( Pratilipi aPratilipi : PRATILIPI_TABLE )
			if( pratilipiId <= aPratilipi.getId() )
				pratilipiId = aPratilipi.getId() + 1;
		
		( ( PratilipiEntity ) pratilipi ).setId( pratilipiId );
		PRATILIPI_TABLE.add( pratilipi );
		
		return pratilipi;
		
	}
	
	@Override
	public Pratilipi createOrUpdatePratilipi( Pratilipi pratilipi, Page page, AuditLog auditLog ) {
		return createOrUpdatePratilipi( pratilipi, auditLog );
	}

	public void deletePage( Page page ) {
		PAGE_TABLE.remove( page );
	}
	
	
	// AUTHOR Table
	
	@Override
	public Author newAuthor() {
		return new AuthorEntity();
	}

	@Override
	public Author getAuthor( Long id ) {
		for( Author author : AUTHOR_TABLE )
			if( author.getId().equals( id ) )
				return author;
		
		return null;
	}
	
	@Override
	public Author getAuthorByUserId( Long userId ) {
		for( Author author : AUTHOR_TABLE )
			if( author.getUserId() != null && author.getUserId().equals( userId ) )
				return author;
		
		return null;
	}
	
	@Override
	public List<Long> getAuthorIdListWithMaxReadCount( Language language, Long minReadCount, Integer resultCount ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataListCursorTuple<Long> getAuthorIdList( AuthorFilter authorFilter,
			String cursor, Integer resultCount ) {
		
		// TODO: Implementation
		return null;
	}

	@Override
	public DataListCursorTuple<Author> getAuthorList(
			AuthorFilter authorFilter, String cursor, Integer resultCount ) {
		
		List<Author> dataList = new ArrayList<>(); 
		for( Author author : AuthorMock.AUTHOR_TABLE )
			dataList.add( author );
		return new DataListCursorTuple<Author>( dataList, null );

	}

	@Override
	public List<Author> getAuthorList( List<Long> idList ) {
		List<Author> authorList = new ArrayList<>( idList.size() );
		for( Long id : idList )
			authorList.add( getAuthor( id ) );
		return authorList;
	}
	
	@Override
	public Author createOrUpdateAuthor( Author author ) {
		
		if( author.getId() != null )
			return author;

		long authorId = 0L;
		for( Author aAuthor : AUTHOR_TABLE )
			if( authorId <= aAuthor.getId() )
				authorId = aAuthor.getId() + 1;
		
		( ( AuthorEntity ) author ).setId( authorId );
		AUTHOR_TABLE.add( author );
		
		return author;
		
	}

	@Override
	public Author createOrUpdateAuthor( Author author, AuditLog auditLog ) {
		return createOrUpdateAuthor( author );
	}

	@Override
	public Author createOrUpdateAuthor( Author author, Page page, AuditLog auditLog ) {
		return createOrUpdateAuthor( author );
	}


	// BLOG Table
	
	@Override
	public Blog newBlog() {
		return new BlogEntity();
	}

	@Override
	public Blog getBlog( Long id ) {
		for( Blog blog : BlogMock.BLOG_TABLE )
			if( id == blog.getId() )
				return blog;
		return null;
	}
	
	@Override
	public Blog createOrUpdateBlog( Blog blog, AuditLog auditLog ) {
		return blog; // TODO: Implementation
	}
	
	
	// BLOG_POST Table
	
	@Override
	public BlogPost newBlogPost() {
		return new BlogPostEntity();
	}

	@Override
	public BlogPost getBlogPost( Long id ) {
		for( BlogPost blogPost : BlogPostMock.BLOGPOST_TABLE )
			if( blogPost.getId().equals( id ) )
				return blogPost;
		return null;
	}
	
	@Override
	public DataListCursorTuple<BlogPost> getBlogPostList( BlogPostFilter blogPostFilter, String cursor, Integer offset, Integer resultCount ) {
//		DataListCursorTuple<BlogPost>
		List<BlogPost> blogPostList = new ArrayList<BlogPost>();
		for( BlogPost blogPost : BlogPostMock.BLOGPOST_TABLE )
			if( blogPost.getBlogId() == blogPostFilter.getBlogId() && 
					blogPost.getLanguage() == blogPostFilter.getLanguage() && 
					blogPostFilter.getState() == BlogPostState.PUBLISHED )
				blogPostList.add( blogPost );
		return new DataListCursorTuple<BlogPost>( blogPostList, "cursor" );
	}
	
	@Override
	public Map<Long, BlogPost> getBlogPosts(Collection<Long> blogPostIdList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlogPost createOrUpdateBlogPost( BlogPost blogPost, AuditLog auditLog ) {
		return blogPost; // TODO: Implementation
	}
	
	@Override
	public BlogPost createOrUpdateBlogPost( BlogPost blogPost, Page page, AuditLog auditLog ) {
		return blogPost; // TODO: Implementation
	}
	
	
	// EVENT Table
	
	@Override
	public Event newEvent() {
		return new EventEntity();
	}

	@Override
	public Event getEvent( Long id ) {
		for( Event event : EventMock.EVENT_TABLE )
			if( event.getId().equals( id ) )
				return event;
		return null;
	}
	
	@Override
	public Event createOrUpdateEvent( Event event, AuditLog auditLog ) {
		if( event.getId() == null ) {
			long id = 1L;
			for( Event e : EventMock.EVENT_TABLE )
				if( id <= e.getId() )
					id = e.getId() + 1;
			( (EventEntity) event ).setId( id );
		}
		return event;
	}
	
	@Override
	public Event createOrUpdateEvent( Event event, Page page, AuditLog auditLog ) {
		return createOrUpdateEvent( event, auditLog );
	}
	
	@Override
	public List<Event> getEventList( Language language ) {
		// TODO: Implementation
		return new ArrayList<>( 0 );
	}
	
	@Override
	public Map<Long, Event> getEvents(Collection<Long> eventIdList) {
		// TODO Auto-generated method stub
		return null;
	}

	
	// USER_PRATILIPI Table
	
	@Override
	public UserPratilipi newUserPratilipi() {
		return new UserPratilipiEntity();
	}
	
	@Override
	public UserPratilipi getUserPratilipi( String userPratilipiId ) {
		return null;
	}
	
	@Override
	public UserPratilipi getUserPratilipi( Long userId, Long pratilipiId ) {
		for( UserPratilipi userPratilipi : UserPratilipiMock.USER_PRATILIPI_TABLE )
			if( userPratilipi.getUserId().equals( userId ) && userPratilipi.getPratilipiId().equals( pratilipiId ) )
				return userPratilipi;
		return null;
	}

	@Override
	public DataListCursorTuple<Long> getUserLibrary(
			Long userId, String cursorStr,
			Integer offset, Integer resultCount ) {

		List<Long> idList = new ArrayList<Long>();
		for( Pratilipi pratilipi : PratilipiMock.PRATILIPI_TABLE )
			idList.add( pratilipi.getId() );
		return new DataListCursorTuple<Long>( idList, "cursor" );
	}
	
	@Override
	public DataListCursorTuple<UserPratilipi> getUserPratilipiList( Long userId,
			Long pratilipiId, String cursorStr, Integer resultCount ) {
		
		// TODO: Implementation
		return null;
	}
	
	@Override
	public DataListCursorTuple<UserPratilipi> getUserPratilipiList( Long userId,
			Long pratilipiId, String cursorStr, Integer resultCount, boolean sortByReviewDate ) {
		
		// TODO: Implementation
		return null;
	}


	@Override
	public Map<String, UserPratilipi> getUserPratilipis(
			Collection<String> userPratilipiIds) {
		// TODO: Implementation
		return null;
	}

	@Override
	public UserPratilipi createOrUpdateUserPratilipi( UserPratilipi userPratilipi, AuditLog auditLog ) {
		for( int i = 0; i < UserPratilipiMock.USER_PRATILIPI_TABLE.size(); i++ ) {
			if( UserPratilipiMock.USER_PRATILIPI_TABLE.get( i ).getUserId().equals( userPratilipi.getUserId() )
					&& UserPratilipiMock.USER_PRATILIPI_TABLE.get( i ).getPratilipiId().equals( userPratilipi.getPratilipiId() ) ) {
				UserPratilipiMock.USER_PRATILIPI_TABLE.remove( i );
				break;
			}
		}

		( (UserPratilipiEntity) userPratilipi ).setId( userPratilipi.getUserId() + "-" + userPratilipi.getPratilipiId() );
		UserPratilipiMock.USER_PRATILIPI_TABLE.add( userPratilipi );
		return userPratilipi;
	}
	

	// USER_AUTHOR Table
	
	@Override
	public UserAuthor newUserAuthor() {
		return new UserAuthorEntity();
	}
	
	@Override
	public UserAuthor getUserAuthor( String userAuthorId ) {
		// TODO: Implementation
		return null;
	}
	
	@Override
	public UserAuthor getUserAuthor( Long userId, Long pratilipiId ) {
		// TODO: Implementation
		return null;
	}
	
	@Override
	public DataListCursorTuple<Long> getUserAuthorFollowList( Long userId, Long authorId, String cursor, Integer offset, Integer resultCount ) {
		// TODO: Implementation
		return new DataListCursorTuple<Long>( new ArrayList<Long>(), cursor );
	}
	
	@Override
	public DataListCursorTuple<UserAuthor> getUserAuthorList( Long userId, Long authorId, String cursorStr, Integer offset, Integer resultCount ) {
		// TODO: Implementation
		return null;
	}

	@Override
	public DataListIterator<UserAuthor> getUserAuthorListIterator( Long userId, Long authorId, String cursor, Integer offset, Integer resultCount ) {
		// TODO: Implementation
		return null;
	}
	
	@Override
	public UserAuthor createOrUpdateUserAuthor( UserAuthor userAuthor, AuditLog auditLog ) {
		// TODO: Implementation
		return userAuthor;
	}

	
	// curated/home.<lang>
	
	@Override
	public List<String> getHomeSectionList( Language language ) { 
		List<String> sectionList = new LinkedList<>();

		try {
			String fileName = CURATED_DATA_FOLDER + "/home." + language.getCode();
			InputStream inputStream = DataAccessor.class.getResource( fileName ).openStream();
			for( String listName : IOUtils.readLines( inputStream, "UTF-8" ) ) {
				listName = listName.trim();
				if( ! listName.isEmpty() )
					sectionList.add( listName );
			}
			inputStream.close();
		} catch( NullPointerException | IOException e ) {
			logger.log( Level.SEVERE, "Exception while reading from home." + language.getNameEn(), e );
		}
		
		return sectionList;
	}

	// curated/personalized.home.<lang>
	@Override
	public List<String> getPersonalizedHomeWidgetList(Language language) {
		List<String> sectionList = new LinkedList<>();

		try {
			String fileName = CURATED_DATA_FOLDER + "/personalized.home." + language.getCode();
			InputStream inputStream = DataAccessor.class.getResource( fileName ).openStream();
			for( String listName : IOUtils.readLines( inputStream, "UTF-8" ) ) {
				listName = listName.trim();
				if( ! listName.isEmpty() )
					sectionList.add( listName );
			}
			inputStream.close();
		} catch( NullPointerException | IOException e ) {
			logger.log( Level.SEVERE, "Exception while reading from personalized.home." + language.getNameEn(), e );
		}

		return sectionList;
	}
	
	// NAVIGATION Table
	
	@Override
	public List<Navigation> getNavigationList( Language language ) {
				
		ArrayList<Navigation> navigationList = null;

		List<String> lines = null;
		try {
			String fileName = CURATED_DATA_FOLDER + "/navigation." + language.getCode();
			InputStream inputStream = DataAccessor.class.getResource( fileName ).openStream();
			lines = IOUtils.readLines( inputStream, "UTF-8" );
			inputStream.close();
		} catch( NullPointerException | IOException e ) {
			logger.log( Level.SEVERE, "Failed to fetch " + language.getNameEn() + " navigation list.", e );
			lines = new ArrayList<>( 0 );
		}


		navigationList = new ArrayList<>( lines.size() );
		
		Navigation navigation = null;
		for( String line : lines ) {
			
			line = line.trim();
			
			if( navigation == null && line.isEmpty() )
				continue;
			
			else if( navigation == null && ! line.isEmpty() )
				navigation = new NavigationEntity( line );
			
			else if( navigation != null && line.isEmpty() ) {
				navigationList.add( navigation );
				navigation = null;
			
			} else if( navigation != null && ! line.isEmpty() ) {
				String url = null;
				String title = null;
				String categoryName = null;
				String imageName = null;
				String apiName = null;
				String apiRequest = null;

				if( line.contains( "App#" ) ) {
					String appInfo = line.substring( line.indexOf( "App#" ) ).trim();
					imageName = appInfo.substring( "App#imageName::".length(), appInfo.lastIndexOf( "App#" ) ).trim();
					apiName = appInfo.substring( appInfo.lastIndexOf( "App#" ) + 4, appInfo.lastIndexOf( "::" ) ) + "Api";
					apiRequest = line.substring( line.indexOf( '{' ), line.indexOf( '}' ) + 1 );
					line = line.substring( 0, line.indexOf( "App#" ) ).trim();
				}
				if( line.contains( "Analytics#" ) ) {
					String analyticsInfo = line.substring( line.indexOf( "Analytics#" ) ).trim();
					categoryName = analyticsInfo.substring( "Analytics#categoryName::".length() ).trim();
					line = line.substring( 0, line.indexOf( "Analytics#" ) ).trim();
				}
				if( line.indexOf( ' ' ) != -1 ) {
					url = line.substring( 0, line.indexOf( ' ' ) );
					title = line.substring( line.indexOf( ' ' ) + 1 ).trim();
				}

				navigation.addLink( new Navigation.Link( title, url, apiName, categoryName, apiRequest, imageName ) );
			}
			
		}

		if( navigation != null )
			navigationList.add( navigation );
		
		return navigationList;
	}

	
	// COMMENT Table
	
	@Override
	public Comment newComment() {
		return new CommentEntity();
	}
	
	@Override
	public Comment getComment( Long commentId ) {
		return null;
	}
	
	@Override
	public Map<Long, Comment> getComments( Collection<Long> commentIds ) {
		return null;
	}
	
	@Override
	public List<Comment> getCommentList( CommentParentType parentType, Long parentId ) {
		return new ArrayList<>( 0 );
	}
	
	@Override
	public List<Comment> getCommentList( CommentParentType parentType, String parentId ) {
		return new ArrayList<>( 0 );
	}
	
	@Override
	public List<Comment> getCommentListByReference( ReferenceType referenceType, Long referenceId ) {
		return getCommentListByReference( referenceType, referenceId.toString() );
	}
	
	@Override
	public List<Comment> getCommentListByReference( ReferenceType referenceType, String referenceId ) {
		return new ArrayList<>( 0 );
	}

	@Override
	public Comment createOrUpdateComment( Comment comment, AuditLog auditLog ) {
		return comment;
	}
	
	
	// USER_VOTE Table

	@Override
	public Vote newVote() {
		return new VoteEntity();
	}
	
	@Override
	public Vote getVote( String voteId ) {
		return null;
	}
	
	@Override
	public Vote getVote( Long userId, VoteParentType parentType, String parentId ) {
		return null;
	}
	
	@Override
	public Map<String, Vote> getVotes( Collection<String> voteIds ) {
		return null;
	}
	
	@Override
	public List<Vote> getVoteListByReference( ReferenceType referenceType, Long referenceId ) {
		return new ArrayList<>( 0 );
	}
	
	@Override
	public List<Vote> getVoteListByReference( ReferenceType referenceType, String referenceId ) {
		return new ArrayList<>( 0 );
	}

	@Override
	public Vote createOrUpdateVote( Vote vote, AuditLog auditLog ) {
		return vote; 
	}
	
	
	// CONVERSATION* Tables
	
	@Override
	public Conversation newConversation( ContactTeam team, Long userId ) {
		return new ConversationEntity( team + "-" + userId );
	}
	
	@Override
	public Conversation newConversation( ContactTeam team, String email ) {
		return new ConversationEntity( team + "-" + email );
	}
	
	@Override
	public Conversation getConversation( ContactTeam team, Long userId ) {
		return null;
	}
	
	@Override
	public Conversation getConversation( ContactTeam team, String email ) {
		return null;
	}

	
	@Override
	public ConversationUser newConversationUser( String conversationId, Long userid ) {
		return new ConversationUserEntity( conversationId, userid );
	}
	
	
	@Override
	public ConversationMessage newConversationMessage() {
		return new ConversationMessageEntity();
	}
	
	@Override
	public Conversation createOrUpdateConversation( Conversation conversation, List<ConversationUser> conversationUserList ) {
		return conversation;
	}
	
	@Override
	public Conversation createOrUpdateConversation( Conversation conversation, ConversationMessage conversationMessage ) {
		return conversation;
	}
	
	
	// MAILING_LIST_SUBSCRIPTION Table
	
	@Override
	public MailingListSubscription newMailingListSubscription() {
		return new MailingListSubscriptionEntity();
	}
	
	@Override
	public List<MailingListSubscription> getMailingListSubscriptionList( MailingList mailingList, String email, String phone ) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public MailingListSubscription createOrUpdateMailingListSubscription( MailingListSubscription mailingListSubscription, AuditLog auditLog ) {
		return mailingListSubscription;
	}


	@Override
	public DataListCursorTuple<AuditLog> getAuditLogList( Date minCreationDate, String cursor,
			Integer resultCount) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<Long, Author> getAuthorsByUserIds( Collection<Long> userIdList ) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<UserPratilipi> getUserPratilipiList(Long userId,
			List<Long> pratilipiIdList) {
		// TODO Auto-generated method stub
		return new ArrayList<UserPratilipi>();
	}


	@Override
	public List<UserAuthor> getUserAuthorList(Long userId,
			List<Long> authorIdList) {
		// TODO Auto-generated method stub
		return new ArrayList<UserAuthor>();
	}


	@Override
	public List<UserAuthor> getUserAuthorList(List<Long> userIdList,
			Long authorId) {
		// TODO Auto-generated method stub
		return new ArrayList<UserAuthor>(); 
	}


	@Override
	public List<String> getRecommendSectionList(Language language) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Notification newNotification( Long userId, NotificationType type, Long sourceId ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Notification newNotification( Long userId, NotificationType type, String sourceId, String createdBy ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Notification getNotification( Long notificationId ) {
		for( Notification notification : NotificationMock.NOTIFICATION_TABLE )
			if( notification.getId() == notificationId )
				return notification;
		return null;
	}
	
	@Override
	public Notification getNotification( Long userId, NotificationType type, Long sourceId ) {
		for( Notification notification : NotificationMock.NOTIFICATION_TABLE )
			if( notification.getUserId() == userId && notification.getType() == type && notification.getSourceIdLong() == sourceId )
				return notification;
		return null;
	}
	
	@Override
	public Notification getNotification( Long userId, NotificationType type, String sourceId, String createdBy ) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getNotificationCout( String createdBy ) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Map<Long, Notification> getNotifications( Collection<Long> notificationIdList ) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Notification> getNotificationListWithFcmPending( Integer resultCount ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataListIterator<Notification> getNotificationListIterator( Long userId, NotificationType type, Long sourceId, String cursor, Integer resultCount ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataListIterator<Notification> getNotificationListIterator( Long userId, NotificationType type, String sourceId, String cursor, Integer resultCount ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Notification createOrUpdateNotification(Notification notification) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Notification> createOrUpdateNotificationList( List<Notification> notificationList ) {
		// TODO Auto-generated method stub
		return null;
	}


	// EMAIL Table
	
	@Override
	public Email newEmail(Long userId, EmailType type, Long primaryContentId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Email newEmail(Long userId, EmailType type, String primaryContentId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Email getEmail(Long userId, EmailType type, Long primaryContentId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Email getEmail(Long userId, EmailType type, String primaryContentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Email getEmail(Long emailId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Email createOrUpdateEmail(Email email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Email> getEmailList( Long userId, EmailType type, Long primaryContentId, EmailState state, Integer resultCount ) {
		return getEmailList( userId, type, primaryContentId.toString(), state, resultCount );
	}

	@Override
	public List<Email> getEmailList(Long userId, EmailType type, String primaryContentId, EmailState state, Integer resultCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Email> createOrUpdateEmailList(List<Email> emailList) {
		// TODO Auto-generated method stub
		return null;
	}

	
	// I18N Table
	
	@Override
	public I18n newI18n( String i18nId ) {
		return new I18nEntity( i18nId );
	}
	
	@Override
	public I18n getI18n( String i18nId ) {
		return null;
	}

	@Override
	public List<I18n> getI18nList(I18nGroup i18nGroup) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map<String, String> getI18nStrings( I18nGroup i18nGroup, Language language ) {
		Map<String, String> i18nStrings = new HashMap<>();
		i18nStrings.put( "notification_and", "notification and" );
		i18nStrings.put( "notification_has_published", "notification has published" );
		i18nStrings.put( "notification_has_followed", "notification has followed" );
		i18nStrings.put( "notification_have_followed", "notification have followed" );
		i18nStrings.put( "notification_others_have_followed", "notification others have followed" );
		return i18nStrings;
	}
	
	@Override
	public I18n createOrUpdateI18n( I18n i18n ) {
		// TODO Auto-generated method stub
		return i18n;
	}
	
	@Override
	public List<I18n> createOrUpdateI18nList( List<I18n> i18nList ) {
		return null;
	}

	
	// BatchProcess Table
	
	public BatchProcess newBatchProcess() {
		return new BatchProcessEntity();
	}
	
	public BatchProcess getBatchProcess( Long batchProcessId ) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<BatchProcess> getAllBatchProcessList() {
		// TODO Auto-generated method stub
		return new ArrayList<BatchProcess>( 0 );
	}
	
	public List<BatchProcess> getIncompleteBatchProcessList() {
		// TODO Auto-generated method stub
		return new ArrayList<BatchProcess>( 0 );
	}
	
	public BatchProcess createOrUpdateBatchProcess( BatchProcess batchProcess ) {
		// TODO Auto-generated method stub
		return batchProcess;
	}

	@Override
	public DataListCursorTuple<BatchProcess> getBatchProcessList(
			BatchProcessType type, BatchProcessState stateCompleted,
			BatchProcessState stateInProgress, String cursor,
			Integer resultCount) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<Long, Pratilipi> getPratilipis(Collection<Long> pratilipiIds) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<Long, Author> getAuthors(Collection<Long> authorIds) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<String, UserAuthor> getUserAuthors(Collection<String> userAuthoriIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEmailCount( Long userId, EmailType type, EmailState state, Integer resultCount ) {
		return 0;
	}

	@Override
	public DataListIterator<Email> getEmailListIteratorForStatePending( Long userId, boolean scheduled ) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Tag newTag() {
		return null;
	}
	
	@Override
	public void deleteTags(List<Long> ids) {} 
	
	@Override
	public Tag createOrUpdateTag(Tag tag) {
		return null;
	}
	
	@Override
	public Tag getTag(Long tagId) {
		return null;
	}
	
	@Override
	public List<Tag> getTagList(Language language, PratilipiType type) {
		return null;
	}

	@Override
	public AuthorByReadCount newAuthorByReadCount() {
		return null;
	}

	@Override
	public AuthorByReadCount getAuthorByReadCountById(String id) {
		return null;
	}

	@Override
	public AuthorByReadCount createOrUpdateAuthorByReadCount(AuthorByReadCount authorByReadCount, Date date) {
		return null;
	}

	@Override
	public void deleteAuthorByReadCount(List<String> ids) {

	}

	@Override
	public DataListCursorTuple<AuthorByReadCount> getAuthorsByReadCount(Date date, Language language, Integer resultCount, String cursorStr) {
		return null;
	}
}
