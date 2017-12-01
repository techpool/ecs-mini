<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#-- Page Description -->
		<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">
		
		<#include "meta/HeadBasic.ftl">
		
		<style>
			img {
				max-width: 100%!important;
			}
		</style>
		
	</head>

	<body>
		<#include "../element/basic/pratilipi-header.ftl">
			<div class="parent-container">
				<div class="container">
					<div id="androidLaunchBottom">
						<div class="secondary-500 pratilipi-shadow box" style="padding: 12px 10px;">
							<h2 class="pratilipi-red">${ blogPost.getTitle() }</h2>
						</div>
						
						<div class="secondary-500 pratilipi-shadow box" style="width: 100%; padding: 15px 20px; margin-bottom: 10px;">
							<div>${ blogPost.getContent() }</div>
						</div>
					</div>
				</div>
			</div>		
			<#include "../element/basic/pratilipi-footer.ftl">
	</body>
	
</html>