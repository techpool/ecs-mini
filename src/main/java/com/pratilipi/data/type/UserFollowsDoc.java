package com.pratilipi.data.type;

import java.util.List;

import com.pratilipi.common.type.UserFollowState;

public interface UserFollowsDoc {

	List<UserAuthorDoc> getFollows( UserFollowState state );

	void setFollows( UserFollowState state, List<UserAuthorDoc> userFollowing );

}
