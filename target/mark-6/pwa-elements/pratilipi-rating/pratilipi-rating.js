function( params ) {

	/* Pass rating in parameter as an ko.observable */
	this.rating = params.rating != null ? params.rating : ko.observable( 0 );

}
