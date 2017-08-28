package com.pratilipi.data.type;

import java.util.Date;

public interface PratilipiGoogleAnalyticsDoc {
	
	int getPageViews( int year, int month, int day );

	long getTotalPageViews();
	
	void setPageViews( int year, int month, int day, int count );

	
	int getReadPageViews( int year, int month, int day );
	
	long getTotalReadPageViews();

	void setReadPageViews( int year, int month, int day, int count );
	
	
	Date getLastUpdated();
	
}
