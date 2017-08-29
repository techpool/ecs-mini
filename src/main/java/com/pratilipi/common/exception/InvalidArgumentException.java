package com.pratilipi.common.exception;

import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class InvalidArgumentException extends Exception {

	@SuppressWarnings("unused")
	private InvalidArgumentException() {}
	
	public InvalidArgumentException( String msg ) {
		super( "{\"message\":\"" + msg + "\"}" );
	}

	public InvalidArgumentException( JsonObject errorMessages ) {
		super( errorMessages.toString() );
	}
	
}
