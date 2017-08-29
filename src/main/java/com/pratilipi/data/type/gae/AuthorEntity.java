package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
import com.pratilipi.common.type.AuthorState;
import com.pratilipi.common.type.Gender;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.type.Author;

@SuppressWarnings("serial")
@Cache
@Entity( name = "AUTHOR" )
public class AuthorEntity implements Author {
	
	@Id
	private Long AUTHOR_ID;

	@Index
	private Long USER_ID;

	
	private String FIRST_NAME;
	
	private String LAST_NAME;

	private String PEN_NAME;
	
	private String FIRST_NAME_EN;
	
	private String LAST_NAME_EN;

	private String PEN_NAME_EN;


	@Index( IfNotNull.class )
	private Gender GENDER;
	
	@Index( IfNotNull.class )
	private String DATE_OF_BIRTH;
	
	
	@Index
	private Language LANGUAGE;

	private String LOCATION;
	
	private String PROFILE_FACEBOOK;
	
	private String PROFILE_TWITTER;

	private String PROFILE_GOOGLE_PLUS;

	private String SUMMARY;
	
	
	@Index
	private AuthorState STATE;
	
	@Index
	private String PROFILE_IMAGE;
	
	@Index
	private String COVER_IMAGE;
	

	@Index
	private Date REGISTRATION_DATE;

	@Index
	private Date LAST_UPDATED;
	
	
	@Index
	private Long FOLLOW_COUNT;

	@Index
	private Integer CONTENT_DRAFTED;
	
	@Index
	private Integer CONTENT_PUBLISHED;
	
	@Index
	private Long TOTAL_READ_COUNT;

	@Index
	private Long TOTAL_FB_LIKE_SHARE_COUNT;

	
	@Index
	private Date _TIMESTAMP_;

	
	public AuthorEntity() {}
	
	public AuthorEntity( Long id ) {
		this.AUTHOR_ID = id;
	}

	
	@Override
	public Long getId() {
		return AUTHOR_ID;
	}

	public void setId( Long id ) {
		this.AUTHOR_ID = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.AUTHOR_ID = key.getId();
	}

	
	@Override
	public Long getUserId() {
		return USER_ID;
	}

	@Override
	public void setUserId( Long userId ) {
		this.USER_ID = userId;
	}

	@Override
	public String getFirstName() {
		return FIRST_NAME;
	}

	@Override
	public void setFirstName( String firstName ) {
		this.FIRST_NAME = firstName;
	}

	@Override
	public String getLastName() {
		return LAST_NAME;
	}

	@Override
	public void setLastName( String lastName ) {
		this.LAST_NAME = lastName;
	}

	@Override
	public String getPenName() {
		return PEN_NAME;
	}

	@Override
	public void setPenName( String penName ) {
		this.PEN_NAME = penName;
	}

	@Override
	public String getFirstNameEn() {
		return FIRST_NAME_EN;
	}

	@Override
	public void setFirstNameEn( String firstNameEn ) {
		this.FIRST_NAME_EN = firstNameEn;
	}

	@Override
	public String getLastNameEn() {
		return LAST_NAME_EN;
	}

	@Override
	public void setLastNameEn( String lastNameEn ) {
		this.LAST_NAME_EN = lastNameEn;
	}

	@Override
	public String getPenNameEn() {
		return PEN_NAME_EN;
	}

	@Override
	public void setPenNameEn( String penNameEn ) {
		this.PEN_NAME_EN = penNameEn;
	}

	
	@Override
	public Gender getGender() {
		return GENDER ;
	}
	
	@Override
	public void setGender( Gender gender ) {
		this.GENDER = gender;
	}
	
	@Override
	public String getDateOfBirth() {
		return DATE_OF_BIRTH;
	}
	
	@Override
	public void setDateOfBirth( String dateOfBirth ) {
		this.DATE_OF_BIRTH = dateOfBirth;
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
	public String getLocation() {
		return LOCATION;
	}
	
	@Override
	public void setLocation( String location ) {
		this.LOCATION = location;
	}
	
	@Override
	public String getProfileFacebook() {
		return PROFILE_FACEBOOK;
	}
	
	@Override
	public void setProfileFacebook( String profileFacebook ) {
		this.PROFILE_FACEBOOK = profileFacebook;
	}
	
	@Override
	public String getProfileTwitter() {
		return PROFILE_TWITTER;
	}
	
	@Override
	public void setProfileTwitter( String profileTwitter ) {
		this.PROFILE_TWITTER = profileTwitter;
	}
	
	@Override
	public String getProfileGooglePlus() {
		return PROFILE_GOOGLE_PLUS;
	}
	
	@Override
	public void setProfileGooglePlus( String profileGooglePlus ) {
		this.PROFILE_GOOGLE_PLUS = profileGooglePlus;
	}
	
	@Override
	public String getSummary() {
		return SUMMARY;
	}

	@Override
	public void setSummary( String summary ) {
		this.SUMMARY = summary;
	}

	
	@Override
	public AuthorState getState() {
		if( STATE == null )
			STATE = AuthorState.ACTIVE;
		return STATE;
	}
	
	@Override
	public void setState( AuthorState state ) {
		this.STATE = state;
	}
	
	@Override
	public String getProfileImage() {
		return PROFILE_IMAGE;
	}

	@Override
	public void setProfileImage( String profileImage ) {
		this.PROFILE_IMAGE = profileImage;
	}
	
	@Override
	public String getCoverImage() {
		return COVER_IMAGE;
	}

	@Override
	public void setCoverImage( String coverImage ) {
		this.COVER_IMAGE = coverImage;
	}
	
	@Override
	public Date getRegistrationDate() {
		return REGISTRATION_DATE;
	}

	@Override
	public void setRegistrationDate( Date registrationDate ) {
		this.REGISTRATION_DATE = registrationDate;
	}

	@Override
	public Date getLastUpdated() {
		return LAST_UPDATED;
	}

	@Override
	public void setLastUpdated( Date lastUpdated ) {
		this.LAST_UPDATED = lastUpdated;
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
	public Integer getContentDrafted() {
		return CONTENT_DRAFTED == null ? 0 : CONTENT_DRAFTED;
	}
	
	@Override
	public void setContentDrafted( Integer contentDrafted ) {
		this.CONTENT_DRAFTED = contentDrafted;
	}
	
	@Override
	public Integer getContentPublished() {
		return CONTENT_PUBLISHED == null ? 0 : CONTENT_PUBLISHED;
	}
	
	@Override
	public void setContentPublished( Integer contentPublished ) {
		this.CONTENT_PUBLISHED = contentPublished;
	}
	
	@Override
	public Long getTotalReadCount() {
		return TOTAL_READ_COUNT == null ? 0L : TOTAL_READ_COUNT;
	}
	
	@Override
	public void setTotalReadCount( Long totalReadCount ) {
		this.TOTAL_READ_COUNT = totalReadCount;
	}
	
	@Override
	public Long getTotalFbLikeShareCount() {
		return TOTAL_FB_LIKE_SHARE_COUNT == null ? 0L : TOTAL_FB_LIKE_SHARE_COUNT;
	}
	
	@Override
	public void setTotalFbLikeShareCount( Long totalFbLikeShareCount ) {
		this.TOTAL_FB_LIKE_SHARE_COUNT = totalFbLikeShareCount;
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
