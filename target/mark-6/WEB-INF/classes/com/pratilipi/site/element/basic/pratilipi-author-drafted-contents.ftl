<#if author.hasAccessToUpdate()==true >
	<#assign draftsUrl = "?action=list_contents&state=DRAFTED">
	<div class="pratilipi-shadow secondary-500 box">	
		<div class="pull-left">
			<a href="${ draftsUrl }"><h5 class="pratilipi-no-margin pratilipi-red">
				${ _strings.author_drafts }
					<#-- <span><button class="pratilipi-grey-button"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add new</button></span> -->
			</h5></a>
			<a href="${ draftsUrl }"><p class="works-number"> ${ author.getContentDrafted() } ${ _strings.author_drafts }</p></a>
		</div>
		<#if ( author.getContentDrafted() > 3 )>		
			<a class="pull-right pratilipi-red pratilipi-view-more-link" href="${ draftsUrl }"> <div class="sprites-icon arrow-right-red-icon"></div> </a>
		</#if>	                   
		<div class="clearfix"></div>
		<hr class="pratilipi-margin-top-2">	
		<#if draftedPratilipiList?has_content>
			<#list draftedPratilipiList as pratilipi>
				<#include "pratilipi-draft-card-mini.ftl">
			</#list>
		</#if>			
	</div>
</#if>

