var Content = function (content_container, parent_object) {
    this.$content_container = content_container;
    this.parent_object = parent_object;
};

Content.prototype.init = function() {
    this.changeDefaultToParagraph();
    this.unformatPastedData();
    this.delegateTargetBlankToLinks();
    this.dismissPopoverOnClickingOutside();
    this.delegateRemoveImageListener();
};

Content.prototype.changeDefaultToParagraph = function() {
    var _this = this;
    <#-- this.$content_container.one("click", function() {
        document.execCommand('formatBlock', false, 'p');
    }); -->
    this.$content_container.on('keypress', function(ev){
        if( ev.keyCode == '13' && !_this.isSelectionInsideElement( "blockquote" ) ) {    
            document.execCommand('formatBlock', false, 'p');
            /* return false; */
        }
        else if( ev.keyCode == '13' && _this.isSelectionInsideElement( "blockquote" ) ) {
   <#--         document.execCommand('InsertParagraph');
            document.execCommand('Outdent'); -->
	      ev.preventDefault();
	      document.execCommand('insertParagraph', false);
	      document.execCommand('formatBlock', false, 'p');            
            return false;
        }
    });
};

Content.prototype.unformatPastedData = function() {
	var _this = this;
    this.$content_container.on("paste", function(e) {
        /* cancel paste */
        e.preventDefault();
		
        /* get text representation of clipboard */
        var text = e.originalEvent.clipboardData.getData("text/plain");
        /* insert text manually */
        var $closest_element = $( e.target ).closest("p,blockquote");
        var ptext = _this.convertTextToParagraphs( text );
        var text_array_length = text.split("\n").length;
        
        if( text_array_length > 1 ) {
            
	        if( $closest_element.length ) {
			    if( $closest_element.text().length == 0) {
		        	$closest_element.replaceWith( ptext );
		        }
		        else {
		        	$closest_element.after( ptext );
		        } 
		    }
		    /* if first line */
		    else {
		    	_this.$content_container.append( ptext );
		    }   
		}
		else {
			document.execCommand("insertHTML", false, text);
		}     
    });
};

Content.prototype.convertTextToParagraphs = function( text ) {
	var text_array = text.split("\n");
	var counter = 0;
	var p_array = text_array.map( function(text) {
		if( text.trim().length ) {
			counter = 0;
			return "<p>" + text + "</p>";
		}
		else {
			counter++;
			if( counter < 2 ) {
				return "<p><br></p>";
			}
			else {
				return "";
			}
		}
	});
	return p_array.join("");
};

Content.prototype.delegateTargetBlankToLinks = function() {
    this.$content_container.on( "click mouseover", "a:not(.tooltip_link)", function( event ) {
        /* window.open( $( this ).attr( "href"), '_blank'); */
        if (typeof $(this).data('toggle') == 'undefined' || $(this).data("popover") == "absent") { 
            var href = $(this).attr("href");
            var a_tag = '<a class="tooltip_link" target="_blank" href="' + href + '">' + href + "</a>";

            $(this).attr('data-toggle', "popover").data("placement", "bottom").data("trigger", "manual").data("html","true").data("content", a_tag).popover('show');
            $(this).on('click mouseover', function() {
               
                $(this).popover('show');
            }); 
            if( $(this).data("popover") == "absent" ) {
            	$(this).data("popover","present");
            }    
        }
    });

    this.$content_container.on( "click", "a.tooltip_link", function(event) {
        window.open( $( this ).attr( "href"), '_blank');
    });

};

Content.prototype.dismissPopoverOnClickingOutside = function() {
    $(document).on('click', function (e) {
        /* did not click a popover toggle or popover */
        if ($(e.target).data('toggle') !== 'popover'
            && $(e.target).parents('.popover.in').length === 0) { 
            $('[data-toggle="popover"]').popover('hide');
        }
    });
};

Content.prototype.isSelectionInsideElement = function( tagName ) {
    var sel, containerNode;
    tagName = tagName.toUpperCase(); 
    if (window.getSelection) {
        sel = window.getSelection();
        if (sel.rangeCount > 0) {
            containerNode = sel.getRangeAt(0).commonAncestorContainer;
        }
    } else if ( (sel = document.selection) && sel.type != "Control" ) {
        containerNode = sel.createRange().parentElement();
    }
    while (containerNode) {
        if ( ( containerNode.nodeType == "1" || containerNode.nodeType == 1 ) && containerNode.tagName == tagName) {
            return true;
        }
        containerNode = containerNode.parentNode;
    }
    return false;    
};

Content.prototype.getClosestElementToSelection = function( tagName ) {
    var sel, containerNode;
    tagName = tagName.toUpperCase(); 
    if (window.getSelection) {
        sel = window.getSelection();
        if (sel.rangeCount > 0) {
            containerNode = sel.getRangeAt(0).commonAncestorContainer;
        }
    } else if ( (sel = document.selection) && sel.type != "Control" ) {
        containerNode = sel.createRange().parentElement();
    }
    while (containerNode) {
        if ( ( containerNode.nodeType == "1" || containerNode.nodeType == 1 ) && containerNode.tagName == tagName) {
            return containerNode;
        }
        containerNode = containerNode.parentNode;
    }
    return false;    
};

Content.prototype.itemIsLinked = function(){
    if(window.getSelection().toString() != ""){
        var selection = window.getSelection().getRangeAt(0);
        if (selection && ( selection.startContainer.parentNode.tagName === 'A' || selection.endContainer.parentNode.tagName === 'A' ) ) {
            return true;
        } 
    }
    return false;
};

Content.prototype.populateContent = function( response ) {
	if( response == undefined ) {
		this.reset();
	}
	else {
		this.$content_container.html( response );
		var $delete_icon = '<img src="https://0.ptlp.co/resource-all/icon/svg/trash.svg" class="show-cursor" data-behaviour="remove-image">';
		var $all_images = this.$content_container.find("img");
		$all_images.addClass("writer-image").attr({
			"tabindex": "-1",
			'data-toggle': "popover",
			"data-placement": "top",
			"data-trigger": "focus",
			"data-html":"true",
			"data-content": $delete_icon,
			"data-template": '<div class="popover" role="tooltip" contenteditable="false"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
		}).popover();
		if( isMobile() ) {
			$all_images.attr( "src", function( ) {
		     	return $(this).attr("src") + "&width=240";
			});
		}
		this.$content_container.find("a").data("popover", "absent");
	}
}; 

Content.prototype.delegateRemoveImageListener = function() {
	var _this = this;
	this.$content_container.on("mousedown", "img[data-behaviour=remove-image]", function(e) {
		_this.$content_container.find("img.remove").popover('hide').remove();
	});
};

Content.prototype.reset = function() {
	this.$content_container.empty();
};

Content.prototype.getContent = function() {
	return this.$content_container.html();
};


Content.prototype.convertTextNodesToParagraphs = function() {
	this.$content_container.contents().filter(function() {
	  return this.nodeType == 3;
	}).replaceWith(function() {
	  return "<p>" + $(this).text() + "</p>";
	});
};

Content.prototype.checkFirstChild = function() {
	<#-- test it properly -->
	if( !( this.$content_container.children().first().is("p,blockquote,img") ) ) {
		this.$content_container.children().first().replaceWith( function() {
	  		return "<p>" + this.outerHTML + "</p>";
		} );
	}
};

Content.prototype.checkLastBr = function() {
	<#-- test it properly -->
	var $br = this.$content_container.children().last();
	if( $br.is("br") ) {
		$br.remove();
	}
};

Content.prototype.hasEmptyText = function() {
	return ( this.$content_container.children().length == 0 || !( this.$content_container.children().first().is("p,blockquote,img") ) );
};

Content.prototype.wrapInParagraph = function() {
	<#-- this.$content_container.wrapInner("<p>"); -->
	document.execCommand('formatBlock', false, 'p');
};

Content.prototype.removeSpanTags = function() {
	this.$content_container.find("span").replaceWith( function() {
	   return $( this ).html() ;
	});
};

Content.prototype.hasNoSpanTags = function() {
	return !this.$content_container.find("span").length;
};
