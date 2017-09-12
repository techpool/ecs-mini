package com.pratilipi.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pratilipi.api.impl.user.UserAccessTokenApi;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.RequestCookie;
import com.pratilipi.common.type.RequestHeader;
import com.pratilipi.common.type.RequestParameter;
import com.pratilipi.common.type.Website;
import com.pratilipi.common.util.SystemProperty;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.type.AppProperty;
import com.pratilipi.data.util.AccessTokenDataUtil;

public class AccessTokenFilter implements Filter {

	private static final ThreadLocal<AccessToken> threadLocalAccessToken = new ThreadLocal<AccessToken>();
	private static final ThreadLocal<String> threadLocalAccessTokenId = new ThreadLocal<String>();

	@Override
	public void init( FilterConfig config ) throws ServletException { }

	@Override
	public void destroy() { }

	@Override
	public void doFilter( ServletRequest req, ServletResponse resp, FilterChain chain )
			throws IOException, ServletException {

		HttpServletRequest request = ( HttpServletRequest ) req;
		HttpServletResponse response = ( HttpServletResponse ) resp;

		if( request.getHeader( "Access-Token" ) != null )
			threadLocalAccessTokenId.set( request.getHeader( "Access-Token" ) );
		else if( request.getParameter( RequestParameter.ACCESS_TOKEN.getName() ) != null )
			threadLocalAccessTokenId.set( request.getParameter( RequestParameter.ACCESS_TOKEN.getName() ) );
		else
			threadLocalAccessTokenId.set( getCookieValue( RequestCookie.ACCESS_TOKEN.getName(), request ) );

		chain.doFilter( request, response );
		threadLocalAccessTokenId.remove();

	}

	public static String getCookieValue( String cookieName, HttpServletRequest request ) {
		Cookie[] cookies = request.getCookies();
		if( cookies == null ) return null;
		for( Cookie cookie : cookies ) {
			if( cookie.getName().equals( cookieName ) )
				return cookie.getValue().trim();
		}
		return null;
	}

	public static AccessToken getAccessToken() {
		return threadLocalAccessToken.get();
	}

	public static String getAccessTokenId() { return threadLocalAccessTokenId.get(); }

}
