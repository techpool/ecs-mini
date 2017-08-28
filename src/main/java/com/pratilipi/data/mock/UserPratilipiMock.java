package com.pratilipi.data.mock;

import static com.pratilipi.data.mock.PratilipiMock.*;
import static com.pratilipi.data.mock.UserMock.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.pratilipi.data.type.UserPratilipi;
import com.pratilipi.data.type.gae.UserPratilipiEntity;

public class UserPratilipiMock {

	public static final List<UserPratilipi> USER_PRATILIPI_TABLE = new LinkedList<>();

	public static final UserPratilipi user_pratilipi_1 = new UserPratilipiEntity( user_1.getId(), hiPratilipi_1.getId() );
	public static final UserPratilipi user_pratilipi_2 = new UserPratilipiEntity( user_2.getId(), hiPratilipi_1.getId() );
	public static final UserPratilipi user_pratilipi_3 = new UserPratilipiEntity( user_3.getId(), hiPratilipi_1.getId() );

	public static final UserPratilipi user_pratilipi_4 = new UserPratilipiEntity( user_1.getId(), guPratilipi_1.getId() );
	public static final UserPratilipi user_pratilipi_5 = new UserPratilipiEntity( user_2.getId(), guPratilipi_1.getId() );
	public static final UserPratilipi user_pratilipi_6 = new UserPratilipiEntity( user_3.getId(), guPratilipi_1.getId() );

	public static final UserPratilipi user_pratilipi_7 = new UserPratilipiEntity( user_1.getId(), taPratilipi_1.getId() );
	public static final UserPratilipi user_pratilipi_8 = new UserPratilipiEntity( user_2.getId(), taPratilipi_1.getId() );
	public static final UserPratilipi user_pratilipi_9 = new UserPratilipiEntity( user_3.getId(), taPratilipi_1.getId() );


	static {
		USER_PRATILIPI_TABLE.add( user_pratilipi_1 );
		USER_PRATILIPI_TABLE.add( user_pratilipi_2 );
		USER_PRATILIPI_TABLE.add( user_pratilipi_3 );


		user_pratilipi_1.setAddedToLib( true );
		user_pratilipi_1.setAddedToLibDate( new Date() );
		user_pratilipi_1.setRating( 3 );
		user_pratilipi_1.setRatingDate( new Date() );
		user_pratilipi_1.setReviewTitle( "Review 1" );
		user_pratilipi_1.setReview( "Average, Could have done better. Expected a lot from you!" );
		user_pratilipi_1.setReviewDate( new Date() );

		user_pratilipi_2.setAddedToLib( true );
		user_pratilipi_2.setAddedToLibDate( new Date() );
		user_pratilipi_2.setRating( 4 );
		user_pratilipi_2.setRatingDate( new Date() );
		user_pratilipi_2.setReviewTitle( "Review 2" );
		user_pratilipi_2.setReview( "Good story! I liked it!" );
		user_pratilipi_2.setReviewDate( new Date() );

		user_pratilipi_3.setAddedToLib( true );
		user_pratilipi_3.setAddedToLibDate( new Date() );
		user_pratilipi_3.setRating( 5 );
		user_pratilipi_3.setRatingDate( new Date() );
		user_pratilipi_3.setReviewTitle( "Review 3" );
		user_pratilipi_3.setReview( "Just love it !!! Excellent, WOW..." );
		user_pratilipi_3.setReviewDate( new Date() );

		user_pratilipi_4.setAddedToLib( true );
		user_pratilipi_4.setAddedToLibDate( new Date() );
		user_pratilipi_4.setRating( 3 );
		user_pratilipi_4.setRatingDate( new Date() );
		user_pratilipi_4.setReviewTitle( "Review 1" );
		user_pratilipi_4.setReview( "Average, Could have done better. Expected a lot from you!" );
		user_pratilipi_4.setReviewDate( new Date() );

		user_pratilipi_5.setAddedToLib( true );
		user_pratilipi_5.setAddedToLibDate( new Date() );
		user_pratilipi_5.setRating( 4 );
		user_pratilipi_5.setRatingDate( new Date() );
		user_pratilipi_5.setReviewTitle( "Review 2" );
		user_pratilipi_5.setReview( "Good story! I liked it!" );
		user_pratilipi_5.setReviewDate( new Date() );

		user_pratilipi_6.setAddedToLib( true );
		user_pratilipi_6.setAddedToLibDate( new Date() );
		user_pratilipi_6.setRating( 5 );
		user_pratilipi_6.setRatingDate( new Date() );
		user_pratilipi_6.setReviewTitle( "Review 3" );
		user_pratilipi_6.setReview( "Just love it !!! Excellent, WOW..." );
		user_pratilipi_6.setReviewDate( new Date() );

		user_pratilipi_7.setAddedToLib( true );
		user_pratilipi_7.setAddedToLibDate( new Date() );
		user_pratilipi_7.setRating( 3 );
		user_pratilipi_7.setRatingDate( new Date() );
		user_pratilipi_7.setReviewTitle( "Review 1" );
		user_pratilipi_7.setReview( "Average, Could have done better. Expected a lot from you!" );
		user_pratilipi_7.setReviewDate( new Date() );

		user_pratilipi_8.setAddedToLib( true );
		user_pratilipi_8.setAddedToLibDate( new Date() );
		user_pratilipi_8.setRating( 4 );
		user_pratilipi_8.setRatingDate( new Date() );
		user_pratilipi_8.setReviewTitle( "Review 2" );
		user_pratilipi_8.setReview( "Good story! I liked it!" );
		user_pratilipi_8.setReviewDate( new Date() );

		user_pratilipi_9.setAddedToLib( true );
		user_pratilipi_9.setAddedToLibDate( new Date() );
		user_pratilipi_9.setRating( 5 );
		user_pratilipi_9.setRatingDate( new Date() );
		user_pratilipi_9.setReviewTitle( "Review 3" );
		user_pratilipi_9.setReview( "Just love it !!! Excellent, WOW..." );
		user_pratilipi_9.setReviewDate( new Date() );

	}
	
}