<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#-- Page Description -->
		<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">
		
		<#include "meta/HeadBasic.ftl">
	</head>

	<body>
		<#include "../element/basic/pratilipi-header.ftl">
		<div class="parent-container" style="">
			<div class="container">
				<div id="androidLaunchBottom">
					<#if ( ( action == "edit_profile") && author.hasAccessToUpdate() )>
						<#include "../element/basic/pratilipi-author-settings.ftl">
					<#elseif action == "list_contents">
					<div class="pratilipi-shadow secondary-500 box">
						<#if state == "PUBLISHED">
							<#include "../element/basic/pratilipi-published-list.ftl">
						<#elseif state == "DRAFTED">
							<#include "../element/basic/pratilipi-drafted-list.ftl">						
						</#if>
					</div>
					<#elseif ( action=="account" && !user.isGuest() )>
						<#include "../element/basic/pratilipi-my-account.ftl">	
					<#else>
						<#include "../element/basic/pratilipi-author-details.ftl">
						<#include "../element/basic/pratilipi-author-tabs.ftl">
					</#if>
				</div>
			</div>
		</div>
		<#include "../element/basic/pratilipi-footer.ftl">
		<script>
			function roundOffRating(n) {
			    return (Math.round(n*2)/2).toFixed(1);
			};		
			
			function FollowUnfollowPostRequest(follow){
			    $.ajax({type: "POST",
			            url: "/api/userauthor/follow",
			            data: { authorId: "${ author.getId()?c }", following: follow },
			            success:function(response){
			            	console.log(response);
			            	console.log(typeof response);
			            	
			            	var parsed_data = jQuery.parseJSON( response );
			      			if ( parsed_data.following == follow ) {
			      				window.location.reload();
			      			}
			      			else {
			      				
			      			}
			    		},
			            fail:function(response){
							alert("Sorry, we could not process your request.");
			    		}			    		
			    		
			    });
			  }
		</script>
	</body>
	
</html>