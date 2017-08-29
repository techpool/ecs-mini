package com.pratilipi.taskqueue;

import java.util.HashMap;
import java.util.Map;

public class TaskQueueFactory {

	private static final Map<String, TaskQueue> taskQueueMap = new HashMap<>();
	
	private static final String QUEUE_USER				= "user";
	private static final String QUEUE_USER_OFFLINE		= "user-offline";
	private static final String QUEUE_PRATILIPI			= "pratilipi";
	private static final String QUEUE_PRATILIPI_OFFLINE	= "pratilipi-offline";
	private static final String QUEUE_AUTHOR			= "author";
	private static final String QUEUE_AUTHOR_OFFLINE	= "author-offline";
	private static final String QUEUE_USER_AUTHOR		= "user-author";
	private static final String QUEUE_NOTIFICATION		= "fe-notification";
	private static final String QUEUE_EMAIL_HP			= "hp-email";
	private static final String QUEUE_CONVERSATION_EMAIL		= "conversation-email";
	
	
	public static TaskQueue getUserTaskQueue() {
		return getTaskQueue( QUEUE_USER );
	}

	public static TaskQueue getUserOfflineTaskQueue() {
		return getTaskQueue( QUEUE_USER_OFFLINE );
	}

	public static TaskQueue getPratilipiTaskQueue() {
		return getTaskQueue( QUEUE_PRATILIPI );
	}

	public static TaskQueue getPratilipiOfflineTaskQueue() {
		return getTaskQueue( QUEUE_PRATILIPI_OFFLINE );
	}

	public static TaskQueue getAuthorTaskQueue() {
		return getTaskQueue( QUEUE_AUTHOR );
	}
	
	public static TaskQueue getAuthorOfflineTaskQueue() {
		return getTaskQueue( QUEUE_AUTHOR_OFFLINE );
	}

	public static TaskQueue getUserAuthorTaskQueue() {
		return getTaskQueue( QUEUE_USER_AUTHOR );
	}

	public static TaskQueue getNotificationTaskQueue() {
		return getTaskQueue( QUEUE_NOTIFICATION );
	}

	public static TaskQueue getEmailHpTaskQueue() {
		return getTaskQueue( QUEUE_EMAIL_HP );
	}
	
	public static TaskQueue getConversationTaskQueue() {
		return getTaskQueue(QUEUE_CONVERSATION_EMAIL);
	}
	
	protected static TaskQueue getTaskQueue( String taskQueueName ) {
		TaskQueue taskQueue = taskQueueMap.get( taskQueueName );
		if( taskQueue == null ) {
			taskQueue = new TaskQueueGaeImpl( taskQueueName );
			taskQueueMap.put( taskQueueName, taskQueue );
		}
		return taskQueue;
	}
	
	public static Task newTask() {
		return new TaskGaeImpl();
	}
	
}
