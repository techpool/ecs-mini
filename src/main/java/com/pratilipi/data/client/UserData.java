package com.pratilipi.data.client;

import java.io.Serializable;
import java.util.Date;

import com.pratilipi.common.type.Gender;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.UserState;

public class UserData implements Serializable {

	private static final long serialVersionUID = 5175279997821871117L;

	
	private Long userId;

	private AuthorData author;
	
	private String facebookId;

	private String googleId;

	private Boolean password;
	
	private String firstName;
	
	private String lastName;

	private String displayName;

	
	private Gender gender;

	private String dateOfBirth;
	
	
	private String email;
	private boolean hasEmail;

	private String phone;
	private boolean hasPhone;
	
	private Language language;
	private boolean hasLanguage;

	
	private UserState state;

	
	private String profilePageUrl;
	
	private String profileImageUrl;
	
	
	private Long signUpDateMillis;

	
	private Long followCount;

	private Boolean following;

	
	private String firebaseToken;

	
	
	public UserData() {}

	public UserData( Long userId ) {
		this.userId = userId;
	}

	
	public Long getId() {
		return userId;
	}

	public void setId( Long id ) {
		this.userId = id;
	}
	
	public AuthorData getAuthor() {
		return author;
	}

	public void setAuthor( AuthorData author ) {
		this.author = author;
	}
	
	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId( String facebookId ) {
		this.facebookId = facebookId;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId( String googleId ) {
		this.googleId = googleId;
	}
	
	public Boolean hasPassword() {
		return password;
	}

	public void setPassword( Boolean password ) {
		this.password = password ;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String firstName ) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName( String lastName ) {
		this.lastName = lastName;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName( String displayName ) {
		this.displayName = displayName;
	}

	
	public Gender getGender() {
		return gender;
	}

	public void setGender( Gender gender ) {
		this.gender = gender;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth( String dateOfBirth ) {
		this.dateOfBirth = dateOfBirth;
	}
	
	
	public String getEmail() {
		return email == null || email.trim().isEmpty() ? null : email.trim().toLowerCase();
	}

	public void setEmail( String email ) {
		this.email = email;
		this.hasEmail = true;
	}
	
	public boolean hasEmail() {
		return hasEmail;
	}

	public String getPhone() {
		return phone == null ? null : phone.trim();
	}

	public void setPhone( String phone ) {
		this.phone = phone;
		this.hasPhone = true;
	}
	
	public boolean hasPhone() {
		return hasPhone;
	}
	
	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage( Language language ) {
		this.language = language;
		this.hasLanguage = true;
	}
	
	public boolean hasLanguage() {
		return hasLanguage;
	}
	
	
	public UserState getState() {
		return state;
	}

	public void setState( UserState state ) {
		this.state = state;
	}
	

	public String getProfilePageUrl() {
		return profilePageUrl;
	}

	public void setProfilePageUrl( String profilePageUrl ) {
		this.profilePageUrl = profilePageUrl;
	}
	
	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl( String profileImageUrl ) {
		this.profileImageUrl = profileImageUrl;
	}
	

	public Date getSignUpDate() {
		return signUpDateMillis == null ? null : new Date( signUpDateMillis );
	}

	public void setSignUpDate( Date signUpDate ) {
		this.signUpDateMillis = signUpDate == null ? null : signUpDate.getTime();
	}

	
	public Long getFollowCount() {
		return followCount;
	}
	
	public void setFollowCount( Long followCount ) {
		this.followCount = followCount;
	}

	
	public boolean isFollowing() {
		return following == null ? false : following;
	}

	public void setFollowing( boolean isFollowing ) {
		this.following = isFollowing;
	}


	public String getFirebaseToken() {
		return firebaseToken;
	}

	public void setFirebaseToken( String firebaseToken ) {
		this.firebaseToken = firebaseToken;
	}

}
