package com.pratilipi.common.exception;

@SuppressWarnings("serial")
public class UnexpectedServerException extends Exception {

	public UnexpectedServerException() {
		this( "Some exception occurred at server. Please try again." );
	}

	public UnexpectedServerException( String msg ) {
		super( "{\"message\":\"" + msg + "\"}" );
	}

}
