package com.pratilipi.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.BlogPostData;
import com.pratilipi.data.client.EventData;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.util.AuthorDataUtil;
import com.pratilipi.data.util.BlogPostDataUtil;
import com.pratilipi.data.util.EventDataUtil;
import com.pratilipi.data.util.PratilipiDataUtil;
import com.pratilipi.i18n.I18n;
import com.pratilipi.site.PratilipiSite;

public class SEOTitleUtil {

	private static String _getPageTitle( String i18nId, Map<String,String> dataModel, Language language ) 
			throws UnexpectedServerException {

		if( language == null ) // www.gamma.pratilipi.com
			language = Language.ENGLISH;

		String contentTitle = FreeMarkerUtil.processString( dataModel, I18n.getString( i18nId, language ) ).trim();
		String contentTitleEn = FreeMarkerUtil.processString( dataModel, I18n.getString( i18nId, Language.ENGLISH ) ).trim();

		return ( contentTitle != null && ! contentTitle.isEmpty() ?  contentTitle + " « " : "" ) + I18n.getString( "pratilipi", language )  + " " + language.getName()
				+ " | "
				+ ( contentTitleEn != null && ! contentTitleEn.isEmpty() ? contentTitleEn + " « " : "" ) + I18n.getString( "pratilipi", Language.ENGLISH ) + " " + language.getNameEn();

	}

	// TYPE: PRATILIPI
	private static Map<String, String> _getDataModelForPratilipiData( PratilipiData pratilipi ) {
		Map<String,String> dataModel = new HashMap<>();
		dataModel.put( "authorName", pratilipi.getAuthor().getName() != null ? pratilipi.getAuthor().getName() : pratilipi.getAuthor().getNameEn() );
		dataModel.put( "authorNameEn", pratilipi.getAuthor().getNameEn() != null ? pratilipi.getAuthor().getNameEn() : pratilipi.getAuthor().getName() );
		dataModel.put( "pratilipiTitle", pratilipi.getTitle() );
		dataModel.put( "pratilipiTitleEn", pratilipi.getTitleEn() != null ? pratilipi.getTitleEn() : pratilipi.getTitle() );
		dataModel.put( "language", pratilipi.getLanguage().getNameEn() );
		return dataModel;
	}

	public static String getPratilipiPageTitle( Long pratilipiId, Language language ) 
			throws UnexpectedServerException {

		return getPratilipiPageTitle( 
				PratilipiDataUtil.createPratilipiData( DataAccessorFactory.getDataAccessor().getPratilipi( pratilipiId ) ),
				language );
	}	

	public static String getPratilipiPageTitle( PratilipiData pratilipi, Language language ) 
			throws UnexpectedServerException {

		return _getPageTitle( "seo_pratilipi_page", _getDataModelForPratilipiData( pratilipi ), language );
	}

	public static String getReadPageTitle( Long pratilipiId, Language language ) 
			throws UnexpectedServerException {

		return getReadPageTitle( 
				PratilipiDataUtil.createPratilipiData( DataAccessorFactory.getDataAccessor().getPratilipi( pratilipiId ) ),
				language );
	}	

	public static String getReadPageTitle( PratilipiData pratilipi, Language language ) 
			throws UnexpectedServerException {

		return _getPageTitle( "seo_reader_panel", _getDataModelForPratilipiData( pratilipi ), language );
	}
	
	public static String getWritePageTitle( Long pratilipiId, Language language ) 
			throws UnexpectedServerException {

		return getWritePageTitle( 
				PratilipiDataUtil.createPratilipiData( DataAccessorFactory.getDataAccessor().getPratilipi( pratilipiId ) ),
				language );
	}	

	public static String getWritePageTitle( PratilipiData pratilipi, Language language ) 
			throws UnexpectedServerException {

		return _getPageTitle( "seo_writer_panel", _getDataModelForPratilipiData( pratilipi ), language );
	}



	// TYPE: AUTHOR
	public static String getAuthorPageTitle( Long authorId, Language language ) 
			throws UnexpectedServerException {

		return getAuthorPageTitle( 
				AuthorDataUtil.createAuthorData( DataAccessorFactory.getDataAccessor().getAuthor( authorId ) ),
				language );

	}

	public static String getAuthorPageTitle( AuthorData author, Language language ) 
			throws UnexpectedServerException {

		Map<String,String> dataModel = new HashMap<>();
		dataModel.put( "authorName", author.getName() != null ? author.getName() : author.getNameEn() );
		dataModel.put( "authorNameEn", author.getNameEn() != null ? author.getNameEn() : author.getName() );
		return _getPageTitle( "seo_author_page", dataModel, language );
	}



	// TYPE: EVENT
	public static String getEventsPageTitle( Language language ) 
			throws UnexpectedServerException {

		return _getPageTitle( "seo_events_page", null, language );
	}

	public static String getEventPageTitle( Long eventId, Language language )
			throws UnexpectedServerException {

		return getEventPageTitle(
				EventDataUtil.createEventData( DataAccessorFactory.getDataAccessor().getEvent( eventId ), false ),
				language );

	}

	public static String getEventPageTitle( EventData event, Language language )
			throws UnexpectedServerException {

		Map<String,String> dataModel = new HashMap<>();
		dataModel.put( "eventName", event.getName() );
		dataModel.put( "eventNameEn", event.getNameEn() != null ? event.getNameEn() : event.getName() );
		return _getPageTitle( "seo_event_page", dataModel, language );
	}



	// TYPE: HOME
	public static String getMasterHomePageTitle() {
		return I18n.getString( "seo_root_domain", Language.ENGLISH );
	}

	public static String getHomePageTitle( Language language ) 
			throws UnexpectedServerException {

		Map<String,String> dataModel = new HashMap<>();
		dataModel.put( "language", language.getNameEn() );
		return _getPageTitle( "seo_home_page", dataModel, language );
	}



	// TYPE: BLOG
	public static String getBlogPageTitle( Language language ) 
			throws UnexpectedServerException {

		return _getPageTitle( "seo_blog_page", null, language );
	}
	
	public static String getAuthorInterviewPageTitle( Language language ) 
			throws UnexpectedServerException {

		return _getPageTitle( "seo_author_interview", null, language );
	}
	
	public static String getBlogPostPageTitle( Long blogPostId, Language language ) 
			throws UnexpectedServerException {

		return getBlogPostPageTitle( 
				BlogPostDataUtil.createBlogPostData( DataAccessorFactory.getDataAccessor().getBlogPost( blogPostId ) ),
				language );
	}

	public static String getBlogPostPageTitle( BlogPostData blog, Language language ) 
			throws UnexpectedServerException {

		Map<String,String> dataModel = new HashMap<>();
		dataModel.put( "blogTitle", blog.getTitle() );
		dataModel.put( "blogTitleEn", blog.getTitleEn() != null ? blog.getTitleEn() : blog.getTitle() );
		return _getPageTitle( "seo_blogpost_page", dataModel, language );
	}



	// TYPE: LIBRARY
	public static String getLibraryPageTitle( Language language ) 
			throws UnexpectedServerException {

		return _getPageTitle( "seo_library_page", null, language );
	}



	// TYPE: SEARCH
	public static String getSearchPageTitle( Language language ) 
			throws UnexpectedServerException {

		return _getPageTitle( "seo_search_page", null, language );
	}



	// TYPE: NOTIFICATIONS
	public static String getNotificationsPageTitle( Language language ) 
			throws UnexpectedServerException {

		return _getPageTitle( "seo_notifications_page", null, language );
	}



	// TYPE: CATEGORY_LIST
	public static String getListPageTitle( String listName, Language language ) 
			throws UnexpectedServerException {

		String listTitle = null;

		try {
			String fileName = "list." + language.getCode() + "." + listName;
			InputStream inputStream = DataAccessor.class.getResource( "curated/" + fileName ).openStream();
			LineIterator it = IOUtils.lineIterator( inputStream, "UTF-8" );
			listTitle = it.nextLine().trim();
			LineIterator.closeQuietly( it );
		} catch( NullPointerException | IOException e ) {
			throw new UnexpectedServerException();
		}

		Map<String, String> dataModel = new HashMap<>();
		
		if( listTitle.contains( "|" ) ) {
			dataModel.put( "listTitle", listTitle.substring( 0, listTitle.indexOf( "|" ) ).trim() );
			dataModel.put( "listTitleEn", listTitle.substring( listTitle.indexOf( "|" ) + 1 ).trim() );
		} else {
			dataModel.put( "listTitle", listTitle );
			dataModel.put( "listTitleEn", "" );
		}

		return _getPageTitle( "seo_list_page", dataModel, language );  
	}



	// TYPE: STATIC
	private static Map<String, String> _getStaticPageDataModel( String fileName, Language language ) {

		try {

			InputStream inputStream = PratilipiSite.class.getResource( PratilipiSite.dataFilePrefix + "static." + language.getCode() + "." + fileName ).openStream();
			LineIterator it = IOUtils.lineIterator( inputStream, "UTF-8" );
			String listTitle = it.nextLine().trim();
			LineIterator.closeQuietly( it );

			Map<String, String> dataModel = new HashMap<>();
			if( language == Language.ENGLISH ) {
				dataModel.put( "staticTitle", "" );
				dataModel.put( "staticTitleEn", listTitle );
			} else {
				dataModel.put( "staticTitle", listTitle );
				dataModel.put( "staticTitleEn", "" );
			}

			return dataModel;

		} catch( NullPointerException | IOException e ) {
			return null;
		}

	}

	public static String getStaticPageTitle( String pageName, Language language ) 
			throws UnexpectedServerException {

		Map<String, String> dataModel = _getStaticPageDataModel( pageName, language );
		if( dataModel == null )
			dataModel = _getStaticPageDataModel( pageName, Language.ENGLISH );

		return _getPageTitle( "seo_static_page", dataModel, language );

	}



	// TYPE: FOLLOW
	public static String getFollowersPageTitle( Long userId, Language language ) 
			throws UnexpectedServerException {

		return getFollowersPageTitle(
				AuthorDataUtil.createAuthorData( DataAccessorFactory.getDataAccessor().getAuthorByUserId( userId ) ),
				language );
	}

	public static String getFollowersPageTitle( AuthorData author, Language language ) 
			throws UnexpectedServerException {

		Map<String,String> dataModel = new HashMap<>();
		dataModel.put( "authorName", author.getName() != null ? author.getName() : author.getNameEn() );
		dataModel.put( "authorNameEn", author.getNameEn() != null ? author.getNameEn() : author.getName() );
		return _getPageTitle( "seo_followers_page", dataModel, language );
	}
	
	public static String getFollowingPageTitle( Long authorId, Language language ) 
			throws UnexpectedServerException {

		return getFollowingPageTitle(
				AuthorDataUtil.createAuthorData( DataAccessorFactory.getDataAccessor().getAuthor( authorId ) ),
				language );
	}

	public static String getFollowingPageTitle( AuthorData author, Language language ) 
			throws UnexpectedServerException {

		Map<String,String> dataModel = new HashMap<>();
		dataModel.put( "authorName", author.getName() != null ? author.getName() : author.getNameEn() );
		dataModel.put( "authorNameEn", author.getNameEn() != null ? author.getNameEn() : author.getName() );
		return _getPageTitle( "seo_following_page", dataModel, language );

	}

}
