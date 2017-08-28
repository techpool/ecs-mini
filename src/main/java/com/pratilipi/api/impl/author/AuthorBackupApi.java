package com.pratilipi.api.impl.author;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.AuthorFilter;
import com.pratilipi.common.util.GsonIstDateAdapter;
import com.pratilipi.data.BlobAccessor;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.BlobEntry;

@SuppressWarnings("serial")
@Bind( uri = "/author/backup" )
public class AuthorBackupApi extends GenericApi {

	private static final Logger logger =
			Logger.getLogger( AuthorBackupApi.class.getName() );
	
	private static final String CSV_HEADER = "AuthorId,UserId,"
			+ "FirstName,LastName,PenName,FirstNameEN,LastNameEN,PenNameEN,"
			+ "Language,HasSummary,ContentsPublished,RegistrationDate";
	private static final String CSV_SEPARATOR = ",";
	private static final String LINE_SEPARATOR = "\n";
	
	
	public static class GetRequest extends GenericRequest {
		
		private Boolean generateCsv;

	}

	
	@Get
	public GenericResponse get( GetRequest request ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
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
		AuthorFilter authorFilter = new AuthorFilter();
		String cursor = null;
		Gson gson = new GsonBuilder().registerTypeAdapter( Date.class, new GsonIstDateAdapter() ).create();
		
		while( true ) {
			DataListCursorTuple<Author> authorListCursorTupe = dataAccessor.getAuthorList( authorFilter, cursor, 1000 );
			List<Author> authorList = authorListCursorTupe.getDataList();
			
			for( Author author : authorList ) {
				backup.append( gson.toJson( author ) + LINE_SEPARATOR );

				if( request.generateCsv != null &&  request.generateCsv )
					csv.append( "'" + author.getId() )
							.append( CSV_SEPARATOR ).append( author.getUserId()			== null ? "" : "'" + author.getUserId() )
							.append( CSV_SEPARATOR ).append( author.getFirstName()		== null ? "" : author.getFirstName() )
							.append( CSV_SEPARATOR ).append( author.getLastName()		== null ? "" : author.getLastName() )
							.append( CSV_SEPARATOR ).append( author.getPenName()		== null ? "" : author.getPenName() )
							.append( CSV_SEPARATOR ).append( author.getFirstNameEn()	== null ? "" : author.getFirstNameEn() )
							.append( CSV_SEPARATOR ).append( author.getLastNameEn()		== null ? "" : author.getLastNameEn() )
							.append( CSV_SEPARATOR ).append( author.getPenNameEn()		== null ? "" : author.getPenNameEn() )
							.append( CSV_SEPARATOR ).append( author.getLanguage() )
							.append( CSV_SEPARATOR ).append( author.getSummary() != null && author.getSummary().trim().length() != 0 )
							.append( CSV_SEPARATOR ).append( author.getContentPublished() )
							.append( CSV_SEPARATOR ).append( csvDateFormat.format( author.getRegistrationDate() ) )
							.append( LINE_SEPARATOR );
				
			}
			
			count = count + authorList.size();

			if( authorList.size() < 1000 )
				break;
			else
				cursor = authorListCursorTupe.getCursor();
		}

		
		String fileName = "datastore.author/"
				+ dateFormat.format( backupDate ) + "/"
				+ "author-" + dateTimeFormat.format( backupDate );

		BlobEntry authorBackupEntry = blobAccessor.newBlob(
				fileName,
				backup.toString().getBytes( Charset.forName( "UTF-8" ) ),
				"text/plain" );
		
		blobAccessor.createOrUpdateBlob( authorBackupEntry );
		
		
		if( request.generateCsv != null &&  request.generateCsv ) {
			BlobEntry authorCsvEntry = blobAccessor.newBlob(
					"datastore/author.csv",
					csv.toString().getBytes( Charset.forName( "UTF-8" ) ),
					"text/csv" );
			blobAccessor.createOrUpdateBlob( authorCsvEntry );
		}
		

		logger.log( Level.INFO, "Backed up " + count + " Author Entities." );

		return new GenericResponse();
		
	}
	
}