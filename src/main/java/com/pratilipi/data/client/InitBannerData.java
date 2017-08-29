package com.pratilipi.data.client;

import com.google.gson.JsonObject;


public class InitBannerData {

	private String title;
	
	private String imageUrl;
	
	private String miniImageUrl;
	
	private String actionUrl;
	
	private String apiName;
	
	private JsonObject apiRequest;
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl( String imageUrl ) {
		this.imageUrl = imageUrl;
	}
	
	public String getMiniImageUrl() {
		return miniImageUrl;
	}

	public void setMiniImageUrl( String miniImageUrl ) {
		this.miniImageUrl = miniImageUrl;
	}
	
	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl( String actionUrl ) {
		this.actionUrl = actionUrl;
	}

	public String getApiName() {
		return apiName;
	}
	
	public void setApiName( String apiName ) {
		this.apiName = apiName;
	}
	
	public JsonObject getApiRequest() {
		return apiRequest;
	}
	
	public void setApiRequest( JsonObject apiRequest ) {
		this.apiRequest = apiRequest;
	}

}