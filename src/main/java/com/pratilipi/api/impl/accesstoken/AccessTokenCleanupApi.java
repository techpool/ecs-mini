package com.pratilipi.api.impl.accesstoken;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;

@SuppressWarnings("serial")
@Bind( uri = "/accesstoken/cleanup" )
public class AccessTokenCleanupApi extends GenericApi {
	
	private static final Logger logger =
			Logger.getLogger( AccessTokenCleanupApi.class.getName() );
	
	
	@Get
	public GenericResponse get( GenericRequest request ) {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		while( true ) {
			int count = dataAccessor.deleteExpiredAccessTokenList( 1000 );
			logger.log( Level.INFO, "Deleted " + count + " AccessToken entities ..." );
			if( count < 1000 )
				break;
		}
		
		return new GenericResponse();
		
	}
	
}
