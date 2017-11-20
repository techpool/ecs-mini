function( params ) {

	var self = this;
	var dataAccessor = new DataAccessor();

	/* Params */
	this.pratilipi = params.pratilipi;
	this.userPratilipi = params.userPratilipi;
	this.updatePratilipi = params.updatePratilipi;
	this.updateUserPratilipi = params.updateUserPratilipi;

	/* Constants */
	var firstLoadReviewCount = 5;
	var subsequentLoadReviewCount = 10;

	/* ReviewList and LoadingState */
	this.reviewList = ko.observableArray();
	this.totalReviewCount = ko.observable();
	var cursor = null;

	/*
	 * 4 Possible values for 'loadingState'
	 * LOADING
	 * LOADED_EMPTY
	 * LOADED
	 * FAILED
	 * 
	 * */
	this.loadingState = ko.observable();

	/* Booleans */
	this.addOrDeleteReviewRequestOnFlight = ko.observable( false );

	/* User Inputs */
	this.ratingInput = ko.observable();
	this.reviewInput = ko.observable();


	/* Functions */

	/* Review List */
	this.addToReviewList = function( review ) {
		/* Updating review doesn't change the reviewDate. Going date-wise chronological order, update the same review if already present  */
		var hasUpdatedReview = review.userPratilipiId == self.userPratilipi.userPratilipiId() && self.userPratilipi.review() != null;
		if( hasUpdatedReview ) {
			for( var i = 0; i < self.reviewList().length; i++ ) {
				if( review.userPratilipiId == self.reviewList()[i].userPratilipiId() ) {
					self.reviewList()[i].rating( review.rating );
					self.reviewList()[i].review( review.review );
					break;
				}
			}
		} else if( review.review != null ) {
			self.reviewList.unshift( ko.mapping.fromJS( review ) );
			self.totalReviewCount( self.totalReviewCount() + 1 );
		}
	};

	this.deleteFromReviewList = function( review ) {
		self.reviewList.remove( function( item ) {
			return item.userPratilipiId() == self.userPratilipi.userPratilipiId();		   
		});
		self.totalReviewCount( self.totalReviewCount() - 1 );
	};

	this.pushToReviewList = function( revList ) {
		for( var i = 0; i < revList.length; i++ )
			self.reviewList.push( ko.mapping.fromJS( revList[i] ) );
	};

	this._fetchReviewList = function( reviewCount ) {
		if( self.loadingState() == "LOADING" ) return;
		self.loadingState( "LOADING" );
		dataAccessor.getReviewList( self.pratilipi.pratilipiId(), cursor, null, reviewCount,
				function( reviewListResponse ) {
					if( reviewListResponse == null ) {
						self.loadingState( "FAILED" );
						return;
					}
					var reviewList = reviewListResponse[ "reviewList" ];
					cursor = reviewListResponse[ "cursor" ];
					self.pushToReviewList( reviewList );
					self.loadingState( self.reviewList().length > 0 ? "LOADED" : "LOADED_EMPTY" );
					self.totalReviewCount( reviewListResponse[ "numberFound" ] );
		});
	};

	this.loadInitialReviews = function() {
		self.reviewList( [] );
		cursor = null;
		self.loadingState( null );
		self.totalReviewCount( 0 );
		self._fetchReviewList( firstLoadReviewCount );
	};

	this.loadMoreReviews = function() {
		self._fetchReviewList( subsequentLoadReviewCount );
	};



	/* Add/Edit Review Modal */
	var reviewInputDialog = $( '#pratilipi-review-input-dialog' );
	this.openReviewModal = function() {		
		if( appViewModel.user.isGuest() ) {
			goToLoginPage( { "action": "openReviewModal", "ratingInput": self.ratingInput() } );
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

	/* Delete Review Modal */
	var deleteReviewConfirmationDialog = $( '#pratilipi-review-delete-confirmation-dialog' );
	this.openDeleteReviewModal = function() {
		self.closeReviewModal();
		deleteReviewConfirmationDialog.modal( 'show' );
	};

	this.closeDeleteReviewModal = function() {
		deleteReviewConfirmationDialog.modal( 'hide' );
		self.openReviewModal();
	};

	/* Clicking anywhere outside the screen */
	deleteReviewConfirmationDialog.on( 'hidden.bs.modal', function(e) {
		self.closeDeleteReviewModal();
	});



	/* Update Pratilipi Objects */
	this.updatePratilipiObject = function( userPratilipi ) {
		/* check if its a new rating or updated rating, and calculate average rating accordingly and update values.*/
		var pratilipi = {};

		var oldRating = self.userPratilipi.rating();
		var newRating = userPratilipi.rating;
		var ratingCount = self.pratilipi.ratingCount() != null ? self.pratilipi.ratingCount() : 0;
		var averageRating = self.pratilipi.averageRating() != null ? self.pratilipi.averageRating() : 0;
		var totalRating =  ratingCount * averageRating;

		if( self.userPratilipi.rating() == 0 ) { /* Added a rating */
			pratilipi[ "ratingCount" ] = ++ratingCount;
		}

		pratilipi[ "averageRating" ] = ( totalRating - oldRating + newRating ) / ratingCount;
		self.updatePratilipi( pratilipi );
	};



	/* Create/Update Review */
	this.createOrUpdateReview = function() {
		if( self.addOrDeleteReviewRequestOnFlight() ) return;
		self.addOrDeleteReviewRequestOnFlight( true );
		ToastUtil.toastUp( "${ _strings.working }" );
		dataAccessor.createOrUpdateReview( self.pratilipi.pratilipiId(), self.ratingInput(), self.reviewInput(), 
			function( review ) {
				if( review.rating == null ) review.rating = 0;
				if( review.reviewState != "PUBLISHED" || review.review == null || review.review.trim() == "" ) review.review = null;
				/* Update reviewList Array first and then userPratilipi Object
				 * We need to know whether user has added/edited his/her review
				 *  */
				self.addToReviewList( review );
				/* Update Pratilipi Object first and then userPratilipi Object
				 * We need old rating to update in Pratilipi object
				 *  */
				self.updatePratilipiObject( review );
				self.updateUserPratilipi( review );
				self.addOrDeleteReviewRequestOnFlight( false );
				self.closeReviewModal();
				ToastUtil.toast( "${ _strings.updated_review }" );
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
				self.deleteFromReviewList( review );
				self.updateUserPratilipi( review );
				self.addOrDeleteReviewRequestOnFlight( false );
				deleteReviewConfirmationDialog.modal( 'hide' );
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


	/* Computed Observables */
	this.hasMoreReviews = ko.computed( function() {
		return self.reviewList().length < self.totalReviewCount() && self.loadingState() == "LOADED";
	}, this );

	this.hasAccessToReview = ko.computed( function() {
		return appViewModel.user.isGuest() || self.userPratilipi.hasAccessToReview();  
	}, this );

	this.canSubmitReview = ko.computed( function() {
		return self.ratingInput() != 0 && ! self.addOrDeleteReviewRequestOnFlight();
	}, this );

	this.pratilipiIdObserver = ko.computed( function() {
		if( self.pratilipi.pratilipiId() == null ) return;
		setTimeout( function() {
			self.loadInitialReviews();
		}, 0 );
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

}