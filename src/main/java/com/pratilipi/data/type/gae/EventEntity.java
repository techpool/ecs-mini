package com.pratilipi.data.type.gae;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.type.Event;

@Cache
@Entity( name = "EVENT" )
public class EventEntity implements Event {

	@Id
	private Long EVENT_ID;

	private String NAME;
	private String NAME_EN;

	@Index
	private Language LANGUAGE;
	@Deprecated
	private Long LANGUAGE_ID;
	private String DESCRIPTION;
	private List<Long> PRATILIPI_IDS;
	
	@Deprecated
	private Date START_DATE;
	@Deprecated
	private Date END_DATE;
	
	@Index
	private Date CREATION_DATE;
	@Index
	private Date LAST_UPDATED;


	public EventEntity() {}

	public EventEntity( Long id ) {
		this.EVENT_ID = id;
	}

	
	@Override
	public Long getId() {
		return EVENT_ID;
	}
	
	public void setId( Long id ) {
		this.EVENT_ID = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.EVENT_ID = key.getId();
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
	public Language getLanguage() {
		return LANGUAGE;
	}

	@Override
	public void setLanguage( Language language ) {
		this.LANGUAGE = language;
	}


	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public void setDescription( String description ) {
		this.DESCRIPTION = description;
	}

	@Override
	public List<Long> getPratilipiIdList() {
		return PRATILIPI_IDS == null ? new ArrayList<Long>( 0 ) : PRATILIPI_IDS;
	}
	
	@Override
	public void setPratilipiIdList( List<Long> pratilipiIdList ) {
		this.PRATILIPI_IDS = pratilipiIdList;
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
