function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.requestOnFlight = params.requestOnFlight;
	this.isRegister = params.isRegister;

	this.facebookLogin = function() {
		if( self.requestOnFlight() )
			return;
		FB.login( function( fbResponse ) {
			if( fbResponse == null || fbResponse.authResponse == null ) {
				self.requestOnFlight( false );
				return;
			}
			self.requestOnFlight( true );
			ToastUtil.toastUp( "${ _strings.working }" );
			dataAccessor.loginFacebookUser( fbResponse.authResponse.accessToken,
				function( user ) {
					ToastUtil.toast( "${ _strings.user_login_success }" );
					window.location.href = self.isRegister ? user[ "profilePageUrl" ] : getRetUrl( true );
				}, function( error ) {
					self.requestOnFlight( false );
					var message = "${ _strings.server_error_message }";
					if( error[ "fbUserAccessToken" ] != null )
						message = error[ "fbUserAccessToken" ];
					else if( error[ "message" ] != null )
						message = error[ "message" ];
					ToastUtil.toast( message, 3000, true );
			});
		}, { scope: 'public_profile,email,user_birthday' } );
	};

	/* Initialise Facebook Client App */
    window.fbAsyncInit = function() {
    	FB.init({
    		appId: '293990794105516',
    		cookie : true,
    		xfbml: true,
    		version: 'v2.6'
    	});
    };
    (function(d, s, id) {
    	var js, fjs = d.getElementsByTagName(s)[0];
    	if(d.getElementById(id)) {return;}
    	js = d.createElement(s); js.id = id;
    	js.src = "//connect.facebook.net/en_US/sdk.js";
    	fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

}