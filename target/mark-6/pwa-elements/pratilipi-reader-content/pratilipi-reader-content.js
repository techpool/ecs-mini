function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.pratilipi = params.pratilipi;
	this.updatePratilipi = params.updatePratilipi;
	this.userPratilipi = params.userPratilipi;
	this.updateUserPratilipi = params.updateUserPratilipi;
	this.chapterNo = params.chapterNo;
	this.chapterCount = params.chapterCount;
	this.fontSize = params.fontSize;
	this.imageSize = params.imageSize;
	this.setImageSize = params.setImageSize;
	this.lineHeight = params.lineHeight;
	this.setChapterNo = params.setChapterNo;

	this.content = ko.observable();
	this.isLoading = ko.observable( true );
	var contentMap = {};

	this._getImageUrl = function( chapterNo ) {
		if( chapterNo == null ) return;
		return "<img style='width: " + self.imageSize() + "px' class='image-content js-image-content' src='/api/pratilipi/content/image?pratilipiId=" +
					self.pratilipi.pratilipiId() + "&" +
					"name=" + contentMap[ chapterNo ] + "' />";
	},

	this._precache = function( chapterNo ) {
		setTimeout( function() {
			if( self.pratilipi.contentType() == "PRATILIPI" ) {
				if( self.chapterNo() > 1 )
					self._loadContent( self.chapterNo() - 1 );
				if( self.chapterNo() < self.chapterCount() )
					self._loadContent( self.chapterNo() + 1 );
			} else if( self.pratilipi.contentType() == "IMAGE" ) {
				if( self.chapterNo() > 1 )
					$( self._getImageUrl( self.chapterNo() - 1 ) ).trigger( 'load' );
				if( self.chapterNo() < self.chapterCount() )
					$( self._getImageUrl( self.chapterNo() + 1 ) ).trigger( 'load' );
			}
		}, 0 );
	};

	this._loadContent = function( chapterNo ) {
		if( contentMap[ chapterNo ] !== undefined ) /* Either marked null or has content */
			return;
		contentMap[ chapterNo ] = null;
		dataAccessor.getPratilipiContent( self.pratilipi.pratilipiId(), chapterNo, self.pratilipi.oldContent(),
			function( contentResponse ) {
				if( contentResponse == null )
					contentResponse = { "content": "", "chapterNo": chapterNo };

				var content = contentResponse.content != null ? contentResponse.content.replace( /&nbsp;/g, " " ) : "";
				if( contentResponse.chapterTitle != null )
					content = "<h1 class='chapter-title'>" + contentResponse.chapterTitle + "</h1>" + content;

				var cNo = contentResponse.chapterNo;
				contentMap[ cNo ] = content;
				if( self.chapterNo() == cNo ) {
					self.content( content );
					self.isLoading( false );
					self._precache(); /* Not making calls to x+1 and x-1 chapter if chapter is not available. Giving utmost importance to the current page. */
				}
		});
	};

	this._initialiseImageContentReader = function() {
		dataAccessor.getPratilipiContent( self.pratilipi.pratilipiId(), null, true,
			function( contentResponse ) {
				var chapters = contentResponse.content.chapters;
				var c = 1;
				for( var i = 0; i < chapters.length; i++ ) {
					var pages = chapters[i].pages;
					for( var j = 0; j < pages.length; j++ )
						contentMap[ c++ ] = pages[j].pagelets[0].data.name;
				}
				/* First Load */
				self.isLoading( false );
				self.content( self._getImageUrl( self.chapterNo() ) );
				self.setImageSizeDOM();
				self._precache();
		});
	};

	this.chapterNoObserver = ko.computed( function() {
		if( self.chapterNo() == null ) return;

		setTimeout( function() {
			if( self.pratilipi.contentType() == "PRATILIPI" ) {
				if( contentMap[ self.chapterNo() ] === undefined ) {
					self.isLoading( true );
					self._loadContent( self.chapterNo() );
				} else if( contentMap[ self.chapterNo() ] == null ) {
					self.isLoading( true );
				} else if( contentMap[ self.chapterNo() ] != null ) {
					self.content( contentMap[ self.chapterNo() ] );
					self.isLoading( false );
					self._precache(); /* Since the page is already available, precaching other pages */
				}

			} else if( self.pratilipi.contentType() == "IMAGE" ) {
				if( isEmpty( contentMap ) ) {
					self.isLoading( true );
					self._initialiseImageContentReader();
				} else {
					self.content( self._getImageUrl( self.chapterNo() ) );
					self._precache();
				}
			}
		}, 0 );

	}, this );

	this.readerContentClassObserver = ko.computed( function() {
		var getLineHeightClass = function( lineHeight ) {
			if( lineHeight == 'SMALL' ) return 'lh-sm';
			if( lineHeight == 'MEDIUM' ) return 'lh-md';
			if( lineHeight == 'LARGE' ) return 'lh-lg';
		};
		var getFontClass = function( fontSize ) {
			return "content-font-" + fontSize;
		};
		return getLineHeightClass( self.lineHeight() ) + " " + getFontClass( self.fontSize() );
	}, this );

	this.setImageSizeDOM = function() {
		var contentDiv = $( ".pratilipi-reader .pratilipi-reader-content .js-content" );
		if( self.imageSize() == -1 ) { /* undetermined */
			if( contentDiv.width() == 0 ) return;
			self.setImageSize( contentDiv.width() );
		}
		jQuery( "img.js-image-content" ).css( 'width', self.imageSize() + 'px', 'important' );
	};

	this.imageSizeObserver = ko.computed( function() {
		self.imageSize();
		setTimeout( function() {
			self.setImageSizeDOM();
		}, 0 );
	}, this );



	this.goToNextChapter = function() {
		self.setChapterNo( self.chapterNo() + 1 );
	};


	/* Rating and review */
	this.ratingInput = ko.observable(0);
	this.reviewInput = ko.observable();
	this.addOrDeleteReviewRequestOnFlight = ko.observable( false );

	var reviewInputDialog = $( '#pratilipi-reader-review-input-dialog' );
	this.openReviewModal = function() {
		if( appViewModel.user.isGuest() ) {
			goToLoginPage( { "id": self.pratilipi.pratilipiId(), "action": "openReviewModal", "ratingInput": self.ratingInput() } );
			return;
		}
		reviewInputDialog.modal( 'show' );
	};

	this.closeReviewModal = function() {
		reviewInputDialog.modal( 'hide' );
		self.ratingInput( self.userPratilipi.rating() );
		self.reviewInput( self.userPratilipi.review() );
	};

	/* Clicking anywhere outside the screen */
	reviewInputDialog.on( 'hidden.bs.modal', function(e) {
		self.closeReviewModal();
	});

	/* Create/Update Review */
	this.createOrUpdateReview = function() {
		if( self.addOrDeleteReviewRequestOnFlight() ) return;
		self.addOrDeleteReviewRequestOnFlight( true );
		ToastUtil.toastUp( "${ _strings.working }" );
		dataAccessor.createOrUpdateReview( self.pratilipi.pratilipiId(), self.ratingInput(), self.reviewInput(),
			function( review ) {
				if( review.rating == null ) review.rating = 0;
				if( review.reviewState != "PUBLISHED" || review.review == null || review.review.trim() == "" ) review.review = null;
				self.updateUserPratilipi( review );
				self.addOrDeleteReviewRequestOnFlight( false );
				self.closeReviewModal();
				ToastUtil.toast( "${ _strings.updated_review }" );
				redirect( self.pratilipi.pageUrl() );
			}, function( error ) {
				self.addOrDeleteReviewRequestOnFlight( false );
				ToastUtil.toast( "${ _strings.server_error_message }" );
		});
	};

	/* Delete Review */
	this.deleteReview = function( review ) {
		if( self.addOrDeleteReviewRequestOnFlight() ) return;
		self.addOrDeleteReviewRequestOnFlight( true );
		ToastUtil.toastUp( "${ _strings.working }" );
		dataAccessor.deleteReview( self.pratilipi.pratilipiId(),
			function( review ) {
				self.updateUserPratilipi( review );
				self.addOrDeleteReviewRequestOnFlight( false );
				ToastUtil.toast( "${ _strings.success_generic_message }" );
			}, function( error ) {
				self.addOrDeleteReviewRequestOnFlight( false );
				ToastUtil.toast( "${ _strings.server_error_message }" );
		} );
	};

	this.submitReview = function() {
		/* Not Updated rating, just cleared the review */
		if( self.ratingInput() == self.userPratilipi.rating() &&
				self.userPratilipi.review() != null &&
				self.userPratilipi.review().trim() != "" &&
				self.reviewInput() != null &&
				self.reviewInput().trim() == "" )
			self.deleteReview();
		else
			self.createOrUpdateReview();
	};

	this.canSubmitReview = ko.computed( function() {
		return self.ratingInput() != 0 && ! self.addOrDeleteReviewRequestOnFlight();
	}, this );

	this.userPratilipiMetaObserver = ko.computed( function() {
		self.ratingInput( self.userPratilipi.rating() );
		self.reviewInput( self.userPratilipi.review() );
	}, this );

	this.userObserver = ko.computed( function() {
		if( ! appViewModel.user.isGuest() && getUrlParameter( 'action' ) == "openReviewModal" ) {
			if( getUrlParameter( "ratingInput" ) != null && getUrlParameter( "ratingInput" ) > 0 ) {
				self.ratingInput( parseInt( getUrlParameter( "ratingInput" ) ) );
			}
			self.openReviewModal();
		}
	}, this );


	/* Share */
	var getShareUrl = function( utm_source ) {
		return encodeURIComponent(
			window.location.origin + self.pratilipi.pageUrl()
			+ ( self.pratilipi.pageUrl().indexOf( '?' ) == -1 ? "?" : "&" )
			+ "utm_language=${ language?lower_case }" + "&"
			+ "utm_version=pwa" + "&"
			+ "utm_device=" + ( isMobile() ? "mobile" : "desktop" ) + "&"
			+ "utm_parent=reader" + "&"
			+ "utm_action=share" + "&"
			+ "utm_source=" + utm_source
		);
	};

	var getWhatsappText = function() {
		return '"' + self.pratilipi.title() + '"${ _strings.whatsapp_read_story } ' + getShareUrl() +" ${ _strings.whatsapp_read_unlimited_stories }";
	};

	this.shareOnFacebook = function() {
		window.open( "http://www.facebook.com/sharer.php?u=" + getShareUrl( "facebook" ),
				"share", "width=1100,height=500,left=70px,top=60px" );
	};

	this.shareOnTwitter = function( utm_location ) {
		window.open( "http://twitter.com/share?url=" + getShareUrl( "twitter" ),
				"share", "width=1100,height=500,left=70px,top=60px" );
	};

	this.shareOnGplus = function( utm_location ) {
		window.open( "https://plus.google.com/share?url=" + getShareUrl( "gplus" ),
				"share", "width=1100,height=500,left=70px,top=60px" );
	};

	this.shareOnWhatsapp = function() {
		window.open( "whatsapp://send?text=" + getWhatsappText() );
	};

}