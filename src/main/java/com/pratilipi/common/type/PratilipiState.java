package com.pratilipi.common.type;

public enum PratilipiState {

	DRAFTED,		// Work in progress
	SUBMITTED,		// Final work waiting for approval from Moderator
	PUBLISHED,		// Published work (free or paid)
	DISCONTINUED,	// Work discontinued by Author
	BLOCKED,		// Work blocked by System when Author's profile is BLOCKED
	DELETED,		// Work deleted by User OR by System when Author's profile is DELETED
	
}