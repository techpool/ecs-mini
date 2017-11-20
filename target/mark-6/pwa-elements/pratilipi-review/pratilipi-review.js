function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.review = params.review;
	this.isLiked = ko.observable( self.review.isLiked ? self.review.isLiked() : false );
	this.likeCount = ko.observable( self.review.likeCount ? self.review.likeCount() : 0 );
	this.totalCommentCount = ko.observable( self.review.commentCount ? self.review.commentCount() : 0 );

	/* Like / Dislike Review */
	this.voteRequestOnFlight = ko.observable( false );
	this.likeOrDislikeReview = function() {
		if( appViewModel.user.isGuest() ) {
			goToLoginPage();
			return;
		}
		if( self.voteRequestOnFlight() ) return;
		self.voteRequestOnFlight( true );
		var toggleIsLiked = function() {
			self.likeCount( self.isLiked() ? self.likeCount() - 1 : self.likeCount() + 1 );
			self.isLiked( ! self.isLiked() );
		};
		toggleIsLiked();
		dataAccessor.likeOrDislikeReview( self.review.userPratilipiId(), self.isLiked(), 
			function( vote ) { 
				self.voteRequestOnFlight( false );
				ga_CA( 'Review', vote.isLiked ? 'Review Like' : 'Review UnLike' );
			}, function( error ) {
				toggleIsLiked();
				self.voteRequestOnFlight( false );
				ToastUtil.toast( error.message != null ? error.message : "${ _strings.server_error_message }" );
		});
	};



	/* CommentList */
	this.commentList = ko.observableArray();
	this.addToCommentsList = function( comment ) {
		self.commentList.push( ko.mapping.fromJS( comment ) );
	};
	this.updateCommentList = function( commentList ) {
		for( var i = 0; i < commentList.length; i++ )
			self.commentList.push( ko.mapping.fromJS( commentList[i] ) );
	};

	var cursor = null;
	this.isCommentsLoading = ko.observable();
	this.hasMoreContents = ko.observable( false );

	/* Constants */
	var firstLoadCommentCount = 5;
	var subsequentLoadCommentCount = 10;

	this._fetchCommentList = function( resultCount ) {
		if( self.isCommentsLoading() ) return;
		self.isCommentsLoading( true );
		dataAccessor.getReviewCommentList( self.review.userPratilipiId(), cursor, resultCount, 
			function( commentListResponse ) {
				if( commentListResponse == null ) {
					self.hasMoreContents( true );
					self.isCommentsLoading( false );
					return;
				}
				var commentList = commentListResponse.commentList;
				cursor = commentListResponse.cursor;
				self.updateCommentList( commentList );
				self.isCommentsLoading( false );
				self.hasMoreContents( resultCount == commentList.length );
				setTimeout( function() {
					componentHandler.upgradeDom();
				}, 0 );
		});
	};

	this.loadMoreComments = function() {
		self._fetchCommentList( subsequentLoadCommentCount );
	};


	/* Show / Hide Comments */
	this.commentSectionVisible = ko.observable( false );

	this.showCommentSection = function() {
		self.commentSectionVisible( true );
		if( self.commentList().length == 0 && self.totalCommentCount() > 0 )
			self._fetchCommentList( firstLoadCommentCount );
	};

	this.hideCommentSection = function() {
		self.commentSectionVisible( false );
	};

	this.toggleCommentSection = function() {
		self.commentSectionVisible() ? self.hideCommentSection() : self.showCommentSection();
	};


	/* Comment Input */
	this.addCommentInput = ko.observable();
	this.addCommentInputVisible = ko.observable( false );

	this.openAddCommentInput = function() {
		if( appViewModel.user.isGuest() ) {
			goToLoginPage();
			return;
		}
		self.addCommentInput( null );
		self.showCommentSection();
		self.addCommentInputVisible( true );
	};

	this.closeAddCommentInput = function() {
		self.addCommentInputVisible( false );
	};

	this.toggleAddCommentInput = function() {
		self.addCommentInputVisible() ? self.closeAddCommentInput() : self.openAddCommentInput();
	};


	/* Add Comment */
	this.addCommentRequestOnFlight = ko.observable( false );
	this.submitAddComment = function() {
		if( self.addCommentRequestOnFlight() ) return;
		self.addCommentRequestOnFlight( true );
		ToastUtil.toastUp( "${ _strings.working }" );
		dataAccessor.createOrUpdateReviewComment( self.review.userPratilipiId(), null, self.addCommentInput(), 
			function( comment ) {
				self.addToCommentsList( comment );
				self.closeAddCommentInput();
				self.addCommentRequestOnFlight( false );
				ToastUtil.toast( "${ _strings.success_generic_message }" );
			}, function( error ) {
				self.addCommentRequestOnFlight( false );
				ToastUtil.toast( error.message != null ? error.message : "${ _strings.server_error_message }" );
		});
	};


	/* Delete Comment */
	this.deleteComment = function( viewModel ) {
		var commentId = viewModel.comment.commentId();
		ToastUtil.toastUp( "${ _strings.working }" );
		dataAccessor.deleteComment( commentId, 
			function( comment ) {
				ToastUtil.toast( "${ _strings.success_generic_message }" );
				/* Hack remove delete modal forcibly */
				$( '.modal' ).modal( 'hide' );
				$( 'body' ).removeClass( 'modal-open' );
				$( '.modal-backdrop' ).remove();
				self.commentList.remove( function( item ) {
					return item.commentId() == comment.commentId;		   
				});
				self.totalCommentCount( self.totalCommentCount() - 1 )
			}, function( error ) {
				ToastUtil.toast( error.message != null ? error.message : "${ _strings.server_error_message }" );
		});
	};

	/* Computed observables */
	this.canSubmitAddComment = ko.computed( function() {
		return self.addCommentInputVisible() && self.addCommentInput() != null && self.addCommentInput().trim() != "" && ! self.addCommentRequestOnFlight();
	}, this );

}