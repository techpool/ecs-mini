function( params ) { 
	var self = this;
	var dataAccessor = new DataAccessor();

	this.pratilipi = params.pratilipi;
	this.hideLibraryButton = params.hideLibraryButton != null ? params.hideLibraryButton : false;
	this.canRemoveFromLibrary = params.canRemoveFromLibrary != null ? params.canRemoveFromLibrary : true; /* Default set to true */
	this.redirectToReaderOnClick = params.redirectToReaderOnClick != null ? params.redirectToReaderOnClick : false;
	this.showCategoryButton = params.showCategoryButton != null ? params.showCategoryButton : false; /* Default set to false */

	this.libraryButtonVisible = ko.observable( false );
	this.shareButtonVisible = ko.observable( false );
	this.libraryRequestOnFlight = ko.observable( false );

	this.updatePratilipi = function( pratilipi ) {
		ko.mapping.fromJS( pratilipi, {}, self.pratilipi );
	};

	this.titleDisplay = ko.computed( function() {
		return self.pratilipi.displayTitle ? self.pratilipi.displayTitle() : self.pratilipi.title();
	}, this );

	this.nameDisplay = ko.computed( function() {
		return self.pratilipi.author.displayName ? self.pratilipi.author.displayName() : self.pratilipi.author.name();
	}, this );

	this.switchLibraryState = function() {

		if( ! self.canRemoveFromLibrary && self.pratilipi.addedToLib() ) 
			return;

		self.libraryRequestOnFlight( true );

		var getAddedToLibMessage = function( addedToLib ) {
			var text = addedToLib ? "${ _strings.library_add_success }" : "${ _strings.library_remove_success }";
			var title = self.titleDisplay().substring( 0, 10 ) + ( self.titleDisplay().length > 10 ? "..." : "" );
			return text.replace( "$pratilipiTitle", title );
		};

		var addedToLib = ! self.pratilipi.addedToLib();
		if( addedToLib ) /* Toast only for remove action */
			ToastUtil.toast( getAddedToLibMessage( addedToLib ), 3000 );
		else 
			ToastUtil.toastCallBack( getAddedToLibMessage( addedToLib ), 4000, "${ _strings.undo_action }", self.switchLibraryState );

		self.updatePratilipi( { "addedToLib": addedToLib } );
		dataAccessor.addOrRemoveFromLibrary( self.pratilipi.pratilipiId(), addedToLib,
			function( userPratilipi ) {
				self.libraryRequestOnFlight( false );
			}, function( error ) {
				self.libraryRequestOnFlight( false );
				self.updatePratilipi( { "addedToLib": ! addedToLib } );
				var message = "${ _strings.server_error_message }";
				if( error[ "message" ] != null )
					message = error[ "message" ];
				ToastUtil.toast( message, 3000, true );
		});
	};

	this.addToOrRemoveFromLibrary = function( vm, evt ) {
		evt.stopPropagation();
		if( appViewModel.user.isGuest() ) {
			goToLoginPage( null, { "message": "LIBRARY" } );
			return;
		}
		self.switchLibraryState();
	};

	this.sharePratilipi = function( vm, evt ) {
		evt.stopPropagation();
		ShareUtil.sharePratilipi( ko.mapping.toJS( self.pratilipi ) );
	};

	this.userObserver = ko.computed( function() {
		self.libraryButtonVisible( appViewModel.user.author.authorId() != self.pratilipi.author.authorId() );
	}, this );

	this.pratilipiMetaObserver = ko.computed( function() {
		self.shareButtonVisible( self.pratilipi.state == null || self.pratilipi.state() == "PUBLISHED" );
	}, this );
	
	this.showCategoryModal = function() {
		$( '#addCategory-'+self.pratilipi.pratilipiId() ).modal();
	};
	
	setTimeout( function() { if( typeof( componentHandler ) !== 'undefined' ) componentHandler.upgradeDom(); }, 0 );

}
