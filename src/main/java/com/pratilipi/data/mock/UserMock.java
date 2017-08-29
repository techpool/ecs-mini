package com.pratilipi.data.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.pratilipi.common.type.UserSignUpSource;
import com.pratilipi.common.type.UserState;
import com.pratilipi.data.type.User;
import com.pratilipi.data.type.gae.UserEntity;

public class UserMock {

	public static final List<User> USER_TABLE = new LinkedList<>();

	public static final User user_1 = new UserEntity( 1L );
	public static final User user_2 = new UserEntity( 2L );
	public static final User user_3 = new UserEntity( 3L );

	
	static {
		USER_TABLE.add( user_1 );
		USER_TABLE.add( user_2 );
		USER_TABLE.add( user_3 );

		user_1.setEmail( "user-one@pratilipi.com" );
		user_1.setPassword( "8EilZIQ04Vuf5sbYrp16FV4Z+3IE36ciZUyOeUc8mQA=$7iAI9oWvfqriubH34iWulvkQlAtc5bsLpIc6LuHYUlQ=" ); // password
		user_1.setFacebookId( "12345678901" );
		user_1.setFollowCount( 128L );
		user_1.setPhone( "9876598765" );
		user_1.setSignUpDate( new Date() );
		user_1.setSignUpSource( UserSignUpSource.WEBSITE_M6_FACEBOOK );
		user_1.setState( UserState.ACTIVE );
		
		user_2.setEmail( "user-two@pratilipi.com" );
		user_2.setPassword( "8EilZIQ04Vuf5sbYrp16FV4Z+3IE36ciZUyOeUc8mQA=$7iAI9oWvfqriubH34iWulvkQlAtc5bsLpIc6LuHYUlQ=" ); // password
		user_2.setFollowCount( 256L );
		user_2.setPhone( "9876598765" );
		user_2.setSignUpDate( new Date() );
		user_2.setSignUpSource( UserSignUpSource.WEBSITE_M6 );
		user_2.setState( UserState.REGISTERED );

		user_3.setEmail( "user-three@pratilipi.com" );
		user_3.setPassword( "8EilZIQ04Vuf5sbYrp16FV4Z+3IE36ciZUyOeUc8mQA=$7iAI9oWvfqriubH34iWulvkQlAtc5bsLpIc6LuHYUlQ=" ); // password
		user_3.setFollowCount( 512L );
		user_3.setPhone( "9876598765" );
		user_3.setSignUpDate( new Date() );
		user_3.setSignUpSource( UserSignUpSource.ANDROID_APP );
		user_3.setState( UserState.ACTIVE );
	}
	
}