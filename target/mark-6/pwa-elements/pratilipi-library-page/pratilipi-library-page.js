function() {
	var self = this;
	var cursor = null;
	var resultCount = 20;
	var dataAccessor = new DataAccessor();

	this.pratilipiList = ko.observableArray();
	this.hasMoreContents = ko.observable( true );
	
	/* Loading state */
	/*
	 * 4 Possible values for 'loadingState'
	 * LOADING
	 * LOADED_EMPTY
	 * LOADED
	 * FAILED
	 * 
	 * */
	this.loadingState = ko.observable();

	this.updatePratilipiList = function( pratilipiList ) {
		for( var i = 0; i < pratilipiList.length; i++ )
			self.pratilipiList.push( ko.mapping.fromJS( pratilipiList[i] ) );
	};

	this.fetchUserLibraryList = function() {
		if( self.loadingState() == "LOADING" || ! self.hasMoreContents() ) return;
		self.loadingState( "LOADING" );
		dataAccessor.getUserLibraryList( cursor, resultCount,
				function( pratilipiListResponse ) {
					if( pratilipiListResponse == null ) {
						self.loadingState( "FAILED" );
						return;
					}
					var loadMore = self.pratilipiList().length != 0;
					var pratilipiList = pratilipiListResponse.pratilipiList;
					self.updatePratilipiList( pratilipiList );
					cursor = pratilipiListResponse.cursor;
					self.loadingState( self.pratilipiList().length > 0 || pratilipiList.length > 0 ? "LOADED" : "LOADED_EMPTY" );
					self.hasMoreContents( pratilipiList.length == resultCount && cursor != null );
					if( loadMore ) ga_CA( 'Pratilipi', 'LoadMore' );
		});
	};

	this.userObserver = ko.computed( function() {
		if( appViewModel.user.isGuest() && appViewModel.user.userId() == 0 ) {
			goToLoginPage( null, { "message": "LIBRARY" } );
		}
	}, this );

	this.pageScrollObserver = ko.computed( function() {
		if( ( appViewModel.scrollTop() / $( ".js-pratilipi-list-grid" ).height() ) > 0.6 ) {
			setTimeout( function() {
				self.fetchUserLibraryList();
			}, 100 ); /* locationObserver and pageScrollObserver will be triggered at once. */
		}
	}, this );

	this.locationObserver = ko.computed( function() {
        if( appViewModel.currentView() != LOCATION.LIBRARY.ko_element ) {
            /* Dispose all observers */
            self.locationObserver.dispose();
            self.pageScrollObserver.dispose();
            self.userObserver.dispose();
            return;
        }
        appViewModel.currentLocation();
        setTimeout( function() {
            self.fetchUserLibraryList();
        }, 0 );
    }, this );

}
