package com.pratilipi.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.type.UserPreferenceRtdb;


public class RtdbAccessorWithMemcache implements RtdbAccessor {

	private static final int expirationDeltaMinutes = 5;
	
	private final RtdbAccessor rtdbAccessor;
	private final Memcache memcache;


	public RtdbAccessorWithMemcache( RtdbAccessor rtdbAccessor, Memcache memcache ) {
		this.rtdbAccessor = rtdbAccessor;
		this.memcache = memcache;
	}


	// PREFERENCE Table

	@Override
	public UserPreferenceRtdb getUserPreference( Long userId )
			throws UnexpectedServerException {

		if( userId == null )
			return null;
		
		UserPreferenceRtdb userPref = memcache.get( _getUserPreferenceMemcacheId( userId ) );
		if( userPref == null ) {
			userPref = rtdbAccessor.getUserPreference( userId );
			memcache.put( _getUserPreferenceMemcacheId( userId ), userPref, expirationDeltaMinutes );
		}
		
		return userPref;
	}

	@Override
	public Map<Long, UserPreferenceRtdb> getUserPreferences( Collection<Long> userIds )
			throws UnexpectedServerException {

		if( userIds == null || userIds.isEmpty() )
			return new HashMap<>();

		userIds = new HashSet<>( userIds );
		
		Set<String> memcacheIds = new HashSet<>( userIds.size() );
		for( Long userId : userIds )
			memcacheIds.add( _getUserPreferenceMemcacheId( userId ) );
		Map<String, UserPreferenceRtdb> userPrefs = memcache.getAll( memcacheIds );

		Map<Long, UserPreferenceRtdb> userPreferences = new HashMap<>( userIds.size() );
		List<Long> missingUserIdList = new ArrayList<>();
		for( Long userId : userIds ) {
			UserPreferenceRtdb userPref = userPrefs.get( _getUserPreferenceMemcacheId( userId ) );
			if( userPref != null )
				userPreferences.put( userId, userPref );
			else
				missingUserIdList.add( userId );
		}
		
		if( ! missingUserIdList.isEmpty() ) {
			Map<Long, UserPreferenceRtdb> missingUserPreferences = rtdbAccessor.getUserPreferences( missingUserIdList );
			userPreferences.putAll( missingUserPreferences );
			Map<String, UserPreferenceRtdb> missingUserPrefs = new HashMap<>( missingUserIdList.size() );
			for( Long userId : missingUserIdList )
				missingUserPrefs.put( _getUserPreferenceMemcacheId( userId ), missingUserPreferences.get( userId ) );
			memcache.putAll( missingUserPrefs, expirationDeltaMinutes );
		}
		
		return userPreferences;

	}

	@Override
	public Map<Long, UserPreferenceRtdb> getUserPreferences( Date minLastUpdated ) 
			throws UnexpectedServerException {

		return rtdbAccessor.getUserPreferences( minLastUpdated );

	}

	@Override
	public Map<Long, UserPreferenceRtdb> getUserPreferences( Integer maxAndroidVersionCode ) 
			throws UnexpectedServerException {

		return rtdbAccessor.getUserPreferences( maxAndroidVersionCode );

	}

	private String _getUserPreferenceMemcacheId( Long userId ) {
		return "Firebase.PREFERENCE-" + userId;
	}


	public void setPratilipiIndex( Long pratilipiId, JsonObject jsonObject )
			throws UnexpectedServerException {

		rtdbAccessor.setPratilipiIndex( pratilipiId, jsonObject );

	}

}
