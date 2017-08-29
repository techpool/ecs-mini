package com.pratilipi.data.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.I18nGroup;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.NotificationState;
import com.pratilipi.common.type.NotificationType;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.RequestParameter;
import com.pratilipi.common.util.Async;
import com.pratilipi.common.util.FirebaseApi;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.DataListIterator;
import com.pratilipi.data.client.NotificationData;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.client.UserData;
import com.pratilipi.data.type.BatchProcess;
import com.pratilipi.data.type.I18n;
import com.pratilipi.data.type.Notification;
import com.pratilipi.filter.AccessTokenFilter;

public class NotificationDataUtil {
	
	private static final Logger logger =
			Logger.getLogger( NotificationDataUtil.class.getName() );
	
	
	private static final Map<String, I18n> i18ns;
	
	static {
		List<I18n> i18nList = DataAccessorFactory.getDataAccessor().getI18nList( I18nGroup.NOTIFICATION );
		i18ns = new HashMap<>( i18nList.size() );
		for( I18n i18n : i18nList )
			i18ns.put( i18n.getId(), i18n );
	}
	
	
	public static boolean hasAccessToListData( Long userId ) {
		return AccessTokenFilter.getAccessToken().getUserId().equals( userId );
	}
	
	public static boolean hasAccessToUpdateData( Notification notification ) {
		return notification.getUserId().equals( AccessTokenFilter.getAccessToken().getUserId() );
	}
	

	private static String createNotificationMessage( PratilipiData pratilipiData, Language language, boolean plainText ) { // NotificationType == PRATILIPI_ADD
		String pratilipiTitle = pratilipiData.getTitle() == null
				? pratilipiData.getTitleEn()
				: pratilipiData.getTitle();
		String authorName = pratilipiData.getAuthor().getName() == null
				? pratilipiData.getAuthor().getNameEn()
				: pratilipiData.getAuthor().getName();
		return ( plainText ? "" : "<b>" ) + authorName + ( plainText ? " " : "</b> " )
				+ i18ns.get( "notification_has_published" ).getI18nString( language )
				+ ( plainText ? " " : " </b>" ) + pratilipiTitle + ( plainText ? "" : "</b> " );
	}
	
	private static String createNotificationMessage( List<Long> userIdList, Map<Long, UserData> users, Language notificationLanguage, boolean plainText ) { // NotificationType == AUTHOR_FOLLOW
		if( userIdList.size() == 0 ) {
			return null;
		} else if( userIdList.size() == 1 ) {
			return ( plainText ? "" : "<b>" ) + users.get( userIdList.get( 0 ) ).getDisplayName() + ( plainText ? " " : "</b> " )
					+ i18ns.get( "notification_has_followed" ).getI18nString( notificationLanguage );
		} else if( userIdList.size() == 2 ) {
			return ( plainText ? "" : "<b>" ) + users.get( userIdList.get( 1 ) ).getDisplayName() + ( plainText ? " " : "</b> " )
					+ i18ns.get( "notification_and" ).getI18nString( notificationLanguage )
					+ ( plainText ? " " : " <b>" ) + users.get( userIdList.get( 0 ) ).getDisplayName() + ( plainText ? " " : "</b> " )
					+ i18ns.get( "notification_have_followed" ).getI18nString( notificationLanguage );
		} else if( userIdList.size() == 3 ) {
			return ( plainText ? "" : "<b>" ) + users.get( userIdList.get( 2 ) ).getDisplayName() + ( plainText ? ", " : "</b>, " )
					+ ( plainText ? "" : "<b>" ) + users.get( userIdList.get( 1 ) ).getDisplayName() + ( plainText ? " " : "</b> " )
					+ i18ns.get( "notification_and" ).getI18nString( notificationLanguage )
					+ ( plainText ? " " : " <b>" ) + users.get( userIdList.get( 0 ) ).getDisplayName() + ( plainText ? " " : "</b> " )
					+ i18ns.get( "notification_have_followed" ).getI18nString( notificationLanguage );
		} else {
			return ( plainText ? "" : "<b>" ) + users.get( userIdList.get( userIdList.size() - 1 ) ).getDisplayName() + ( plainText ? ", " : "</b>, " )
					+ ( plainText ? "" : "<b>" ) + users.get( userIdList.get( userIdList.size() - 2 ) ).getDisplayName() + ( plainText ? " " : "</b> " )
					+ i18ns.get( "notification_and" ).getI18nString( notificationLanguage )
					+ " " + ( userIdList.size() - 2 ) + " "
					+ i18ns.get( "notification_others_have_followed" ).getI18nString( notificationLanguage );
		}
	}
	
	
	public static List<NotificationData> createNotificationDataList( List<Notification> notificationList, Language language, boolean plainText )
			throws UnexpectedServerException {
		
		// Pre-fetching required User and Pratilipi Entities
		List<Long> userIdList = new LinkedList<>();
		List<Long> pratilipiIdList = new LinkedList<>();
		for( Notification notification : notificationList ) {
			userIdList.add( notification.getUserId() );
			if( notification.getType() == NotificationType.PRATILIPI ) {
				pratilipiIdList.add( notification.getSourceIdLong() );
			} else if( notification.getType() == NotificationType.PRATILIPI_PUBLISHED_FOLLOWER ) {
				pratilipiIdList.add( notification.getSourceIdLong() );
			} else if( notification.getType() == NotificationType.AUTHOR_FOLLOW ) {
				if( notification.getDataIds().size() <= 3 )
					userIdList.addAll( notification.getDataIds() );
				else
					userIdList.addAll( notification.getDataIds().subList( notification.getDataIds().size() - 3, notification.getDataIds().size() ) );
			}
		}
		
		Map<Long, UserData> users = UserDataUtil.createUserDataList( userIdList, true );
		List<PratilipiData> pratilipiDataList = PratilipiDataUtil.createPratilipiDataList( pratilipiIdList, true );
		Map<Long, PratilipiData> pratilipis = new HashMap<>( pratilipiDataList.size() );
		for( PratilipiData pratilipiData : pratilipiDataList )
			pratilipis.put( pratilipiData.getId(), pratilipiData );

		
		// Creating Notification Data list
		List<NotificationData> notificationDataList = new ArrayList<>( notificationList.size() );
		for( Notification notification : notificationList ) {
			
			NotificationData notificationData = new NotificationData( notification.getId() );
			notificationData.setUserId( notification.getUserId() );
			
			Language notificationLanguage = language == null
					? users.get( notification.getUserId() ).getLanguage()
					: language;
			if( notificationLanguage == null )
				notificationLanguage = Language.ENGLISH;
			
			if( notification.getType() == NotificationType.PRATILIPI ) {
				
				String createdBy = notification.getCreatedBy();
				if( createdBy.startsWith( "BATCH_PROCESS::" ) ) {
					Long batchProcessId = Long.parseLong( createdBy.substring( 15 ) );
					BatchProcess batchProcess = DataAccessorFactory.getDataAccessor().getBatchProcess( batchProcessId );
					JsonObject execDoc = new Gson().fromJson( batchProcess.getExecDoc(), JsonElement.class ).getAsJsonObject();
					notificationData.setMessage( execDoc.get( "message" ).getAsString() );
				}
				
				PratilipiData pratilipiData = pratilipis.get( notification.getSourceIdLong() );
				notificationData.setSourceUrl( pratilipiData.getPageUrl() + "?" + RequestParameter.NOTIFICATION_ID.getName() + "=" + notification.getId() );
				notificationData.setSourceImageUrl( pratilipiData.getCoverImageUrl() );
				
			} else if( notification.getType() == NotificationType.PRATILIPI_PUBLISHED_FOLLOWER ) {

				PratilipiData pratilipiData = pratilipis.get( notification.getSourceIdLong() );
				if( pratilipiData.getState() ==  PratilipiState.PUBLISHED ) {
					notificationData.setMessage( createNotificationMessage( pratilipiData, notificationLanguage, plainText ) );
					notificationData.setSourceUrl( pratilipiData.getPageUrl() + "?" + RequestParameter.NOTIFICATION_ID.getName() + "=" + notification.getId() );
					notificationData.setSourceImageUrl( pratilipiData.getCoverImageUrl() );
					notificationData.setDisplayImageUrl( pratilipiData.getAuthor().getImageUrl() );
				}
			
			} else 	if( notification.getType() == NotificationType.AUTHOR_FOLLOW ) {
				
				notificationData.setMessage( createNotificationMessage( notification.getDataIds(), users, notificationLanguage, plainText ) );
				notificationData.setSourceUrl( "/followers?" + RequestParameter.NOTIFICATION_ID.getName() + "=" + notification.getId() );
				if( notification.getDataIds().size() != 0 )
					notificationData.setDisplayImageUrl( users.get( notification.getDataIds().get( notification.getDataIds().size() - 1 ) ).getProfileImageUrl() );
			
			}
			
			notificationData.setSourceId( notification.getSourceId() );
			notificationData.setState( notification.getState() );
			notificationData.setNotificationType( notification.getType() );
			notificationData.setLastUpdatedDate( notification.getLastUpdated() );
			notificationDataList.add( notificationData );
			
		}
		
		
		return notificationDataList;
		
	}

	public static DataListCursorTuple<NotificationData> getNotificationList( Long userId, Language language, String cursor, Integer resultCount )
			throws InsufficientAccessException, UnexpectedServerException {
		
		if( ! hasAccessToListData( userId ) )
			throw new InsufficientAccessException();
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		
		// Fetching Notification Entities
		DataListIterator<Notification> notifListIterator =
				dataAccessor.getNotificationListIterator( userId, null, (String) null, cursor, null );
		
		List<Notification> notifList = resultCount == null
				? new ArrayList<Notification>()
				: new ArrayList<Notification>( resultCount );
				
		while( notifListIterator.hasNext() ) {
			Notification notif = notifListIterator.next();
			if( notif.getType().isValid( notif ) )
				notifList.add( notif );
			if( resultCount != null && notifList.size() == resultCount )
				break;
		}
		
		
		// Return
		return new DataListCursorTuple<>(
				createNotificationDataList( notifList, language, false ),
				notifListIterator.getCursor() );
		
	}
	
	
	public static void saveNotificationState( Long notificationId, NotificationState state )
			throws InsufficientAccessException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Notification notification = dataAccessor.getNotification( notificationId );
		if( notification.getState() == state )
			return;
		
		if( ! hasAccessToUpdateData( notification ) )
			throw new InsufficientAccessException();

		notification.setState( state );
		notification = dataAccessor.createOrUpdateNotification( notification );
		
	}

	public static void saveNotificationState(Map<Long, NotificationState> notificationStates )
			throws InsufficientAccessException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Map<Long, Notification> notifications = dataAccessor.getNotifications( notificationStates.keySet() );
		for( Map.Entry<Long, NotificationState> entry : notificationStates.entrySet() ) {
			Notification notification = notifications.get( entry.getKey() );
			if( notification.getState() == entry.getValue() )
				continue;
			if( ! hasAccessToUpdateData( notification ) )
				throw new InsufficientAccessException();
			notification.setState( entry.getValue() );
		}

		dataAccessor.createOrUpdateNotificationList( new ArrayList<Notification>( notifications.values() ) );

	}

	public static void updateFirebaseDb( final Long userId, List<Notification> notifList, Async async ) throws UnexpectedServerException {
		
		List<NotificationData> notifDataList = createNotificationDataList( notifList, null, true );
		
		final List<Long> notifIdListToAdd = new LinkedList<>();
		final List<Long> notifIdListToRemove = new LinkedList<>();
		for( NotificationData notifData : notifDataList ) {
			if( notifData.getMessage() != null && notifData.getState() == NotificationState.UNREAD )
				notifIdListToAdd.add( notifData.getId() );
			else
				notifIdListToRemove.add( notifData.getId() );
		}
		
		FirebaseApi.updateUserNotificationData(
				userId,
				notifIdListToAdd,
				notifIdListToRemove,
				async );
		
	}

	public static void sendFcm( Long notifId ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Notification notif = dataAccessor.getNotification( notifId );
		
		if( ! notif.isFcmPending() )
			return;
		
		if( notif.getState() == NotificationState.UNREAD ) {
			
			List<Notification> notifList = new ArrayList<>( 1 );
			notifList.add( notif );
			
			NotificationData notifData = createNotificationDataList( notifList, null, true ).get( 0 ); // TODO: Implement createNotificationData
			if( notifData.getMessage() != null ) { // TODO: Cancel previously sent FCM if notifData.getMessage() == null
				
				List<String> fcmTokenList = dataAccessor.getFcmTokenList( notifData.getUserId() );
				if( fcmTokenList.size() != 0 ) {
					
					String fcmResponse = FirebaseApi.sendCloudMessage2(
							fcmTokenList,
							notifData.getMessage(),
							( ((long)notifData.getId() ) % Integer.MAX_VALUE ) + "", // TODO: Remove Hack
							notifData.getNotificationType().getAndroidHandler(),
							notifData.getSourceId(),
							notifData.getSourceImageUrl(),
							notifData.getDisplayImageUrl() );
					
					if( notif.getFcmResponse() == null )
						notif.setFcmResponse( fcmResponse );
					else
						notif.setFcmResponse( notif.getFcmResponse() + "\n" + fcmResponse );
					
				}
				
			}
			
		}
		
		notif.setFcmPending( false );
		notif = dataAccessor.createOrUpdateNotification( notif );
		
	}
	
	
}