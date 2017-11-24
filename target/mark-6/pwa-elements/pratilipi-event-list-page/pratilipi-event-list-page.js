function() {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.eventList = ko.observableArray();
	this.isLoading = ko.observable();

	this.fetchEventList = function() {
		self.isLoading( true );
		dataAccessor.getEventList( function( eventListResponse ) {
			if( eventListResponse == null ) {
				self.isLoading( false );
				return;
			}
            self.eventList( eventListResponse.eventList );
            self.isLoading( false );
        });
	};

	this.fetchEventList();

}