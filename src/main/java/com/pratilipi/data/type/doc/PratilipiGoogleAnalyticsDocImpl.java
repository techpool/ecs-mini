package com.pratilipi.data.type.doc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pratilipi.data.type.PratilipiGoogleAnalyticsDoc;

public class PratilipiGoogleAnalyticsDocImpl implements PratilipiGoogleAnalyticsDoc {
	
	public static class YearlyPageViews {
		//          Day      Count
		private Map<Integer, Integer> JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC;
	};
	
	
	private Map<Integer, YearlyPageViews> pageViews = new HashMap<>();
	private Map<Integer, YearlyPageViews> readPageViews = new HashMap<>();
	private Long lastUpdated;
	@Deprecated
	private Long lastUpdatedMillis;
	

	@Override
	public int getPageViews( int year, int month, int day ) {
		return _getPageViews( pageViews, year, month, day );
	}
	
	@Override
	public long getTotalPageViews() {
		return _getTotalPageViews( pageViews );
	}

	@Override
	public void setPageViews( int year, int month, int day, int count ) {
		_setPageViews( pageViews, year, month, day, count );
		lastUpdated = new Date().getTime();
	}

	
	@Override
	public int getReadPageViews( int year, int month, int day ) {
		return _getPageViews( readPageViews, year, month, day);
	}
	
	public long getTotalReadPageViews() {
		return _getTotalPageViews( readPageViews );
	}

	@Override
	public void setReadPageViews( int year, int month, int day, int count ) {
		_setPageViews( readPageViews, year, month, day, count );
		lastUpdated = new Date().getTime();
	}
	

	private int _getPageViews( Map<Integer, YearlyPageViews> yearlyPageViewsMap, int year, int month, int day ) {
		YearlyPageViews yearlyPageViews = yearlyPageViewsMap.get( year );
		if( yearlyPageViews == null )
			return 0;
		
		Map<Integer, Integer> monthlyPageViews = null;
		switch( month ) {
			case 1: monthlyPageViews = yearlyPageViews.JAN; break;
			case 2: monthlyPageViews = yearlyPageViews.FEB; break;
			case 3: monthlyPageViews = yearlyPageViews.MAR; break;
			case 4: monthlyPageViews = yearlyPageViews.APR; break;
			case 5: monthlyPageViews = yearlyPageViews.MAY; break;
			case 6: monthlyPageViews = yearlyPageViews.JUN; break;
			case 7: monthlyPageViews = yearlyPageViews.JUL; break;
			case 8: monthlyPageViews = yearlyPageViews.AUG; break;
			case 9: monthlyPageViews = yearlyPageViews.SEP; break;
			case 10: monthlyPageViews = yearlyPageViews.OCT; break;
			case 11: monthlyPageViews = yearlyPageViews.NOV; break;
			case 12: monthlyPageViews = yearlyPageViews.DEC; break;
		}
		if( monthlyPageViews == null )
			return 0;
		
		return monthlyPageViews.get( day ) == null ? 0 : monthlyPageViews.get( day );
	}
	
	private long _getTotalPageViews( Map<Integer, YearlyPageViews> yearlyPageViewsMap ) {
		long totalCount = 0L;
		for( YearlyPageViews year : yearlyPageViewsMap.values() ) {
			if( year.JAN != null )
				for( Integer count : year.JAN.values() )
					totalCount += count;
			if( year.FEB != null )
				for( Integer count : year.FEB.values() )
					totalCount += count;
			if( year.MAR != null )
				for( Integer count : year.MAR.values() )
					totalCount += count;
			if( year.APR != null )
				for( Integer count : year.APR.values() )
					totalCount += count;
			if( year.MAY != null )
				for( Integer count : year.MAY.values() )
					totalCount += count;
			if( year.JUN != null )
				for( Integer count : year.JUN.values() )
					totalCount += count;
			if( year.JUL != null )
				for( Integer count : year.JUL.values() )
					totalCount += count;
			if( year.AUG != null )
				for( Integer count : year.AUG.values() )
					totalCount += count;
			if( year.SEP != null )
				for( Integer count : year.SEP.values() )
					totalCount += count;
			if( year.OCT != null )
				for( Integer count : year.OCT.values() )
					totalCount += count;
			if( year.NOV != null )
				for( Integer count : year.NOV.values() )
					totalCount += count;
			if( year.DEC != null )
				for( Integer count : year.DEC.values() )
					totalCount += count;
		}
		return totalCount;
	}
	
	private void _setPageViews( Map<Integer, YearlyPageViews> yearlyPageViewsMap, int year, int month, int day, int count ) {
		
		YearlyPageViews readYear = yearlyPageViewsMap.get( year );
		if( readYear == null ) {
			readYear = new YearlyPageViews();
			yearlyPageViewsMap.put( year, readYear );
		}

		Map<Integer, Integer> dayMap = null;
		switch ( month ) {
			case 1:
				if( readYear.JAN == null )
					readYear.JAN = new HashMap<>();
				dayMap = readYear.JAN; break;
			case 2:
				if( readYear.FEB == null )
					readYear.FEB = new HashMap<>();
				dayMap = readYear.FEB; break;
			case 3:
				if( readYear.MAR == null )
					readYear.MAR = new HashMap<>();
				dayMap = readYear.MAR; break;
			case 4:
				if( readYear.APR == null )
					readYear.APR = new HashMap<>();
				dayMap = readYear.APR; break;
			case 5:
				if( readYear.MAY == null )
					readYear.MAY = new HashMap<>();
				dayMap = readYear.MAY; break;
			case 6:
				if( readYear.JUN == null )
					readYear.JUN = new HashMap<>();
				dayMap = readYear.JUN; break;
			case 7:
				if( readYear.JUL == null )
					readYear.JUL = new HashMap<>();
				dayMap = readYear.JUL; break;
			case 8:
				if( readYear.AUG == null )
					readYear.AUG = new HashMap<>();
				dayMap = readYear.AUG; break;
			case 9:
				if( readYear.SEP == null )
					readYear.SEP = new HashMap<>();
				dayMap = readYear.SEP; break;
			case 10:
				if( readYear.OCT == null )
					readYear.OCT = new HashMap<>();
				dayMap = readYear.OCT; break;
			case 11:
				if( readYear.NOV == null )
					readYear.NOV = new HashMap<>();
				dayMap = readYear.NOV; break;
			case 12:
				if( readYear.DEC == null )
					readYear.DEC = new HashMap<>();
				dayMap = readYear.DEC; break;
		}
		
		dayMap.put( day, count );
		
	}
	
	
	@Override
	public Date getLastUpdated() {
		if( lastUpdated == null ) {
			lastUpdated = lastUpdatedMillis;
			lastUpdatedMillis = null;
		}
		return lastUpdated == null ? new Date( 0 ) : new Date( lastUpdated );
	}
	
}
