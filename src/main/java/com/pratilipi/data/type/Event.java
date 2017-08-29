package com.pratilipi.data.type;

import java.util.Date;
import java.util.List;

import com.pratilipi.common.type.Language;

public interface Event extends GenericOfyType {

	Long getId();
	
	String getName();
	
	void setName( String name );
	
	String getNameEn();
	
	void setNameEn( String nameEn );
	

	Language getLanguage();
	
	void setLanguage( Language language );
	
	String getDescription();

	void setDescription( String description );

	List<Long> getPratilipiIdList();
	
	void setPratilipiIdList( List<Long> pratilipiIdList );

		
	Date getCreationDate();
	
	void setCreationDate( Date creationDate );
	
	Date getLastUpdated();

	void setLastUpdated( Date lastUpdated );
	
}
