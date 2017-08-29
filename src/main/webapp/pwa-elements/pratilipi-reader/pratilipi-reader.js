function() {
	var self = this;
	var dataAccessor = new DataAccessor();

	var defaultPratilipi = {
			"pratilipiId": null,
			"title": null,
			"titleEn": null,
			"language": null,
			"author": { "authorId": null,"name": null, "pageUrl": null },
			"summary": null,
			"pageUrl": null,
			"coverImageUrl": null,
			"readPageUrl": null,
			"writePageUrl": null,
			"type": null,
			"contentType": null,
			"oldContent": false,
			"index": null,
			"pageCount": null,
			"state": null,
			"listingDateMillis": null,
			"wordCount": null,
			"reviewCount": null,
			"ratingCount": null,
			"averageRating": null,
			"readCount": null,
			"fbLikeShareCount": null,
			"hasAccessToUpdate": false
	};

	var defaultUserPratilipi = {
			"userPratilipiId": null,
			"userId": null,
			"pratilipiId": null,
			"userName": null,
			"userImageUrl": null,
			"userProfilePageUrl": null,
			"rating": 0,
			"review": null,
			"reviewState": null,
			"addedToLib": false,
			"hasAccessToReview": true,
			"isLiked": false,
	};

	this.updatePratilipi = function( pratilipi, initialiseObject ) {
		var updatedPratilipi = JSON.parse( JSON.stringify( initialiseObject ? defaultPratilipi : pratilipi ) );
		if( initialiseObject && pratilipi != null ) {
			if( pratilipi.summary != null && pratilipi.summary.trim() == "" )
				pratilipi.summary = null;
			for( var key in pratilipi )
				if( pratilipi.hasOwnProperty( key ) )
					updatedPratilipi[ key ] = pratilipi[ key ];
			if( pratilipi.author != null ) {
				for( var key in pratilipi.author )
					if( pratilipi.author.hasOwnProperty( key ) )
						updatedPratilipi.author[ key ] = pratilipi.author[ key ];
			}
		}
		ko.mapping.fromJS( updatedPratilipi, {}, self.pratilipi );
		MetaTagUtil.setMetaTagsForPratilipi( ko.mapping.toJS( self.pratilipi ) );
	};

	this.updateUserPratilipi = function( userPratilipi, initialiseObject ) {
		var updatedUserPratilipi = JSON.parse( JSON.stringify( initialiseObject ? defaultUserPratilipi : userPratilipi ) );
		if( initialiseObject && userPratilipi != null ) {
			if( userPratilipi.rating == null )
				userPratilipi.rating = 0;
			if( userPratilipi.review != null && userPratilipi.review.trim() == "" )
				userPratilipi.review = null;
			if( userPratilipi.reviewState != null && userPratilipi.reviewState != "PUBLISHED" )
				userPratilipi.review = null;
			for( var key in userPratilipi )
				if( userPratilipi.hasOwnProperty( key ) )
					updatedUserPratilipi[ key ] = userPratilipi[ key ];
		}
		ko.mapping.fromJS( updatedUserPratilipi, {}, self.userPratilipi );
	};

	this.setChapterNo = function( chapterNo ) { /* Backward Compatibility */
		chapterNo = parseInt( chapterNo );
		if( chapterNo < 1 || chapterNo > self.chapterCount() ) return;
		self.chapterNo( chapterNo );
		if( self.scrollTop() > 100 ) { self.scrollToTop( 0 ); }
		setCookie( "reader_page_number_" + self.pratilipi.pratilipiId(), chapterNo, 30, "/read" );
	};


	/* Font Sizes */
	var defaultFontSize = 18;
	this.minFontSize = 12;
	this.maxFontSize = 32;

	/* Image Size */
	this.minImageSize = 300;
	this.maxImageSize = 1500;

	this.getFontSize = function() {
		if( getCookie( "fontSize" ) != null )
			return parseInt( getCookie( "fontSize" ) );
		return defaultFontSize;
	};

	this.setFontSize = function( fontSize ) {
		fontSize = parseInt( fontSize );
		if( fontSize > self.maxFontSize || fontSize < self.minFontSize ) return;
		appViewModel.readerFontSize( fontSize );
		setTimeout( function() { setCookie( "fontSize", fontSize, 30, "/read" ) }, 0 );
	};

	this.getImageSize = function() {
		if( getCookie( "imageSize" ) != null )
			return parseInt( getCookie( "imageSize" ) );
		return -1; /* -1 for undetermined */
	};

	this.setImageSize = function( imageSize ) {
		imageSize = parseInt( imageSize );
		if( imageSize > self.maxImageSize || imageSize < self.minImageSize ) return;
		appViewModel.readerImageSize( imageSize );
		setTimeout( function() { setCookie( "imageSize", imageSize, 30, "/read" ) }, 0 );
	};

	this.getTheme = function() { /* Backward Compatibility */
		if( getCookie( "PREFERRED_READING_MODE" ) == "NIGHT_MODE" ) return "NIGHT";
		if( getCookie( "PREFERRED_READING_MODE" ) == "SEPIA_MODE" ) return "SEPIA";
		return "NORMAL";
	};

	this.setTheme = function( theme ) { /* Backward Compatibility */
		appViewModel.readerTheme( theme );
		var mode = "NORMAL_MODE";
		if( theme == "NIGHT" ) mode = "NIGHT_MODE";
		if( theme == "SEPIA" ) mode = "SEPIA_MODE";
		setTimeout( function() { setCookie( "PREFERRED_READING_MODE", mode, 30, "/read" ) }, 0 );
	};

	this.getLineHeight = function() {
		if( getCookie( "lineHeight" ) != null )
			return getCookie( "lineHeight" );
		return "MEDIUM";
	};

	this.setLineHeight = function( lineHeight ) {
		appViewModel.readerLineHeight( lineHeight );
		setTimeout( function() { setCookie( "lineHeight", lineHeight, 30, "/read" ) }, 0 );
	};

	/* Variables */
	this.dataLoadedState = ko.observable(); /* 3 Possible states: LOADING, LOADED and FAILED */
	this.pratilipi = ko.mapping.fromJS( defaultPratilipi, {}, self.pratilipi );
	this.userPratilipi = ko.mapping.fromJS( defaultUserPratilipi, {}, self.userPratilipi );
	this.libraryRequestOnFlight = ko.observable( false );
	this.chapterNo = ko.observable();
	this.chapterCount = ko.observable();
	this.index = ko.observableArray();

	this.initialiseReader = function() {
		/* Setting up Reader */
		if( appViewModel.readerFontSize() == null )
			appViewModel.readerFontSize( self.getFontSize() );
		if( appViewModel.readerImageSize() == null )
			appViewModel.readerImageSize( self.getImageSize() );
		if( appViewModel.readerTheme() == null )
			appViewModel.readerTheme( self.getTheme() );
		if( appViewModel.readerLineHeight() == null )
			appViewModel.readerLineHeight( self.getLineHeight() );

		/* Api call */
		self.dataLoadedState( "LOADING" );
		dataAccessor.getPratilipiAndIndex( getUrlParameter( "id" ),
			function( pratilipi, userPratilipi, indexResponse ) {

				if( pratilipi == null || indexResponse == null ) { /* No / Wrong / Drafted / Deleted pratilipiId */
					self.dataLoadedState( "FAILED" );
					return;
				}

				/* Updating properties */
				self.updatePratilipi( pratilipi, true );
				self.updateUserPratilipi( userPratilipi, true );

				/* Setting index and chapterCount */
				/* Two different versions of contents */
				var index = [];
				if( pratilipi.oldContent ) {
					if( pratilipi.index != null ) {
						index = JSON.parse( pratilipi.index );
						for( var i = 0; i < index.length; i++ ) {
							if( index[i].title == null )
								index[i].title = "${ _strings.writer_chapter } " + index[i].chapterNo;
							index[i].chapterNo = index[i].pageNo;
						}
					}
					self.chapterCount( pratilipi.pageCount > 0 ? pratilipi.pageCount : 1 );
				} else {
					index = indexResponse.index;
					for( var i = 0; i < index.length; i++ )
						if( index[i].title == null )
							index[i].title = "${ _strings.writer_chapter } " + index[i].chapterNo;
					self.chapterCount( index != null && index.length > 0 ? index.length : 1 );
				}
				self.index( index );

				/* set chapterNo */
				var chapterNo = 1;
				if( getUrlParameter( "chapterNo" ) != null )
					chapterNo = parseInt( getUrlParameter( "chapterNo" ) );
				if( getCookie( "reader_page_number_" + pratilipi.pratilipiId ) != null )
					chapterNo = parseInt( getCookie( "reader_page_number_" + pratilipi.pratilipiId ) );
				if( chapterNo < 1 ) chapterNo = 1; if( chapterNo > self.chapterCount() ) chapterNo = self.chapterCount();
				self.setChapterNo( chapterNo );
				self.dataLoadedState( "LOADED" );
		});
	};

	this.openSettings = function() {
		$( "#pratilipi-reader-settings" ).modal();
	};

	this.openNavigationModal = function() {
		if( !( typeof( componentHandler ) == 'undefined' ) ) {
			componentHandler.upgradeAllRegistered();
		}
		document.querySelector( '.mdl-layout' ).MaterialLayout.toggleDrawer();

	};

	this.switchLibraryState = function() {
		if( appViewModel.user.isGuest() ) {
			goToLoginPage( { "id": self.pratilipi.pratilipiId() }, { "message": "LIBRARY" } );
			return;
		}
		if( self.libraryRequestOnFlight() ) return;
		self.libraryRequestOnFlight( true );
		var getAddedToLibMessage = function( addedToLib ) {
			var text = addedToLib ? "${ _strings.library_add_success }" : "${ _strings.library_remove_success }";
			var title = self.pratilipi.title().substring( 0, 10 ) + ( self.pratilipi.title().length > 10 ? "..." : "" );
			return text.replace( "$pratilipiTitle", title );
		};
		var addedToLib = ! self.userPratilipi.addedToLib();
		if( addedToLib ) /* Toast only for remove action */
			ToastUtil.toast( getAddedToLibMessage( addedToLib ), 3000 );
		else
			ToastUtil.toastCallBack( getAddedToLibMessage( addedToLib ), 4000, "${ _strings.undo_action }", self.switchLibraryState );

		self.updateUserPratilipi( { "addedToLib": addedToLib } );
		dataAccessor.addOrRemoveFromLibrary( self.pratilipi.pratilipiId(), addedToLib,
			function( userPratilipi ) {
				self.libraryRequestOnFlight( false );
			}, function( error ) {
				self.libraryRequestOnFlight( false );
				self.updateUserPratilipi( { "addedToLib": ! addedToLib } );
				var message = "${ _strings.server_error_message }";
				if( error[ "message" ] != null )
					message = error[ "message" ];
				ToastUtil.toast( message, 3000, true );
		});
	};

	this.sharePratilipi = function() {
		ShareUtil.sharePratilipi( ko.mapping.toJS( self.pratilipi ) );
	};

	/* Settings */
	this.increaseFontSize = function() { self.setFontSize( appViewModel.readerFontSize() + 2 ); };
	this.decreaseFontSize = function() { self.setFontSize( appViewModel.readerFontSize() - 2 ); };
	this.increaseImageSize = function() { self.setImageSize( appViewModel.readerImageSize() + 50 ); };
	this.decreaseImageSize = function() { self.setImageSize( appViewModel.readerImageSize() - 50 ); };

	this.setNormalTheme = function() { self.setTheme( "NORMAL" );};
	this.setNightTheme = function() { self.setTheme( "NIGHT" ); };
	this.setSepiaTheme = function() { self.setTheme( "SEPIA" ); };

	this.setSmallLineHeight = function() { self.setLineHeight( "SMALL" ) };
	this.setMediumLineHeight = function() { self.setLineHeight( "MEDIUM" ) };
	this.setLargeLineHeight = function() { self.setLineHeight( "LARGE" ) };

	/* Report Content */
	var reportContentModal = $( "#pratilipi-reader-report-content" );
	this.reportContentText = ko.observable( "" );
	this.reportContentRequestOnFlight = ko.observable( false );

	this.reportContent = function() {
		if( appViewModel.user.isGuest() ) {
			goToLoginPage( { "id": self.pratilipi.pratilipiId(), "action": "reportContent" } );
			return;
		}
		reportContentModal.modal( 'show' );
	};

	this.submitReportContent = function() {
		if( self.reportContentRequestOnFlight() ) return;
		self.reportContentRequestOnFlight( true );
		ToastUtil.toastUp( "${ _strings.working }" );
		dataAccessor.reportContent( self.reportContentText(),
			function( success ) {
				self.reportContentRequestOnFlight( false );
				ToastUtil.toast( "${ _strings.success_generic_message }" );
				self.closeReportContentModal();
			}, function( error ) {
				self.reportContentRequestOnFlight( false );
				ToastUtil.toast( error.message != null ? error.message : "${ _strings.server_error_message }" );
		});
	};

	this.canReportContent = ko.computed( function() {
		return self.reportContentText().trim() != '' && ! self.reportContentRequestOnFlight();
	}, this );

	this.closeReportContentModal = function() {
		self.reportContentText( "" );
		reportContentModal.modal( "hide" );
	};

	/* Clicking anywhere outside the screen */
	reportContentModal.on( 'hidden.bs.modal', function(e) {
		self.closeReportContentModal();
	});



	/* Navigate on Reader */
	this.keyupHandler = function( keyCode ) {
		/* Left => 37 ; Right => 39 */
		if( keyCode == 37 )
			self.setChapterNo( self.chapterNo() - 1 );
		else if( keyCode == 39 )
			self.setChapterNo( self.chapterNo() + 1 );
	};


	/* ProgressBar */
	var progressBar = null;
	this.setProgressBar = function() {
		if( progressBar == null ) return;
		var documentHeight = $( ".js-body-layout-content .js-body-layout-content-grid" ).height() + 16; /* 16 => Padding of js-body-layout-content-grid */
		var windowHeight   = $(window).height() - 54; /* 54 => Hardcoded height of header + scrollbar */
		var distanceToTop  = self.scrollTop();
		var percentScrolled = distanceToTop/(documentHeight - windowHeight) * 100;
		progressBar.setProgress( percentScrolled );
	};

	document.querySelector( '#reader-progress-bar' ).addEventListener( 'mdl-componentupgraded', function() {
		progressBar = this.MaterialProgress;
		progressBar.setProgress(0);
	});

	/* ScrollTop Handlers */
	this.scrollTop = ko.observable();
	this.scrollToTop = function() {
		$( ".js-body-layout-content" ).animate({ scrollTop: 0 }, 200 );
	};

	this.notifyOfScrollEvent = function() {
		self.scrollTop( $( ".js-body-layout-content" ).scrollTop() );
	};

	this.scrollTopObserver = ko.computed( function() {
		self.scrollTop();
		appViewModel.scrollTop( self.scrollTop() );
		setTimeout( function() { self.setProgressBar(); }, 0 );
	}, this );


	/* Left and Right keys */
	$(document).keyup( function(e) { self.keyupHandler( e.which ); });

	this.userObserver = ko.computed( function() {
		if( ! appViewModel.user.isGuest() && getUrlParameter( 'action' ) == "reportContent" ) {
			self.reportContent();
		}
	}, this );

	this.locationObserver = ko.computed( function() {
		jQuery.fn.removeAttributes = function() {
			return this.each(function() {
				var attributes = $.map(this.attributes, function(item) { return item.name; });
				var element = $(this);
				$.each(attributes, function(i, item) { element.removeAttr(item); });
			});
		};
		if( appViewModel.currentView() != LOCATION.READER.ko_element ) {
			/* Dispose all observers */
			$('body').removeAttributes();
			self.scrollTopObserver.dispose();
			self.userObserver.dispose();
			self.locationObserver.dispose();
			self.canReportContent.dispose();
			return;
		}
		appViewModel.currentLocation();
		setTimeout( function() {
			self.initialiseReader();
		}, 0 );
	}, this );

	$( document ).ready( function() { $( document ).on( "contextmenu",function() { return false; }); $( document ).mousedown( function(e) { if( e.button == 2 ) return false; else return true; }); }); $(document).keyup(function(e){ if(e.keyCode == 44) return false; }); document.onkeydown = function(e) { var isCtrl = false; if(e.which == 17) isCtrl = true; if(( ( e.which == 67 ) || ( e.which == 80 ) ) && isCtrl == true) return false; };
	function clickIE4(){ if (event.button==2){ return false; } } function clickNS4(e){ if (document.layers||document.getElementById&&!document.all){ if (e.which==2||e.which==3){ return false; } } } if (document.layers){ document.captureEvents(Event.MOUSEDOWN); document.onmousedown=clickNS4; } else if (document.all&&!document.getElementById){ document.onmousedown=clickIE4; } document.oncontextmenu=new Function( "return false" );
	$('body').attr('ondragstart', 'return false'); $('body').attr('onselectstart', 'return false'); $('body').attr('onContextMenu', 'return false'); $('body').attr('onkeydown', 'if ((arguments[0] || window.event).ctrlKey) return false');

}
