function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.author = params.author;

	var processUserList = function( userList ) {
		if( userList == null ) return [];
		var followList = [];
		for( var i = 0; i < userList.length; i++ ) {
			var user = userList[i];
			var follow = {
				authorId: user.author.authorId,
				name: user.author.name,
				pageUrl: user.profilePageUrl,
				followCount: user.author.followCount,
				imageUrl: user.profileImageUrl,
				following: user.author.following,
				canFollow: user.userId != appViewModel.user.userId(),
				requestOnFlight: false /* Hack: To manipulate */
			};
			followList.push( follow );
		}
		return followList;
	};

	var processAuthorList = function( authorList ) {
		if( authorList == null ) return [];
		var followList = [];
		for( var i = 0; i < authorList.length; i++ ) {
			var author = authorList[i];
			var follow = {
					authorId: author.authorId,
					name: author.name,
					pageUrl: author.pageUrl,
					followCount: author.followCount,
					imageUrl: author.imageUrl,
					following: author.following,
					canFollow: author.user == null || author.user.userId != appViewModel.user.userId(),
					requestOnFlight: false /* Hack: To manipulate */
			};
			followList.push( follow );
		}
		return followList;
	};

	/* Followers and Following List */
	this.followersList = ko.observableArray();
	this.followingList = ko.observableArray();

	this.updateFollowersList = function( userList ) {
		var processedUserList = processUserList( userList );
		for( var i = 0; i < processedUserList.length; i++ )
			self.followersList.push( ko.mapping.fromJS( processedUserList[i] ) );
	};

	this.updateFollowingList = function( authorList ) {
		var processedAuthorList = processAuthorList( authorList );
		for( var i = 0; i < processedAuthorList.length; i++ )
			self.followingList.push( ko.mapping.fromJS( processedAuthorList[i] ) );
	};


	/* Fetching followers and following list */
	var followersCursor = null;
	var followingCursor = null;

	this.isLoadingFollowers = ko.observable( false );
	this.isLoadingFollowing = ko.observable( false );

	this.hasMoreFollowers = ko.observable( true );
	this.hasMoreFollowing = ko.observable( true );

	this.numberFoundFollowers = ko.observable();
	this.numberFoundFollowing = ko.observable();

	this._loadFollowersList = function( resultCount ) {
		if( self.isLoadingFollowers() ) return;
		self.isLoadingFollowers( true );
		dataAccessor.getAuthorFollowers( self.author.authorId(), followersCursor, null, resultCount,
			function( followersListResponse ) {
				if( followersListResponse == null ) {
					self.isLoadingFollowers( false );
					return;
				}
				var userList = followersListResponse.userList;
				self.updateFollowersList( userList );
				followersCursor = followersListResponse.cursor;
				self.numberFoundFollowers( followersListResponse.numberFound );
				self.isLoadingFollowers( false );
				self.hasMoreFollowers( userList.length == resultCount );
		});
	};

	this._loadFollowingList = function( resultCount ) {
		if( self.isLoadingFollowing() ) return;
		self.isLoadingFollowing( true );
		dataAccessor.getUserFollowing( self.author.user.userId(), followingCursor, null, resultCount,
				function( followingListResponse ) {
					if( followingListResponse == null ) {
						self.isLoadingFollowing( false );
						return;
					}
					var authorList = followingListResponse.authorList;
					self.updateFollowingList( authorList );
					followingCursor = followingListResponse.cursor;
					self.numberFoundFollowing( followingListResponse.numberFound );
					self.isLoadingFollowing( false );
					self.hasMoreFollowing( authorList.length == resultCount );
			});
	};


	/* Constants */
	var initialFollowersResultCount = 10;
	var subsequentFollowersResultCount = 15;
	var initialFollowingResultCount = 10;
	var subsequentFollowingResultCount = 15;


	this.loadInitialFollowers = function() {
		self.followersList( [] );
		followersCursor = null;
		self.isLoadingFollowers( false );
		self.hasMoreFollowers( true );
		self.numberFoundFollowers( 0 );
		self._loadFollowersList( initialFollowersResultCount );
	};

	this.loadMoreFollowers = function() {
		self._loadFollowersList( subsequentFollowersResultCount );
	};

	this.loadInitialFollowing = function() {
		self.followingList( [] );
        followingCursor = null;
        self.isLoadingFollowing( false );
        self.hasMoreFollowing( true );
        self.numberFoundFollowing( 0 );
		self._loadFollowingList( initialFollowingResultCount );
	};

	this.loadMoreFollowing =  function() {
		self._loadFollowingList( subsequentFollowingResultCount );
	};


	/* Follow / UnFollow Author */
	this.followOrUnfollowAuthor = function( vm ) {
		if( appViewModel.user.isGuest() ) {
			goToLoginPage( null, { "message": "FOLLOW" } );
			return;
		}
		if( ! vm.canFollow() ) 
			return;
		if( vm.requestOnFlight() ) 
			return;
		vm.requestOnFlight( true );

		var getFollowingMessage = function( following, name ) {
            var text = following ? "${ _strings.author_follow_success }" : "${ _strings.author_unfollow_success }";
            return text.replace( "$authorName", name );
        };

		var switchState = function() {
			vm.followCount( vm.following() ? vm.followCount() - 1 : vm.followCount() + 1 );
			vm.following( ! vm.following() );
		};

		switchState();
		ToastUtil.toast( getFollowingMessage( vm.following(), vm.name() ) );

		dataAccessor.followOrUnfollowAuthor( vm.authorId(), vm.following(), 
			function( userAuthor ) {
				vm.requestOnFlight( false );
				ga_CA( 'Author', userAuthor.following ? 'Follow' : 'UnFollow' );
			}, function( error ) {
				vm.requestOnFlight( false );
				switchState();
				ToastUtil.toast( error[ "message" ] != null ? error[ "message" ] : "${ _strings.server_error_message }", 3000, true );
		});
	};

	/* Computed Observables */
	this.authorIdObserver = ko.computed( function() {
		if( self.author.authorId() == null ) return;
		setTimeout( function() {
			self.loadInitialFollowers();
		}, 0 );
	}, this );

	this.authorUserIdObserver = ko.computed( function() {
		if( self.author.user.userId() == null ) return;
		setTimeout( function() {
			self.loadInitialFollowing();
		}, 0 );
	}, this );

}
