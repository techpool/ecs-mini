function( params ) { 
	var self = this;
	this.title = ko.observable( '' );
	this.titleEn = ko.observable( '' );
	this.type = ko.observable( null );
	this.agreedTerms = ko.observable( false );
	this.requestOnFlight = ko.observable( false );

	this.submit = function() {

		if( self.requestOnFlight() ) return;

		if( self.title().trim() == "" ) {
			ToastUtil.toast( "${ _strings.writer_error_title_required }" );
			return;
		}

		if( self.type() == null || self.type().trim() == "" ) {
			ToastUtil.toast( "${ _strings.writer_error_category_required }" );
			return;
		}

		if( ! self.agreedTerms() ) {
			ToastUtil.toast( "${ _strings.writer_error_copyright_required }" );
			return;
		}

		var successCallBack = function( pratilipi ) {
			ToastUtil.toastUp( "${ _strings.success_generic_message }" );
			window.location.href = pratilipi.writePageUrl;
		};

		var errorCallBack = function( error, status ) {
			ToastUtil.toast( error.message != null ? error.message : "${ _strings.server_error_message }" );
			self.requestOnFlight( false );
		};

		ToastUtil.toastUp( "${ _strings.working }" );
		self.requestOnFlight( true );
		var pratilipi = { 
				"title": self.title(),
				"titleEn": self.titleEn(),
				"language":  "${ language }",
				"type": self.type(),
				"state": "DRAFTED"
		};

		if( appViewModel.pratilipiWriteAuthorId() != null )
			pratilipi[ "authorId" ] = appViewModel.pratilipiWriteAuthorId();

		var dataAccessor = new DataAccessor();
		dataAccessor.createOrUpdatePratilipi( pratilipi, successCallBack, errorCallBack );

	};

	this.updateType = function() {
		self.type( document.querySelector( '#pratilipiWrite #pratilipi_write_type' ).getAttribute( "data-val" ) );
	};

	this.canCreatePratilipi = ko.computed( function() {
		return self.title() != null && self.title() != "" && self.agreedTerms() && self.type() != null && ! self.requestOnFlight();
	}, this );


	$( "#pratilipiWrite" ).on( 'hidden.bs.modal', function(e) {
		appViewModel.pratilipiWriteAuthorId( null );
		self.title( null );
        self.titleEn( null );
        self.type( null );
        self.agreedTerms( false );
        $( '#pratilipiWrite #agree-terms-conditions' ).parent().removeClass( "is-checked" );
		$( '#pratilipiWrite #pratilipi_write_type' ).attr( "data-val", null );
		$( '#pratilipiWrite #pratilipi_write_type' ).attr( "value", null );
		$( '#pratilipiWrite #pratilipi_write_type' ).val( null );
		$( '#pratilipiWrite #pratilipi_write_type' ).parent().removeClass("is-dirty");
        getmdlSelect.init( ".getmdl-select" );
	});

}