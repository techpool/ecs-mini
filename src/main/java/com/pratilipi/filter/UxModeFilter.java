package com.pratilipi.filter;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PageType;
import com.pratilipi.common.type.Website;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.Page;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.type.Event;
import com.pratilipi.data.type.BlogPost;


public class UxModeFilter implements Filter {

	private static boolean isWebApp = false;
	private static boolean isAndroidApp = false;
	
	private static final ThreadLocal<Boolean> threadLocalBasicMode = new ThreadLocal<Boolean>();
	private static final ThreadLocal<Website> threadLocalWebsite = new ThreadLocal<Website>();
	private static final ThreadLocal<String> threadLocalEcsEndpoint = new ThreadLocal<>();

	private static final Logger logger = Logger.getLogger( UxModeFilter.class.getName() );
	
	@Override
	public void init( FilterConfig config ) throws ServletException {
		String moduleParam = config.getInitParameter( "Module" );
		isWebApp = moduleParam != null && moduleParam.equalsIgnoreCase( "WebApp" );
		isAndroidApp = moduleParam != null && moduleParam.equalsIgnoreCase( "Android" );
	}

	@Override
	public void destroy() { }

	@Override
	public void doFilter( ServletRequest req, ServletResponse resp, FilterChain chain )
			throws IOException, ServletException {
		
		if( isAndroidApp ) {

			threadLocalBasicMode.set( false );
			threadLocalWebsite.set( null );
		
		} else {
		
			HttpServletRequest request = ( HttpServletRequest ) req;
			HttpServletResponse response = ( HttpServletResponse ) resp;

			boolean requestFromEcs = request.getHeader( "ECS-HostName" ) != null;
			String hostName = requestFromEcs ? request.getHeader( "ECS-HostName" ) : request.getServerName();

			// Defaults - for all test environments
			boolean basicMode = false;
			Website website = null;

			// Figuring out Mode and Languages from a pre-configured list
			for( Website web : Website.values() ) {
				if( hostName.equals( web.getHostName() ) ) {
					basicMode = false;
					website = web;
					break;
				} else if( hostName.equals( web.getMobileHostName() ) ) {
					basicMode = true;
					website = web;
					break;
				}
			}

			if( request.getHeader( "ECS-Endpoint" ) != null ) {
				threadLocalEcsEndpoint.set( request.getHeader( "ECS-Endpoint" ) );
			} else if( website != null ) {
				if( website == Website.ALPHA ) {
					threadLocalEcsEndpoint.set( "https://hindi.pratilipi.com" );
					basicMode = true;
				} else {
					threadLocalEcsEndpoint.set( "https://" + (basicMode ? website.getMobileHostName() : website.getHostName()) );
				}
			} else {
				threadLocalEcsEndpoint.set( "https://api.pratilipi.com" );
			}

			threadLocalBasicMode.set( basicMode );
			threadLocalWebsite.set( website );

		}
		
		chain.doFilter( req, resp );

		threadLocalBasicMode.remove();
		threadLocalWebsite.remove();
		threadLocalEcsEndpoint.remove();
		
	}


	public static boolean isAndroidApp() {
		return isAndroidApp;
	}

	public static boolean isBasicMode() {
		return threadLocalBasicMode.get();
	}

	public static Website getWebsite() {
		return threadLocalWebsite.get();
	}

	public static String getEcsEndpoint() {
		return threadLocalEcsEndpoint.get();
	}

	@Deprecated
	public static Language getDisplayLanguage() {
		Website website = threadLocalWebsite.get();
		return website == null ? null : website.getDisplayLanguage();
	}

	@Deprecated
	public static Language getFilterLanguage() {
		Website website = threadLocalWebsite.get();
		return website == null ? null : website.getFilterLanguage();
	}

}
