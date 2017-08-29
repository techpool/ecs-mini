package com.pratilipi.api.impl.email;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.EmailFrequency;
import com.pratilipi.common.type.EmailState;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DataListIterator;
import com.pratilipi.data.RtdbAccessor;
import com.pratilipi.data.type.AppProperty;
import com.pratilipi.data.type.Email;
import com.pratilipi.data.type.User;
import com.pratilipi.data.type.UserPreferenceRtdb;
import com.pratilipi.data.util.EmailDataUtil;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;

@SuppressWarnings("serial")
@Bind( uri = "/email/process" )
public class EmailProcessApi extends GenericApi {

	private static final Integer MAX_ANDROID_VERSION = 24;
	private static final Integer PENDING_EMAIL_THRESHOLD = 10000;

	private static final Logger logger = 
			Logger.getLogger( EmailProcessApi.class.getName() );


	public static class PostRequest extends GenericRequest {

		@Validate( minLong = 1L )
		private Long emailId;

		@Validate( minLong = 1L )
		private Long userId;

	}


	@Post
	public GenericResponse post( PostRequest request ) throws UnexpectedServerException {

		if( request.emailId != null )
			EmailDataUtil.sendEmail( request.emailId );

		if( request.userId != null )
			EmailDataUtil.sendEmailsToUser( request.userId );

		return new GenericResponse();

	}

	@Get
	public GenericResponse get( GenericRequest request )
			throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		RtdbAccessor rtdbAccessor = DataAccessorFactory.getRtdbAccessor();

		Map<Long, UserPreferenceRtdb> userPreferences = null;
		List<Email> emailList = new ArrayList<>();

		DataListIterator<Email> it = dataAccessor.getEmailListIteratorForStatePending( null, false );
		Set<Long> userIds = new HashSet<>();

		while( it.hasNext() ) {
			Email email = it.next();
			emailList.add( email );
			userIds.add( email.getUserId() );
		}

		userPreferences = rtdbAccessor.getUserPreferences( userIds );
		Map<Long, User> users = dataAccessor.getUsers( userPreferences.keySet() );

		// Re-scheduling for all Emails
		DateFormat dateFormat = new SimpleDateFormat( "dd-MMM-yyyy HH:mm:ss z" );
		dateFormat.setTimeZone( TimeZone.getTimeZone( "IST" ) );

		for( Email email : emailList ) {

			UserPreferenceRtdb preference = userPreferences.get( email.getUserId() );
			User user = users.get( email.getUserId() );

			if( email.getScheduledDate().before( new Date() ) )
				continue;

			if( email.getScheduledDate().equals( preference.getEmailFrequency().getNextSchedule( user.getLastEmailedDate() ) ) )
				continue;

			email.setScheduledDate( preference.getEmailFrequency().getNextSchedule( user.getLastEmailedDate() ) );
			email.setLastUpdated( new Date() );
			if( email.getScheduledDate() == null ) { // User changed setting to NEVER -> Scheduled Date will be null
				email.setState( EmailState.DROPPED );
			} else {
				logger.log( Level.INFO, "Rescheduling email: " + email.getId() +
						" from " + dateFormat.format( email.getScheduledDate() ) +
						" to " + dateFormat.format( preference.getEmailFrequency().getNextSchedule( user.getLastEmailedDate() ) ) );
			}

		}


		// Creating Tasks
		List<Task> taskList = new ArrayList<>();

		Set<Long> userIdTaskSet = new HashSet<>();
		for( Email email : emailList ) {
			if( email.getState() != EmailState.PENDING ) // User changed setting to NEVER -> Email Dropped
				continue;
			if( email.getScheduledDate().after( new Date() ) )
				continue;
			UserPreferenceRtdb preference = userPreferences.get( email.getUserId() );
			if( preference.getEmailFrequency() == EmailFrequency.IMMEDIATELY )
				taskList.add( TaskQueueFactory.newTask()
						.setUrl( "/email/process" )
						.addParam( "emailId", email.getId().toString() ) );
			else
				userIdTaskSet.add( email.getUserId() );

			email.setState( EmailState.IN_PROGRESS );
			email.setLastUpdated( new Date() );

		}

		for( Long userId : userIdTaskSet )
			taskList.add( TaskQueueFactory.newTask()
					.setUrl( "/email/process" )
					.addParam( "userId", userId.toString() ) );

		TaskQueueFactory.getEmailHpTaskQueue().addAll( taskList );

		emailList = dataAccessor.createOrUpdateEmailList( emailList );

		return new GenericResponse();

	}

}
