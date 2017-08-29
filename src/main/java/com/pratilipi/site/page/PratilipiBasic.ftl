<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#include "meta/HeadBasic.ftl">
		<style>
			.comment {
				display: block;
				padding-left: 4px;
			}
			.comment .comment-child {
				border-left: 2px solid #d3d3d3;
				padding: 12px 8px 16px 8px;
			}
			.comment img.user-img {
				width: 36px;
				height: 36px;
				float: left;
			}
			.comment .user-name {
				margin-top: 8px;
			}
			.comment .user-name a {
				text-transform: capitalize;
			}
			.comment .content {
				margin-top: 12px;
				display: block;
				margin-left: 12px;
			}
		</style>
		<script>
			function loadComments( parentId ) {
				$.ajax({
					type: 'get',
					url: '/api/comment/list',
					data: {
						'parentId': parentId, 
						'parentType': "REVIEW"
					},
					success: function( response ) {
						var parsed_data = jQuery.parseJSON( response.trim().replace( /(?:\r\n|\r|\n)/g, '\\n' ) );
						var commentList = parsed_data[ "commentList" ];
						var cursor = parsed_data[ "cursor" ];
						var html = "";
						for( var i = commentList.length - 1; i >= 0; i-- ) {
							html += '<div class="comment">' + 
							   '<div class="comment-child">' +
							   '<a href="' + commentList[i].user.profilePageUrl + '">' +
							   '<img class="user-img img-circle" src="' + commentList[i].user.profileImageUrl + ( commentList[i].user.profileImageUrl.indexOf( '?' ) == -1 ? '?' : '&' ) + 'width=36' + '" />' +
							   '</a>' +
							   '<div class="user-name">' + 
							   '<a href="' + commentList[i].user.profilePageUrl + '">' + 
							   commentList[i].user.displayName + 
							   '</a>' + 
							   '</div>' +
							   '<div class="date-added">' + convertDate( commentList[i].lastUpdatedMillis != null ? commentList[i].lastUpdatedMillis : commentList[i].creationDateMillis ) + '</div>' + 
							   '<div class="content">' + ( commentList[i].content != null ? commentList[i].content : "&nbsp;" ) + '</div>' +
							   '<a style="margin-left: 12px;" class="pratilipi-red" href="<#if user.isGuest() == true>/login?ret=${ pratilipi.getPageUrl() }?review=reply%26parentId=<#else>?review=reply&parentId=</#if>' + parentId + '">${ _strings.comment_reply_to_comment }</a>' +
							   '</div>' + 
							   '</div>';
						}
						jQuery( "button#view-replies-" + parentId ).css( "display", "none" ); 
						jQuery( "div#comments-" + parentId ).html( html );
					},
					error: function( response ) {
						var message = jQuery.parseJSON( response.responseText );
						var status = response.status;
						if( message["message"] != null )
							alert( "Error " + status + " : " + message["message"] ); 
						else
							alert( "Error fetching replies for this review! Please try again" );
					}
				});
			}
		</script>
	</head>
	
	<body>
		<#include "../element/basic/pratilipi-header.ftl">
		<div class="parent-container">
			<div class="container">
				<div id="androidLaunchBottom">
					<#-- review param is null -->
					<#if !reviewParam??>
						<#assign reviewParam = "pratilipi">
					</#if>
					
					<#-- Edge cases -->
					<#-- 1. User Logged out from review input screen -->
					<#if reviewParam == "write" && ! userpratilipi?? >
						<#assign reviewParam = "pratilipi">
					</#if>
					
					<#-- 2. Author reviewing his own book -->
					<#if reviewParam == "write" && userpratilipi?? && userpratilipi.hasAccessToReview() == false >
						<div class="box" style="padding: 20px auto; text-align: center;">
							Sorry! You are not Authorized to write review for this book! You can review this book from logging in from a different account!
						</div>
						<#assign reviewParam = "hide">
					</#if>
		
					
					<#-- Show review-input if reviewParam == write. Else if reviewParam == list, show complete review-list with page numbers
							If reviewParam = pratilipi, show pratilipi page and few reviews. -->
					<#if ( action == "edit_content" && pratilipi.hasAccessToUpdate() )>
						<#include "../element/basic/pratilipi-edit-content.ftl">	
					<#elseif reviewParam == "write">
						<#include "../element/basic/pratilipi-review-input.ftl">
					<#elseif reviewParam == "reply">
						<#include "../element/basic/pratilipi-comment-input.ftl">
					<#elseif reviewParam == "list">
						<#include "../element/basic/pratilipi-review-list.ftl">
						<#-- Add page navigation -->
						<#assign currentPage = reviewListPageCurr>
						<#assign maxPage = reviewListPageMax>
						<#assign prefix = "?review=list&" >
						<#include "../element/basic/pratilipi-page-navigation.ftl">
					<#elseif reviewParam == "pratilipi">
						<#include "../element/basic/pratilipi-pratilipi.ftl">
						<#include "../element/basic/pratilipi-review-list.ftl">
						<#if pratilipi.reviewCount?? >
							<#if pratilipi.reviewCount gt 10>
								<div style="text-align: center; margin: 20px auto;">
									<a style="width: 100%; display: block;" class="pratilipi-new-blue-button text-center" href="?review=list">
										${ _strings.review_see_all_reviews }
									</a>
								</div>
							</#if>
						</#if>
					</#if>
				</div>
			</div>
		</div>
		<#include "../element/basic/pratilipi-footer.ftl">
	</body>
	
</html>