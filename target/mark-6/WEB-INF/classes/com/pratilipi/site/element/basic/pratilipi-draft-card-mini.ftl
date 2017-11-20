<div class="media pratilipi-without-margin">
  <a class="media-left" href="${ pratilipi.getPageUrl() }">
    <img class="media-object" src="${ pratilipi.getCoverImageUrl(75) }">	
  </a>
  <div class="media-body">
  	<div class="pull-left">	
	    <a href="${ pratilipi.getPageUrl() }"><h3 class="media-heading bigger-line-height">${ pratilipi.getTitle()!pratilipi.getTitleEn() } </h3></a>
	    <div>
	    	<button type="button" class="pratilipi-red-button pratilipi-padding-7" onclick="changePratilipiState( '${ pratilipi.getId()?c }', 'PUBLISHED' )">${ _strings.pratilipi_publish_it }</button>
	    	<button type="button" class="pratilipi-red-button pratilipi-padding-7" onclick="confirmAndChangePratilipiState( '${ pratilipi.getId()?c }', 'DELETED' )">${ _strings.pratilipi_delete_content }</button>
	    </div> 
	 </div>
  </div>
  <hr>
</div>
