function( params ) { 
	var self = this;
	var dataAccessor = new DataAccessor();

	/* Setting Local Variables */
	this.author = params.author;
	this.userAuthor = params.userAuthor;
	this.updateAuthor = params.updateAuthor;
	this.updateUserAuthor = params.updateUserAuthor;

	this.authorDisplayName = ko.computed( function() {
		return self.author.fullName() != null ? self.author.fullName() : self.author.fullNameEn();
	}, this );

	/* Follow/Unfollow Author */
	this.followRequestOnFlight = ko.observable( false );
	this.followOrUnfollowAuthor = function() {
		if( appViewModel.user.isGuest() ) {
			goToLoginPage( null, { "message": "FOLLOW" } );
			return;
		}
		if( self.followRequestOnFlight() ) 
			return;
		self.followRequestOnFlight( true );

		ToastUtil.toast( "${ _strings.success_generic_message }" );
		var switchState = function() {
			self.updateAuthor( { "followCount": self.userAuthor.following() ? self.author.followCount() - 1 : self.author.followCount() + 1 } );
			self.updateUserAuthor( { "following": ! self.userAuthor.following() } );
		};

		switchState();
		dataAccessor.followOrUnfollowAuthor( self.author.authorId(), self.userAuthor.following(), 
			function( userAuthor ) {
				self.followRequestOnFlight( false );
				ga_CA( 'Author', userAuthor.following ? 'Follow' : 'UnFollow' );
			}, function( error ) {
				self.followRequestOnFlight( false );
				switchState();
				ToastUtil.toast( error[ "message" ] != null ? error[ "message" ] : "${ _strings.server_error_message }", 3000, true );
		});
	};

	/* Can Follow and Settings Link */
	this.canFollow = ko.observable( false );
	this.settingsLink = ko.observable( "/settings" );

	this.authorObserver = ko.computed( function() {
	    if( self.author.authorId() == null && self.author.user.userId() == null ) return;
	    /* self.author.user.userId() => null for fictional Authors */
    	self.canFollow( appViewModel.user.userId() != self.author.user.userId() );
		if( self.author.hasAccessToUpdate() && appViewModel.user.userId() != self.author.user.userId() ) /* AEEs */
			self.settingsLink( "/settings?authorId=" + self.author.authorId() + "&userId=" + self.author.user.userId() );
		else
			self.settingsLink( "/settings" );
    }, this );


	/* Share Author */
	this.shareAuthor = function() {
		ShareUtil.shareAuthor( ko.mapping.toJS( self.author ) );
		ga_CA( appViewModel.isUserPage() ? 'User' : 'Author', 'Share' );
	};


	/* Image Upload */
    this.imageUploaded = ko.observable( false );
    this.chooseImageFile = function() {
        document.getElementById( 'uploadAuthorImageInput' ).value= null;
        document.getElementById( "uploadAuthorImageInput" ).click();
    };

    this.uploadAuthorImage = function( vm, evt ) {
        var files = evt.target.files;
        var file = files[0];
        self.imageUploaded( true );
        document.getElementById( "uploadAuthorImageForm" ).submit();
    };

    this.iframeLoaded = function( vm, evt ) {
        if( ! self.imageUploaded() )
            return;
        var response = JSON.parse( evt.currentTarget.contentDocument.body.innerText );
        if( response[ "coverImageUrl" ] != null ) {
            /* Success Callback */
            self.updateAuthor( { "profileImageUrl": response[ "coverImageUrl" ] } );
            ToastUtil.toast( "${ _strings.success_generic_message }", 3000 );
        } else if( response[ "message" ] != null ) {
            ToastUtil.toast( response[ "message" ], 3000 );
        }
        self.imageUploaded( false );
    };
    
    /* Cover Upload */
    this.coverUploaded = ko.observable( false );
    this.chooseCoverFile = function() {
        document.getElementById( 'uploadAuthorCoverInput' ).value= null;
        document.getElementById( "uploadAuthorCoverInput" ).click();
    };

    this.uploadAuthorCover = function( vm, evt ) {
        var files = evt.target.files;
        var file = files[0];
        self.coverUploaded( true );
        document.getElementById( "uploadAuthorCoverForm" ).submit();
    };

    this.coverIframeLoaded = function( vm, evt ) {
        if( ! self.coverUploaded() )
            return;
        var response = JSON.parse( evt.currentTarget.contentDocument.body.innerText );
        if( response[ "coverImageUrl" ] != null ) {
            /* Success Callback */
            self.updateAuthor( { "coverImageUrl": response[ "coverImageUrl" ] } );
            ToastUtil.toast( "${ _strings.success_generic_message }", 3000 );
        } else if( response[ "message" ] != null ) {
            ToastUtil.toast( response[ "message" ], 3000 );
        }
        self.coverUploaded( false );
    };

    this.getReadCountText = ko.computed( function() {
    	return "${ _strings.author_readby_count }".replace( "$read_count", formatReadCount( self.author.totalReadCount() ) );
    }, this );

    /* UI Helpers */
    this.focusPublishedContentSection = function() {
    	var targetDiv = $( '.js-pratilipi-author-info' );
    	appViewModel.scrollToTop( $( ".js-author-published-contents" ).position().top + $( ".js-pratilipi-header" ).height() );
    };

}