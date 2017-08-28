package com.pratilipi.api.impl.pratilipi;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.pratilipi.TagsApi.PostRequest;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.util.TagDataUtil;
import com.pratilipi.filter.AccessTokenFilter;

@SuppressWarnings("serial")
@Bind(uri = "/pratilipi/tags/remove")
public class DeleteTagsApi extends GenericApi{

	public static class PostRequest extends GenericRequest {
		
		@Validate(required=true)
		List<Long> ids;
				
		public List<Long> getIds() {
			return ids;
		}
		
	}
	
	@Post
	public GenericResponse addTags(PostRequest request) throws InsufficientAccessException {
		
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		if(accessToken.getUserId() != 5073076857339904L) {
			Logger.getLogger(TagsApi.class.getSimpleName())
					.log(Level.SEVERE, "AccessToken : " + accessToken.getId());
			Logger.getLogger(TagsApi.class.getSimpleName())
					.log(Level.SEVERE, "User Id : " + accessToken.getUserId());
			throw new InsufficientAccessException();
		}
		
		TagDataUtil.removeTags(request.getIds());
		
		return new GenericResponse();
	}
}
