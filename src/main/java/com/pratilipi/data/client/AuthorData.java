package com.pratilipi.data.client;

import java.io.Serializable;
import java.util.Date;

import com.pratilipi.common.type.AuthorState;
import com.pratilipi.common.type.Gender;
import com.pratilipi.common.type.Language;

public class AuthorData implements Serializable {

	private static final long serialVersionUID = -8877405689421212298L;


	private Long authorId;

	private UserData user;
	
	
	private String firstName;
	private boolean hasFirstName;
	
	private String lastName;
	private boolean hasLastName;
	
	private String penName;
	private boolean hasPenName;

	private String name;

	private String fullName;

	
	private String firstNameEn;
	private boolean hasFirstNameEn;
	
	private String lastNameEn;
	private boolean hasLastNameEn;
	
	private String penNameEn;
	private boolean hasPenNameEn;
	
	private String nameEn;

	private String fullNameEn;


	private Gender gender;
	private boolean hasGender;
	
	private String dateOfBirth;
	private boolean hasDateOfBirth;

	
	private Language language;
	private boolean hasLanguage;
	
	private String location;
	private boolean hasLocation;
	
	private String profileFacebook;
	private boolean hasProfileFacebook;
	
	private String profileTwitter;
	private boolean hasProfileTwitter;

	private String profileGooglePlus;
	private boolean hasProfileGooglePlus;

	
	private String summary;
	private boolean hasSummary;
	

	private AuthorState state;
	private boolean hasState;

	
	private String pageUrl;
	@Deprecated
	private String imageUrl;
	private Boolean hasCoverImage;
	private String coverImageUrl;
	private Boolean hasProfileImage;
	private String profileImageUrl;

	
	private Long registrationDateMillis;
	
	private Long followCount;
	
	private Integer contentDrafted;
	
	private Integer contentPublished;
	
	private Long totalReadCount;
	
	private Long totalFbLikeShareCount;

	
	private Boolean following;
	private Boolean hasAccessToUpdate;
	
	
	
	public AuthorData() {}
	
	public AuthorData( Long authorId ) {
		this.authorId = authorId;
	}
	
	
	public Long getId() {
		return authorId;
	}

	public void setId( Long id ) {
		this.authorId = id;
	}

	public UserData getUser() {
		return user;
	}

	public void setUser( UserData user ) {
		this.user = user;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String firstName ) {
		this.firstName = firstName;
		this.hasFirstName = true;
	}

	public boolean hasFirstName() {
		return hasFirstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName( String lastName ) {
		this.lastName = lastName;
		this.hasLastName = true;
	}
	
	public boolean hasLastName() {
		return hasLastName;
	}

	public String getPenName() {
		return penName;
	}

	public void setPenName( String penName ) {
		this.penName = penName;
		this.hasPenName = true;
	}
	
	public boolean hasPenName() {
		return hasPenName;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName( String fullName ) {
		this.fullName = fullName;
	}

	public String getFirstNameEn() {
		return firstNameEn;
	}

	public void setFirstNameEn( String firstNameEn ) {
		this.firstNameEn = firstNameEn;
		this.hasFirstNameEn = true;
	}
	
	public boolean hasFirstNameEn() {
		return hasFirstNameEn;
	}

	public String getLastNameEn() {
		return lastNameEn;
	}

	public void setLastNameEn( String lastNameEn ) {
		this.lastNameEn = lastNameEn;
		this.hasLastNameEn = true;
	}
	
	public boolean hasLastNameEn() {
		return hasLastNameEn;
	}

	public String getPenNameEn() {
		return penNameEn;
	}

	public void setPenNameEn( String penNameEn ) {
		this.penNameEn = penNameEn;
		this.hasPenNameEn = true;
	}
	
	public boolean hasPenNameEn() {
		return hasPenNameEn;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn( String nameEn ) {
		this.nameEn = nameEn;
	}

	public String getFullNameEn() {
		return fullNameEn;
	}

	public void setFullNameEn( String fullNameEn ) {
		this.fullNameEn = fullNameEn;
	}


	public Gender getGender() {
		return gender;
	}

	public void setGender( Gender gender ) {
		this.gender = gender;
		this.hasGender = true;
	}
	
	public boolean hasGender() {
		return hasGender;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth( String dateOfBirth ) {
		this.dateOfBirth = dateOfBirth;
		this.hasDateOfBirth = true;
	}
	
	public boolean hasDateOfBirth() {
		return hasDateOfBirth;
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

	public String getLocation() {
		return location;
	}
	
	public void setLocation( String location ) {
		this.location = location;
		this.hasLocation = true;
	}
	
	public boolean hasLocation() {
		return hasLocation;
	}

	public String getProfileFacebook() {
		return profileFacebook;
	}
	
	public void setProfileFacebook( String profileFacebook ) {
		this.profileFacebook = profileFacebook;
		this.hasProfileFacebook = true;
	}
	
	public boolean hasProfileFacebook() {
		return hasProfileFacebook;
	}

	public String getProfileTwitter() {
		return profileTwitter;
	}
	
	public void setProfileTwitter( String profileTwitter ) {
		this.profileTwitter = profileTwitter;
		this.hasProfileTwitter = true;
	}
	
	public boolean hasProfileTwitter() {
		return hasProfileTwitter;
	}

	public String getProfileGooglePlus() {
		return profileGooglePlus;
	}
	
	public void setProfileGooglePlus( String profileGooglePlus ) {
		this.profileGooglePlus = profileGooglePlus;
		this.hasProfileGooglePlus = true;
	}
	
	public boolean hasProfileGooglePlus() {
		return hasProfileGooglePlus;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary( String summary ) {
		this.summary = summary;
		this.hasSummary = true;
	}
	
	public boolean hasSummary() {
		return this.hasSummary;
	}

	
	public AuthorState getState() {
		return state;
	}

	public void setState( AuthorState state ) {
		this.state = state;
		this.hasState = true;
	}
	
	public boolean hasState() {
		return this.hasState;
	}

	
	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl( String pageUrl ) {
		this.pageUrl = pageUrl;
	}

	@Deprecated
	public String getImageUrl() {
		return imageUrl;
	}

	@Deprecated
	public String getImageUrl( int width ) {
		return imageUrl.indexOf( '?' ) == -1
				? imageUrl + "?width=" + width
				: imageUrl + "&width=" + width;
	}

	@Deprecated
	public void setImageUrl( String imageUrl ) {
		this.imageUrl = imageUrl;
	}

	public Boolean hasCoverImage() {
		return hasCoverImage;
	}

	public void setHasCoverImage( Boolean hasCoverImage ) {
		this.hasCoverImage = hasCoverImage;
	}
	
	public String getCoverImageUrl() {
		return coverImageUrl;
	}

	public String getCoverImageUrl( int width ) {
		return coverImageUrl.indexOf( '?' ) == -1
				? coverImageUrl + "?width=" + width
				: coverImageUrl + "&width=" + width;
	}

	public void setCoverImageUrl( String coverImageUrl ) {
		this.coverImageUrl = coverImageUrl;
	}

	
	public Boolean hasProfileImage() {
		return hasProfileImage;
	}

	public void setHasProfileImage( Boolean hasProfileImage ) {
		this.hasProfileImage = hasProfileImage;
	}
	
	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getProfileImageUrl( int width ) {
		return profileImageUrl.indexOf( '?' ) == -1
				? profileImageUrl + "?width=" + width
				: profileImageUrl + "&width=" + width;
	}

	public void setProfileImageUrl( String profileImageUrl ) {
		this.profileImageUrl = profileImageUrl;
	}

	
	public Date getRegistrationDate() {
		return registrationDateMillis == null ? null : new Date( registrationDateMillis );
	}

	public void setRegistrationDate( Date registrationDate ) {
		this.registrationDateMillis = registrationDate == null ? null : registrationDate.getTime();
	}
	
	public Long getFollowCount() {
		return followCount;
	}
	
	public void setFollowCount( Long followCount ) {
		this.followCount = followCount;
	}

	public Integer getContentDrafted() {
		return contentDrafted;
	}

	public void setContentDrafted( Integer contentDrafted ) {
		this.contentDrafted = contentDrafted;
	}

	public Integer getContentPublished() {
		return contentPublished;
	}

	public void setContentPublished( Integer contentPublished ) {
		this.contentPublished = contentPublished;
	}

	public Long getTotalReadCount() {
		return totalReadCount;
	}

	public void setTotalReadCount( Long totalReadCount ) {
		this.totalReadCount = totalReadCount;
	}

	public Long getTotalFbLikeShareCount() {
		return totalFbLikeShareCount;
	}

	public void setTotalFbLikeShareCount( Long totalFbLikeShareCount ) {
		this.totalFbLikeShareCount = totalFbLikeShareCount;
	}
	
	public boolean isFollowing() {
		return following == null ? false : following;
	}

	public void setFollowing( boolean isFollowing ) {
		this.following = isFollowing;
	}
	
	public boolean hasAccessToUpdate() {
		return hasAccessToUpdate == null ? false : hasAccessToUpdate;
	}

	public void setAccessToUpdate( boolean hasAccessToUpdate ) {
		this.hasAccessToUpdate = hasAccessToUpdate;
	}
	
}