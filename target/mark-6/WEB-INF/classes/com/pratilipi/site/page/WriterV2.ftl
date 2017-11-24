<!DOCTYPE html>
<html lang="${ lang }">
	<head>
		<#include "meta/Head.ftl">
		<#-- FB Event JS SDK -->
		<script>
			window.fbAsyncInit = function() {
				FB.init({
					appId      : '293990794105516',
					xfbml      : true,
					version    : 'v2.9'
				});
				FB.AppEvents.logPageView();
				<#-- Setting user ID for FB analytics -->
				FB.AppEvents.setUserID('${user.getId()?c}')
			};
		
			(function(d, s, id){
				var js, fjs = d.getElementsByTagName(s)[0];
				if (d.getElementById(id)) {return;}
				js = d.createElement(s); js.id = id;
				js.src = "//connect.facebook.net/en_US/sdk.js";
				fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));
			
		</script> 
	</head>
	<body>
		
		<#assign hasAccess = true>

		<#if user.isGuest() >
			<#assign hasAccess = false>
		<#elseif !pratilipiId?? >
			<#assign hasAccess = false>
		<#elseif !pratilipi.hasAccessToUpdate() >
			<#assign hasAccess = false>
		</#if>


		<#if hasAccess>
			<#include "../element/standard/writer-panel-tinymce/index.html">
		<#else>
			<script>
				$( document ).ready(function() {
				    window.location.href = "/?action=start_writing";
				});
			</script>
		</#if>
		<#include "meta/Font.ftl">
	</body>
</html>
