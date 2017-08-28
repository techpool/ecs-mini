function( params ) {
	var self = this;

	var resultCount = params.resultCount;
	this.notificationsPageBehaviour = params.notificationsPageBehaviour != null ? params.notificationsPageBehaviour : false;
	var listenToFirebase = params.listenToFirebase != null ? params.listenToFirebase : false;
	var dataAccessor = new DataAccessor();
	var cursor = null;

	/* Loading state */
	/*
	 * 4 Possible values for 'loadingState'
	 * LOADING
	 * LOADED_EMPTY
	 * LOADED
	 * FAILED
	 * 
	 * */
	this.loadingState = ko.observable();
	this.notificationList = ko.observableArray();
	this.hasMoreContents = ko.observable( false );

	this.setNotificationList = function( notificationList ) {
		self.notificationList( [] );
		self.updateNotificationList( notificationList );
	};

	this.updateNotificationList = function( notificationList ) {
		for( var i = 0; i < notificationList.length; i++ )
        	self.notificationList.push( ko.mapping.fromJS( notificationList[i] ) );
	};

	this.fetchNotificationList = function() {
		if( self.loadingState() == "LOADING" ) return;
		self.loadingState( "LOADING" );
		dataAccessor.getNotificationList( cursor, resultCount,
				function( notificationListResponse ) {
					if( notificationListResponse == null ) {
						self.loadingState( "FAILED" );
						return;
					}
					var notificationList = notificationListResponse[ "notificationList" ];
					self.loadingState( self.notificationList().length > 0 || notificationList.length > 0 ? "LOADED" : "LOADED_EMPTY" );
					if( ! self.notificationsPageBehaviour ) {
						self.setNotificationList( notificationList );
						return;
					}
					cursor = notificationListResponse.cursor;
					self.updateNotificationList( notificationList );
					self.hasMoreContents( notificationList.length == resultCount && self.notificationsPageBehaviour );
		});
	};

	this.resetNotificationList = function() {
		self.fetchNotificationList( true );
	};

	this.markNotificationRead = function( vm, e ) {
		var notifId = vm.notificationId;
		for( var i = 0; i < self.notificationList().length; i++ ) {
			if( notifId == self.notificationList()[i].notificationId ) {
				self.notificationList()[i].state( "READ" );
				break;
			}
		}
		return true;
	};

	this.userObserver = ko.computed( function() {
		if( ! appViewModel.user.isGuest() ) {
			setTimeout( self.fetchNotificationList, 0 );
		}
	}, this );

	this.notificationCountObserver = ko.computed( function() {
		if( ! appViewModel.user.isGuest() && appViewModel.notificationCount() != 0 && listenToFirebase ) {
			setTimeout( self.resetNotificationList, 0 );
		}
	}, this );

}
