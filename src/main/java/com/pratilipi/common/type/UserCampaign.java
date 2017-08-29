package com.pratilipi.common.type;

public enum UserCampaign {

	SOFT_LAUNCH,	// SoftLaunch
	PRE_LAUNCH,		// PreLaunch
	SHA_HARIDHAM,	// Publisher:5684064812007424
	AEE_TEAM,
	;
	
	
	public static UserCampaign fromString( String campaign ) {
		switch( campaign ) {
			case "SoftLaunch":
				return SOFT_LAUNCH;
			case "PreLaunch":
				return PRE_LAUNCH;
			case "Publisher:5684064812007424":
				return SHA_HARIDHAM;
			default:
				return UserCampaign.valueOf( campaign );
		}
	}

	public String toString() {
		switch( this ) {
			case SOFT_LAUNCH:
				return "SoftLaunch";
			case PRE_LAUNCH:
				return "PreLaunch";
			case SHA_HARIDHAM:
				return "Publisher:5684064812007424";
			default:
				return super.toString();
		}
	}
	
}
