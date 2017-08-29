package com.pratilipi.taskqueue;

public interface Task {

	Task setUrl( String url );
	
	Task addParam( String paramName, String value );

}
