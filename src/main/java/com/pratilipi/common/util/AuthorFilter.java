package com.pratilipi.common.util;

import java.util.Date;

import com.pratilipi.common.type.AuthorState;
import com.pratilipi.common.type.Language;

public class AuthorFilter {
	
	private Language language;

	private AuthorState state;
	
	private Date minLastUpdated;
	private Boolean minLastUpdatedInclusive;
	
	private Boolean orderByContentPublished;
	
	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage( Language language ) {
		this.language = language;
	}

	public AuthorState getState() {
		return state;
	}

	public void setState( AuthorState state ) {
		this.state = state;
	}

	public Date getMinLastUpdated() {
		return minLastUpdated;
	}

	public boolean isMinLastUpdatedInclusive() {
		return minLastUpdatedInclusive == null ? true : minLastUpdatedInclusive;
	}
	
	public void setMinLastUpdate( Date minLastUpdated, boolean inclusive ) {
		this.minLastUpdated = minLastUpdated;
		this.minLastUpdatedInclusive = inclusive;
	}
	
	public Boolean getOrderByContentPublished() {
		return orderByContentPublished;
	}

	public void setOrderByContentPublished( Boolean orderByContentPublished ) {
		this.orderByContentPublished = orderByContentPublished;
	}

}
