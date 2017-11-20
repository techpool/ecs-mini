function() {
	var self = this;
	var dataAccessor = new DataAccessor();
	var cursor = null;
	var resultCount = 10;

	this.title = window.location.pathname == "/blog" ? "${ _strings.seo_blog_page }" : "${ _strings.seo_author_interview }";

	this.sendGAEvent = function() {
		ga_CA( window.location.pathname == '/blog' ? 'Blog' : 'AuthorInterview', 'Open' );
		return true;
	};

	this.blogPostList = ko.observableArray();
	this.updateBlogPostList = function( blogPostList ) {
		for( var i = 0; i < blogPostList.length; i++ )
			self.blogPostList.push( blogPostList[i] );
	};

	this.isLoading = ko.observable();
	this.hasMoreContents = ko.observable( true );

	this.fetchBlogPostList = function() {
		if( self.isLoading() || ! self.hasMoreContents() ) return;
		self.isLoading( true );
		dataAccessor.getBlogPostListByUri( window.location.pathname, "PUBLISHED", cursor, resultCount,
			function( blogPostListResponse ) {
				if( blogPostListResponse == null ) {
					self.isLoading( false );
					return;
				}
				var blogPostList = blogPostListResponse.blogPostList;
				self.updateBlogPostList( blogPostList );
				cursor = blogPostListResponse.cursor;
				self.isLoading( false );
				self.hasMoreContents( blogPostList.length == resultCount && cursor != null );
		});
	};

	this.pageScrollObserver = ko.computed( function() {
		if( ( appViewModel.scrollTop() / $( ".js-pratilipi-blog-post-grid" ).height() ) > 0.6 ) {
			setTimeout( function() {
				self.fetchBlogPostList();
			}, 100 ); /* locationObserver and pageScrollObserver will be triggered at once. */
		}
	}, this );

	this.locationObserver = ko.computed( function() {
        if( appViewModel.currentView() != LOCATION.BLOG.ko_element ) {
            /* Dispose all observers */
            self.locationObserver.dispose();
            self.pageScrollObserver.dispose();
            return;
        }
        appViewModel.currentLocation();
        setTimeout( function() {
            self.fetchBlogPostList();
        }, 0 );
    }, this );


	var container = $( ".pratilipi-blog-page .js-blog-post-content" );
	var lineHeight = parseInt( container.css( "line-height" ) );
	container.css( "max-height", (lineHeight*3) + 'px' );

}
