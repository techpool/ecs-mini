package com.pratilipi.common.util;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.BlobAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.type.User;
import com.pratilipi.data.type.gae.AuthorEntity;
import com.pratilipi.data.type.gae.PratilipiEntity;
import com.pratilipi.data.type.gae.UserEntity;

public class DataStoreBackupUtil {

	public static void csvUser() throws UnexpectedServerException {
		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessorBackup();
		
		DateFormat csvDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
		csvDateFormat.setTimeZone( TimeZone.getTimeZone( "Asia/Kolkata" ) );
		
		String CSV_HEADER = "UserId,FacebookId,Email,SignUpDate";
		String CSV_SEPARATOR = ",";
		String LINE_SEPARATOR = "\n";

		
		StringBuilder csv = new StringBuilder( CSV_HEADER + LINE_SEPARATOR );
		
		int count = 0;
		QueryResultIterator<UserEntity> itr = ObjectifyService.ofy()
				.cache( false )
				.load()
				.type( UserEntity.class )
				.chunk( 1000 )
				.iterator();
		while( itr.hasNext() ) {
			User user = itr.next();
			csv.append( "'" + user.getId().toString() )
					.append( CSV_SEPARATOR ).append( user.getFacebookId() == null ? "" : "'" + user.getFacebookId() )
					.append( CSV_SEPARATOR ).append( user.getEmail()	  == null ? "" : user.getEmail() )
					.append( CSV_SEPARATOR ).append( csvDateFormat.format( user.getSignUpDate() ) )
					.append( LINE_SEPARATOR );
			count++;
			if( count % 1000 == 0 )
				System.out.println( count + " ..." );
		}
		
		System.out.println( count + " ..." );
		

		BlobEntry userCsvEntry = blobAccessor.newBlob(
				"datastore/user.csv",
				csv.toString().getBytes( Charset.forName( "UTF-8" ) ),
				"text/csv" );
		blobAccessor.createOrUpdateBlob( userCsvEntry );
		
	}
	
	public static void csvPratilipi() throws UnexpectedServerException {
		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessorBackup();

		DateFormat csvDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
		csvDateFormat.setTimeZone( TimeZone.getTimeZone( "Asia/Kolkata" ) );

		String CSV_HEADER = "PratilipiId,AuthorId,"
				+ "Title,TitleEN,Language,HasSummary,"
				+ "Type,ContentType,State,HasCover,ListingDate,"
				+ "ReviewCount, RatingCount, TotalRating, ReadCount";
		String CSV_SEPARATOR = ",";
		String LINE_SEPARATOR = "\n";
		
		
		StringBuilder csv = new StringBuilder( CSV_HEADER + LINE_SEPARATOR );

		int count = 0;
		QueryResultIterator<PratilipiEntity> itr = ObjectifyService.ofy()
				.cache( false )
				.load()
				.type( PratilipiEntity.class )
				.chunk( 1000 )
				.iterator();
		while( itr.hasNext() ) {
			Pratilipi pratilipi = itr.next();
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
			count++;
			if( count % 1000 == 0 )
				System.out.println( count + " ..." );
		}
		
		System.out.println( count + " ..." );

		
		BlobEntry authorCsvEntry = blobAccessor.newBlob(
				"datastore/pratilipi.csv",
				csv.toString().getBytes( Charset.forName( "UTF-8" ) ),
				"text/csv" );
		blobAccessor.createOrUpdateBlob( authorCsvEntry );
		
	}
	
	public static void csvAuthor() throws UnexpectedServerException {
		
		String CSV_HEADER = "AuthorId,UserId,"
				+ "FirstName,LastName,PenName,FirstNameEN,LastNameEN,PenNameEN,"
				+ "Language,HasSummary,ContentsPublished,FollowCount,RegistrationDate";
		String CSV_SEPARATOR = ",";
		String LINE_SEPARATOR = "\n";
		
		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessorBackup();
		
		DateFormat csvDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
		csvDateFormat.setTimeZone( TimeZone.getTimeZone( "Asia/Kolkata" ) );

		StringBuilder csv = new StringBuilder( CSV_HEADER + LINE_SEPARATOR );
		int count = 0;
		QueryResultIterator<AuthorEntity> itr = ObjectifyService.ofy()
				.cache( false )
				.load()
				.type( AuthorEntity.class )
				.chunk( 1000 )
				.iterator();
		while( itr.hasNext() ) {
			Author author = itr.next();
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
					.append( CSV_SEPARATOR ).append( author.getFollowCount() )
					.append( CSV_SEPARATOR ).append( csvDateFormat.format( author.getRegistrationDate() ) )
					.append( LINE_SEPARATOR );
			count++;
			if( count % 1000 == 0 )
				System.out.println( count + " ..." );
		}
		
		System.out.println( count + " ..." );

		BlobEntry authorCsvEntry = blobAccessor.newBlob(
				"datastore/author.csv",
				csv.toString().getBytes( Charset.forName( "UTF-8" ) ),
				"text/csv" );
		blobAccessor.createOrUpdateBlob( authorCsvEntry );
		
	}

	
}
