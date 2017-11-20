function( params ) { 
	var self = this;

	/* Pass originalText as ko.observable() */
	this.originalText = params.originalText;

	/* Booleans */
	this.isViewMoreVisible = ko.observable( false );
	this.isViewMoreShown = ko.observable( true );
	this.maxHeightPx = ko.observable( "initial" );

	this.toggleViewMore = function( vm, e ) {
		self.isViewMoreShown( ! self.isViewMoreShown() );
		if( self.isViewMoreShown() ) {
			var pos = e.currentTarget.parentElement.parentElement.offsetTop; /* Not adding header padding */
			appViewModel.scrollToTop( pos );
		}
	};

}