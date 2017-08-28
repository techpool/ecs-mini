var TableOfContents = function(toc_container, pagination_object, parent_object) {
	this.parent_object = parent_object;
	this.pagination_object = pagination_object;
    this.toc_container = toc_container;
    this.$dropdown_menu_list = toc_container.find(".dropdown-menu");
    if( isMobile() ) {
    	this.$book_name_container = toc_container.find("[data-behaviour=edit-title] strong");
    }
    else {
    	this.$book_name_container = toc_container.find("#book_name_toc");
    }
    this.$chapters_separator_second = this.$dropdown_menu_list.find("[data-behaviour=chapters_separator_second]");
    this.chapters = [];
    this.pratilipi_data = ${ pratilipiJson };
    this.book_name = this.pratilipi_data.title ? this.pratilipi_data.title : this.pratilipi_data.titleEn;
    this.$new_chapter_button = this.$dropdown_menu_list.find("[data-behaviour=new_chapter]");
    this.$edit_title = this.$dropdown_menu_list.find('li[data-behaviour="edit-title"]');
    this.$deleteConfirmationModal = $("#chapterDeleteModal");
    this.$titleChangeModal = $("#titleChangelModal");
    this.form_validated = true;

};

TableOfContents.prototype.init = function () {
	this.changeName( this.book_name );
    /* this.populateChapters(); */
    this.attachNewChapterListener();
    this.delegateDeleteChapterListeners();
    this.removeEventListenersOnDeleteModalHide();
    this.attachEditTitleListener();
};

TableOfContents.prototype.changeName = function( name ) {
	this.book_name = name;
	this.$book_name_container.text( name );
	this.setListWidth();
};

TableOfContents.prototype.setListWidth = function() {
    /* setting height of dropdown ul equal to height of the trigger div */
    this.$dropdown_menu_list.width( this.toc_container.find("#table_of_contents_dropdown").width() - 2);
};

TableOfContents.prototype.populateIndex = function( index ) {
    var _this = this;
    this.emptyChaptersList();
    this.chaptersList = index;
    $.each(this.chaptersList, function(index, chapter) {
        var ch = new Chapter( chapter, _this.parent_object );
        _this.chapters.push(ch);
        ch.getListDomElement().insertBefore( _this.$chapters_separator_second );
    });
    this.pagination_object.setProgressPage();
};

TableOfContents.prototype.emptyChaptersList = function() {
	this.chapters = [];
	this.$dropdown_menu_list.find(".toc_chapter_item").remove();
};

TableOfContents.prototype.attachNewChapterListener = function() {
	var _this = this;
	this.$new_chapter_button.on('click', function(e) {
		e.preventDefault();
		_this.parent_object.addNewChapter( parseInt( _this.parent_object.currChapter ) + 1 );
	});
};

TableOfContents.prototype.changeCurrentChapterName = function( currChapter, title ) {
	this.chapters[ currChapter - 1 ].changeName( title );
};

TableOfContents.prototype.setCurrentChapterActive = function() {
	this.$dropdown_menu_list.find(".current-chapter").removeClass("current-chapter");
	this.chapters[ this.parent_object.currChapter - 1 ].setActive();
};

TableOfContents.prototype.delegateDeleteChapterListeners = function() {
	var _this = this;
	this.$dropdown_menu_list.on("click", "div[data-behaviour=delete-chapter]", function(e) {
		e.stopPropagation();
		var chapter_object = $(this).data("relatedObject");
		var chapterNum = chapter_object.chapterNo;
		if( _this.parent_object.hasUnsavedChanges() && ( _this.parent_object.currChapter >= chapterNum ) && _this.parent_object.currChapter != 1) {
			  var a = _this.parent_object.confirmLeavingWithoutSaving();
			  a.then(function (b) {
			    if( b == "save" ) {
			    	_this.parent_object.saveChapter();
			    }
			    else {
					_this.deleteSelectedChapter( chapter_object );
			    }
			  });
		}
		else {
			_this.deleteSelectedChapter( chapter_object );
		}
			
	});
};

TableOfContents.prototype.deleteSelectedChapter = function( chapter_object ) {
	var _this = this;
	this.$deleteConfirmationModal.find(".modal-title").text( chapter_object.name );
	this.$deleteConfirmationModal.modal('show');
	
	this.$deleteConfirmationModal.find('#ok_button').one('click', function() {
	    _this.$deleteConfirmationModal.modal('hide');
	    _this.parent_object.removeChapter( chapter_object.chapterNo );
    });	
}; 

TableOfContents.prototype.removeEventListenersOnDeleteModalHide = function () {
 	var _this = this; 
	this.$deleteConfirmationModal.on('hidden.bs.modal', function (e) {
		_this.$deleteConfirmationModal.find('#ok_button').unbind("click");
	});
};

TableOfContents.prototype.attachEditTitleListener = function() {
	var _this = this;
	this.$edit_title.on("click", function() {
		_this.$titleChangeModal.find("#title-vernacular").val( _this.pratilipi_data.title );
		_this.$titleChangeModal.find("#title-english").val( _this.pratilipi_data.titleEn );
		_this.$titleChangeModal.modal('show');
	});
	this.$titleChangeModal.find("form").on("submit", function(e) {
		e.preventDefault();
		_this.validateTitleForm();
	} );
};

TableOfContents.prototype.validateTitleForm = function() {
	this.resetErrorStates();
	var $vernacular_title = this.$titleChangeModal.find("#title-vernacular");
	var $english_title = this.$titleChangeModal.find("#title-english");
	if( this.isEmptyStr( $vernacular_title.val() ) ) {
		$vernacular_title.closest(".form-group").addClass("has-error");
		$vernacular_title.after('<span class="error-exclamation glyphicon glyphicon-exclamation-sign form-control-feedback" aria-hidden="true"></span>');
		this.form_validated = false;
	}
	
	if( this.form_validated ) {
		this.ajaxSubmitForm( $vernacular_title.val(), $english_title.val() );
	}
};

TableOfContents.prototype.isEmptyStr = function(str) {
	return ( str.length === 0 || !str.trim() );
};

TableOfContents.prototype.resetErrorStates = function() {
	this.form_validated = true;
	var $title_form = this.$titleChangeModal.find("form");
	$title_form.find(".has-error").removeClass("has-error");
	$title_form.find(".error-exclamation").remove();
	
};

TableOfContents.prototype.ajaxSubmitForm = function( vernacular_title, english_title ) {
	var _this = this;
	_this.$titleChangeModal.modal('hide');
	var ajax_data = {
			title: vernacular_title ,
    		titleEn: english_title,
    		pratilipiId: "${ pratilipiId?c }"
    	   };
    $.ajax({type: "POST",
        url: "/api/pratilipi?_apiVer=2",
        data: ajax_data,
        success:function(response){
        	
        	var parsed_data = jQuery.parseJSON( response );
        	_this.pratilipi_data = parsed_data;
        	var book_name = parsed_data.title ? parsed_data.title : parsed_data.titleEn;
        	_this.parent_object.changeName( book_name );
        	/* reset Pratilipi data and table of contents title in frontend */
		},
        fail:function(response){
        	var message = jQuery.parseJSON( response.responseText );
			alert(message);
		}			    		
		
	});	
	
};

TableOfContents.prototype.addNewChapterButton = function() {
    var $divider = $("<li>", {
        role: "separator",
        "class": "divider"
    });

    var $boldNewChapter = $("<strong>", {
        "class": "pratilipi-red"
    })
    .text(" + New Chapter");
    var $aNewChapter = $("<a>", {
        href: "#", 
    }).append( $boldNewChapter );
    var $liNewChapter = $("<li>", {
        "class": "text-center"
    }).append( $aNewChapter );

    this.$dropdown_menu_list.append( $divider ).append( $liNewChapter );
};
