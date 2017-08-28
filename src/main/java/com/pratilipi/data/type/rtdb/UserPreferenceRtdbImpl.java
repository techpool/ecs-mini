package com.pratilipi.data.type.rtdb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pratilipi.common.type.EmailFrequency;
import com.pratilipi.common.type.NotificationType;
import com.pratilipi.data.type.UserPreferenceRtdb;

public class UserPreferenceRtdbImpl implements UserPreferenceRtdb {

	private EmailFrequency emailFrequency;

	private Map<NotificationType, Boolean> notificationSubscriptions;

	private Long lastUpdated;

	private Integer androidVersionCode;


	@Override
	public EmailFrequency getEmailFrequency() {
		return emailFrequency == null ? EmailFrequency.IMMEDIATELY : emailFrequency;
	}

	@Override
	public void setEmailFrequency( EmailFrequency emailFrequency ) {
		this.emailFrequency = emailFrequency;
	}

	@Override
	public boolean isNotificationSubscribed( NotificationType notificationType ) {
		return notificationSubscriptions == null || notificationSubscriptions.get( notificationType ) == null
				? notificationType.getSubscriptionDefault()
				: notificationSubscriptions.get( notificationType );
	}

	@Override
	public void setNotificationSubscription( NotificationType notificationType, boolean subscribed ) {
		if( this.notificationSubscriptions == null )
			this.notificationSubscriptions = new HashMap<NotificationType, Boolean>();
		this.notificationSubscriptions.put( notificationType, subscribed );
	}
	
	@Override
	public Date getLastUpdated() {
		return lastUpdated != null ? new Date( lastUpdated ) : null;
	}
	
	@Override
	public void setLastUpdated( Date lastUpdated ) {
		this.lastUpdated = lastUpdated.getTime();
	}

	@Override
	public Integer getAndroidVersionCode() {
		return androidVersionCode;
	}

	@Override
	public void setAndroidVersionCode( Integer androidVersionCode ) {
		this.androidVersionCode = androidVersionCode;
	}

}