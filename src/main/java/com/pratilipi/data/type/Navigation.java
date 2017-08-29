package com.pratilipi.data.type;

import java.io.Serializable;
import java.util.List;

import com.google.gson.JsonObject;
import com.pratilipi.common.util.SystemProperty;

public interface Navigation extends Serializable {

	@SuppressWarnings("serial")
	class Link implements Serializable {
		
		private String name;
		private String url;
		private String categoryName;
		private String apiName;
		private Object apiRequest;
		private String imageUrl;
		
		public Link( String name, String url, String apiName, String categoryName, String apiRequest, String imageName ) {
			this.name = name;
			this.url = url;
			this.categoryName = categoryName;
			this.apiName = apiName;
			this.apiRequest = apiRequest;
			// TODO: Move images to static.pratilipi.com asap
			this.imageUrl = ( SystemProperty.CDN != null ? SystemProperty.CDN.replace( '*', '0' ) : "" ) + "/resource-all/android-category-banners/" + imageName;
		}
		
		public String getName() {
			return name;
		}
		
		public String getUrl() {
			return url;
		}

		public String getcategoryName() {
			return categoryName;
		}

		public String getApiName() {
			return apiName;
		}
		
		public void setApiName( String apiName ) {
			this.apiName = apiName;
		}
		
		public Object getApiRequest() {
			return apiRequest;
		}
		
		public void setApiRequest( JsonObject apiRequest ) {
			this.apiRequest = apiRequest;
		}
		
		public String getImageUrl() {
			return imageUrl;
		}
		
	}
	
	
	String getTitle();
	
	void setTitle( String title );
	
	List<Link> getLinkList();
	
	void addLink( Link link );
	
}