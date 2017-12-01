function Timer( fn, t ) { /* http://stackoverflow.com/questions/8126466/javascript-reset-setinterval-back-to-0 */
	var timerObj = setInterval( fn, t );

	this.stop = function() {
		if( timerObj ) {
			clearInterval( timerObj );
			timerObj = null;
		}
		return this;
	};

	/* start timer using current settings (if it's not already running) */
	this.start = function() {
		if( !timerObj ) {
			this.stop();
			timerObj = setInterval( fn, t );
		}
		return this;
	};

	/* start with new interval, stop current interval */
	this.reset = function( newT ) {
		if( newT ) t = newT;
		return this.stop().start();
	};

}