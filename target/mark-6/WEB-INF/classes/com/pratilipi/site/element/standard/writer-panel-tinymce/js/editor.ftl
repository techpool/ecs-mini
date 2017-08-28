var Editor = function( parent_object ) {
	this.$image_input = $( "#image_input" );
	this.parent_object = parent_object;
	this.google_analytics_event_value_hash = {
		"Bold": "bold", 
	   	"Italic": "italic",
	 	"Underline": "underline",
	 	"Align left": "align_left",
	 	"Align center": "align_center",
	 	"Align right": "align right",
	 	"Blockquote": "blockquote",
	 	"Insert/edit link": "link",
	 	"Insert/edit image": "image",
	 	"Bullet list": "unordered_list",
	 	"Numbered list": "ordered_list"	 
	};
	this.$chapter_content_object = $( "#chapter-content" );
};

Editor.prototype.init = function() {
	var _this = this;
	this.attachImageInputListener();
	/* this.attachGAClickEventListener(); */

	tinymce.init({
		selector: '#chapter-content',
		auto_focus: 'chapter-content',
		inline: true,
		min_height: 400,
		max_height: 500,
		block_formats: 'Paragraph=p;',	
		plugins : ['autoresize autolink lists link image', 'paste'],
		menubar: false,
		statusbar: false,
		toolbar: 'bold italic underline | CustomLeftAlign CustomCenterAlign CustomRightAlign | CustomBlockquote link imageCustom | Ulist Olist',
		height: 300,
		language: '${ lang }',
		link_context_toolbar: false,
		anchor_bottom: false,
		anchor_top: false,
		default_link_target: "_blank", 
		allow_unsafe_link_target: false,
		target_list: false,
		link_title: false,
		
		<#-- pasting from other sources -->
		paste_data_images: true,
		paste_as_text: false,
		paste_auto_cleanup_on_paste : true,		
	 /*		paste_merge_formats: true, 
			paste_remove_styles: false,
			paste_remove_styles_if_webkit: false,
			paste_strip_class_attributes: true,
			paste_text_sticky: true,
			paste_text_sticky_default: true,
			paste_enable_default_filters: true,
			paste_filter_drop: true,
			paste_word_valid_elements: "i,em,u",
			paste_retain_style_properties: "color font-size", */
		paste_webkit_styles: "none",
		paste_remove_styles_if_webkit: false,
		paste_text_linebreaktype: "p",

		<#-- spell check and other options -->
		browser_spellcheck: false,
		allow_conditional_comments: false,
		allow_html_in_named_anchor: false,

		<#-- p on clicking Enter key -->
		forced_root_block: 'p',
		force_br_newlines: false,
		force_p_newlines: true,
		remove_trailing_brs: false,

		<#-- u instead of text decoration -->
		formats: {
			bold: { inline : 'b', exact : true },
			italic: { inline : 'i', exact : true },
			underline : { inline : 'u', exact : true },
			blockquote: { block: 'blockquote' },
			img: { block:'img' },
			alignleft : { selector : 'p,li', styles : {textAlign : 'left'} },
			aligncenter :{ selector : 'p,li', styles : {textAlign : 'center'} },
			alignright : { selector : 'p,li', styles : {textAlign : 'right'} },				
		},
		setup: function(ed) {
			ed.on( 'init', function(e) {
				_this.parent_object.initializeData();
				
				ed.on( "keydown", function(e) {
					<#-- console.log( _this.$chapter_content_object.text().trim().split(/\s+/).length ); -->
					var keycode = e.keycode || e.which;
					var translation = new Translation( keycode );
					if( translation.action == 'enter' && _this.getClosestSelectionNode( 'blockquote' ).length ) {
						ed.formatter.remove( 'blockquote' );
					}						
				});				 
			});

			if( screen.width > 480 ) {
				ed.on( "keydown", function(e) {
					var keycode = e.keycode || e.which;
					var translation = new Translation( keycode );
					if( ( translation.action == 'backspace' || translation.action == 'enter' ) 
							&& _this.parent_object.content_transliteration_object.suggester.getMode() ) {
						_this.parent_object.content_transliteration_object.sendKeyToSuggester( translation );
						e.preventDefault(); 
					}
				});
				ed.on( 'OpenWindow', function(e) {
					var $url_text_object = $( e.win.$el.find( ".mce-textbox.mce-last" )[0] );
					var url_text_transliteration_object = new transliterationApp( $url_text_object, "${ lang }" );
					url_text_transliteration_object.init();
				});
			}
			ed.addButton( 'imageCustom', {
				icon: 'image',
				tooltip: "Insert/edit image",
				cmd: "mceImage",
				onpostrender: monitorImageChange
			});
			ed.addButton( 'Ulist', {
				icon: 'mce-ico mce-i-bullist',
				tooltip: "Bullet list",
				onpostrender: monitorListChange,
				onclick: function() {
					var alignment = _this.getClosestSelectionNode( 'li' ).css( "textAlign" );
					ed.execCommand( 'InsertUnorderedList', false, null );
					if( alignment ) {
						_this.getClosestSelectionNode( 'p' ).css( "textAlign", alignment );
					}
				}			
			});
			ed.addButton( 'Olist', {
				icon: 'mce-ico mce-i-numlist',
				tooltip: "Numbered list",
				onpostrender: monitorListChange,
				onclick: function() {
					var alignment = _this.getClosestSelectionNode( 'li' ).css( "textAlign" );
					ed.execCommand( 'InsertOrderedList', false, null );
					if( alignment ) {
						_this.getClosestSelectionNode( 'p' ).css( "textAlign", alignment );
					}
				}			
			});		
			ed.addButton( 'CustomBlockquote', {
				icon: 'mce-ico mce-i-blockquote',
				tooltip: "Blockquote",
				cmd: "mceBlockQuote",
				onpostrender: monitorBlockquoteChange
			});
			ed.addButton( 'CustomLeftAlign', {
				icon: 'mce-ico mce-i-alignleft',
				tooltip: "Align left",
				cmd: "JustifyLeft",
				onpostrender: monitorAlignmentChange
			}); 
			ed.addButton( 'CustomCenterAlign', {
				icon: 'mce-ico mce-i-aligncenter',
				tooltip: "Align center",
				cmd: "JustifyCenter",
				onpostrender: monitorAlignmentChange
			});			
			ed.addButton( 'CustomRightAlign', {
				icon: 'mce-ico mce-i-alignright',
				tooltip: "Align right",
				cmd: "JustifyRight",
				onpostrender: monitorAlignmentChange
			});
			function lowercasedElemName( elem ) {
				return elem.nodeName.toLowerCase();
			}
			function monitorImageChange() {
				var btn = this;
				ed.on( 'NodeChange', function(e) {
					var parents = e.parents.map( lowercasedElemName );
					btn.disabled( 	parents.includes( "blockquote" ) || 
									parents.includes( "li" ) || 
									parents.includes( "u" ) || 
									parents.includes( "i" ) || 
									parents.includes( "b" ) || 
									parents.includes( "a" ) );
				});
			}
			function monitorListChange() {
				var btn = this;
				ed.on( 'NodeChange', function(e) {
					var parents = e.parents.map( lowercasedElemName );
					btn.disabled( parents.includes( "blockquote" ) );
				});
			}
			function monitorBlockquoteChange() {
				var btn = this;
				ed.on( 'NodeChange', function(e) {
					var parents = e.parents.map( lowercasedElemName );
					btn.disabled( parents.includes( "li" ) );
				});
			}
			function monitorAlignmentChange() {
				var btn = this;
				ed.on( 'NodeChange', function(e) {
					var parents = e.parents.map( lowercasedElemName );
					btn.disabled( parents.includes( "blockquote" ) || parents.includes( "img" ) );
				});
			}
		},
		images_upload_handler: function( blobInfo, success, failure ) {
			var fd = new FormData();
			var cur_page = _this.parent_object.currChapter;
			fd.append( 'data', blobInfo.blob() );
			fd.append( 'pratilipiId', ${ pratilipiId?c } );	
			fd.append( 'pageNo', cur_page );
			$.ajax({
				type:'POST',
				url: '/api/pratilipi/content/image?pratilipiId=${ pratilipiId?c }&pageNo=' + cur_page,
				data: fd,
				cache: true,
				contentType: false,
				processData: false,
				success: function( data ) {
					var parsed_data = jsonParseTryCatch( data );
					var image_name = parsed_data.name;
					var image_url = "/api/pratilipi/content/image?pratilipiId=${ pratilipiId?c }&name=" + image_name;
					_this.parent_object.setNewImageFlag( true );
					success( image_url );
				},
				error: function( data ) {
					alert( 'HTTP Error: ' + data.status );
					return;
				}
			});
		},

		file_browser_callback: function( field_name, url, type, win ) {
			if( type=='image' ) {
				$( '#field_name' ).val( field_name );
				_this.$image_input.click();
			}
		},

		paste_postprocess: function( plugin, args ) {
			$( args.node ).find( "a:has(img)" ).replaceWith( function() {
				return $(this).find( "img" );
			});			
			$( args.node ).find( "div" ).replaceWith( function() {
				if( $(this).text().length ) {
					return "<p>" + $(this).html() + '</p>';
				} else {
					return "";
				}
			});
			$( args.node ).find( "h1,h2,h3,h4,h5,h6" ).replaceWith( function() {
				if( $(this).text().length ) {
					if( $(this).closest( "p,blockquote,li" ).length ) {
						return "<b>" + $(this).html() + '</b>';
					} else {
						return "<p><b>" + $(this).html() + "</b></p>";
					}
				} else {
					return "";
				}
			});		 
		},

		<#-- enforcing rules to editor -->
		valid_elements : 'p[style],img[src|width|height],blockquote,b,i,u,a[href|target=_blank],br,b/strong,i/em,ol,ul,li',
		extended_valid_elements: 'img[src|width|height],p[style],blockquote,ul,ol,li[style],a[href|target=_blank],br',
		valid_children : 'body[p|img|blockquote|ol|ul],-body[br],p[b|i|u|a[href]|br],-p[img],blockquote[b|i|u|a[href]|br],-blockquote[blockquote|img|p],ol[li],ul[li],-ul[ul|ol|img],li[b|i|u|a[href]|br],-li[img|blockquote|p]',
		invalid_elements : "div",
		valid_styles: { 'p': 'text-align', 'li': 'text-align' },

		<#-- image -->
		image_description: false,
		image_dimensions: false,
	});		
};

Editor.prototype.getClosestSelectionNode = function( nodeName ) {
	return $( tinymce.activeEditor.selection.getNode() ).closest( nodeName );
};

Editor.prototype.attachImageInputListener = function() {
	var _this = this;
	this.$image_input.on( "change", function() {
		_this.uploadOnServer();
	});
};

Editor.prototype.uploadOnServer = function() {
	var _this = this;
	var field_name = "#" + $( '#field_name' ).val();
	var $parent_div = $( field_name ).closest( "div" );
	var fd = new FormData();
	var cur_page = this.parent_object.currChapter;
	var blob = this.$image_input.get(0).files[0];
	fd.append( 'data', blob );
	fd.append( 'pratilipiId', ${ pratilipiId?c } );
	fd.append( 'pageNo', cur_page );
	$parent_div.addClass( "small-spinner" );

	$.ajax({
		type:'POST',
		url: '/api/pratilipi/content/image?pratilipiId=${ pratilipiId?c }&pageNo=' + cur_page,
		data: fd,
		cache: true,
		contentType: false,
		processData: false,
		success: function( data ) {
			var parsed_data = jsonParseTryCatch( data );
			var image_name = parsed_data.name;
			var image_url = "/api/pratilipi/content/image?pratilipiId=${ pratilipiId?c }&name=" + image_name;
			 /* if( isMobile() ) {
				 image_url += "&width=240";
			 } */
			_this.$image_input.val( "" );
			$( field_name ).val( image_url );
			_this.parent_object.setNewImageFlag( true );
		},
		error: function( data ){
			_this.$image_input.val( "" );	
			alert( "Sorry your image could not be uploaded due to a server problem" );						
		},
		complete: function() {
			$parent_div.removeClass( "small-spinner" );
		}
	});
};

Editor.prototype.attachGAClickEventListener = function() {
	var _this = this;
	$("body").on("click", ".mce-toolbar .mce-btn", function() {
		_this.triggerGoogleAnalyticsEvent( $(this).attr("aria-label") );
	});
};

Editor.prototype.getGAEventVal = function( key ) {
	return this.google_analytics_event_value_hash[ key ];
};

Editor.prototype.triggerGoogleAnalyticsEvent = function( event_label ) {
	var event_label_val = this.getGAEventVal( event_label );
	ga('send', 'event', 'editor', 'click', event_label_val, 1);
};
