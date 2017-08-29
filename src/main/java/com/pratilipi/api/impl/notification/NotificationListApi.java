package com.pratilipi.api.impl.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.impl.blogpost.BlogPostListApi;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.client.NotificationData;
import com.pratilipi.data.util.NotificationDataUtil;
import com.pratilipi.filter.AccessTokenFilter;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
@Bind( uri = "/notification/list" )
public class NotificationListApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		private Language language;
		private String cursor;
		private Integer resultCount;

		public void setCursor( String cursor ) {
			this.cursor = cursor;
		}

		public void setResultCount( Integer resultCount ) {
			this.resultCount = resultCount;
		}

	}
	
	public static class Response extends GenericResponse { 

		private List<NotificationApi.Response> notificationList;
		private String cursor;


		@SuppressWarnings("unused")
		private Response() {}

		public Response( List<NotificationData> notificationDataList, String cursor ) {
			this.notificationList = new ArrayList<>( notificationDataList.size() );
			for( NotificationData notificationData : notificationDataList )
				this.notificationList.add(  new NotificationApi.Response( notificationData, NotificationListApi.class ) );
			this.cursor = cursor;
		}

		public List<NotificationApi.Response> getNotificationList() {
			return notificationList;
		}

		public String getCursor() {
			return cursor;
		}

	}
	
	
	@Get
	public Response get( GetRequest request ) throws InsufficientAccessException, UnexpectedServerException {

		Map<String, String> paramsMap = new HashMap<>();

		if( request.language != null )
			paramsMap.put( "language", request.language.toString() );

		if( request.cursor != null )
			paramsMap.put( "cursor", request.cursor );
		if( request.resultCount != null )
			paramsMap.put( "resultCount", request.resultCount + "" );

		String responseString = HttpUtil.doGet( UxModeFilter.getEcsEndpoint() + "/api/notification/list", paramsMap );
		return new Gson().fromJson( responseString, Response.class );


	}

}