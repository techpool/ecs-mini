function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	var pratilipiCursor = null;
	var authorCursor = null;

	var pratilipiResultCount = 20;
	var authorResultCount = 10;

	var pratilipiNumberFound = null;
	var authorNumberFound = null;

	this.pratilipiList = ko.observableArray();
	this.authorList = ko.observableArray();


	this.updatePratilipiList = function( pratilipiList ) {
		for( var i = 0; i < pratilipiList.length; i++ ) {
			if( pratilipiList[i].author.name == null )
				pratilipiList[i].author.name = " ";
			pratilipiList[i].pageUrl += ( pratilipiList[i].pageUrl.indexOf( "?" ) == -1 ? "?" : "&" ) + "searchQuery=" + encodeURIComponent( self.updatedSearchQuery() );
			self.pratilipiList.push( ko.mapping.fromJS( pratilipiList[i] ) );
		}
	};

	this.updateAuthorList = function( authorList ) {
		for( var i = 0; i < authorList.length; i++ ) {
			authorList[i].pageUrl += ( authorList[i].pageUrl.indexOf( "?" ) == -1 ? "?" : "&" ) + "searchQuery=" + encodeURIComponent( self.updatedSearchQuery() );
			self.authorList.push( ko.mapping.fromJS( authorList[i] ) );
		}
	};

	this.hasMorePratilipiContents = ko.observable( true );
	this.hasMoreAuthorContents = ko.observable( true );

	/* Loaded state */
	/*
	 * 4 Possible values for 'loadedState'
	 * INITIAL
	 * LOADED_EMPTY
	 * LOADED
	 * FAILED
	 *
	 * */
	this.loadedState = ko.observable( "INITIAL" );
	this.isLoading = ko.observable( false ); /* Initial Query loading state */

	this.isLoadingPratilipi = ko.observable( false );
	this.isLoadingAuthor = ko.observable( false );

	this.updatedSearchQuery = ko.observable(); /* For displaying to User */
	this.inputFocused = ko.observable( true ); /* 2 way binding */

	this.searchQueryObserver = ko.computed( function() {
		appViewModel.searchQuery();
		setTimeout( function() {
			if( appViewModel.searchQuery() == null || appViewModel.searchQuery().trim() == "" ) {
				self.loadedState( "INITIAL" );
				self.clearAllValues();
			} else {
				self.fetchInitialList();
			}
		}, 0 );
	}, this );

	this.clearAllValues = function() {
		self.pratilipiList( [] );
		self.authorList( [] );
		pratilipiCursor = null;
		authorCursor = null;
		pratilipiNumberFound = null;
		authorNumberFound = null;
		self.hasMorePratilipiContents( true );
		self.hasMoreAuthorContents( true );
	};

	this.fetchInitialList = function() {
		self.isLoadingPratilipi( false );
		self.isLoadingAuthor( false );
		self.isLoading( true );
		dataAccessor.getInitialSearchResults( appViewModel.searchQuery(),
			function( searchResponse ) {

				self.isLoading( false );
				self.updatedSearchQuery( appViewModel.searchQuery() );

				if( searchResponse == null ) {
					self.loadedState( "FAILED" );
					return;
				}

				var pratilipi = searchResponse.pratilipi != null && !isEmpty( searchResponse.pratilipi ) ? searchResponse.pratilipi : { "pratilipiList": [], "pratilipiCursor": null, "pratilipiNumberFound": 0 };
				var author = searchResponse.author != null && !isEmpty( searchResponse.author ) ? searchResponse.author : { "authorList": [], "authorCursor": null, "authorNumberFound": 0 };

				var pratilipiList = pratilipi.pratilipiList;
				var authorList = author.authorList;

				if( pratilipiList.length == 0 && authorList == 0 ) {
					self.loadedState( "LOADED_EMPTY" );
					self.clearAllValues();
					return;
				}

				self.pratilipiList( [] );
				self.authorList( [] );

				self.updatePratilipiList( pratilipiList );
				self.updateAuthorList( authorList );

				pratilipiCursor = pratilipi.pratilipiCursor;
				authorCursor = author.authorCursor;

				pratilipiNumberFound = pratilipi.numberFound;
				authorNumberFound = author.numberFound;

				self.hasMorePratilipiContents( pratilipiNumberFound != 0 && self.pratilipiList().length < pratilipiNumberFound );
				self.hasMoreAuthorContents( authorNumberFound != 0 && self.authorList().length < authorNumberFound );

				self.loadedState( "LOADED" );

		});
	};

	this.fetchSubsequentPratilipiList = function() {
		if( self.isLoadingPratilipi() )
			return;
		self.isLoadingPratilipi( true );
		ga_CA( "Pratilipi", "LoadMore" );
		dataAccessor.getPratilipiSearchResults( appViewModel.searchQuery(), pratilipiCursor, pratilipiResultCount,
			function( searchResponse ) {
				if( searchResponse == null ) {
					self.isLoadingPratilipi( false );
					return;
				}
				var pratilipi = searchResponse.pratilipi != null && !isEmpty( searchResponse.pratilipi ) ? searchResponse.pratilipi : { "pratilipiList": {}, "pratilipiCursor": null, "pratilipiNumberFound": 0 };
				var pratilipiList = pratilipi.pratilipiList;
				self.updatePratilipiList( pratilipiList );
				pratilipiCursor = pratilipi.pratilipiCursor;
				pratilipiNumberFound = pratilipi.numberFound;
				self.hasMorePratilipiContents( pratilipiNumberFound != 0 && self.pratilipiList().length < pratilipiNumberFound );
				self.isLoadingPratilipi( false );
		});
	};

	this.fetchSubsequentAuthorList = function() {
		self.isLoadingAuthor( true );
		ga_CA( "Author", "LoadMore" );
		dataAccessor.getAuthorSearchResults( appViewModel.searchQuery(), authorCursor, authorResultCount,
			function( searchResponse ) {
				if( searchResponse == null ) {
					self.isLoadingAuthor( false );
					return;
				}
				var author = searchResponse.author != null && !isEmpty( searchResponse.author ) ? searchResponse.author : { "authorList": {}, "authorCursor": null, "authorNumberFound": 0 };
				var authorList = author.authorList;
				self.updateAuthorList( authorList );
				authorCursor = author.authorCursor;
				authorNumberFound = author.numberFound;
				self.hasMoreAuthorContents( authorNumberFound != 0 && self.authorList().length < authorNumberFound );
				self.isLoadingAuthor( false );
		});
	};

	this.exitSearch = function() {
		if( appViewModel.searchQuery() != null )
			appViewModel.searchQuery( null );
		else
			redirect( getUrlParameter( "retUrl" ) != null ? decodeURIComponent( getUrlParameter( "retUrl" ) ) : "/" );
	};

	this.placeholderText = ko.computed( function() {
		var windowsize = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
		return windowsize > 768 ? "${ _strings.search_bar_help }" : "${ _strings.search }";
	}, this );

	this.pageScrollObserver = ko.computed( function() {
		if( ( appViewModel.scrollTop() / $( ".js-pratilipi-list-grid" ).height() ) > 0.6 ) {
			setTimeout( function() {
				self.fetchSubsequentPratilipiList();
			}, 100 ); /* locationObserver and pageScrollObserver will be triggered at once. */
		}
	}, this );

	this.locationObserver = ko.computed( function() {
		if( appViewModel.currentView() != LOCATION.SEARCH_V2.ko_element ) {
			/* Dispose all observers */
			self.pageScrollObserver.dispose();
			self.placeholderText.dispose();
			self.searchQueryObserver.dispose();
			self.locationObserver.dispose();
			return;
		}
	}, this );

}