<script>
	$( document ).ready(function() {
	    $( "#book-rating-${ pratilipi.getId()?c }" ).prepend( roundOffRating( ${ pratilipi.getAverageRating() } ) );
	});
</script>

<div class="media pratilipi-without-margin">
  <a class="media-left" href="${ pratilipi.getPageUrl() }">
    <img class="media-object" src="${ pratilipi.getCoverImageUrl(75) }">	
  </a>
  <div class="media-body" style="position: relative;">
	  	<div class="col-xs-11">
			<a href="${ pratilipi.getPageUrl() }"><h4 class="media-heading clip-content-1-line bigger-line-height pratilipi-card-heading pratilipi-without-margin">${ pratilipi.getTitle()!pratilipi.getTitleEn() } &nbsp; </h4></a>
		    <div>
			    <#if ( pratilipi.getReadCount() > 0 ) >
			    	<span style="color: #696969;">${ pratilipi.getReadCount() } <div class="sprites-icon read-count-icon"></div></span>
			    </#if>
			    <#if ( pratilipi.getAverageRating() >= 1 ) >
			    	<span style="color: #696969;" id="book-rating-${ pratilipi.getId()?c }"> <div class="sprites-icon star-icon"></div></span>
			    </#if>			    
		    </div>
		 </div>
		 <div class="col-xs-1 pratilipi-no-padding" >
		 	<a onclick="gotoShare( '${ pratilipi.getPageUrl() }', '${ pratilipi.getTitle()!pratilipi.getTitleEn() }', 'web_mini', 'content_share' )" >
		 		<div class="sprites-icon share-small-icon"></div> 
		 	</a> 
		 </div> 
	    <div class="align-to-bottom">
	    	<#if user.isGuest() == false>
	    		<#if author.hasAccessToUpdate() >
	    			<button type="button" class="pratilipi-red-button pratilipi-card-mini-button pratilipi-font-size-14" onclick="confirmAndChangePratilipiState( '${ pratilipi.getId()?c }', 'DRAFTED' )">${ _strings.pratilipi_move_to_drafts }</button>
	    		</#if>
	    		<#if ( author.getUser()?? && ( user.getId() != author.getUser().getId() ) )>
		    		<#if pratilipi.isAddedToLib() == true>
		    			<button type="button" class="pratilipi-grey-button pratilipi-card-mini-button pratilipi-font-size-14" onclick="AddToLibrary( ${ pratilipi.getId()?c }, false )">- ${ _strings.library } </button>
		    		<#else>
		    			<button type="button" class="pratilipi-red-button pratilipi-card-mini-button pratilipi-font-size-14" onclick="AddToLibrary( ${ pratilipi.getId()?c }, true )">+ ${ _strings.library } </button>
		    		</#if>
		    	</#if>	
	    	</#if>
	    </div> 		 
  </div>
  <hr>
</div>
