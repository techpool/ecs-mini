package com.pratilipi.data.client;

import java.util.Date;

import com.pratilipi.common.type.NotificationState;
import com.pratilipi.common.type.NotificationType;

public class NotificationData {

	private Long notificationId;

	private Long userId;

	private NotificationType type;
	
	private String message;
	
	private String sourceId;
	
	private String sourceUrl;
	
	private String sourceImageUrl;

	private String displayImageUrl;

	private NotificationState state;

	private Date lastUpdatedDate;


	public NotificationData() {}
	
	public NotificationData( Long id ) {
		this.notificationId = id;
	}
	

	public Long getId() {
		return notificationId;
	}

	public void setId( Long id ) {
		this.notificationId = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId( Long userId ) {
		this.userId = userId;
	}

	public NotificationType getNotificationType() {
		return type;
	}

	public void setNotificationType( NotificationType type ) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage( String message ) {
		this.message = message;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId( String sourceId ) {
		this.sourceId = sourceId;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl( String sourceUrl ) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceImageUrl() {
		return sourceImageUrl;
	}

	public void setSourceImageUrl( String sourceImageUrl ) {
		this.sourceImageUrl = sourceImageUrl;
	}

	public String getDisplayImageUrl() {
		return displayImageUrl;
	}

	public void setDisplayImageUrl( String displayImageUrl ) {
		this.displayImageUrl = displayImageUrl;
	}

	public NotificationState getState() {
		return state;
	}
	
	public void setState( NotificationState state ) {
		this.state = state;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate( Date lastUpdatedDate ) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

}
