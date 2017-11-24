<script>
	var config = {
		apiKey: "AIzaSyAAnK0-vDmY1UEcrRRbCzXgdpF2oQn-E0w",
		authDomain: "prod-pratilipi.firebaseapp.com",
		databaseURL: "https://prod-pratilipi.firebaseio.com",
		storageBucket: "prod-pratilipi.appspot.com",
	};

	firebase.initializeApp( config );

	var node = null;
	var newNotificationCount = null;
	var userLoggedIn = null;
	var user = ${ userJson };

	function firebaseLogin() {
		firebase.auth().signInWithCustomToken( user.firebaseToken );
	}

	function firebaseLogout() {
		firebase.auth().signOut();
	}

	function userChanged( usr ) {
		user = usr;
		if( userLoggedIn == null )
			return;
		if( usr.isGuest && userLoggedIn )
			firebaseLogout();
		else if( ! usr.isGuest && ! userLoggedIn )
			firebaseLogin();
	}

	function resetNewNotificationCount() {
		if( newNotificationCount != 0 && user.userId != 0 ) {
			node = firebase.database().ref( "NOTIFICATION" ).child( user.userId ).child( "newNotificationCount" );
			node.set( 0 );
		}
	}

	firebase.auth().onAuthStateChanged( function( usr ) {
		if( usr ) {
			<#-- Session expired in server but not in firebase - very rare case -->
			if( user.isGuest ) {
				firebaseLogout();
				return;
			}
			userLoggedIn = true;
			node = firebase.database().ref( "NOTIFICATION" ).child( usr.uid ).child( "newNotificationCount" );
			node.on( 'value', function( snapshot ) {
				newNotificationCount = snapshot.val() != null ? snapshot.val() : 0;
				if( document.querySelector( 'pratilipi-header' ) != null )
					document.querySelector( 'pratilipi-header' ).updateNewNotificationCount( newNotificationCount );
			});
		} else {
			<#-- Session expired in firebase but not in server - very rare case -->
			if( ! user.isGuest ) {
				firebaseLogin();
				return;
			}
			userLoggedIn = false;
			node = null;
		}
	});

	function getNotificationPreferences( getNotifPreferencesCallback ) {

		var node = firebase.database().ref( "PREFERENCE" ).child( user.userId );

		node.once( 'value', function( snapshot ) {
			var notification_preferences = {};
			notification_preferences[ "emailFrequency" ] = snapshot.child( 'emailFrequency' ).val();
			notification_preferences[ "notificationSubscriptions" ] = snapshot.child( 'notificationSubscriptions' ).val();
			getNotifPreferencesCallback( notification_preferences );
		});

	}

	function setNotificationPreferences( notification_preferences ) {

	    var node = firebase.database().ref( "PREFERENCE" ).child( user.userId );

	    node.set({
			"emailFrequency": notification_preferences[ "emailFrequency" ],
			"notificationSubscriptions": notification_preferences[ "notificationSubscriptions" ],
			"lastUpdated": firebase.database.ServerValue.TIMESTAMP 
		});

	}
	
</script>
