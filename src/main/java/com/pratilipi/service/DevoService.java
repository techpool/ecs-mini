package com.pratilipi.service;

import com.pratilipi.api.ApiRegistry;
import com.pratilipi.api.GenericBatchApi;
import com.pratilipi.api.GenericService;
import com.pratilipi.api.batchprocess.BatchProcessApi;
import com.pratilipi.api.batchprocess.BatchProcessListApi;
import com.pratilipi.api.impl.accesstoken.AccessTokenCleanupApi;
import com.pratilipi.api.impl.appproperty.AppPropertyApi;
import com.pratilipi.api.impl.auditlog.AuditLogProcessApi;
import com.pratilipi.api.impl.author.*;
import com.pratilipi.api.impl.blogpost.BlogPostApi;
import com.pratilipi.api.impl.blogpost.BlogPostListApi;
import com.pratilipi.api.impl.comment.CommentApi;
import com.pratilipi.api.impl.comment.CommentListApi;
import com.pratilipi.api.impl.contact.ContactApi;
import com.pratilipi.api.impl.contact.ConversationEmailApi;
import com.pratilipi.api.impl.email.EmailProcessApi;
import com.pratilipi.api.impl.event.EventApi;
import com.pratilipi.api.impl.event.EventBannerApi;
import com.pratilipi.api.impl.event.EventListApi;
import com.pratilipi.api.impl.i18n.I18nApi;
import com.pratilipi.api.impl.init.InitBannerApi;
import com.pratilipi.api.impl.init.InitBannerListApi;
import com.pratilipi.api.impl.init.InitV2Api;
import com.pratilipi.api.impl.mailinglist.MailingListSubscribeApi;
import com.pratilipi.api.impl.navigation.NavigationListApi;
import com.pratilipi.api.impl.notification.NotificationApi;
import com.pratilipi.api.impl.notification.NotificationBatchApi;
import com.pratilipi.api.impl.notification.NotificationListApi;
import com.pratilipi.api.impl.notification.NotificationProcessApi;
import com.pratilipi.api.impl.page.PageApi;
import com.pratilipi.api.impl.page.PageContentApi;
import com.pratilipi.api.impl.pratilipi.*;
import com.pratilipi.api.impl.test.TestApi;
import com.pratilipi.api.impl.user.*;
import com.pratilipi.api.impl.userauthor.UserAuthorFollowListApi;
import com.pratilipi.api.impl.userauthor.UserAuthorFollowV1Api;
import com.pratilipi.api.impl.userauthor.UserAuthorFollowV2Api;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiBackfillApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiLibraryApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiLibraryListApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiReviewApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiReviewListApi;
import com.pratilipi.api.impl.vote.VoteApi;

@SuppressWarnings("serial")
public class DevoService extends GenericService {

	static {

		ApiRegistry.register( InitV2Api.class );						// default
		ApiRegistry.register( GenericBatchApi.class );				// default

		ApiRegistry.register( InitBannerApi.class );				// default & AWS CloudFront
		ApiRegistry.register( InitBannerListApi.class );			// default

		ApiRegistry.register( UserAccessTokenApi.class );
		ApiRegistry.register( UserLoginApi.class );					// default
		ApiRegistry.register( UserLoginFacebookApi.class );			// default
		ApiRegistry.register( UserLoginGoogleApi.class );			// default
		ApiRegistry.register( UserEmailApi.class );					// default
		ApiRegistry.register( UserLogoutApi.class );				// default
		ApiRegistry.register( UserRegisterApi.class );				// default
		ApiRegistry.register( UserVerificationApi.class );			// default
		ApiRegistry.register( UserPasswordUpdateApi.class );		// default

		ApiRegistry.register( PageApi.class );							// default
		ApiRegistry.register( PageContentApi.class );							// default

		ApiRegistry.register( PratilipiV1Api.class );					// default
		ApiRegistry.register( PratilipiV2Api.class );					// default
		ApiRegistry.register( PratilipiListV1Api.class );				// default
		ApiRegistry.register( PratilipiListV2Api.class );				// default
		ApiRegistry.register( PratilipiCoverApi.class );				// default & AWS CloudFront
		ApiRegistry.register( PratilipiContentV1Api.class );			// default
		ApiRegistry.register( PratilipiContentV2Api.class );			// default
		ApiRegistry.register( PratilipiContentV3Api.class );			// default
		ApiRegistry.register( PratilipiContentBatchApi.class );			// default
		ApiRegistry.register( PratilipiContentIndexApi.class );			// default
		ApiRegistry.register( PratilipiContentImageApi.class );			// default
		ApiRegistry.register( PratilipiContentChapterAddApi.class );	// default
		ApiRegistry.register( PratilipiContentChapterDeleteApi.class );	// default
		ApiRegistry.register( PratilipiStatsApi.class );				// default
		ApiRegistry.register( PratilipiTagsUpdateApi.class );			// default

		ApiRegistry.register( AuthorApi.class );					// default
		ApiRegistry.register( AuthorListApi.class );				// default
		ApiRegistry.register( AuthorImageApi.class );				// default & AWS CloudFront
		ApiRegistry.register( AuthorImageRemoveApi.class );			// default
		ApiRegistry.register( AuthorCoverApi.class );				// default & AWS CloudFront
		ApiRegistry.register( AuthorCoverRemoveApi.class );			// default
		ApiRegistry.register( AuthorRecommendApi.class );			// default
		ApiRegistry.register( AuthorListByReadCountApi.class );		// default

		ApiRegistry.register( EventApi.class );						// default
		ApiRegistry.register( EventListApi.class );					// default
		ApiRegistry.register( EventBannerApi.class );				// default

		ApiRegistry.register( BlogPostApi.class );					// default
		ApiRegistry.register( BlogPostListApi.class );				// default

		ApiRegistry.register( UserV1Api.class );					// default
		ApiRegistry.register( UserV2Api.class );					// default

		ApiRegistry.register( UserPratilipiApi.class );				// default
		ApiRegistry.register( UserPratilipiLibraryApi.class );		// default
		ApiRegistry.register( UserPratilipiLibraryListApi.class );	// default
		ApiRegistry.register( UserPratilipiBackfillApi.class );		// default

		ApiRegistry.register( UserPratilipiReviewApi.class );		// default
		ApiRegistry.register( UserPratilipiReviewListApi.class );	// default

		ApiRegistry.register( UserAuthorFollowV1Api.class );		// default
		ApiRegistry.register( UserAuthorFollowV2Api.class );		// default
		ApiRegistry.register( UserAuthorFollowListApi.class );		// default

		ApiRegistry.register( CommentApi.class );					// default
		ApiRegistry.register( CommentListApi.class );				// default

		ApiRegistry.register( VoteApi.class );						// default

		ApiRegistry.register( ContactApi.class );					// default
		ApiRegistry.register( ConversationEmailApi.class );			// default

		ApiRegistry.register( MailingListSubscribeApi.class );		// default

		ApiRegistry.register( NotificationApi.class );
		ApiRegistry.register( NotificationBatchApi.class );
		ApiRegistry.register( NotificationListApi.class );			// default

		ApiRegistry.register( BatchProcessApi.class );				// default
		ApiRegistry.register( BatchProcessListApi.class );			// default

		ApiRegistry.register( I18nApi.class );						// default
		ApiRegistry.register( NavigationListApi.class );			// default

		ApiRegistry.register( TagsApi.class );						// default
		ApiRegistry.register( DeleteTagsApi.class );						// default

		ApiRegistry.register( AppPropertyApi.class );						// default

		ApiRegistry.register( UserProcessApi.class );   // queues
		ApiRegistry.register( UserEmailApi.class );   // queues
		ApiRegistry.register( UserFacebookValidationApi.class );   // queues
		ApiRegistry.register( UserBackupApi.class );   // queues

		ApiRegistry.register( AccessTokenCleanupApi.class );   // queues
		ApiRegistry.register( AuditLogProcessApi.class );   // queues

		ApiRegistry.register( PratilipiProcessApi.class );   // queues
		ApiRegistry.register( PratilipiStatsApi.class );   // queues
		ApiRegistry.register( PratilipiBackupApi.class );   // queues
		ApiRegistry.register( PratilipiIdfApi.class );   // queues

		ApiRegistry.register( AuthorProcessApi.class );   // queues
		ApiRegistry.register( AuthorBackupApi.class );   // queues

		ApiRegistry.register( NotificationProcessApi.class );   // queues
		ApiRegistry.register( EmailProcessApi.class );   // queues

		ApiRegistry.register( BatchProcessApi.class );   // queues

		ApiRegistry.register( ConversationEmailApi.class );   // queues

		ApiRegistry.register( UserPratilipiBackfillApi.class );   // queues

		ApiRegistry.register( TestApi.class );

	}

}
