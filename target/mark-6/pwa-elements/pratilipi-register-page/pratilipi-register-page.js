function() {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.requestOnFlight = ko.observable( false );
	this.userName = ko.observable();
    this.userEmail = ko.observable();
    this.userPassword = ko.observable();
    this.agreedTerms = ko.observable( false );

	this.register = function() {
        if( self.requestOnFlight() )
            return;
        if( self.userName() == null || self.userName().trim() == "" ) {
            ToastUtil.toast( "${ _strings.user_name_empty }" );
            return;
        }
        if( self.userEmail() == null || self.userEmail().trim() == "" ) {
            ToastUtil.toast( "${ _strings.user_email_empty }." );
            return;
        }
        if( self.userPassword() == null || self.userPassword().trim() == "" ) {
            ToastUtil.toast( "${ _strings.user_password_empty }" );
            return;
        }
        if( ! validateEmail( self.userEmail() ) ) {
            ToastUtil.toast( "${ _strings.user_email_invalid }" );
            return;
        }
        if( ! self.agreedTerms() ) {
            ToastUtil.toast( "${ _strings.accept_terms }" );
            return;
        }
        self.requestOnFlight( true );
        ToastUtil.toastUp( "${ _strings.working }" );
        dataAccessor.registerUser( self.userName(), self.userEmail(), self.userPassword(),
            function( user ) {
                ToastUtil.toast( "${ _strings.user_register_success }" );
                window.location.href = user[ "profilePageUrl" ];
            }, function( error ) {
                self.requestOnFlight( false );
                var message = "${ _strings.server_error_message }";
                if( error[ "name" ] != null )
                    message = error[ "name" ];
                else if( error[ "email" ] != null )
                    message = error[ "email" ];
                else if( error[ "password" ] != null )
                    message = error[ "password" ];
                else if( error[ "message" ] != null )
                    message = error[ "message" ];
                ToastUtil.toast( message, 3000, true );
        });
    };

    this.registerButtonEnabled = ko.computed( function() {
        return self.userName() != null && self.userName().trim() != ""
            && self.userEmail() != null && self.userEmail().trim() != ""
            && self.userPassword() != null && self.userPassword().trim() != ""
            && self.agreedTerms() && ! self.requestOnFlight();
    }, self );

    this.locationObserver = ko.computed( function() {
        if( appViewModel.currentView() != LOCATION.REGISTER.ko_element ) {
            /* Clear All Fields */
            self.userName( null );
            self.userEmail( null );
            self.userPassword( null );
            self.agreedTerms( false );

            /* Dispose all observers */
            self.locationObserver.dispose();
            return;
        }
        appViewModel.currentLocation();
    }, this );

}