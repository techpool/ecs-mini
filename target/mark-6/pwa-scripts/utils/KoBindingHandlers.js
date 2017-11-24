ko.bindingHandlers.seeMore = { /* Used in summary page */
	update: function( element, valueAccessor, allBindings, viewModel, bindingContext ) {
		var p = $( element ).find( "p" );
		p.text( viewModel.originalText() );
		setTimeout( function() {
			var pHeight = p.height();
			var pAllowedLineHeight = parseFloat( p.css( "line-height" ) ) * 3;
			var isViewMoreVisible = pHeight > pAllowedLineHeight;
			viewModel.isViewMoreVisible( isViewMoreVisible );
			viewModel.maxHeightPx( isViewMoreVisible ? pAllowedLineHeight + 'px' : 'initial' );
		}, 0 );
	}
};

ko.bindingHandlers.seeMoreText = { /* Used in Author page */
	update: function( element, valueAccessor, allBindings, viewModel, bindingContext ) {
		var text = valueAccessor().value;
		var numberOfLines = valueAccessor().lines != null ? valueAccessor().lines : 1;
		if( text == null ) {
			$( element ).css( "visibility", "hidden" );
			return;
		}
		$( element ).css( "visibility", "visible" );
		setTimeout( function() {
			var target_width = $( element ).parent().width();
            var span = document.createElement( 'span' );
            span.style.position = "absolute";
            span.style.top = "-1000px";
            span.style.whiteSpace = 'nowrap';
            span.style.fontFamily = $( element ).css( "font-family" );
            span.style.fontSize = $( element ).css( "font-size" );
            span.innerHTML = " ... " + "(${ _strings.show_more })";
            document.body.appendChild( span );
            var fit = text.length;
            for( var i = 0; i < fit; i++ ) {
                span.innerHTML += text[i];
                if( span.clientWidth > target_width ) {
                    fit = i - 1;
                    break;
                }
            }
            document.body.removeChild( span );
            var characterLimit = fit * numberOfLines;
            if( text.length <= characterLimit ) {
                $( element ).html( text );
                $( element ).css( "white-space", "pre-wrap" );
                return;
            }
            var observer = function() {
                $( element ).find( "a" ).click( function( e ) {
                    var state = e.currentTarget.getAttribute( "data-shown" );
                    if( state == "less" )
                        showMore();
                    else if( state == "more" )
                        showLess( true );
                });
            };
            var showMore = function() {
                var res = text + " " + "<a style='white-space: nowrap;' data-shown='more' href='#'>(${ _strings.show_less })</a>";
                $( element ).html( res );
                $( element ).css( "white-space", "pre-wrap" );
                observer();
            };
            var showLess = function( scrollToTop ) {
                var res = text.substring( 0, characterLimit ) + " ... " + "<a style='white-space: nowrap;' data-shown='less' href='#'>(${ _strings.show_more })</a>";
                $( element ).html( res );
                $( element ).css( "white-space", "normal" );
                observer();
                if( scrollToTop ) {
                    var pos = $( element ).position().top;
                    if( pos < appViewModel.scrollTop() )
                        appViewModel.scrollToTop( pos );
                }
            };
            showLess( false );
		}, 0 );
	}
};

ko.bindingHandlers.slideIn = {
	init: function( element, valueAccessor ) {
		var value = ko.utils.unwrapObservable( valueAccessor() );
		$(element).toggle( value );
	},
	update: function( element, valueAccessor ) {
		var value = ko.utils.unwrapObservable( valueAccessor() );
		value ? $(element).slideDown() : $(element).slideUp();
	}
};

ko.bindingHandlers.transliterate = {
	init: function( element, valueAccessor ) {
		var $content_object = $(element);
		var content_transliteration_object = new transliterationApp( $content_object, "${ lang }" );
		content_transliteration_object.init();
	},
};

/* http://stackoverflow.com/questions/32957407/material-design-lite-how-to-programatically-reset-a-floating-label-input-text#32958279 */
ko.bindingHandlers.mdlFloatingInput = {
	init: function( element, valueAccessor, allBindingsAccessor, data, context ) {
		var $el = $(element),
			$enclosingDiv = $( '<div>' ).insertAfter( $el ),
			$label = $( '<label>' ),
			params = valueAccessor();
		$el.attr( 'id', params.id );
		$label.attr( 'for', params.id ).text( params.label );
		$el.addClass( 'mdl-textfield__input' );
		$label.addClass( 'mdl-textfield__label' );
		$enclosingDiv.addClass( "mdl-textfield mdl-js-textfield mdl-textfield--floating-label" ).append( $el ).append( $label );
		ko.bindingHandlers.value.init( element, function () { return params.value; }, allBindingsAccessor, data, context );
	},
	update: function( element, valueAccessor, allBindingsAccessor, data, context ) {
		var params = valueAccessor(),
			value = params.value();
		ko.bindingHandlers.value.update( element, function () { return params.value; }, allBindingsAccessor, data, context );
		$(element).parent()[ value ? 'addClass' : 'removeClass' ]( 'is-dirty' );
	}
};

ko.bindingHandlers.mdlInput = {
	init: function( element, valueAccessor, allBindingsAccessor, data, context ) {
		var $el = $(element),
			$enclosingDiv = $( '<div>' ).insertAfter( $el ),
			$label = $( '<label>' ),
			params = valueAccessor();
		$el.attr( 'id', params.id );
		$label.attr( 'for', params.id ).text( params.label );
		$el.addClass( 'mdl-textfield__input' );
		$label.addClass( 'mdl-textfield__label' );
		$enclosingDiv.addClass( "mdl-textfield mdl-js-textfield" ).append( $el ).append( $label );
		ko.bindingHandlers.value.init( element, function () { return params.value; }, allBindingsAccessor, data, context );
	},
	update: function( element, valueAccessor, allBindingsAccessor, data, context ) {
		var params = valueAccessor(),
			value = params.value();
		ko.bindingHandlers.value.update( element, function () { return params.value; }, allBindingsAccessor, data, context );
		$(element).parent()[ value ? 'addClass' : 'removeClass' ]( 'is-dirty' );
	}
};

/* HammerJs */
var events = [ 'tap', 'doubletap', 'hold', 'rotate',
	'drag', 'dragstart', 'dragend', 'dragleft', 'dragright', 'dragup',
	'dragdown', 'transform', 'transformstart',
	'transformend', 'swipe', 'swipeleft', 'swiperight',
	'swipeup', 'swipedown', 'pinch', 'pinchin', 'pinchout' ];

ko.utils.arrayForEach( events, function( eventName ) {
	ko.bindingHandlers[eventName] = {
		init: function( element, valueAccessor ) {
			var hammer = new Hammer( element );
			var value = valueAccessor();
			hammer.on( eventName, function(e) {
				value(e);
			});
		}
	}
});

ko.bindingHandlers.carousel = {
	update: function( element, valueAccessor, allBindingsAccessor, data, context ) {
		if( ! valueAccessor() )
			return;
		setTimeout( function() {
			var carousel_wrapper = $( element ).find( '.carousel-wrapper' );
			var carousel = $( carousel_wrapper ).find( '.carousel' );
			var ul = $( carousel ).find( 'ul' );
			var li_width = ul.find( 'li' ).outerWidth();
			var button = $( element ).find( 'button[action]' );

			var scrollWidth = $( carousel_wrapper )[0].scrollWidth;
			var width = $( carousel_wrapper ).width();
			var visible_elements = parseInt( width / li_width );

			var translateX = 0;

			var setTranslate = function() {
				$( carousel ).css({
                    '-webkit-transform' : 'translateX(' + translateX + 'px)',
                    '-moz-transform'    : 'translateX(' + translateX + 'px)',
                    '-ms-transform'     : 'translateX(' + translateX + 'px)',
                    '-o-transform'      : 'translateX(' + translateX + 'px)',
                    'transform'         : 'translateX(' + translateX + 'px)'
                });
			};
			var showPrev = function() {
				var threshold = 0;
				if( translateX == threshold ) {
					translateX = 30;
					setTranslate();
					setTimeout( function() {
						translateX = threshold;
						setTranslate();
					}, 200 );
				} else {
					translateX = Math.min( 0, translateX + ( visible_elements * li_width ) );
					setTranslate();
				}
			};
			var showNext = function() {
				var threshold = width - scrollWidth;
				if( translateX == threshold ) {
					translateX = threshold - 30;
					setTranslate();
					setTimeout( function() {
						translateX = threshold;
						setTranslate();
					}, 200 );
				} else {
					translateX = Math.max( width - scrollWidth, translateX - ( visible_elements * li_width ) );
					setTranslate();
				}
			};

			var hammer = new Hammer( element );
			hammer.on( 'swipeleft', function(e) {
				showNext();
			});
			hammer.on( 'swiperight', function(e) {
				showPrev();
			});
			$( button ).click( function(e) {
				var action = $( e.currentTarget ).attr( 'action' );
				if( action == "prev" )
					showPrev();
				else if( action == "next" )
					showNext();
			});
		}, 0 );
	}
};