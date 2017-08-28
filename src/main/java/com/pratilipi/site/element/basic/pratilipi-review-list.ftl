<div class="secondary-500 pratilipi-shadow box" style="padding: 12px 10px;">
	<h2 style="display: inline-block; margin-right: 10px;" class="pratilipi-red">${ _strings.review_heading }</h2>
	<#if pratilipi.getState() == "PUBLISHED">
		<#if userpratilipi?? && userpratilipi.hasAccessToReview()?? && userpratilipi.hasAccessToReview() == true>
				<a href="?review=write" style="display: inline-block" class="pratilipi-grey-button">
				<#if userpratilipi.review?? >
					${ _strings.review_edit_review }
				<#else>
					${ _strings.review_write_a_review }
				</#if></a>
		<#elseif user.isGuest() == true>
			<a href="/login?ret=${ pratilipi.getPageUrl() }?review=write" style="display: inline-block" class="pratilipi-grey-button">${ _strings.review_write_a_review }</a>
		</#if>	
	</#if>			
</div>

<#assign hasReview = false >
<#if userpratilipi?? && userpratilipi.review?? > <#-- User has review -->
	<#-- Show user review on top iff userpratilipi.reviewState != "DELETED" && userpratilipi.reviewState != "BLOCKED" -->
	<#if userpratilipi.reviewState != "DELETED" && userpratilipi.reviewState != "BLOCKED">
		<#assign review=userpratilipi>
		<#include "pratilipi-review.ftl">
	</#if>
	
	<#-- Check and run a loop to remove user's review from review List -->
	<#list reviewList as review>
		<#if review.getId() != userpratilipi.getId()>
			<#include "pratilipi-review.ftl">
		</#if>
	</#list>

<#else> <#-- User Doesn't have review -->
	<#if reviewList?has_content>
		<#list reviewList as review>
			<#include "pratilipi-review.ftl">
		</#list>
	</#if>
</#if>

<#if !hasReview>
	<div style="padding: 25px 10px;" class="secondary-500 pratilipi-shadow box">
		<div class="sprites-icon size-24-icon info-icon"></div>
		<div class="text-center">${ _strings.pratilipi_no_reviews }</div>
	</div>
</#if>