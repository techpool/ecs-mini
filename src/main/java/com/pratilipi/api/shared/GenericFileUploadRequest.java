package com.pratilipi.api.shared;

import com.pratilipi.api.annotation.Validate;

public class GenericFileUploadRequest extends GenericRequest {

	@Validate( required = true )
	private String name;

	@Validate( required = true )
	private byte[] data;

	@Validate( required = true )
	private String mimeType;

	
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData( byte[] data ) {
		this.data = data;
	}

	public String getMimeType() {
		return mimeType;
	}
	
	public void setMimeType( String mimeType ) {
		this.mimeType = mimeType;
	}
	
}