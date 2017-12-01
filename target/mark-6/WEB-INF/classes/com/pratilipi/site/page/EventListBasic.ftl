<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#-- Page Description -->
		<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">
		
		<#include "meta/HeadBasic.ftl">
		<style>
			.title-holder {
				margin: 4px 0px 8px 0px;
			}
			.title-holder .event-title {
				font-size: 18px;
			}
			.title-holder .view-more {
				font-size: 14px;
				float: right;
				color: #094c89;
			}
			.description-summary {
				max-height: 120px; 
				overflow: hidden;
				padding-right: 4px;
				font-size: 14px;
				line-height: 20px;
			}
		</style>
	</head>

	<body>
		<#include "../element/basic/pratilipi-header.ftl">
			<div class="parent-container">
				<div class="container">
					<div id="androidLaunchBottom">
						<#list eventList as event>
							<div class="secondary-500 pratilipi-shadow box" style="padding: 12px 16px;">
								<div class="title-holder">
									<a class="pratilipi-red event-title" href="${ event.pageUrl }">${ event.name }</a>
									<a class="view-more" href="${ event.pageUrl }"><div class="sprites-icon arrow-right-red-icon"></div></a>
								</div>
								<div class="text-muted description-summary">${ event.getDescription()! }</div>
							</div>
						</#list>
					</div>
				</div>
			</div>		
			<#include "../element/basic/pratilipi-footer.ftl">
	</body>
	
</html>