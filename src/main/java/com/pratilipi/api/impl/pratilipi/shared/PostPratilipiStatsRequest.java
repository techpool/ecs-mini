package com.pratilipi.api.impl.pratilipi.shared;

import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;

public class PostPratilipiStatsRequest extends GenericRequest {
	
	@Validate( required = true, minLong = 1L )
	private Long pratilipiId;
	
	private Long readCountOffset;
	private Long readCount;

	private Long fbLikeShareCountOffset;
	private Long fbLikeShareCount;
	
	
	public Long getPratilipiId() {
		return pratilipiId;
	}
	
	public Long getReadCountOffset() {
		return readCountOffset;
	}

	public Long getReadCount() {
		return readCount;
	}
	
	public Long getFbLikeShareCountOffset() {
		return fbLikeShareCountOffset;
	}
	
	public Long getFbLikeShareCount() {
		return fbLikeShareCount;
	}
	
}
