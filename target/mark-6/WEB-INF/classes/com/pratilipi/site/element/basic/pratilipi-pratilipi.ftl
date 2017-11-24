<#if userpratilipi?? && userpratilipi.isAddedtoLib()?? >
	<script>
		function addToOrRemoveFromLibrary() {
			tapClevertapEvent();
			$.ajax({
					
				type: 'post',
				url: '/api/userpratilipi/library',
	
				data: { 
					'pratilipiId': ${ pratilipi.getId()?c }, 
					'addedToLib': <#if userpratilipi.isAddedtoLib()>false<#else>true</#if>
				},
				
				success: function( response ) {
					var data = jQuery.parseJSON( response );
					if( data.addedToLib )
						alert( "${ _strings.added_to_library }" );
					else
						alert( "${ _strings.removed_from_library }" );

					location.reload(); 
				},
				
				error: function( response ) {
					var message = jQuery.parseJSON( response.responseText );
					var status = response.status;
	
					if( message["message"] != null )
						alert( "Error " + status + " : " + message["message"] );
					else if( message["error"] != null )
						alert( "Error " + status + " : " + message["error"] ); 
					else
						alert( "Some exception occured at the server! Please try again." );
				}
			});
		}
	</script>
</#if>

<script>
	$( document ).ready( function() {
		var pratilipiTypes = ${ pratilipiTypesJson };
		<#if pratilipi.getType() ??>
			$( '#pratilipiType-${ pratilipi.getId()?c }' ).html( pratilipiTypes[ "${ pratilipi.getType() }" ].name );
		</#if>
		$( '#creationDate-${ pratilipi.getId()?c }' ).html( "${ _strings.pratilipi_listing_date }&nbsp;&minus;&nbsp;" + convertDate( ${ pratilipi.getListingDateMillis()?c } ) );
		
		<#if pratilipi.hasAccessToUpdate()==true >
			$("#uploadPratilipiImageInput").hide();
		
		    $('#uploadPratilipiImage').on('submit',(function(e) {
		        e.preventDefault();
		        var formData = new FormData(this);
		
		        $.ajax({
		            type:'POST',
		            url: $(this).attr('action'),
		            data:formData,
		            cache:false,
		            contentType: false,
		            processData: false,
		            success:function(data){
		                location.reload();
		            },
		            error: function(data){
		                alert( "${ _strings.server_error_message }" );
		            }
		        });
		    }));
		    
		    $(".pratilipi-file-upload").on('click', function() {
		    	$("#uploadPratilipiImageInput").trigger('click');
		    });
		    
		    $("#uploadPratilipiImageInput").on('change', function() {
				$("#uploadPratilipiImage").submit();
			});		
		</#if>
	});
</script>

<div class="secondary-500 pratilipi-shadow box text-center">
	<h2 class="pratilipi-red" style="position: relative;">${ pratilipi.title!pratilipi.titleEn }
	</h2>
	
	<#if pratilipi.author?? >
		<a href="${ pratilipi.author.pageUrl }"><h4>${ pratilipi.author.name }</h4></a>
	</#if>
	<div style="position: relative;">
		<#if pratilipi.hasAccessToUpdate()==true >
			<a href="?action=edit_content" style="position: absolute;right: 36px;top: 0;"><div class="sprites-icon settings-icon"></div></a>
		</#if>
		<#if pratilipi.getState() == "PUBLISHED" >
			<a onclick="gotoShare( '${ pratilipi.getPageUrl() }', '${ pratilipi.title!pratilipi.titleEn }', 'web_mini', 'content_share' )" style="position: absolute;right: 0;top: 0;"><div class="sprites-icon share-icon"></div></a>
		</#if>
		<div style="width: 150px; height: 225px; margin: 15px auto; position: relative;" class="pratilipi-shadow">
			<img src="${ pratilipi.getCoverImageUrl( 150 ) }" alt="${ pratilipi.title!pratilipi.titleEn }" title="${ pratilipi.titleEn!pratilipi.title }" />
			<#if pratilipi.hasAccessToUpdate()==true >
				<div class="pratilipi-file-upload" style="margin-bottom: -2px;">
					<div class="sprites-icon book-camera-icon"></div>
				</div>
			</#if>
		</div>
	</div>
	<#-- <div style="display: block;" class="fb-like" data-href="http://www.pratilipi.com/pratilipi/${ pratilipi.getId() }" data-layout="button" data-action="like" data-size="small" data-show-faces="false" data-share="false"></div> -->
	<#if pratilipi.ratingCount gt 0 >
		<a <#if user.isGuest() == true>href="/login?ret=${ pratilipi.getPageUrl() }?review=write"<#else>href="?review=write"</#if> >
			<#assign rating=pratilipi.averageRating >
			<#include "pratilipi-rating.ftl" ><small>(${ pratilipi.ratingCount })</small>
		</a>
	<#else>
		<#if pratilipi.getState() == "PUBLISHED" && userpratilipi?? && userpratilipi.hasAccessToReview()?? && userpratilipi.hasAccessToReview() == true>
			<a <#if user.isGuest() == true>href="/login?ret=${ pratilipi.getPageUrl() }?review=write"<#else>href="?review=write"</#if> class="link" style="text-decoration: underline;">${ _strings.rating_be_first_one }</a>
		</#if>
	</#if>
	
	<h6 style="margin-top: 10px;" id="pratilipiType-${ pratilipi.getId()?c }"></h6>
	
	<div style="margin:25px 0px 5px 0px">
		<h5 id="creationDate-${ pratilipi.getId()?c }"></h5>
		<h5>${ _strings.pratilipi_count_reads }&nbsp;&minus;&nbsp;${ pratilipi.getReadCount()?c }</h5>
	</div>
	
	<div style="padding-top: 20px; padding-bottom: 20px;">
		<a class="pratilipi-light-blue-button" onClick="trackPixelEvents( 'ReadOnSummaryPage' );" href="${ pratilipi.readPageUrl }&ret=${ pratilipi.getPageUrl() }">${ _strings.read }</a>
		<br />
		<#if pratilipi.getState() == "PUBLISHED" && ( user.isGuest() || pratilipi.getAuthor().getId() != user.getAuthor().getId() )>
			<#if userpratilipi?? && userpratilipi.isAddedtoLib()??>
				<button style="margin-top: 15px;" type="button" class="pratilipi-grey-button" onclick="addToOrRemoveFromLibrary()">
					<#if !userpratilipi.isAddedtoLib()>${ _strings.add_to_library }<#else>${ _strings.remove_from_library }</#if>
				</button>
			<#else>
				<a style="margin-top: 15px;" class="pratilipi-grey-button" href="/login?ret=${ pratilipi.getPageUrl() }">
					${ _strings.add_to_library }
				</a>
			</#if>
		</#if>
	</div>
	<#if pratilipi.hasAccessToUpdate()==true >
		<form id="uploadPratilipiImage" method="post" enctype="multipart/form-data" action="/api/pratilipi/cover?pratilipiId=${ pratilipi.getId()?c }">
			<input id="uploadPratilipiImageInput" type="file" name="{{ pratilipi.getId()?c }}" accept="image/*">
		</form>
	</#if>
			
</div>

<#if pratilipi.summary?? && pratilipi.summary != "" >
	<div class="secondary-500 pratilipi-shadow box">
		<h2 style="margin-top: 10px; margin-bottom: 15px;" class="pratilipi-red text-center">${ _strings.pratilipi_summary }</h2>
		<div style="text-align: justify;">${ pratilipi.summary }</div>
	</div>
</#if>

<div style="min-height: 7px;"></div>