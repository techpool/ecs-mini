package com.pratilipi.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.MatchScorer;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;
import com.google.appengine.api.search.StatusCode;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.AuthorFilter;
import com.pratilipi.common.util.PratilipiFilter;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.client.UserData;

public class SearchAccessorGaeImpl implements SearchAccessor {

	private static final Logger logger =
			Logger.getLogger( SearchAccessorGaeImpl.class.getName() );

	private static final SearchService searchService =
			SearchServiceFactory.getSearchService();

	private final Index searchIndex;

	
	// Constructor
	
	public SearchAccessorGaeImpl( String indexName ) {
		searchIndex = searchService.getIndex( IndexSpec.newBuilder().setName( indexName ) );
	}


	// Helper Methods
	
	public Results<ScoredDocument> search(
			String searchQuery, SortOptions sortOptions, String cursorStr, Integer offset,
			Integer resultCount, String... fieldsToReturn ) {
		
		if( sortOptions == null ) {
			SortOptions.Builder sortOptionsBuilder = SortOptions.newBuilder();
			sortOptionsBuilder.setMatchScorer( MatchScorer.newBuilder() );
			sortOptionsBuilder.addSortExpression( SortExpression.newBuilder()
					.setExpression( SortExpression.SCORE_FIELD_NAME )
					.setDirection( SortExpression.SortDirection.DESCENDING )
					.setDefaultValueNumeric( 0.0 ) );
			sortOptions = sortOptionsBuilder.setLimit( 1000 ).build(); // Max allowed: 10000
		}
		
		QueryOptions.Builder queryOptionsBuilder = QueryOptions.newBuilder()
				.setSortOptions( sortOptions )
				.setLimit( resultCount == null ? 1000 : resultCount )
				.setNumberFoundAccuracy( 1000 ) // Max allowed: 10000
				.setFieldsToReturn( fieldsToReturn );
		
		if( cursorStr != null && ! cursorStr.trim().isEmpty() )
			queryOptionsBuilder.setCursor( Cursor.newBuilder().build( cursorStr ) );
		else if( offset != null && offset > 0 )
			queryOptionsBuilder.setOffset( offset );
		else
			queryOptionsBuilder.setCursor( Cursor.newBuilder() ); // Without this, query will not return a cursor.
		
		Query query = Query.newBuilder()
				.setOptions( queryOptionsBuilder )
				.build( searchQuery );

		logger.log( Level.INFO, "Search Query: " + query.toString() );
		
	    return searchIndex.search( query );
	}

	protected void indexDocument( Document document ) throws UnexpectedServerException {
		List<Document> documentList = new ArrayList<>( 1 );
		documentList.add( document );
		indexDocumentList( documentList );
	}
	
	protected void indexDocumentList( List<Document> documentList ) throws UnexpectedServerException {
		int batchSize = 20; // Max allowed is 200
		for( int i = 0; i < documentList.size(); i = i + batchSize ) {
			try {
				searchIndex.put( documentList.subList( i, i + batchSize > documentList.size() ? documentList.size() : i + batchSize ) );
			} catch( PutException e ) {
				if( StatusCode.TRANSIENT_ERROR.equals( e.getOperationResult().getCode() ) ) {
					logger.log( Level.WARNING, "Failed to update search index. Retrying ...", e );
					try {
						Thread.sleep( 100 );
						i = i - batchSize;
						continue;
					} catch( InterruptedException ex ) {
						// Do nothing
					}
				} else {
					logger.log( Level.SEVERE, "Failed to update search index.", e );
					throw new UnexpectedServerException();
				}
			}
		}
	}

	protected void deleteIndex( String docId ) {
		searchIndex.delete( docId );
	}

	
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
		
		SortOptions.Builder sortOptionsBuilder = SortOptions.newBuilder();

		if( pratilipiFilter.getOrderByReadCount() != null ) {
			sortOptionsBuilder.addSortExpression( SortExpression.newBuilder()
					.setExpression( "readCount" )
					.setDirection( pratilipiFilter.getOrderByReadCount()
							? SortExpression.SortDirection.ASCENDING
							: SortExpression.SortDirection.DESCENDING )
					.setDefaultValueNumeric( 0.0 ) );

		} else if( query != null && ! query.isEmpty() ) {
			sortOptionsBuilder.setMatchScorer( MatchScorer.newBuilder() );
			sortOptionsBuilder.addSortExpression( SortExpression.newBuilder()
					.setExpression( SortExpression.SCORE_FIELD_NAME )
					.setDirection( SortExpression.SortDirection.DESCENDING )
					.setDefaultValueNumeric( 0.0 ) );
			sortOptionsBuilder.addSortExpression( SortExpression.newBuilder()
					.setExpression( "relevance" )
					.setDirection( SortExpression.SortDirection.DESCENDING )
					.setDefaultValueNumeric( 0.0 ) );
			
		} else {
			sortOptionsBuilder.addSortExpression( SortExpression.newBuilder()
					.setExpression( "relevance" )
					.setDirection( SortExpression.SortDirection.DESCENDING )
					.setDefaultValueNumeric( 0.0 ) );
		}
		
		SortOptions sortOptions = sortOptionsBuilder.setLimit( 1000 ).build();

		
		String searchQuery = pratilipiFilter.getType() == null
				? "docType:Pratilipi"
				: "docType:Pratilipi-" + pratilipiFilter.getType().getName();

		if( pratilipiFilter.getLanguage() != null )
			searchQuery = searchQuery + " AND language:" + pratilipiFilter.getLanguage().getNameEn();

		if( pratilipiFilter.getAuthorId() != null )
			searchQuery = searchQuery + " AND author:" + pratilipiFilter.getAuthorId();

		if( query != null && ! query.isEmpty() )
			searchQuery = "( " + query + " ) AND " + searchQuery;

		
		Results<ScoredDocument> result = search( searchQuery, sortOptions, cursorStr, offset, resultCount, "docId" );
		
		List<Long> pratilipiIdList = new ArrayList<>( result.getNumberReturned() ); 
		for( ScoredDocument document : result )
			pratilipiIdList.add( Long.parseLong( document.getOnlyField( "docId" ).getAtom() ) );
		
		Cursor cursor = resultCount == null || pratilipiIdList.size() < resultCount ? null : result.getCursor();
		
		return new DataListCursorTuple<Long>(
				pratilipiIdList,
				cursor == null ? null : cursor.toWebSafeString(),
				result.getNumberFound() );
	}

	@Override
	public void indexPratilipiData( PratilipiData pratilipiData, String keywords ) throws UnexpectedServerException {
		indexDocument( createDocument( pratilipiData, keywords ) );
	}

	@Override
	public void indexPratilipiDataList( Map<PratilipiData, String> pratilipiDataKeywordsMap ) throws UnexpectedServerException {
		List<Document> documentList = new ArrayList<>( pratilipiDataKeywordsMap.size() );
		for( Map.Entry<PratilipiData, String> dataEntry : pratilipiDataKeywordsMap.entrySet() )
			documentList.add( createDocument( dataEntry.getKey(), dataEntry.getValue() ) );
		indexDocumentList( documentList );
	}

	@Override
	public void deletePratilipiDataIndex( Long pratilipiId ) {
		deleteIndex( "PratilipiData:" + pratilipiId );
	}

	private Document createDocument( PratilipiData pratilipiData, String keywords ) {

		String docId = "PratilipiData:" + pratilipiData.getId();
		
		Builder docBuilder = Document.newBuilder()
				.setId( docId )
				.addField( Field.newBuilder().setName( "docId" ).setAtom( pratilipiData.getId().toString() ) )
				.addField( Field.newBuilder().setName( "docType" ).setAtom( "Pratilipi" ) )

				// 2x weightage to Title
				.addField( Field.newBuilder().setName( "title" ).setText( pratilipiData.getTitle() ) )
				.addField( Field.newBuilder().setName( "title" ).setText( pratilipiData.getTitle() ) )
				.addField( Field.newBuilder().setName( "title" ).setText( pratilipiData.getTitleEn() ) )
				.addField( Field.newBuilder().setName( "title" ).setText( pratilipiData.getTitleEn() ) )

				 // 4x weightage to Language
				.addField( Field.newBuilder().setName( "language" ).setText( pratilipiData.getLanguage().getName() ) )
				.addField( Field.newBuilder().setName( "language" ).setText( pratilipiData.getLanguage().getName() ) )
				.addField( Field.newBuilder().setName( "language" ).setText( pratilipiData.getLanguage().getName() ) )
				.addField( Field.newBuilder().setName( "language" ).setText( pratilipiData.getLanguage().getName() ) )
				.addField( Field.newBuilder().setName( "language" ).setText( pratilipiData.getLanguage().getNameEn() ) )
				.addField( Field.newBuilder().setName( "language" ).setText( pratilipiData.getLanguage().getNameEn() ) )
				.addField( Field.newBuilder().setName( "language" ).setText( pratilipiData.getLanguage().getNameEn() ) )
				.addField( Field.newBuilder().setName( "language" ).setText( pratilipiData.getLanguage().getNameEn() ) )
				
				.addField( Field.newBuilder().setName( "summary" ).setHTML( pratilipiData.getSummary() ) );

		if( pratilipiData.getType() != null ) {
			docBuilder.addField( Field.newBuilder().setName( "docType" ).setAtom( "Pratilipi-" + pratilipiData.getType().getName() ) )
					// 4x weightage to PratilipiType
					.addField( Field.newBuilder().setName( "keyword" ).setAtom( pratilipiData.getType().getName() ) )
					.addField( Field.newBuilder().setName( "keyword" ).setAtom( pratilipiData.getType().getName() ) )
					.addField( Field.newBuilder().setName( "keyword" ).setAtom( pratilipiData.getType().getName() ) )
					.addField( Field.newBuilder().setName( "keyword" ).setAtom( pratilipiData.getType().getName() ) )
					.addField( Field.newBuilder().setName( "keyword" ).setAtom( pratilipiData.getType().getNamePlural() ) )
					.addField( Field.newBuilder().setName( "keyword" ).setAtom( pratilipiData.getType().getNamePlural() ) )
					.addField( Field.newBuilder().setName( "keyword" ).setAtom( pratilipiData.getType().getNamePlural() ) )
					.addField( Field.newBuilder().setName( "keyword" ).setAtom( pratilipiData.getType().getNamePlural() ) )
					
					.addField( Field.newBuilder().setName( "readCount" ).setNumber( pratilipiData.getReadCount() ) )
					.addField( Field.newBuilder().setName( "relevance" ).setNumber( pratilipiData.getRelevance() ) );
		}
		
		if( keywords != null && ! keywords.isEmpty() )
			docBuilder.addField( Field.newBuilder().setName( "keywords" ).setText(
					keywords.length() > 262144 ? keywords.substring( 0, 262144 ) : keywords // 1048576/4 = 262144
			) );
				
		if( pratilipiData.getAuthorId() != null )
			docBuilder.addField( Field.newBuilder().setName( "author" ).setAtom( pratilipiData.getAuthorId().toString() ) )
					// 3x weightage to Author Name
					.addField( Field.newBuilder().setName( "author" ).setText( pratilipiData.getAuthor().getFullName() ) )
					.addField( Field.newBuilder().setName( "author" ).setText( pratilipiData.getAuthor().getFullName() ) )
					.addField( Field.newBuilder().setName( "author" ).setText( pratilipiData.getAuthor().getFullName() ) )
					.addField( Field.newBuilder().setName( "author" ).setText( pratilipiData.getAuthor().getFullNameEn() ) )
					.addField( Field.newBuilder().setName( "author" ).setText( pratilipiData.getAuthor().getFullNameEn() ) )
					.addField( Field.newBuilder().setName( "author" ).setText( pratilipiData.getAuthor().getFullNameEn() ) );
		
/*		for( Long genreId : pratilipiData.getCategoryIdList() )
			docBuilder.addField( Field.newBuilder().setName( "genre" ).setAtom( genreId.toString() ) );

		for( String genreName : pratilipiData.getCategoryNameList() ) {
			// 4x weightage to Genre
			docBuilder.addField( Field.newBuilder().setName( "genre" ).setText( genreName ) );
			docBuilder.addField( Field.newBuilder().setName( "genre" ).setText( genreName ) );
			docBuilder.addField( Field.newBuilder().setName( "genre" ).setText( genreName ) );
			docBuilder.addField( Field.newBuilder().setName( "genre" ).setText( genreName ) );
		}*/

		return docBuilder.build();
	}

	
	// AUTHOR Data
	
	@Override
	public DataListCursorTuple<Long> searchAuthor( String query, AuthorFilter authorFilter, String cursorStr, Integer offset, Integer resultCount ) {
		
		SortOptions.Builder sortOptionsBuilder = SortOptions.newBuilder();

		if( authorFilter.getOrderByContentPublished() != null ) {
			sortOptionsBuilder.addSortExpression( SortExpression.newBuilder()
					.setExpression( "contentPublished" )
					.setDirection( authorFilter.getOrderByContentPublished()
							? SortExpression.SortDirection.ASCENDING
							: SortExpression.SortDirection.DESCENDING )
					.setDefaultValueNumeric( 0.0 ) );

		} else if( query != null && ! query.isEmpty() ) {
			sortOptionsBuilder.setMatchScorer( MatchScorer.newBuilder() );
			sortOptionsBuilder.addSortExpression( SortExpression.newBuilder()
					.setExpression( SortExpression.SCORE_FIELD_NAME )
					.setDirection( SortExpression.SortDirection.DESCENDING )
					.setDefaultValueNumeric( 0.0 ) );
			sortOptionsBuilder.addSortExpression( SortExpression.newBuilder()
					.setExpression( "contentPublished" )
					.setDirection( SortExpression.SortDirection.DESCENDING )
					.setDefaultValueNumeric( 0.0 ) );
			
		} else {
			sortOptionsBuilder.addSortExpression( SortExpression.newBuilder()
					.setExpression( "contentPublished" )
					.setDirection( SortExpression.SortDirection.DESCENDING )
					.setDefaultValueNumeric( 0.0 ) );
		}
		
		SortOptions sortOptions = sortOptionsBuilder.setLimit( 1000 ).build();

		
		String searchQuery = "docType:Author";

		if( authorFilter.getLanguage() != null )
			searchQuery = searchQuery + " AND language:" + authorFilter.getLanguage().getNameEn();

		if( query != null && ! query.isEmpty() )
			searchQuery = "( " + query + " ) AND " + searchQuery;

		
		Results<ScoredDocument> result = search( searchQuery, sortOptions, cursorStr, offset, resultCount, "docId" );
		
		List<Long> authorIdList = new ArrayList<>( result.getNumberReturned() ); 
		for( ScoredDocument document : result )
			authorIdList.add( Long.parseLong( document.getOnlyField( "docId" ).getAtom() ) );
		
		Cursor cursor = resultCount == null || authorIdList.size() < resultCount ? null : result.getCursor();
		
		return new DataListCursorTuple<Long>(
				authorIdList,
				cursor == null ? null : cursor.toWebSafeString(),
				result.getNumberFound() );
	}
	
	@Override
	public void indexAuthorData( AuthorData authorData, UserData userData ) throws UnexpectedServerException {
		indexDocument( createDocument( authorData, userData ) );
	}

	private Document createDocument( AuthorData authorData, UserData userData ) {
		
		String docId = "AuthorData:" + authorData.getId();
		
		Builder docBuilder = Document.newBuilder()
				.setId( docId )
				.addField( Field.newBuilder().setName( "docId" ).setAtom( authorData.getId().toString() ) )
				.addField( Field.newBuilder().setName( "docType" ).setAtom( "Author" ) );

		// 4x weightage to Language
		if( authorData.getLanguage() != null )
			docBuilder.addField( Field.newBuilder().setName( "language" ).setText( authorData.getLanguage().getName() ) )
					.addField( Field.newBuilder().setName( "language" ).setText( authorData.getLanguage().getName() ) )
					.addField( Field.newBuilder().setName( "language" ).setText( authorData.getLanguage().getName() ) )
					.addField( Field.newBuilder().setName( "language" ).setText( authorData.getLanguage().getName() ) )
					.addField( Field.newBuilder().setName( "language" ).setText( authorData.getLanguage().getNameEn() ) )
					.addField( Field.newBuilder().setName( "language" ).setText( authorData.getLanguage().getNameEn() ) )
					.addField( Field.newBuilder().setName( "language" ).setText( authorData.getLanguage().getNameEn() ) )
					.addField( Field.newBuilder().setName( "language" ).setText( authorData.getLanguage().getNameEn() ) );
				
		// 3x weightage to Author Name
		if( authorData.getFullName() != null )
			docBuilder.addField( Field.newBuilder().setName( "name" ).setText( authorData.getFullName() ) )
					.addField( Field.newBuilder().setName( "name" ).setText( authorData.getFullName() ) )
					.addField( Field.newBuilder().setName( "name" ).setText( authorData.getFullName() ) );
		if( authorData.getFullNameEn() != null )
			docBuilder.addField( Field.newBuilder().setName( "name" ).setText( authorData.getFullNameEn() ) )
					.addField( Field.newBuilder().setName( "name" ).setText( authorData.getFullNameEn() ) )
					.addField( Field.newBuilder().setName( "name" ).setText( authorData.getFullNameEn() ) );

		docBuilder.addField( Field.newBuilder().setName( "summary" ).setHTML( authorData.getSummary() ) );

		docBuilder.addField( Field.newBuilder().setName( "contentPublished" ).setNumber( authorData.getContentPublished() ) );
		docBuilder.addField( Field.newBuilder().setName( "totalReadCount" ).setNumber( authorData.getTotalReadCount() ) );

		
		if( userData != null )
			docBuilder.addField( Field.newBuilder().setName( "userId" ).setAtom( userData.getId().toString() ) );
		if( userData != null && userData.getEmail() != null )
			docBuilder.addField( Field.newBuilder().setName( "email" ).setAtom( userData.getEmail() ) );
		if( userData != null && userData.getPhone() != null )
			docBuilder.addField( Field.newBuilder().setName( "phone" ).setAtom( userData.getPhone() ) );
			
		
		return docBuilder.build();
		
	}
	
	@Override
	public void deleteAuthorDataIndex( Long authorId ) {
		deleteIndex( "AuthorData:" + authorId );
	}

}
