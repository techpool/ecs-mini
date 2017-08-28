package com.pratilipi.data;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BlobInfo.Builder;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.type.gcs.BlobEntryGcsImpl;

public class BlobAccessorGcsImpl2 implements BlobAccessor {

	private final Storage gcsService = StorageOptions.getDefaultInstance().getService();

	private final String bucketName;

	
	public BlobAccessorGcsImpl2( String bucketName ) {
		this.bucketName = bucketName;
	}

	
	@Override
	public BlobEntry newBlob( String fileName ) {
		return new BlobEntryGcsImpl( fileName );
	}

	@Override
	public BlobEntry newBlob( String fileName, byte[] data, String mimeType ) {
		return new BlobEntryGcsImpl( fileName, data, mimeType );
	}

	@Override
	public BlobEntry getBlob( String fileName ) {
		Blob blob = gcsService.get( BlobId.of( bucketName, fileName ) );
		return blob == null || blob.getSize() == 0 ? null : new BlobEntryGcsImpl( blob );
	}

	@Override
	public BlobEntry createOrUpdateBlob( BlobEntry blobEntry ) {
		
		BlobId blobId = BlobId.of( bucketName, blobEntry.getName() );
		Builder blobInfoBuilder = BlobInfo.newBuilder( blobId );
		
		if( blobEntry.getMimeType() != null )
			blobInfoBuilder.setContentType( blobEntry.getMimeType() );
		
		if( blobEntry.getCacheControl() != null )
			blobInfoBuilder.setCacheControl( blobEntry.getCacheControl() );
		
		if( blobEntry.getMetaName() != null ) {
			Map<String, String> metadata = new HashMap<>();
			metadata.put( BlobEntry.META_NAME, blobEntry.getMetaName() );
			blobInfoBuilder.setMetadata( metadata );
		}
		
		return new BlobEntryGcsImpl( gcsService.create(
				blobInfoBuilder.build(),
				blobEntry.getData() ) );
		
	}

	@Override
	public boolean deleteBlob( BlobEntry blobEntry ) {
		return gcsService.delete( BlobId.of( bucketName, blobEntry.getName() ) );
	}

}
