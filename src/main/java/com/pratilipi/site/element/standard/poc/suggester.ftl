var Suggester = function(selectorOrElem, onResolve) {
  if(selectorOrElem) {
    this.elem = $(selectorOrElem);
    this.setMode( false );
  } else {
    throw 'Coding karni nahi aati.'
  }

  if(typeof onResolve == 'function') {
    this.resolveCallback = onResolve;
  } else {
    throw 'Coding karni nahi aati.'
  }

  this.text = '';
  this.savedSelection = null;
};

var suggesterMethods = {
  init: function() {
    var self = this;

    $contentPlaceholder.on("blur", function() {
      if( self.getMode() ) {
        if (this.savedSelection) {
            rangy.removeMarkers(this.savedSelection);
        }
        this.savedSelection = rangy.saveSelection();
      }
    });

    this.elem.on('click',  'div.suggestion', function(e) {
      self.elem.find(".highlight-suggestion").removeClass("highlight-suggestion");
      $(this).addClass("highlight-suggestion");
      if (this.savedSelection) {
        rangy.restoreSelection(savedSel, true);
        this.savedSelection = null;
      }  
      self.handleWordSelection();
    } );
  },

  type: function(keycode) {
    var translation = new Translation(keycode);
    if(translation.action == 'typing') {
      if( !this.getMode() ) {
        //add a span tag there and set the relative position of the dropdown
        this.curSpan = insertNodeAtRange();
        this.setSuggesterPosition();
        this.showSuggester();
        this.setMode( true );
      }
      this.setText(this.text + translation.text);
    } else if(translation.action == 'space' || translation.action == 'enter') {
      this.handleWordSelection();
    } else if(translation.action == 'backspace') {
      this.backspace();
    } else if(translation.action == 'up') {
      this.goUp();
    } else if( translation.action == 'down') {
      this.goDown();
    } else if( translation.action == 'escape' ) {
      this.clear();
    }
  },

  backspace: function() {
    if( this.text.length == 1 ) {
      this.clear();
    } else {
      this.setText(this.text.substr(0, this.text.length -1));
    }
  },

  goUp: function() {
    var $suggestions = this.elem.find('.suggestions');
    var $currently_highlighted_suggestion = $suggestions.find('.highlight-suggestion');
    var $prev_suggestion = $currently_highlighted_suggestion.prev();
    $currently_highlighted_suggestion.removeClass("highlight-suggestion");
    if( $prev_suggestion.length ) {
      $prev_suggestion.addClass("highlight-suggestion");
    } else {
       $suggestions.find(".suggestion:last").addClass("highlight-suggestion");
    }
  },  

  goDown: function() {
    var $suggestions = this.elem.find('.suggestions');
    var $currently_highlighted_suggestion = $suggestions.find('.highlight-suggestion');
    var $next_suggestion = $currently_highlighted_suggestion.next();
    $currently_highlighted_suggestion.removeClass("highlight-suggestion");
    if( $next_suggestion.length ) {
      $next_suggestion.addClass("highlight-suggestion");
    } else {
       $suggestions.find(".suggestion:first").addClass("highlight-suggestion");
    }
  },

  getSuggestions: function() {
    var self = this;
    $.ajax({
      url: "https://www.google.com/inputtools/request?ime=transliteration_en_hi&num=5&cp=0&cs=0&ie=utf-8&oe=utf-8&app=jsapi",
      data: {
          text: self.text
      },
      type: "GET",
   
      dataType : "json",
    })
    .done(function( json ) {
      self.suggestions = json[1][0][1];
      self.suggestions.push( self.text );
      self.suggest();
    });
  },

  clearSuggestions: function() {
    this.suggestions = [];
    this.suggest();
  },

  suggest: function() {
    var $suggestions = this.elem.find('.suggestions');
    $suggestions.empty();
    
    this.suggestions.forEach(function(suggestion) {
      var $suggestion = $("<div class='suggestion'></div>");
      $suggestion.text(suggestion);
      $suggestions.append($suggestion);
    });
    $suggestions.find(".suggestion:first").addClass("highlight-suggestion");    
  },

  clear: function() {
    this.setText('');
    this.hideSuggester();
    this.clearSpanTags();
    this.setMode(false);
  },

  setText: function(text) {
    // this.addSpanElement();
    this.text = text;
    this.getInputElem().text(this.text);
    if(text) {
      this.getSuggestions();
    } else {
      this.clearSuggestions();
    }
  },

  getInputElem: function() {
    if (!this.inputElem) {
      this.inputElem = this.elem.find('.word-input');
    }
    return this.inputElem;
  },

  resolve: function(withSuggestion) {
    this.resolveCallback && this.resolveCallback(withSuggestion, this.text);
    this.clear();
  },

  setMode: function( val ) {
    this.mode = val;
  },

  getMode: function() {
    return this.mode;
  },

  setSuggesterPosition: function() {
    var self = this;
    this.elem.css({
      top: self.curSpan.offsetTop + self.curSpan.offsetHeight,
      left: self.curSpan.offsetLeft
    });
  },

  showSuggester: function() {
    this.elem.show();
  },

  hideSuggester: function() {
    this.elem.hide();
  },

  clearSpanTags: function() {
    $("span.curWord").remove();
  },

  handleWordSelection: function() {
      var selected_word;
      var $highlighted_word = this.elem.find(".highlight-suggestion");
      selected_word = $highlighted_word.length ? $highlighted_word.text() : this.suggestions[0];
      this.resolve( selected_word );
  }
    
};

for(var method in suggesterMethods) {
  Suggester.prototype[method] = suggesterMethods[method];
}

var Translation = function(keycode) {
  if(keycode == 13) {
    this.action = 'enter'
  } else if(keycode == 32) {
    this.action = 'space';
  } else if(keycode == 8) {
    this.action = 'backspace';
  } else if(keycode == 38) {
    this.action = 'up';
  } else if(keycode == 40) {
    this.action = 'down';
  } else if(keycode == 37) {
    this.action = 'left';
  } else if(keycode == 39) {
    this.action = 'right';
  } else if(keycode == '27') {
    this.action = 'escape';
  }

  else if(keycode > 32 && keycode < 127) {
    this.action = 'typing';
    this.text = String.fromCharCode(keycode).toLowerCase();
  }
};