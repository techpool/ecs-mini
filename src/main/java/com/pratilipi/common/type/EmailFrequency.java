package com.pratilipi.common.type;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public enum EmailFrequency {

	IMMEDIATELY	( 0 ),
	DAILY		( TimeUnit.DAYS.toMillis( 1 ) ),
	WEEKLY		( TimeUnit.DAYS.toMillis( 7 ) ),
	MONTHLY		( TimeUnit.DAYS.toMillis( 30 ) ),
	NEVER		( -1 ),
	;
	
	
	private long delayMillis;
	
	private EmailFrequency( long delayMillis ) {
		this.delayMillis = delayMillis;
	}

	
	public Date getNextSchedule( Date lastEmailed ) {

		if( this == NEVER )
			return null;

		if( this == IMMEDIATELY )
			return new Date();

		while( lastEmailed.before( new Date() ) )
			lastEmailed = new Date( lastEmailed.getTime() + delayMillis );

		return lastEmailed;

	}

}
