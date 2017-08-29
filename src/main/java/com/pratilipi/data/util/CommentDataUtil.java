package com.pratilipi.data.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.CommentParentType;
import com.pratilipi.common.type.CommentState;
import com.pratilipi.common.type.ReferenceType;
import com.pratilipi.common.util.UserAccessUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.client.CommentData;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.type.AuditLog;
import com.pratilipi.data.type.Comment;
import com.pratilipi.data.type.CommentDoc;
import com.pratilipi.data.type.PratilipiReviewsDoc;
import com.pratilipi.data.type.User;
import com.pratilipi.data.type.UserPratilipi;
import com.pratilipi.data.type.UserPratilipiDoc;
import com.pratilipi.filter.AccessTokenFilter;

public class CommentDataUtil {
	
	public static boolean hasAccessToAddCommentData( CommentData commentData ) {
		
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		
		if( ! commentData.getUserId().equals( accessToken.getUserId() ) )
			return false;

		if( ! UserAccessUtil.hasUserAccess( accessToken.getUserId(), null, AccessType.COMMENT_ADD ) )
			return false;
		
		return true;
		
	}
	
	public static boolean hasAccessToUpdateCommentData( Comment comment ) {
		return hasAccessToUpdateCommentData( comment.getState(), comment.getUserId() );
	}

	public static boolean hasAccessToUpdateCommentData( CommentState commentState, Long commentUserId ) {
		
		if( commentState != CommentState.ACTIVE && commentState != CommentState.DELETED )
			return false;
		
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		
		if( ! commentUserId.equals( accessToken.getUserId() ) )
			return false;

		if( ! UserAccessUtil.hasUserAccess( accessToken.getUserId(), null, AccessType.COMMENT_UPDATE ) )
			return false;
		
		return commentUserId.equals( accessToken.getUserId() );
	
	}

	
	public static CommentData createCommentData( Comment comment ) {
		
		CommentData commentData = new CommentData( comment.getId() );
		
		commentData.setUserId( comment.getUserId() );
		commentData.setParentType( comment.getParentType() );
		commentData.setParentId( comment.getParentId() );
		commentData.setReferenceType( comment.getReferenceType() );
		commentData.setReferenceId( comment.getReferenceId() );
		commentData.setContent( comment.getContent() );
		commentData.setState( comment.getState() );
		commentData.setCreationDate( comment.getCreationDate() );
		commentData.setLastUpdated( comment.getLastUpdated() );

		commentData.setAccessToUpdate( hasAccessToUpdateCommentData( comment ) );

		return commentData;
		
	}

	public static CommentData createCommentData( CommentDoc commentDoc ) {
		
		CommentData commentData = new CommentData( commentDoc.getId() );
		
		commentData.setUserId( commentDoc.getUserId() );
		commentData.setContent( commentDoc.getContent() );
		commentData.setCreationDate( commentDoc.getCreationDate() );
		commentData.setLastUpdated( commentDoc.getLastUpdated() );
		
		commentData.setLikeCount( commentDoc.getLikeCount() );
		
		commentData.setAccessToUpdate( hasAccessToUpdateCommentData(
				CommentState.ACTIVE,
				commentDoc.getUserId() ) );

		commentData.setLiked( commentDoc.getLikedByUserIds().contains( AccessTokenFilter.getAccessToken().getUserId() ) );
		
		return commentData;
		
	}
	
	public static List<CommentData> createCommentDataList( List<CommentDoc> commentDocList ) {
		
		List<Long> userIdList = new ArrayList<>( commentDocList.size() );
		for( CommentDoc commentDoc : commentDocList )
			if( ! userIdList.contains( commentDoc.getUserId() ) )
				userIdList.add( commentDoc.getUserId() );
		
		Map<Long, UserData> userDataMap = UserDataUtil.createUserDataList( userIdList, true );
		
		List<CommentData> commentDataList = new ArrayList<>( commentDocList.size() );
		for( CommentDoc commentDoc : commentDocList ) {
			CommentData commentData = createCommentData( commentDoc );
			commentData.setUser( userDataMap.get( commentDoc.getUserId() ) );
			commentDataList.add( commentData );
		}
		
		return commentDataList;
		
	}
	
	
	public static DataListCursorTuple<CommentData> getCommentList(
			CommentParentType parentType, String parentId,
			String cursor, Integer offset, Integer resultCount ) throws UnexpectedServerException {

		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		
		if( parentType == CommentParentType.REVIEW ) {
			
			Long userId = Long.parseLong( parentId.substring( 0, parentId.indexOf( '-' ) ) );
			Long pratilipiId = Long.parseLong( parentId.substring( parentId.indexOf( '-' ) + 1 ) );
			
			PratilipiReviewsDoc reviewsDoc = docAccessor.getPratilipiReviewsDoc( pratilipiId );
			List<CommentDoc> commentDocList = null;
			for( UserPratilipiDoc reviewDoc : reviewsDoc.getReviews() ) {
				if( reviewDoc.getUserId().equals( userId ) ) {
					commentDocList = reviewDoc.getComments();
					break;
				}
			}

			int fromIndex = ( cursor == null ? 0 : Integer.parseInt( cursor ) ) + ( offset == null ? 0 : offset );
			int toIndex = resultCount == null ? commentDocList.size() : fromIndex + resultCount;
			
			commentDocList = commentDocList.subList(
					Math.min( fromIndex, commentDocList.size() ),
					Math.min( toIndex, commentDocList.size() ) );
			
			List<CommentData> commentDataList = createCommentDataList( commentDocList );
			
			return new DataListCursorTuple<CommentData>(
					commentDataList,
					toIndex + "",
					(long) (int) reviewsDoc.getReviews().size() );
			
		}
		
		return null;
		
	}
	
	public static CommentData saveCommentData( CommentData commentData )
			throws InvalidArgumentException, InsufficientAccessException {
		
		_validateCommentDataForSave( commentData );
		
		boolean isNew = commentData.getId() == null;
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Comment comment = isNew ? dataAccessor.newComment() : dataAccessor.getComment( commentData.getId() );

		if ( isNew && ! hasAccessToAddCommentData( commentData ) )
			throw new InsufficientAccessException();
		if( ! isNew && ! hasAccessToUpdateCommentData( comment ) )
			throw new InsufficientAccessException();

		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				isNew ? AccessType.COMMENT_ADD : AccessType.COMMENT_UPDATE,
				comment );

		
		if( isNew ) {
			comment.setUserId( commentData.getUserId() );
			comment.setParentType( commentData.getParentType() );
			comment.setParentId( commentData.getParentId() );
			if( commentData.getParentType() == CommentParentType.REVIEW ) {
				UserPratilipi userPratilipi = dataAccessor.getUserPratilipi( commentData.getParentId() );
				comment.setReferenceType( ReferenceType.PRATILIPI );
				comment.setReferenceId( userPratilipi.getPratilipiId() );
			}
			comment.setCreationDate( new Date() );
		} else {
			comment.setLastUpdated( new Date() );
		}
		
		if( commentData.hasContent() )
			comment.setContent( commentData.getContent() );
		if( commentData.hasState() )
			comment.setState( commentData.getState() );
		
		
		comment = dataAccessor.createOrUpdateComment( comment, auditLog );

		
		User user = dataAccessor.getUser( commentData.getUserId() );
		UserData userData = UserDataUtil.createUserData( user );
		
		commentData = createCommentData( comment );
		commentData.setUser( userData );
		
		return commentData;
		
	}
	
	private static void _validateCommentDataForSave( CommentData commentData )
			throws InvalidArgumentException {
		
		boolean isNew = commentData.getId() == null;
		
		JsonObject errorMessages = new JsonObject();

		// New comment must have a user id.
		if( isNew && ( commentData.getUserId() == null || commentData.getUserId() == 0L ) )
			errorMessages.addProperty( "userId", GenericRequest.ERR_USER_ID_REQUIRED );
		
		// New comment must have a parent type.
		if( isNew && commentData.getParentType() == null )
			errorMessages.addProperty( "parentType", GenericRequest.ERR_COMMENT_PARENT_TYPE_REQUIRED );
		
		// New comment must have a parent id.
		if( isNew && commentData.getParentId() == null )
			errorMessages.addProperty( "parentId", GenericRequest.ERR_COMMENT_PARENT_ID_REQUIRED );
		
		// New comment must have content.
		if( isNew && commentData.getContent() == null )
			errorMessages.addProperty( "content", GenericRequest.ERR_COMMENT_CONTENT_REQUIRED );
		// Content can not be unset or set to null for old comment
		else if( ! isNew && commentData.hasContent() && commentData.getContent() == null )
			errorMessages.addProperty( "content", GenericRequest.ERR_COMMENT_CONTENT_REQUIRED );
		
		
		if( errorMessages.entrySet().size() > 0 )
			throw new InvalidArgumentException( errorMessages );

	}
	
}