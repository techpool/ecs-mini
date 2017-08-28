package com.pratilipi.data.type;

import java.util.Date;

import com.pratilipi.common.type.CommentParentType;
import com.pratilipi.common.type.CommentState;
import com.pratilipi.common.type.ReferenceType;


public interface Comment extends GenericOfyType {

	Long getId();
	
	
	Long getUserId();
	
	void setUserId( Long userId );
	
	CommentParentType getParentType();
	
	void setParentType( CommentParentType parentType );
	
	String getParentId();
	
	Long getParentIdLong();
	
	void setParentId( Long parentId );
	
	void setParentId( String parentId );
	
	ReferenceType getReferenceType();

	void setReferenceType( ReferenceType referenceType );

	String getReferenceId();

	Long getReferenceIdLong();

	void setReferenceId( Long parentId );

	void setReferenceId( String parentId );
	
	
	String getContent();
	
	void setContent( String content );
	
	CommentState getState();
	
	void setState( CommentState state );
	

	Date getCreationDate();
	
	void setCreationDate( Date creationDate );
	
	Date getLastUpdated();
	
	void setLastUpdated( Date lastUpdated );

}