var Editor = function ( editor_container, content_object ) {
    this.$editor_container = editor_container;
    this.content_object = content_object;
    this.icons_object = {"bold": {highlighted: "https://0.ptlp.co/resource-all/icon/svg/bold-red.svg", unhighlighted: "https://0.ptlp.co/resource-all/icon/svg/bold.svg"},
    "italic": {highlighted: "https://0.ptlp.co/resource-all/icon/svg/italic-red.svg", unhighlighted: "https://0.ptlp.co/resource-all/icon/svg/italic.svg"},
    "underline": {highlighted: "https://0.ptlp.co/resource-all/icon/svg/underline-red.svg", unhighlighted: "https://0.ptlp.co/resource-all/icon/svg/underline.svg"},
    "justifyLeft": {highlighted: "https://0.ptlp.co/resource-all/icon/svg/paragraph-left-red.svg", unhighlighted: "https://0.ptlp.co/resource-all/icon/svg/paragraph-left.svg"},
    "justifyCenter": {highlighted: "https://0.ptlp.co/resource-all/icon/svg/paragraph-center-red.svg", unhighlighted: "https://0.ptlp.co/resource-all/icon/svg/paragraph-center.svg"},
    "justifyRight": {highlighted: "https://0.ptlp.co/resource-all/icon/svg/paragraph-right-red.svg", unhighlighted: "https://0.ptlp.co/resource-all/icon/svg/paragraph-right.svg"},
    "blockquote": {highlighted: "https://0.ptlp.co/resource-all/icon/svg/quotes-left-red.svg", unhighlighted: "https://0.ptlp.co/resource-all/icon/svg/quotes-left.svg"},
    "createLink": {highlighted: "https://0.ptlp.co/resource-all/icon/svg/link-red.svg", unhighlighted: "https://0.ptlp.co/resource-all/icon/svg/link-505050.svg"},
    "insertImage": {highlighted: "https://0.ptlp.co/resource-all/icon/svg/trash-d0021b.svg", unhighlighted: "https://0.ptlp.co/resource-all/icon/svg/camera2.svg"} };
    this.$execCommandLinks = this.$editor_container.find(".execCommand");
    /* this.$alignmentLinks = this.$editor_container.find( '[data-role="alignment"]' ); */
    this.$urlModal = $('#urlModal');
    this.highlightEditorOptionsFlag = true;
};

Editor.prototype.init = function() {
    this.attachExecCommandListeners();
    this.attachBlockquoteListener();
    this.attachLinkListener();
    this.attachLinkSelectionListener();
    this.attachImageSelectionListener();
    this.addTextSelectionListener();
    this.addImageListener();
    /* this.addRemoveImageListener(); */
    this.removeEventListenersOnUrlModalHide();
};

Editor.prototype.attachExecCommandListeners = function() {
    var _this = this;
    this.$execCommandLinks.each(function( i ) {
        $(this).on("click", function(e) {
            e.preventDefault();
            if ( !( _this.content_object.isSelectionInsideElement("blockquote") && $(this).hasClass("alignment") ) ) {
                document.execCommand(this.id, false, null);
                _this.highlightEditorsOptions();
            }

        });
    });
};

Editor.prototype.attachBlockquoteListener = function() {
    var _this = this;
    this.$editor_container.find("#blockquote").on("click", function( e ) {
        e.preventDefault;
        _this.toggleBlockquote();
        _this.highlightBlockquoteOption();
    });
};


Editor.prototype.attachLinkListener = function() {
    var _this = this;
    this.$editor_container.find("#link").on("click", function( e ) {
        e.preventDefault;
        if( _this.content_object.itemIsLinked() ) {
            document.execCommand("unlink", false, null);
            _this.content_object.$content_container.find(".popover").remove();
        }
        else {
            _this.promptUrlBox();
        }
        
    });
};

Editor.prototype.saveSelection = function() {
    if (window.getSelection) {
        var sel = window.getSelection();
        if (sel.getRangeAt && sel.rangeCount) {
            return sel.getRangeAt(0);
        }
    } else if (document.selection && document.selection.createRange) {
        return document.selection.createRange();
    }
    return null;
};

Editor.prototype.restoreSelection = function(range) {
    if (range) {
        if (window.getSelection) {
            var sel = window.getSelection();
            sel.removeAllRanges();
            sel.addRange(range);
        } else if (document.selection && range.select) {
            range.select();
        }
    }
};

Editor.prototype.promptUrlBox = function() {
	var saved_selection = this.saveSelection();
	var _this = this;
	this.$urlModal.modal('show');
	this.$urlModal.find("#input_url").val("");
	this.$urlModal.find('form[data-behaviour="url_form"]').one('submit', function(e) {
		e.preventDefault();
	    var url = _this.$urlModal.find("#input_url").val();
	    _this.$urlModal.modal('hide');
	    _this.restoreSelection( saved_selection );
	    url = _this.addhttp( url );
	    _this.linkUrlToText( url );
    });

};

Editor.prototype.linkUrlToText = function(url) {
	if(url.length) {
	    document.execCommand( "createLink", false, url+ " " );
	}
};

Editor.prototype.attachLinkSelectionListener = function() {
    var _this = this;
    this.content_object.$content_container.on("selectstart", function() {
        $(document).one('mouseup', function() {
            if ( _this.content_object.itemIsLinked() ) {
                _this.$editor_container.find("#link img").attr( "src", _this.icons_object["createLink"]["highlighted"] );
            
                $( document ).one('mouseup', function() {
                    _this.$editor_container.find("#link img").attr( "src", _this.icons_object["createLink"]["unhighlighted"] );
                });

            }
        });
    });
};

Editor.prototype.attachImageSelectionListener = function() {
    var _this = this;
    var $add_image = this.$editor_container.find("#insertImage");
    var $remove_image = this.$editor_container.find("#removeImage");
    
    this.content_object.$content_container.on("click", "img.writer-image", function(e) {
    	$( this ).addClass("remove").focus();
    	_this.resetExecCommandIcons();
    	_this.setHighlightEditorOptionsFlag( false );
    });    
    this.content_object.$content_container.on("blur", "img.writer-image", function(e) {
    	_this.content_object.$content_container.find("img.remove").removeClass("remove");
    	_this.setHighlightEditorOptionsFlag( true );
    });    
   
};

Editor.prototype.addTextSelectionListener = function() {
    var _this = this;
    this.content_object.$content_container.on("mouseup keyup", function() {
    	if( _this.highlightEditorOptionsFlag ) {
        	_this.highlightEditorsOptions();
        }
    });
}; 

Editor.prototype.highlightEditorsOptions = function() {
    var _this = this;
    this.$execCommandLinks.each(function( i ) {
        var img_src = document.queryCommandState( this.id ) ? _this.icons_object[ this.id ]["highlighted"] : _this.icons_object[ this.id ]["unhighlighted"];
        $(this).find("img").attr( "src", img_src );
    });
    this.highlightBlockquoteOption();
};

Editor.prototype.setHighlightEditorOptionsFlag = function( flag ) {
	this.highlightEditorOptionsFlag = flag;
}; 

Editor.prototype.highlightBlockquoteOption = function() {
    var blockquote_img_src = this.content_object.isSelectionInsideElement( "blockquote" ) ? this.icons_object["blockquote"]["highlighted"] : this.icons_object[ "blockquote" ]["unhighlighted"];
    this.$editor_container.find( "#blockquote img" ).attr( "src", blockquote_img_src );
};

Editor.prototype.unhighlightBlockquoteOption = function() {
    var blockquote_img_src = this.icons_object["blockquote"]["unhighlighted"];
    this.$editor_container.find( "#blockquote img" ).attr( "src", blockquote_img_src );
};

Editor.prototype.toggleBlockquote = function() {
    if( this.content_object.isSelectionInsideElement( "p" ) ) {

        document.execCommand("formatBlock", false, "blockquote");
    }
    else {
        document.execCommand("formatBlock", false, "p");
    } 
    
    <#-- if( this.content_object.isSelectionInsideElement( "blockquote" ) ) {
		var $blockquote = $( this.content_object.getClosestElementToSelection("blockquote") );
		var ptext;
		var $block_p = $blockquote.find("p");
		if( $block_p.length ) {
			ptext = $block_p.html();
		}
		else {
			ptext = $blockquote.html();
		}
		var $p = $("<p>").html( ptext );
		$blockquote.replaceWith( $p );
    }
    else {
    	var ptarget = $( this.content_object.getClosestElementToSelection("p") );
    	if( ptarget ) {
    		var $p = $( ptarget );
	    	var $blockquote = $("<blockquote>").html( $p.html() );
	        $p.replaceWith( $blockquote );    		
    	}
    	else {
    		document.execCommand("formatBlock", false, "blockquote");
    	}

    } -->
};

Editor.prototype.addImageListener = function() {
	var _this = this;
	var $upload_image_select = $("#upload_images");
	this.$editor_container.find("#insertImage").on('click', function() {
		$upload_image_select.trigger('click');
	});
	$upload_image_select.on('change', function(evt) {
		    ImageTools.resize(this.files[0], {
		        width: 480, /* maximum width */
		        height: 480 /* maximum height */
		    }, function(blob, didItResize) {
		        /* didItResize will be true if it managed to resize it, otherwise false (and will return the original file as 'blob') */
		        var $current_element = $( _this.getSelectionStart() );
		        var $last_element;
		        if( $current_element.closest("p").length ) {
		        	$last_element =  $current_element.closest("p") ;
		        }
		        else if ( $current_element.closest("blockquote").length ) {
		        	$last_element =  $current_element.closest("blockquote") ;
		        }
		        else {
		        	$last_element = null;
		        }
				var $img = $("<img>").attr({
					"src": window.URL.createObjectURL(blob),
					"tabindex": -1,
				} ).addClass("writer-image").addClass("blur-image");
				if( isMobile() ) {
					$img.addClass("mobile-blur-image");
				}
				if( $last_element ) {
					$img.insertAfter( $last_element );
				}
				else {
					var $p = $("<p> &nbsp; </p>");
					_this.content_object.$content_container.append($p).append($img);
				}
				$("<p><br></p>").insertAfter($img);
				var cur_page = _this.content_object.parent_object.currChapter;
				var fd = new FormData();
					fd.append('data', blob);
					fd.append('pratilipiId', ${ pratilipiId?c } );  
					fd.append('pageNo', cur_page );
					
				$.ajax({
		            type:'POST',
		            url: '/api/pratilipi/content/image?pratilipiId=${ pratilipiId?c }&pageNo=' + cur_page,
		            data:fd,
		            cache:true,
		            contentType: false,
		            processData: false,
		            success:function(data){
		                var parsed_data = jQuery.parseJSON( data );
		                var image_name = parsed_data.name;
		                var $delete_icon = '<img class="show-cursor" src="https://0.ptlp.co/resource-all/icon/svg/trash.svg" data-behaviour="remove-image">';
		                var image_url = "/api/pratilipi/content/image?pratilipiId=${ pratilipiId?c }&name=" + image_name;
		                if( isMobile() ) {
		                	image_url += "&width=240";
		                }
		                $img.attr( "src", image_url ).attr( "name", image_name ).removeClass("blur-image").attr({
							"tabindex": "-1",
							'data-toggle': "popover",
							"data-placement": "top",
							"data-trigger": "focus",
							"data-html":"true",
							"data-content": $delete_icon,
							"data-template": '<div class="popover" role="tooltip" contenteditable="false"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
						}).popover();
		            	$upload_image_select.val("");
		            },
		            error: function(data){
		                $img.remove();
		                $upload_image_select.val("");
		            }
		        });
		    });
	});	    
	
};



Editor.prototype.getSelectionStart = function () {
   var node = document.getSelection().anchorNode;
   return (node.nodeType == 3 ? node.parentNode : node);
};

Editor.prototype.removeEventListenersOnUrlModalHide = function () {
 	var _this = this; 
	this.$urlModal.on('hidden.bs.modal', function (e) {
		_this.$urlModal.find('form[data-behaviour="url_form"]').unbind("submit");
	});
};

Editor.prototype.resetExecCommandIcons = function() {
	var _this = this;
	this.$execCommandLinks.each( function() {
		$(this).find("img").attr("src", _this.icons_object[this.id]["unhighlighted"]);
	});
	this.unhighlightBlockquoteOption();
};

Editor.prototype.addhttp = function (url) {
    if ( url.length && !url.match(/^http([s]?):\/\/.*/)) {
        url = "http://" + url;
    }
    return url;
};