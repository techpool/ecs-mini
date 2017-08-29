package com.pratilipi.data.type;

import java.io.Serializable;
import java.util.Date;

public interface BlobEntry extends Serializable {
	
	String META_NAME = "X-Name"; // Original file name
	
	
	String getName();
	
	void setName( String name );

	
	byte[] getData();
	
	void setData( byte[] byteArray );
	
	long getDataLength();
	
	
	String getMimeType();

	void setMimeType( String mimeType );

	
	String getMetaName();
	
	void setMetaName( String originalFileName );
	
	
	String getCacheControl();
	
	void setCacheControl( String cacheControl );

	String getETag();
	

	Date getLastModified();
	
}
