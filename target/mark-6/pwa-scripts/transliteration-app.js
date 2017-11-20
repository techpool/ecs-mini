(function () {

var properties = [
  'direction',  /* RTL support */
  'boxSizing',
  'width',  /* on Chrome and IE, exclude the scrollbar, so the mirror div wraps exactly as the textarea does */
  'height',
  'overflowX',
  'overflowY',  /* copy the scrollbar for IE */

  'borderTopWidth',
  'borderRightWidth',
  'borderBottomWidth',
  'borderLeftWidth',
  'borderStyle',

  'paddingTop',
  'paddingRight',
  'paddingBottom',
  'paddingLeft',

  /* https://developer.mozilla.org/en-US/docs/Web/CSS/font */
  'fontStyle',
  'fontVariant',
  'fontWeight',
  'fontStretch',
  'fontSize',
  'fontSizeAdjust',
  'lineHeight',
  'fontFamily',

  'textAlign',
  'textTransform',
  'textIndent',
  'textDecoration',  /* might not make a difference, but better be safe */

  'letterSpacing',
  'wordSpacing',

  'tabSize',
  'MozTabSize'

];

var isBrowser = (typeof window !== 'undefined');
var isFirefox = (isBrowser && window.mozInnerScreenX != null);

function getCaretCoordinates(element, position, options) {
  if(!isBrowser) {
    throw new Error('textarea-caret-position#getCaretCoordinates should only be called in a browser');
  }

  var debug = options && options.debug || false;
  if (debug) {
    var el = document.querySelector('#input-textarea-caret-position-mirror-div');
    if ( el ) { el.parentNode.removeChild(el); }
  }

  /* mirrored div */
  var div = document.createElement('div');
  div.id = 'input-textarea-caret-position-mirror-div';
  document.body.appendChild(div);

  var style = div.style;
  var computed = window.getComputedStyle? getComputedStyle(element) : element.currentStyle;  /* currentStyle for IE < 9 */

  /* default textarea styles */
  style.whiteSpace = 'pre-wrap';
  if (element.nodeName !== 'INPUT')
    style.wordWrap = 'break-word';  /* only for textarea-s */

  /* position off-screen */
  style.position = 'absolute';  /* required to return coordinates properly */
  if (!debug)
    style.visibility = 'hidden';  /* not 'display: none' because we want rendering */

  /* transfer the element's properties to the div */
  properties.forEach(function (prop) {
    style[prop] = computed[prop];
  });

  if (isFirefox) {
    /* Firefox lies about the overflow property for textareas: https://bugzilla.mozilla.org/show_bug.cgi?id=984275 */
    if (element.scrollHeight > parseInt(computed.height))
      style.overflowY = 'scroll';
  } else {
    style.overflow = 'hidden';  /* for Chrome to not render a scrollbar; IE keeps overflowY = 'scroll' */
  }

  div.textContent = element.value.substring(0, position);
  /* the second special handling for input type="text" vs textarea: spaces need to be replaced with non-breaking spaces - http://stackoverflow.com/a/13402035/1269037 */
  if (element.nodeName === 'INPUT')
    div.textContent = div.textContent.replace(/\s/g, '\u00a0');

  var span = document.createElement('span');

  span.textContent = element.value.substring(position) || '.';  /* || because a completely empty faux span doesn't render at all */
  div.appendChild(span);

  var coordinates = {
    top: span.offsetTop + parseInt(computed['borderTopWidth']),
    left: span.offsetLeft + parseInt(computed['borderLeftWidth'])
  };

  if (debug) {
    span.style.backgroundColor = '#aaa';
  } else {
    document.body.removeChild(div);
  }

  return coordinates;
}

if (typeof module != 'undefined' && typeof module.exports != 'undefined') {
  module.exports = getCaretCoordinates;
} else if(isBrowser){
  window.getCaretCoordinates = getCaretCoordinates;
}

}());

var transliterationApp = function( $transliterable_elem, lang ) {
  this.$transliterable_elem = $transliterable_elem;
  this.lang = lang;
};

transliterationApp.prototype.init = function() {
  var _this = this;
  this.setTransliterationElementType();
  this.setSuggestionResolveFunction();
  if( $( ".word-suggester" ).length == 0 ) {
	  var suggesDiv = document.createElement( 'div' );
	  suggesDiv.className = "word-suggester";
	  suggesDiv.innerHTML = "<div class='word-input'></div><div class='suggestions'><div class='suggestion'></div></div>"; 
	  document.body.appendChild( suggesDiv );
  }
  var $suggester_div = $( ".word-suggester" );
  document.body.appendChild(  $suggester_div.get(0) );
  this.suggester = new Suggester( $suggester_div.get(0), _this.onSuggestionPicked, _this.$transliterable_elem, _this.isTransliterationInputType(), _this.lang );
  this.suggester.init();
  this.$transliterable_elem.on('keypress', _this.suppressKeypress.bind( _this ));
  this.$transliterable_elem.on('keydown', _this.suppressKeydown.bind( _this ));
  this.$transliterable_elem.on("click", function(event) {
    if( _this.suggester.getMode() && ( !$(event.target).closest('.word-suggester').length ) ) {
      _this.suggester.clear();
    }
  });
};

function getFirstRange() {
  var sel = rangy.getSelection();
  return sel.rangeCount ? sel.getRangeAt(0) : null;
};

transliterationApp.prototype.onSuggestionPickedInsideDiv = function( word, eng_word ) {
  if (window.getSelection) {
    var text = ( word ? word : eng_word ) + "\u00A0";
    sel = rangy.getSelection();
    var range = getFirstRange();
    if (range) {
      var text_node = document.createTextNode(text);
      range.insertNode( text_node );
      range.setStartAfter( text_node );
      /* sel.collapseToEnd(); */
      sel.setSingleRange(range);
    }
  }
};

transliterationApp.prototype.onSuggestionPickedInsideInput = function(word, eng_word) {
  /* console.log(':::::::Resolved with word ' + word + '::::::::') */
  var text = ( word ? word : eng_word ) + " ";
  var startPos = this.content_holder.get(0).selectionStart;
  var endPos = this.content_holder.get(0).selectionEnd;
  var positon_after_selection = startPos + text.length ;
  var word = this.content_holder.get(0).value.substring(0, startPos)
  + text + this.content_holder.get(0).value.substring(endPos, this.content_holder.get(0).value.length);
  $( this.content_holder.get(0)).val( word ).change();
  this.content_holder.get(0).selectionStart = this.content_holder.get(0).selectionEnd = positon_after_selection;
};

function insertNodeAtRange() {
  var range = getFirstRange();
  if (range) {
    var el = document.createElement("span");
    el.className = "curWord";
    range.insertNode(el);
    rangy.getSelection().setSingleRange(range);
    return el;
  }
};

transliterationApp.prototype.setTransliterationElementType = function() {
  this.isInputTypeElement = this.$transliterable_elem.is("input,textarea") ? true : false;
};

transliterationApp.prototype.setSuggestionResolveFunction = function() {
  this.onSuggestionPicked = this.isTransliterationInputType() ? this.onSuggestionPickedInsideInput : this.onSuggestionPickedInsideDiv;
};

transliterationApp.prototype.isTransliterationInputType = function() {
  return this.isInputTypeElement;
};

transliterationApp.prototype.suppressKeypress = function(e) {
  var keycode = e.keycode || e.which;
  var isShiftKey = e.shiftKey;
  var translation = new Translation( keycode, isShiftKey );
  if( this.shouldPreventKeypress( translation ) ) {
    e.preventDefault();
    this.sendKeyToSuggester( translation );
  }
};

transliterationApp.prototype.sendKeyToSuggester = function( translation ) {
  this.suggester.type( translation );
};

transliterationApp.prototype.shouldPreventKeypress = function( translation ) {
  /* var translation = new Translation( keycode ); */
  return ( this.isNotBackspaceKey(translation) && !this.isKeydownActionKey(translation) && ( this.isNotKeypressActionKey(translation) || this.suggester.getMode() ) );
};

transliterationApp.prototype.isNotKeypressActionKey = function( translation ) {
  return ( translation.action != 'space' && translation.action != 'special_char' );
};

transliterationApp.prototype.isNotBackspaceKey = function( translation ) {
  return translation.action != 'backspace';
};

transliterationApp.prototype.suppressKeydown = function(e) {
  var keycode = e.keycode || e.which;
  var isShiftKey = e.shiftKey;
  var translation = new Translation( keycode, isShiftKey );
  if( this.shouldPreventKeydown( translation ) ) {
    e.preventDefault();
    this.sendKeyToSuggester( translation );
  }
};

transliterationApp.prototype.shouldPreventKeydown = function( translation ) {
  /* var translation = new Translation( keycode ); */
  return ( this.isKeydownActionKey(translation) && this.suggester.getMode() );
};

transliterationApp.prototype.isKeydownActionKey = function( translation ) {
  if( this.isTransliterationInputType() ) {
    return ( translation.action == 'up' || translation.action == 'down' || translation.action == 'left' || translation.action == 'right' || translation.action == 'escape' || translation.action == 'tab' || translation.action == 'backspace');
  } else {
    return ( translation.action == 'up' || translation.action == 'down' || translation.action == 'left' || translation.action == 'right' || translation.action == 'escape' );
  }
};


window.onload = function() {
  if(screen.width > 480) {
    rangy.init();
  }
};
