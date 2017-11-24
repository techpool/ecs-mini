function() {
	var self = this;

	var defaultPratilipi = { 
			"pratilipiId": null, 
			"title": null, 
			"titleEn": null, 
			"language": null, 
			"author": { "authorId": null,"name": null, "pageUrl": null }, 
			"summary": null, 
			"pageUrl": null, 
			"coverImageUrl": null,
			"readPageUrl": null,
			"writePageUrl": null,
			"type": null,
			"state": null,
			"listingDateMillis": null,
			"wordCount": null,
			"reviewCount": null,
			"ratingCount": null,
			"averageRating": null, 
			"readCount": null, 
			"fbLikeShareCount": null,
			"hasAccessToUpdate": false,
			"tags": null,
			"suggestedTags": null
	};

	var defaultUserPratilipi = {
			"userPratilipiId": null,
			"userId": null,
			"pratilipiId": null,
			"userName": null,
			"userImageUrl": null,
			"userProfilePageUrl": null,
			"rating": 0,
			"review": null,
			"reviewState": null,
			"addedToLib": false, 
			"hasAccessToReview": true, 
			"isLiked": false,
	};

	this.pratilipi = ko.mapping.fromJS( defaultPratilipi, {}, self.pratilipi );
	this.userPratilipi = ko.mapping.fromJS( defaultUserPratilipi, {}, self.userPratilipi );

	/*  START
	    Developer : RAHUL RANJAN
	    Function : Show share pop up when pratilipi.title() != null
    */
    this.pratilipiTitle = ko.observable();

	ko.computed( function() {
	    if (this.pratilipi.title() == null)
	        return;

	    this.pratilipiTitle(this.pratilipi.title() + "  ");

	    /* Show popup for hindi and self published contents only */

	    var url = document.URL;
        if (sessionStorage.publishType
            && sessionStorage.publishType == "self"
            && appViewModel.language == "HINDI") {
            $("#shareModal").modal("show");
            $("#shareModal").on('shown.bs.modal', function() {
                self.FBEvent("LANDED_SHARE_CONTENT", null);
                sessionStorage.publishType = null;
            });
        }
	}, this);

	/* END */

	this.updatePratilipi = function( pratilipi, initialiseObject ) {
        var updatedPratilipi = JSON.parse( JSON.stringify( initialiseObject ? defaultPratilipi : pratilipi ) );
        if( initialiseObject && pratilipi != null ) {
            if( pratilipi.summary != null && pratilipi.summary.trim() == "" )
                pratilipi.summary = null;
            for( var key in pratilipi )
                if( pratilipi.hasOwnProperty( key ) )
                    updatedPratilipi[ key ] = pratilipi[ key ];
            if( pratilipi.author != null ) {
                for( var key in pratilipi.author )
                    if( pratilipi.author.hasOwnProperty( key ) )
                        updatedPratilipi.author[ key ] = pratilipi.author[ key ];
            }
        }
        ko.mapping.fromJS( updatedPratilipi, {}, self.pratilipi );
        MetaTagUtil.setMetaTagsForPratilipi( ko.mapping.toJS( self.pratilipi ) );
    };

    this.updateUserPratilipi = function( userPratilipi, initialiseObject ) {
		var updatedUserPratilipi = JSON.parse( JSON.stringify( initialiseObject ? defaultUserPratilipi : userPratilipi ) );
		if( initialiseObject && userPratilipi != null ) {
			if( userPratilipi.rating == null )
				userPratilipi.rating = 0;
			if( userPratilipi.review != null && userPratilipi.review.trim() == "" )
				userPratilipi.review = null;
			if( userPratilipi.reviewState != null && userPratilipi.reviewState != "PUBLISHED" )
				userPratilipi.review = null;
			for( var key in userPratilipi )
				if( userPratilipi.hasOwnProperty( key ) )
					updatedUserPratilipi[ key ] = userPratilipi[ key ];
		}
		ko.mapping.fromJS( updatedUserPratilipi, {}, self.userPratilipi );
	};

	this.initialDataLoaded = ko.observable( false );
	this.fetchPratilipiAndUserPratilipi = function() {
		var dataAccessor = new DataAccessor();
		dataAccessor.getPratilipiByUri( window.location.pathname, true, 
				function( pratilipi, userPratilipi ) {
			if( pratilipi == null ) { /* Invalid Uri */
				appViewModel.currentView( LOCATION.PAGE_NOT_FOUND[ "ko_element" ] );
				return;
			}
			self.updatePratilipi( pratilipi, true );
			self.updateUserPratilipi( userPratilipi, true );
			if( ! self.initialDataLoaded() ) self.initialDataLoaded( true );
		});
	};

    /*  START
        Developer : Rahul Ranjan
        Function : click event of FB Share button shown after publishing content.
     */
    this.fbShare = function() {
        var shareUrl = window.location.origin + this.pratilipi.pageUrl();
        var utmParameter = "?utm_source=pratilipi_website&utm_medium=post_publish_popup&utm_campaign=content_share";
        $("#shareModal").modal("hide");
        window.open( "http://www.facebook.com/sharer.php?u=" + shareUrl + utmParameter, "share", "width=1100,height=500,left=70px,top=60px" );
        this.FBEvent('SHARE_CONTENT', 'FB');
    };

    this.FBEvent = function(event_name, entity_value) {
        var params = {};
        params['ENVIRONMENT'] = 'GROWTH';
        params['CONTENT_LANGUAGE'] = this.pratilipi.language();
        params['SCREEN_NAME'] = 'PRATILIPI_PAGE';
        params['LOCATION'] = 'POST_PUBLISH_POPUP';
        params['USERID'] = appViewModel.user.userId();
        params['PRATILIPI_TYPE'] =  this.pratilipi.type();
        params['CONTENT_ID'] =  this.pratilipi.pratilipiId();
        params['AUTHOR_ID'] =  this.pratilipi.author.authorId();
        params['ACCESS_LEVEL'] = appViewModel.user.author.authorId() == this.pratilipi.author.authorId() ? 'SELF' : 'OTHER';
        if (entity_value != null)
            params['ENTITY_VALUE'] =  entity_value;

        FB.AppEvents.logEvent(event_name, null, params);
    };

    /* END */

	/* Landing from Search page */
	var searchQuery = getUrlParameter( "searchQuery" ) != null ? decodeURIComponent( getUrlParameter( "searchQuery" ) ) : null;
	this.locationObserver = ko.computed( function() {
		if( appViewModel.currentView() != LOCATION.PRATILIPI.ko_element ) {
			if( appViewModel.currentView() == LOCATION.SEARCH_V2.ko_element && searchQuery != null ) {
				setTimeout( function() {
					appViewModel.searchQuery( searchQuery );
				}, 50 );
			}
			/* Dispose all observers */
			self.locationObserver.dispose();
			return;
		}
		appViewModel.currentLocation();
		setTimeout( function() {
			self.initialDataLoaded( false );
			self.fetchPratilipiAndUserPratilipi();
			/* Hack remove delete modal forcibly */
            $( '.modal' ).modal( 'hide' );
            $( 'body' ).removeClass( 'modal-open' );
            $( '.modal-backdrop' ).remove();
		}, 0 );
	}, this );

}
