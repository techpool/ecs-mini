<#if review.getReview()?? && review.getReview() != "" >
	 
	<#assign hasReview = true >
	 
	<#if review.getReviewDateMillis()?? >
		<script>
			$( document ).ready( function() {
				$( '#reviewDate-${ review.getId() }' ).html( convertDate( ${ review.getReviewDateMillis()?c } ) );
			});
		</script>
	</#if>
	<div class="secondary-500 pratilipi-shadow box" style="padding: 10px 20px;">
		<div style="padding: 12px 4px;">
			<a href="${ review.getUserProfilePageUrl() }">
				<img style="margin: 0; margin-top: 8px;" class="img-circle pratilipi-shadow pull-left" src="${ review.getUser().getProfileImageUrl( 48 ) }" alt="${ review.getUserName() }" title="${ review.getUserName() }"/>
			</a>
			<div style="display: inline-block; margin-left: 16px;">
				<a style="text-transform: capitalize;" href="${ review.getUserProfilePageUrl() }">${ review.getUserName() }</a>  
                <#if review.rating?? >
					<div>
						<#assign rating=review.rating>
						<#include "pratilipi-rating.ftl" >
					</div>
				</#if>
				<#if review.getReviewDateMillis()?? >
					<span style="display: block;" id="reviewDate-${ review.getId() }"></span>
				</#if>
			</div>
		</div>
		<div style="text-align: justify; font-size: 15px; margin-bottom: 8px; clear: both; padding-top: 8px;">${ review.getReview() }</div>
		<div class="reply-section">
			<#if user.isGuest() == true>
				<a class="reply-text pratilipi-red" href="/login?ret=${ pratilipi.getPageUrl() }?review=reply%26parentId=${ review.getId() }">${ _strings.comment_reply_to_comment }</a>
			<#else>
				<a class="reply-text pratilipi-red" href="?review=reply&parentId=${ review.getId() }">${ _strings.comment_reply_to_comment }</a>
			</#if>
			<#if review.getCommentCount() gt 0>
				<button class="pratilipi-red" style="outline: none; border: none; padding: 0px; background: none; display: block; font-size: 14px;" id="view-replies-${ review.getId() }" onClick="loadComments( '${ review.getId() }' )">${ review.getCommentCount() }&nbsp;<#if review.getCommentCount() gt 1>${ _strings.comment_number_of_comments_plural }<#else>${ _strings.comment_number_of_comments_singular }</#if></button>
			</#if>
		</div>
		<div style="margin-top: 12px;" id="comments-${ review.getId() }"></div>
	</div>
</#if>