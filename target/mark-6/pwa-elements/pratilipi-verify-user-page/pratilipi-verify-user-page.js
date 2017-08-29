function() {
	var dataAccessor = new DataAccessor();
	this.requestOnFlight = ko.observable( true );
	dataAccessor.verifyUser( getUrlParameter( "e" ), getUrlParameter( "t" ),
		function() {
			self.requestOnFlight( false );
			ToastUtil.toastUp( "${ _strings.success_generic_message }" );
            setTimeout( function() {
                window.location.href = "/";
            }, 4000 );
		}, function() {
			var message = "${ _strings.server_error_message }";
	        if( error.message != null ) message = error.message;
	        ToastUtil.toastUp( message, 4000 );
	        self.requestOnFlight( false );
	 });
}