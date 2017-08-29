package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.data.type.Tag;

@Cache
@Entity( name = "TAG" )
public class TagEntity implements Tag {

	@Id
	private Long TAG_ID;


	private String NAME;

	private String NAME_EN;

	@Index
	private PratilipiType PRATILIPI_TYPE;

	@Index
	private Language LANGUAGE;

	private Date CREATION_DATE;

	public TagEntity() {}
	
	public TagEntity( Long tagId ) {
		this.TAG_ID = tagId;
	}


	@Override
	public Long getId() {
		return TAG_ID;
	}

	public void setId( Long id ) {
		this.TAG_ID = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.TAG_ID = key.getId();
	}


	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void setName( String name ) {
		this.NAME = name;
	}

	@Override
	public String getNameEn() {
		return NAME_EN;
	}

	@Override
	public void setNameEn( String nameEn ) {
		this.NAME_EN = nameEn;
	}

	@Override
	public PratilipiType getType() {
		return PRATILIPI_TYPE;
	}

	@Override
	public void setType( PratilipiType pratilipiType ) {
		this.PRATILIPI_TYPE = pratilipiType;
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
	public Date getCreationDate() {
		return this.CREATION_DATE;
	}
	
	@Override
	public void setCreationDate(Date creationDate) {
		this.CREATION_DATE = creationDate;
	}
}
