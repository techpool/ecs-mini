package com.pratilipi.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.util.AuthorDataUtil;
import com.pratilipi.data.util.PratilipiDataUtil;

public class PratilipiSiteFilter implements Filter {

	private final String subDomainPrefix = "(www|hindi|gujarati|tamil|marathi|malayalam|bengali|telugu|kannada)";
	private final String mobileSubDomainPrefix = "(m|hi|gu|ta|mr|ml|bn|te|kn)";
	private final Pattern validHostSubdomainRegEx = Pattern.compile(
			"www\\.(" + subDomainPrefix + "|" + mobileSubDomainPrefix + ")\\.pratilipi\\.com" );
	private final Pattern validHostRegEx = Pattern.compile(
			subDomainPrefix + "\\.pratilipi\\.com" // Prod (Standard)
					+ "|"
					+ mobileSubDomainPrefix + "\\.pratilipi\\.com" // Prod (Basic)
					+ "|"
					+ "gae-prod\\.pratilipi\\.com" // gae-prod from ecs
					+ "|"
					+ subDomainPrefix + "\\-gamma\\.pratilipi\\.com" // Gamma (Standard)
					+ "|"
					+ mobileSubDomainPrefix + "\\-gamma\\.pratilipi\\.com" // Gamma (Basic)
					+ "|"
					+ "gae-gamma\\.pratilipi\\.com" // gae-gamma from ecs
					+ "|"
					+ "devo-pratilipi\\.appspot\\.com" // Devo (Standard)
					+ "|"
					+ "localhost|127.0.0.1" );
	private final Map<String, String> redirections = new HashMap<>();
	private final Pattern oldPratilipiCoverUrlRegEx = Pattern.compile(
			"/resource\\.(book|poem|story|article|pratilipi)-cover/.*"
					+ "|"
					+ "/pratilipi-cover/(150|original)/.*" );
	private final Pattern oldAuthorImageUrlRegEx = Pattern.compile(
			"/resource.author-image/.*"
					+ "|"
					+ "/author-image/150/.*" );
	private final Pattern oldPratilipiReaderUrlRegEx = Pattern.compile( "/read/(book|poem|story|article|pratilipi)/.*" );

	{
		redirections.put( "/theme.pratilipi/logo.png", "/logo.png" );
		redirections.put( "/apple-touch-icon.png", "/favicon.ico" );
		redirections.put( "/apple-touch-icon-120x120.png", "/favicon.ico" );
		redirections.put( "/apple-touch-icon-precomposed.png", "/favicon.ico" );
		redirections.put( "/apple-touch-icon-120x120-precomposed.png", "/favicon.ico" );

//		redirections.put( "/robots.txt", "/service.robots" );

		redirections.put( "/give-away", "/" );
		redirections.put( "/give-away/Gora.pdf", "/" );
		redirections.put( "/give-away/Kukurmutta.pdf", "/" );
		redirections.put( "/give-away/Ram_Ki_Shakti_Pooja.pdf", "/" );
		redirections.put( "/give-away/Utkrasht_Sahitya_1.pdf", "/" );
		redirections.put( "/give-away/Chandrakanta.pdf", "/book/5673309542809600" );

		redirections.put( "/event/gpv", "/event/gnayam-pada-varai" );
		redirections.put( "/event/gnayam&pada&varai", "/event/gnayam-pada-varai" );
		redirections.put( "/event/gnayam&pada&varai>", "/event/gnayam-pada-varai" );
		redirections.put( "/event/kk", "/event/kondaadapadaadha-kaadhalgal" );
		redirections.put( "/event/yn", "/event/yaadhumaagi-nindraal" );

		redirections.put( "/books/gujarati",    "http://gujarati.pratilipi.com/search?q=books" );
		redirections.put( "/poems/gujarati",    "http://gujarati.pratilipi.com/search?q=poems" );
		redirections.put( "/stories/gujarati",  "http://gujarati.pratilipi.com/search?q=stories" );
		redirections.put( "/articles/gujarati", "http://gujarati.pratilipi.com/search?q=articles" );

		redirections.put( "/books/tamil",    "http://tamil.pratilipi.com/search?q=books" );
		redirections.put( "/poems/tamil",    "http://tamil.pratilipi.com/search?q=poems" );
		redirections.put( "/stories/tamil",  "http://tamil.pratilipi.com/search?q=stories" );
		redirections.put( "/articles/tamil", "http://tamil.pratilipi.com/search?q=articles" );

		redirections.put( "/books/hindi",    "http://hindi.pratilipi.com/search?q=books" );
		redirections.put( "/poems/hindi",    "http://hindi.pratilipi.com/search?q=poems" );
		redirections.put( "/stories/hindi",  "http://hindi.pratilipi.com/search?q=stories" );
		redirections.put( "/articles/hindi", "http://hindi.pratilipi.com/search?q=articles" );

		redirections.put( "/about", "/about/pratilipi" );
		redirections.put( "/career", "/work-with-us" );

		redirections.put( "/authors", "/admin/authors" );
		redirections.put( "/email-templates", "/admin/email-templates" );
		redirections.put( "/batch-process", "/admin/batch-process" );

		redirections.put( "/resetpassword", "/forgot-password" );

	}


	@Override
	public void init( FilterConfig config ) throws ServletException { }

	@Override
	public void destroy() { }

	@Override
	public void doFilter( ServletRequest req, ServletResponse resp, FilterChain chain )
			throws IOException, ServletException {

		HttpServletRequest request = ( HttpServletRequest ) req;
		HttpServletResponse response = ( HttpServletResponse ) resp;

		String host = request.getServerName();
		String requestUri = request.getRequestURI();
		String userAgent = request.getHeader( "user-agent" );


		if( requestUri.equals( "/_ah/warmup" ) || requestUri.equals( "/_ah/start" ) || requestUri.equals( "/_ah/stop" ) ) {
			response.setStatus( HttpServletResponse.SC_NO_CONTENT );


		} else if( userAgent != null && ( userAgent.startsWith( "libwww-perl" ) || userAgent.startsWith( "WhatsApp/" ) ) ) {
			response.setStatus( HttpServletResponse.SC_NO_CONTENT );


		} else if( validHostSubdomainRegEx.matcher( host ).matches() ) { // Redirecting to <lang>.pratilipi.com
			response.setStatus( HttpServletResponse.SC_MOVED_PERMANENTLY );
			String queryString = request.getQueryString();
			if( queryString == null || queryString.isEmpty() )
				response.setHeader( "Location", ( request.isSecure() ? "https:" : "http:" ) + "//" + host.substring( 4 ) + requestUri );
			else
				response.setHeader( "Location", ( request.isSecure() ? "https:" : "http:" ) + "//" + host.substring( 4 ) + requestUri + "?" + request.getQueryString() );


		} else if( ! validHostRegEx.matcher( host ).matches() ) { // Redirecting to www.pratilipi.com
			response.setStatus( HttpServletResponse.SC_MOVED_PERMANENTLY );
			String queryString = request.getQueryString();
			if( queryString == null || queryString.isEmpty() )
				response.setHeader( "Location", ( request.isSecure() ? "https:" : "http:" ) + "//www.pratilipi.com" + requestUri + "?redirect=gcp" );
			else
				response.setHeader( "Location", ( request.isSecure() ? "https:" : "http:" ) + "//www.pratilipi.com" + requestUri + "?" + request.getQueryString() + "&redirect=gcp" );


		} else if( requestUri.length() > 1 && requestUri.endsWith( "/" ) ) { // Removing trailing "/"
			response.setStatus( HttpServletResponse.SC_MOVED_PERMANENTLY );
			response.setHeader( "Location", requestUri.substring( 0, requestUri.length() -1 ) );


		} else if( redirections.get( requestUri ) != null ) {
			response.setStatus( HttpServletResponse.SC_MOVED_PERMANENTLY );
			response.setHeader( "Location", redirections.get( requestUri ) );


		} else if( "/api.pratilipi/pratilipi/resource".equals( requestUri ) ) { // Redirecting to new Pratilipi content image url
			response.setStatus( HttpServletResponse.SC_MOVED_PERMANENTLY );
			String queryString = request.getQueryString();
			if( queryString == null || queryString.isEmpty() )
				response.setHeader( "Location", "/api/pratilipi/content/image" );
			else
				response.setHeader( "Location", "/api/pratilipi/content/image" + "?" + request.getQueryString() );


		} else if( oldPratilipiCoverUrlRegEx.matcher( requestUri ).matches() ) { // Redirecting to new Pratilipi cover url
			response.setStatus( HttpServletResponse.SC_MOVED_PERMANENTLY );
			String pratilipiIdStr = requestUri.substring( requestUri.lastIndexOf( '/' ) + 1 );
			Long pratilipiId = Long.parseLong( pratilipiIdStr );
			Pratilipi pratilipi = DataAccessorFactory.getDataAccessor().getPratilipi( pratilipiId );
			response.setHeader( "Location", PratilipiDataUtil.createPratilipiCoverUrl( pratilipi, 150 ) );


		} else if( oldAuthorImageUrlRegEx.matcher( requestUri ).matches() ) { // Redirecting to new Author image url
			response.setStatus( HttpServletResponse.SC_MOVED_PERMANENTLY );
			String authorIdStr = requestUri.substring( requestUri.lastIndexOf( '/' ) + 1 );
			Long authorId = Long.parseLong( authorIdStr );
			Author author = DataAccessorFactory.getDataAccessor().getAuthor( authorId );
			response.setHeader( "Location", AuthorDataUtil.createAuthorProfileImageUrl( author, 150 ) );


		} else if( oldPratilipiReaderUrlRegEx.matcher( requestUri ).matches() ) { // Redirecting to new Pratilipi reader url
			response.setStatus( HttpServletResponse.SC_MOVED_PERMANENTLY );
			response.setHeader( "Location", requestUri.replaceFirst( "/(book|poem|story|article|pratilipi)/", "?id=" ) );


		} else {
			DataAccessorFactory.getL1CacheAccessor().flush();
			chain.doFilter( request, response );
		}

	}

}
