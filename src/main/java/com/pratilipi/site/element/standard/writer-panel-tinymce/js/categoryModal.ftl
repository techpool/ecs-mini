var CategoryModal = function() {
	this.pratilipiTagIds = [];
	this.pratilipi_data = ${ pratilipiJson };
	this.systemCategoriesJson = ${ tagsJson };
	this.originalContentType = this.pratilipi_data.type;
	this.currentContentType = this.pratilipi_data.type;
	this.nextButton = $( "[data-behaviour='goto_summary_button']" );
	this.addTagButton = $( "[data-behaviour='add_user_tag_button']" );
	this.pratilipiTag = $( "[data-behaviour='system_category']" ); /* system categories*/
	this.userTag = $( "[data-behaviour='user_suggested_tag']" ); /* already user sugg tags */
	this.messageSpan = $("#category_msg");
	this.$systemCategoriesContainer = $( "[data-behaviour='system-categories-container']" );
	this.$userTagsContainer = $("#user-tags-container");
	this.$systemCategoriesLengthViolationMsgContainer = $("[data-behaviour='system-categories-length-msg']");
	this.$suggestedCategoriesLengthViolationMsgContainer = $("[data-behaviour='suggested-categories-length-msg']");
	this.$suggestedCategoryInput = $("#user_suggested_tags_input");
	this.$contentTypeRadios = $("input[type=radio][name='pratilipi-type']");
	this.fbEvent = new FBEvents();
};

CategoryModal.prototype.init = function() {
	// this.preselectContentType();
	// this.prepopulateSystemCategories();
	this.setSelectedTags();
	this.addClickListener();
	this.addChangeListeners();
	this.checkNextButtonState();
};

CategoryModal.prototype.preselectContentType = function () {
	var _this = this;
	this.contentTypeRadios = document.querySelectorAll('input[type=radio][name="pratilipi-type"]');
	if(['ARTICLE', 'STORY', 'POEM'].includes(contentType)) {
		Array.prototype.forEach.call(this.contentTypeRadios, function(radio) {
			if (radio.value == _this.originalContentType) {
				radio.checked = true;
			}
		});
	}
};

CategoryModal.prototype.prepopulateSystemCategories = function () {
	if(['ARTICLE', 'STORY', 'POEM'].includes(contentType)) {
		var systemCategories = _this.systemCategoriesJson[contentType];

		for(var i=0; i<systemCategories.length; i++) {

			$('<div/>', {
				id: systemCategories[i].id,
				"class": 'pratilipi-tags pratilipi-tag-element',
				"data-behaviour": "system_category",
				"data-select": 0,
				"data-deselect": 0,
				text: systemCategories[i].name
			}).appendTo(this.$systemCategoriesContainer);

		}
	}
};

CategoryModal.prototype.addClickListener = function() {
	var _this = this;

	this.addTagButton.on('click', function() {
		_this.addUserTag();
	});

	this.$systemCategoriesContainer.on("click", "div[data-behaviour='system_category']", function(event) {
		_this.pratilipiTagClicked($(this));
	});

	this.$userTagsContainer.on( "click", "button[data-behaviour='remove_suggested_category']", function( event ) {
    _this.userTagClicked( $( this ) );
	});

	this.nextButton.on('click', function() {
		_this.saveTags();
	});
};

CategoryModal.prototype.addChangeListeners = function () {
	var _this = this;
	this.$suggestedCategoryInput.on('change paste keyup', function() {
    _this.checkSuggestedTagLength($(this));
	});

	this.$contentTypeRadios.on('change', function () {
		_this.handleContentTypeChange($(this));
	});
};


CategoryModal.prototype.setSelectedTags = function() {
	this.showSystemCategoriesLengthViolationMsg(false);
	var tags = this.pratilipi_data.tags;
	var _this = this;
	if (tags) {
		tags.forEach(function(tag){
			$('#' + tag.id).addClass("pratilipi-tag-checked");
			$('#' + tag.id).attr("data-select", "1");
			_this.pratilipiTagIds.push(tag.id);
		});

		if(tags.length > 3) {
			_this.showSystemCategoriesLengthViolationMsg(true);
		}
	}
};

CategoryModal.prototype.markSystemCategoriesAsChecked = function (categoryIds) {
	categoryIds.forEach(function(id){
		$('#' + id).addClass("pratilipi-tag-checked");
		$('#' + id).attr("data-select", "1");
	});
};

CategoryModal.prototype.changeSystemCategoriesOptions = function (contentType) {
	this.$systemCategoriesContainer.empty();
	var systemCategories = this.systemCategoriesJson[contentType];

	for(var i=0; i<systemCategories.length; i++) {
		$('<div/>', {
			id: systemCategories[i].id,
			"class": 'pratilipi-tags pratilipi-tag-element',
			"data-behaviour": "system_category",
			"data-select": 0,
			"data-deselect": 0,
			text: systemCategories[i].name
		}).appendTo(this.$systemCategoriesContainer);

	}
};

CategoryModal.prototype.handleContentTypeChange = function ($element) {
	this.currentContentType = $element.val();
	this.changeSystemCategoriesOptions(this.currentContentType);
	this.showSystemCategoriesLengthViolationMsg(false);
	if(this.currentContentType == this.originalContentType) {
		if(this.pratilipiTagIds.length) {
			this.markSystemCategoriesAsChecked(this.pratilipiTagIds);
		}
		if(this.pratilipiTagIds.length > 3) {
			this.showSystemCategoriesLengthViolationMsg(true);
		}
	}

	this.checkNextButtonState();
};


CategoryModal.prototype.addUserTag = function() {
	var inputElement = $("#user_suggested_tags_input");
	var userTag = $.trim(inputElement.val());
	var _this = this;

	if (!userTag)
		return;

	/* clear input box. Add tag to the suggested list and to suggested selected list */
	inputElement.val("");
	inputElement.focus();

	/* Creating new span and appending to container div */

	var $suggestedTagDiv = $("<div/>", {
		"class": 'pratilipi-tag-element font-16 pratilipi-tag-checked tag-deletable',
		"data-behaviour": "user_suggested_tag",
		"data-select": 1,
		"data-deselect": 0,
	})

	var $textSpan = $("<span/>", {
		"class": 'mdl-chip__text font-16',
		text: userTag
	});

	var $deleteButton = $("<button/>", {
		class: 'mdl-chip__action',
		type: "button",
		"data-behaviour": "remove_suggested_category"
	}).append($("<div/>", {
		"class": 'sprites-icon cross-with-circle-icon'
	}));

	$suggestedTagDiv.append($textSpan).append($deleteButton);

	$suggestedTagDiv.appendTo(this.$userTagsContainer);

	/*var newSpan = document.createElement("span");
	newSpan.setAttribute("class", "user-tags pratilipi-tag-element pratilipi-tag-checked");
	newSpan.setAttribute("data-behaviour", "user_suggested_tag");
	newSpan.setAttribute("data-select", "1");
	newSpan.setAttribute("data-deselect", "0");
	newSpan.onclick = function(event) {
		_this.userTagClicked(event);
	};
	newSpan.innerHTML = userTag;
	userTagsContainer.appendChild(newSpan);
	*/

	/* Enable next button */
	/* this.nextButton.removeClass('category-save-button-disabled'); */
	this.checkNextButtonState();

	/* FB - USER TAG SELECT EVENT */
	this.fbEvent.logEvent('SELECT_TAG', null, userTag, null, null, null, null, null)
};


CategoryModal.prototype.pratilipiTagClicked = function(element) {
	var id = element.attr('id');

	if(!element.hasClass("pratilipi-tag-checked") && $(".pratilipi-tag-checked[data-behaviour='system_category']").length >= 3) {
		this.showSystemCategoriesLengthViolationMsg(true);
	} else {
		this.showSystemCategoriesLengthViolationMsg(false);
		element.toggleClass("pratilipi-tag-checked");
		if (element.hasClass("pratilipi-tag-checked")) {

			/* FB - CATEGORY SELECT EVENT */
			var selectCount = Number(element.attr("data-select"))+1;
			element.attr("data-select",selectCount);
			if (selectCount == 1)	/* send FB event only first time */
				this.fbEvent.logEvent('SELECT_CATEGORY', null, id.toString(), null, null, null, null, null)
		} else {
			/* User is not an admin */
			if (!this.fbEvent.isUserAdmin()) {
				/* disable if category is not selected */
				var selectedCount = $(".pratilipi-tag-checked").length;

			}

			/* FB - CATEGORY DESELECT EVENT */
			var deselectCount = Number(element.attr("data-deselect"))+1;
			element.attr("data-deselect",deselectCount);
			if (deselectCount == 1) { /* send FB event only first time */
				if (this.pratilipiTagIds.indexOf(Number(id)) != -1) /* send only if it is existing category */
					this.fbEvent.logEvent('DESELECT_CATEGORY', null, id.toString(), null, null, null, null, null)
			}
		}
		this.checkNextButtonState();
	}
};


CategoryModal.prototype.userTagClicked = function($element) {
	$element.closest("div[data-behaviour='user_suggested_tag']").remove();
	this.checkNextButtonState();
};


CategoryModal.prototype.saveTags = function() {
	isCategoriesUpdated = false;
	isUserTagsUpdated = false;


	/* message to choose category */
	/* if (this.nextButton.hasClass("category-save-button-disabled")) {
		this.messageSpan.text('${ _strings.tags_add_category_to_proceed }');
		this.messageSpan.addClass('category-failed-message');
		this.messageSpan.css('visibility', 'visible');
		return;
	} */

	selectedTagIds = [];
	pratilipiSelectedTags = $(".pratilipi-tags.pratilipi-tag-checked");
	pratilipiSelectedTags.each(function(i, object) {
		selectedTagIds.push($(object).attr('id'));
	});
	if (this.pratilipiTagIds && this.pratilipiTagIds.length) {	/* self.pratilipi.tags is not null and not empty */
		if (selectedTagIds.toString() != this.pratilipiTagIds.toString()) {
			isCategoriesUpdated = true;
		}
	} else {	/* self.pratilipi.tags is null or empty */
		if (selectedTagIds.length > 0)	/* selectedTagIds length is greater than 0 */
			isCategoriesUpdated = true;
	}

	$("#publishModal").trigger("getRecommendedImages", [selectedTagIds, this.currentContentType]);

	userTags = [];
	userSelectedTags = $("[data-behaviour='user_suggested_tag']");
	userSelectedTags.each(function(i, object) {
		var value = $.trim($(object).find("span").text());
		userTags.push(value);
	});
	/* existing suggested tags is not null and not equal to updated value */
	if (this.pratilipi_data.suggestedTags) { /* this.pratilipi_data.suggestedTags is not null */
		if (userTags.toString() != this.pratilipi_data.suggestedTags.toString()) {
			isUserTagsUpdated = true;
		}
	} else { /* this.pratilipi_data.suggestedTags is null  */
		if (userTags.length > 0)  /* suggestedTags length is greater than 0 */
			isUserTagsUpdated = true;
	}

	if (isCategoriesUpdated || isUserTagsUpdated) {

		var fbEventType, fbEventValue;

		if (isCategoriesUpdated && isUserTagsUpdated) {
			fbEventValue = "CATEGORY_TAG";
			if ((this.pratilipiTagIds && this.pratilipiTagIds.length) &&
					(this.pratilipi_data.suggestedTags && this.pratilipi_data.suggestedTags.length))
				fbEventType =  "UPDATE";
			else
				fbEventType =  "NEW";
		} else if (isCategoriesUpdated) {
			fbEventValue = "CATEGORY";
			if (this.pratilipiTagIds && this.pratilipiTagIds.length)
				fbEventType =  "UPDATE";
			else
				fbEventType =  "NEW";
		} else if (isUserTagsUpdated) {
			fbEventValue = "TAG";
			if (this.pratilipi_data.suggestedTags && this.pratilipi_data.suggestedTags.length)
				fbEventType =  "UPDATE";
			else
				fbEventType =  "NEW";
		}

		/* making server call */
		this.ajaxCall(selectedTagIds, userTags, fbEventType, fbEventValue);
		return;
	}

	/* server call not required when author come to edit content OR user is an admin */
	if (this.pratilipi_data.suggestedTags || this.pratilipi_data.tags || this.fbEvent.isUserAdmin()) {
		/* move to next modal */
		$("#categoryModal").modal("hide");
		$('#publishModal').modal('show');
		return;
	}

};


CategoryModal.prototype.ajaxCall = function(selectedTags, userTags, fbEventType, fbEventValue) {
	var _this = this;

	var ajaxData = {
		pratilipiId: ${ pratilipiId?c },
		type: _this.currentContentType,
		tagIds: JSON.stringify(selectedTags),
		suggestedTags: JSON.stringify(userTags)
	};

	toastr.options = {
		positionClass: 'toast-top-center',
		"timeOut": "500"
	};

	var isSuccess = 0;
	$.ajax({
		type: "POST",
		url: "/api/pratilipi/tags/update",
		data: ajaxData,
		beforeSend: function() {

			/* disable elements before making server call */
			_this.nextButton.attr('disabled', true);

			/* show processing message */
			_this.messageSpan.text('${_strings.tags_processing}');
			_this.messageSpan.removeClass('category-failed-message');
			_this.messageSpan.css('visibility', 'visible');
		},
		success: function( response ){
			console.log("Server call successful");
			toastr.success( '${_strings.updated_pratilipi_info_success}' );
			isSuccess = 1;

			_this.pratilipiTagIds = selectedTags.slice();

			/* Update suggested tag in praitlipi_data json and recreate all spans. */
			_this.pratilipi_data.suggestedTags = userTags;
			_this.createUserTagSpans(userTags);

			/* Enabling next button and hide processing message. */
			_this.nextButton.removeAttr('disabled');
			_this.messageSpan.css('visibility', 'hidden');

			/* hide current modal and show publish modal */
			$("#categoryModal").modal("hide");
			$('#publishModal').modal('show');
		},
		error: function( response ) {
			console.log("Server call failed");
			_this.nextButton.removeAttr('disabled');
			isSuccess = 0;

			/* show retry message */
			_this.messageSpan.text('${ _strings.tags_please_retry }');
			_this.messageSpan.addClass('category-failed-message');
			_this.messageSpan.css('visibility', 'visible');
		},
		complete: function() {
			/* Send FB Event */
			_this.fbEvent.logEvent(
				'SUBMIT_CATEGORY',
				fbEventType,
				fbEventValue,
				selectedTags ? selectedTags.length : 0,
				selectedTags ? selectedTags.toString() : null,
				userTags ? userTags.length : 0,
				userTags ? userTags.toString() : null,
				isSuccess
			);
        	
		}
	});
};


CategoryModal.prototype.createUserTagSpans = function(userTags) {
	var userTagsContainer = document.getElementById("user-tags-container");
	var _this = this;

	/* Remove existing tags. */
	while (userTagsContainer.hasChildNodes()) {
	    userTagsContainer.removeChild(userTagsContainer.lastChild);
	}

	/* Add New tags */
	userTags.forEach(function(tag) {

		var $suggestedTagDiv = $("<div/>", {
			"class": 'pratilipi-tag-element font-16 pratilipi-tag-checked tag-deletable',
			"data-behaviour": "user_suggested_tag",
			"data-select": 1,
			"data-deselect": 0,
		})

		var $textSpan = $("<span/>", {
			"class": 'mdl-chip__text font-16',
			text: tag
		});

		var $deleteButton = $("<button/>", {
			class: 'mdl-chip__action',
			type: "button",
			"data-behaviour": "remove_suggested_category"
		}).append($("<div/>", {
			"class": 'sprites-icon cross-with-circle-icon'
		}));

		$suggestedTagDiv.append($textSpan).append($deleteButton);

		$suggestedTagDiv.appendTo(_this.$userTagsContainer);

	});
};

CategoryModal.prototype.showSystemCategoriesLengthViolationMsg = function (showErrorBoolean) {
	showErrorBoolean ? this.$systemCategoriesLengthViolationMsgContainer.show() : this.$systemCategoriesLengthViolationMsgContainer.hide();
};

CategoryModal.prototype.showSuggestedCategoryLengthViolationMessage = function (showErrorBoolean) {
	showErrorBoolean ? this.$suggestedCategoriesLengthViolationMsgContainer.show() : this.$suggestedCategoriesLengthViolationMsgContainer.hide();
};

CategoryModal.prototype.disableAddSuggestedTagButton = function (disableButtonBoolean) {
	this.addTagButton.prop('disabled', disableButtonBoolean);
};

CategoryModal.prototype.disableNextButton = function (disableButtonBoolean) {
	this.nextButton.prop('disabled', disableButtonBoolean);
};

CategoryModal.prototype.checkNextButtonState = function () {
	var systemCategoriesLength = $(".pratilipi-tags.pratilipi-tag-checked").length;
	var suggestedCategoriesLength = $("[data-behaviour='user_suggested_tag']").length;
	var isOwnsBook = !this.fbEvent.isUserAdmin();
	this.disableNextButton(systemCategoriesLength > 3 || (isOwnsBook && ((suggestedCategoriesLength + systemCategoriesLength) == 0)));
};

CategoryModal.prototype.checkSuggestedTagLength = function ($input) {
	this.showSuggestedCategoryLengthViolationMessage($input.val().length > 30);
	this.disableAddSuggestedTagButton($input.val().length > 30);
};
