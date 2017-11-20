/* Test Environment */
var isTestEnvironment = ${ website.isTestEnvironment()?c };
var aEEUserIdList = [];
<#list aEEUserIdList as aEEUserId>aEEUserIdList.push( ${ aEEUserId?c } );</#list>

/* Location */
var LOCATION = {

	HOME: { ga_value: "HomePage", ko_element: "pratilipi-home-page", fb_value: "HOME" },
	LIST: { ga_value: "ListPage", ko_element: "pratilipi-list-page" },
	LIBRARY: { ga_value: "LibraryPage", ko_element: "pratilipi-library-page" },

	SEARCH : { ga_value: "SearchPage", ko_element: "pratilipi-search-page" },
	SEARCH_V2: { ga_value: "SearchPage", ko_element: "pratilipi-search-page-v2", hide_appshell: true },

	EVENTS: { ga_value: "AllEventsPage", ko_element: "pratilipi-event-list-page" },
	EVENT: { ga_value: "EventPage", ko_element: "pratilipi-event-page" },

	AUTHOR: { ga_value: "AuthorPage", ko_element: "pratilipi-author-page", fb_value: "AUTHOR_PAGE" },
	USER: { ga_value: "UserPage", ko_element: "pratilipi-author-page" },
	PRATILIPI: { ga_value: "PratilipiPage", ko_element: "pratilipi-pratilipi-page", fb_value: "PRATILIPI_PAGE" },

	LOGIN: { ga_value: "LoginPage", ko_element: "pratilipi-login-page", hide_appshell: true },
	REGISTER: { ga_value: "RegisterPage", ko_element: "pratilipi-register-page", hide_appshell: true },
	FORGOT_PASSWORD: { ga_value: "ForgotPasswordPage", ko_element: "pratilipi-forgot-password-page", hide_appshell: true },
	RESET_PASSWORD: { ga_value: "ResetPasswordPage", ko_element: "pratilipi-reset-password-page", hide_appshell: true },
	VERIFY_USER: { ga_value: "VerifyUserPage", ko_element: "pratilipi-verify-user-page", hide_appshell: true },

	NOTIFICATIONS: { ga_value: "NotificationsPage", ko_element: "pratilipi-notifications-page" },
	SETTINGS: { ga_value: "UserSettings", ko_element: "pratilipi-settings-page" },

	BLOG: { ga_value: "AllBlogsPage", ko_element: "pratilipi-blog-page" },
	AUTHOR_INTERVIEWS: { ga_value: "AllAuthorInterviewsPage", ko_element: "pratilipi-blog-page" },

	BLOG_POST: { ga_value: "BlogPage", ko_element: "pratilipi-blog-post-page" },
	AUTHOR_INTERVIEW_POST: { ga_value: "AuthorInterviewPage", ko_element: "pratilipi-blog-post-page" },

	PRATILIPI_2016: { ga_value: "Pratilipi2016Page", ko_element: "pratilipi-pratilipi2016-page" },
	STATIC: { ga_value: "StaticPage", ko_element: "pratilipi-static-page" },
	MY_PROFILE: { ga_value: "MyProfilePage", ko_element: "pratilipi-myprofile-page" },
	PAGE_NOT_FOUND: { ga_value: "PageNotFound", ko_element: "pratilipi-pagenotfound-page" },

	READER: { ga_value: "Reader", ko_element: "pratilipi-reader", hide_appshell: true },
	WRITER: { ga_value: "Writer", ko_element: null, fb_value: "WRITER_PANEL" }

};


function getLocation( location ) {

	String.prototype.count = function( s1 ) {
		return ( this.length - this.replace( new RegExp(s1,"g"), '' ).length ) / s1.length;
	};

	if( location == null )
		location = window.location.pathname;

	if( location == "/" )
		return LOCATION.HOME;
	else if( location == "/library" )
		return LOCATION.LIBRARY;
	else if( location == "/search" )
		return LOCATION.SEARCH_V2;
	/* return appViewModel && appViewModel.isAEETestEnvironment() ? LOCATION.SEARCH_V2 : LOCATION.SEARCH; */

	else if( location == "/login" )
		return LOCATION.LOGIN;
	else if( location == "/register" )
		return LOCATION.REGISTER;
	else if( location == "/forgot-password" )
		return LOCATION.FORGOT_PASSWORD;
	else if( location == "/reset-password" )
		return LOCATION.RESET_PASSWORD;
	else if( location == "/verify-user" )
		return LOCATION.VERIFY_USER;

	else if( location == "/notifications" )
		return LOCATION.NOTIFICATIONS;
	else if( location == "/settings" )
		return LOCATION.SETTINGS;

	else if( location == "/events" )
		return LOCATION.EVENTS;
	else if( location.startsWith( "/event/" ) && location.count( '/' ) == 2 )
		return LOCATION.EVENT;

	else if( location == "/blog" )
		return LOCATION.BLOG;
	else if( location == "/author-interviews" )
		return LOCATION.AUTHOR_INTERVIEWS;

	else if( location.startsWith( "/blog/" ) && location.count( '/' ) == 2 )
		return LOCATION.BLOG_POST;
	else if( location.startsWith( "/author-interviews/" ) && location.count( '/' ) == 2 )
		return LOCATION.AUTHOR_INTERVIEW_POST;
	else if( location.startsWith( "/blogpost/" ) && location.count( '/' ) == 2 )
		return LOCATION.BLOG_POST;

	else if( location == "/pratilipi-2016" )
		return LOCATION.PRATILIPI_2016;
	else if( location == "/my-profile" )
		return LOCATION.MY_PROFILE;

	/* List pages */
	<#list listTitleList as aList>
	else if( location == "${ aList.url }" )
		return LOCATION.LIST;
	</#list>

	/* Static pages */
	<#list staticUrlList as aUrl>
	else if( location == "${ aUrl }" )
		return LOCATION.STATIC;
	</#list>

	/* Reader */
	else if( location == "/read" )
		return LOCATION.READER;
	/* Writer */
	else if( location == "/write" || location == "/pratilipi-write" )
		return LOCATION.WRITER;

	/* Setting for the first time in case routing is not supported */
	else if( location.startsWith( "/author/" ) && location.count( '/' ) == 2 )
		return appViewModel == null ? LOCATION.AUTHOR : ( appViewModel.isUserPage() ? LOCATION.USER : LOCATION.AUTHOR );
	else if( location.count( '/' ) == 1 )
		return appViewModel == null ? LOCATION.AUTHOR : ( appViewModel.isUserPage() ? LOCATION.USER : LOCATION.AUTHOR );

	else if( location.startsWith( "/pratilipi/" ) && location.count( '/' ) == 2 )
		return LOCATION.PRATILIPI;
	else if( location.startsWith( "/book/" ) && location.count( '/' ) == 2 )
			return LOCATION.PRATILIPI;
	else if( location.startsWith( "/story/" ) && location.count( '/' ) == 2 )
		return LOCATION.PRATILIPI;
	else if( location.startsWith( "/article/" ) && location.count( '/' ) == 2 )
		return LOCATION.PRATILIPI;
	else if( location.startsWith( "/poem/" ) && location.count( '/' ) == 2 )
		return LOCATION.PRATILIPI;
	else if( location.count( '/' ) == 2 )
		return LOCATION.PRATILIPI;

	else
		return LOCATION.PAGE_NOT_FOUND;

}


var AppViewModel = function() {
	var defaultUser = {
		"userId": null,
		"author": { "authorId": null },
		"displayName": null,
		"email": null,
		"phone": null,
		"state": null,
		"isGuest": true, 
		"isEmailVerified": false,
		"profilePageUrl": null, 
		"profileImageUrl": null, 
		"firebaseToken": null
	};
	var self = this;
	this.user = ko.mapping.fromJS( defaultUser, {}, this.user );
	this.notificationCount = ko.observable( -1 );
	this.searchQuery = ko.observable();
	this.pratilipiWriteAuthorId = ko.observable();
	this.userPreferences = ko.observable( {} );
	this.metaTags = ko.observableArray();
	this.pageTitle = ko.observable( "${ _strings.pratilipi } | Pratilipi" );
	this.randomQuote = ko.observable( getRandomQuote() );
	this.scrollTop = ko.observable();
	this.scrollTopObserver = ko.computed( function() {
		self.scrollTop();
		setTimeout( function() { $(window).scroll(); /* Trigger scroll event on window */ } );
	}, this );
	this.notifyOfScrollEvent = function() {
		self.scrollTop( Math.max( $( ".mdl-layout" ).scrollTop(), $( ".mdl-layout__content" ).scrollTop() ) );
	};
	this.scrollToTop = function( scrollTop ) {
		$( ".mdl-layout" ).animate({ scrollTop: scrollTop != null ? scrollTop : 0 }, 200 );
		$( ".mdl-layout__content" ).animate({ scrollTop: scrollTop != null ? scrollTop : 0 }, 200 );
	};

	this.isUserPage = ko.observable(); /* Used for GA tracking -> When Author is visiting his/her own page. */

	/* Routing */
	this.hideAppShell = ko.observable( getLocation()[ "hide_appshell" ] != null ? getLocation()[ "hide_appshell" ] : false );
	this.currentView = ko.observable( getLocation()[ "ko_element" ] );
	this.currentLocation = ko.observable();

	/* Reader - Persisting Settings as cookies alone might not work well */
	this.readerFontSize = ko.observable(); /* Font Size: 12px - 32px */
	this.readerImageSize = ko.observable(); /* Image Size: 300px - 1500px */
	this.readerTheme = ko.observable(); /* 3 States: NORMAL, NIGHT, SEPIA */
	this.readerLineHeight = ko.observable(); /* 3 States: SMALL, MEDIUM, LARGE */

	/* Test Environment */
	this.isTestEnvironment = isTestEnvironment;
	this.isAEETestEnvironment = ko.computed( function() {
		return this.isTestEnvironment || aEEUserIdList.indexOf( this.user.userId() ) !== -1;
	}, this );

	/* Setting searchQuery */
	this.locationObserver = ko.computed( function() {
		this.searchQuery( this.currentLocation() == "/search" && getUrlParameter( "q" ) != null ? decodeURI( getUrlParameter( "q" ) ) : null );
	}, this );

	/* Setting language */
	this.language = "${ language.name() }";

};

var appViewModel = new AppViewModel();

/* Random Quote */
var randomQuoteTimer = new Timer( function() { /* Refer TimerUtil.js */
	appViewModel.randomQuote( getRandomQuote() );
}, 5000);


var initFirebase = function() {
	if( appViewModel.user.isGuest() ) return;
	var firebaseLibrary = document.createElement( 'script' );
	firebaseLibrary.setAttribute( "src", "${ firebaseLibrary }" );
	firebaseLibrary.onload = function() {
		var config = {
			apiKey: "${ firebaseApiKey }",
			authDomain: "${ firebaseAuthDomain }",
			databaseURL: "${ firebaseDatabaseUrl }",
			projectId: "${ firebaseProjectId }",
			storageBucket: "${ firebaseStorageBucket }",
		};
		firebase.initializeApp( config );
		firebase.auth().onAuthStateChanged( function( fbUser ) {
			if( fbUser ) {
				var newNotificationCountNode = firebase.database().ref( "NOTIFICATION" ).child( fbUser.uid ).child( "newNotificationCount" );
				newNotificationCountNode.on( 'value', function( snapshot ) {
					var newNotificationCount = snapshot.val() != null ? snapshot.val() : 0;
					appViewModel.notificationCount( newNotificationCount );
				});
				var userPreferencesNode = firebase.database().ref( "PREFERENCE" ).child( fbUser.uid );
				userPreferencesNode.on( 'value', function( snapshot ) {
					var userPreferences = snapshot.val() != null ? snapshot.val() : {};
					appViewModel.userPreferences( userPreferences );
				});
			} else {
				firebase.auth().signInWithCustomToken( appViewModel.user.firebaseToken() );
			}
		});
	};
	document.body.appendChild( firebaseLibrary );
};

var setFbAnalyticsUser = function() {
		FB.AppEvents.setUserID(appViewModel.user.userId().toString());
};

var resetFbNotificationCount = function() {
	var node = firebase.database().ref( "NOTIFICATION" ).child( appViewModel.user.userId() ).child( "newNotificationCount" );
	node.set( 0 );
};

var setUserPreferences = function( userPreferences ) {
	var node = firebase.database().ref( "PREFERENCE" ).child( appViewModel.user.userId() );
	node.set({
		"emailFrequency": userPreferences[ "emailFrequency" ],
		"notificationSubscriptions": userPreferences[ "notificationSubscriptions" ],
		"lastUpdated": firebase.database.ServerValue.TIMESTAMP 
	});
};

var updateUser = function() {
	var dataAccessor = new DataAccessor();
	dataAccessor.getUser( function( user ) {
		ko.mapping.fromJS( user, {}, appViewModel.user );
		initFirebase();
		setFbAnalyticsUser();
	});
};

ko.applyBindings( appViewModel, document.getElementsByTagName("html")[0] );
updateUser();

var sammy = Sammy(function () {

	var regex = "^(?!\/pratilipi-write$|\/write$|\/admin$|\/admin\/.*|\/edit-event$|\/edit-blog$).*$";

	this.get( regex,
		function () {

			/* Hack - Followers page not implemented yet */
			if( window.location.pathname == "/followers" ) {
				redirect( "/my-profile", true );
			}

			var location = getLocation();
			appViewModel.currentView( location[ "ko_element" ] );
			appViewModel.currentLocation( window.location.pathname );
			appViewModel.hideAppShell( location.hide_appshell != null ? location.hide_appshell : false );

			/* Scroll to Top */
			appViewModel.scrollToTop();

			/* Random Quote */
			appViewModel.randomQuote( getRandomQuote() );
			randomQuoteTimer.reset();

			/* Mdl sucks */
			setTimeout( function() { if( typeof( componentHandler ) !== 'undefined' ) componentHandler.upgradeDom(); }, 0 );

			/* GoogleAnalytics Pageview */
			if( ! appViewModel.user.isGuest() )
				ga( 'set', 'userId', appViewModel.user.userId() );
			ga( 'set', 'page', window.location.href );
			ga( 'set', 'dimension1', appViewModel.user.userId() == null ? 0 : appViewModel.user.userId() );
			ga( 'set', 'dimension2', '${ ga_website }' );
			ga( 'set', 'dimension3', '${ ga_websiteMode }' );
			ga( 'set', 'dimension4', '${ ga_websiteVersion }' );
			ga( 'send', 'pageview' );

			/* Notification Read */
			if( getUrlParameter( "notifId" ) != null ) new DataAccessor().updateNotificationState( getUrlParameter( "notifId" ), "READ" );

		}
	);

	this.post( '.*', function() {
		return false;
	});

	this._checkFormSubmission = function( form ) {
		return (false);
	};

}).run();