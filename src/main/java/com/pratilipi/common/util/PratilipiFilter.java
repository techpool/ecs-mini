package com.pratilipi.common.util;

import java.io.Serializable;
import java.util.Date;

import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.PratilipiType;

@SuppressWarnings("serial")
public class PratilipiFilter implements Serializable {
	
	private Long authorId;
	private Language language;
	private PratilipiType type;
	private String listName;
	private PratilipiState state;
	
	private Date minLastUpdated;
	private Boolean minLastUpdatedInclusive;
	
	private Boolean orderByReadCount;
	private Boolean orderByListingDate;
	private Boolean orderByLastUpdated;
	

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId( Long authorId ) {
		this.authorId = authorId;
	}
	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage( Language language ) {
		this.language = language;
	}

	public PratilipiType getType() {
		return type;
	}

	public void setType( PratilipiType pratilipiType ) {
		this.type = pratilipiType;
	}

	public String getListName() {
		return listName;
	}
	
	public void setListName( String listName ) {
		this.listName = listName;
	}
	
	public PratilipiState getState() {
		return state;
	}

	public void setState( PratilipiState state ) {
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
	
	public Boolean getOrderByReadCount() {
		return orderByReadCount;
	}

	public void setOrderByReadCount( Boolean orderByReadCount ) {
		this.orderByReadCount = orderByReadCount;
	}

	public Boolean getOrderByListingDate() {
		return orderByListingDate;
	}

	public void setOrderByListingDate( Boolean orderByListingDate ) {
		this.orderByListingDate = orderByListingDate;
	}

	public Boolean getOrderByLastUpdate() {
		return orderByLastUpdated;
	}

	public void setOrderByLastUpdate( Boolean orderByLastUpdated ) {
		this.orderByLastUpdated = orderByLastUpdated;
	}

	
	public String toUrlEncodedString() {
		
		String urlEncodedString = "";
		
		if( authorId != null )
			urlEncodedString += "&authorId=" + authorId;
		if( language != null )
			urlEncodedString += "&language=" + language;
		if( type != null )
			urlEncodedString += "&type=" + type;
		if( listName != null )
			urlEncodedString += "&listName=" + listName;
		if( state != null )
			urlEncodedString += "&state=" + state;
		if( orderByReadCount != null )
			urlEncodedString += "&orderByReadCount=" + orderByReadCount;
		if( orderByLastUpdated != null )
			urlEncodedString += "&orderByLastUpdated=" + orderByLastUpdated;
		
		return urlEncodedString.length() == 0 ? "" : urlEncodedString.substring( 1 );
		
	}
	
}
