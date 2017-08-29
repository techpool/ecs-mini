package com.pratilipi.api.impl.pratilipi;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.data.util.PratilipiDataUtil;

@SuppressWarnings("serial")
@Bind(uri = "/pratilipi/tags/update")
public class PratilipiTagsUpdateApi extends GenericApi {

	public static class PostRequest extends GenericRequest {
		List<Long> tagIds; 
		
		List<String> suggestedTags;
		
		PratilipiType type;
		
		@Validate(required=true)
		Long pratilipiId;
		
		public List<Long> getTagIds() {
			return tagIds;
		}
		
		public List<String> getSuggestedTags() {
			return suggestedTags;
		}
		
		public PratilipiType getPratilipiType() {
			return type;
		}
		
		public Long getPratilipiId() {
			return pratilipiId;
		}
	}
	
	public static class PostResponse extends GenericResponse {
		Boolean isUpdated;
		
		public PostResponse() {}
		
		public PostResponse(Boolean isUpdated) {
			this.isUpdated = isUpdated;
		}
	}
	
	
	
	@Post
	public PostResponse updatePratilipiTags(PostRequest request) 
		throws InsufficientAccessException, UnexpectedServerException {
		
		List<Long> tagIds = request.getTagIds();
		List<String> suggestedTags = request.getSuggestedTags();
		PratilipiType pratilipiType = request.getPratilipiType();
		Long pratilipiId = request.getPratilipiId();
		
		Logger.getLogger(PratilipiTagsUpdateApi.class.getSimpleName())
				.log(Level.INFO, "TagIds : " + tagIds);

		Logger.getLogger(PratilipiTagsUpdateApi.class.getSimpleName())
		.log(Level.INFO, "SuggestedTags : " + suggestedTags);

		if(tagIds == null && suggestedTags == null)
			throw new UnexpectedServerException("Both tagIds and suggestedTags cannot be null");
		
		boolean isUpdated = PratilipiDataUtil.updatePratilipiTags(pratilipiId, pratilipiType, tagIds, suggestedTags);
		
		return new PostResponse(isUpdated);
	}
	
}
