function() {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.newPassword = ko.observable();
	this.confirmPassword = ko.observable();
	this.requestOnFlight = ko.observable( false );

	this.resetPassword = function() {
		if( self.newPassword() != this.confirmPassword() ) {
			ToastUtil.toast( "Passwords doesn't match!" );
			return;
		}
		self.requestOnFlight( true );
		dataAccessor.resetUserPassword( getUrlParameter( "e" ), getUrlParameter( "t" ), self.newPassword(),
			function( user ) {
				ToastUtil.toastUp( "${ _strings.success_generic_message }" );
				setTimeout( function() {
					window.location.href = "/";
				}, 4000 );
			}, function( error ) {
				var message = "${ _strings.server_error_message }";
				if( error.email != null ) message = error.email;
				if( error.message != null ) message = error.message;
				ToastUtil.toast( message, 4000 );
				self.requestOnFlight( false );
		});
	};

	this.resetPasswordButtonEnabled = ko.computed( function() {
        return self.newPassword() != null && self.newPassword().trim() != ""
            && self.confirmPassword() != null && self.confirmPassword().trim() != ""
            && ! self.requestOnFlight();
    }, this );

}