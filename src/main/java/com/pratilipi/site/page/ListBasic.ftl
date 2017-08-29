<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#include "meta/HeadBasic.ftl">
	</head>
	<script>
	
		function triggerCleverTapContentClickEvent( content_id, content_name, author_id, author_name ) {
			var event_name = "Click Content Card";
			var params = {
				"Screen Name": "Content List",
			    "Location": "Category List",
			    "List Name": "${ pratilipiListTitle }",
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
					<#if pratilipiListTitle??>
						<div class="secondary-500 pratilipi-shadow box" style="padding: 12px 10px;">
							<h2 class="pratilipi-red">${ pratilipiListTitle }</h2>
						</div>
					</#if>
		
					<#list pratilipiList as pratilipi>
						<#include "../element/basic/pratilipi-pratilipi-card.ftl">
					</#list>
		
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