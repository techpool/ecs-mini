function( params ) {
	var self = this;

	var openWriteDialog = function() {
		$( "#pratilipiWrite" ).modal();
	};

	this.search = function( formElement ) {
		if( appViewModel.searchQuery() && appViewModel.searchQuery().trim().length && window.location.pathname != "/search" )
			redirect( "/search?q=" + appViewModel.searchQuery() );
	};

	this.redirectToSearch = function() {
		redirect( "/search?retUrl=" + encodeURIComponent( window.location.pathname ) );
	};

	this.openMenuNavigationDrawer = function() {
		if( !( typeof( componentHandler ) == 'undefined' ) ) {
			componentHandler.upgradeAllRegistered();
		}
	  document.querySelector( '.mdl-layout' ).MaterialLayout.toggleDrawer();
	};

	this.write = function() {
		if( isMobile() ) {
			ToastUtil.toast( "${ _strings.write_on_desktop_only }", 5000 );
			return;
		}
		if( appViewModel.user.isGuest() ) {
			goToLoginPage( { "action": "write" }, { "message": "WRITE" } );
		} else {
			openWriteDialog();
		}
	};

	/* Loading Notifications */
	var notificationContainer = $( ".js-pratilipi-header #notificationContainer" );
	var notificationTitle = $( ".js-pratilipi-header #notificationTitle" );
	var notificationsBody = $( ".js-pratilipi-header #notificationsBody" );
	var notificationLink = $( ".js-pratilipi-header #notificationLink" );
	var notificationFooter = $( ".js-pratilipi-header #notificationFooter" );
	var viewMoreNotificationLink = $( ".js-pratilipi-header #notificationFooter a" );

	notificationLink.click( function() {
		resetFbNotificationCount();
		var windowsize = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
		if( windowsize < 768 ) {
			redirect( "/notifications" );
		} else {
			notificationContainer.fadeToggle(50);
		}
		return false;
	});

	$( document ).click( function() { notificationContainer.hide(); } );
	/* notificationContainer.click( function(e) { e.stopPropagation(); } ); */
	notificationTitle.click( function(e) { e.stopPropagation(); } );
	notificationsBody.click( function(e) { notificationContainer.hide(); } );
	viewMoreNotificationLink.click( function(e) { notificationContainer.hide(); } );

	this.userObserver = ko.computed( function() {
		if( ! appViewModel.user.isGuest() ) {
			if( getUrlParameter( 'action' ) == "write" )
				openWriteDialog();
			if( getUrlParameter( 'action' ) == "notifications" )
				notificationLink.click();
		}
	}, this );

}
