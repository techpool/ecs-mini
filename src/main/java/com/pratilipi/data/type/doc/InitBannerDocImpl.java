package com.pratilipi.data.type.doc;

import com.google.gson.JsonObject;
import com.pratilipi.data.type.InitBannerDoc;

public class InitBannerDocImpl implements InitBannerDoc {

	private String title;
	
	private String banner;
	
	private String bannerMini;
	
	private String actionUrl;
	
	private String apiName;
	
	private JsonObject apiRequest;

	
	public InitBannerDocImpl() {}
	
	public InitBannerDocImpl( String title, String banner, String bannerMini,
			String actionUrl, String apiName, JsonObject apiRequest ) {
		this.title = title;
		this.banner = banner;
		this.bannerMini = bannerMini;
		this.actionUrl = actionUrl;
		this.apiName = apiName;
		this.apiRequest = apiRequest;
	}

	
	public String getTitle() {
		return title;
	}
	
	public String getBanner() {
		return banner;
	}
	
	public String getBannerMini() {
		return bannerMini;
	}
	
	public String getActionUrl() {
		return actionUrl;
	}

	@Override
	public String getApiName() {
		return apiName;
	}

	@Override
	public JsonObject getApiRequest() {
		return apiRequest;
	}
	
}
