package com.pratilipi.common.util;

import com.pratilipi.common.type.BlogPostState;
import com.pratilipi.common.type.Language;

public class BlogPostFilter {
	
	private Long blogId;
	private Language language;
	private BlogPostState state;
	

	public Long getBlogId() {
		return blogId;
	}

	public void setBlogId( Long blogId ) {
		this.blogId = blogId;
	}
	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage( Language language ) {
		this.language = language;
	}

	public BlogPostState getState() {
		return state;
	}

	public void setState( BlogPostState state ) {
		this.state = state;
	}
	
}
