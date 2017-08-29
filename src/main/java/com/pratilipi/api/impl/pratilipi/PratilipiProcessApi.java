package com.pratilipi.api.impl.pratilipi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.impl.pratilipi.shared.PostPratilipiProcessRequest;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AuthorState;
import com.pratilipi.common.type.PageType;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.util.PratilipiFilter;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.AppProperty;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.Page;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.util.PratilipiDataUtil;
import com.pratilipi.data.util.PratilipiDocUtil;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;

@SuppressWarnings("serial")
@Bind( uri = "/pratilipi/process" )
public class PratilipiProcessApi extends GenericApi {

	private static final Logger logger =
			Logger.getLogger( PratilipiProcessApi.class.getName() );
	
	
	@Get
	public GenericResponse getPratilipiProcess( GenericRequest request )
			throws UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		
		// START: Creating ValidateData Tasks
		
		
		// Fetching AppProperty
		String appPropertyId = "Api.PratilipiProcess.ValidateData";
		AppProperty appProperty = dataAccessor.getAppProperty( appPropertyId );
		if( appProperty == null ) {
			appProperty = dataAccessor.newAppProperty( appPropertyId );
			appProperty.setValue( new Date( 0 ) );
		}

		// Fetching list of Pratilipi ids.
		PratilipiFilter pratilipiFilter = new PratilipiFilter();
		pratilipiFilter.setMinLastUpdate( (Date) appProperty.getValue(), false );
		List<Long> pratilipiIdList = dataAccessor.getPratilipiIdList( pratilipiFilter, null, 0, 10000 ).getDataList();

		// Creating one task per Pratilipi id.
		List<Task> taskList = new ArrayList<>( pratilipiIdList.size() );
		for( Long pratilipiId : pratilipiIdList ) {
			Task task = TaskQueueFactory.newTask()
					.setUrl( "/pratilipi/process" )
					.addParam( "pratilipiId", pratilipiId.toString() )
					.addParam( "validateData", "true" );
			taskList.add( task );
		}
		TaskQueueFactory.getPratilipiOfflineTaskQueue().addAll( taskList );
		logger.log( Level.INFO, "Added " + taskList.size() + " ValidateData tasks." );

		// Updating AppProperty.
		if( pratilipiIdList.size() > 0 ) {
			Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiIdList.get( pratilipiIdList.size() - 1 ) );
			Date lastUpdatedDate = pratilipi.getLastUpdated();
			logger.log( Level.INFO, "Processing all contents updated from " + (Date) appProperty.getValue()  + " to " + lastUpdatedDate + "." );
			logger.log( Level.INFO, "Last Updated PratilipiID : " + pratilipi.getId() );
			appProperty.setValue( lastUpdatedDate );
			appProperty = dataAccessor.createOrUpdateAppProperty( appProperty );
		}
		
		
		// END: Creating ValidateData Tasks

		
		// START: Creating UpdateStats Tasks
		
		
/*		appPropertyId = "Api.PratilipiProcess.UpdateStats";
		appProperty = dataAccessor.getAppProperty( appPropertyId );
		if( appProperty == null ) {
			appProperty = dataAccessor.newAppProperty( appPropertyId );
			appProperty.setValue( new Date( 1420072200000L ) ); // 01 Jan 2015, 06:00 AM IST
		}
		
		Date date = (Date) appProperty.getValue();
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone( TimeZone.getTimeZone( "IST" ) );
		cal.setTime( new Date( date.getTime() - TimeUnit.HOURS.toMillis( 6 ) ) ); // To cover delay in Google Analytics report generation
		
		int year = cal.get( Calendar.YEAR );
		int month = cal.get( Calendar.MONTH ) + 1;
		int day = cal.get( Calendar.DAY_OF_MONTH );
		
		
		List<Long> updatedPratilipiIdList = 
				PratilipiDocUtil.updatePratilipiGoogleAnalyticsPageViews( year, month, day );
		
		
		taskList = new ArrayList<>(updatedPratilipiIdList.size() );
		for( Long pratilipiId : updatedPratilipiIdList ) {
			Task task = TaskQueueFactory.newTask()
					.setUrl( "/pratilipi/process" )
					.addParam( "pratilipiId", pratilipiId.toString() )
					.addParam( "updateStats", "true" );
			taskList.add( task );
		}
		TaskQueueFactory.getPratilipiOfflineTaskQueue().addAll( taskList );
		logger.log( Level.INFO, "Added " + taskList.size() + " UpdateStats task(s)." );

		
		appProperty.setValue( date.getTime() + TimeUnit.DAYS.toMillis( 1 ) > new Date().getTime()
				? new Date()
				: new Date( date.getTime() + TimeUnit.DAYS.toMillis( 1 ) ) );
		appProperty = dataAccessor.createOrUpdateAppProperty( appProperty );*/
		
		
		// END: Creating UpdateStats Tasks
		
		
		return new GenericResponse();
		
	}
	
	@Post
	public GenericResponse postPratilipiProcess( PostPratilipiProcessRequest request )
			throws InvalidArgumentException, UnexpectedServerException, InsufficientAccessException {

		List<Long> pratilipiIdList = request.getPratilipiIdList() != null ? request.getPratilipiIdList() : new ArrayList<Long>( 1 );
		if( request.getPratilipiId() != null )
			pratilipiIdList.add( request.getPratilipiId() );
		
		if( pratilipiIdList.size() == 0 ) {
			logger.log( Level.SEVERE, "0 pratilipiId found." );
			throw new InvalidArgumentException( "'pratilipiId' or 'pratilipiIdList' must be set/non-empty." );
		}
		
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		
		if( request.validateData() ) {
			for( Long pratilipiId : pratilipiIdList ) {

				Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
				Author author = dataAccessor.getAuthor( pratilipi.getAuthorId() );
				Page pratilipiPage = dataAccessor.getPage( PageType.PRATILIPI, pratilipiId );
				
				if( pratilipi.getLanguage() == null )
					throw new InvalidArgumentException( "Language is null." );
				
				if( pratilipi.getAuthorId() != null && author == null )
					throw new InvalidArgumentException( "Invalid Author id." );
				
				if( pratilipi.getState() == PratilipiState.PUBLISHED && author != null && author.getState() == AuthorState.DELETED )
					throw new InvalidArgumentException( "Linked Author entity is DELETED." );
				
				if( pratilipi.getState() == PratilipiState.DELETED && pratilipiPage != null )
					throw new InvalidArgumentException( "DELETED Pratilipi has non-deleted Page entity." );
				
				if( pratilipi.getState() != PratilipiState.DELETED && pratilipiPage == null )
					throw new InvalidArgumentException( "Page entity is missing for the Pratilipi." );
				
				if( pratilipi.getState() == PratilipiState.DELETED )
					continue;
				
				if( pratilipi.getTitle() == null && pratilipi.getTitleEn() == null )
					throw new InvalidArgumentException( "Title is missing for the Pratilipi." );
					
				if( ( pratilipi.getTitle() != null && pratilipi.getTitle().trim().isEmpty() )
						|| ( pratilipi.getTitleEn() != null && pratilipi.getTitleEn().trim().isEmpty() ) )
					throw new InvalidArgumentException( "Title has empty string." );
				
			}
		}
		
		
		if( request.processData() ) {
			PratilipiDataUtil.updatePratilipiSearchIndex( pratilipiIdList );
			PratilipiDataUtil.updateFacebookScrape( pratilipiIdList );
		}

		
		if( request.processContent() ) {
			for( Long pratilipiId : pratilipiIdList ) {
				PratilipiDocUtil.updatePratilipiContent( pratilipiId );
				PratilipiDataUtil.updatePratilipiIndex( pratilipiId );
				boolean changed = PratilipiDocUtil.updateMeta( pratilipiId );
				if( changed )
					PratilipiDataUtil.updatePratilipiSearchIndex( pratilipiId, null );
			}
		}
		
		if( request.processContentDoc() ) {
			for( Long pratilipiId : pratilipiIdList ) {
				boolean changed = PratilipiDocUtil.updateMeta( pratilipiId );
				if( changed )
					PratilipiDataUtil.updatePratilipiSearchIndex( pratilipiId, null );
			}
		}
		
		if( request.updateReviewsDoc() ) {
			for( long pratilipiId : pratilipiIdList )
				PratilipiDocUtil.updatePratilipiReviews( pratilipiId );
		}
		
		
		if( request.updateStats() ) {
			List<Task> taskList = new ArrayList<>( pratilipiIdList.size() );
			List<Pratilipi> pratilipiList = DataAccessorFactory.getDataAccessor().getPratilipiList( pratilipiIdList );
			for( int i = 0; i < pratilipiIdList.size(); i++ ) {
				Pratilipi pratilipi = pratilipiList.get( i );
				if( pratilipi == null || pratilipi.getState() != PratilipiState.PUBLISHED ) {
					pratilipiList.remove( i );
					pratilipiIdList.remove( i );
					i--;
					continue;
				}
				PratilipiDataUtil.updatePratilipiStats( pratilipi.getId() );
				if( pratilipi.getAuthorId() != null ) { // Creating tasks to update author entities
					Task task = TaskQueueFactory.newTask()
							.setUrl( "/author/process" )
							.addParam( "authorId", pratilipi.getAuthorId().toString() )
							.addParam( "updateStats", "true" );
					taskList.add( task );
				}
			}
			PratilipiDataUtil.updatePratilipiSearchIndex( pratilipiIdList );
			TaskQueueFactory.getAuthorOfflineTaskQueue().addAll( taskList );
		}
		
		
		if( request.updateUserPratilipiStats() ) {
			for( long pratilipiId : pratilipiIdList )
				PratilipiDataUtil.updateUserPratilipiStats( pratilipiId );
		}
		
		
		return new GenericResponse();
		
	}
	
}
