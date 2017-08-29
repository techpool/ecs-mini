package com.pratilipi.common.type;

public enum EmailState {

	PENDING,
	IN_PROGRESS,
	DEFERRED,	// Email not valid anymore (at the time of sending)
	@Deprecated
	INVALID_EMAIL,
	SENT,		// Email successfully sent
	DROPPED		// Permanently dropped e-mail

}
