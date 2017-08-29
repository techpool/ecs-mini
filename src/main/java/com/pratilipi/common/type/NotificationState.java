package com.pratilipi.common.type;

public enum NotificationState {

	UNREAD,	// Mostly new
	READ,	// Marked READ by User - changes to UNREAD on update
	MUTED,	// Marked MUTED by User - doesn't change state on update
	
}