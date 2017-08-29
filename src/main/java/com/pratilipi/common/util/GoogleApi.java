package com.pratilipi.common.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.type.AppProperty;
import com.pratilipi.filter.UxModeFilter;

public class GoogleApi {

	private static final Logger logger =
			Logger.getLogger( GoogleApi.class.getName() );

	private static GoogleIdTokenVerifier webTokenVerifier;
	private static GoogleIdTokenVerifier androidTokenVerifier;


	// Using "App Engine default Service Account", implementation will change for systems other than Google App Engine
	private static HttpRequestInitializer getCredential( Collection<String> scopes ) {
		return new AppIdentityCredential( scopes ); // Works only on Google AppEngine
	}

	// Using "App Engine default Service Account", implementation will change for systems other than Google App Engine
	public static String getAccessToken( Collection<String> scopes ) {
		return AppIdentityServiceFactory.getAppIdentityService().getAccessToken( scopes ).getAccessToken();
	}

	
	private static String getWebClientId() {
		return DataAccessorFactory.getDataAccessor()
				.getAppProperty( AppProperty.GOOGLE_WEB_CLIENT_ID )
				.getValue();
	}

	private static String getAppClientId() {
		return DataAccessorFactory.getDataAccessor()
				.getAppProperty( AppProperty.GOOGLE_ANDROID_CLIENT_ID )
				.getValue();
	}

	
	public static GoogleIdTokenVerifier getWebIdTokenVerifier() {
		if( webTokenVerifier == null )
			webTokenVerifier = new GoogleIdTokenVerifier
					.Builder( new NetHttpTransport(), new JacksonFactory() )
					.setAudience( Arrays.asList( getWebClientId() ) )
					.setIssuer( "accounts.google.com" )
					.build();
		return webTokenVerifier;
	}

	public static GoogleIdTokenVerifier getAndroidIdTokenVerifier() {
		if( androidTokenVerifier == null )
			androidTokenVerifier = new GoogleIdTokenVerifier
					.Builder( new NetHttpTransport(), new JacksonFactory() )
					.setAudience( Arrays.asList( getWebClientId() ) )
					.setIssuer( "https://accounts.google.com" )
					.build();
		return androidTokenVerifier;
	}
	

	public static Analytics getAnalytics( Collection<String> scopes )
			throws UnexpectedServerException {
		
		try {
			return new Analytics.Builder(
					GoogleNetHttpTransport.newTrustedTransport(),
					JacksonFactory.getDefaultInstance(),
					getCredential( scopes ) )
				.setApplicationName( "Pratilipi" )
				.build();
		} catch( GeneralSecurityException | IOException e ) {
			logger.log( Level.SEVERE, "Failed to create Analytics Service Object.", e );
			throw new UnexpectedServerException();
		}
		
	}

	
	public static UserData getUserData( String googleIdToken )
			throws InvalidArgumentException, UnexpectedServerException {

		try {

			GoogleIdToken idToken = UxModeFilter.isAndroidApp() 
					? getAndroidIdTokenVerifier().verify( googleIdToken ) 
					: getWebIdTokenVerifier().verify( googleIdToken );

			String authorisedParty = UxModeFilter.isAndroidApp() ? getAppClientId() : getWebClientId();
			if( idToken == null || idToken.getPayload() == null || ! idToken.getPayload().getAuthorizedParty().equals( authorisedParty ) ) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty( "googleIdToken", "Invalid GoogleIdToken !" );
				throw new InvalidArgumentException( jsonObject );
			}

			Payload payload = idToken.getPayload();
			logger.log( Level.INFO, "GoogleApi Payload : " + new Gson().toJson( payload ) );

			if( payload.get( "given_name" ) == null
					|| ( (String) payload.get( "given_name" ) ).isEmpty() ) {
				logger.log( Level.SEVERE, "Google given_name is missing for GoogleUser: " + payload.getSubject() );
				throw new UnexpectedServerException();
			}

			UserData userData = new UserData();
			userData.setGoogleId( payload.getSubject() );
			userData.setFirstName( (String) payload.get( "given_name" ) );
			userData.setLastName( (String) payload.get( "family_name" ) );
			userData.setEmail( payload.getEmail() );

			return userData;

		} catch( GeneralSecurityException | IOException e ) {
			logger.log( Level.SEVERE, "Google id token verification failed: " + e );
			throw new UnexpectedServerException();
		}

	}

}
