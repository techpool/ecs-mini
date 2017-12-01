<#if ( ( user.isGuest() == false ) && ( author.getUser()?? ) && ( author.getUser().getId()?? ) && ( user.getId() == author.getUser().getId() ) && ( !publishedPratilipiList?has_content ) ) >
<#else>
	<#assign publishedUrl = "?action=list_contents&state=PUBLISHED">
	<div class="pratilipi-shadow secondary-500 box">	
		<div class="pull-left">
			<a href="${ publishedUrl }"><h5 class="pratilipi-red pratilipi-no-margin">
				${ _strings.author_published_works }
			</h5></a>
			<a href="${ publishedUrl }"><p class="works-number"> ${author.getContentPublished()} ${ _strings.author_count_works }</p></a>
		</div>
		<#if ( author.getContentPublished() > 3 )>	
			<a class="pull-right pratilipi-red pratilipi-view-more-link" href="${ publishedUrl }"><div class="sprites-icon arrow-right-red-icon"></div></a>
		</#if>	
		<div class="clearfix"></div>
		<hr class="pratilipi-margin-top-2">	
		
		<#if publishedPratilipiList?has_content>
			<#include "pratilipi-card-list.ftl">
		<#else>
			<div style="padding: 50px 10px;" class="secondary-500 pratilipi-shadow box">
				<img style="width: 48px; height: 48px; margin: 0px auto 20px auto; display: block;" 
						src="https://storage.googleapis.com/devo-pratilipi.appspot.com/icomoon_24_icons/SVG/info.svg" alt="${ _strings.author_no_contents_published }" />
				<div class="text-center">${ _strings.author_no_contents_published }</div>
			</div>
		</#if>
	</div>
</#if>
