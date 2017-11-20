function() {
	var self = this;

	this.userObserver = ko.computed( function() {
		if( appViewModel.user.userId() == null )
			return;
		setTimeout( function() {
			if( appViewModel.user.userId() != 0 )
				redirect( appViewModel.user.profilePageUrl(), true );
			else
				goToLoginPage( getUrlParameters() );
		}, 0 );
	}, this );

	this.locationObserver = ko.computed( function() {
        if( appViewModel.currentView() != LOCATION.MY_PROFILE.ko_element ) {
            /* Dispose all observers */
            self.locationObserver.dispose();
            self.userObserver.dispose();
            return;
        }
        appViewModel.currentLocation();
    }, this );

}