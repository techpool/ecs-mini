package com.pratilipi.data.type;

import java.util.Date;

import com.pratilipi.common.type.BlogPostState;
import com.pratilipi.common.type.Language;

public interface BlogPost extends GenericOfyType {

	Long getId();
	
	Long getBlogId();

	void setBlogId( Long blogId );

	
	String getTitle();
	
	void setTitle( String title );
	
	String getTitleEn();
	
	void setTitleEn( String titleEn );
	
	String getContent();

	void setContent( String content );

	Language getLanguage();
	
	void setLanguage( Language language );

	BlogPostState getState();
	
	void setState( BlogPostState state );
	
	
	Long getCreatedBy();
	
	void setCreatedBy( Long userId );
	
	Date getCreationDate();
	
	void setCreationDate( Date creationDate );
	
	Date getLastUpdated();

	void setLastUpdated( Date lastUpdated );
	
}
