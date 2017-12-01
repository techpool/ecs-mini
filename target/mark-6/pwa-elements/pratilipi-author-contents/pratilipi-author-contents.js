function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.author = params.author;

	this.publishedPratilipiList = ko.observableArray();
	this.draftedPratilipiList = ko.observableArray();

	this.updatePublishedPratilipiList = function( pratilipiList ) {
		for( var i = 0; i < pratilipiList.length; i++ ) {
			var pratilipi = pratilipiList[i];
			pratilipi[ "author" ] = { "authorId": self.author.authorId(), "name": "" };
			if (!pratilipi.hasOwnProperty('tags')) 
				pratilipi["tags"] = null;
			if (!pratilipi.hasOwnProperty('suggestedTags'))
				pratilipi["suggestedTags"] = null;
				
			self.publishedPratilipiList.push( ko.mapping.fromJS( pratilipi ) );
		}
	};

	this.updateDraftedPratilipiList = function( pratilipiList ) {
		for( var i = 0; i < pratilipiList.length; i++ ) {
			var pratilipi = pratilipiList[i];
			if( pratilipi[ "title" ] == null ) pratilipi[ "title" ] = " ";
			if( pratilipi[ "pageUrl" ] == null ) continue; /* Hack: Fix for Deleted entitites from backend */
			pratilipi[ "author" ] = { "authorId": self.author.authorId(), "name": "" };
			self.draftedPratilipiList.push( ko.mapping.fromJS( pratilipi ) );
		}
	};


	/* Load Drafted and Published Contents */
	var publishedCursor = null;
	var draftedCursor = null;

	/* Loading state */
	/*
	 * 4 Possible values
	 * LOADING
	 * LOADED_EMPTY
	 * LOADED
	 * FAILED
	 * 
	 * */
	this.loadingStatePublished = ko.observable();
	this.loadingStateDrafted = ko.observable();

	this.hasMorePublishedContents = ko.observable( true );
	this.hasMoreDraftedContents = ko.observable( true );


	this._loadPublishedContents = function( resultCount ) {
		if( self.loadingStatePublished() == "LOADING" ) return;
		self.loadingStatePublished( "LOADING" );
		dataAccessor.getPratilipiListByAuthor( self.author.authorId(), "PUBLISHED", publishedCursor, null, resultCount,
				function( pratilipiListResponse ) {
					if( pratilipiListResponse == null ) {
						self.loadingStatePublished( "FAILED" );
						return;
					}
					var pratilipiList = pratilipiListResponse.pratilipiList;
					self.updatePublishedPratilipiList( pratilipiList );
					publishedCursor = pratilipiListResponse.cursor;
					self.loadingStatePublished( self.publishedPratilipiList().length > 0 ? "LOADED" : "LOADED_EMPTY" );
					self.hasMorePublishedContents( pratilipiList.length == resultCount && publishedCursor != null );
		});
	};

	this._loadDraftedContents = function( resultCount ) {
		if( self.loadingStateDrafted() == "LOADING" ) return;
		self.loadingStateDrafted( "LOADING" );
		dataAccessor.getPratilipiListByAuthor( self.author.authorId(), "DRAFTED", draftedCursor, null, resultCount,
				function( pratilipiListResponse ) {
					if( pratilipiListResponse == null ) {
						self.loadingStateDrafted( "FAILED" );
						return;
					}
					var pratilipiList = pratilipiListResponse.pratilipiList;
					self.updateDraftedPratilipiList( pratilipiList );
					draftedCursor = pratilipiListResponse.cursor;
					self.loadingStateDrafted( self.draftedPratilipiList().length > 0 ? "LOADED" : "LOADED_EMPTY" );
					self.hasMoreDraftedContents( pratilipiList.length == resultCount && draftedCursor != null );
		});
	};


	/* Constants */
	var initialPublishedResultCount = 6;
	var subsequentPublishedResultCount = 6;
	var initialDraftedResultCount = 5;
	var subsequentDraftedResultCount = 6;

	this.loadInitialPublishedContents = function() {
		self.publishedPratilipiList( [] );
		publishedCursor = null;
		self.loadingStatePublished( null );
		self.hasMorePublishedContents( true );
		self._loadPublishedContents( initialPublishedResultCount );
	};

	this.loadMorePublishedContents =  function() {
		self._loadPublishedContents( subsequentPublishedResultCount );
	};

	this.loadInitialDraftedContents = function() {
		self.draftedPratilipiList( [] );
        draftedCursor = null;
        self.loadingStateDrafted( null );
        self.hasMoreDraftedContents( true );
		self._loadDraftedContents( initialDraftedResultCount );
	};

	this.loadMoreDraftedContents =  function() {
		self._loadDraftedContents( subsequentDraftedResultCount );
	};


	/* Create new Draft */
	this.createNewPratilipi = function() {
		if( isMobile() ) {
			ToastUtil.toast( "${ _strings.write_on_desktop_only }", 5000 );
			return;
		}
		appViewModel.pratilipiWriteAuthorId( self.author.authorId() );
		$( "#pratilipiWrite" ).modal();
	};

	/* Computed Observables */
	this.authorIdObserver = ko.computed( function() {
		if( self.author.authorId() == null ) return;
		setTimeout( function() {
			if( self.author.hasAccessToUpdate() ) {
				self.loadInitialDraftedContents();
			}
			self.loadInitialPublishedContents();
		}, 0 );
	}, this );

}
