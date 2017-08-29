function() {
	var self = this;
	var dataAccessor = new DataAccessor();

	var defaultEvent = {
			"eventId": null,
			"name": null,
			"nameEn": null,
			"language": null,
			"description": null,
			"pageUrl": null,
			"bannerImageUrl": null,
			"hasAccessToUpdate": false
	};

	this.event = ko.mapping.fromJS( defaultEvent, {}, self.event );

	this.updateEvent = function( event ) {
		if( event == null )
			event = defaultEvent;
		ko.mapping.fromJS( event, {}, self.event );
		MetaTagUtil.setMetaTagsForEvent( ko.mapping.toJS( self.event ) );
	};

	this.initialDataLoaded = ko.observable( false );

	this.fetchEvent = function() {
		dataAccessor.getEventByUri( window.location.pathname,
				function( event ) {
			if( event == null ) { /* Invalid Uri */
				appViewModel.currentView( LOCATION.PAGE_NOT_FOUND[ "ko_element" ] );
				return;
			}
			self.updateEvent( event );
			if( ! self.initialDataLoaded() ) self.initialDataLoaded( true );
		});
	};


	/* Event Entries -> Pratilipi List */
	var cursor = null;
	var resultCount = 20;

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
		dataAccessor.getPratilipiListByEventId( self.event.eventId(), cursor, null, resultCount,
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

	this.pageScrollObserver = ko.computed( function() {
		if( ( appViewModel.scrollTop() / $( ".js-pratilipi-list-grid" ).height() ) > 0.6 ) {
			setTimeout( function() {
				if( self.event.eventId() == null ) return;
				self.fetchPratilipiList();
			}, 100 ); /* locationObserver and pageScrollObserver will be triggered at once. */
		}
	}, this );

	this.eventIdListener = ko.computed( function() {
		if( self.event.eventId() == null ) return;
		setTimeout( function() {
			self.fetchPratilipiList();
		}, 0);
	}, this );

	this.locationObserver = ko.computed( function() {
        if( appViewModel.currentView() != LOCATION.EVENT.ko_element ) {
            /* Dispose all observers */
            self.locationObserver.dispose();
            self.pageScrollObserver.dispose();
            self.eventIdListener.dispose();
            return;
        }
        appViewModel.currentLocation();
        setTimeout( function() {
            self.pratilipiList( [] );
            self.hasMoreContents( true );
            self.isLoading( false );
            self.initialDataLoaded( false );
            self.fetchEvent();
        }, 0 );
    }, this );

}
