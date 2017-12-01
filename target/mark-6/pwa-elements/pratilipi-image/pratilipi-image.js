function( params ) {
	var self = this;

	this.src = typeof( params.src ) == "string" ? ko.observable( params.src ) : params.src;

	this.width = params.width;
	this.mobWidth = params.mobWidth != null ? params.mobWidth : self.width;

	this.height = params.height != null ? params.height : params.width;
	this.mobHeight = params.mobHeight != null ? params.mobHeight : self.height;

	this.imageCircle = params.imageCircle != null ? params.imageCircle : false;
	this.alt = params.alt != null ? params.alt : "${ _strings.pratilipi }";

	this.isSmallImageLoaded = ko.observable();
	this.isLargeImageLoaded = ko.observable();

	this._onSmallImageLoad = function() {
		self.isSmallImageLoaded( true );
	};

	this._onLargeImageLoad = function() {
		self.isLargeImageLoaded( true );
	};

	this.wt = ko.observable();
	this.ht = ko.observable();

	this.setDimension = function() {
		self.wt( $(window).width() < 480 ? self.mobWidth : self.width );
		self.ht( $(window).width() < 480 ? self.mobHeight : self.height );
	};

	/* Hack for mozilla - setTimeout executes after image is loaded. need to call this method only after first time */
	var firstTime = true;
	this.srcObserver = ko.computed( function() {
		if( self.src() == null ) return;
		if( firstTime ) { firstTime = false; return; }
		setTimeout( function() {
			self.isSmallImageLoaded( false );
			self.isLargeImageLoaded( false );
		}, 0 );
	}, this );

	self.setDimension();
	$(window).on( 'resize', function() { self.setDimension(); } );

}