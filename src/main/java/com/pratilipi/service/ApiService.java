package com.pratilipi.service;

import com.pratilipi.api.ApiRegistry;
import com.pratilipi.api.GenericBatchApi;
import com.pratilipi.api.GenericService;
import com.pratilipi.api.batchprocess.BatchProcessApi;
import com.pratilipi.api.batchprocess.BatchProcessListApi;
import com.pratilipi.api.impl.appproperty.AppPropertyApi;
import com.pratilipi.api.impl.author.*;
import com.pratilipi.api.impl.blogpost.BlogPostApi;
import com.pratilipi.api.impl.blogpost.BlogPostListApi;
import com.pratilipi.api.impl.comment.CommentApi;
import com.pratilipi.api.impl.comment.CommentListApi;
import com.pratilipi.api.impl.contact.ContactApi;
import com.pratilipi.api.impl.contact.ConversationEmailApi;
import com.pratilipi.api.impl.ecs.EcsUserFollowCountApi;
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
import com.pratilipi.api.impl.page.PageApi;
import com.pratilipi.api.impl.page.PageContentApi;
import com.pratilipi.api.impl.personalizedinit.PersonalizedInitApi;
import com.pratilipi.api.impl.pratilipi.DeleteTagsApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentBatchApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentChapterAddApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentChapterDeleteApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentImageApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentIndexApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentV1Api;
import com.pratilipi.api.impl.pratilipi.PratilipiContentV2Api;
import com.pratilipi.api.impl.pratilipi.PratilipiContentV3Api;
import com.pratilipi.api.impl.pratilipi.PratilipiCoverApi;
import com.pratilipi.api.impl.pratilipi.PratilipiListV1Api;
import com.pratilipi.api.impl.pratilipi.PratilipiListV2Api;
import com.pratilipi.api.impl.pratilipi.PratilipiListV3Api;
import com.pratilipi.api.impl.pratilipi.PratilipiStatsApi;
import com.pratilipi.api.impl.pratilipi.PratilipiTagsUpdateApi;
import com.pratilipi.api.impl.pratilipi.PratilipiV1Api;
import com.pratilipi.api.impl.pratilipi.PratilipiV2Api;
import com.pratilipi.api.impl.pratilipi.TagsApi;
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
import com.pratilipi.common.util.PersonalizedInitUtil;

@SuppressWarnings("serial")
public class ApiService extends GenericService {
	
	static {
		
		ApiRegistry.register( InitV2Api.class );					// *.pratilipi.com
		ApiRegistry.register( PersonalizedInitApi.class );			// *.pratilipi.com
		ApiRegistry.register( GenericBatchApi.class );				// *.pratilipi.com
		
		ApiRegistry.register( InitBannerApi.class );				// *.pratilipi.com & AWS CloudFront
		ApiRegistry.register( InitBannerListApi.class );			// *.pratilipi.com

		ApiRegistry.register( UserLoginApi.class );					// *.pratilipi.com
		ApiRegistry.register( UserLoginFacebookApi.class );			// *.pratilipi.com
		ApiRegistry.register( UserLoginGoogleApi.class );			// *.pratilipi.com
		ApiRegistry.register( UserEmailApi.class );					// *.pratilipi.com
		ApiRegistry.register( UserLogoutApi.class );				// *.pratilipi.com
		ApiRegistry.register( UserRegisterApi.class );				// *.pratilipi.com
		ApiRegistry.register( UserVerificationApi.class );			// *.pratilipi.com
		ApiRegistry.register( UserPasswordUpdateApi.class );		// *.pratilipi.com
		ApiRegistry.register( UserFirebaseTokenApi.class );     // TODO: Hack - Remove it asap
		ApiRegistry.register( UserAccessTokenFcmTokenApi.class );   // TODO: Hack - Remove it asap

		ApiRegistry.register( PageApi.class );							// *.pratilipi.com
		ApiRegistry.register( PageContentApi.class );							// *.pratilipi.com
		
		ApiRegistry.register( PratilipiV1Api.class );					// *.pratilipi.com
		ApiRegistry.register( PratilipiV2Api.class );					// *.pratilipi.com
		ApiRegistry.register( PratilipiListV1Api.class );				// *.pratilipi.com
		ApiRegistry.register( PratilipiListV2Api.class );				// *.pratilipi.com
		ApiRegistry.register( PratilipiListV3Api.class );				// *.pratilipi.com
		ApiRegistry.register( PratilipiCoverApi.class );				// *.pratilipi.com & AWS CloudFront
		ApiRegistry.register( PratilipiContentV1Api.class );			// *.pratilipi.com
		ApiRegistry.register( PratilipiContentV2Api.class );			// *.pratilipi.com
		ApiRegistry.register( PratilipiContentV3Api.class );			// *.pratilipi.com
		ApiRegistry.register( PratilipiContentBatchApi.class );			// *.pratilipi.com
		ApiRegistry.register( PratilipiContentIndexApi.class );			// *.pratilipi.com
		ApiRegistry.register( PratilipiContentImageApi.class );			// *.pratilipi.com
		ApiRegistry.register( PratilipiContentChapterAddApi.class );	// *.pratilipi.com
		ApiRegistry.register( PratilipiContentChapterDeleteApi.class );	// *.pratilipi.com
		ApiRegistry.register( PratilipiStatsApi.class );				// *.pratilipi.com
		ApiRegistry.register( PratilipiTagsUpdateApi.class );			// *.pratilipi.com
		
		ApiRegistry.register( AuthorApi.class );					// *.pratilipi.com
		ApiRegistry.register( AuthorListApi.class );				// *.pratilipi.com
		ApiRegistry.register( AuthorImageApi.class );				// *.pratilipi.com & AWS CloudFront
		ApiRegistry.register( AuthorImageRemoveApi.class );			// *.pratilipi.com
		ApiRegistry.register( AuthorCoverApi.class );				// *.pratilipi.com & AWS CloudFront
		ApiRegistry.register( AuthorCoverRemoveApi.class );			// *.pratilipi.com
		ApiRegistry.register( AuthorRecommendApi.class );			// *.pratilipi.com
		ApiRegistry.register( AuthorListByReadCountApi.class );		// *.pratilipi.com
		
		ApiRegistry.register( EventApi.class );						// *.pratilipi.com
		ApiRegistry.register( EventListApi.class );					// *.pratilipi.com
		ApiRegistry.register( EventBannerApi.class );				// *.pratilipi.com
		
		ApiRegistry.register( BlogPostApi.class );					// *.pratilipi.com
		ApiRegistry.register( BlogPostListApi.class );				// *.pratilipi.com
		
		ApiRegistry.register( UserV1Api.class );					// *.pratilipi.com
		ApiRegistry.register( UserV2Api.class );					// *.pratilipi.com
		
		ApiRegistry.register( UserPratilipiApi.class );				// *.pratilipi.com
		ApiRegistry.register( UserPratilipiLibraryApi.class );		// *.pratilipi.com
		ApiRegistry.register( UserPratilipiLibraryListApi.class );	// *.pratilipi.com
		ApiRegistry.register( UserPratilipiBackfillApi.class );		// *.pratilipi.com
		
		ApiRegistry.register( UserPratilipiReviewApi.class );		// *.pratilipi.com
		ApiRegistry.register( UserPratilipiReviewListApi.class );	// *.pratilipi.com
		
		ApiRegistry.register( UserAuthorFollowV1Api.class );		// *.pratilipi.com
		ApiRegistry.register( UserAuthorFollowV2Api.class );		// *.pratilipi.com
		ApiRegistry.register( UserAuthorFollowListApi.class );		// *.pratilipi.com

		ApiRegistry.register( CommentApi.class );					// *.pratilipi.com
		ApiRegistry.register( CommentListApi.class );				// *.pratilipi.com
		
		ApiRegistry.register( VoteApi.class );						// *.pratilipi.com
		
		ApiRegistry.register( ContactApi.class );					// *.pratilipi.com
		ApiRegistry.register( ConversationEmailApi.class );			// *.pratilipi.com
		
		ApiRegistry.register( MailingListSubscribeApi.class );		// *.pratilipi.com

		ApiRegistry.register( NotificationApi.class );
		ApiRegistry.register( NotificationBatchApi.class );
		ApiRegistry.register( NotificationListApi.class );			// *.pratilipi.com
		
		ApiRegistry.register( BatchProcessApi.class );				// *.pratilipi.com
		ApiRegistry.register( BatchProcessListApi.class );			// *.pratilipi.com

		ApiRegistry.register( I18nApi.class );						// *.pratilipi.com
		ApiRegistry.register( NavigationListApi.class );			// *.pratilipi.com

		ApiRegistry.register( TagsApi.class );						// *.pratilipi.com
		ApiRegistry.register( DeleteTagsApi.class );						// *.pratilipi.com

		ApiRegistry.register( AppPropertyApi.class );						// *.pratilipi.com

		ApiRegistry.register( EcsUserFollowCountApi.class );

		ApiRegistry.register( TestApi.class );

	}
	
}
