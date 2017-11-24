<div class="pratilipi-shadow pratilipi-block secondary-500 ">
	<ul class="nav nav-tabs" role="tablist">
		<li style="width: 50%;" role="presentation" class="text-center <#if publishedPratilipiList?has_content>active</#if>">	
			<a class="nav-tab-red-underline" href="#author-activity" class="text-center" aria-controls="author-activity" role="tab" data-toggle="tab">${ _strings.author_activity }</a>
		</li>
		<li style="width: 50%;" role="presentation" class="text-center <#if !publishedPratilipiList?has_content>active</#if>">				
			<a class="nav-tab-red-underline" href="#author-about" class="text-center" aria-controls="author-about" 	role="tab" data-toggle="tab">${ _strings.author_about }</a>
		</li>
	</ul>
</div>
<div class="tab-content">
	<#include "pratilipi-author-activity.ftl">
	<#include "pratilipi-author-about.ftl">
</div>