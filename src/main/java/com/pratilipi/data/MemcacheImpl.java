package com.pratilipi.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemcacheImpl implements Memcache {

	private static final Logger logger =
			Logger.getLogger( MemcacheImpl.class.getName() );

	private final int cacheSize = 256;
	@SuppressWarnings("serial")
	private final Map<Object, Object> cache =
			new LinkedHashMap<Object, Object>( 16, 0.75f, true ) {
	
		@Override
		protected boolean removeEldestEntry( Map.Entry<Object, Object> eldest ) {
			return size() >= cacheSize;
		}		
	
	};

	
	@SuppressWarnings("unchecked")
	@Override
	public <K, T extends Serializable> T get( K key ) {
		T value = (T) cache.get(key);
		if( value == null )
			logger.log( Level.INFO, "Cache Miss: " + key );
//		else
//			logger.log( Level.INFO, "Cache Hit: " + key );
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, T extends Serializable> Map<K, T> getAll( Collection<K> keys ) {
		Map<K, T> keyValueMap = new HashMap<K, T>();
		for( K key : keys )
			keyValueMap.put( key, (T) cache.get(key) );
		return keyValueMap;
	}
	
	@Override
	public <K, T extends Serializable> void put( K key, T value ) {
		put( key, value, 0 );
	}

	@Override
	public <K, T extends Serializable> void put( K key, T value, int expirationDeltaMinutes ) {
		cache.put( key, value );
	}

	@Override
	public <K, T extends Serializable> void putAll( Map<K, T> keyValueMap ) {
		putAll( keyValueMap, 0 );
	}

	@Override
	public <K, T extends Serializable> void putAll( Map<K, T> keyValueMap, int expirationDeltaMinutes ) {
		for( Entry<K, T> entry : keyValueMap.entrySet() )
			cache.put( entry.getKey(), entry.getValue() );
	}

	@Override
	public <K> void remove( K key ) {
		cache.remove( key );
	}

	@Override
	public void flush() {
		cache.clear();
	}

}
