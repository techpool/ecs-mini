package com.pratilipi.data.mock;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.pratilipi.data.type.AppProperty;
import com.pratilipi.data.type.gae.AppPropertyEntity;

public class AppPropertyMock {
	
	private static final Logger logger =
			Logger.getLogger( AppPropertyMock.class.getName() );
	
	public static final List<AppProperty> APP_PROPERTY_TABLE = new LinkedList<>();
	
	public static final AppPropertyEntity fbCredAppProperty = new AppPropertyEntity( AppProperty.FACEBOOK_CREDENTIALS );
	
	public static final AppPropertyEntity firebaseServiceAccount = new AppPropertyEntity( AppProperty.SERVICE_ACCOUNT_FIREBASE );

	
	static {
		// Adding Facebook Test App credentials
		Map<String, String> facebookCredentials = new HashMap<String, String>();
		facebookCredentials.put( "appId", "540237692796510" );
		facebookCredentials.put( "appSecret", "307beaac9c1a3393c1cc539c03832fba" );
		fbCredAppProperty.setValue( facebookCredentials );
		APP_PROPERTY_TABLE.add( fbCredAppProperty );

		// Adding service account credentials from localhost
		String firebaseServiceAccountCredentials = new String();
		try {
			InputStream inputStream = new FileInputStream( "config/firebase.json" );
			List<String> lines = IOUtils.readLines( inputStream, "UTF-8" );
			for( String line : lines )
				firebaseServiceAccountCredentials = firebaseServiceAccountCredentials + line;
			inputStream.close();
		} catch( NullPointerException | IOException e ) {
			logger.log( Level.SEVERE, "Exception while reading from json file.", e );
		}
		firebaseServiceAccountCredentials = firebaseServiceAccountCredentials.replaceAll( "\n", "\\\\n" );
		firebaseServiceAccount.setValue( firebaseServiceAccountCredentials );
		APP_PROPERTY_TABLE.add( firebaseServiceAccount );
		
	}

}