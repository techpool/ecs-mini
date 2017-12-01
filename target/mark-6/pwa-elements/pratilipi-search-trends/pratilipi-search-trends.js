function() {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.keywordList = ko.observableArray();
	this.isLoading = ko.observable( false );

	this.keywordClicked = function( vm, e ) {
		var keyword = $( e.currentTarget ).attr( 'data-val' );
		var index = $( e.currentTarget ).attr( 'data-index' );
		ga_CAV( "Search", "TrendingSearch", index );
		appViewModel.searchQuery( keyword );
	};

	this.fetchSearchTrends = function() {
		Array.prototype.clean = function( deleteValue ) {
			for( var i = 0; i < this.length; i++ ) {
				if( this[i] == deleteValue ) {
					this.splice(i, 1);
					i--;
				}
			}
			return this;
		};
		self.isLoading( true );
		dataAccessor.getTrendingSearchKeywords( function( response ) {
			if( response == null || isEmpty( response ) ) {
				return;
			}
			var keywordList = response.trending_keywords.clean( undefined );
			self.keywordList( keywordList );
			self.isLoading( false );
		});
	};

	this.fetchSearchTrends();

}