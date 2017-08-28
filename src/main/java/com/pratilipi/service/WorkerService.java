package com.pratilipi.service;

import com.pratilipi.api.ApiRegistry;
import com.pratilipi.api.GenericService;
import com.pratilipi.api.batchprocess.BatchProcessApi;
import com.pratilipi.api.impl.accesstoken.AccessTokenCleanupApi;
import com.pratilipi.api.impl.auditlog.AuditLogProcessApi;
import com.pratilipi.api.impl.author.AuthorBackupApi;
import com.pratilipi.api.impl.author.AuthorProcessApi;
import com.pratilipi.api.impl.contact.ConversationEmailApi;
import com.pratilipi.api.impl.email.EmailProcessApi;
import com.pratilipi.api.impl.notification.NotificationProcessApi;
import com.pratilipi.api.impl.pratilipi.PratilipiBackupApi;
import com.pratilipi.api.impl.pratilipi.PratilipiIdfApi;
import com.pratilipi.api.impl.pratilipi.PratilipiProcessApi;
import com.pratilipi.api.impl.pratilipi.PratilipiStatsApi;
import com.pratilipi.api.impl.user.UserBackupApi;
import com.pratilipi.api.impl.user.UserEmailApi;
import com.pratilipi.api.impl.user.UserFacebookValidationApi;
import com.pratilipi.api.impl.user.UserProcessApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiBackfillApi;

@SuppressWarnings("serial")
public class WorkerService extends GenericService {
	
	static {

		ApiRegistry.register( UserProcessApi.class );
		ApiRegistry.register( UserEmailApi.class );
		ApiRegistry.register( UserFacebookValidationApi.class );
		ApiRegistry.register( UserBackupApi.class );
		
		ApiRegistry.register( AccessTokenCleanupApi.class );
		ApiRegistry.register( AuditLogProcessApi.class );
		
		ApiRegistry.register( PratilipiProcessApi.class );
		ApiRegistry.register( PratilipiStatsApi.class );
		ApiRegistry.register( PratilipiBackupApi.class );
		ApiRegistry.register( PratilipiIdfApi.class );

		ApiRegistry.register( AuthorProcessApi.class );
		ApiRegistry.register( AuthorBackupApi.class );
		
		ApiRegistry.register( NotificationProcessApi.class );
		ApiRegistry.register( EmailProcessApi.class );

		ApiRegistry.register( BatchProcessApi.class );
		
		ApiRegistry.register( ConversationEmailApi.class );
		
		ApiRegistry.register( UserPratilipiBackfillApi.class );
		
	}
	
}
