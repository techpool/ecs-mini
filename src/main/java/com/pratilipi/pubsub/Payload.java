package com.pratilipi.pubsub;

import com.google.gson.Gson;

public abstract class Payload {
	
	public String toString() {
		return new Gson().toJson( this );
	}
	
}
