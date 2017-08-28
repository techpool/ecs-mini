function( params ) {
	var self = this;
	var pratilipiId = params.pratilipiId; /* ko observable */
	var context = params.context;

	var resultCount = 6;
	var dataAccessor = new DataAccessor();

	this.title = ko.observable();
	this.pratilipiList = ko.observableArray();
	this.isLoading = ko.observable( false );

	this.updatePratilipiList = function( pratilipiList ) {
		for( var i = 0; i < pratilipiList.length; i++ ) {
			pratilipiList[i]["ga_index"] = i;
			self.pratilipiList.push( ko.mapping.fromJS( pratilipiList[i] ) );
		}
	};

	this.fetchPratilipiList = function() {
		if( self.isLoading() ) return;
		self.isLoading( true );
		/*
		var context = null;
		if( getLocation() == LOCATION.PRATILIPI )
			context = "pratilipiPage";
		else if( getLocation() == LOCATION.READ )
			context = "readPage";
		*/
		dataAccessor.getPratilipiRecommendation( pratilipiId(), context, resultCount,
			function( response ) {
				if( response == null ) {
					self.isLoading( false );
					return;
				}
				self.title( response.title );
				self.updatePratilipiList( response.pratilipiList );
				self.isLoading( false );
		});
	};

	this.pratilipiIdObserver = ko.computed( function() {
		if( pratilipiId() == null )
			return;
		setTimeout( function() {
			self.title( null );
			self.pratilipiList( [] );
			self.isLoading( false );
			self.fetchPratilipiList();
		}, 0 );
	}, this );

}