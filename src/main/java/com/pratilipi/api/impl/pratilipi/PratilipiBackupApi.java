package com.pratilipi.api.impl.pratilipi;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.ListOptions;
import com.google.appengine.tools.cloudstorage.ListResult;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.AppProperty;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.type.gae.PratilipiEntity;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;


@SuppressWarnings("serial")
@Bind( uri = "/pratilipi/backup" )
public class PratilipiBackupApi extends GenericApi {

	private static final Logger logger =
			Logger.getLogger( PratilipiBackupApi.class.getName() );
	
	private static final String CSV_HEADER = "PratilipiId,AuthorId,"
			+ "Title,TitleEN,Language,HasSummary,"
			+ "Type,ContentType,State,HasCover,ListingDate,"
			+ "ReviewCount, RatingCount, TotalRating, ReadCount";
	private static final String CSV_SEPARATOR = ",";
	private static final String LINE_SEPARATOR = "\n";


	public static class GetRequest extends GenericRequest {
		
		private Boolean generateCsv;

	}
	
	public static class PostRequest extends GenericRequest {
		
		@Validate( required = true, minLong = 1L )
		private Long pratilipiId;
		
	}
	
	
	@Get
	public GenericResponse get( GetRequest request ) throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		
		// Fetching Cursor from AppProperty
		AppProperty appProperty = dataAccessor.getAppProperty( AppProperty.API_PRATILIPI_BACKUP );
		if( appProperty == null )
			appProperty = dataAccessor.newAppProperty( AppProperty.API_PRATILIPI_BACKUP );
		Cursor cursor = appProperty.getValue() == null
				? null
				: Cursor.fromWebSafeString( (String) appProperty.getValue() );
		
		
		QueryResultIterator<Key<PratilipiEntity>> itr = ObjectifyService.ofy().load()
				.type( PratilipiEntity.class )
				.filter( "LAST_UPDATED >=", new Date( 1480530600000L ) ) // Thu Dec 01 00:00:00 IST 2016
				.order( "LAST_UPDATED" )
				.startAt( cursor )
				.keys()
				.iterator();

		int batchSize = 1000;
		List<Task> taskList = new ArrayList<>( batchSize );
		while( itr.hasNext() ) {
			TaskQueueFactory.getPratilipiTaskQueue().add( TaskQueueFactory.newTask()
					.setUrl( "/pratilipi/backup" )
					.addParam( "pratilipiId", itr.next().getId() + "" ) );
			if( taskList.size() == batchSize || ! itr.hasNext() ) {
				TaskQueueFactory.getPratilipiTaskQueue().addAll( taskList );
				taskList.clear();
			}
		}
		
		
		// Updating Cursor to AppProperty
		appProperty.setValue( itr.getCursor().toWebSafeString() );
		dataAccessor.createOrUpdateAppProperty( appProperty );
		
		
/*		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessorBackup();
		
		Date backupDate = new Date();

		DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
		DateFormat csvDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
		DateFormat dateTimeFormat = new SimpleDateFormat( "yyyy-MM-dd-HH:mm-z" );
		dateFormat.setTimeZone( TimeZone.getTimeZone( "Asia/Kolkata" ) );
		csvDateFormat.setTimeZone( TimeZone.getTimeZone( "Asia/Kolkata" ) );
		dateTimeFormat.setTimeZone( TimeZone.getTimeZone( "Asia/Kolkata" ) );

		StringBuilder backup = new StringBuilder();
		StringBuilder csv = new StringBuilder( CSV_HEADER + LINE_SEPARATOR );

		int count = 0;
		String cursor = null;
		PratilipiFilter pratilipiFilter = new PratilipiFilter();
		Gson gson = new GsonBuilder().registerTypeAdapter( Date.class, new GsonIstDateAdapter() ).create();
		
		while( true ) {
			DataListCursorTuple<Pratilipi> pratilipiListCursorTuple = dataAccessor.getPratilipiList( pratilipiFilter, cursor, 1000 );
			List<Pratilipi> pratilipiList = pratilipiListCursorTuple.getDataList();

			for( Pratilipi pratilipi : pratilipiList ) {
                backup.append( gson.toJson( pratilipi ) + LINE_SEPARATOR );

				if( request.generateCsv != null && request.generateCsv )
					csv.append( "'" + pratilipi.getId() )
							.append( CSV_SEPARATOR ).append( pratilipi.getAuthorId() == null ? "" : "'" + pratilipi.getAuthorId() )
							.append( CSV_SEPARATOR ).append( pratilipi.getTitle()	 == null ? "" : "\"" + pratilipi.getTitle().replace( "\"", "\"\"" ) + "\"" )
							.append( CSV_SEPARATOR ).append( pratilipi.getTitleEn()	 == null ? "" : "\"" + pratilipi.getTitleEn().replace( "\"", "\"\"" ) + "\"" )
							.append( CSV_SEPARATOR ).append( pratilipi.getLanguage() )
							.append( CSV_SEPARATOR ).append( pratilipi.getSummary() != null && pratilipi.getSummary().trim().length() != 0 )
							.append( CSV_SEPARATOR ).append( pratilipi.getType() )
							.append( CSV_SEPARATOR ).append( pratilipi.getContentType() )
							.append( CSV_SEPARATOR ).append( pratilipi.getState() )
							.append( CSV_SEPARATOR ).append( pratilipi.getCoverImage() != null )
							.append( CSV_SEPARATOR ).append( csvDateFormat.format( pratilipi.getListingDate() ) )
							.append( CSV_SEPARATOR ).append( pratilipi.getReviewCount() )
							.append( CSV_SEPARATOR ).append( pratilipi.getRatingCount() )
							.append( CSV_SEPARATOR ).append( pratilipi.getTotalRating() )
							.append( CSV_SEPARATOR ).append( pratilipi.getReadCount() )
							.append( LINE_SEPARATOR );
				
			}
			
			count = count + pratilipiList.size();

			if( pratilipiList.size() < 1000 )
				break;
			else
				cursor = pratilipiListCursorTuple.getCursor();
		}
		

		String fileName = "datastore.pratilipi/"
				+ dateFormat.format( backupDate ) + "/"
				+ "pratilipi-" + dateTimeFormat.format( backupDate );

		BlobEntry pratilipiBackupEntry = blobAccessor.newBlob(
				fileName,
				backup.toString().getBytes( Charset.forName( "UTF-8" ) ),
				"text/plain" );
		
		blobAccessor.createOrUpdateBlob( pratilipiBackupEntry );
		
		
		if( request.generateCsv != null && request.generateCsv ) {
			BlobEntry authorCsvEntry = blobAccessor.newBlob(
					"datastore/pratilipi.csv",
					csv.toString().getBytes( Charset.forName( "UTF-8" ) ),
					"text/csv" );
			blobAccessor.createOrUpdateBlob( authorCsvEntry );
		}
		

		logger.log( Level.INFO, "Backed up " + count + " Pratilipi Entities." );
*/
		return new GenericResponse();
		
	}
	
	@Post
	public GenericResponse post( PostRequest request ) throws UnexpectedServerException {
		
		Pratilipi pratilipi = DataAccessorFactory.getDataAccessor().getPratilipi( request.pratilipiId );

		Date dateTime = new Date( pratilipi.getLastUpdated().getTime() + TimeUnit.HOURS.toMillis( 1L ) - 1 );
		DateFormat dateTimeFormat = new SimpleDateFormat( "yyyy-MM-dd-HH'.00'-z" );
		dateTimeFormat.setTimeZone( TimeZone.getTimeZone( "Asia/Kolkata" ) );
		
		
		GcsService gcsService = GcsServiceFactory.createGcsService( RetryParams.getDefaultInstance() );

		String srcBucket = "static.pratilipi.com";
		String dstBucket = "backup.pratilipi.com";
		
		String srcPrefix = "pratilipi/" + request.pratilipiId + "/";
		String dstPrefix = srcBucket + "/pratilipi-" + dateTimeFormat.format( dateTime ) + "/" + request.pratilipiId + "/";
		
		
		try {
			ListResult result = gcsService.list( srcBucket, new ListOptions.Builder().setPrefix( srcPrefix ).build() );
			while( result.hasNext() ) {
				String srcName = result.next().getName();
				String dstName = dstPrefix + srcName.substring( srcPrefix.length() );
				gcsService.copy(
						new GcsFilename( srcBucket, srcName),
						new GcsFilename( dstBucket, dstName ) );
			}
		} catch( IOException e ) {
			logger.log( Level.SEVERE, "", e );
			throw new UnexpectedServerException();
		}

		
		return new GenericResponse();
	
	}
	
}