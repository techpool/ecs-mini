var $contentPlaceholder = $('#content-placeholder');

function getFirstRange() {
    var sel = rangy.getSelection();
    return sel.rangeCount ? sel.getRangeAt(0) : null;
}

var onSuggestionPicked = function(word, eng_word) {
  // console.log(':::::::Resolved with word ' + word + '::::::::')
	if (window.getSelection) {
    var text = ( word ? word : eng_word ) + "\u00A0";
    sel = rangy.getSelection();
    var range = getFirstRange();
    if (range) {
	      var text_node = document.createTextNode(text);
        range.insertNode( text_node );
        range.setStartAfter( text_node );
        // sel.collapseToEnd();
        sel.setSingleRange(range);
    }
	}
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
}

var suggester = new Suggester('.word-suggester', onSuggestionPicked);
suggester.init();

var suppressKeypress = function(e) {
  var keycode = e.keycode || e.which;
  if( shouldPreventKeypress( keycode ) ) {
    e.preventDefault();
    sendKeyToSuggester(keycode);
  }
};

var sendKeyToSuggester = function(keycode) {
      suggester.type(keycode);
};

var shouldPreventKeypress = function( keycode ) {
    var translation = new Translation( keycode );
    return ( isNotBackspaceKey(translation) && !isKeydownActionKey(translation) && ( isNotKeypressActionKey(translation) || suggester.getMode() ) );
};

var isNotKeypressActionKey = function( translation ) {
  return ( translation.action != 'space' );
};

var isNotBackspaceKey = function( translation ) {
  return translation.action != 'backspace';
}

var suppressKeydown = function(e) {
  var keycode = e.keycode || e.which;
  if( shouldPreventKeydown( keycode ) ) {
    e.preventDefault();
    sendKeyToSuggester(keycode);
  }
};

var shouldPreventKeydown = function( keycode ) {
    var translation = new Translation( keycode );
    return ( isKeydownActionKey(translation) && suggester.getMode() );
};

var isKeydownActionKey = function( translation ) {
    return ( translation.action == 'up' || translation.action == 'down' || translation.action == 'left' || translation.action == 'right' || translation.action == 'escape');
};

$contentPlaceholder.on('keypress', suppressKeypress);
$contentPlaceholder.on('keydown', suppressKeydown);
$contentPlaceholder.on("click", function(event) {

  if( suggester.getMode() && ( !$(event.target).closest('.word-suggester').length ) ) {
    suggester.clear();
  }
});

window.onload = function() {
	rangy.init();
};
