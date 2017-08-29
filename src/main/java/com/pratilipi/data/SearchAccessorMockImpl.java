package com.pratilipi.data;

import static com.pratilipi.data.mock.GlobalIndexMock.GLOBAL_INDEX;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.AuthorFilter;
import com.pratilipi.common.util.PratilipiFilter;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.client.UserData;

public class SearchAccessorMockImpl implements SearchAccessor {

	// Constructor
	
	public SearchAccessorMockImpl( String indexName ) {}


	// PRATILIPI Data
	
	@Override
	public DataListCursorTuple<Long> searchPratilipi( PratilipiFilter pratilipiFilter, String cursorStr, Integer resultCount ) {
		return searchPratilipi( null, pratilipiFilter, cursorStr, null, resultCount );
	}
		
	@Override
	public DataListCursorTuple<Long> searchPratilipi( String query, PratilipiFilter pratilipiFilter, String cursorStr, Integer resultCount ) {
		return searchPratilipi( query, pratilipiFilter, cursorStr, null, resultCount );
	}
	
	@Override
	public DataListCursorTuple<Long> searchPratilipi( String query, PratilipiFilter pratilipiFilter, String cursorStr, Integer offset, Integer resultCount ) {
		
		List<Long> pratilipiIdList = new LinkedList<>();
		
		for( Object object : GLOBAL_INDEX ) {
			if( object.getClass() != PratilipiData.class )
				continue;

			PratilipiData pratilipiData = (PratilipiData) object;
			
			if( pratilipiFilter.getLanguage() != null
					&& !pratilipiFilter.getLanguage().equals( pratilipiData.getLanguage() ) )
				continue;
			
			if( pratilipiFilter.getAuthorId() != null
					&& !pratilipiFilter.getAuthorId().equals( pratilipiData.getAuthorId() ) )
				continue;
			
			if( pratilipiFilter.getType() != null
					&& !pratilipiFilter.getType().equals( pratilipiData.getType() ) )
				continue;
			
			pratilipiIdList.add( ( (PratilipiData) object ).getId() );
		}
		
		return new DataListCursorTuple<Long>( pratilipiIdList, null );
	}

	@Override
	public void indexPratilipiData( PratilipiData pratilipiData, String keywords ) throws UnexpectedServerException {
		GLOBAL_INDEX.add( pratilipiData );
	}

	@Override
	public void indexPratilipiDataList( Map<PratilipiData, String> pratilipiDataList ) throws UnexpectedServerException {
		GLOBAL_INDEX.addAll( pratilipiDataList.keySet() );
	}

	@Override
	public void deletePratilipiDataIndex( Long pratilipiId ) {
		for( Object object : GLOBAL_INDEX ) {
			if( object.getClass() == PratilipiData.class ) {
				if( ( (PratilipiData) object ).getId() == pratilipiId ) {
					GLOBAL_INDEX.remove( object );
					break;
				}
			}
		}
	}

	
	// AUTHOR Data
	
	@Override
	public DataListCursorTuple<Long> searchAuthor( String query, AuthorFilter authorFilter, String cursorStr, Integer offset, Integer resultCount ) {
		
		List<Long> authorIdList = new LinkedList<>();
		
		// TODO: Implementation
		
		return new DataListCursorTuple<Long>( authorIdList, null );
	}

	@Override
	public void indexAuthorData( AuthorData authorData, UserData userData ) throws UnexpectedServerException {
		// TODO: Implementation
	}

	@Override
	public void deleteAuthorDataIndex( Long authorId ) {
		for( Object object : GLOBAL_INDEX ) {
			if( object.getClass() == AuthorData.class ) {
				if( ( (AuthorData) object ).getId() == authorId ) {
					GLOBAL_INDEX.remove( object );
					break;
				}
			}
		}
	}
	
}