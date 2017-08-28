package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pratilipi.common.type.PageType;
import com.pratilipi.data.type.Page;

@SuppressWarnings("serial")
@Cache
@Entity( name = "PAGE" )
public class PageEntity implements Page {

	@Id
	private Long PAGE_ID;
	
	@Index
	private PageType PAGE_TYPE;

	@Index
	private String URI;

	@Index
	private String URI_ALIAS;

	@Index
	private Long PRIMARY_CONTENT_ID;
	
	@Index
	private Date CREATION_DATE;
	
	
	public PageEntity() {}
	
	public PageEntity( Long id ) {
		this.PAGE_ID = id;
	}
	
	
	@Override
	public Long getId() {
		return PAGE_ID;
	}
	
	public void setId( Long id ) {
		this.PAGE_ID = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.PAGE_ID = key.getId();
	}

	
	@Override
	public PageType getType() {
		return PAGE_TYPE;
	}

	@Override
	public void setType( PageType type ) {
		this.PAGE_TYPE = type;
	}

	@Override
	public String getUri() {
		return URI;
	}

	@Override
	public void setUri( String uri ) {
		this.URI = uri;
	}

	@Override
	public String getUriAlias() {
		return URI_ALIAS;
	}

	@Override
	public void setUriAlias( String uri ) {
		this.URI_ALIAS = uri;
	}

	@Override
	public Long getPrimaryContentId() {
		return PRIMARY_CONTENT_ID;
	}

	@Override
	public void setPrimaryContentId( Long pageContentId ) {
		this.PRIMARY_CONTENT_ID = pageContentId;
	}

	@Override
	public Date getCreationDate() {
		return CREATION_DATE;
	}

	@Override
	public void setCreationDate( Date creationDate ) {
		this.CREATION_DATE = creationDate;
	}

}
