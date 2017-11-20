function( params ) {
	var self = this;
	var getEmailFrequencyVernacular = function( emailFrequency ) {
		switch( emailFrequency ) {
			case "IMMEDIATELY":
				return "${ _strings.email_frequency_immediate }";
			case "DAILY":
				return "${ _strings.email_frequency_daily }";
			case "WEEKLY":
				return "${ _strings.email_frequency_weekly }";
			case "MONTHLY":
				return "${ _strings.email_frequency_monthly }";
			case "NEVER":
				return "${ _strings.email_frequency_never }";
		}
	};

	this.updateNotificationPreferences = function() {
		var userPreferences = {};
		userPreferences[ "emailFrequency" ] = document.querySelector( "#pratilipiNotificationSettings #emailFrequency" ).getAttribute( "data-val" );
		userPreferences[ "notificationSubscriptions" ] = {};
		$( '#pratilipiNotificationSettings .mdl-js-checkbox' ).each( function( index, element ) {
			userPreferences[ "notificationSubscriptions" ][ element.firstChild.value ] = element.firstChild.checked;
		});
		setUserPreferences( userPreferences );
		ToastUtil.toast( "${ _strings.success_generic_message }" );
		$( "#pratilipiNotificationSettings" ).modal( 'hide' );
	};

	this.firebaseCallback = ko.computed( function() {
		var userPreferences = appViewModel.userPreferences();
		var emailFrequency = userPreferences[ "emailFrequency" ] != null ? userPreferences[ "emailFrequency" ] : "IMMEDIATELY";
		var notificationSubscriptions = userPreferences[ "notificationSubscriptions" ] != null ? 
				userPreferences[ "notificationSubscriptions" ] : {};

		$( "#pratilipiNotificationSettings #emailFrequency" ).attr( 'data-val', emailFrequency );
		$( "#pratilipiNotificationSettings #emailFrequency" ).attr( 'value', getEmailFrequencyVernacular( emailFrequency ) );
		$( '#pratilipiNotificationSettings .mdl-js-checkbox' ).each( function( index, element ) {
			if( element.MaterialCheckbox == null ) return;
			var val = element.firstChild.value;
			if( notificationSubscriptions[ val ] != null && ! notificationSubscriptions[ val ] )
				element.MaterialCheckbox.uncheck();
			else
				element.MaterialCheckbox.check();
		});

	}, this );

}
