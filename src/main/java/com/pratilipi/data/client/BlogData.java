package com.pratilipi.data.client;

import java.util.Date;

import com.pratilipi.common.type.Language;

public class BlogData {
	
	private Long blogId;
	
	
	private String title;
	private boolean hasTitle;
	
	private String titleEn;
	private boolean hasTitleEn;
	
	private Language language;
	private boolean hasLanguage;
	
	
	private Long creationDateMillis;
	private Long lasUpdatedMillis;
	
	
	private String pageUrl;
	
	private Boolean hasAccessToUpdate;

	
	
	public Long getId() {
		return blogId;
	}

	public void setId( Long id ) {
		this.blogId = id;
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

	
	public Date getCreationDate() {
		return creationDateMillis == null ? null : new Date( creationDateMillis );
	}
	
	public void setCrationDate( Date creationDate ) {
		this.creationDateMillis = creationDate == null ? null : creationDate.getTime();
	}
	
	public Date getLastUpdated() {
		return lasUpdatedMillis == null ? null : new Date( lasUpdatedMillis );
	}
	
	public void setLastUpdated( Date lastUpdated ) {
		this.lasUpdatedMillis = lastUpdated == null ? null : lastUpdated.getTime();
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
