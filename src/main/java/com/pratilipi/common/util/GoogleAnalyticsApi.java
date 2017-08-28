package com.pratilipi.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.Analytics.Data.Ga.Get;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.GaData;
import com.pratilipi.common.exception.UnexpectedServerException;

public class GoogleAnalyticsApi {
	
	private static final Logger logger =
			Logger.getLogger( GoogleAnalyticsApi.class.getName() );

	
	private static Analytics getAnalytics() throws UnexpectedServerException {
		List<String> scopes = new LinkedList<>();
		scopes.add( AnalyticsScopes.ANALYTICS_READONLY );
		return GoogleApi.getAnalytics( scopes );
	}
	
	public static Map<String, Integer> getPageViews( String date )
			throws UnexpectedServerException {
		
		Map<String, Integer> uriViewsMap = new HashMap<String, Integer>();
		
			
		try {
			
			while( true ) {
				
				Get apiQuery = getAnalytics().data().ga()
						.get( "ga:96325104",		// Table Id.
								date,				// Start Date YYYY-MM-DD
								date,				// End Date YYYY-MM-DD
								"ga:pageviews" )	// Metrics.
						.setDimensions( "ga:pagePath" )
						.setStartIndex( uriViewsMap.size() + 1 )
						.setMaxResults( 10000 );
				
				GaData gaData = apiQuery.execute();
				
				if( gaData.getRows() != null )
					for( List<String> row : gaData.getRows() )
						uriViewsMap.put( row.get( 0 ), Integer.parseInt( row.get( 1 ) ) );
				
				if( uriViewsMap.size() == gaData.getTotalResults() )
					break;
				
			}
				
		} catch( IOException e ) {
			logger.log( Level.SEVERE, "Failed to fetch data from Google Analytics.", e );
			throw new UnexpectedServerException();
		}
			
		
		return uriViewsMap;
		
	}
	
	public static Map<Long, Long> getPratilipiReadCount( List<Long> pratilipiIdList ) throws UnexpectedServerException {
		int idsPerRequest = 80;
		Map<Long, Long> idCountMap = new HashMap<Long, Long>();
		
		for( int i = 0; i < pratilipiIdList.size(); i = i + idsPerRequest ) {
			String filters = "";
			for( int j = 0; i + j < pratilipiIdList.size() && j < idsPerRequest; j++ )
				filters = filters + "ga:pagePath=~^/read\\?id=" + pratilipiIdList.get( i + j ) + ".*,";
			filters = filters.substring( 0, filters.length() - 1 );
			
			try {
				Get apiQuery = getAnalytics().data().ga()
						.get( "ga:89762686",		// Table Id.
								"2015-01-01",		// Start Date.
								"today",			// End Date.
								"ga:pageviews" )	// Metrics.
						.setDimensions( "ga:pagePath" )
						.setFilters( filters );
				
				GaData gaData = apiQuery.execute();
				if( gaData.getRows() != null ) {
					for( List<String> row : gaData.getRows() ) {
						String pagePath = row.get( 0 );
						if( pagePath.indexOf( '&' ) != -1 )
							pagePath = pagePath.substring( 0, pagePath.indexOf( '&' ) );
						Long pratilipiId = Long.parseLong( pagePath.substring( pagePath.indexOf( '=' ) + 1 ) );
						long readCount = Long.parseLong( row.get( 1 ) );
						if( idCountMap.containsKey( pratilipiId ) )
							idCountMap.put( pratilipiId , readCount + idCountMap.get( pratilipiId ) );
						else
							idCountMap.put( pratilipiId , readCount );
					}
				}
			} catch( IOException e ) {
				logger.log( Level.SEVERE, "Failed to fetch data from Google Analytics.", e );
				throw new UnexpectedServerException();
			}
		}
		
		return idCountMap;
	}
	
}