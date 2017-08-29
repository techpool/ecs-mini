package com.pratilipi.data.type;

import java.io.Serializable;
import java.util.Date;

import com.pratilipi.common.type.PageType;

public interface Page extends GenericOfyType, Serializable {

	Long getId();

	PageType getType();

	void setType( PageType type );

	String getUri();

	void setUri( String uri );

	String getUriAlias();

	void setUriAlias( String uri );

	Long getPrimaryContentId();

	void setPrimaryContentId( Long pageContentId );

	Date getCreationDate();

	void setCreationDate( Date creationDate );

}