package com.pratilipi.common.type;

public enum RequestHeader {
	
	ACCESS_TOKEN ( "AccessToken" ),
	;
	
	
	private String name;

	
	private RequestHeader( String name ) {
		this.name = name;
	}

	
	public String getName() {
		return name;
	}

}
