package com.pratilipi.data.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.BatchProcessState;
import com.pratilipi.common.type.BatchProcessType;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.NotificationType;
import com.pratilipi.common.util.AuthorFilter;
import com.pratilipi.common.util.UserAccessUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListCursorTuple;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.client.BatchProcessData;
import com.pratilipi.data.type.AccessToken;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.BatchProcess;
import com.pratilipi.data.type.BatchProcessDoc;
import com.pratilipi.data.type.Notification;
import com.pratilipi.filter.AccessTokenFilter;


public class BatchProcessDataUtil {
	
	private static final Logger logger =
			Logger.getLogger( BatchProcessDataUtil.class.getName() );


	public static boolean hasAccessToCreateBatchProcess( Language language ) {
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		return UserAccessUtil.hasUserAccess( accessToken.getUserId(), language, AccessType.BATCH_PROCESS_ADD );
	}

	public static boolean hasAccessToListBatchProcess() {
		AccessToken accessToken = AccessTokenFilter.getAccessToken();
		return UserAccessUtil.hasUserAccess( accessToken.getUserId(), null, AccessType.BATCH_PROCESS_LIST );
	}

	
	public static BatchProcessData createBatchProcessData( BatchProcess batchProcess ) {
		
		BatchProcessData batchProcessData = new BatchProcessData();
		
		batchProcessData.setId( batchProcess.getId() );
		batchProcessData.setType( batchProcess.getType() );
		batchProcessData.setInitDoc( batchProcess.getInitDoc() );
		batchProcessData.setExecDoc( batchProcess.getExecDoc() );
		batchProcessData.setStateInProgress( batchProcess.getStateInProgress() );
		batchProcessData.setStateCompleted( batchProcess.getStateCompleted() );
		batchProcessData.setCreationDate( batchProcess.getCreationDate() );
		batchProcessData.setLastUpdated( batchProcess.getLastUpdated() );
		
		return batchProcessData;
		
	}
	
	public static List<BatchProcessData> createBatchProcessDataList( List<BatchProcess> batchProcessList ) {
		List<BatchProcessData> batchProcessDataList = new ArrayList<>();
		for( BatchProcess batchProcess : batchProcessList )
			batchProcessDataList.add( createBatchProcessData( batchProcess ) );
		return batchProcessDataList;
	}
	

	public static DataListCursorTuple<BatchProcessData> getBatchProcessList(
			BatchProcessType batchProcessType, String cursor, Integer resultCount )
			throws InsufficientAccessException {
		
		if( ! hasAccessToListBatchProcess() )
			throw new InsufficientAccessException();

		DataListCursorTuple<BatchProcess> batchProcessListCursorTuple = DataAccessorFactory.getDataAccessor()
				.getBatchProcessList( batchProcessType, null, null, cursor, resultCount );
		
		return new DataListCursorTuple<BatchProcessData>(
				createBatchProcessDataList( batchProcessListCursorTuple.getDataList() ),
				batchProcessListCursorTuple.getCursor() );

	}
	
	public static void createBatchProcess( BatchProcessType type, String initDoc, String execDoc, Language language ) 
			throws InsufficientAccessException, InvalidArgumentException {

		if( ! hasAccessToCreateBatchProcess( language ) )
			throw new InsufficientAccessException();

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		BatchProcess batchProcess = dataAccessor.newBatchProcess();
		batchProcess.setType( type );
		batchProcess.setInitDoc( initDoc );
		batchProcess.setExecDoc( execDoc );
		batchProcess.setCreationDate( new Date() );

		batchProcess = dataAccessor.createOrUpdateBatchProcess( batchProcess );

	}
	
	
	public static boolean exec( Long batchProcessId ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		BatchProcess batchProcess = dataAccessor.getBatchProcess( batchProcessId );
		if( batchProcess.getStartAt().after( new Date() ) )
			return false;
		if( batchProcess.getStateInProgress() != null && ! batchProcess.getStateInProgress().isTimedOut( batchProcess.getLastUpdated() ) )
			return false;
		
		BatchProcessState currState = batchProcess.getType().getNextState( batchProcess.getStateCompleted() );
		if( currState == null )
			throw new UnexpectedServerException();
		
		batchProcess.setStateInProgress( currState );
		batchProcess.setLastUpdated( new Date() );
		batchProcess = dataAccessor.createOrUpdateBatchProcess( batchProcess );

		System.out.println( "Process Id: " + batchProcess.getId() );
		System.out.println( "Process Type: " + batchProcess.getType() );
		System.out.println( "Process State: " + batchProcess.getStateInProgress() );
		
		if( currState == BatchProcessState.INIT )
			_execStateInit( batchProcess );
		else if( currState == BatchProcessState.GET_USER_IDS_BY_AUTHOR_FILTER )
			_execStateGetUserIdsByAuthorFilter( batchProcess );
		else if( currState == BatchProcessState.CREATE_NOTIFICATIONS_FOR_USER_IDS )
			_execStateCreateNotificationsForUserIds( batchProcess );
		else if( currState == BatchProcessState.VALIDATE_NOTIFICATION_COUNT )
			_execStateValidateNotificationCount( batchProcess );
		else if( currState == BatchProcessState.COMPLETED )
			batchProcess.setStateCompleted( currState );
		
		batchProcess.setStateInProgress( null );
		batchProcess.setLastUpdated( new Date() );
		batchProcess = dataAccessor.createOrUpdateBatchProcess( batchProcess ); // Saving Entity

		return true;
		
	}
	
	
	private static void _execStateInit( BatchProcess batchProcess )
			throws UnexpectedServerException {
		
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		BatchProcessDoc bpDoc = docAccessor.newBatchProcessDoc();
		JsonObject initDoc = new Gson().fromJson(
				batchProcess.getInitDoc(),
				JsonElement.class ).getAsJsonObject();
		
		for( Entry<String,JsonElement> entry : initDoc.entrySet() )
			bpDoc.setData( entry.getKey(), entry.getValue() );
		docAccessor.save( batchProcess.getId(), bpDoc ); // Saving Doc

		batchProcess.setStateCompleted( BatchProcessState.INIT );

	}
	
	private static void _execStateGetUserIdsByAuthorFilter( BatchProcess batchProcess )
			throws UnexpectedServerException {
	
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		BatchProcessDoc processDoc = docAccessor.getBatchProcessDoc( batchProcess.getId() );
		
		AuthorFilter authorFilter = processDoc.getData(
				BatchProcessState.GET_USER_IDS_BY_AUTHOR_FILTER.getInputName(),
				AuthorFilter.class );
		Set<Long> userIdSet = new HashSet<>();
		
		List<Author> tempAuthorList = null;
		String cursor = null;
		int resultCount = 200;
		do {
			DataListCursorTuple<Author> authorListCusorTuple = dataAccessor.getAuthorList( authorFilter, cursor, resultCount );
			tempAuthorList = authorListCusorTuple.getDataList();
			for( Author author : tempAuthorList )
				if( author.getUserId() != null )
					userIdSet.add( author.getUserId() );
			cursor = authorListCusorTuple.getCursor();
		} while( tempAuthorList.size() >= resultCount );
		
		processDoc.setData(
				BatchProcessState.GET_USER_IDS_BY_AUTHOR_FILTER.getOutputName(),
				userIdSet );
		docAccessor.save( batchProcess.getId(), processDoc ); // Saving Doc

		batchProcess.setStateCompleted( BatchProcessState.GET_USER_IDS_BY_AUTHOR_FILTER );
		
	}

	private static void _execStateCreateNotificationsForUserIds( BatchProcess batchProcess )
			throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		
		BatchProcessDoc processDoc = docAccessor.getBatchProcessDoc( batchProcess.getId() );
		
		Set<Long> userIdSet = processDoc.getData(
				BatchProcessState.CREATE_NOTIFICATIONS_FOR_USER_IDS.getInputName(),
				new TypeToken<Set<Long>>(){}.getType() );

		Map<Long,Long> userIdNotifIdMap = processDoc.getData(
				BatchProcessState.CREATE_NOTIFICATIONS_FOR_USER_IDS.getOutputName(),
				new TypeToken<Map<Long,Long>>(){}.getType() );
		
		
		JsonObject execDoc = new Gson().fromJson( batchProcess.getExecDoc(), JsonElement.class ).getAsJsonObject();
		NotificationType type = NotificationType.valueOf( execDoc.get( "type" ).getAsString() );
		String sourceId = execDoc.get( "sourceId" ).getAsString();
		String createdBy = "BATCH_PROCESS::" + batchProcess.getId();
		
		
		if( userIdNotifIdMap == null ) { // First attempt on this state
			
			userIdNotifIdMap = new HashMap<>();
			
		} else { // Recovering from previous (failed) attempt
			
			for( Entry<Long, Long> entry : userIdNotifIdMap.entrySet() ) {
				
				if( entry.getValue() != 0L ) {
					userIdSet.remove( entry.getKey() );
					continue;
				}
				
				Notification notification = dataAccessor.getNotification(
						entry.getKey(),
						type,
						sourceId,
						createdBy );
				
				if( notification != null ) {
					entry.setValue( notification.getId() );
					userIdSet.remove( entry.getKey() );
				}
				
			}
			
		}


		while( ! userIdSet.isEmpty() ) {
			
			List<Long> userIdList = new ArrayList<>( 100 );
			for( Long userId : userIdSet ) {
				userIdNotifIdMap.put( userId, 0L ); // Can't put null (instead of 0) here because Gson ignores keys with null values
				userIdList.add( userId );
				if( userIdList.size() == 100 ) // Limiting to 100 users per run
					break;
			}
			userIdSet.removeAll( userIdList );

		
			processDoc.setData(
					BatchProcessState.CREATE_NOTIFICATIONS_FOR_USER_IDS.getOutputName(),
					userIdNotifIdMap );
			docAccessor.save( batchProcess.getId(), processDoc ); // Saving Doc


			List<Notification> notifList = new ArrayList<>( userIdList.size() );
			for( Long userId : userIdList )
				notifList.add( dataAccessor.newNotification(
						userId,
						type,
						sourceId,
						createdBy ));
			
			notifList = dataAccessor.createOrUpdateNotificationList( notifList );
			for( Notification notif : notifList )
				userIdNotifIdMap.put( notif.getUserId(), notif.getId() );
			
		}

		
		processDoc.setData(
				BatchProcessState.CREATE_NOTIFICATIONS_FOR_USER_IDS.getOutputName(),
				userIdNotifIdMap );
		docAccessor.save( batchProcess.getId(), processDoc ); // Saving Doc
		
		batchProcess.setStateCompleted( BatchProcessState.CREATE_NOTIFICATIONS_FOR_USER_IDS );

	}
	
	private static void _execStateValidateNotificationCount( BatchProcess batchProcess )
			throws UnexpectedServerException {
	
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		
		BatchProcessDoc processDoc = docAccessor.getBatchProcessDoc( batchProcess.getId() );
		String createdBy = "BATCH_PROCESS::" + batchProcess.getId();
		
		Map<Long,Long> userIdNotifIdMap = processDoc.getData(
				BatchProcessState.VALIDATE_NOTIFICATION_COUNT.getInputName(),
				new TypeToken<Map<Long,Long>>(){}.getType() );
		
		int notifCount = dataAccessor.getNotificationCout( createdBy );
		
		
		if( userIdNotifIdMap.size() == notifCount )
			batchProcess.setStateCompleted( BatchProcessState.VALIDATE_NOTIFICATION_COUNT );
		else
			logger.log( Level.INFO, "Validation failed !" );
		
	}
	
}
