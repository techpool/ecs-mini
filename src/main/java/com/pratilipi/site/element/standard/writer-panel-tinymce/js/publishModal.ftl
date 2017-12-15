var PublishModal = function ( publish_modal_container ) {
    this.$publish_modal_container = publish_modal_container;
    this.$form = this.$publish_modal_container.find( "#publish_form" );
    this.$category_select = this.$form.find( "#category" );
    this.$image_container = this.$form.find( ".image-container" );
    this.$upload_image_button = this.$form.find( ".upload-image-button" );
    this.$summary = this.$form.find( "#summary" );
    this.form_validated = true;
    this.shouldBeAnUpdateBookCoverEvent = true;
    this.pratilipi_data = ${ pratilipiJson };
    this.systemCategoriesJson = ${ tagsJson };
    this.user_data = ${userJson};
    this.userIsAnAEEE = false;

    console.log(this.pratilipi_data);
    console.log(this.user_data);
};

PublishModal.prototype.init = function() {
    <#-- this.generateCategoryOptions(); -->
    this.hideCoverImageForm();
    this.prepopulateBookDetails();
    this.attachCoverImageListeners();
    this.attachFormSubmitListener();
    this.attachGetRecommendedImagesListener();
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

        if (!this.lastCoverUrl.endsWith('/cover')) {
            this.$image_container.html( '<img class="cover-image" src="' + this.lastCoverUrl + '" alt="' + this.pratilipi_data.title + '" style="margin: 0;width: 167px;height: 250px;">' );
        } else {
            this.shouldBeAnUpdateBookCoverEvent = false;
        }
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

    this.$upload_image_button.on( 'click', function() {
        $( "#uploadPratilipiImageInput" ).trigger( 'click' );
    });
        
    $( "#uploadPratilipiImageInput" ).on( 'change', function() {
        $( "#uploadPratilipiImage" ).submit();
    });
        
    $( '#uploadPratilipiImage' ).on( 'submit',function(e) {
        e.preventDefault();
        var blob = $( this ).find( "#uploadPratilipiImageInput" ).get( 0 ).files[0];
        /* didItResize will be true if it managed to resize it, otherwise false (and will return the original file as 'blob') */
        _this.$image_container.html('<img class="cover-image" alt="' + _this.pratilipi_data.title + '" style="margin: 0;width: 167px;height: 250px;">' );
        var $img = _this.$image_container.find( ".cover-image" );
        $img.attr( "src", window.URL.createObjectURL( blob ) ).addClass( "blur-image" );
        var fd = new FormData();
        fd.append( 'data', blob );
        fd.append( 'pratilipiId', ${ pratilipiId?c } );    
        
        _this.recommendedImageSource = null;
        if (_this.currentlySelectedThumbnail) {
            _this.currentlySelectedThumbnail.removeClass('selected');
            _this.currentlySelectedThumbnail.find('img').css('filter', 'unset');    
        }
        
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
        _this.ajaxUpdateSelectedRecommendedImageCount(function() {
            _this.ajaxSubmitForm();
        });

        var fbEvents = new FBEvents();
        if (!_this.shouldBeAnUpdateBookCoverEvent) {
            if (_this.recommendedImageSource) {
                fbEvents.logGrowthEvent('NEWBOOKINFO_BOOKCOVER_WRITER', 'PALBUM', 'WRITER', 'BOOKCOVER', 'NEWBOOKINFO', 'WPRC001A', _this.userIsAnAEEE );
            } else if (!_this.lastCoverUrl.endsWith('/cover')) {
                fbEvents.logGrowthEvent('NEWBOOKINFO_BOOKCOVER_WRITER', 'SELFUPLOAD', 'WRITER', 'BOOKCOVER', 'NEWBOOKINFO', 'WPRC001A', _this.userIsAnAEEE );
            }
        } else {
            if (_this.recommendedImageSource) {
                fbEvents.logGrowthEvent('UPDATEBOOKINFO_BOOKCOVER_WRITER', 'PALBUM', 'WRITER', 'BOOKCOVER', 'UPDATEBOOKINFO', 'WPRC001A', _this.userIsAnAEEE );
            } else {
                fbEvents.logGrowthEvent('UPDATEBOOKINFO_BOOKCOVER_WRITER', 'SELFUPLOAD', 'WRITER', 'BOOKCOVER', 'UPDATEBOOKINFO', 'WPRC001A', _this.userIsAnAEEE );
            }
        }
        fbEvents.logGrowthEvent('PUBLISHBOOK_BOOKCOVER_WRITER', null, 'WRITER', 'BOOKCOVER', 'PUBLISHBOOK', 'WPRC001A', _this.userIsAnAEEE );
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

PublishModal.prototype.ajaxUpdateSelectedRecommendedImageCount = function(callback) {
    var _this = this;
    if (_this.recommendedImageSource) {
        $.ajax( {
            type: "POST",
            url: "/api/coverimage-recommendation/cover/select",
            data: { imageUrl: _this.recommendedImageSource },
            success:function( response ) {
                console.log(response);
                callback();
            },
            error: function( response ) {
                console.log(response);
                callback();
            }                          
        });
    } else {
        callback();
    }
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

PublishModal.prototype.attachGetRecommendedImagesListener = function() {
    var _this = this;
    $( "#publishModal" ).on( "getRecommendedImages", function( event, selectedTagIds, contentType ) {
        getRecommendedImages(selectedTagIds, contentType, _this.systemCategoriesJson[contentType]);
    });

    var getRecommendedImages = function(selectedTagIds, contentType, availableTagIds) {
        var pratilipiType = contentType;
        var selectedCategoryEnglishName = [];

        availableTagIds.forEach(function(eachTag) {
            if (selectedTagIds.indexOf(String(eachTag.id)) > -1) {
                // var selectedCategory = eachTag.pageUrl.split('/').pop();
		var selectedCategory = eachTag.nameEn;
                selectedCategoryEnglishName.push(selectedCategory);
            }
        });

        console.log("/api/coverimage-recommendation/cover?categories=" + selectedCategoryEnglishName.join(','));
        var responseFromServer = {};
        $.ajax({
            type: "GET",
            url: "/api/coverimage-recommendation/cover?categories=" + selectedCategoryEnglishName.join(','),
            success: function( response ){
                console.log("Server call successful");
                console.log(response);
                responseFromServer = response;
            },
            error: function( response ) {
                console.log("Server call failed");
                console.log(response);
            },
            complete: function(xhr) {
                if (xhr.status > 400 && xhr.status < 500) {
                    _this.userIsAnAEEE = true;
                }
                var fbEvents = new FBEvents();
                fbEvents.logGrowthEvent('LANDED_BOOKCOVER_WRITER', null, 'WRITER', 'BOOKCOVER', 'LANDED', 'WPRC001A', _this.userIsAnAEEE);
                console.log('Complete event for the images');
                var recommendationResponse = responseFromServer;

                if (!recommendationResponse || !recommendationResponse.recommendations || recommendationResponse.recommendations.length === 0) {
                    $('.hide-on-fail').remove();
                    return;
                }

                setTimeout(function() {
                    $('.recommendation-image-list').scrollTop(0);
                }, 500);

                var recommendationImages = recommendationResponse.recommendations;
                var imageContainer = $('#publishModal .recommendation-image-list');

                imageContainer.html('');
                recommendationImages.forEach(function(eachImage, indexOfImage) {
                    imageContainer.append('\
                        <div class="img__wrap" style="margin: 5px;" >\
                            <img class="img__img cover-image" src="' + eachImage.split('.jpeg')[0] + '_thumbnail.jpeg' + '" id="' + indexOfImage + '-rec-image" crossorigin="anonymous" />\
                            <div class="img__description_layer">\
                                <p class="img__description">${ _strings.writer_upload }</p>\
                            </div>\
                        </div>');
                });

                $( "#1-rec-image" ).on('load', function() {
                    $('#publishModal .pratilipi-loading-state').remove();
                });
                $(".recommendation-image-list .img__wrap").on('click', function(){
                    
                    $(this).addClass('selected');
                    $(this).find('img').css('filter', 'brightness(0.3)');
                    
                    if (_this.currentlySelectedThumbnail) {
                        _this.currentlySelectedThumbnail.removeClass('selected');
                        _this.currentlySelectedThumbnail.find('img').css('filter', 'unset');    
                    }

                    _this.currentlySelectedThumbnail = $(this);

                    var imageSource = $(this).find('img').attr('src').split('_thumbnail')[0] + '.jpeg';
                    _this.recommendedImageSource = imageSource;
                    function loadXHR(url) {
                        return new Promise(function(resolve, reject) {
                            try {
                                var xhr = new XMLHttpRequest();
                                xhr.open("GET", url);
                                xhr.responseType = "blob";
                                xhr.onerror = function() {reject("Network error.")};
                                xhr.onload = function() {
                                    if (xhr.status === 200) {resolve(xhr.response)}
                                    else {reject("Loading error:" + xhr.statusText)}
                                };
                                xhr.send();
                            }
                            catch(err) {reject(err.message)}
                        });
                    }

                    var $img = $('.image-container');
                    $img.html( '<img class="cover-image" src="' + imageSource + '" alt="' + _this.pratilipi_data.title + '" style="margin: 0;width: 167px;height: 250px;">' );
                    loadXHR(imageSource).then(function(blob) {
                        var $img = $('.image_container .cover-image');
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
                                console.log('Image URL (From server): ', image_url);
                                $img.html( '<img class="cover-image" src="' + image_url + '" alt="' + _this.pratilipi_data.title + '" style="margin: 0;width: 167px;height: 250px;">' );
                                $( "#uploadPratilipiImageInput" ).val( "" );
                            },
                            error: function( data ) {
                                $img.removeClass( "blur-image" );
                            }
                        });
                    }).catch(function ( error_messsage ){
                        var fbEvents = new FBEvents();
                        fbEvents.logGrowthEvent('FAILURE_EVENT_GLOBAL', JSON.stringify(error_messsage), 'WRITER', 'BOOKCOVER', 'UPDATEBOOKINFO', 'WPRC001A');
                    });
                });
            }
        });
    };

};

PublishModal.prototype.isEmptyStr = function( str ) {
    return ( str==null || str.length === 0 || !str.trim() );
};
