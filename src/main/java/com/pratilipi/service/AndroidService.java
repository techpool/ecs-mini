package com.pratilipi.service;

import com.pratilipi.api.ApiRegistry;
import com.pratilipi.api.GenericBatchApi;
import com.pratilipi.api.GenericService;
import com.pratilipi.api.impl.author.AuthorApi;
import com.pratilipi.api.impl.author.AuthorCoverApi;
import com.pratilipi.api.impl.author.AuthorCoverRemoveApi;
import com.pratilipi.api.impl.author.AuthorImageApi;
import com.pratilipi.api.impl.author.AuthorImageRemoveApi;
import com.pratilipi.api.impl.author.AuthorRecommendApi;
import com.pratilipi.api.impl.comment.CommentApi;
import com.pratilipi.api.impl.comment.CommentListApi;
import com.pratilipi.api.impl.contact.ContactApi;
import com.pratilipi.api.impl.event.EventApi;
import com.pratilipi.api.impl.event.EventListApi;
import com.pratilipi.api.impl.init.InitBannerListApi;
import com.pratilipi.api.impl.init.InitV1Api;
import com.pratilipi.api.impl.init.InitV2Api;
import com.pratilipi.api.impl.navigation.NavigationListApi;
import com.pratilipi.api.impl.notification.NotificationApi;
import com.pratilipi.api.impl.notification.NotificationBatchApi;
import com.pratilipi.api.impl.notification.NotificationListApi;
import com.pratilipi.api.impl.page.PageApi;
import com.pratilipi.api.impl.page.PageContentApi;
import com.pratilipi.api.impl.personalizedinit.PersonalizedInitApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentBatchApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentChapterDeleteApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentImageApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentIndexApi;
import com.pratilipi.api.impl.pratilipi.PratilipiContentV1Api;
import com.pratilipi.api.impl.pratilipi.PratilipiContentV3Api;
import com.pratilipi.api.impl.pratilipi.PratilipiCoverApi;
import com.pratilipi.api.impl.pratilipi.PratilipiListV1Api;
import com.pratilipi.api.impl.pratilipi.PratilipiListV2Api;
import com.pratilipi.api.impl.pratilipi.PratilipiListV3Api;
import com.pratilipi.api.impl.pratilipi.PratilipiTagsUpdateApi;
import com.pratilipi.api.impl.pratilipi.PratilipiV1Api;
import com.pratilipi.api.impl.pratilipi.PratilipiV2Api;
import com.pratilipi.api.impl.user.UserAccessTokenApi;
import com.pratilipi.api.impl.user.UserAccessTokenFcmTokenApi;
import com.pratilipi.api.impl.user.UserEmailApi;
import com.pratilipi.api.impl.user.UserFirebaseTokenApi;
import com.pratilipi.api.impl.user.UserLoginApi;
import com.pratilipi.api.impl.user.UserLoginFacebookApi;
import com.pratilipi.api.impl.user.UserLoginGoogleApi;
import com.pratilipi.api.impl.user.UserLogoutApi;
import com.pratilipi.api.impl.user.UserPasswordUpdateApi;
import com.pratilipi.api.impl.user.UserRegisterApi;
import com.pratilipi.api.impl.user.UserV1Api;
import com.pratilipi.api.impl.user.UserV2Api;
import com.pratilipi.api.impl.userauthor.UserAuthorApi;
import com.pratilipi.api.impl.userauthor.UserAuthorFollowListApi;
import com.pratilipi.api.impl.userauthor.UserAuthorFollowV1Api;
import com.pratilipi.api.impl.userauthor.UserAuthorFollowV2Api;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiLibraryApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiLibraryListApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiReviewApi;
import com.pratilipi.api.impl.userpratilipi.UserPratilipiReviewListApi;
import com.pratilipi.api.impl.vote.VoteApi;

@SuppressWarnings("serial")
public class AndroidService extends GenericService {
	
	static {
		
		ApiRegistry.register( GenericBatchApi.class );
		
		ApiRegistry.register( InitV1Api.class );
		ApiRegistry.register( InitV2Api.class );
		ApiRegistry.register( PersonalizedInitApi.class );		// By GROWTH_TEAM
		ApiRegistry.register( InitBannerListApi.class );
		
		ApiRegistry.register( UserV1Api.class );
		ApiRegistry.register( UserV2Api.class );
		ApiRegistry.register( UserAccessTokenApi.class );
		ApiRegistry.register( UserAccessTokenFcmTokenApi.class );
		ApiRegistry.register( UserFirebaseTokenApi.class );
		ApiRegistry.register( UserLoginApi.class );
		ApiRegistry.register( UserLoginFacebookApi.class );
		ApiRegistry.register( UserLoginGoogleApi.class );
		ApiRegistry.register( UserEmailApi.class );
		ApiRegistry.register( UserLogoutApi.class );
		ApiRegistry.register( UserRegisterApi.class );
		ApiRegistry.register( UserPasswordUpdateApi.class );

		ApiRegistry.register( PageApi.class );
		ApiRegistry.register( PageContentApi.class );
		
		ApiRegistry.register( PratilipiV1Api.class );
		ApiRegistry.register( PratilipiV2Api.class );
		ApiRegistry.register( PratilipiListV1Api.class );
		ApiRegistry.register( PratilipiListV2Api.class );
		ApiRegistry.register( PratilipiListV3Api.class );
		ApiRegistry.register( PratilipiContentV1Api.class );
		ApiRegistry.register( PratilipiContentV3Api.class );
		ApiRegistry.register( PratilipiContentBatchApi.class );
		ApiRegistry.register( PratilipiContentChapterDeleteApi.class );
		ApiRegistry.register( PratilipiContentIndexApi.class );
		ApiRegistry.register( PratilipiCoverApi.class );
		ApiRegistry.register( PratilipiContentImageApi.class );
		ApiRegistry.register( PratilipiTagsUpdateApi.class );
		
		ApiRegistry.register( AuthorApi.class );
		ApiRegistry.register( AuthorImageApi.class );
		ApiRegistry.register( AuthorImageRemoveApi.class );
		ApiRegistry.register( AuthorCoverApi.class );
		ApiRegistry.register( AuthorCoverRemoveApi.class );
		ApiRegistry.register( AuthorRecommendApi.class );
		
		ApiRegistry.register( EventApi.class );
		ApiRegistry.register( EventListApi.class );
		
		ApiRegistry.register( UserPratilipiApi.class );
		ApiRegistry.register( UserPratilipiLibraryApi.class );
		ApiRegistry.register( UserPratilipiLibraryListApi.class );
		
		ApiRegistry.register( UserPratilipiReviewApi.class );
		ApiRegistry.register( UserPratilipiReviewListApi.class );

		ApiRegistry.register( UserAuthorApi.class );
		ApiRegistry.register( UserAuthorFollowV1Api.class );
		ApiRegistry.register( UserAuthorFollowV2Api.class );
		ApiRegistry.register( UserAuthorFollowListApi.class );

		ApiRegistry.register( CommentApi.class );
		ApiRegistry.register( CommentListApi.class );
		
		ApiRegistry.register( VoteApi.class );
		
		ApiRegistry.register( ContactApi.class );
		
		ApiRegistry.register( NavigationListApi.class );

		ApiRegistry.register( NotificationApi.class );
		ApiRegistry.register(NotificationBatchApi.class );
		ApiRegistry.register( NotificationListApi.class );

	}
	
}
