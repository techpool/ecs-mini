<script>
	function triggerCleverTapContentClickEvent( content_id, content_name, author_id, author_name ) {
		var event_name = "Click Content Card";
		var params = {
			"Screen Name": "Search",
		    "Location": "Content Results",
		    "Value": "${ pratilipiListSearchQuery }",
		    "Content ID": content_id,
		    "Content Name": content_name,		    
		    "Author ID": author_id,
		    "Author Name": author_name
		};		
	}
</script>

<div class="pratilipi-shadow secondary-500 box">
	<div class="align-text-center">
		<h2 class="pratilipi-red pratilipi-no-margin">
			${ _strings.event_entries }
		</h2>
	</div>
</div>

<#list pratilipiList as pratilipi>
	<#include "pratilipi-pratilipi-card.ftl">
</#list>

<#-- Add page navigation -->
<#assign currentPage = pratilipiListPageCurr>
<#assign maxPage = pratilipiListPageMax>
<#include "pratilipi-page-navigation.ftl">