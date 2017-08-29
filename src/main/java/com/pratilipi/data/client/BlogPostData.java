package com.pratilipi.data.client;

import java.util.Date;

import com.pratilipi.common.type.BlogPostState;
import com.pratilipi.common.type.Language;

public class BlogPostData {
	
	private Long blogPostId;
	
	private Long blogId;
	private boolean hasBlogId;
	
	
	private String title;
	private boolean hasTitle;
	
	private String titleEn;
	private boolean hasTitleEn;
	
	private String content;
	private boolean hasContent;
	
	private Language language;
	private boolean hasLanguage;

	private BlogPostState state;
	private boolean hasState;
	
	private UserData createdBy;
	
	private Long creationDateMillis;
	private Long lastUpdatedMillis;
	
	
	private String pageUrl;
	
	private Boolean hasAccessToUpdate;

	
	public BlogPostData() {}
	
	public BlogPostData( Long id ) {
		this.blogPostId = id;
	}
	
	
	public Long getId() {
		return blogPostId;
	}

	public void setId( Long id ) {
		this.blogPostId = id;
	}

	public Long getBlogId() {
		return blogId;
	}

	public void setBlogId( Long blogId ) {
		this.blogId = blogId;
		this.hasBlogId = true;
	}

	public boolean hasBlogId() {
		return hasBlogId;
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
		this.hasTitle = true;
	}
	
	public boolean hasTitle() {
		return hasTitle;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn( String titleEn ) {
		this.titleEn = titleEn;
		this.hasTitleEn = true;
	}
	
	public boolean hasTitleEn() {
		return hasTitleEn;
	}

	public String getContent() {
		return content;
	}
	
	public void setContent( String content ) {
		this.content = content;
		this.hasContent = true;
	}

	public boolean hasContent() {
		return hasContent;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage( Language language ) {
		this.language = language;
		this.hasLanguage = true;
	}
	
	public boolean hasLanguage() {
		return hasLanguage;
	}
	
	public BlogPostState getState() {
		return state;
	}
	
	public void setState( BlogPostState state ) {
		this.state = state;
		this.hasState = true;
	}
	
	public boolean hasState() {
		return hasState;
	}
	
	public UserData getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy( UserData user ) {
		this.createdBy = user;
	}
	
	public Date getCreationDate() {
		return new Date( creationDateMillis );
	}
	
	public void setCreationDate( Date creationDate ) {
		this.creationDateMillis = creationDate.getTime();
	}
	
	public Date getLastUpdated() {
		return new Date( lastUpdatedMillis );
	}
	
	public void setLastUpdated( Date lastUpdated ) {
		this.lastUpdatedMillis = lastUpdated.getTime();
	}
	
	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl( String pageUrl ) {
		this.pageUrl = pageUrl;
	}

	public boolean hasAccessToUpdate() {
		return hasAccessToUpdate == null ? false : hasAccessToUpdate;
	}
	
	public void setAccessToUpdate( Boolean hasAccessToUpdate ) {
		this.hasAccessToUpdate = hasAccessToUpdate;
	}

}
