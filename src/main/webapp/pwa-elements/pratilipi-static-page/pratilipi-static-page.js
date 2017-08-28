function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.initialDataLoaded = ko.observable( false );
	this.title = ko.observable();
    this.content = ko.observable();

	this.fetchStaticContent = function() {
		dataAccessor.getPageContent( window.location.pathname.substring(1).replace( "/", "_" ),
			function( pageContent ) {
				self.title( pageContent.title );
				self.content( pageContent.content );
				self.initialDataLoaded( true );
		});
	};

	this.locationObserver = ko.computed( function() {
        if( appViewModel.currentView() != LOCATION.STATIC.ko_element ) {
            /* Dispose all observers */
            self.locationObserver.dispose();
            return;
        }
        appViewModel.currentLocation();
        setTimeout( function() {
            self.initialDataLoaded( false );
            self.fetchStaticContent();
        }, 0 );
    }, this );

}
