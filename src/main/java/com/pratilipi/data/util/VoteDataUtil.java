package com.pratilipi.data.util;

import java.util.Date;

import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.ReferenceType;
import com.pratilipi.common.type.VoteParentType;
import com.pratilipi.common.type.VoteType;
import com.pratilipi.common.util.UserAccessUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.VoteData;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.type.AuditLog;
import com.pratilipi.data.type.Comment;
import com.pratilipi.data.type.UserPratilipi;
import com.pratilipi.data.type.Vote;
import com.pratilipi.filter.AccessTokenFilter;

public class VoteDataUtil {
	
	public static boolean hasAccessToAddOrUpdateData( Vote vote ) {
		
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		
		if( ! vote.getUserId().equals( accessToken.getUserId() ) )
			return false;
		
		if( ! UserAccessUtil.hasUserAccess( accessToken.getUserId(), null, AccessType.VOTE ) )
			return false;
		
		return true;
		
	}
	
	
	public static VoteData createVoteData( Vote vote ) {
		
		VoteData voteData = new VoteData( vote.getId() );
		
		voteData.setUserId( vote.getUserId() );
		voteData.setParentType( vote.getParentType() );
		voteData.setParentId( vote.getParentId() );
		voteData.setReferenceType( vote.getReferenceType() );
		voteData.setReferenceId( vote.getReferenceId() );
		voteData.setType( vote.getType() );
		voteData.setCreationDate( vote.getCreationDate() );
		voteData.setLastUpdated( vote.getLastUpdated() );

		return voteData;
		
	}

	
	public static VoteData saveVoteData( Long userId, VoteParentType parentType, String parentId, VoteType type )
			throws InsufficientAccessException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Vote vote = dataAccessor.getVote( userId, parentType, parentId );

		boolean isNew = vote == null;

		if( isNew )
			vote = dataAccessor.newVote();

		
		AuditLog auditLog = dataAccessor.newAuditLog(
				AccessTokenFilter.getAccessToken(),
				AccessType.VOTE,
				vote );

		if( isNew ) {
			vote.setUserId( userId );
			vote.setParentType( parentType );
			vote.setParentId( parentId );
			if( parentType == VoteParentType.REVIEW ) {
				UserPratilipi userPratilipi = dataAccessor.getUserPratilipi( parentId );
				vote.setReferenceType( ReferenceType.PRATILIPI );
				vote.setReferenceId( userPratilipi.getPratilipiId() );
			} else if( parentType == VoteParentType.COMMENT ) {
				Comment comment = dataAccessor.getComment( Long.parseLong( parentId ) );
				UserPratilipi userPratilipi = dataAccessor.getUserPratilipi( comment.getParentId() );
				vote.setReferenceType( ReferenceType.PRATILIPI );
				vote.setReferenceId( userPratilipi.getPratilipiId() );
			}
			vote.setCreationDate( new Date() );
		} else {
			vote.setLastUpdated( new Date() );
		}

		vote.setType( type );

		
		if ( ! hasAccessToAddOrUpdateData( vote ) )
			throw new InsufficientAccessException();

		
		vote = dataAccessor.createOrUpdateVote( vote, auditLog );
		
		
		return createVoteData( vote );
		
	}
	
}