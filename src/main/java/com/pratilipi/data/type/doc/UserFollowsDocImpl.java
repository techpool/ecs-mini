package com.pratilipi.data.type.doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pratilipi.common.type.UserFollowState;
import com.pratilipi.data.type.UserAuthorDoc;
import com.pratilipi.data.type.UserFollowsDoc;

public class UserFollowsDocImpl implements UserFollowsDoc {

	private Map<UserFollowState, List<UserAuthorDocImpl>> follows;

	
	@Override
	public List<UserAuthorDoc> getFollows( UserFollowState state ) {
		if( follows != null && follows.containsKey( state ) )
			return new ArrayList<UserAuthorDoc>( follows.get( state ) );
		else
			return new ArrayList<>( 0 );
	}

	@Override
	public void setFollows( UserFollowState state, List<UserAuthorDoc> userFollows ) {
		
		if( state == null || userFollows == null || userFollows.size() == 0 ) {
			if( this.follows != null )
				this.follows.remove( state );
			return;
		}
		
		List<UserAuthorDocImpl> userFollowList = new ArrayList<>( userFollows.size() );
		for( UserAuthorDoc follow : userFollows )
			userFollowList.add( (UserAuthorDocImpl) follow );
		
		if( this.follows == null )
			this.follows = new HashMap<>();
		this.follows.put( state, userFollowList );

	}

}
