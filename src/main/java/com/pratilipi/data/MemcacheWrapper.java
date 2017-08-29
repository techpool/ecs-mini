package com.pratilipi.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class MemcacheWrapper implements Memcache {

	private final Memcache cacheL1;
	private final Memcache cacheL2;
	
	
	public MemcacheWrapper( Memcache cacheL1, Memcache cacheL2 ) {
		this.cacheL1 = cacheL1;
		this.cacheL2 = cacheL2;
	}

	
	@Override
	public <K, T extends Serializable> T get( K key ) {
		T value = cacheL1.get( key );
		if( value == null ) {
			value = cacheL2.get( key );
			if( value != null )
				cacheL1.put( key, value );
		}
		return value;
	}

	@Override
	public <K, T extends Serializable> Map<K, T> getAll( Collection<K> keys ) {

		Map<K, T> values = cacheL1.getAll( keys );
		
		Collection<K> missingKeys = new LinkedList<>();
		for( K key : keys )
			if( values.get( key ) == null )
				missingKeys.add( key );

		if( missingKeys.size() != 0 ) {
			Map<K, T> moreValues = cacheL2.getAll( missingKeys );
			cacheL1.putAll( moreValues );
			values.putAll( moreValues );
		}
		
		return values;
		
	}

	@Override
	public <K, T extends Serializable> void put( K key, T value ) {
		cacheL1.put( key, value );
		cacheL2.put( key, value );
	}

	@Override
	public <K, T extends Serializable> void put( K key, T value, int expirationDeltaMinutes ) {
		cacheL1.put( key, value, expirationDeltaMinutes );
		cacheL2.put( key, value, expirationDeltaMinutes );
	}

	@Override
	public <K, T extends Serializable> void putAll( Map<K, T> keyValueMap ) {
		cacheL1.putAll( keyValueMap );
		cacheL2.putAll( keyValueMap );
	}

	@Override
	public <K, T extends Serializable> void putAll( Map<K, T> keyValueMap, int expirationDeltaMinutes ) {
		cacheL1.putAll( keyValueMap, expirationDeltaMinutes );
		cacheL2.putAll( keyValueMap, expirationDeltaMinutes );
	}

	@Override
	public <K> void remove( K key ) {
		cacheL1.remove( key );
		cacheL2.remove( key );
	}

	@Override
	public void flush() {
		cacheL1.flush();
		cacheL2.flush();
	}

}
