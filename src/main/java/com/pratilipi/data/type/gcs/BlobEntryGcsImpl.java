package com.pratilipi.data.type.gcs;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.tools.cloudstorage.GcsFileMetadata;
import com.google.cloud.storage.Blob;
import com.pratilipi.data.type.BlobEntry;

@SuppressWarnings("serial")
public class BlobEntryGcsImpl implements BlobEntry {
	
	private String fileName;
	private byte[] data;
	private long length;
	private String mimeType;
	private String cacheControl;
	private String eTag;
	private Date lastModified;
	private Map<String, String> metaData;
	
	
	
	public BlobEntryGcsImpl( String fileName ) {
		this.fileName = fileName;
	}
	
	public BlobEntryGcsImpl( String fileName, byte[] data, String mimeType ) {
		this.fileName = fileName;
		this.data = data;
		this.mimeType = mimeType;
	}
	
	public BlobEntryGcsImpl( ByteBuffer byteBuffer, GcsFileMetadata gcsFileMetadata ) {
		this.fileName = gcsFileMetadata.getFilename().getObjectName();
		this.data = byteBuffer.array();
		this.length = gcsFileMetadata.getLength();
		this.mimeType = gcsFileMetadata.getOptions().getMimeType();
		this.cacheControl = gcsFileMetadata.getOptions().getCacheControl();
		this.eTag = gcsFileMetadata.getEtag();
		this.lastModified = gcsFileMetadata.getLastModified();
		this.metaData = gcsFileMetadata.getOptions().getUserMetadata();
	}

	public BlobEntryGcsImpl( Blob blob ) {
		this.fileName	  = blob.getName();
		this.data		  = blob.getContent();
		this.length		  = blob.getSize();
		this.mimeType	  = blob.getContentType();
		this.cacheControl = blob.getCacheControl();
		this.eTag		  = blob.getEtag();
		this.lastModified = new Date( blob.getUpdateTime() );
		this.metaData	  = blob.getMetadata();
	}
	
	
	
	@Override
	public String getName() {
		return fileName;
	}
	
	@Override
	public void setName( String fileName ) {
		this.fileName = fileName;
	}

	
	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public void setData( byte[] byteArray ) {
		this.data = byteArray;
	}
	
	@Override
	public long getDataLength() {
		return length;
	}
	

	@Override
	public String getMimeType() {
		return mimeType;
	}
	
	@Override
	public void setMimeType( String mimeType ) {
		this.mimeType = mimeType;
	}
	
	
	@Override
	public String getMetaName() {
		if( metaData == null )
			return null;
		return metaData.get( META_NAME );
	}
	
	@Override
	public void setMetaName( String name ) {
		if( metaData == null )
			metaData = new HashMap<>( 0 );
		metaData.put( META_NAME, name );
	}
	
	
	@Override
	public String getCacheControl() {
		return cacheControl;
	}

	@Override
	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}
	
	@Override
	public String getETag() {
		return eTag;
	}

	
	@Override
	public Date getLastModified() {
		return lastModified;
	}

}
