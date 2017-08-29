function( params ) {
	var self = this;

	<#assign cookie_show_banner = "USER_NOTIFIED_APP_LAUNCHED">
	<#assign cookie_banner_clicked = "APP_LAUNCHED_CLICKED">
	<#assign cookie_banner_crossed = "APP_LAUNCHED_CROSSED">
	<#assign playstore_url="https://play.google.com/store/apps/details?id=com.pratilipi.mobile.android&referrer=utm_source%3Dpratilipi_main_web%26utm_medium%3Dweb_bottom_strip%26utm_campaign%3Dapp_download">

	var showBanner = getCookie( "${ cookie_show_banner }" ) == null || getCookie( "${ cookie_show_banner }" ) == "true";
	var click_count = getCookie( "${ cookie_banner_clicked }" ) == null ? 0 : parseInt( getCookie( "${ cookie_banner_clicked }" ) );
	var cross_count = getCookie( "${ cookie_banner_crossed }" ) == null ? 0 : parseInt( getCookie( "${ cookie_banner_crossed }" ) );


	this.stripVisible = ko.observable( showBanner );
	if( self.stripVisible() )
		ga_CA( 'app_download_strip', 'app_strip_show' );

	this.execLogic = function() {
		if( click_count >= 3 ) {
			setCookie( "${ cookie_show_banner }", false, 365, "/" );
			return;
		}
		if( click_count > 0 && click_count < 3 ) {
			if( cross_count > 2 )
				cross_count = 0;
			if( cross_count == 0 )
				setCookie( "${ cookie_show_banner }", false, 3, "/" );
			if( cross_count == 1 )
				setCookie( "${ cookie_show_banner }", false, 7, "/" );
			if( cross_count == 2 )
				setCookie( "${ cookie_show_banner }", false, 30, "/" );
		}
		else {
			if( cross_count < 3 )
				setCookie( "${ cookie_show_banner }", false, null, "/" );
			if( cross_count >= 3 && cross_count < 6)
				setCookie( "${ cookie_show_banner }", false, 2, "/" );
			if( cross_count >= 6 )
				setCookie( "${ cookie_show_banner }", false, 7, "/" );
		}

		setCookie( "${ cookie_banner_clicked }", click_count, 365, "/" );
		setCookie( "${ cookie_banner_crossed }", cross_count, 365, "/" );

	};

	this.androidBannerCrossed = function() {
		cross_count++;
        self.execLogic();
        ga_CA( 'app_download_strip', 'app_strip_close' );
        self.stripVisible( false );
        return false;
    };

    this.androidBannerClicked = function() {
        click_count++;
        self.execLogic();
        self.stripVisible( false );
        ga_CA( 'app_download_strip', 'app_strip_click' );
        var newtab = window.open( '', '_blank' );
        newtab.location = "${ playstore_url }";
        return false;
    };

	this.stripVisibleObserver = ko.computed( function() {
		self.stripVisible();
		setTimeout( function() {
			var pratilipiHeader = $( ".js-pratilipi-header" );
			if( pratilipiHeader == null ) return;
			if( ! self.stripVisible() ) {
				pratilipiHeader.css( "margin-top", 0 );
				return;
			}
			var androidStrip = $( ".js-pratilipi-android-strip" );
			pratilipiHeader.css( "margin-top", ( androidStrip.height() +
                                parseInt( androidStrip.css( "padding-top" ) ) +
                                parseInt( androidStrip.css( "padding-bottom" ) ) ) + "px" );
		}, 0);
	}, this );
}