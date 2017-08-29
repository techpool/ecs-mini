package com.pratilipi.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.type.BlobEntry;

public class BlobAccessorWithMemcache implements BlobAccessor {

	private static final Logger logger = 
			Logger.getLogger( BlobAccessorWithMemcache.class.getName() );

	
	private final static String PREFIX = "BlobEntry-";

	private final static int SEGMENT_SIZE = 1000 * 1024; // bytes

	private final BlobAccessor blobAccessor;
	private final Memcache memcache;


	public BlobAccessorWithMemcache( BlobAccessor blobAccessor, Memcache memcache ) {
		this.blobAccessor = blobAccessor;
		this.memcache = memcache;
	}
	
	
	private BlobEntry memcacheGet( String fileName ) {
		
		BlobEntry blobEntry = memcache.get( PREFIX + fileName );

		if( blobEntry == null )
			return null;
		
		if( blobEntry.getData().length == blobEntry.getDataLength() )
			return blobEntry;

		
		int dataLength = (int) blobEntry.getDataLength();
		
		List<String> keyList = new ArrayList<String>( (int) Math.ceil( (double) dataLength / SEGMENT_SIZE ) );
		for( int i = 1; i < (int) Math.ceil( (double) dataLength / SEGMENT_SIZE ); i++ )
			keyList.add( PREFIX + fileName + "?" + i );
		
		Map<String, Serializable> keySegmentMap = memcache.getAll( keyList );

		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write( blobEntry.getData() );
			for( String key : keyList ) {
				byte[] dataSegment = (byte[]) keySegmentMap.get( key );
				if( dataSegment == null )
					return null;
				baos.write( dataSegment );
			}
		} catch( IOException e ) {
			logger.log( Level.SEVERE, "Failed to load and stitch blob data.", e );
			return null;
		}
		
		
		byte[] blobData = baos.toByteArray();
		if( blobData.length != dataLength ) {
			logger.log( Level.SEVERE, "Blob size (" + blobData.length + ") did not match expected size (" + dataLength + ")" );
			return null;
		}
		blobEntry.setData( blobData );
		
		
		return blobEntry;
		
	}
	
	private void memcachePut( BlobEntry blobEntry ) {
		
		if( blobEntry == null )
			return;
		
		
		if( blobEntry.getDataLength() <= 1000 * 1024 ) {
			memcache.put( PREFIX + blobEntry.getName(), blobEntry );
			return;
		}

		
		// blobEntry.getData().length > 1000 * 1024
		byte[] blobData = blobEntry.getData();
		int dataLength = (int) blobEntry.getDataLength();

		blobEntry.setData( Arrays.copyOfRange( blobData, 0, SEGMENT_SIZE ) );
		
		Map<String, Serializable> keySegmentMap = new HashMap<>();
		keySegmentMap.put( PREFIX + blobEntry.getName(), blobEntry );
		for( int i = 1; i < (int) Math.ceil( (double) dataLength / SEGMENT_SIZE ); i++ ) {
			int fromIndex = i * SEGMENT_SIZE;
			int toIndex = (i + 1) * SEGMENT_SIZE;
			toIndex = toIndex > dataLength ? dataLength : toIndex;
			keySegmentMap.put( PREFIX + blobEntry.getName() + "?" + i, Arrays.copyOfRange( blobData, fromIndex, toIndex ) );
		}
		
		memcache.putAll( keySegmentMap );

		blobEntry.setData( blobData );
		
	}
	
	
	@Override
	public BlobEntry newBlob( String fileName ) {
		return blobAccessor.newBlob( fileName );
	}

	@Override
	public BlobEntry newBlob( String fileName, byte[] data, String mimeType ) {
		return blobAccessor.newBlob( fileName, data, mimeType );
	}

	@Override
	public BlobEntry getBlob( String fileName ) throws UnexpectedServerException {
		BlobEntry blobEntry = memcacheGet( fileName );
		if( blobEntry == null ) {
			blobEntry = blobAccessor.getBlob( fileName );
			memcachePut( blobEntry );
		}
		return blobEntry;
	}
	
	@Override
	public BlobEntry createOrUpdateBlob( BlobEntry blobEntry ) throws UnexpectedServerException {
		String fileName = blobEntry.getName();
		blobEntry = blobAccessor.createOrUpdateBlob( blobEntry );
		if( blobEntry == null ) // TODO: Remove this as soon as older impl of GcsImpl is removed
			memcache.remove( PREFIX + fileName );
		else
			memcachePut( blobEntry );
		return blobEntry;
	}

	@Override
	public boolean deleteBlob( BlobEntry blobEntry ) {
		boolean deleted = blobAccessor.deleteBlob( blobEntry );
		memcache.remove( PREFIX + blobEntry.getName() );
		return deleted;
	}

}