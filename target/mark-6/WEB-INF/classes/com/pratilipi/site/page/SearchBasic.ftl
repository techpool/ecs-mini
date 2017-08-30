<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#include "meta/HeadBasic.ftl">
	</head>
	<script>
	
	function triggerCleverTapContentClickEvent( content_id, content_name, author_id, author_name ) {
		var event_name = "Click Content Card";
		var params = {
			"Screen Name": "Search",
		    "Location": "Content Results",
		    "Value": "<#if pratilipiListSearchQuery??>${ pratilipiListSearchQuery }</#if>",
		    "Content ID": content_id,
		    "Content Name": content_name,		    
		    "Author ID": author_id,
		    "Author Name": author_name
		};		
	}
	</script>

	<body>
		<#include "../element/basic/pratilipi-header.ftl">
		<div class="parent-container">
			<div class="container">
				<div id="androidLaunchBottom">
					<#if pratilipiList?has_content>
						<#list pratilipiList as pratilipi>
							<#include "../element/basic/pratilipi-pratilipi-card.ftl">
						</#list>
					<#else>
						<div style="padding: 25px 10px;" class="secondary-500 pratilipi-shadow box">
						<div class="sprites-icon size-24-icon search-block-icon"></div>
							<div class="text-center">${ _strings.search_no_results_found }</div>
						</div>
					</#if>
		
					<#-- Add page navigation -->
					<#assign currentPage = pratilipiListPageCurr>
					<#assign maxPage = pratilipiListPageMax>
					
					<#include "../element/basic/pratilipi-page-navigation.ftl">
				</div>
			</div>
		</div>
		<#include "../element/basic/pratilipi-footer.ftl">
	</body>
	
</html>