<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#-- Page Description -->
		<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">
		
		<#include "meta/HeadBasic.ftl">
	</head>

	<body>
		<#include "../element/basic/pratilipi-header.ftl">
		<div class="parent-container">
			<div class="container">
				<div id="androidLaunchBottom">
					<div class="secondary-500 pratilipi-shadow box" style="padding: 20px; overflow: hidden;">
						<h3>${ title }</h3>
						<div style="text-align: justify;">${ content }</div>
					</div>
				</div>	
			</div>
		</div>
		<#include "../element/basic/pratilipi-footer.ftl">
	</body>
	
</html>