package com.pratilipi.data.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.pratilipi.common.type.AuthorState;
import com.pratilipi.common.type.Gender;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.gae.AuthorEntity;

public class AuthorMock {

	public static final List<Author> AUTHOR_TABLE = new LinkedList<>();

	public static final Author hiAuthor_1 = new AuthorEntity( 1L );
	public static final Author guAuthor_1 = new AuthorEntity( 2L );
	public static final Author taAuthor_1 = new AuthorEntity( 3L );

	
	static {
		AUTHOR_TABLE.add( hiAuthor_1 );
		AUTHOR_TABLE.add( guAuthor_1 );
		AUTHOR_TABLE.add( taAuthor_1 );

		hiAuthor_1.setFirstName( "हिंदी" );
		hiAuthor_1.setLastName( "ऑथर" );
		hiAuthor_1.setPenName( "पेन नाम" );
		hiAuthor_1.setFirstNameEn( "Hindi" );
		hiAuthor_1.setLastNameEn( "Author" );
		hiAuthor_1.setPenNameEn( "PenName" );
		hiAuthor_1.setSummary( "<b>एक पल का क्रोध आपका भविष्य बिगाड सकता है ।</b> <i>एक पल का क्रोध आपका भविष्य बिगाड सकता है ।</i> एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है ।<br/><br/> एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है । एक पल का क्रोध आपका भविष्य बिगाड सकता है ।<br/><br/>" );
		hiAuthor_1.setLanguage( Language.HINDI );
		hiAuthor_1.setGender( Gender.MALE );
		hiAuthor_1.setDateOfBirth( "17-03-1994" );
		hiAuthor_1.setContentDrafted( 0 );
		hiAuthor_1.setContentPublished( 3 );
		hiAuthor_1.setFollowCount( 10772L );
		hiAuthor_1.setLocation( "New Delhi" );
		hiAuthor_1.setRegistrationDate( new Date() );
		hiAuthor_1.setTotalReadCount( 1000L );
		hiAuthor_1.setTotalFbLikeShareCount( 100L );
		hiAuthor_1.setState( AuthorState.ACTIVE );
		hiAuthor_1.setUserId( 1L );
		
		guAuthor_1.setFirstName( "ગુજરાતી" );
		guAuthor_1.setLastName( "ઐઠોર" );
		guAuthor_1.setFirstNameEn( "Gujarati" );
		guAuthor_1.setLastNameEn( "Author" );
		guAuthor_1.setLanguage( Language.GUJARATI );
		guAuthor_1.setGender( Gender.FEMALE );
		guAuthor_1.setDateOfBirth( "08-12-1982" );
		guAuthor_1.setContentDrafted( 0 );
		guAuthor_1.setContentPublished( 3 );
		guAuthor_1.setFollowCount( 124L );
		guAuthor_1.setLocation( "Gandhi Nagar" );
		guAuthor_1.setRegistrationDate( new Date() );
		guAuthor_1.setTotalReadCount( 16L );
		guAuthor_1.setTotalFbLikeShareCount( 12L );
		guAuthor_1.setState( AuthorState.ACTIVE );
		guAuthor_1.setUserId( 2L );

		taAuthor_1.setFirstName( "தமிழ்" );
		taAuthor_1.setLastName( "ஆதோர்" );
		taAuthor_1.setFirstNameEn( "Tamil" );
		taAuthor_1.setLastNameEn( "Author" );
		taAuthor_1.setPenNameEn( "PenName" );
		taAuthor_1.setLanguage( Language.TAMIL );
		taAuthor_1.setGender( Gender.MALE );
		taAuthor_1.setDateOfBirth( "01-01-1970" );
		taAuthor_1.setContentDrafted( 0 );
		taAuthor_1.setContentPublished( 3 );
		taAuthor_1.setFollowCount( 500022L );
		taAuthor_1.setLocation( "Chennai" );
		taAuthor_1.setRegistrationDate( new Date() );
		taAuthor_1.setTotalReadCount( 1096L );
		taAuthor_1.setTotalFbLikeShareCount( 100L );
		taAuthor_1.setState( AuthorState.ACTIVE );
		taAuthor_1.setUserId( 3L );
	}
	
}
