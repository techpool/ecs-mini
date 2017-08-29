package com.pratilipi.data.type;

import com.google.gson.JsonObject;

public interface InitBannerDoc {

	String getTitle();
	
	String getBanner();
	
	String getBannerMini();
	
	String getActionUrl();
	
	String getApiName();
	
	JsonObject getApiRequest();
	
}