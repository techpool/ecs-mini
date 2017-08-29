package com.pratilipi.data.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AuthorState;
import com.pratilipi.common.type.BlogPostState;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.RequestParameter;
import com.pratilipi.common.type.Website;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.BlogPost;
import com.pratilipi.data.type.Event;
import com.pratilipi.data.type.Page;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.site.PratilipiSite;


public class PageDataUtil {

	private static final Logger logger = 
			Logger.getLogger( PageDataUtil.class.getName() );

	private static final String LINE_SEPARATOR = "\n";
	private static final Long SITEMAP_PAGE_COUNT = 1000000000000L;
	private static final String SITEMAP_NAMESPACE = "http://www.sitemaps.org/schemas/sitemap/0.9";


	public static String getSitemap( String type, String cursor, Website website, boolean basicMode ) 
			throws UnexpectedServerException {

		if( website == Website.ALL_LANGUAGE )
			return _getSiteMapForMainWebsite( basicMode ? website.getMobileHostName() : website.getHostName() );

		/* Only include canonical URLs. A common mistake is to include URLs of
		 * duplicate pages. This increases the load on your server without
		 * improving indexing. */
		if( basicMode )		
			return _getSiteMapForBasicMode( website.getMobileHostName() );

		if( type == null ) // Sitemap Index
			return _getSitemapIndex( website.getHostName() );

		if( type.equals( "page" ) )
			return _getSitemapForTypePage(
					Long.parseLong( cursor ),
					website.getHostName(),
					website.getFilterLanguage() );

		if( type.equals( "other" ) )
			return _getSitemapForTypeOther(
					website.getHostName(),
					website.getFilterLanguage() );

		logger.log( Level.SEVERE, "Sitemap type not supported : " + type );

		throw new UnexpectedServerException();

	}


	private static String _getSiteMapForMainWebsite( String hostName ) {
		StringBuilder sitemap = new StringBuilder( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEPARATOR );
		sitemap.append( "<urlset xmlns=\"" + SITEMAP_NAMESPACE + "\">" + LINE_SEPARATOR );
		sitemap.append( _getSitemapEntry( hostName, "/", null, null, null ) );
		sitemap.append( "</urlset>" );
		return sitemap.toString();
	}

	private static String _getSiteMapForBasicMode( String hostName ) {		

		StringBuilder sitemap = new StringBuilder( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEPARATOR );		
		sitemap.append( "<urlset xmlns=\"" + SITEMAP_NAMESPACE + "\">" + LINE_SEPARATOR );		

		// Links specific to basic Mode		
		sitemap.append( _getSitemapEntry( hostName, "/login", null, null, null ) );		
		sitemap.append( _getSitemapEntry( hostName, "/register", null, null, null ) );		
		sitemap.append( _getSitemapEntry( hostName, "/account", null, null, null ) );		
		sitemap.append( _getSitemapEntry( hostName, "/forgot-password", null, null, null ) );		
		sitemap.append( _getSitemapEntry( hostName, "/updatepassword", null, null, null ) );		
		sitemap.append( _getSitemapEntry( hostName, "/navigation", null, null, null ) );		

		sitemap.append( "</urlset>" );		
		return sitemap.toString();		

  	}

	private static String _getSitemapIndex( String hostName ) {

/*		<?xml version="1.0" encoding="UTF-8"?>
		<sitemapindex xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
			<sitemap>
				<loc>http://www.example.com/sitemap1.xml.gz</loc>
				<lastmod>2004-10-01T18:23:17+00:00</lastmod>
			</sitemap>
			<sitemap>
				<loc>http://www.example.com/sitemap2.xml.gz</loc>
			</sitemap>
		</sitemapindex> */

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		Long pageIdFirst = dataAccessor.getPageIdFirst();
		Long pageIdLast = dataAccessor.getPageIdLast();

		StringBuilder sitemapIndex = new StringBuilder( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEPARATOR );
		sitemapIndex.append( "<sitemapindex xmlns=\"" + SITEMAP_NAMESPACE + "\">" + LINE_SEPARATOR );

		// Pages without PAGE entity
		sitemapIndex.append( _getSitemapIndexEntry( hostName, "other", null, null ) );

		// Pages with PAGE entity
		for( long i = pageIdFirst; i <= pageIdLast; i += SITEMAP_PAGE_COUNT )
			sitemapIndex.append( _getSitemapIndexEntry( hostName, "page", i, null ) );

		sitemapIndex.append( "</sitemapindex>" );

		return sitemapIndex.toString();

	}

	private static String _getSitemapIndexEntry( String hostName, String type, Long cursor, Date lastModified ) {	

/*		<sitemap>
			<loc>http://www.example.com/sitemap.xml.gz</loc>
			<lastmod>2004-10-01</lastmod>
		</sitemap> */

		StringBuilder sitemap = new StringBuilder( "<sitemap>" + LINE_SEPARATOR );

		String loc = "https://" + hostName + "/sitemap";
		if( type != null ) {
			loc = loc + "?" + RequestParameter.SITEMAP_TYPE.getName() + "=" + type;
			if( cursor != null )
				loc = loc + "&" + RequestParameter.SITEMAP_CURSOR.getName() + "=" + cursor;
		}
		sitemap.append( "<loc>" ).append( _getEntityEscapedUrl( loc ) ).append( "</loc>" ).append( LINE_SEPARATOR );

		if( lastModified != null ) {
			DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
			dateFormat.setTimeZone( TimeZone.getTimeZone( "Asia/Kolkata" ) );
			sitemap.append( "<lastmod>" ).append( dateFormat.format( lastModified ) ).append( "</lastmod>" ).append( LINE_SEPARATOR );
		}
		sitemap.append( "</sitemap>" ).append( LINE_SEPARATOR );

		return sitemap.toString();

	}

	
	private static String _getSitemapForTypePage( Long cursor, String hostName, Language language ) {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		List<Page> pageList = dataAccessor.getPageList( cursor, cursor + SITEMAP_PAGE_COUNT );

		Map<Long,String> pratilipiPageMap = new HashMap<>();
		Map<Long,String> authorPageMap = new HashMap<>();
		Map<Long,String> blogPageMap = new HashMap<>();
		Map<Long,String> blogPostPageMap = new HashMap<>();
		Map<Long,String> eventPageMap = new HashMap<>();

		for( Page page : pageList ) {
			String pageUri = page.getUriAlias() != null ? page.getUriAlias() : page.getUri();
			switch( page.getType() ) {
				case PRATILIPI:
					pratilipiPageMap.put( page.getPrimaryContentId(), pageUri );
					break;
				case AUTHOR:
					authorPageMap.put( page.getPrimaryContentId(), pageUri );
					break;
				case BLOG:
					blogPageMap.put( page.getPrimaryContentId(), pageUri );
					break;
				case BLOG_POST:
					blogPostPageMap.put( page.getPrimaryContentId(), pageUri );
					break;
				case EVENT:
					eventPageMap.put( page.getPrimaryContentId(), pageUri );
					break;
				default:
					break;
			}
		}


		Map<Long, Pratilipi> pratilipis = dataAccessor.getPratilipis( pratilipiPageMap.keySet() );
		Map<Long, Author> authors = dataAccessor.getAuthors( authorPageMap.keySet() );
		Map<Long, BlogPost> blogPosts = dataAccessor.getBlogPosts( blogPostPageMap.keySet() );
		Map<Long, Event> events = dataAccessor.getEvents( eventPageMap.keySet() );


		StringBuilder sitemap = new StringBuilder( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEPARATOR );
		sitemap.append( "<urlset xmlns=\"" + SITEMAP_NAMESPACE + "\">" + LINE_SEPARATOR );

		// For a few cases when sitemap is empty, add an entry to avoid error on crawler
		if( pratilipiPageMap.isEmpty() 
				&& authorPageMap.isEmpty() 
				&& blogPageMap.isEmpty() 
				&& blogPostPageMap.isEmpty() 
				&& eventPageMap.isEmpty() ) {
			sitemap.append( _getSitemapEntry( hostName, "/", null, "hourly", "0.9" ) );
		}

		for( Long pratilipiId : pratilipiPageMap.keySet() ) {
			Pratilipi pratilipi = pratilipis.get( pratilipiId );
			if( pratilipi.getLanguage() != language )
				continue;
			if( pratilipi.getState() != PratilipiState.PUBLISHED )
				continue;
			sitemap.append( _getSitemapEntry( hostName, pratilipiPageMap.get( pratilipiId ), pratilipi.getLastUpdated(), "daily", "0.7" ) );
			sitemap.append( _getSitemapEntry( hostName, "/read?" + RequestParameter.CONTENT_ID.getName() + "=" + pratilipiId, pratilipi.getLastUpdated(), "daily", "0.7" ) );
		}

		for( Long authorId : authorPageMap.keySet() ) {
			Author author = authors.get( authorId );
			if( author.getLanguage() != language )
				continue;
			if( author.getState() != AuthorState.ACTIVE )
				continue;
			sitemap.append( _getSitemapEntry( hostName, authorPageMap.get( authorId ), author.getLastUpdated(), "daily", "0.6" ) );
		}

		for( Long blogId : blogPageMap.keySet() )
			sitemap.append( _getSitemapEntry( hostName, blogPageMap.get( blogId ), null, "weekly", null ) );

		for( Long blogPostId : blogPostPageMap.keySet() ) {
			BlogPost blogPost = blogPosts.get( blogPostId );
			if( blogPost.getLanguage() != language )
				continue;
			if( blogPost.getState() != BlogPostState.PUBLISHED )
				continue;
			sitemap.append( _getSitemapEntry( hostName, blogPostPageMap.get( blogPostId ), blogPost.getLastUpdated(), "weekly", "0.6" ) );
		}

		for( Long eventId : eventPageMap.keySet() ) {
			Event event = events.get( eventId );
			if( event.getLanguage() != language )
				continue;
			sitemap.append( _getSitemapEntry( hostName, eventPageMap.get( eventId ), event.getLastUpdated(), "weekly", "0.6" ) );
		}

		sitemap.append( "</urlset>" );
		return sitemap.toString();

	}

	private static String _getSitemapForTypeOther( String hostName, Language language ) {

		StringBuilder sitemap = new StringBuilder( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEPARATOR );
		sitemap.append( "<urlset xmlns=\"" + SITEMAP_NAMESPACE + "\">" + LINE_SEPARATOR );

		// Home page
		sitemap.append( _getSitemapEntry( hostName, "/", null, "hourly", "0.9" ) );

		// Event list page
		sitemap.append( _getSitemapEntry( hostName, "/events", null, "weekly", null ) );

		// Category pages
		for( String categoryName : _getCategoryNameList( language ) )
			sitemap.append( _getSitemapEntry( hostName, "/" + categoryName, null, "hourly", "0.9" ) );

		// Static pages
		for( String staticPage : _getStaticPageList( language ) )
			sitemap.append( _getSitemapEntry( hostName, "/" + staticPage.replace( "_", "/" ), null, "monthly", null ) );

		sitemap.append( "</urlset>" );

		return sitemap.toString();

	}

	
	private static List<String> _getCategoryNameList( Language language ) {

		File folder = new File( DataAccessor.class.getResource( "curated" ).getFile() );
		File[] files = folder.listFiles();

		List<String> categoryNameList = new ArrayList<>();
		String fileNamePrefix = "list." + language.getCode();
		for( File file : files ) {
			if( ! file.isFile() )
				continue;
			if( ! file.getName().startsWith( fileNamePrefix ) )
				continue;
			categoryNameList.add( file.getName().substring( fileNamePrefix.length() + 1 ) );
		}

		return categoryNameList;
		
	}
	
	private static List<String> _getStaticPageList( Language language ) {

		File folder = new File( PratilipiSite.class.getResource( PratilipiSite.dataFilePrefix ).getFile() );
		File[] files = folder.listFiles();

		String fileNamePrefix = "static." + language.getCode();
		String fileNameEnPrefix = "static." + Language.ENGLISH.getCode();

		Set<String> fileNames = new HashSet<>();
		for( File file : files ) {
			if( ! file.isFile() )
				continue;
			if( file.getName().startsWith( fileNamePrefix ) || file.getName().startsWith( fileNameEnPrefix ) )
				fileNames.add( file.getName().substring( file.getName().lastIndexOf( "." ) + 1 ) );
		}

		return new ArrayList<String>( fileNames );

	}

	private static String _getSitemapEntry( String hostName, String uri, Date lastUpdated, String changeFrequency, String priority ) {
/*		<url>
			<loc>http://www.example.com/</loc>
			<lastmod>2005-01-01</lastmod>
			<changefreq>monthly</changefreq>
			<priority>0.8</priority>
		</url>
*/
		String loc = "https://" + hostName + uri;
		StringBuilder sitemapUrl = new StringBuilder( "<url>" + LINE_SEPARATOR );
		sitemapUrl.append( "<loc>" ).append( _getEntityEscapedUrl( loc ) ).append( "</loc>" ).append( LINE_SEPARATOR );
		if( lastUpdated != null ) {
			DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
			dateFormat.setTimeZone( TimeZone.getTimeZone( "Asia/Kolkata" ) );
			sitemapUrl.append( "<lastmod>" ).append( dateFormat.format( lastUpdated ) ).append( "</lastmod>" ).append( LINE_SEPARATOR );
		}
		if( changeFrequency != null )
			sitemapUrl.append( "<changefreq>" ).append( changeFrequency ).append( "</changefreq>" ).append( LINE_SEPARATOR );
		if( priority != null )
			sitemapUrl.append( "<priority>" ).append( priority ).append( "</priority>" ).append( LINE_SEPARATOR );

		sitemapUrl.append( "</url>" ).append( LINE_SEPARATOR );

		return sitemapUrl.toString();

	}

	private static String _getEntityEscapedUrl( String url ) {
		return 	url.replace( "&", "&amp;" )
					.replace( "'", "&apos;" )
					.replace( "\"", "&quot;" )
					.replace( ">", "&gt;" )
					.replace( "<", "&lt;" );
	}

}
