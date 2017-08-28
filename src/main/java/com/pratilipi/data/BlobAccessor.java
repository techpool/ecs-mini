package com.pratilipi.data;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.type.BlobEntry;

public interface BlobAccessor {
	
	// TODO: Remove UnexpectedServerException from following methods
	
	BlobEntry newBlob( String fileName );

	BlobEntry newBlob( String fileName, byte[] data, String mimeType );

	BlobEntry getBlob( String fileName ) throws UnexpectedServerException;

	BlobEntry createOrUpdateBlob( BlobEntry blobEntry ) throws UnexpectedServerException;
	
	boolean deleteBlob( BlobEntry blobEntry );

}
