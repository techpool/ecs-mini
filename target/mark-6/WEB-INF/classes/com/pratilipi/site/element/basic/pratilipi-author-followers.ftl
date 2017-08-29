<div class="pratilipi-shadow secondary-500 box">
	<#assign followersUrl = "/followers?aId=" + author.getId()?c + "&ret=" + requestUrl >	
	<div class="pull-left">
		<a href="${ followersUrl }"><h5 class="pratilipi-red pratilipi-no-margin">
			 ${ _strings.author_followers_heading }
		</h5></a>
		<a href="${ followersUrl }"><p class="works-number">${ followersList.getNumberFound() } ${ _strings.author_follow_members }</p></a>
	</div>
	<#if ( followersList.getNumberFound() > 3 )>
		<a class="pull-right pratilipi-red pratilipi-view-more-link" href="${ followersUrl }"><div class="sprites-icon arrow-right-red-icon"></div></a>
	</#if>	
	<div class="clearfix"></div>
	<hr class="pratilipi-margin-top-2">	

	<#if followersList.getUserList()?has_content>
		<#include "pratilipi-follow-author-card.ftl">
		<#list followersList.getUserList() as local_user>
			<#if user.getId()?? && ( user.getId() != local_user.getId() ) >
				<#assign can_follow_boolean = "true">
			<#else>
				<#assign can_follow_boolean = "false">
			</#if>
			<#assign local_author = local_user.getAuthor() >
			<@follow_author_card isGuest=user.isGuest()?c can_follow=can_follow_boolean retUrl=author.getPageUrl() authorId=local_author.getId() followCount=local_author.getFollowCount() following=local_author.isFollowing() name=local_user.getDisplayName() pageUrl=local_user.getProfilePageUrl() imageUrl=local_user.getProfileImageUrl(100)/>
		</#list>
	</#if>		
</div>	