function( params ) {
	var self = this;

	this.pratilipi = params.pratilipi;
	this.userPratilipi = params.userPratilipi;
	this.openSettings = params.openSettings;
	this.switchLibraryState = params.switchLibraryState;
	this.sharePratilipi = params.sharePratilipi;
	this.reportContent = params.reportContent;
	this.openNavigationModal = params.openNavigationModal;
	this.index = params.index;
	this.chapterNo = params.chapterNo;
	this.setChapterNo = params.setChapterNo;

	this.changeChapter = function( vm, e ) {
        self.setChapterNo( vm.chapterNo );
        document.querySelector( '.mdl-layout' ).MaterialLayout.toggleDrawer();
    };

	this.pratilipiObserver = ko.computed( function() {
		if( self.pratilipi.title() == null ) return;
		setTimeout( function() {
			var divWidth = $( ".pratilipi-reader .pratilipi-reader-header .js-pratilipi-title-holder" ).width();
            var text = $( ".pratilipi-reader .pratilipi-reader-header .js-pratilipi-title-holder .js-pratilipi-title" );
            var fontSize = 20.5;
            do {
                text.css( "font-size", fontSize -= 0.5 );
            } while( text.width() > divWidth && fontSize > 12 );
		}, 0 );
	}, this );

}