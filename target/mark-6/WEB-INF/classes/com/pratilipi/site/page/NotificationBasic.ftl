<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#include "meta/HeadBasic.ftl">
	</head>

	<body>
		<#include "../element/basic/pratilipi-header.ftl">
		<#include "../element/basic/pratilipi-facebook-login.ftl">
		<#if ( action?? && action == "settings" )>
			<#include "../element/basic/pratilipi-notification-settings.ftl">
		<#else>
			<div class="parent-container">
				<div class="container">
					<div class="pratilipi-shadow secondary-500 box">
						<#if user.isGuest()>
							<div style="padding: 25px 10px;" class="secondary-500 pratilipi-shadow box">
								<div class="sprites-icon size-24-icon info-icon"></div>
								<div class="text-center"><a class="login-link" href="/login?ret=/notification">${ _strings.user_login_to_view_notifications }</a></div>
							</div>							
						<#else>
							<div class="pull-left">
								<a style="cursor: pointer;">
									<div class="sprites-icon black-arrow-left-icon"></div>
							  	</a>	
							</div>
							<div class="pull-right">
								<a style="cursor: pointer;" href="/notifications?action=settings">
									<div class="sprites-icon settings-icon"></div>
							  	</a>
							</div>
							<div class="center-heading">
								<h5 class="pratilipi-red pratilipi-bold pratilipi-no-margin">
									${ _strings.notification_notifications }
								</h5>
							</div>					
							<div class="clearfix"></div>
							<hr>				
							<#if notificationList?has_content >
								<div class="list-group">
								<#list notificationList as notification>
									<#if ( notification.getMessage()?? && notification.getSourceUrl()?? ) >
										<a href="${ notification.getSourceUrl() }" style="margin-bottom:4px;" class="list-group-item <#if ( notification.getState() == "UNREAD")>notification-unread-state</#if>">${ notification.getMessage() }</a>
									</#if>	
								</#list>
								</div>
							<#else>
								<div style="padding: 50px 10px;" class="secondary-500 pratilipi-shadow box">
									<div class="sprites-icon size-24-icon info-icon"></div>
									<div class="text-center">${ _strings.notifications_no_notifications }</div>
								</div>							
							</#if>
						</#if>	
					</div>				
				</div>
			</div>			
		</#if>
		<#include "../element/basic/pratilipi-footer.ftl">
	</body>
	<script>
		function goBack() {
			if( getUrlParameter( "ret" ) != null )
				window.location.href =  getUrlParameter( "ret" );
			else
				window.location.href = "/";
		}
	</script>	
</html>