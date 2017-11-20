<script>
	var convertDateToYMD = function( input ) {
		var datePart = input.match(/\d+/g);
		var day = datePart[0], month = datePart[1], year = datePart[2];
		return year+'-'+month+'-'+day;
	};
	$( document ).ready(function() {
	    $( "#since-date-${ author.getId()?c }" ).append( convertDate( ${ author.getRegistrationDateMillis()?c } ) );
	    <#if author.getDateOfBirth()?? >
	    	$( "#birth-date-${ author.getId()?c }" ).append( convertDate( convertDateToYMD( "${ author.getDateOfBirth() }" ) ) );
	    </#if>
	});
</script>
<div class="pratilipi-shadow secondary-500 box">
	<div class="pull-left">
		<h5 class="pratilipi-red pratilipi-bold">
			${ _strings.author_biography } &nbsp;
		</h5>					
	</div>
	<div class="clearfix"></div>
	<hr>
	<#if author.getRegistrationDateMillis()?? >
		<p id="since-date-${ author.getId()?c }"><span class="pratilipi-bold">${ _strings.author_since }: </span> &nbsp; &nbsp; </p>
	</#if>
	<#if author.getDateOfBirth()?? && author.getDateOfBirth() != "" >
		<p id="birth-date-${ author.getId()?c }"><span class="pratilipi-bold">${ _strings.author_date_of_birth }:</span> &nbsp; &nbsp; </p>
	</#if>
	<#if author.getLocation()?? && author.getLocation() != "" >
		<p><span class="pratilipi-bold">${ _strings.author_location }:</span> &nbsp; &nbsp; ${ author.getLocation() }</p>
	</#if>	
	<#if author.getSummary()?? && author.getSummary() != "" >
		<p class="pratilipi-bold">${ _strings.pratilipi_summary }: </p>
		<p> ${ author.getSummary() }</p>
	</#if>	
</div>