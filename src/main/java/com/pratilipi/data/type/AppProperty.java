package com.pratilipi.data.type;

public interface AppProperty extends GenericOfyType {
	
	String GOOGLE_WEB_CLIENT_ID = "Google.WebClient.Id";
	String GOOGLE_ANDROID_CLIENT_ID = "Google.AndroidClient.Id";
	
	String SERVICE_ACCOUNT_FIREBASE = "ServiceAccount.Firebase.Key";
	String FCM_SERVER_KEY = "Firebase.CloudMessaging.ServerKey";
	
	String FACEBOOK_CREDENTIALS = "Facebook.Credentials";

	
	String WORKER_ACCESS_TOKEN_ID = "Module.Worker.AccessToken";

	
	String API_PRATILIPI_BACKUP = "Api.PratilipiBackup";

	String TOP_AUTHORS_DATE = "TopAuthors.Date";

	String PERSONALIZED_HOME_FILENAME = "Personalized.Home.Filename";
	
	String getId();
	
	<T> T getValue();

	<T> void setValue( T value );
	
}