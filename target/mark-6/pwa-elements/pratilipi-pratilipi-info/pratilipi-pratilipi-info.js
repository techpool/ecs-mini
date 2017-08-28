function( params ) { 
	var self = this;
	var dataAccessor = new DataAccessor();

	/* Setting Local Variables */
	this.pratilipi = params.pratilipi;
	this.userPratilipi = params.userPratilipi;
	this.updatePratilipi = params.updatePratilipi;
	this.updateUserPratilipi = params.updateUserPratilipi;

	var defaultAuthor = { 
		"authorId": null, 
		"name": null, 
		"nameEn": null, 
		"pageUrl": null, 
		"profileImageUrl": null,
		"summary": null
	};

	var defaultUserAuthor = {
		"following": false 
	};

	this.author = ko.mapping.fromJS( defaultAuthor, {}, self.author );
	this.userAuthor = ko.mapping.fromJS( defaultUserAuthor, {}, self.userAuthor );
	this.authorLoaded = ko.observable( false );

	this.canAddToLibrary = ko.observable( false );
	this.canShare = ko.observable( false );
	this.canDelete = ko.observable( false );

	this.pratilipiRequestOnFlight = ko.observable( false );
	this.userPratilipiRequestOnFlight = ko.observable( false );
	this.userAuthorRequestOnFlight = ko.observable( false );

	this.authorIsUser = ko.observable( false );

	/* Summary Input */
	this.summaryInputValue = ko.observable();
	this.summaryInputActive = ko.observable( false );
	this.summaryInputRequestOnFlight = ko.observable( false );
	this.openSummaryInput = function() {
		self.summaryInputValue( self.pratilipi.summary() );
		self.summaryInputActive( true );
	};
	this.closeSummaryInput = function() {
		self.summaryInputActive( false );
	};
	this.toggleSummaryInput = function() {
		if( self.summaryInputActive() )
			self.closeSummaryInput();
		else
			self.openSummaryInput();
	};
	this.submitSummaryInput = function() {
		ToastUtil.toastUp( "${ _strings.working }" );
		var pratilipi = { 
				"pratilipiId": self.pratilipi.pratilipiId(),
				"summary": self.summaryInputValue()
		};
		self.summaryInputRequestOnFlight( true );
		var dataAccessor = new DataAccessor();
		dataAccessor.createOrUpdatePratilipi( pratilipi, 
			function( pratilipi ) {
				self.updatePratilipi( pratilipi );
				self.summaryInputRequestOnFlight( false );
				ToastUtil.toast( "${ _strings.updated_pratilipi_info_success }", 3000 );
				self.closeSummaryInput();
			}, function( error ) {
				self.summaryInputRequestOnFlight( false );
				var message = "${ _strings.server_error_message }";
				if( error[ "message" ] != null )
					message = error[ "message" ];
				ToastUtil.toast( message, 3000 );
		} );
	};


	/* Author */
	this.updateAuthor = function( author ) {
		ko.mapping.fromJS( author, {}, self.author );
	};

	this.updateUserAuthor = function( userAuthor ) {
		ko.mapping.fromJS( userAuthor, {}, self.userAuthor );
	};

	this.fetchAuthorAndUserAuthor = function() {
		self.authorLoaded( false );
		dataAccessor.getAuthorById( self.pratilipi.author.authorId() , true, 
			function( author, userAuthor ) {
				self.updateAuthor( author );
				self.updateUserAuthor( userAuthor );
				self.authorLoaded( true );
		});
	};


	/* Add to Library */
	this.addToLibrary = function() {

		if( appViewModel.user.isGuest() ) {
			goToLoginPage( null, { "message": "LIBRARY" } );
			return;
		}

		var getAddedToLibMessage = function( addedToLib ) {
			var text = addedToLib ? "${ _strings.library_add_success }" : "${ _strings.library_remove_success }";
			var title = self.pratilipi.title().substring( 0, 10 ) + ( self.pratilipi.title().length > 10 ? "..." : "" );
			return text.replace( "$pratilipiTitle", title );
		};

		var addedToLib = ! self.userPratilipi.addedToLib();

		if( addedToLib ) /* Toast only for remove action */
			ToastUtil.toast( getAddedToLibMessage( addedToLib ), 3000 );
		else
			ToastUtil.toastCallBack( getAddedToLibMessage( addedToLib ), 4000, "${ _strings.undo_action }", self.addToLibrary );

		self.updateUserPratilipi( { "addedToLib": addedToLib } );
		self.userPratilipiRequestOnFlight( true );

		dataAccessor.addOrRemoveFromLibrary( self.pratilipi.pratilipiId(), addedToLib,
			function( userPratilipi ) {
				self.userPratilipiRequestOnFlight( false );
			}, function( error ) {
				self.userPratilipiRequestOnFlight( false );
				ToastUtil.toast( error[ "message" ] != null ? error[ "message" ] : "${ _strings.server_error_message }" );
				self.updateUserPratilipi( { "addedToLib": ! addedToLib } );
		});

	};


	/* Follow Author */
	this.followAuthor = function() {

		if( appViewModel.user.isGuest() ) {
			goToLoginPage( null, { "message": "FOLLOW" } );
			return;
		}

		var getFollowingMessage = function( following ) {
			var text = following ? "${ _strings.author_follow_success }" : "${ _strings.author_unfollow_success }";
			return text.replace( "$authorName", self.pratilipi.author.name() );
		};

		var following = ! self.userAuthor.following();
		if( following ) /* Toast only for unfollow action */
			ToastUtil.toast( getFollowingMessage( following ), 3000 );
		else
			ToastUtil.toastCallBack( getFollowingMessage( following ), 4000, "${ _strings.undo_action }", self.followAuthor );

		self.updateUserAuthor( { "following": following } );
		self.userAuthorRequestOnFlight( true );

		dataAccessor.followOrUnfollowAuthor( self.author.authorId(), following,
			function( userAuthor ) {
				self.userAuthorRequestOnFlight( false );
				ga_CA( 'Author', userAuthor.following ? 'Follow' : 'UnFollow' );
			}, function( error ) {
				self.userAuthorRequestOnFlight( false );
				ToastUtil.toast( error[ "message" ] != null ? error[ "message" ] : "${ _strings.server_error_message }" );
				self.updateUserAuthor( { "following": ! following } );
		});

	};


	/* Publish or Unpublish */
	this.togglePratilipiState = function() {

		var currentState = self.pratilipi.state();
		var newState = self.pratilipi.state() != "PUBLISHED" ? "PUBLISHED" : "DRAFTED";

		var getPublishedMessage = function( newState ) {
			var text = newState == "PUBLISHED" ? "${ _strings.pratilipi_publish_success }" : "${ _strings.pratilipi_unpublish_success }";
			var title = self.pratilipi.title().substring( 0, 10 ) + ( self.pratilipi.title().length > 10 ? "..." : "" );
			return text.replace( "$pratilipiTitle", title );
		};

		if( newState == "DRAFTED" ) /* Toast only for Add to Drafts action */
			ToastUtil.toastCallBack( getPublishedMessage( newState ), 4000, "${ _strings.undo_action }", self.togglePratilipiState );
		else
			ToastUtil.toast( getPublishedMessage( newState ), 3000 );

		self.updatePratilipi( { "state": newState } );
		self.pratilipiRequestOnFlight( true );

		dataAccessor.createOrUpdatePratilipi( { "pratilipiId": self.pratilipi.pratilipiId(), "state": newState },
			function( pratilipi ) {
				self.pratilipiRequestOnFlight( false );
			}, function( error ) {
				self.pratilipiRequestOnFlight( false );
				self.updatePratilipi( { "state": currentState } );
				ToastUtil.toast( error.message, 3000 );
		});

	};


	/* Share */
	this.share = function() {
	    var utmParameter = "utm_source=pratilipi_website&utm_medium=main_share_popup&utm_campaign=content_share";
		ShareUtil.sharePratilipi( ko.mapping.toJS( self.pratilipi ), utmParameter );
	};


	/* Image Upload */
	this.imageUploaded = ko.observable( false );
	this.chooseImageFile = function() {
		document.getElementById( 'uploadPratilipiImageInput' ).value= null;
		document.getElementById( "uploadPratilipiImageInput" ).click();
	};

	this.uploadPratilipiImage = function( vm, evt ) {
		var files = evt.target.files;
		var file = files[0];
		self.imageUploaded( true );
		document.getElementById( "uploadPratilipiImageForm" ).submit();
	};

	this.iframeLoaded = function( vm, evt ) {
		if( ! self.imageUploaded() )
			return;
		var response = JSON.parse( evt.currentTarget.contentDocument.body.innerText );
		if( response[ "coverImageUrl" ] != null ) {
			/* Success Callback */
			var coverImageUrl = response[ "coverImageUrl" ];
			self.updatePratilipi( { "coverImageUrl": coverImageUrl } );
			ToastUtil.toast( "${ _strings.success_generic_message }", 3000 );
		} else if( response[ "message" ] != null ) {
			ToastUtil.toast( response[ "message" ], 3000 );
		}
		self.imageUploaded( false );
	};


	/* Delete Pratilipi */
	this.openDeletePratilipiDialog = function() {
		$( "#pratilipi-info-delete-confirmation-dialog" ).modal();
	};

	this.deletePratilipi = function() {
		var currentState = self.pratilipi.state();
		var pratilipi = { "pratilipiId": self.pratilipi.pratilipiId(), "state": "DELETED" };
		self.pratilipiRequestOnFlight( true );
		ToastUtil.toastUp( "${ _strings.working }" );
		dataAccessor.createOrUpdatePratilipi( pratilipi,
			function( pratilipi ) {
				ToastUtil.toast( "${ _strings.pratilipi_deleted_successfully }", 3000 );
				window.location.href = self.author.pageUrl();
			}, function( error ) {
				self.pratilipiRequestOnFlight( false );
				self.updatePratilipi( { "state": currentState } );
				ToastUtil.toast( error.message, 3000 );
		});
	};

	/* Computed observables - separate observers for ID and meta - performance optimization */
	this.authorIdObserver = ko.computed( function() {
		/* Check for the exact data. 
		 * ko mapping updates properties in a different fashion
		 * First it updates only the 'pratilipi' object
		 * Then it updates the 'author' object inside 'pratilipi' object 
		 * As a result, the function will be called 2 times if we check for pratilipiId */
		if( self.pratilipi.author.authorId() == null )
			return;
		self.fetchAuthorAndUserAuthor();
	}, this );


	this.pratilipiMetaObserver = ko.computed( function() {
		self.canAddToLibrary( self.pratilipi.state() == "PUBLISHED" 
			&& ( appViewModel.user.isGuest() || self.pratilipi.author.authorId() != appViewModel.user.author.authorId() ) );
		self.canShare( self.pratilipi.state() == "PUBLISHED" );
		self.canDelete( self.pratilipi.state() == "DRAFTED" && self.pratilipi.hasAccessToUpdate() );
		self.authorIsUser( self.pratilipi.author.authorId() == appViewModel.user.author.authorId() );
	}, this );


}
