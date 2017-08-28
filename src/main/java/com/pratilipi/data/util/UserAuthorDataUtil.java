package com.pratilipi.data.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.UserFollowState;
import com.pratilipi.common.util.UserAccessUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.AuthorData;
import com.pratilipi.data.client.UserAuthorData;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.type.AuditLog;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.User;
import com.pratilipi.data.type.UserAuthor;
import com.pratilipi.filter.AccessTokenFilter;

public class UserAuthorDataUtil {
	
	private static final Logger logger =
			Logger.getLogger( UserAuthorDataUtil.class.getName() );

	
	public static boolean hasAccessToUpdateUserAuthorData( UserAuthor userAuthor, AccessType accessType ) {
		
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		
		if( ! userAuthor.getUserId().equals( accessToken.getUserId() ) )
			return false;
		
		if( ! UserAccessUtil.hasUserAccess( accessToken.getUserId(), null, accessType ) )
			return false;

		// User can not follow his/her own Author profile.
		if( accessType == AccessType.USER_AUTHOR_FOLLOWING ) {
			DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
			Author author = dataAccessor.getAuthor( userAuthor.getAuthorId() );
			if( userAuthor.getUserId().equals( author.getUserId() ) )
				return false;
		}
		
		return true;

	}
	

	public static UserAuthorData createUserAuthorData( UserAuthor userAuthor ) {
		
		if( userAuthor == null )
			return null;
		
		UserAuthorData userAuthorData = new UserAuthorData();
		userAuthorData.setId( userAuthor.getId() );
		userAuthorData.setUserId( userAuthor.getUserId() );
		userAuthorData.setAuthorId( userAuthor.getAuthorId() );
		userAuthorData.setFollowState( userAuthor.getFollowState() );
		userAuthorData.setFollowDate( userAuthor.getFollowDate() );
		
		return userAuthorData;
		
	}
	
	public static List<UserAuthorData> createUserAuthorDataList( List<UserAuthor> userAuthorList ) {
		List<UserAuthorData> userPratilipiDataList = new ArrayList<>( userAuthorList.size() );
		for( UserAuthor userAuthor : userAuthorList )
			userPratilipiDataList.add( createUserAuthorData( userAuthor ) );
		return userPratilipiDataList;
	}

	
	public static UserAuthorData getUserAuthor( Long userId, Long authorId ) {

		if( userId == null || userId.equals( 0L ) || authorId == null || authorId.equals( 0L ) )
			return null;
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		UserAuthor userAuthor = dataAccessor.getUserAuthor( userId, authorId );
		if( userAuthor == null ) {
			userAuthor = dataAccessor.newUserAuthor();
			userAuthor.setUserId( userId );
			userAuthor.setAuthorId( authorId );
		}
		
		return createUserAuthorData( userAuthor );
		
	}

	public static DataListCursorTuple<AuthorData> getUserFollowList( Long userId, String cursor, Integer offset, Integer resultCount ) {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		User user = dataAccessor.getUser( userId );
		if( user.getFollowCount() == 0L )
			return new DataListCursorTuple<>( new ArrayList<AuthorData>( 0 ), null, 0L );
		
		
		DataListCursorTuple<Long> authorIdListCursorTuple = dataAccessor.getUserAuthorFollowList( userId, null, cursor, offset, resultCount );
		List<Long> authorIdList = authorIdListCursorTuple.getDataList();
		List<AuthorData> authorDataList = AuthorDataUtil.createAuthorDataList( authorIdList, true );

		
		// Setting AuthorData.isFollowing flag
		if( userId.equals( AccessTokenFilter.getAccessToken().getUserId() ) ) {

			for( AuthorData authorData : authorDataList )
				authorData.setFollowing( true );

		} else {
			
			List<UserAuthor> userAuthorList = dataAccessor.getUserAuthorList(
					AccessTokenFilter.getAccessToken().getUserId(),
					authorIdList );
			
			if( userAuthorList.size() != 0 )
				for( int i = 0; i < authorIdList.size(); i++ )
					if( userAuthorList.get( i ) != null && userAuthorList.get( i ).getFollowState() == UserFollowState.FOLLOWING )
						authorDataList.get( i ).setFollowing( true );
			
		}
		
		
		// Setting UserData.isFollowing flag
		Author author = dataAccessor.getAuthorByUserId( AccessTokenFilter.getAccessToken().getUserId() );
		if( author != null ) {
			
			List<Long> userIdList = new ArrayList<>( authorDataList.size() );
			List<UserData> userDataList = new ArrayList<>( authorDataList.size() );
			for( AuthorData authorData : authorDataList ) {
				if( authorData.getUser() != null && authorData.getUser().getId() != null ) {
					userIdList.add( authorData.getUser().getId() );
					userDataList.add( authorData.getUser() );
				}
			}
			
			List<UserAuthor> userAuthorList = dataAccessor.getUserAuthorList(
					userIdList,
					author.getId() );

			if( userAuthorList != null ) {
				for( UserAuthor userAuthor : userAuthorList )
					if( userAuthor != null && userAuthor.getFollowState() == UserFollowState.FOLLOWING )
						userDataList.get( userIdList.indexOf( userAuthor.getUserId() ) ).setFollowing( true );
			}

		}

		
		return new DataListCursorTuple<>(
				authorDataList,
				authorIdListCursorTuple.getCursor(),
				user.getFollowCount() );

	}
	
	public static DataListCursorTuple<UserData> getAuthorFollowList( Long authorId, String cursor, Integer offset, Integer resultCount ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		Author author = dataAccessor.getAuthor( authorId );
		if( author.getFollowCount() == 0L )
			return new DataListCursorTuple<>( new ArrayList<UserData>( 0 ), null, 0L );
		
		
		DataListCursorTuple<Long> userIdListCursorTuple = dataAccessor.getUserAuthorFollowList( null, authorId, cursor, offset, resultCount );
		List<Long> userIdList = userIdListCursorTuple.getDataList();
		Map<Long, UserData> users = UserDataUtil.createUserDataList( userIdList, true );
		
		List<UserData> userDataList = new ArrayList<>( userIdList.size() );
		for( Long userId : userIdList )
			userDataList.add( users.get( userId ) );
		
		
		// Setting UserData.isFollowing flag
		if( AccessTokenFilter.getAccessToken().getUserId().equals( author.getUserId() ) ) {

			for( UserData userData : userDataList )
				userData.setFollowing( true );

		} else {
			
			Author authorProfile = dataAccessor.getAuthorByUserId( AccessTokenFilter.getAccessToken().getUserId() );
			
			if( authorProfile != null ) {
				List<UserAuthor> userAuthorList = dataAccessor.getUserAuthorList( userIdList, authorProfile.getId() );
				for( int i = 0; i < userIdList.size(); i++ )
					if( userAuthorList.get( i ) != null && userAuthorList.get( i ).getFollowState() == UserFollowState.FOLLOWING )
						userDataList.get( i ).setFollowing( true );
			}
			
		}

		
		// Setting AuthorData.isFollowing flag
		List<Long> authorIdList = new ArrayList<>( userDataList.size() );
		for( UserData userData : userDataList )
			authorIdList.add( userData.getAuthor().getId() );
		
		List<UserAuthor> userAuthorList = dataAccessor.getUserAuthorList(
				AccessTokenFilter.getAccessToken().getUserId(),
				authorIdList );
		
		if( userAuthorList.size() != 0 )
			for( int i = 0; i < authorIdList.size(); i++ )
				if( userAuthorList.get( i ) != null && userAuthorList.get( i ).getFollowState() == UserFollowState.FOLLOWING )
					userDataList.get( i ).getAuthor().setFollowing( true );
		
		
		return new DataListCursorTuple<>(
				userDataList,
				userIdListCursorTuple.getCursor(),
				author.getFollowCount() );
		
	}
	
	
	public static UserAuthorData saveUserAuthorFollow( Long userId, Long authorId, UserFollowState followState )
			throws InvalidArgumentException, InsufficientAccessException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		UserAuthor userAuthor = dataAccessor.getUserAuthor( userId, authorId );
		if( userAuthor == null ) {
			userAuthor = dataAccessor.newUserAuthor();
			userAuthor.setUserId( userId );
			userAuthor.setAuthorId( authorId );
		}
		
		
		if( ! hasAccessToUpdateUserAuthorData( userAuthor, AccessType.USER_AUTHOR_FOLLOWING ) )
			throw new InsufficientAccessException();
		
		
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		AuditLog auditLog = dataAccessor.newAuditLog( accessToken, AccessType.USER_AUTHOR_FOLLOWING, userAuthor );
		
		userAuthor.setFollowState( followState );
		userAuthor.setFollowDate( new Date() );
		
		userAuthor = dataAccessor.createOrUpdateUserAuthor( userAuthor, auditLog );
		
		
		return createUserAuthorData( userAuthor );
		
	}

}
