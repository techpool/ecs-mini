package com.pratilipi.api.shared;

public class GenericFileDownloadResponse extends GenericResponse {

	private byte[] data;
	private String mimeType;
	private String eTag;
	
	
	@SuppressWarnings("unused")
	private GenericFileDownloadResponse() {}
	
	public GenericFileDownloadResponse( byte[] data, String mimeType, String eTag ) {
		this.data = data;
		this.mimeType = mimeType;
		this.eTag = eTag;
	}
	
	
	public byte[] getData() {
		return data;
	}

	public String getMimeType() {
		return mimeType;
	}
	
	public String getETag() {
		return eTag;
	}

}