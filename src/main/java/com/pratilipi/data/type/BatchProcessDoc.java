package com.pratilipi.data.type;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;

public interface BatchProcessDoc {
	
	void setData( String name, JsonElement data );
	
	void setData( String name, Object data );
	
	<T> T getData( String name, Type type );
	
}
