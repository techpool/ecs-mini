package com.pratilipi.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;
import com.pratilipi.common.type.PageType;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Page;


public class LogAndSecurityFilter implements Filter {

	private static final Logger logger = 
			Logger.getLogger( LogAndSecurityFilter.class.getName() ); 

	
	@Override
	public void init( FilterConfig filterConfig ) throws ServletException {	}

	@Override
	public void destroy() {	}

	@Override
	public void doFilter( ServletRequest request, ServletResponse response,
			FilterChain chain ) throws IOException, ServletException {

		String uri = ( ( HttpServletRequest ) request ).getRequestURI().toString();
		if( ! uri.equals( "/poc1" ) ) {
			DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
			Page page = dataAccessor.getPage( uri );
			if( page != null ) {
				if( page.getType() == PageType.BLOG_POST ) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty( "pageId", page.getId() );
					jsonObject.addProperty( "accessToken", AccessTokenFilter.getAccessToken().getId() );
					logger.log( Level.INFO, "DataFlow#PAGE_HIT::" + jsonObject.toString() );
				}
			}
		}

		chain.doFilter( request, response );

	}

}
