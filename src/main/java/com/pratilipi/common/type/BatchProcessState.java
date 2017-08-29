package com.pratilipi.common.type;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public enum BatchProcessState {
	
	INIT								( null,					null,				TimeUnit.MINUTES.toMillis( 1 ) ),
	
	GET_USER_IDS_BY_AUTHOR_FILTER		( "authorFilter",		"userIds",			TimeUnit.MINUTES.toMillis( 15 ) ),
	
	CREATE_NOTIFICATIONS_FOR_USER_IDS	( "userIds",			"notificationIds",	TimeUnit.MINUTES.toMillis( 60 ) ),
	
	VALIDATE_NOTIFICATION_COUNT			( "notificationIds",	"null",				TimeUnit.MINUTES.toMillis( 15 ) ),
	
	COMPLETED							( null,					null,				TimeUnit.MINUTES.toMillis( 1 ) );
	
	
	private String inputName;
	private String outputName;
	private Long timeOutMillis;
	
	private BatchProcessState( String inputName, String outputName, long timeOutMillis ) {
		this.inputName = inputName;
		this.outputName = outputName;
		this.timeOutMillis = timeOutMillis;
	}
	
	
	public String getInputName() {
		return inputName;
	}
	
	public String getOutputName() {
		return outputName;
	}
	
	public boolean isTimedOut( Date lastUpdated ) {
		return new Date().getTime() - lastUpdated.getTime() > timeOutMillis;
	}
	
}