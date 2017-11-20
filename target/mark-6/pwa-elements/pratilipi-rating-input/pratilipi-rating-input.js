function( params ) {
	var self = this;

	/* Pass rating in parameter as an ko.observable */
	this.rating = params.rating;

	this.setRating = function( rating ) {
		if( rating == null ) self.rating( 0 );
		if( rating < 0 ) self.rating( 0 );
		if( rating > 5 ) self.rating( 5 );
		self.rating( rating );
		ga_CA( 'Review', 'Star Rating' );
	}

}
