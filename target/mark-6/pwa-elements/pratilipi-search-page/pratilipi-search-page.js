function() {
	var self = this;
	var cursor = null;
	var resultCount = 20;
	var dataAccessor = new DataAccessor();

	this.searchTitle = ko.observable();
	this.isListPage = ko.observable(); /* TODO: Remove this once we are rid of search pages in navigation */
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

	this.fetchPratilipiList = function() {
		if( self.loadingState() == "LOADING" || ! self.hasMoreContents() ) return;
		self.loadingState( "LOADING" );
		dataAccessor.getPratilipiListBySearchQuery( appViewModel.searchQuery(), cursor, null, resultCount,
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

	var listsearchTitleMap = {
        <#list navigationList as navigation>
            <#list navigation.linkList as link>
            '${ link.url }': '${ link.name?replace( "'", "&#39;", 'r' ) }',
            </#list>
        </#list>
    };

	this.searchQueryUpdated = function() {
		/* Setting searchTitle for search pages coming from navigation */
		self.searchTitle( listsearchTitleMap[ "/search?q=" + appViewModel.searchQuery() ] != null ?
                          			listsearchTitleMap[ "/search?q=" + appViewModel.searchQuery() ] : "${ _strings.search_results }" );
		self.isListPage( listsearchTitleMap[ "/search?q=" + appViewModel.searchQuery() ] != null );
		self.pratilipiList( [] );
		cursor = null;
		self.loadingState( null );
		self.hasMoreContents( true );
		self.fetchPratilipiList();
	};

	this.searchQueryObserver = ko.computed( function() {
		if( appViewModel.searchQuery() == null )
			return;
		setTimeout( function() {
			self.searchQueryUpdated();
			ga_CAV( 'Header', 'Search', appViewModel.searchQuery() );
		}, 0 );
	}, this );

	this.pageScrollObserver = ko.computed( function() {
		if( ( appViewModel.scrollTop() / $( ".js-pratilipi-list-grid" ).height() ) > 0.6 ) {
			setTimeout( function() {
				self.fetchPratilipiList();
			}, 100 ); /* locationObserver and pageScrollObserver will be triggered at once. */
		}
	}, this );

	this.getLoadingStateText = ko.computed( function() {
		return "${ _strings.search_loading }".replace( "$query", appViewModel.searchQuery() );
	});

	this.locationObserver = ko.computed( function() {
        if( appViewModel.currentView() != LOCATION.SEARCH.ko_element ) {
            /* Dispose all observers */
            self.getLoadingStateText.dispose();
            self.locationObserver.dispose();
            self.pageScrollObserver.dispose();
            self.searchQueryObserver.dispose();
            return;
        }
    }, this );

}
