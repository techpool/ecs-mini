package com.pratilipi.common.util;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.AppProperty;

public class FirebaseApi {

	private static Boolean hasBeenInitialized = false;

	private static final Logger logger =
			Logger.getLogger( FirebaseApi.class.getName() );

	
	private static final String CLOUD_MESSAGING_API_URL = "https://fcm.googleapis.com/fcm/send";
	private static final String DATABASE_URL = SystemProperty.FIREBASE_DB_URL;
	private static final String DATABASE_NOTIFICATION_TABLE = "NOTIFICATION";

	
	private synchronized static void initialiseFirebase() {

		if( hasBeenInitialized )
			return;

		logger.log( Level.INFO, "Firebase Apps: " + FirebaseApp.getApps() );
		logger.log( Level.INFO, "Initialising Firebase..." );

		String serviceAccountKey = DataAccessorFactory.getDataAccessor()
				.getAppProperty( AppProperty.SERVICE_ACCOUNT_FIREBASE )
				.getValue();

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setServiceAccount( IOUtils.toInputStream( serviceAccountKey, StandardCharsets.UTF_8 ) )
				.setDatabaseUrl( DATABASE_URL )
				.build();

		FirebaseApp.initializeApp( options );
		hasBeenInitialized = true;

		logger.log( Level.INFO, "Firebase has been initialised successfully!" );
	}

	
	public static String getCustomTokenForUser( Long userId ) {
		initialiseFirebase();
		return FirebaseAuth.getInstance().createCustomToken( userId.toString() );
	}
	
	private static String getFcmServerKey() {
		return DataAccessorFactory.getDataAccessor()
				.getAppProperty( AppProperty.FCM_SERVER_KEY )
				.getValue();
	}


	@Deprecated
	public static String sendCloudMessage( List<String> fcmTokenList, String message, String tag,
			String androidHandler, String sourceId, String sourceImageUrl, String displayImageUrl )
			throws UnexpectedServerException {

		Map<String, String> headersMap = new HashMap<>();
		headersMap.put( "Authorization", "key=" + getFcmServerKey() );

		JsonObject notificationJson = new JsonObject();
		notificationJson.addProperty( "body", message );
		notificationJson.addProperty( "tag", tag );
		notificationJson.addProperty( "sound", "disabled" );
		notificationJson.addProperty( "icon", "ic_pratilipi_notification_icon" );
		notificationJson.addProperty( "click_action", androidHandler );
		
		JsonObject dataJson = new JsonObject();
		dataJson.addProperty( "sourceId", sourceId );
		dataJson.addProperty( "sourceImageUrl", sourceImageUrl );
		dataJson.addProperty( "displayImageUrl", displayImageUrl );
		
		JsonObject bodyJson = new JsonObject();
		bodyJson.add( "registration_ids", new Gson().toJsonTree( fcmTokenList ) );
		bodyJson.add( "notification", notificationJson );
		bodyJson.add( "data", dataJson );

		String responsePayload = HttpUtil.doPost( CLOUD_MESSAGING_API_URL, headersMap, bodyJson );

		// Firebase might return with one or more error responses.
		for( JsonElement resultJson : new Gson().fromJson( responsePayload, JsonElement.class ).getAsJsonObject().get( "results" ).getAsJsonArray() )
			if( resultJson.getAsJsonObject().get( "error" ) != null )
				logger.log( Level.SEVERE, "Firebase responded with error: " + resultJson.getAsJsonObject().get( "error" ).getAsString() );

		
		return responsePayload;

	}

	public static String sendCloudMessage2( List<String> fcmTokenList, String message, String tag,
			String androidHandler, String sourceId, String sourceImageUrl, String displayImageUrl )
			throws UnexpectedServerException {

		Map<String, String> headersMap = new HashMap<>();
		headersMap.put( "Authorization", "key=" + getFcmServerKey() );

		JsonObject dataJson = new JsonObject();
		dataJson.addProperty( "body", message );
		dataJson.addProperty( "tag", tag );
		dataJson.addProperty( "sound", "disabled" );
		dataJson.addProperty( "click_action", androidHandler );
		dataJson.addProperty( "sourceId", sourceId );
		dataJson.addProperty( "sourceImageUrl", sourceImageUrl );
		dataJson.addProperty( "displayImageUrl", displayImageUrl );
		
		JsonObject bodyJson = new JsonObject();
		bodyJson.add( "registration_ids", new Gson().toJsonTree( fcmTokenList ) );
		bodyJson.add( "data", dataJson );

		String responsePayload = HttpUtil.doPost( CLOUD_MESSAGING_API_URL, headersMap, bodyJson );

		// Firebase might return with one or more error responses.
		for( JsonElement resultJson : new Gson().fromJson( responsePayload, JsonElement.class ).getAsJsonObject().get( "results" ).getAsJsonArray() )
			if( resultJson.getAsJsonObject().get( "error" ) != null )
				logger.log( Level.SEVERE, "Firebase responded with error: " + resultJson.getAsJsonObject().get( "error" ).getAsString() );

		
		return responsePayload;

	}

	
	public static class NotificationDB {
		
		private List<Long> notificationIdList;
		private Integer newNotificationCount;

		
		private NotificationDB() {}
		
		private NotificationDB( List<Long> notificationIdList ) {
			this.notificationIdList = notificationIdList;
			this.newNotificationCount = notificationIdList.size();
		}
		
		
		public List<Long> getNotificationIdList() {
			return notificationIdList;
		}
		
		public Integer getNewNotificationCount() {
			return newNotificationCount;
		}
		
	}
	
	public static void updateUserNotificationData(
			Long userId,
			final List<Long> notifIdListToAdd,
			final List<Long> notifIdListToRemove,
			final Async async ) {

		
		initialiseFirebase();


		DatabaseReference databaseReference = FirebaseDatabase.getInstance()
				.getReference()
				.child( DATABASE_NOTIFICATION_TABLE )
				.child( userId.toString() );


		databaseReference.runTransaction( new Transaction.Handler() {

			@Override
			public Transaction.Result doTransaction( MutableData mutableData ) {

				// Current list of notificationIds with Firebase
				List<Long> notifIdList = new LinkedList<>();
				if( mutableData.getValue() != null ) {
					NotificationDB notifDB = mutableData.getValue( NotificationDB.class );
					if( notifDB.getNewNotificationCount() > 0 )
						notifIdList = notifDB.getNotificationIdList();
				}

				// Add/Remove notificationIds
				notifIdList.removeAll( notifIdListToAdd ); // Remove ids first to avoid duplicates
				notifIdList.removeAll( notifIdListToRemove );
				notifIdList.addAll( notifIdListToAdd );
				
				// Updating Firebase
				mutableData.setValue( new NotificationDB( notifIdList ) );
				return Transaction.success( mutableData );

			}

			@Override
			public void onComplete( DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot ) {
				
				if( committed ) { // Transaction successful
					
					async.exec();
				
//				} else if( databaseError == null ) { // Transaction aborted
				
				} else { // Transaction failed
					
					logger.log( Level.SEVERE, "Transaction failed with error code : " + databaseError.getCode() );
					
				}
				
			}

		} );
		
	}

}
