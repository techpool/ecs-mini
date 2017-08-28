package com.pratilipi.data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.tools.cloudstorage.GcsFileMetadata;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFileOptions.Builder;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.type.gcs.BlobEntryGcsImpl;

public class BlobAccessorGcsImpl implements BlobAccessor {

	private static final Logger logger =
			Logger.getLogger( BlobAccessorGcsImpl.class.getName() );

	private static final GcsService gcsService =
			GcsServiceFactory.createGcsService( RetryParams.getDefaultInstance() );
	
	private final String bucketName;
	
	
	public BlobAccessorGcsImpl( String bucketName ) {
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
	public BlobEntry getBlob( String fileName ) throws UnexpectedServerException {
		
		GcsFilename gcsFileName
				= new GcsFilename( bucketName, fileName );
		
		try {

			GcsFileMetadata gcsFileMetadata
					= gcsService.getMetadata( gcsFileName );
			
			if( gcsFileMetadata == null )
				return null;
			
			if( gcsFileMetadata.getLength() == 0 )
				return null;
			
			GcsInputChannel gcsInputChannel
					= gcsService.openReadChannel( gcsFileName, 0 );
			
			ByteBuffer byteBuffer
					= ByteBuffer.allocate( (int) gcsFileMetadata.getLength() );
			gcsInputChannel.read( byteBuffer );
		
			if( byteBuffer.position() != gcsFileMetadata.getLength() ) {
				logger.log( Level.SEVERE, "Byte buffer size of " + byteBuffer.position() + " is not same as content lenght of " + gcsFileMetadata.getLength() );
				throw new UnexpectedServerException();
			}
			
			return new BlobEntryGcsImpl( byteBuffer, gcsFileMetadata );
			
		} catch( IOException ex ) {
			logger.log( Level.INFO, "Failed to fetch blob with name '" + fileName + "'", ex );
			throw new UnexpectedServerException();
		}
		
	}

	@Override
	public BlobEntry createOrUpdateBlob( BlobEntry blobEntry ) throws UnexpectedServerException {
		
		GcsFilename gcsFileName
				= new GcsFilename( bucketName, blobEntry.getName() );

		Builder builder = new GcsFileOptions.Builder();
		if( blobEntry.getMimeType() != null )
			builder.mimeType( blobEntry.getMimeType() );
		if( blobEntry.getCacheControl() != null )
			builder.cacheControl( blobEntry.getCacheControl() );
		if( blobEntry.getMetaName() != null )
			builder.addUserMetadata( BlobEntry.META_NAME, blobEntry.getMetaName() );
		GcsFileOptions gcsFileOptions = builder.build();

		try {
			GcsOutputChannel gcsOutputChannel = gcsService
					.createOrReplace( gcsFileName , gcsFileOptions );
			gcsOutputChannel.write( ByteBuffer.wrap( blobEntry.getData() ) );
			gcsOutputChannel.close();
		} catch( IOException ex ) {
			logger.log( Level.INFO, "Failed to create/update blob with name '" + blobEntry.getName() + "'", ex );
			throw new UnexpectedServerException();
		}
		
		return null;
		
	}

	@Override
	public boolean deleteBlob( BlobEntry blobEntry ) {
		try {
			gcsService.delete( new GcsFilename( bucketName, blobEntry.getName() ) );
			return true;
		} catch( IOException ex ) {
			logger.log( Level.INFO, "Failed to delete blob with name '" + blobEntry.getName() + "'", ex );
			return false;
		}
	}

}
