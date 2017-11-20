<div role="tabpanel" class="tab-pane <#if !publishedPratilipiList?has_content>active</#if>" id="author-about">
	<#include "pratilipi-author-biography.ftl">
	<#if followingList??>
		<#include "pratilipi-author-following.ftl">
	</#if>
	<#include "pratilipi-author-followers.ftl">										
</div>