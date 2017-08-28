function( params ) {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.isAdmin = getUrlParameter( "authorId" ) != null && getUrlParameter( "userId" ) != null;

	/* PROFILE SETTINGS */
	this.firstName = ko.observable();
	this.lastName = ko.observable();
	this.firstNameEn = ko.observable();
	this.lastNameEn = ko.observable();
	this.penName = ko.observable();
	this.language = ko.observable();
	this.summary = ko.observable();

	this.email = ko.observable();
	this.phone = ko.observable();
	this.gender = ko.observable();
	this.dateOfBirth = ko.observable();
	this.isLoading = ko.observable();

	this.authorPageUrl = null;

	/* Original Data - Disable the button if user hadn't changed any setting */
	var originalAuthor = null;
	var originalUser = {};

	this.updateLanguage = function() {
		self.language( document.querySelector( '#pratilipi-settings-language' ).getAttribute( "data-val" ) );
	};

	this.updateGender = function() {
		self.gender( document.querySelector( '#pratilipi-settings-gender' ).getAttribute( "data-val" ) );
	};

	var getAuthorId = function() {
		return getUrlParameter( "authorId" ) != null ? getUrlParameter( "authorId" ) : appViewModel.user.author.authorId();
	};

	var getUserId = function() {
		return getUrlParameter( "userId" ) != null ? getUrlParameter( "userId" ) : appViewModel.user.userId();
	};

	var formatDateOfBirth = function( inputDate ) {
		if( inputDate == null ) return null;
		var d = new Date( inputDate ),
			month = '' + ( d.getMonth() + 1 ),
			day = '' + d.getDate(),
			year = d.getFullYear();
		if (month.length < 2) month = '0' + month;
		if (day.length < 2) day = '0' + day;
		return [ day, month, year ].join( '-' );
	};

	this.loadUserAndAuthor = function() {
		if( self.isLoading() ) return;
		self.isLoading( true );
		dataAccessor.getAuthorById( getAuthorId(), false,
			function( author ) {
				originalAuthor = author;
				self.firstName( author.firstName );
				self.lastName( author.lastName );
				self.firstNameEn( author.firstNameEn );
				self.lastNameEn( author.lastNameEn );
				self.penName( author.penName );
				self.language( author.language );
				$( "#pratilipi-settings-language" ).attr( 'data-val', author.language );
				$( "#pratilipi-settings-language" ).attr( 'value', getLanguageVernacular( author.language ) );
				self.summary( author.summary );
				var getGenderValue = function( gender ) {
					if( gender == "MALE" ) return "${ _strings.gender_male }";
					if( gender == "FEMALE" ) return "${ _strings.gender_female }";
					if( gender == "OTHER" ) return "${ _strings.gender_other }";
				};
				self.gender( author.gender );
				if( author.gender != null ) {
					$( "#pratilipi-settings-gender" ).attr( 'data-val', author.gender );
                    $( "#pratilipi-settings-gender" ).attr( 'value', getGenderValue( author.gender ) );
				}
				var getDateOfBirthValue = function( date ) {
					if( date == null ) return null;
					var d = new Date( date.replace( /(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3") ),
						month = '' + ( d.getMonth() + 1 ),
						day = '' + d.getDate(),
						year = d.getFullYear();
						if (month.length < 2) month = '0' + month;
						if (day.length < 2) day = '0' + day;
						return [ year, month, day ].join( '-' );
				};
				self.dateOfBirth( getDateOfBirthValue( author.dateOfBirth ) );
				self.authorPageUrl = author.pageUrl;

				/* Loading User Info */
				if( self.isAdmin ) {
					dataAccessor.getUserById( author.user.userId,
						function( user ) {
							originalUser = user;
							self.email( user.email );
							self.phone( user.phone );
					});
				} else {
					originalUser = { "email": appViewModel.user.email(), "phone": appViewModel.user.phone() };
					self.email( appViewModel.user.email() );
					self.phone( appViewModel.user.phone() );
				}
				self.isLoading( false );

		});
	};

	this.updateProfileSettings = function() {
		/* Validity Checks */
		if( self.email() != null && self.email().trim() > 0 && ! validateEmail( self.email() ) ) {
			ToastUtil.toast( "${ _strings.android_email_incorrect }" );
			return;
		}
		if( self.phone() != null && self.phone().trim() > 0 && ! validatePhone( self.phone() ) ) {
			ToastUtil.toast( "${ _strings.android_phone_incorrect }" );
			return;
		}

		var _getVal = function( val ) {
			if( val == null || val.trim() == "" )
				return "";
			return val;
		};

		var author = { "authorId": getAuthorId(), "firstName": self.firstName(), "language": self.language() };
		author[ "lastName" ] = _getVal( self.lastName() );
		author[ "firstNameEn" ] = _getVal( self.firstNameEn() );
		author[ "lastNameEn" ] = _getVal( self.lastNameEn() );
		author[ "penName" ] = _getVal( self.penName() );
		author[ "summary" ] = _getVal( self.summary() );
		author[ "gender" ] = _getVal( self.gender() );
		if( self.dateOfBirth() ) author[ "dateOfBirth" ] = formatDateOfBirth( self.dateOfBirth() );

		/* 4 states: INITIAL, LOADING, SUCCESS, FAIL */
		self.userApiCallState = ko.observable( "INITIAL" );
		self.authorApiCallState = ko.observable( "INITIAL" );
		var errorMessage = null; /* In case UserApi Or AuthorApi failed. */

		self.callbackObserver = ko.computed( function() {
			if( self.userApiCallState() == "SUCCESS" && self.authorApiCallState() == "SUCCESS" ) {
				ToastUtil.toast( "${ _strings.success_generic_message }" );
				self.isLoading( false );
				self.userApiCallState( "INITIAL" );
				self.authorApiCallState( "INITIAL" );
				if( self.isAdmin ) {
					/* Redirect to the author page */
					redirect( self.authorPageUrl );
				}
			} else if( self.userApiCallState() == "LOADING" || self.authorApiCallState() == "LOADING" ) {
				return;
			} else if( self.userApiCallState() == "FAILED" || self.authorApiCallState() == "FAILED" ) {
				if( self.userApiCallState() == "FAILED" )
				ToastUtil.toast( errorMessage ); /* Ensure errorMessage field is set */
				errorMessage = null;
				self.isLoading( false );
			}
		}, self );

		self.isLoading( true );
		ToastUtil.toastUp( "${ _strings.working }" );

		self.userApiCallState( "LOADING" );
		dataAccessor.createOrUpdateUser( getUserId(), self.email(), self.phone(),
			function( user ) {
				self.userApiCallState( "SUCCESS" );
			}, function( error ) {
				var message = "${ _strings.server_error_message }";
				if( error[ "email" ] != null ) message = error[ "email" ];
				if( error[ "phone" ] != null ) message = error[ "phone" ];
				if( error[ "message" ] != null ) message = error[ "message" ];
				errorMessage = message;
				self.userApiCallState( "FAILED" );
		});

		self.authorApiCallState( "LOADING" );
		dataAccessor.createOrUpdateAuthor( author,
			function( aAuthor ) {
				self.authorPageUrl = aAuthor.pageUrl;
				self.authorApiCallState( "SUCCESS" );
			}, function( error ) {
				var message = "${ _strings.server_error_message }";
				if( error[ "language" ] != null ) message = error[ "language" ];
				if( error[ "message" ] != null ) message = error[ "message" ];
				errorMessage = message;
				self.authorApiCallState( "FAILED" );
		});
	};

	this.canSaveProfileSettings = ko.computed( function() {
		return self.firstName() != null && self.firstName().trim() != "" &&
				self.language() != null && self.language().trim() != "" &&
				! self.isLoading() &&
				(
					self.firstName() != originalAuthor.firstName ||
					self.lastName()  != originalAuthor.lastName ||
					self.firstNameEn()  != originalAuthor.firstNameEn ||
					self.lastNameEn()  != originalAuthor.lastNameEn ||
					self.penName()  != originalAuthor.penName ||
					self.language()  != originalAuthor.language ||
					self.summary()  != originalAuthor.summary ||
					self.email()  != originalUser.email ||
					self.phone()  != originalUser.phone ||
					self.gender()  != originalAuthor.gender ||
					formatDateOfBirth( self.dateOfBirth() ) != originalAuthor.dateOfBirth
				);
	}, this );

	this.userObserver = ko.computed( function() {
		if( appViewModel.user.userId() == null ) return;
		if( appViewModel.user.isGuest() && appViewModel.user.userId() == 0 )
			goToLoginPage();

		setTimeout( function() {
			self.loadUserAndAuthor();
		}, 0 );

	}, this );


	/* NOTIFICATION SETTINGS */
	var originalEmailFrequency = null;
	var originalNotificationSubscriptions = null;

	this.emailFrequency = ko.observable();
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
		userPreferences[ "emailFrequency" ] = document.querySelector( "#pratilipi-settings-email-frequency" ).getAttribute( "data-val" );
		userPreferences[ "notificationSubscriptions" ] = {};
		$( '#notification-settings .mdl-js-checkbox' ).each( function( index, element ) {
			userPreferences[ "notificationSubscriptions" ][ element.firstChild.value ] = element.firstChild.checked;
		});
		setUserPreferences( userPreferences );
		ToastUtil.toast( "${ _strings.success_generic_message }" );
	};

	this.firebaseCallback = ko.computed( function() {
		var userPreferences = appViewModel.userPreferences();

		setTimeout( function() {
			self.emailFrequency( userPreferences[ "emailFrequency" ] != null ? userPreferences[ "emailFrequency" ] : "IMMEDIATELY" );
			$( "#pratilipi-settings-email-frequency" ).attr( 'data-val', self.emailFrequency() );
			$( "#pratilipi-settings-email-frequency" ).attr( 'value', getEmailFrequencyVernacular( self.emailFrequency() ) );
			originalEmailFrequency = self.emailFrequency();

			var notificationSubscriptions = userPreferences[ "notificationSubscriptions" ] != null ?
					userPreferences[ "notificationSubscriptions" ] : {};
			$( '#notification-settings .mdl-js-checkbox' ).each( function( index, element ) {
				if( element.MaterialCheckbox == null ) return;
				var val = element.firstChild.value;
				if( notificationSubscriptions[ val ] == null ) notificationSubscriptions[ val ] = true;
				notificationSubscriptions[ val ] ? element.MaterialCheckbox.check() : element.MaterialCheckbox.uncheck();
			});
			originalNotificationSubscriptions = notificationSubscriptions;

		}, 0 );

	}, this );

	this.randomVariable = ko.observable( 0 ); /* Hack: To execute computed */
	$( '#notification-settings .mdl-js-checkbox' ).click( function() { self.randomVariable( self.randomVariable() + 1 ); } );

	this.canSaveNotificationSettings = ko.computed( function() {
		self.randomVariable(); self.emailFrequency();
		if( originalNotificationSubscriptions == null ) return;
		var canSave = false;
	    $( '#notification-settings .mdl-js-checkbox' ).each( function( index, element ) {
	        if( originalNotificationSubscriptions[ element.firstChild.value ] != element.firstChild.checked ) {
	            canSave = true;
	            return;
	        }
        });
		return canSave || self.emailFrequency() != originalEmailFrequency;
	}, this );

	this.updateEmailFrequency = function() {
		self.emailFrequency( $( "#pratilipi-settings-email-frequency" ).attr( "data-val" ) );
	};


	/* PASSWORD SETTINGS */
	this.currentPassword = ko.observable();
	this.newPassword = ko.observable();
	this.confirmPassword = ko.observable();

	this.updatePassword = function() {
		if( self.newPassword() != self.confirmPassword() ) {
			ToastUtil.toast( "Passwords doesn't match!" );
			return;
		}
		if( self.isLoading() ) return;
		self.isLoading( true );
		ToastUtil.toastUp( "${ _strings.working }" );
		dataAccessor.updateUserPassword( self.currentPassword(), self.newPassword(),
			function( user ) {
				self.currentPassword( null );
				self.newPassword( null );
				self.confirmPassword( null );
				self.isLoading( false );
				ToastUtil.toast( "${ _strings.success_generic_message }" );
			}, function( error ) {
				self.isLoading( false );
				var message = "${ _strings.server_error_message }";
				if( error[ "password" ] != null ) message = error[ "password" ];
				if( error[ "newPassword" ] != null ) message = error[ "newPassword" ];
				if( error[ "message" ] != null ) message = error[ "message" ];
				ToastUtil.toast( message );
		});
	};

	this.canUpdatePassword = ko.computed( function() {
		return self.currentPassword() != null && self.currentPassword() != "" &&
				self.newPassword() != null && self.newPassword() != "" &&
				self.confirmPassword() != null && self.confirmPassword() != "" &&
				! self.isLoading();
	}, this );


	/* LOGOUT */
	this.logoutUser = function() {
		if( self.isLoading() ) return;
		self.isLoading( true );
		ToastUtil.toastUp( "${ _strings.working }" );
		dataAccessor.logoutUser(
			function( user ) {
				ToastUtil.toastUp( "${ _strings.success_generic_message }" );
				window.location.href = "/";
			}, function( error ) {
				self.isLoading( false );
				ToastUtil.toast( error[ "message" ] != null ? error[ "message" ] : "${ _strings.server_error_message }" );
		});
	};

	/* Helpers */
	var alignRightPane = function() {
		var element = $( ".js-tabs-right-pane" );
		var width = element.width() - parseInt( element.css( "margin-left" ) ) - parseInt( element.css( "margin-right" ) );
		$( '.js-width-50' ).css( 'width', ( width / 2 ) > 300 ? '50%' : '100%' );
	};

	$( window ).resize( function() { alignRightPane(); });


	this.tabTitle = ko.observable();
	var setTabTitle = function() {
		setTimeout( function() {
			$( ".mdl-tabs__panel" ).each( function( index, element ) {
				if( $( element ).hasClass( "is-active" ) )
					self.tabTitle( $( element ).attr( 'data-name' ) );
			});
		}, 0 );
	};

	$( ".mdl-tabs__tab" ).on( 'click', function() { setTabTitle(); });

	$( document ).ready( function() { alignRightPane();	setTabTitle(); });

	/* Location Observer */
	this.locationObserver = ko.computed( function() {
        if( appViewModel.currentView() != LOCATION.SETTINGS.ko_element ) {
            /* Dispose all observers */
            self.canSaveProfileSettings.dispose();
            self.userObserver.dispose();
            self.firebaseCallback.dispose();
            self.canSaveNotificationSettings.dispose();
            self.canUpdatePassword.dispose();
            self.locationObserver.dispose();
            return;
        }
        setTimeout( function() {
            getmdlSelect.init( ".getmdl-select" );
        }, 0 );
    }, this );

}