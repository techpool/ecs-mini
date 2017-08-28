function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.comment = params.comment;
	this.deleteComment = params.deleteComment;
	this.toggleCommentInput = params.toggleCommentInput;

	this.isLiked = ko.observable( self.comment.isLiked ? self.comment.isLiked() : false );
	this.likeCount = ko.observable( self.comment.likeCount ? self.comment.likeCount() : 0 );

	/* Like / Dislike Comment */
	this.voteRequestOnFlight = ko.observable( false );
	this.likeOrDislikeComment = function() {
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
		dataAccessor.likeOrDislikeComment( self.comment.commentId(), self.isLiked(), 
			function( vote ) { 
				self.voteRequestOnFlight( false );
			}, function( error ) {
				toggleIsLiked();
				self.voteRequestOnFlight( false );
				ToastUtil.toast( error.message != null ? error.message : "${ _strings.server_error_message }" );
		});
	};

	/* Edit Comment - Input */
	this.editCommentInput = ko.observable();
	this.editCommentVisible = ko.observable( false );

	this.openEditComment = function() {
		self.editCommentInput( self.comment.content() );
		self.editCommentVisible( true );
	};

	this.closeEditComment = function() {
		self.editCommentVisible( false );
	};

	/* Edit Comment - Submit */
	this.editCommentRequestOnFlight = ko.observable( false );
	this.submitEditComment = function() {
		if( self.editCommentRequestOnFlight() ) return;
		self.editCommentRequestOnFlight( true );
		ToastUtil.toastUp( "${ _strings.working }" );
		dataAccessor.createOrUpdateReviewComment( null, self.comment.commentId(), self.editCommentInput(), 
			function( comment ) {
				self.comment.content( comment.content );
				self.closeEditComment();
				self.editCommentRequestOnFlight( false );
				ToastUtil.toast( "${ _strings.success_generic_message }" );
			}, function( error ) {
				self.editCommentRequestOnFlight( false );
				ToastUtil.toast( error.message != null ? error.message : "${ _strings.server_error_message }" );
		});
	};

	/* Computed observables */
	this.canSubmitEditComment = ko.computed( function() {
		return self.editCommentVisible() && self.editCommentInput() != null && self.editCommentInput().trim() != "" && ! self.editCommentRequestOnFlight();
	}, this );

}