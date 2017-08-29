package com.pratilipi.common.type;

public enum RequestParameter {
	
	API_VERSION( "_apiVer" ),
	
	ACCESS_TOKEN ( "accessToken" ),
	LIST_PAGE_NUMBER( "page" ),
	READER_PAGE_NUMBER( "pageNo" ),
	CONTENT_ID( "id" ),
	SEARCH_QUERY( "q" ),
	PRATILIPI_ID( "pId" ),
	PRATILIPI_REVIEW( "review" ),
	PASSWORD_RESET_EMAIL_EMAIL( "email" ),
	PASSWORD_RESET_EMAIL_TOKEN( "token" ),
	PASSWORD_RESET_EMAIL_FLAG( "passwordReset" ),
	VERIFY_EMAIL_FLAG( "verifyUser" ),
	AUTHOR_ID( "aId" ),
	USER_ID( "uId" ),
	PRATILIPI_STATE( "state" ),
	RESULT_COUNT( "count" ),
	NOTIFICATION_ID( "notifId" ),
	SITEMAP_TYPE( "type" ),
	SITEMAP_CURSOR( "cursor" )
	;
	
	
	private String name;

	
	private RequestParameter( String name ) {
		this.name = name;
	}

	
	public String getName() {
		return name;
	}

}
