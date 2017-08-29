package com.pratilipi.data;

import java.util.Map;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.AuthorFilter;
import com.pratilipi.common.util.PratilipiFilter;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.client.UserData;

public interface SearchAccessor {
	
	DataListCursorTuple<Long> searchPratilipi( PratilipiFilter pratilipiFilter, String cursorStr, Integer resultCount );
	
	DataListCursorTuple<Long> searchPratilipi( String searchQuery, PratilipiFilter pratilipiFilter, String cursorStr, Integer resultCount );

	DataListCursorTuple<Long> searchPratilipi( String searchQuery, PratilipiFilter pratilipiFilter, String cursorStr, Integer offset, Integer resultCount );

	void indexPratilipiData( PratilipiData pratilipiData, String keywords ) throws UnexpectedServerException;

	void indexPratilipiDataList( Map<PratilipiData, String> pratilipiDataListKeywordsMap ) throws UnexpectedServerException;

	void deletePratilipiDataIndex( Long pratilipiId );

	
	DataListCursorTuple<Long> searchAuthor( String query, AuthorFilter authorFilter, String cursorStr, Integer offset, Integer resultCount );
	
	void indexAuthorData( AuthorData authorData, UserData userData ) throws UnexpectedServerException;
	
	
	void deleteAuthorDataIndex( Long authorId );
	
}