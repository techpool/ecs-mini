package com.pratilipi.data.type.doc;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pratilipi.data.type.BatchProcessDoc;

public class BatchProcessDocImpl implements BatchProcessDoc {
	
	private transient final Gson gson = new Gson();
	
	
	private JsonObject data;
	@SuppressWarnings("unused")
	private Date lastUpdated;
	

	@Override
	public void setData( String name, JsonElement data ) {
		if( this.data == null )
			this.data = new JsonObject();
		this.data.add( name, data );
		this.lastUpdated = new Date();
	}
	
	@Override
	public void setData( String name, Object data ) {
		if( this.data == null )
			this.data = new JsonObject();
		this.data.add( name, gson.toJsonTree( data ) );
		this.lastUpdated = new Date();
	}
	
	public <T> T getData( String name, Type type ) {
		if( data == null || data.get( name ) == null )
			return null;
		return gson.fromJson( data.get( name ), type );
	}
	
}