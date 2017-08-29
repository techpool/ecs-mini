function( params ) { 

	var self = this;
	var editPratilipiDialog = $( "#pratilipi_edit_pratilipi" );
	var dropdown = $( "#pratilipi_edit_pratilipi #pratilipi_edit_pratilipi_type" );

	this.pratilipi = params.pratilipi;
	this.updatePratilipi = params.updatePratilipi;

	this.pratilipiId = ko.observable( null );
	this.title = ko.observable();
	this.titleEn = ko.observable();
	/* this.type = ko.observable(); */

	this.setToDefault = function() {
		self.pratilipiId( self.pratilipi.pratilipiId() );
        self.title( self.pratilipi.title() );
        self.titleEn( self.pratilipi.titleEn() );
        /* self.type( self.pratilipi.type() );
		dropdown.attr( 'data-val', self.pratilipi.type() );
        dropdown.attr( 'value', getPratilipiTypeVernacular( self.pratilipi.type() ) );
        dropdown.val( getPratilipiTypeVernacular( self.pratilipi.type() ) ); */
		getmdlSelect.init( ".getmdl-select" );
	};

	this.pratilipiObserver = ko.computed( function() {
		if( self.pratilipi.pratilipiId() == null ) return;
		setTimeout( function() {
			self.setToDefault();
		}, 0 );
	}, this );


	this.requestOnFlight = ko.observable( false );
	this.submit = function() {

		if( self.requestOnFlight() ) return;

		if( self.title().trim() == "" ) {
			ToastUtil.toast( "${ _strings.writer_error_title_required }" );
			return;
		}

		/* if( self.type() == null || self.type().trim() == "" ) {
			ToastUtil.toast( "${ _strings.writer_error_category_required }" );
			return;
		} */

		var successCallBack = function( pratilipi ) {
			ToastUtil.toast( "${ _strings.success_generic_message }" );
			self.updatePratilipi( pratilipi );
			self.requestOnFlight( false );
			self.closeEditPratilipi();
		};

		var errorCallBack = function( error, status ) {
			ToastUtil.toast( error.message != null ? error.message : "${ _strings.server_error_message }", 3000 );
			self.requestOnFlight( false );
		};

		ToastUtil.toastUp( "${ _strings.working }" );
		var pratilipi = { 
				"pratilipiId": self.pratilipiId(),
				"title": self.title(),
				"titleEn": self.titleEn()
				/* "type": self.type() */
		};

		self.requestOnFlight( true );
		var dataAccessor = new DataAccessor();
		dataAccessor.createOrUpdatePratilipi( pratilipi, successCallBack, errorCallBack );

	};

	/* this.updateType = function() {
		self.type( dropdown.attr( "data-val" ) );
	}; */

	this.canEditPratilipi = ko.computed( function() {
		return self.title() != null && self.title() != "" && ! self.requestOnFlight();
	}, this );

	this.closeEditPratilipi = function() {
		editPratilipiDialog.modal( "hide" );
		setTimeout( function() {
			self.setToDefault();
		}, 0);
	};

	editPratilipiDialog.on( 'hidden.bs.modal', function(e) {
		self.closeEditPratilipi();
	});

}