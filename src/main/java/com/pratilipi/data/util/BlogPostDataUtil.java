package com.pratilipi.data.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.BlogPostState;
import com.pratilipi.common.type.PageType;
import com.pratilipi.common.util.BlogPostFilter;
import com.pratilipi.common.util.UriAliasUtil;
import com.pratilipi.common.util.UserAccessUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.BlogPostData;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.type.AuditLog;
import com.pratilipi.data.type.BlogPost;
import com.pratilipi.data.type.Page;
import com.pratilipi.data.type.User;
import com.pratilipi.filter.AccessTokenFilter;


public class BlogPostDataUtil {
	
	private static final Logger logger =
			Logger.getLogger( BlogPostDataUtil.class.getName() );


	public static boolean hasAccessToListBlogPostData( BlogPostFilter blogPostFilter ) {
		
		// Case 1: User can list PUBLISHED BlogPosts.
		if( blogPostFilter.getState() == BlogPostState.PUBLISHED )
			return true;

		
		// Case 2: User with BLOG_POST_LIST access can list BlogPosts in any State.
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), blogPostFilter.getLanguage(), AccessType.BLOG_POST_LIST ) )
			return true;

		
		return false;
		
	}

	public static boolean hasAccessToAddBlogPostData( BlogPostData blogPostData ) {
		
		// User with BLOG_POST_ADD access can add a Blog Post.
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), blogPostData.getLanguage(), AccessType.BLOG_POST_ADD ) )
			return true;
		
		return false;
		
	}

	public static boolean hasAccessToUpdateBlogPostData( BlogPost blogPost, BlogPostData blogPostData ) {

		// User with BLOG_POST_UPDATE access can update any Blog Post.
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), blogPost.getLanguage(), AccessType.BLOG_POST_UPDATE ) ) {
			if( blogPostData == null || ! blogPostData.hasLanguage() || blogPostData.getLanguage() == blogPost.getLanguage() )
				return true;
			else if( UserAccessUtil.hasUserAccess( accessToken.getUserId(), blogPostData.getLanguage(), AccessType.BLOG_POST_UPDATE ) )
				return true;
		}
		
		return false;
		
	}
	
	
	public static BlogPostData createBlogPostData( BlogPost blogPost ) {
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Page blogPostPage = dataAccessor.getPage( PageType.BLOG_POST, blogPost.getId() );
		return createBlogPostData( blogPost, blogPostPage );
	}
	
	public static BlogPostData createBlogPostData( BlogPost blogPost, Page blogPostPage ) {
		BlogPostData blogPostData = new BlogPostData( blogPost.getId() );
		blogPostData.setBlogId( blogPost.getBlogId() );
		blogPostData.setTitle( blogPost.getTitle() );
		blogPostData.setTitleEn( blogPost.getTitleEn() );
		blogPostData.setContent( blogPost.getContent() );
		blogPostData.setLanguage( blogPost.getLanguage() );
		blogPostData.setState( blogPost.getState() );
		
		User user = DataAccessorFactory.getDataAccessor().getUser( blogPost.getCreatedBy() );
		if( user != null ) {
			UserData userData = UserDataUtil.createUserData( user );
			blogPostData.setCreatedBy( userData );
		}
		
		blogPostData.setCreationDate( blogPost.getCreationDate() );
		blogPostData.setLastUpdated( blogPost.getLastUpdated() );
		blogPostData.setPageUrl( blogPostPage.getUriAlias() == null ? blogPostPage.getUri() : blogPostPage.getUriAlias() );
		blogPostData.setAccessToUpdate( hasAccessToUpdateBlogPostData( blogPost, null ) );
		return blogPostData;
	}
	
	public static List<BlogPostData> createBlogPostDataList( List<BlogPost> blogPostList ) {
		List<Long> blogIdList = new ArrayList<>();
		for( BlogPost blogPost : blogPostList )
			blogIdList.add( blogPost.getId() );
	
		Map<Long, Page> blogPostPages = DataAccessorFactory.getDataAccessor()
				.getPages( PageType.BLOG_POST, blogIdList );
		
		List<BlogPostData> blogPostDataList = new ArrayList<>();
		for( BlogPost blogPost : blogPostList )
			blogPostDataList.add( createBlogPostData( blogPost, blogPostPages.get( blogPost.getId() ) ) );
		
		return blogPostDataList;
	}
	
	
	public static DataListCursorTuple<BlogPostData> getBlogPostDataList(
			BlogPostFilter blogPostFilter, String cursor, Integer offset, Integer resultCount ) {
		
		DataListCursorTuple<BlogPost> blogPostListCursorTuple = DataAccessorFactory
				.getDataAccessor()
				.getBlogPostList( blogPostFilter, cursor, offset, resultCount );
		
		return new DataListCursorTuple<BlogPostData>(
				createBlogPostDataList( blogPostListCursorTuple.getDataList() ),
				blogPostListCursorTuple.getCursor() );
	
	}
	
	
	public static BlogPostData saveBlogPostData( BlogPostData blogPostData )
			throws InvalidArgumentException, InsufficientAccessException {
		
		_validateBlogPostDataForSave( blogPostData );
		
		boolean isNew = blogPostData.getId() == null;
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		BlogPost blogPost = isNew ? dataAccessor.newBlogPost() : dataAccessor.getBlogPost( blogPostData.getId() );
		
		if ( isNew && ! hasAccessToAddBlogPostData( blogPostData ) )
			throw new InsufficientAccessException();
		if( ! isNew && ! hasAccessToUpdateBlogPostData( blogPost, blogPostData ) )
			throw new InsufficientAccessException();
		

		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				isNew ? AccessType.BLOG_POST_ADD : AccessType.BLOG_POST_UPDATE,
				blogPost );
		
		
		if( isNew && blogPostData.hasBlogId() ) // Changing blog id is not allowed
			blogPost.setBlogId( blogPostData.getBlogId() );
		if( blogPostData.hasTitle() )
			blogPost.setTitle( blogPostData.getTitle() );
		if( blogPostData.hasTitleEn() )
			blogPost.setTitleEn( blogPostData.getTitleEn() );
		if( blogPostData.hasContent() )
			blogPost.setContent( blogPostData.getContent() );
		if( blogPostData.hasLanguage() )
			blogPost.setLanguage( blogPostData.getLanguage() );
		if( blogPostData.hasState() )
			blogPost.setState( blogPostData.getState() );
		if( isNew ) {
			blogPost.setCreatedBy( AccessTokenFilter.getAccessToken().getUserId() );
			blogPost.setCreationDate( new Date() );
		} else if( blogPost.getCreatedBy() == null ) {
			blogPost.setCreatedBy( AccessTokenFilter.getAccessToken().getUserId() );
		}
		blogPost.setLastUpdated( new Date() );
		
		
		blogPost = dataAccessor.createOrUpdateBlogPost( blogPost, auditLog );
		
		_updateBlogPostPageUrl( blogPost );

		return createBlogPostData( blogPost );
		
	}
	
	private static void _validateBlogPostDataForSave( BlogPostData blogPostData )
			throws InvalidArgumentException {
		
		boolean isNew = blogPostData.getId() == null;
		
		JsonObject errorMessages = new JsonObject();

		// New blogPost must have blog id.
		if( isNew && ( ! blogPostData.hasBlogId() || blogPostData.getBlogId() == null ) )
			errorMessages.addProperty( "blogId", GenericRequest.ERR_BLOG_ID_REQUIRED );
		// Blog id can not be un-set or set to null.
		else if( ! isNew && blogPostData.hasBlogId() && blogPostData.getBlogId() == null )
			errorMessages.addProperty( "blogId", GenericRequest.ERR_BLOG_ID_REQUIRED );

		// New blogPost must have language.
		if( isNew && ( ! blogPostData.hasLanguage() || blogPostData.getLanguage() == null ) )
			errorMessages.addProperty( "language", GenericRequest.ERR_LANGUAGE_REQUIRED );
		// Language can not be un-set or set to null.
		else if( ! isNew && blogPostData.hasLanguage() && blogPostData.getLanguage() == null )
			errorMessages.addProperty( "language", GenericRequest.ERR_LANGUAGE_REQUIRED );

		// New blogPost must have state.
		if( isNew && ( ! blogPostData.hasState() || blogPostData.getState() == null ) )
			errorMessages.addProperty( "state", GenericRequest.ERR_BLOG_POST_STATE_REQUIRED );

		if( errorMessages.entrySet().size() > 0 )
			throw new InvalidArgumentException( errorMessages );

	}
	
	
	private static void _updateBlogPostPageUrl( BlogPost blogPost ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Page blogPage = dataAccessor.getPage( PageType.BLOG, blogPost.getBlogId() );
		Page blogPostPage = dataAccessor.getPage( PageType.BLOG_POST, blogPost.getId() );

		if( blogPost.getState() == BlogPostState.DELETED ) {
			if( blogPostPage != null )
				dataAccessor.deletePage( blogPostPage );
			return;
		}
		
		boolean isNew = blogPostPage == null;
		
		if( isNew ) {
			blogPostPage = dataAccessor.newPage();
			blogPostPage.setType( PageType.BLOG_POST );
			blogPostPage.setUri( PageType.BLOG_POST.getUrlPrefix() + blogPost.getId() );
			blogPostPage.setPrimaryContentId( blogPost.getId() );
			blogPostPage.setCreationDate( new Date() );
		}
		
		String uriAlias = UriAliasUtil.generateUriAlias(
				blogPostPage.getUriAlias(),
				blogPage.getUriAlias() + "/",
				blogPost.getTitleEn() == null ? blogPost.getTitle() : blogPost.getTitleEn() );
		
		if( isNew && uriAlias == null ) {
			// Do Not Return
		} else if( uriAlias == blogPostPage.getUriAlias()
				|| ( uriAlias != null && uriAlias.equals( blogPostPage.getUriAlias() ) )
				|| ( blogPostPage.getUriAlias() != null && blogPostPage.getUriAlias().equals( uriAlias ) ) ) {
			// Do Nothing.
			return;
		} else {
			logger.log( Level.INFO, "Updating Event Page Url: '" + blogPostPage.getUriAlias() + "' -> '" + uriAlias + "'" );
			blogPostPage.setUriAlias( uriAlias );
		}
		
		blogPostPage = dataAccessor.createOrUpdatePage( blogPostPage );
	
	}

	
}
