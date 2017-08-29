package com.pratilipi.common.type;

public enum RequestCookie {
	
	ACCESS_TOKEN ( "access_token" ),
	FONT_SIZE ( "fontSize" ),
	IMAGE_SIZE ( "imageSize" ),
	PAGE_NO ( "pageNo" ),
	USER_NOTIFIED_APP_LAUNCHED ( "USER_NOTIFIED_APP_LAUNCHED" ),
	APP_LAUNCHED_CLICK_COUNT ( "APP_LAUNCHED_CLICKED" ),
	APP_LAUNCHED_CROSS_COUNT ( "APP_LAUNCHED_CROSSED" )
	;
	
	
	private String name;

	
	private RequestCookie( String name ) {
		this.name = name;
	}

	
	public String getName() {
		return name;
	}

}
