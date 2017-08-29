package com.pratilipi.data.type.gae;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;
import com.pratilipi.data.type.AppProperty;

@Cache
@Entity( name = "APP_PROPERTY" )
public class AppPropertyEntity implements AppProperty {

	@Id
	private String APP_PROPERTY_ID;
	
	@Serialize
	private Object VALUE;


	public AppPropertyEntity() {}
	
	public AppPropertyEntity( String id ) {
		this.APP_PROPERTY_ID = id;
	}

	
	@Override
	public String getId() {
		return APP_PROPERTY_ID;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.APP_PROPERTY_ID = key.getName();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue() {
		return (T) VALUE;
	}

	@Override
	public <T> void setValue( T value ) {
		this.VALUE = value;
	}
	
}
