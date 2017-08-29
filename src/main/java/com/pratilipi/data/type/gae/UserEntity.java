package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.UserCampaign;
import com.pratilipi.common.type.UserSignUpSource;
import com.pratilipi.common.type.UserState;
import com.pratilipi.common.type.UserStatus;
import com.pratilipi.data.type.User;

@SuppressWarnings("serial")
@Cache
@Entity( name = "USER" )
public class UserEntity implements User {
	
	@Id
	private Long USER_ID;
	
	@Index( IfNotNull.class )
	private String FACEBOOK_ID;

	@Index( IfNotNull.class )
	private String GOOGLE_ID;
	
	@Index
	private String PASSWORD;
	

	@Index( IfNotNull.class )
	private String EMAIL;
	
	@Index( IfNotNull.class )
	private String PHONE;
	
	@Index( IfNotNull.class )
	private Language LANGUAGE;
	
	
	@Index( IfNotNull.class )
	private String VERIFICATION_TOKEN;

	@Deprecated
	@Index( IfNotNull.class )
	private UserStatus STATUS;
	
	@Index
	private UserState STATE;
	
	
	@Index( IfNotNull.class )
	private String CAMPAIGN;
	
	@Index( IfNotNull.class )
	private String REFERER;
	
	@Index
	private Date SIGN_UP_DATE;
	
	@Index
	private UserSignUpSource SIGN_UP_SOURCE;
	
	@Index
	private Date LAST_UPDATED;
	

	@Index( IfNotNull.class )
	private Date LAST_EMAILED;
	
	
	@Index
	private Long FOLLOW_COUNT;
	
	@Index
	private Date _TIMESTAMP_;
	

	
	public UserEntity() {}
	
	public UserEntity( Long id ) {
		this.USER_ID = id;
	}
	
	
	@Override
	public Long getId() {
		return USER_ID;
	}
	
	public void setId( Long id ) {
		this.USER_ID = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}

	@Override
	public <T> void setKey( Key<T> key ) {
		this.USER_ID = key.getId();
	}

	@Override
	public String getFacebookId() {
		return FACEBOOK_ID;
	}
	
	@Override
	public void setFacebookId( String facebookId ) {
		this.FACEBOOK_ID = facebookId;
	}

	@Override
	public String getGoogleId() {
		return GOOGLE_ID;
	}
	
	@Override
	public void setGoogleId( String googleId ) {
		this.GOOGLE_ID = googleId;
	}
	
	@Override
	public String getPassword() {
		return PASSWORD;
	}
	
	@Override
	public void setPassword( String password ) {
		this.PASSWORD = password;
	}
	
	
	@Override
	public String getEmail() {
		return EMAIL;
	}
	
	@Override
	public void setEmail( String email ) {
		this.EMAIL = email;
	}
	
	@Override
	public String getPhone() {
		return PHONE;
	}
	
	@Override
	public void setPhone( String phone ) {
		this.PHONE = phone;
	}
	
	@Override
	public Language getLanguage() {
		return LANGUAGE;
	}

	@Override
	public void setLanguage( Language language ) {
		this.LANGUAGE = language;
	}

	
	@Override 
	public String getVerificationToken() {
		return VERIFICATION_TOKEN;
	}
	
	@Override
	public void setVerificationToken( String verificationToken ) {
		this.VERIFICATION_TOKEN = verificationToken;
	}

	@Override
	public UserState getState() {
		return STATE;
	}
	
	@Override
	public void setState( UserState state ) {
		this.STATE = state;
	}
	
	
	@Override
	public UserCampaign getCampaign() {
		return CAMPAIGN == null ? null : UserCampaign.fromString( CAMPAIGN );
	}
	
	@Override
	public void setCampaign( UserCampaign campaign ) {
		this.CAMPAIGN = campaign == null ? null : campaign.toString();
	}
	
	@Override
	public String getReferrer() {
		return REFERER;
	}
	
	@Override
	public void setReferrer( String referrer ) {
		this.REFERER = referrer;
	}
	
	@Override
	public Date getSignUpDate() {
		return SIGN_UP_DATE;
	}
	
	@Override
	public void setSignUpDate( Date signUpDate ) {
		this.SIGN_UP_DATE = signUpDate;
	}
	
	@Override
	public UserSignUpSource getSignUpSource() {

		if( CAMPAIGN != null && CAMPAIGN.equals( "Publisher:5684064812007424" ) ) {
			SIGN_UP_SOURCE = null;
		} else if( SIGN_UP_SOURCE == null && STATUS != null ) {
			switch( STATUS ) {
				case PRELAUNCH_REFERRAL:
				case PRELAUNCH_SIGNUP:
					SIGN_UP_SOURCE = UserSignUpSource.PRE_LAUNCH_WEBSITE;
					break;
				case POSTLAUNCH_REFERRAL:
				case POSTLAUNCH_SIGNUP:
					SIGN_UP_SOURCE = UserSignUpSource.WEBSITE;
					break;
				case POSTLAUNCH_SIGNUP_SOCIALLOGIN:
					SIGN_UP_SOURCE = UserSignUpSource.WEBSITE_FACEBOOK;
					break;
				case ANDROID_SIGNUP:
					SIGN_UP_SOURCE = UserSignUpSource.ANDROID_APP;
					break;
				case ANDROID_SIGNUP_FACEBOOK:
					SIGN_UP_SOURCE = UserSignUpSource.ANDROID_APP_FACEBOOK;
					break;
				case ANDROID_SIGNUP_GOOGLE:
					SIGN_UP_SOURCE = UserSignUpSource.ANDROID_APP_GOOGLE;
					break;
			}
		}
		
		return SIGN_UP_SOURCE;
		
	}
	
	@Override
	public void setSignUpSource( UserSignUpSource signUpSource ) {
		this.SIGN_UP_SOURCE = signUpSource;
	}
		
	@Override
	public Date getLastUpdated() {
		return LAST_UPDATED == null ? SIGN_UP_DATE : LAST_UPDATED;
	}
	
	@Override
	public void setLastUpdated( Date lastUpdated ) {
		this.LAST_UPDATED = lastUpdated;
	}
	

	@Override
	public Date getLastEmailedDate() {
		return LAST_EMAILED == null ? SIGN_UP_DATE: LAST_EMAILED;
	}
	
	@Override
	public void setLastEmailedDate( Date lastEmailed ) {
		this.LAST_EMAILED = lastEmailed;
	}

	
	@Override
	public Long getFollowCount() {
		return FOLLOW_COUNT == null ? 0 : FOLLOW_COUNT;
	}
	
	@Override
	public void setFollowCount( Long followCount ) {
		this.FOLLOW_COUNT = followCount;
	}
	
	
	@Override
	public Date getTimestamp() {
		return _TIMESTAMP_;
	}

	@Override
	public void setTimestamp( Date timestamp ) {
		this._TIMESTAMP_ = timestamp;
	}	

}
