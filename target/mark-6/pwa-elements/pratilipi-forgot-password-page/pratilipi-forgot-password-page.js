function() {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.userEmail = ko.observable();
	this.requestOnFlight = ko.observable( false );

	this.forgotPassword = function() {
		if( self.requestOnFlight() )
			return;
		if( self.userEmail() == null || self.userEmail().trim() == "" ) {
			ToastUtil.toast( "${ _strings.user_email_empty }" );
			return;
		}
		if( ! validateEmail( self.userEmail() ) ) {
			ToastUtil.toast( "${ _strings.user_email_invalid }" );
			return;
		}
		self.requestOnFlight( true );
		dataAccessor.forgotPassword( self.userEmail(),
			function() {
				ToastUtil.toastUp( "${ _strings.password_reset_request_success }" );
				setTimeout( function() {
					window.location.href = getRetUrl( true );
				}, 5000 );
			} , function( error ) {
				var message = "${ _strings.server_error_message }";
                if( error[ "email" ] != null )
                    message = error[ "email" ];
                else if( error[ "message" ] != null )
                    message = error[ "message" ];
                ToastUtil.toast( message, 2000, true );
			}
		);
	};

	this.forgotPasswordButtonEnabled = ko.computed( function() {
        return self.userEmail() != null && self.userEmail().trim() != "" && ! self.requestOnFlight();
    }, self );

    this.locationObserver = ko.computed( function() {
        if( appViewModel.currentView() != LOCATION.FORGOT_PASSWORD.ko_element ) {
            /* Clear All Fields */
            self.userEmail( null );

            /* Dispose all observers */
            self.locationObserver.dispose();
            return;
        }
        appViewModel.currentLocation();
    }, this );

}