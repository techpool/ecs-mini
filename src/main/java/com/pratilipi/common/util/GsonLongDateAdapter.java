package com.pratilipi.common.util;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonLongDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
	
	@Override
	public synchronized JsonElement serialize( Date date, Type type, JsonSerializationContext jsonSerializationContext ) {
		return new JsonPrimitive( date.getTime() );
	}
	
	@Override
	public synchronized Date deserialize( JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext ) {
		return new Date( jsonElement.getAsLong() );
	}
	
}
