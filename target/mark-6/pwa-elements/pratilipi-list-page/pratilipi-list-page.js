function() {
	var self = this;
	var cursor = null;
	var resultCount = 20;
	var dataAccessor = new DataAccessor();

	this.listTitle = ko.observable();
	this.pratilipiList = ko.observableArray();
	this.hasMoreContents = ko.observable( true );
	this.isLoading = ko.observable( false );


	this.updatePratilipiList = function( pratilipiList ) {
		for( var i = 0; i < pratilipiList.length; i++ )
			self.pratilipiList.push( ko.mapping.fromJS( pratilipiList[i] ) );
	};

	this.fetchPratilipiList = function() {
		if( self.isLoading() || ! self.hasMoreContents() ) return;
		self.isLoading( true );
		dataAccessor.getPratilipiListByListName( window.location.pathname.substring(1), cursor, null, resultCount,
				function( pratilipiListResponse ) {
					if( pratilipiListResponse == null ) {
						self.isLoading( false );
						return;
					}
					var loadMore = self.pratilipiList().length != 0;
					var pratilipiList = pratilipiListResponse.pratilipiList;
					self.updatePratilipiList( pratilipiList );
					cursor = pratilipiListResponse.cursor;
					self.isLoading( false );
					self.hasMoreContents( pratilipiList.length == resultCount && cursor != null );
					if( loadMore ) ga_CA( 'Pratilipi', 'LoadMore' );
		});
	};

	this.listUpdated = function() {
		self.listTitle( getListTitle() );
		self.pratilipiList( [] );
		cursor = null;
		self.hasMoreContents( true );
		self.isLoading( false );
		self.fetchPratilipiList();
	};

	this.pageScrollObserver = ko.computed( function() {
        if( ( appViewModel.scrollTop() / $( ".js-pratilipi-list-grid" ).height() ) > 0.6 ) {
            setTimeout( function() {
                self.fetchPratilipiList();
            }, 100 ); /* locationObserver and pageScrollObserver will be triggered at once. */
        }
    }, this );

	this.locationObserver = ko.computed( function() {
		if( appViewModel.currentView() != LOCATION.LIST.ko_element ) {
			/* Dispose all observers */
			self.locationObserver.dispose();
			self.pageScrollObserver.dispose();
			return;
		}
		appViewModel.currentLocation();
		setTimeout( function() {
			self.listUpdated();
		}, 0 );
	}, this );


	/* Hack: Setting listTitle */
	var getListTitle = function() {
		var listListTitleMap = {
			<#list listTitleList as aList>
				'${ aList.url }': '${ aList.title?replace( "'", "&#39;", 'r' ) }',
			</#list>
		};
		return listListTitleMap[ window.location.pathname ] != null ? listListTitleMap[ window.location.pathname ] : null;
	};

}
