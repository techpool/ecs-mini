function() {
	var self = this;

    this.highLightNavigationEntry = function() {
        $( ".js-navigation-drawer a" ).each( function( index, element ) {
            $( element ).css( "font-weight", "400" );
            if( window.location.pathname == "/search" ) {
                if( $( element ).attr( 'href' ) == "/search?q=" + appViewModel.searchQuery() )
                    $( element ).css( "font-weight", "700" );
            } else if( $( element ).attr( 'href' ) == window.location.pathname ) {
                $( element ).css( "font-weight", "700" );
            }
        });
    };

	this.navigationObserver = ko.computed( function() {
        appViewModel.searchQuery();
        appViewModel.currentLocation();
        setTimeout( function() {
            self.highLightNavigationEntry();
        }, 0 );
    }, this );

	$( ".js-navigation-drawer a" ).click( function() {
		document.querySelector( '.mdl-layout' ).MaterialLayout.toggleDrawer();
	});

}