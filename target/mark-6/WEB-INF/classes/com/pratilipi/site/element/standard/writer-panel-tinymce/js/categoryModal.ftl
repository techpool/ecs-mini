var CategoryModal = function() {
	this.pratilipiTagIds = [];
	this.pratilipi_data = ${ pratilipiJson };
	
	this.nextButton = $( "[data-behaviour='goto_summary_button']" );
	this.addTagButton = $( "[data-behaviour='add_user_tag_button']" );
	this.pratilipiTag = $( "[data-behaviour='pratilipi_suggested_tag']" );
	this.userTag = $( "[data-behaviour='user_suggested_tag']" );
	this.messageSpan = $("#category_msg");
	
	this.fbEvent = new FBEvents();
};

CategoryModal.prototype.init = function() {
	this.setSelectedTags();
	this.addClickListener();
	
	if (!(this.pratilipi_data.tags || this.pratilipi_data.suggestedTags) && !this.fbEvent.isUserAdmin())
		this.nextButton.addClass('category-save-button-disabled');
};

CategoryModal.prototype.addClickListener = function() {
	var _this = this;

	this.addTagButton.on('click', function() {
		_this.addUserTag();
	});
	
	this.pratilipiTag.on('click', function(event) {
		_this.pratilipiTagClicked(event);
	});
	
	this.userTag.on('click', function(event) {
		_this.userTagClicked(event);
	});
	
	this.nextButton.on('click', function() {
		_this.saveTags();
	});
};


CategoryModal.prototype.setSelectedTags = function() {
	var tags = this.pratilipi_data.tags;
	var _this = this;
	if (tags) {
		tags.forEach(function(tag){
			$('#' + tag.id).addClass("pratilipi-tag-checked");
			$('#' + tag.id).attr("data-select", "1");
			_this.pratilipiTagIds.push(tag.id);
		});
	}
};


CategoryModal.prototype.addUserTag = function() {
	var userTagsContainer = document.getElementById("user-tags-container");
	var inputElement = $("#user_suggested_tags_input");
	var userTag = $.trim(inputElement.val());
	var _this = this;
	
	if (!userTag)
		return;
		
	/* clear input box. Add tag to the suggested list and to suggested selected list */
	inputElement.val("");
	inputElement.focus();
	
	/* Creating new span and appending to container div */
	var newSpan = document.createElement("span");
	newSpan.setAttribute("class", "user-tags pratilipi-tag-element pratilipi-tag-checked");
	newSpan.setAttribute("data-behaviour", "user_suggested_tag");
	newSpan.setAttribute("data-select", "1");
	newSpan.setAttribute("data-deselect", "0");
	newSpan.onclick = function(event) {
		_this.userTagClicked(event);
	};
	newSpan.innerHTML = userTag;
	userTagsContainer.appendChild(newSpan);
	
	/* Enable next button */
	this.nextButton.removeClass('category-save-button-disabled');
	
	/* FB - USER TAG SELECT EVENT */
	this.fbEvent.logEvent('SELECT_TAG', null, userTag, null, null, null, null, null)
};


CategoryModal.prototype.pratilipiTagClicked = function(event) {
	var element = $(event.target);
	var id = element.attr('id');
	element.toggleClass("pratilipi-tag-checked");
	if (element.hasClass("pratilipi-tag-checked")) {
		/* Enable next button */
		this.nextButton.removeClass('category-save-button-disabled');
		
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
			
			if (!selectedCount)	/* disable when length is 0 */
				this.nextButton.addClass('category-save-button-disabled');
		}
		
		/* FB - CATEGORY DESELECT EVENT */
		var deselectCount = Number(element.attr("data-deselect"))+1;
		element.attr("data-deselect",deselectCount);
		if (deselectCount == 1) { /* send FB event only first time */
			if (this.pratilipiTagIds.indexOf(Number(id)) != -1) /* send only if it is existing category */
				this.fbEvent.logEvent('DESELECT_CATEGORY', null, id.toString(), null, null, null, null, null)
		}
	}
};


CategoryModal.prototype.userTagClicked = function(event) {
	var element = $(event.target);
	element.toggleClass("pratilipi-tag-checked");
	if (!element.hasClass("pratilipi-tag-checked")) {
		/* User is not an admin */
		if (!this.fbEvent.isUserAdmin()) {	
			/* disable if category is not selected */
			var selectedCount = $(".pratilipi-tag-checked").length;
			
			if (!selectedCount)	/* disable when length is 0 */
				this.nextButton.addClass('category-save-button-disabled');
		}
		
		/* GA - USER TAG DESELECT EVENT */
		var deselectCount = Number(element.attr("data-deselect"))+1;
		element.attr("data-deselect",deselectCount);
		if (deselectCount == 1) {/* send GA event only first time */
			var userTag = $.trim(element.text());
			if (this.pratilipi_data.suggestedTags.indexOf(userTag) != -1) /* send only if it is existing user tag */
				this.fbEvent.logEvent('DESELECT_TAG', null, userTag, null, null, null, null, null);
		}
	} else {
		/* Enable next button */
		this.nextButton.removeClass('category-save-button-disabled');
	}
};


CategoryModal.prototype.saveTags = function() {
	isCategoriesUpdated = false;
	isUserTagsUpdated = false;
	
	
	/* message to choose category */
	if (this.nextButton.hasClass("category-save-button-disabled")) {
		this.messageSpan.text('${ _strings.tags_add_category_to_proceed }');
		this.messageSpan.addClass('category-failed-message');
		this.messageSpan.css('visibility', 'visible');
		return;
	}
	
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
	
	userTags = [];
	userSelectedTags = $(".user-tags.pratilipi-tag-checked");
	userSelectedTags.each(function(i, object) {
		var value = $.trim($(object).text());
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
		var newSpan = document.createElement("span");
		newSpan.setAttribute("class", "user-tags pratilipi-tag-element pratilipi-tag-checked");
		newSpan.setAttribute("data-behaviour", "user_suggested_tag");
		newSpan.setAttribute("data-select", "1");
		newSpan.setAttribute("data-deselect", "0");
		newSpan.innerHTML = tag;
		newSpan.onclick = function(event) {
			_this.userTagClicked(event);
		};
		userTagsContainer.appendChild(newSpan);
	});
};
