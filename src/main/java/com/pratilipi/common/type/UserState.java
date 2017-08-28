package com.pratilipi.common.type;

public enum UserState {
	
	GUEST,
	REFERRAL,
	REGISTERED,	// User account NOT active - email verification required
	ACTIVE,		// User account active with verified email
	BLOCKED,	// User account blocked by Moderator/System
	DELETED,	// User account deleted by User
	
}
