package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pratilipi.common.type.BlogPostState;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.type.BlogPost;

@Cache
@Entity( name = "BLOG_POST" )
public class BlogPostEntity implements BlogPost {

	@Id
	private Long BLOG_POST_ID;
	
	@Index
	private Long BLOG_ID;

	private String TITLE;
	private String TITLE_EN;

	private String CONTENT;

	@Index
	private Language LANGUAGE;

	@Index
	private BlogPostState STATE;
	
	@Index
	private Long CREATED_BY; // USER.USER_ID
	
	@Index
	private Date CREATION_DATE;
	@Index
	private Date LAST_UPDATED;


	public BlogPostEntity() {}

	public BlogPostEntity( Long id ) {
		this.BLOG_POST_ID = id;
	}

	
	@Override
	public Long getId() {
		return BLOG_POST_ID;
	}
	
	public void setId( Long id ) {
		this.BLOG_POST_ID = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.BLOG_POST_ID = key.getId();
	}
	
	@Override
	public Long getBlogId() {
		return BLOG_ID;
	}
	
	@Override
	public void setBlogId( Long blogId ) {
		this.BLOG_ID = blogId;
	}
	
	
	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public void setTitle( String title ) {
		this.TITLE = title;
	}

	@Override
	public String getTitleEn() {
		return TITLE_EN;
	}

	@Override
	public void setTitleEn( String titleEn ) {
		this.TITLE_EN = titleEn;
	}

	
	@Override
	public String getContent() {
		return CONTENT;
	}

	@Override
	public void setContent( String content ) {
		this.CONTENT = content;
	}

	@Override
	public Language getLanguage() {
		return LANGUAGE;
	}

	@Override
	public void setLanguage( Language language ) {
		this.LANGUAGE = language;
	}

	@Override
	public BlogPostState getState() {
		return STATE;
	}
	
	@Override
	public void setState( BlogPostState state ) {
		this.STATE = state;
	}
	
	@Override
	public Long getCreatedBy() {
		return CREATED_BY;
	}
	
	@Override
	public void setCreatedBy( Long userId ) {
		this.CREATED_BY = userId;
	}
	
	@Override
	public Date getCreationDate() {
		return CREATION_DATE;
	}
	
	@Override
	public void setCreationDate( Date creationDate ) {
		this.CREATION_DATE = creationDate;
	}

	@Override
	public Date getLastUpdated() {
		return LAST_UPDATED == null ? CREATION_DATE : LAST_UPDATED;
	}

	@Override
	public void setLastUpdated( Date lastUpdated ) {
		this.LAST_UPDATED = lastUpdated;
	}

}
