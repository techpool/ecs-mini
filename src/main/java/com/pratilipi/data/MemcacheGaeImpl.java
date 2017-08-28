package com.pratilipi.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

import com.google.appengine.api.memcache.InvalidValueException;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.stdimpl.GCacheException;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

public class MemcacheGaeImpl implements Memcache {

	private static final Logger logger =
			Logger.getLogger( MemcacheGaeImpl.class.getName() );


	@SuppressWarnings("unchecked")
	@Override
	public <K, T extends Serializable> T get( K key ) {
		try {
			Cache cache = CacheManager.getInstance().getCacheFactory()
					.createCache( Collections.emptyMap() );
			T value = (T) cache.get(key);
			if( value == null )
				logger.log( Level.INFO, "Cache Miss: " + key );
//			else
//				logger.log( Level.INFO, "Cache Hit: " + key );
			return value;
		} catch( InvalidValueException | ClassCastException e ) {
			logger.log( Level.SEVERE, "Failed to typecaste cached value to required type.", e );
			return null;
		} catch( CacheException e ) {
			logger.log( Level.SEVERE, "Failed to create cache instance.", e );
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, T extends Serializable> Map<K, T> getAll( Collection<K> keys ) {
		try {
			Cache cache = CacheManager.getInstance().getCacheFactory()
					.createCache( Collections.emptyMap() );
			return cache.getAll( keys );
		} catch( InvalidValueException e ) {
			logger.log( Level.SEVERE, "Failed to typecaste cached value to required type.", e );
			return null;
		} catch( CacheException e ) {
			logger.log( Level.SEVERE, "Failed to create cache instance.", e );
			return null;
		}
	}

	@Override
	public <K, T extends Serializable> void put( K key, T value ) {
		put( key, value, 15 ); // Hack: 15 min cache out for all resources
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <K, T extends Serializable> void put( K key, T value, int expirationDeltaMinutes ) {

		Map props = new HashMap();
		if( expirationDeltaMinutes > 0 )
			props.put( GCacheFactory.EXPIRATION_DELTA, expirationDeltaMinutes * 60 );

		for( int i = 0; i < 5; i++ ) {

			if( i > 0 ) {
				try {
					long sleepMillis = (long) Math.pow( 2, i - 1 ) * 100;
					logger.log( Level.INFO, "Retrying in " + sleepMillis + " milliseconds ..." );
					Thread.sleep( sleepMillis );
				} catch( InterruptedException ex ) {
					logger.log( Level.SEVERE, "Thread sleep interrupted.", ex );
				}
			}
			
			try {
				Cache cache = CacheManager.getInstance().getCacheFactory()
						.createCache( props );
				cache.put( key, value );
				break;
			} catch( GCacheException | MemcacheServiceException ex ) {
				logger.log( Level.SEVERE, "Failed to update value.", ex );
			} catch( CacheException ex ) {
				logger.log( Level.SEVERE, "Failed to create cache instance.", ex );
			}
			
		}
		
	}

	@Override
	public <K, T extends Serializable> void putAll( Map<K, T> keyValueMap ) {
		putAll( keyValueMap, 0 );
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <K, T extends Serializable> void putAll( Map<K, T> keyValueMap, int expirationDeltaMinutes ) {

		Map props = new HashMap();
		if( expirationDeltaMinutes > 0 )
			props.put( GCacheFactory.EXPIRATION_DELTA, expirationDeltaMinutes * 60 );

		for( int i = 0; i < 5; i++ ) {

			if( i > 0 ) {
				try {
					long sleepMillis = (long) Math.pow( 2, i - 1 ) * 100;
					logger.log( Level.INFO, "Retrying in " + sleepMillis + " milliseconds ..." );
					Thread.sleep( sleepMillis );
				} catch( InterruptedException ex ) {
					logger.log( Level.SEVERE, "Thread sleep interrupted.", ex );
				}
			}
			
			try {
				Cache cache = CacheManager.getInstance().getCacheFactory()
						.createCache( props );
				cache.putAll( keyValueMap );
				break;
			} catch( GCacheException | MemcacheServiceException ex ) {
				logger.log( Level.SEVERE, "Failed to update one or more values.", ex );
			} catch (CacheException ex) {
				logger.log( Level.SEVERE, "Failed to create cache instance.", ex );
			}
		
		}

	}

	@Override
	public <K> void remove( K key ) {
		try {
			Cache cache = CacheManager.getInstance().getCacheFactory()
					.createCache( Collections.emptyMap() );
			cache.remove( key );
		} catch( CacheException ex ) {
			logger.log( Level.SEVERE, "Failed to create cache instance.", ex);
		}
	}

	@Override
	public void flush() {
		try {
			Cache cache = CacheManager.getInstance().getCacheFactory()
					.createCache( Collections.emptyMap() );
			cache.clear();
		} catch( CacheException ex ) {
			logger.log( Level.SEVERE, "Failed to create cache instance.", ex);
		}
	}

}
