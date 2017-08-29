package com.pratilipi.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Gender;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.type.AppProperty;

public class FacebookApi {

	private static final Logger logger =
			Logger.getLogger( FacebookApi.class.getName() );
	
	private static final String GRAPH_API_2p2_URL = "https://graph.facebook.com/v2.2";
	private static final String GRAPH_API_2p4_URL = "https://graph.facebook.com/v2.4";
	private static final String GRAPH_API_2p6_URL = "https://graph.facebook.com/v2.6";
	
	
	public static String getAppId() {
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Map<String, String> facebookCredentials = dataAccessor.getAppProperty( AppProperty.FACEBOOK_CREDENTIALS ).getValue();
		return facebookCredentials.get( "appId" );
	}
	
	private static String getAccessToken() {
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Map<String, String> facebookCredentials = dataAccessor.getAppProperty( AppProperty.FACEBOOK_CREDENTIALS ).getValue();
		return facebookCredentials.get( "appId" ) + "|" + facebookCredentials.get( "appSecret" );
	}
	
	
	public static Boolean validateUserAccessToken( String userAccessToken ) 
			throws UnexpectedServerException {
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put( "input_token", userAccessToken );
		paramsMap.put( "access_token", getAccessToken() );
		String responsePayload = HttpUtil.doGet( GRAPH_API_2p2_URL + "/debug_token", paramsMap );
		
		JsonObject responseJson = new Gson().fromJson( responsePayload, JsonElement.class ).getAsJsonObject();
		return responseJson.get( "error" ) == null
				&& responseJson.get( "data" ) != null
				&& responseJson.get( "data" ).getAsJsonObject().get( "app_id" ).getAsString().equals( getAppId() );
		
	}
	
	public static UserData getUserData( String fbUserAccessToken )
			throws InvalidArgumentException, UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put( "access_token", fbUserAccessToken );
		paramsMap.put( "fields", "id,first_name,last_name,gender,birthday,email" );
		String responsePayload = HttpUtil.doGet( GRAPH_API_2p6_URL + "/me", paramsMap );
		
		JsonObject responseJson = new Gson().fromJson( responsePayload, JsonElement.class ).getAsJsonObject();
		if( responseJson.get( "error" ) != null ) {
			logger.log( Level.SEVERE, "Error response from Graph Api." );
			// Facebook returns code 190 if access-token is expired
			if( responseJson.get( "error" ).getAsJsonObject().get( "code" ).getAsInt() == 190 )
				throw new InvalidArgumentException( "Facebook AccessToken is expired." );
			else
				throw new UnexpectedServerException();
		} else {

			if( responseJson.get( "first_name" ) == null || 
					responseJson.get( "first_name" ).getAsString().isEmpty() ) {
				logger.log( Level.INFO, "HTTP Response : " + responseJson );
				logger.log( Level.SEVERE, "Facebook first_name is missing for FacebookUser: " + responseJson.get( "id" ).getAsString() );
				throw new UnexpectedServerException();
			}

			UserData userData = new UserData();
			userData.setFacebookId( responseJson.get( "id" ).getAsString() );
			userData.setFirstName( responseJson.get( "first_name" ).getAsString() );
			userData.setLastName( responseJson.get( "last_name" ).getAsString() );
			if( responseJson.get( "gender" ) != null )
				userData.setGender( Gender.valueOf( responseJson.get( "gender" ).getAsString().toUpperCase() ) );
			
			if( responseJson.get( "birthday" ) != null ) {
				String dob = responseJson.get( "birthday" ).getAsString();
				String year = dob.substring( 6 );
				String month = dob.substring( 0, 2 );
				String date = dob.substring( 3, 5 );
				userData.setDateOfBirth( year + "-" + month + "-" + date );
			}
			
			if( responseJson.get( "email" ) != null )
				userData.setEmail( responseJson.get( "email" ).getAsString() );
			
			return userData;
			
		}
			
	}
	
	
	public static long getUrlShareCount( String shareUrl ) throws UnexpectedServerException {
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put( "id", shareUrl );
		paramsMap.put( "access_token", getAccessToken() );
		String responsePayload = HttpUtil.doGet( GRAPH_API_2p6_URL, paramsMap );

		JsonElement responseJson = new Gson().fromJson( responsePayload, JsonElement.class );

		// Facebook might return an error response.
		if( responseJson.getAsJsonObject().get( "error" ) != null ) {
			logger.log( Level.SEVERE, "Facebook responded with an error message: " + responseJson.getAsJsonObject().get( "error" ).getAsJsonObject().get( "message" ) );
			throw new UnexpectedServerException();
		}

		JsonElement shareJson = responseJson.getAsJsonObject().get( "share" );
		if( shareJson == null )
			return 0L;
		
		JsonElement shareCountJson = shareJson.getAsJsonObject().get( "share_count" );
		if( shareCountJson == null )
			return 0L;
		
		return shareCountJson.getAsLong();
		
	}
	
	
	public static void postScrapeRequest( String url ) throws UnexpectedServerException {
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put( "id", url );
		paramsMap.put( "scrape", "true" );
		paramsMap.put( "access_token", getAccessToken() );
		String responsePayload = HttpUtil.doPost( GRAPH_API_2p4_URL, paramsMap );
		
		JsonObject responseJson = new Gson().fromJson( responsePayload, JsonObject.class );
		if( responseJson.get( "error" ) != null ) {
			logger.log( Level.SEVERE, "Facebook scrapping for url: " + url + " failed with error \"" + responseJson.get( "error" ) + "\"" );
			throw new UnexpectedServerException();
		}
		
	}

}
