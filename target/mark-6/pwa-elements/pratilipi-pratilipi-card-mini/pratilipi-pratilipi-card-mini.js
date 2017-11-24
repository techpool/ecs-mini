function( params ) {

	var self = this;
	this.pratilipi = params.pratilipi;

	this.openPratilipi = function( vm ) {
		if( this.pratilipi.meta.recommendationType() )
			ga_CAV( 'Pratilipi', 'OpenRecommendation-' + this.pratilipi.meta.recommendationType(), vm.pratilipi.ga_index() );
		else
			ga_CA( 'Pratilipi', 'Open' );
		return true;
	};

	this.titleDisplay = ko.computed( function() {
		return self.pratilipi.displayTitle ? self.pratilipi.displayTitle() : self.pratilipi.title();
	}, this );

}
