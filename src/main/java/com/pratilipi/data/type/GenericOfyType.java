package com.pratilipi.data.type;

import com.googlecode.objectify.Key;

public interface GenericOfyType {

	<T> Key<T> getKey();
	
	<T> void setKey( Key<T> key );
	
}
