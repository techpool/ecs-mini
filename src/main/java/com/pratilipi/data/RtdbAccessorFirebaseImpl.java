package com.pratilipi.data;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.EmailFrequency;
import com.pratilipi.common.type.NotificationType;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.common.util.SystemProperty;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.type.UserPreferenceRtdb;
import com.pratilipi.data.type.rtdb.UserPreferenceRtdbImpl;


public class RtdbAccessorFirebaseImpl implements RtdbAccessor {

	private static final Logger logger =
			Logger.getLogger( RtdbAccessorFirebaseImpl.class.getName() );


	private static final String DATABASE_URL = SystemProperty.FIREBASE_DB_URL;
	private static final String DATABASE_PREFERENCE_TABLE = "PREFERENCE";
	private static final String DATABASE_PRATILIPI_CONTENT_TABLE = "PRATILIPI_CONTENT";


	private final Map<String, String> headersMap;

	
	public RtdbAccessorFirebaseImpl( String googleApiAccessToken ) {
		this.headersMap = new HashMap<>();
		this.headersMap.put( "Authorization", "Bearer " + googleApiAccessToken );
	}


	// PREFERENCE Table

	@Override
	public UserPreferenceRtdb getUserPreference( Long userId )
			throws UnexpectedServerException {

		try {
			BlobEntry blobEntry = HttpUtil.doGet( _getUserPreferenceDbUrl( userId ), headersMap, null );
			String jsonStr = new String( blobEntry.getData(), "UTF-8" );
			return _getUserPreferenceRtdb( jsonStr );
		} catch( UnsupportedEncodingException e ) {
			logger.log( Level.SEVERE, "Failed to parse response from Firebase.", e );
			throw new UnexpectedServerException();
		}

	}

	@Override
	public Map<Long, UserPreferenceRtdb> getUserPreferences( Collection<Long> userIds )
			throws UnexpectedServerException {

		if( userIds == null || userIds.isEmpty() )
			return new HashMap<>();

		userIds = new HashSet<>( userIds );

		List<String> targetUrlList = new ArrayList<>( userIds.size() );
		for( Long userId : userIds )
			targetUrlList.add( _getUserPreferenceDbUrl( userId ) );
		
		Map<String, BlobEntry> blobEntries = HttpUtil.doGet( targetUrlList, headersMap );

		Map<Long, UserPreferenceRtdb> userPreferences = new HashMap<>( userIds.size() );
		try {
			for( Long userId : userIds ) {
				BlobEntry blobEntry = blobEntries.get( _getUserPreferenceDbUrl( userId ) );
				String jsonStr = new String( blobEntry.getData(), "UTF-8" );
				userPreferences.put( userId, _getUserPreferenceRtdb( jsonStr ) );
			}
		} catch( UnsupportedEncodingException e ) {
			logger.log( Level.SEVERE, "Failed to parse response from Firebase.", e );
			throw new UnexpectedServerException();
		}
		
		return userPreferences;

	}

	@Override
	public Map<Long, UserPreferenceRtdb> getUserPreferences( Date minLastUpdated ) 
			throws UnexpectedServerException {

		Map<String,String> paramsMap = new HashMap<>();
		paramsMap.put( "orderBy", "\"" + "lastUpdated" + "\"" );
		paramsMap.put( "startAt", minLastUpdated.getTime() + "" );
		return _getUserPreferences( paramsMap );

	}

	@Override
	public Map<Long, UserPreferenceRtdb> getUserPreferences( Integer maxAndroidVersionCode ) 
			throws UnexpectedServerException {

		Map<String,String> paramsMap = new HashMap<>();
		paramsMap.put( "orderBy", "\"" + "androidVersionCode" + "\"" );
		paramsMap.put( "endAt", maxAndroidVersionCode + "" );
		return _getUserPreferences( paramsMap );

	}

	private Map<Long, UserPreferenceRtdb> _getUserPreferences( Map<String, String> paramsMap )
			throws UnexpectedServerException {

		try {
			
			BlobEntry blobEntry = HttpUtil.doGet( _getUserPreferenceDbUrl(), headersMap, paramsMap );
			String jsonStr = new String( blobEntry.getData(), "UTF-8" );
			JsonObject json = new Gson().fromJson( jsonStr, JsonElement.class ).getAsJsonObject();
			
			Map<Long, UserPreferenceRtdb> userPreferenceMap = new HashMap<>();
			for( Entry<String, JsonElement> entry : json.entrySet() )
				userPreferenceMap.put(
						Long.parseLong( entry.getKey() ),
						_getUserPreferenceRtdb( entry.getValue().getAsJsonObject() ) );
			
			return userPreferenceMap;
			
		} catch( UnsupportedEncodingException e ) {
			logger.log( Level.SEVERE, e.getMessage() );
			throw new UnexpectedServerException();
		}

	}

	private String _getUserPreferenceDbUrl() {
		return DATABASE_URL + DATABASE_PREFERENCE_TABLE + ".json";
	}

	private String _getUserPreferenceDbUrl( Long userId ) {
		return DATABASE_URL + DATABASE_PREFERENCE_TABLE + "/" + userId + ".json";
	}

	private String _getPratilipiContentDbUrl( Long pratilipiId ) {
		return DATABASE_URL + DATABASE_PRATILIPI_CONTENT_TABLE + "/" + pratilipiId + ".json";
	}

	private UserPreferenceRtdb _getUserPreferenceRtdb( String jsonStr ) {
		if( jsonStr.equals( "null" ) )
			jsonStr = "{}";
		return _getUserPreferenceRtdb( new Gson().fromJson( jsonStr, JsonElement.class ).getAsJsonObject() );
	}

	private UserPreferenceRtdb _getUserPreferenceRtdb( JsonObject json ) {

		if( json.has( "emailFrequency" ) ) {
			String emailFrequency = json.get( "emailFrequency" ).getAsString();
			if( emailFrequency.equals( "ONCE A DAY" ) ) {
				json.remove( "emailFrequency" );
				json.addProperty( "emailFrequency", EmailFrequency.DAILY.name() );
			} else if( emailFrequency.equals( "ONCE A WEEK" ) ) {
				json.remove( "emailFrequency" );
				json.addProperty( "emailFrequency", EmailFrequency.WEEKLY.name() );
			}
		}

		if( json.has( "notificationSubscriptions" ) ) {
			JsonObject notifSubscriptionsJson = json.get( "notificationSubscriptions" ).getAsJsonObject();
			List<String> invalidTypes = new ArrayList<>();
			for( Entry<String, JsonElement> entry : notifSubscriptionsJson.entrySet() ) {
				try {
					NotificationType.valueOf( entry.getKey() );
				} catch( IllegalArgumentException ex ) {
					invalidTypes.add( entry.getKey() );
				}
			}
			for( String invalidType : invalidTypes )
				notifSubscriptionsJson.remove( invalidType );
		}

		return new Gson().fromJson( json, UserPreferenceRtdbImpl.class );

	}

	public void setPratilipiIndex( Long pratilipiId, JsonObject jsonObject )
			throws UnexpectedServerException {

		JsonObject lastUpdated = new JsonObject();
		lastUpdated.addProperty( ".sv", "timestamp" );
		jsonObject.add( "lastUpdated", lastUpdated );
		HttpUtil.doPut( _getPratilipiContentDbUrl( pratilipiId ), headersMap, jsonObject );

	}

}
