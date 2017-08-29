package com.pratilipi.data.type;

import java.util.Date;

import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiType;

public interface Tag extends GenericOfyType {

	Long getId();


	String getName();

	void setName( String name );

	String getNameEn();

	void setNameEn( String nameEn );


	PratilipiType getType();

	void setType( PratilipiType type );

	Language getLanguage();

	void setLanguage( Language language );
	
	Date getCreationDate();
	
	void setCreationDate(Date date);

}
