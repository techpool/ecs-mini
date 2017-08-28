package com.pratilipi.data.util;

import java.util.ArrayList;
import java.util.List;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.UserFollowState;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListIterator;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.type.UserAuthor;
import com.pratilipi.data.type.UserAuthorDoc;
import com.pratilipi.data.type.UserFollowsDoc;

public class UserDocUtil {
	
	public static void updateUserFollows( Long userId ) throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		List<UserAuthorDoc> followingAuthorList = new ArrayList<>();
		List<UserAuthorDoc> unfollowedAuthorList = new ArrayList<>();
		List<UserAuthorDoc> ignoredAuthorList = new ArrayList<>();

		DataListIterator<UserAuthor> userAuthorListIterator = dataAccessor.getUserAuthorListIterator( userId, null, null, null, null );
		while( userAuthorListIterator.hasNext() ) {

			UserAuthor userAuthor = userAuthorListIterator.next();
			
			UserAuthorDoc userAuthorDoc = docAccessor.newUserAuthorDoc();
			userAuthorDoc.setAuthorId( userAuthor.getAuthorId() );
			userAuthorDoc.setFollowDate( userAuthor.getFollowDate() );
			
			switch( userAuthor.getFollowState() ) {
				case FOLLOWING:
					followingAuthorList.add( userAuthorDoc );
					break;
				case UNFOLLOWED:
					unfollowedAuthorList.add( userAuthorDoc );
					break;
				case IGNORED:
					ignoredAuthorList.add( userAuthorDoc );
					break;
			}
			
		}

		UserFollowsDoc userFollowingDoc = docAccessor.newUserFollowsDoc();
		userFollowingDoc.setFollows( UserFollowState.FOLLOWING, followingAuthorList );
		userFollowingDoc.setFollows( UserFollowState.UNFOLLOWED, unfollowedAuthorList );
		userFollowingDoc.setFollows( UserFollowState.IGNORED, ignoredAuthorList );

		docAccessor.save( userId, userFollowingDoc );

	}

}
