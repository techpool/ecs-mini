var FinalScreenWriterPanel = function (panel_container) {
    this.$panel_container = panel_container;
    this.$form = this.$panel_container.find("form");
    this.$category_select = this.$form.find("#category");
    this.$image_container = this.$form.find(".image-container");
    this.$summary = this.$form.find("#summary");
    this.form_validated = true;
    this.pratilipi_data = ${ pratilipiJson };
};

FinalScreenWriterPanel.prototype.init = function() {
	this.setBookName();
    this.generateCategoryOptions();
    this.hideCoverImageForm();
    this.prepopulateBookDetails();
    this.attachCoverImageListeners();
    this.attachFormSubmitListener();

};

FinalScreenWriterPanel.prototype.setBookName = function(){
	var book_name = this.pratilipi_data.title ? this.pratilipi_data.title : this.pratilipi_data.titleEn;
	this.$panel_container.find('[data-behaviour="book_name"]').text( book_name );
};

FinalScreenWriterPanel.prototype.generateCategoryOptions = function() {
	var _this = this;
	var category_map = ${ pratilipiTypesJson };
	$.each(category_map, function( key, value ) {
	  	var $option = $("<option>",{
	  		value: key,
	  	}).html( value["name"] );
	  	_this.$category_select.append( $option );
	});
};

FinalScreenWriterPanel.prototype.hideCoverImageForm = function() {
	$("#uploadPratilipiImageInput").hide();
};

FinalScreenWriterPanel.prototype.prepopulateBookDetails = function() {
	if ( this.pratilipi_data.type ) {
		this.$category_select.val( this.pratilipi_data.type );
	}
	
	if( this.pratilipi_data.coverImageUrl ) {
		this.lastCoverUrl = this.pratilipi_data.coverImageUrl;
		this.$image_container.find( ".cover-image" ).attr( "src", this.lastCoverUrl );
	}
	
	if( this.pratilipi_data.summary ) {
		this.$summary.val( this.pratilipi_data.summary );
	}
	
};

FinalScreenWriterPanel.prototype.attachCoverImageListeners = function() {
	var _this = this;
    this.$image_container.on('click', function() {
    	$("#uploadPratilipiImageInput").trigger('click');
    });
    
    $("#uploadPratilipiImageInput").on('change', function() {
		$("#uploadPratilipiImage").submit();
	});
    
    $('#uploadPratilipiImage').on('submit',function(e) {
        e.preventDefault();
        var file = $(this).find("#uploadPratilipiImageInput").get(0).files[0];
		    ImageTools.resize( file, {
		        width: 480, /* maximum width */
		        height: 480 /* maximum height */
		    }, function(blob, didItResize) {
		        /* didItResize will be true if it managed to resize it, otherwise false (and will return the original file as 'blob') */
		        var $img = _this.$image_container.find(".cover-image");
		        $img.attr("src", window.URL.createObjectURL(blob) ).addClass("blur-image");
			    var fd = new FormData();
					fd.append('data', blob);
					fd.append('pratilipiId', ${ pratilipiId?c } );  
					
				$.ajax({
		            type:'POST',
		            url: '/api/pratilipi/cover?pratilipiId=${ pratilipiId?c }',
		            data:fd,
		            cache:true,
		            contentType: false,
		            processData: false,
		            success:function(data){
		                var image_url = jQuery.parseJSON( data ).coverImageUrl;
		                $img.attr( "src", image_url ).removeClass("blur-image");
		                $("#uploadPratilipiImageInput").val("");
		                _this.lastCoverUrl = image_url;
		            },
		            error: function(data){
		                $img.removeClass("blur-image").attr("src", _this.lastCoverUrl);
		            }
		        });    
		        /* you can also now upload this blob using an XHR. */
		    });
		});		               
};

FinalScreenWriterPanel.prototype.attachFormSubmitListener = function() {
	var _this = this;
	this.$form.on( "submit", function(e) {
		e.preventDefault();
		_this.validateForm();
	} );
};

FinalScreenWriterPanel.prototype.validateForm = function() {
	this.resetErrorStates();
	if( this.isEmptyStr( this.$category_select.val() ) ) {
		this.$category_select.closest(".form-group").addClass("has-error");
		this.$category_select.after('<span class="error-exclamation glyphicon glyphicon-exclamation-sign form-control-feedback" style="right: 5%;" aria-hidden="true"></span>');
		this.form_validated = false;
	}
	
	if( this.form_validated ) {
		this.ajaxSubmitForm();
	}
	
};

FinalScreenWriterPanel.prototype.resetErrorStates = function() {
	this.form_validated = true;
	this.$form.find(".has-error").removeClass("has-error");
	this.$form.find(".error-exclamation").remove();
	
};

FinalScreenWriterPanel.prototype.ajaxSubmitForm = function() {
	var ajax_data = {
			pratilipiId: ${ pratilipiId?c },
			type: this.$category_select.val() ,
    		summary: this.$summary.val(),
    		state: "PUBLISHED",
    		_apiVer: "2"
    	   };
    $.ajax({type: "POST",
        url: "/api/pratilipi",
        data: ajax_data,
        success:function(response){
        	var parsed_data = jQuery.parseJSON( response );
  			window.location.href = parsed_data.pageUrl;
		},
        fail:function(response){
        	var message = jQuery.parseJSON( response.responseText );
			alert(message);
		}			    		
		
});	
	
};

FinalScreenWriterPanel.prototype.isEmptyStr = function(str) {
	return ( str==null || str.length === 0 || !str.trim() );
};