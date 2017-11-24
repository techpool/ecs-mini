function() {
	var self = this;

	var defaultAuthor = {
			"authorId": null,
			"user": { "userId": null, "followCount": 0 },
			"firstName": null,
			"firstNameEn": null,
			"lastName": null,
			"lastNameEn": null,
			"name": null,
			"nameEn": null,
			"fullName": null,
			"fullNameEn": null,
			"gender": null,
			"dateOfBirth": null,
			"language": null,
			"location": null,
			"summary": null,
			"pageUrl": null,
			"imageUrl": null,
			"hasCoverImage": false,
			"coverImageUrl": null,
			"hasProfileImage": false,
			"profileImageUrl": null,
			"registrationDateMillis": null,
			"followCount": 0,
			"contentDrafted": 0,
			"contentPublished": 0,
			"totalReadCount": 0,
			"totalFbLikeShareCount": 0,
			"following": false,
			"hasAccessToUpdate": false
	};

	var defaultUserAuthor = {
			"authorId": null,
			"following": false
	};

	this.author = ko.mapping.fromJS( defaultAuthor, {}, self.author );
	this.userAuthor = ko.mapping.fromJS( defaultUserAuthor, {}, self.userAuthor );

	this.updateAuthor = function( author, initialiseObject ) {
		var updatedAuthor = JSON.parse( JSON.stringify( initialiseObject ? defaultAuthor : author ) );
		if( initialiseObject && author != null ) {
			if( author.summary != null && author.summary.trim() == "" )
				author.summary = null;
			for( var key in author )
				if( author.hasOwnProperty( key ) )
					updatedAuthor[ key ] = author[ key ];
			if( author.user != null ) {
				for( var key in author.user )
					if( author.user.hasOwnProperty( key ) )
						updatedAuthor.user[ key ] = author.user[ key ];
			}
		}
		ko.mapping.fromJS( updatedAuthor, {}, self.author );
		MetaTagUtil.setMetaTagsForAuthor( ko.mapping.toJS( self.author ) );
		appViewModel.isUserPage( self.author.authorId() == appViewModel.user.author.authorId() );
	};


	this.updateUserAuthor = function( userAuthor, initialiseObject ) {
		var updatedUserAuthor = JSON.parse( JSON.stringify( initialiseObject ? defaultUserAuthor : userAuthor ) );
		if( initialiseObject && userAuthor != null ) {
			for( var key in userAuthor )
				if( userAuthor.hasOwnProperty( key ) )
					updatedUserAuthor[ key ] = userAuthor[ key ];
		}
		ko.mapping.fromJS( updatedUserAuthor, {}, self.userAuthor );
	};

	this.initialDataLoaded = ko.observable( false );
	this.fetchAuthorAndUserAuthor = function() {
		var dataAccessor = new DataAccessor();
		dataAccessor.getAuthorByUri( window.location.pathname, true, 
				function( author, userAuthor ) {
			if( author == null ) { /* Invalid Uri */
				appViewModel.currentView( LOCATION.PAGE_NOT_FOUND[ "ko_element" ] );
				return;
			}
			self.updateAuthor( author, true );
			self.updateUserAuthor( userAuthor, true );
			if( ! self.initialDataLoaded() ) self.initialDataLoaded( true );
		});
	};

	/* Landing from Search page */
	var searchQuery = getUrlParameter( "searchQuery" ) != null ? decodeURIComponent( getUrlParameter( "searchQuery" ) ) : null;
	this.locationObserver = ko.computed( function() {
		if( appViewModel.currentView() != LOCATION.AUTHOR.ko_element ) {
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
			self.fetchAuthorAndUserAuthor();
			$( ".modal" ).modal( "hide" ); /* Follows Modal */
		}, 0 );
	}, this );


}
