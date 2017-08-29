<style>
	.comment-input {
		min-height: 120px; 
		background: #F5F5F5; 
		padding: 16px 14px;
		border: 1px solid #d3d3d3;
		border-right: 1px solid #f0f0f0;
	}
	textarea {
		border: 1px solid #d3d3d3;
		resize: none;
		width: 100%;
		min-height: 88px;
		padding-left: 8px;
		padding-right: 8px;
		box-sizing: border-box;
	}
	
	.pratilipi-grey-button {
		margin-top: 1px;
	}
	
	button.pratilipi-dark-blue-button {
		padding: 7px 21px;
		min-width: auto;
		height: auto;
	}
</style>

<script>
	function addComment() {
		var parentId = getUrlParameter( "parentId" );
		if( parentId == null || parentId.trim() == "" )
			return;
		
		var content = jQuery( '#comment-input' ).val();
		if( content == null || content.trim() == "" )
			return;
		
		jQuery( '#submitCommentButton' ).prop( 'disabled', true );

		$.ajax({
			type: 'post',
			url: '/api/comment',
			data: { 
				parentId: parentId,
				parentType: "REVIEW",
				content: content
			},
			success: function( response ) {
				jQuery( '#submitCommentButton' ).prop( 'disabled', false );
				window.location.href = "${ pratilipi.getPageUrl() }"; 
			},
			error: function( response ) {
				jQuery( '#submitCommentButton' ).prop( 'disabled', false );
				var message = jQuery.parseJSON( response.responseText );
				var status = response.status;
				if( message["message"] != null )
					alert( "Error " + status + " : " + message["message"] ); 
				else
					alert( "Some error occurred! Please try again!" );
			}
		});
	}
</script>

<div class="secondary-500 pratilipi-shadow box comment-input">
	<div class="row" style="height: 100%;">
		<div class="col-xs-12 col-sm-1 col-md-1 col-lg-1" style="margin-bottom: 8px;">
			<img class="img-circle" style="margin: 0 auto;" src="${ user.getProfileImageUrl( 64 ) }" alt="${ user.getDisplayName() }" title="${ user.getDisplayName() }"/>
			<span class="hidden-sm hidden-md hidden-lg">${ user.getDisplayName() }</span>
		</div>
		<div class="col-xs-12 col-sm-11 col-md-11 col-lg-11" style="height: 100%;">
			<textarea id="comment-input" placeholder="${ _strings.comment_reply_comment_help }"></textarea>
		</div>
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<button id="submitCommentButton" class="pratilipi-dark-blue-button pull-right" onClick="addComment()">${ _strings.comment_submit_comment }</button>
			<a class="pratilipi-grey-button pull-right" href="${ pratilipi.getPageUrl() }">${ _strings.comment_cancel_comment }</a>
		</div>
	</div>
</div>