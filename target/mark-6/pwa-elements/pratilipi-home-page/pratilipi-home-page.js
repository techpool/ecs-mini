function() {
	var self = this;
	var dataAccessor = new DataAccessor();

	this.bannerList = ko.observableArray();
	this.sectionList = ko.observableArray();

	this.fetchHomePageBanners = function() {
		dataAccessor.getHomePageBanners( function( response ) {
			if( response == null ) return;
			self.bannerList( response.bannerList );
			$( '#carousel' ).carousel({
				interval: 6000,
				pause: "hover"
			}).on( "touchstart", function( event ) {
				var xClick = event.originalEvent.touches[0].pageX;
				$(this).one( "touchmove", function( event ) {
					var xMove = event.originalEvent.touches[0].pageX;
					if( Math.floor( xClick - xMove ) > 5 ) {
						$(this).carousel( 'next' );
						$(this).carousel( 'pause' );
					} else if( Math.floor( xClick - xMove ) < -5 ) {
						$(this).carousel( 'prev' );
						$(this).carousel( 'pause' );
					}
				});
				$(this).on( "touchend", function() {
					$(this).off( "touchmove" );
				});
			});
		});
	};

	this.fetchHomePageSections = function() {
		dataAccessor.getHomePageSections( function( response ) {
			var newPratilipiList = function( pList ) {
				var pratilipiList = ko.observableArray();
				for( var i = 0; i < pList.length; i++ )
					pratilipiList.push( ko.mapping.fromJS( pList[i] ) );
				return pratilipiList;
			};
			var sectionList = response.sections;
			for( var i = 0; i < sectionList.length; i++ ) {
				var section = sectionList[i];
				section.pratilipiList = newPratilipiList( section.pratilipiList );
				self.sectionList.push( section );
			}
		});
	};

	this.initialDataLoaded = ko.computed( function() {
		return self.bannerList().length > 0 || self.sectionList().length > 0;
	}, this );

	this.dataLoaded = ko.computed( function() {
		return self.bannerList().length > 0 && self.sectionList().length > 0;
	}, this );

	this.locationObserver = ko.computed( function() {
		if( appViewModel.currentView() != LOCATION.HOME.ko_element ) {
			self.locationObserver.dispose();
			self.dataLoaded.dispose();
			return;
		}
		setTimeout( function() {
			if( getUrlParameter( "email" ) != null && getUrlParameter( "token" ) != null ) {
				if( getUrlParameter( "passwordReset" ) == "true" )
					redirect( "/reset-password?e=" + getUrlParameter( "email" ) + "&t=" + getUrlParameter( "token" ) );
				else if( getUrlParameter( "verifyUser" ) == "true" )
					redirect( "/verify-user?e=" + getUrlParameter( "email" ) + "&t=" + getUrlParameter( "token" ) );
			}

			self.fetchHomePageBanners();
			self.fetchHomePageSections();
		}, 0 );
	}, this );

}
