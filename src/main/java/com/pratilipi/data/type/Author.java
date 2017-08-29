package com.pratilipi.data.type;

import java.io.Serializable;
import java.util.Date;

import com.pratilipi.common.type.AuthorState;
import com.pratilipi.common.type.Gender;
import com.pratilipi.common.type.Language;

public interface Author extends GenericOfyType, Serializable {
	
	Long getId();

	Long getUserId();

	void setUserId( Long userId );

	String getFirstName();
	
	void setFirstName( String firstName );
	
	String getLastName();
	
	void setLastName( String lastName );
	
	String getPenName();
	
	void setPenName( String penName );
	
	String getFirstNameEn();
	
	void setFirstNameEn( String firstNameEn );
	
	String getLastNameEn();
	
	void setLastNameEn( String lastNameEn );
	
	String getPenNameEn();
	
	void setPenNameEn( String penNameEn );

	
	Gender getGender();
	
	void setGender( Gender gender );
	
	String getDateOfBirth();
	
	void setDateOfBirth( String dateOfBirth );
	
	
	Language getLanguage();
	
	void setLanguage( Language language );
	
	String getLocation();
	
	void setLocation( String location );
	
	String getProfileFacebook();
	
	void setProfileFacebook( String profileFacebook );
	
	String getProfileTwitter();
	
	void setProfileTwitter( String profileTwitter );
	
	String getProfileGooglePlus();
	
	void setProfileGooglePlus( String profileGooglePlus );

	String getSummary();
	
	void setSummary( String summary );
	

	AuthorState getState();
	
	void setState( AuthorState state );
	
	String getProfileImage();

	void setProfileImage( String profileImage );
	
	String getCoverImage();
	
	void setCoverImage( String coverImage );
	
	Date getRegistrationDate();
	
	void setRegistrationDate( Date registrationDate );

	Date getLastUpdated();
	
	void setLastUpdated( Date lastUpdated );

	
	Long getFollowCount();
	
	void setFollowCount( Long followCount );
	
	Integer getContentDrafted();
	
	void setContentDrafted( Integer contentDrafted );
	
	Integer getContentPublished();
	
	void setContentPublished( Integer contentPublished );
	
	Long getTotalReadCount();
	
	void setTotalReadCount( Long totalReadCount );
	
	Long getTotalFbLikeShareCount();
	
	void setTotalFbLikeShareCount( Long totalFbLikeShareCount );

	
	Date getTimestamp();
	
	void setTimestamp( Date timestamp );
	
}
