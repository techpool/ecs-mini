function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.requestOnFlight = params.requestOnFlight;
	this.isRegister = params.isRegister;

	this.googleLogin = function() {
		if( self.requestOnFlight() )
			return;

		var GoogleAuth = gapi.auth2.getAuthInstance();
		GoogleAuth.signIn().then( function( googleUser ) {
			self.requestOnFlight( true );
			ToastUtil.toastUp( "${ _strings.working }" );
			dataAccessor.loginGoogleUser( googleUser.getAuthResponse().id_token,
				function( user ) {
					ToastUtil.toast( "${ _strings.user_login_success }" );
					window.location.href = self.isRegister ? user[ "profilePageUrl" ] : getRetUrl( true );
				}, function( error ) {
					self.requestOnFlight( false );
					var message = "${ _strings.server_error_message }";
					if( error[ "googleIdToken" ] != null )
						message = error[ "googleIdToken" ];
					else if( error[ "message" ] != null )
						message = error[ "message" ];
					ToastUtil.toast( message, 3000, true );
			});
		}, function( error ) {
			console.log( JSON.stringify( error, undefined, 2 ) );
			self.requestOnFlight( false );
		});
	};

	/* Initialise Google Client App */
	var domId = "pratilipi-google-login";
	if( document.getElementById( domId ) == null ) {
		var googleAuth2 = document.createElement( 'script' );
        googleAuth2.setAttribute( "src", "https://apis.google.com/js/api:client.js" );
        googleAuth2.setAttribute( "id", domId );
        googleAuth2.onload = function() {
            gapi.load( 'auth2', function() {
                auth2 = gapi.auth2.init({
                    client_id: '659873510744-kfim969enh181h4gbctffrjg5j47tfuq.apps.googleusercontent.com',
                    cookiepolicy: 'single_host_origin',
                });
            });
        };

        document.body.appendChild( googleAuth2 );
	}

}