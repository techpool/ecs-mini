package com.pratilipi.data.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.filter.AccessTokenFilter;

public class AccessTokenDataUtil {
	
	public static final long MIN_EXPIRY_MILLIS = TimeUnit.MILLISECONDS.convert( 15, TimeUnit.DAYS );
	public static final long MAX_EXPIRY_MILLIS = TimeUnit.MILLISECONDS.convert( 30, TimeUnit.DAYS );

	
	public static AccessToken newUserAccessToken( HttpServletRequest request ) {
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		AccessToken accessToken = dataAccessor.newAccessToken();

		accessToken.setUserId( 0L );
		accessToken.setDeviceLocation(
				request.getHeader( "X-AppEngine-City" ),
				request.getHeader( "X-AppEngine-Region" ),
				request.getHeader( "X-AppEngine-Country" ) );
		accessToken.setDeviceUserAgent( request.getHeader( "User-Agent" ) );
		accessToken.setExpiry( new Date( new Date().getTime() + MAX_EXPIRY_MILLIS ) );
		accessToken.setCreationDate( new Date() );
		
		accessToken = dataAccessor.createOrUpdateAccessToken( accessToken );
		return accessToken;
	}
	
	
	public static void setOrUpdateFcmToken( String fcmToken ) {
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if( fcmToken.equals( accessToken.getFcmToken() ) )
			return;
		accessToken.setFcmToken( fcmToken );
		accessToken = DataAccessorFactory.getDataAccessor()
				.createOrUpdateAccessToken( accessToken );
	}
	
}
