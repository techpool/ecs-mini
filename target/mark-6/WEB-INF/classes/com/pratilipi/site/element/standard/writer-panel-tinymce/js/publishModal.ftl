var PublishModal = function ( publish_modal_container ) {
    this.$publish_modal_container = publish_modal_container;
    this.$form = this.$publish_modal_container.find( "#publish_form" );
    this.$category_select = this.$form.find( "#category" );
    this.$image_container = this.$form.find( ".image-container" );
    this.$summary = this.$form.find( "#summary" );
    this.form_validated = true;
    this.pratilipi_data = ${ pratilipiJson };
    this.user_data = ${userJson};
    console.log(this.pratilipi_data);
    console.log(this.user_data);
};

PublishModal.prototype.init = function() {
    <#-- this.generateCategoryOptions(); -->
    this.hideCoverImageForm();
    this.prepopulateBookDetails();
    this.attachCoverImageListeners();
    this.attachFormSubmitListener();
};

PublishModal.prototype.setBookName = function( name ) {
    var book_name;
    if( name ) {
        book_name = name;
    }
    else {
        book_name = this.pratilipi_data.title ? this.pratilipi_data.title : this.pratilipi_data.titleEn;
    }
    this.$publish_modal_container.find( '[data-behaviour="publish_book_name"]' ).text( book_name );
};

PublishModal.prototype.generateCategoryOptions = function() {
    var _this = this;
    var category_map = ${ pratilipiTypesJson };
    $.each( category_map, function( key, value ) {
        var $option = $( "<option>", {
            value: key,
        } ).html( value["name"] );
        _this.$category_select.append( $option );
    });
};

PublishModal.prototype.hideCoverImageForm = function() {
    $( "#uploadPratilipiImageInput" ).hide();
};

PublishModal.prototype.prepopulateBookDetails = function() {
    this.setBookName();
    <#-- if ( this.pratilipi_data.type ) {
        this.$category_select.val( this.pratilipi_data.type );
    } -->
    
    if( this.pratilipi_data.coverImageUrl ) {
        this.lastCoverUrl = this.pratilipi_data.coverImageUrl;
        this.$image_container.find( ".cover-image" ).attr( "src", this.lastCoverUrl );
    }
    
    if( this.pratilipi_data.summary ) {
        this.$summary.val( this.pratilipi_data.summary );
    }
    
};

PublishModal.prototype.attachCoverImageListeners = function() {
    var _this = this;
    this.$image_container.on( 'click', function() {
        $( "#uploadPratilipiImageInput" ).trigger( 'click' );
    });
        
    $( "#uploadPratilipiImageInput" ).on( 'change', function() {
        $( "#uploadPratilipiImage" ).submit();
    });
        
    $( '#uploadPratilipiImage' ).on( 'submit',function(e) {
        e.preventDefault();
        var blob = $( this ).find( "#uploadPratilipiImageInput" ).get( 0 ).files[0];
        /* didItResize will be true if it managed to resize it, otherwise false (and will return the original file as 'blob') */
        var $img = _this.$image_container.find( ".cover-image" );
        $img.attr( "src", window.URL.createObjectURL( blob ) ).addClass( "blur-image" );
        var fd = new FormData();
        fd.append( 'data', blob );
        fd.append( 'pratilipiId', ${ pratilipiId?c } );    
        
        $.ajax( {
            type:'POST',
            url: '/api/pratilipi/cover?pratilipiId=${ pratilipiId?c }',
            data:fd,
            cache:true,
            contentType: false,
            processData: false,
            success:function( data ) {
                var parsed_data = jsonParseTryCatch(data);
                var image_url = parsed_data.coverImageUrl;
                $img.attr( "src", image_url ).removeClass( "blur-image" );
                $( "#uploadPratilipiImageInput" ).val( "" );
                _this.lastCoverUrl = image_url;
            },
            error: function( data ) {
                $img.removeClass( "blur-image" ).attr( "src", _this.lastCoverUrl );
            }
        });
    });                                    
};

PublishModal.prototype.attachFormSubmitListener = function() {
    var _this = this;
    this.$form.on( "submit", function(e) {
        e.preventDefault();
        <#-- _this.validateForm(); -->
        _this.ajaxSubmitForm();
    } );
};

<#-- PublishModal.prototype.validateForm = function() {
    this.resetErrorStates();
    if( this.isEmptyStr( this.$category_select.val() ) ) {
        this.$category_select.closest( ".form-group" ).addClass( "has-error" );
        this.$category_select.after( '<span class="error-exclamation glyphicon glyphicon-exclamation-sign form-control-feedback" style="right: 5%;" aria-hidden="true"></span>' );
        this.form_validated = false;
    }
    
    if( this.form_validated ) {
        this.ajaxSubmitForm();
    }
    
}; -->

PublishModal.prototype.resetErrorStates = function() {
    this.form_validated = true;
    this.$form.find( ".has-error" ).removeClass( "has-error" );
    this.$form.find( ".error-exclamation" ).remove();
    
};

PublishModal.prototype.ajaxSubmitForm = function() {
    var $spinner = $( "<div>" ).addClass( "spinner" );
    this.$publish_modal_container.find( ".modal-content" ).append( $spinner );
    var _this = this;
    var ajax_data = {
        pratilipiId: ${ pratilipiId?c },
        type: this.$category_select.val() ,
        summary: this.$summary.val(),
        state: "PUBLISHED"
    };
    $.ajax( {
        type: "POST",
        url: "/api/pratilipi?_apiVer=2",
        data: ajax_data,
        success:function( response ) {
            var parsed_data = jsonParseTryCatch( response );
            isSuccess = 1;
            /* FB Event for finished Writing */
			var fbEvents = new FBEvents();
			fbEvents.logEvent('PUBLISH_CONTENT', _this.pratilipi_data.state, null, null, null, null, null, 1);

			/*  START
			    Dev : Rahul Ranjan
			    Function : create sessionStorage entry which is used to show share popup on content page.
			*/
            var storage = localStorage;
            if (typeof(Storage) !== "undefined") {
			    if (_this.pratilipi_data.author.authorId.toString() == _this.user_data.author.authorId.toString())
                    sessionStorage.publishType = "self";
                else
                    sessionStorage.publishType = "admin";

            }
            /* END */
            window.location.href = parsed_data.pageUrl;
        },
        fail: function( response ) {
            var message = jsonParseTryCatch( response.responseText );
            alert( message );
            /* FB Event for finished Writing */
			var fbEvents = new FBEvents();
			fbEvents.logEvent('PUBLISH_CONTENT', _this.pratilipi_data.state, null, null, null, null, null, 0);
        },
        complete: function() {
            _this.$publish_modal_container.find( ".spinner" ).remove();
        }                                        
    });     
};

PublishModal.prototype.isEmptyStr = function( str ) {
    return ( str==null || str.length === 0 || !str.trim() );
};