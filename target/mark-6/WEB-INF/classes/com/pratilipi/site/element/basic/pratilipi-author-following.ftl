<#if author.getUser()?? >
	<div class="pratilipi-shadow secondary-500 box">	
		<#assign followingUrl = "/following?uId=" + author.getUser().getId()?c + "&ret=" + requestUrl >
		<div class="pull-left">
			<a href="${ followingUrl }"><h5 class="pratilipi-red pratilipi-no-margin">
				${ _strings.author_following_heading }
			</h5></a>
			<a href="${ followingUrl }"><p class="works-number">${ followingList.getNumberFound() } ${ _strings.author_follow_members }</p></a>
		</div>
		<#if ( followingList.getNumberFound() > 3 )>
			<a class="pull-right pratilipi-red pratilipi-view-more-link" href="${ followingUrl }"><div class="sprites-icon arrow-right-red-icon"></div></a>
		</#if>
		<div class="clearfix"></div>
		<hr class="pratilipi-margin-top-2">	
		
		<#if followingList.getAuthorList()?has_content>
			<#include "pratilipi-follow-author-card.ftl">
			<#list followingList.getAuthorList() as local_author>
				<#if local_author.getUser()?? && local_author.getUser().getId()?? && local_author.getUser().getId() == user.getId() >
					<#assign can_follow_boolean = "false">
				<#else>
					<#assign can_follow_boolean = "true">
				</#if>
				<@follow_author_card isGuest=user.isGuest()?c can_follow=can_follow_boolean retUrl=author.getPageUrl() authorId=local_author.getId() followCount=local_author.getFollowCount() following=local_author.isFollowing() name=local_author.getName()!local_author.getNameEn() pageUrl=local_author.getPageUrl() imageUrl=local_author.getProfileImageUrl(100) />
			</#list>
		</#if>
						
	</div>	
</#if>	