function() {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.requestOnFlight = ko.observable( false );
	this.userEmail = ko.observable();
	this.userPassword = ko.observable();

	this.login = function() {
        if( self.requestOnFlight() )
            return;
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
        self.requestOnFlight( true );
        ToastUtil.toastUp( "${ _strings.working }" );

        dataAccessor.loginUser( self.userEmail(), self.userPassword(),
            function( user ) {
                ToastUtil.toast( "${ _strings.user_login_success }" );
                window.location.href = getRetUrl( true );
            }, function( error ) {
                self.requestOnFlight( false );
                var message = "${ _strings.server_error_message }";
                if( error[ "email" ] != null )
                    message = error[ "email" ];
                else if( error[ "password" ] != null )
                    message = error[ "password" ];
                else if( error[ "message" ] != null )
                    message = error[ "message" ];
                ToastUtil.toast( message, 3000, true );
        });
    };

    this.loginButtonEnabled = ko.computed( function() {
        return self.userEmail() != null && self.userEmail().trim() != ""
            && self.userPassword() != null && self.userPassword().trim() != ""
            && ! self.requestOnFlight();
	}, this );

	this.userObservser = ko.computed( function() {
		if( appViewModel.user.userId() == null || appViewModel.user.userId() == 0 ) return;
		setTimeout( function() {
			ToastUtil.toastUp( "<a href='" + getRetUrl( true ) + "'>${ _strings.user_logged_in_already }</a>" );
		}, 0 );
	}, this );

	this.locationObserver = ko.computed( function() {
        if( appViewModel.currentView() != LOCATION.LOGIN.ko_element ) {
            /* Clear All Fields */
            self.userEmail( null );
            self.userPassword( null );

            /* Dispose all observers */
            self.locationObserver.dispose();
            self.userObservser.dispose();
            return;
        }
        appViewModel.currentLocation();
    }, this );

    if( getUrlParameter( "message" ) != null ) {
        var type = getUrlParameter( "message" );
        var message = null;
        if( type == "NOTIFICATIONS" )
            message = "${ _strings.user_login_to_view_notifications }";
        else if( type == "LIBRARY" )
            message = "${ _strings.user_login_to_view_library }";
        else if( type == "WRITE" )
            message = "${ _strings.write_on_desktop_only }";
        else if( type == "FOLLOW" )
            message = "${ _strings.user_login_to_follow }";
        if( message != null )
            ToastUtil.toast( message, 4000 );
    }



}