package com.pratilipi.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface Memcache {

	<K, T extends Serializable> T get( K key );

	<K, T extends Serializable> Map<K, T> getAll( Collection<K> keys );

	<K, T extends Serializable> void put( K key, T value );

	<K, T extends Serializable> void put( K key, T value, int expirationDeltaMinutes );

	<K, T extends Serializable> void putAll( Map<K, T> keyValueMap );

	<K, T extends Serializable> void putAll( Map<K, T> keyValueMap, int expirationDeltaMinutes );

	<K> void remove( K key );

	void flush();

}
