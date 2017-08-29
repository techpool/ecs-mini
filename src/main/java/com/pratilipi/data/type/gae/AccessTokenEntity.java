package com.pratilipi.data.type.gae;

import java.util.Date;
import java.util.UUID;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
import com.pratilipi.data.type.AccessToken;

@Cache
@Entity( name = "ACCESS_TOKEN" )
public class AccessTokenEntity implements AccessToken {

	@Id
	private String ACCESS_TOKEN_ID;
	
	@Index
	private Long USER_ID;
	
	@Index( IfNotNull.class )
	private String FCM_TOKEN;
	
	@Index( IfNotNull.class )
	private String DEVICE_LOCATION;
	
	@Index( IfNotNull.class )
	private String DEVICE_USER_AGENT;
	
	@Index
	private Date LOGIN_DATE;
	
	@Index
	private Date LOGOUT_DATE;

	@Index
	private Date EXPIRY;

	@Index
	private Date CREATION_DATE;

	
	public AccessTokenEntity() {
		this.ACCESS_TOKEN_ID = UUID.randomUUID().toString();
	}
	
	
	@Override
	public String getId() {
		return ACCESS_TOKEN_ID;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}

	public <T> void setKey( Key<T> key ) {
		this.ACCESS_TOKEN_ID = key.getName();
	}
	
	@Override
	public Long getUserId() {
		return USER_ID;
	}

	@Override
	public void setUserId( Long userId ) {
		this.USER_ID = userId;
	}

	@Override
	public String getFcmToken() {
		return FCM_TOKEN;
	}

	@Override
	public void setFcmToken( String fcmToken ) {
		this.FCM_TOKEN = fcmToken;
	}

	@Override
	public String getDeviceLocation() {
		return DEVICE_LOCATION;
	}

	@Override
	public void setDeviceLocation( String city, String region, String country ) {
		DEVICE_LOCATION = city + ", " + region + ", " + country;
	}
	
	@Override
	public String getDeviceUserAgent() {
		return DEVICE_USER_AGENT;
	}
	
	@Override
	public void setDeviceUserAgent( String userAgent ) {
		this.DEVICE_USER_AGENT = userAgent;
	}
	
	@Override
	public Date getLogInDate() {
		return LOGIN_DATE;
	}
	
	@Override
	public void setLogInDate( Date logInDate ) {
		this.LOGIN_DATE = logInDate;
	}
	
	@Override
	public Date getLogOutDate() {
		return LOGOUT_DATE;
	}
	
	@Override
	public void setLogOutDate( Date logOutDate ) {
		this.LOGOUT_DATE = logOutDate;
	}
	
	@Override
	public Date getExpiry() {
		return EXPIRY;
	}

	@Override
	public void setExpiry( Date expiry ) {
		this.EXPIRY = expiry;
	}

	@Override
	public boolean isExpired() {
		return EXPIRY.before( new Date() );
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
