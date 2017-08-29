package com.pratilipi.common.exception;

@SuppressWarnings("serial")
public class InsufficientAccessException extends Exception {

	public InsufficientAccessException() {
		this( "Insufficient privilege for this action." );
	}

	public InsufficientAccessException( String msg ) {
		super( "{\"message\":\"" + msg + "\"}" );
	}

}
