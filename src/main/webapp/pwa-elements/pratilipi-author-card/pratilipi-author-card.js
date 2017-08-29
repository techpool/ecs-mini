function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.author = params.author;
	this.followRequestOnFlight = ko.observable( false );

	this.followOrUnfollowAuthor = function() {
    		if( appViewModel.user.isGuest() ) {
    			goToLoginPage( null, { "message": "FOLLOW" } );
    			return;
    		}

    		if( self.followRequestOnFlight() )
    			return;

    		self.followRequestOnFlight( true );

    		var getFollowingMessage = function( following, name ) {
                var text = following ? "${ _strings.author_follow_success }" : "${ _strings.author_unfollow_success }";
                return text.replace( "$authorName", name );
            };

    		var switchState = function() {
    			self.author.following( ! self.author.following() );
    		};

    		switchState();
    		ToastUtil.toast( getFollowingMessage( self.author.following(), self.author.name() ) );

    		dataAccessor.followOrUnfollowAuthor( self.author.authorId(), self.author.following(),
    			function( userAuthor ) {
    				self.followRequestOnFlight( false );
    				ga_CA( 'Author', userAuthor.following ? 'Follow' : 'UnFollow' );
    			}, function( error ) {
    				self.followRequestOnFlight( false );
    				switchState();
    				ToastUtil.toast( error[ "message" ] != null ? error[ "message" ] : "${ _strings.server_error_message }", 3000, true );
    		});
    	};

}