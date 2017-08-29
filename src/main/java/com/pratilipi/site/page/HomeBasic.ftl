<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#-- Page Description -->
		<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">
		
		<#include "meta/HeadBasic.ftl">
	</head>
	<script>
	
	function triggerCleverTapContentClickEvent( content_id, content_name, author_id, author_name ) {
		var event_name = "Click Content Card";
		var params = {
			"Screen Name": "Content List",
		    "Location": "Trending List",
		    <#-- Add list name -->
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
					<#list sections as section>
						<div class="secondary-500 pratilipi-shadow box" style="padding: 12px 10px;">
							<h2 class="pratilipi-red" style="display: inline-block;">${ section.getTitle() }</h2>
							<#if section["listPageUrl"]??>
								<a href="${ section.getListPageUrl() }" class="link pull-right pratilipi-blue" style="font-size: 13px;">${ _strings.view_more }...</a>
							</#if>
						</div>
						<#list section.getPratilipiList() as pratilipi>
							<#include "../element/basic/pratilipi-pratilipi-card.ftl">
						</#list>
						<div style="min-height: 10px;"></div>
					</#list>
				</div>
			</div>
		</div>
		<#include "../element/basic/pratilipi-footer.ftl">
		<script>
			$( document ).ready(function() {
				if( getUrlParameter( 'email' ) != null && getUrlParameter( 'token' ) != null ) {
					if( getUrlParameter( 'passwordReset' ) == "true" ) {
						window.location.href = "/updatepassword" + "?" + "email=" + getUrlParameter( 'email' ) + "&" + "token=" + getUrlParameter( 'token' );
					} else if( getUrlParameter( 'verifyUser' ) == "true" ) {
						$.ajax({
							type: 'post',
							url: '/api/user/verification',
							data: {
								'email' : getUrlParameter( 'email' ),
								'verificationToken': getUrlParameter( 'token' )
							},
							success: function( response ) {
								alert( "${ _strings.user_verified_success }" );
								window.location.href = "/";
							},
							error: function ( response ) {
								var message = jQuery.parseJSON( response.responseText );
								var status = response.status;
								if( message["email"] != null ) 
									alert( "Error " + status + " : " + message["email"] );
								else if( message["message"] != null )
									alert( "Error " + status + " : " + message["message"] ); 
								else
									alert( "Invalid Credentials" );
							}
						});
					}
				}
			});
		</script>
	</body>
	
</html>