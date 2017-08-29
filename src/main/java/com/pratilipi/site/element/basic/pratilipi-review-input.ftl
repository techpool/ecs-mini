
<script type="text/javascript">
	function submitReview() {
		var rating = $( '#inputRating' ).val();
		var review = $( '#inputReview' ).val();

		if( rating == null || rating == 0 ) {
			alert( "${ _strings.rating_mandatory_message }" );
			return;
		}

		<#-- Make Ajax call -->

		$.ajax({
		
			type: 'post',
			url: '/api/userpratilipi/review',

			data: { 
				'pratilipiId': ${ pratilipi.id?c }, 
				'rating': rating,
				'review' : review
			},
			
			success: function( response ) {
				if( getUrlParameter( "ret" ) == null )
					window.location.href = "${ pratilipi.pageUrl }";
				else
					window.location.href = decodeURIComponent( getUrlParameter( "ret" ) );
			},
			
			error: function ( response ) {
				var message = jQuery.parseJSON( response.responseText );
				alert( message["message"] );
			}
		});
	}
</script>


<div class="secondary-500 pratilipi-shadow box text-center" style="min-height: 370px;">

		<h3 style="margin-top: 10px; margin-bottom: 15px;" class="pratilipi-red">${ _strings.review_write_a_review }</h3>
		<p class="text-muted">${ _strings.review_content_help }</p>

		<div style="width: 150px; height: 225px; margin: 15px auto;" class="pratilipi-shadow">
			<img src="${ pratilipi.getCoverImageUrl( 150 ) }" alt="${ pratilipi.title!pratilipi.titleEn }" title="${ pratilipi.titleEn!pratilipi.title }" />
		</div>
		<h3 class="pratilipi-red">${ pratilipi.title!pratilipi.titleEn }</h3>
		<#if pratilipi.author?? >
			<a href="${ pratilipi.author.pageUrlAlias!pratilipi.author.pageUrl }"><h5>${ pratilipi.author.name }</h5></a>
		</#if>

	<form id="reviewInputForm" class="form-horizontal" action="javascript:void(0);" style="margin-top: 32px;">
		<div class="form-group">
			<label for="inputRating" class="col-sm-2 control-label">${ _strings.rating_your_rating }</label>
			<div class="col-sm-10">
				<select class="form-control" id="inputRating">
					<option  
						<#if userpratilipi.rating??>
							<#if userpratilipi.rating == 5>selected="selected"</#if>
						</#if>
						value="5">${ _strings.rating_5_star }</option>
					<option
						<#if userpratilipi.rating??>
							<#if userpratilipi.rating == 4>selected="selected"</#if>
						</#if> 
						value="4">${ _strings.rating_4_star }</option>
					<option
						<#if userpratilipi.rating??>
							<#if userpratilipi.rating == 3>selected="selected"</#if>
						</#if> 
						value="3">${ _strings.rating_3_star }</option>
					<option
						<#if userpratilipi.rating??>
							<#if userpratilipi.rating == 2>selected="selected"</#if>
						</#if> 
						value="2">${ _strings.rating_2_star }</option>
					<option
						<#if userpratilipi.rating??>
							<#if userpratilipi.rating == 1>selected="selected"</#if>
						</#if> 
						value="1">${ _strings.rating_1_star }</option>
				</select>
			</div>
		</div>
		
		<div class="form-group">
	        <label for="inputReview" class="col-sm-2 control-label">${ _strings.review_content }</label>
	        <div class="col-sm-10">
	            <textarea 	name="inputReview" 
	            			type="text" 
	            			class="form-control" 
	            			id="inputReview" rows="10" 
	            			placeholder="Review"><#if userpratilipi.review??>${ userpratilipi.review }</#if></textarea>
	        </div>
	    </div>
	    <div class="form-group" style="margin: 25px auto; text-align: center;">
	    	<button class="pratilipi-dark-blue-button" onclick="submitReview()">${ _strings.review_submit_review }</button>
	    </div>
	</form>
</div>

