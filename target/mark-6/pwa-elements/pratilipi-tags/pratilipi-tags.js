function(params) {
	/*Google analytic event Labels*/
	var CATEGORY_TAGS_LABEL = "Category Tags";					/* when tags are clicked */
	var CATEGORY_SUBMIT_LABEL = "Category Submit";				/* when submit button is clicked */
	var CONTENT_CATEGORY_LABEL = "PP - Content Category";		/* when pratilipi type is changed on pratilipi page(PP) */
	var TOOLTIP_LABEL = "g_category_tip";
	/*Google analytic event actions*/
	var SELECT_CATEGORY_ACTION = "Select Category";						/* when predefined tag is selected */
	var DESELECT_CATEGORY_ACTION = "DeSelect Category";					/* when predefined tag is unselected */
	var SELECT_USER_TAG_ACTION = "Select User Tag";						/* when user suggested tag is selected (Add button is clicked) */
	var DESELECT_USER_TAG_ACTION = "DeSelect User Tag";					/* when user suggested tag is unselected */
	var ASSIGN_CATEGORIES_TAGS_ACTION = "Assign Categories - Tags";		/* when both predefined tags and user tags are added(for 1st time) */
	var ASSIGN_CATEGORIES_ACTION = "Assign Categories Only";			/* when predefined tags are added(for 1st time) */
	var ASSIGN_TAGS_ACTION = "Assign Tags Only";						/* when user tags are added(for 1st time) */
	var UPDATE_CATEGORIES_TAGS_ACTION = "Update Categories - Tags";		/* when both predefined tags and user tags are updated */
	var UPDATE_CATEGORIES_ACTION = "Update Categories Only";				/* when predefined tags are updated */
	var UPDATE_TAGS_ACTION = "Update Tags Only";						/* when user tags are updated */
	var UPDATE_PRATILIPI_TYPE = "Update Pratilipi Type";				/* when pratilipi type */
	var TOOLTIP_SHOW_ACTION = "g_tip_show";									/* when tooltip shown */
	var TOOLTIP_LOCATION = "g_pratilipi_page";									/* when tooltip shown */
	
	/* FB analytic parameter name */
	var ENVIRONMENT = "ENVIRONMENT";
	var CONTENT_LANGUAGE = "CONTENT_LANGUAGE";
	var SCREEN_NAME = "SCREEN_NAME";
	var USERID = "USERID";
	var ENTITY_STATE = "ENTITY_STATE";
	var ENTITY_VALUE = "ENTITY_VALUE";
	var CATEGORY_COUNT = "CATEGORY_COUNT";
	var CATEGORY_STRING = "CATEGORY_STRING";
	var TAG_COUNT = "TAG_COUNT";
	var TAG_STRING = "TAG_STRING";
	var PRATILIPI_TYPE = "PRATILIPI_TYPE";
	var CONTENT_ID = "CONTENT_ID";
	var AUTHOR_ID = "AUTHOR_ID";
	var ACCESS_LEVEL = "ACCESS_LEVEL";
	var SUCCESS = "SUCCESS";
	
	/* FB analytic parameter value */
	var ENV_PROD = "PROD";
	var ACCESS_LEVEL_ADMIN = "ADMIN";
	var ACCESS_LEVEL_SELF = "SELF";
	var PRATILIPI_PAGE = "PRATILIPI_PAGE";
	var AUTHOR_PAGE = "AUTHOR_PAGE";
	var OLD = "OLD";
	
	/* FB Analytics Event Name */
	var SELECT_CATEGORY = "SELECT_CATEGORY";
	var DESELECT_CATEGORY = "DESELECT_CATEGORY";
	var SELECT_TAG = "SELECT_TAG";
	var DESELECT_TAG = "DESELECT_TAG";
	var SUBMIT_CATEGORY = "SUBMIT_CATEGORY";
	var CATEGORY_TAG = "CATEGORY_TAG";
	var CATEGORY = "CATEGORY";
	var TAG = "TAG";
	var NEW = "NEW";
	var UPDATE = "UPDATE";
	var SELECT_PRATILIPI_TYPE = "SELECT_PRATILIPI_TYPE";
	
	
	selectedTagIds = [];
	suggestedTags = [];
	pratilipiTagIds = [];
	var dataAccessor = new DataAccessor();
	
	var self = this;

	self.isShownInModal = params.isShownInModal != null ? params.isShownInModal : false; /* Default set to false */
	self.pratilipi = params.pratilipi;
	
	self.tags = ko.observableArray([]);
	self.categories = ko.observableArray([]);
	self.userSuggestedTags = ko.observableArray([]);
	self.type = ko.observable();
	self.tagsLoaded = ko.observable(true);
	self.pratilipiTypeSelected = ko.observable(false);
	self.categoryInputActive = ko.observable(true);
	self.toolTipShown = ko.observable(true);
	
	var modal = document.getElementById("addCategory-" + self.pratilipi.pratilipiId());
	var radios;
	if (self.isShownInModal)
		radios = modal.querySelectorAll('input[type=radio][name="pratilipi-type"]');
	else
		radios = document.querySelectorAll('input[type=radio][name="pratilipi-type"]');

	function changeHandler(event) {
		self.type(this.value);
		self.pratilipiTypeSelected(true);
		/* GA - PRATILIPI TYPE UPDATED EVENT */
		if (self.type() != self.pratilipi.type()) {
			/* clearing selected tags of previous Pratilipi type */
			selectedTagIds = [];
			ga_CAL(CONTENT_CATEGORY_LABEL, UPDATE_PRATILIPI_TYPE, self.type() + "-" + self.pratilipi.type());
			FBEvent(SELECT_PRATILIPI_TYPE, OLD, self.type(), null, null, null, null, null);
			
		} else {
			if (pratilipiTagIds)
				selectedTagIds = pratilipiTagIds.slice();
		}
	}

	Array.prototype.forEach.call(radios, function(radio) {
	   radio.addEventListener('change', changeHandler);
	});

	/* pratilipiTagIds : contains ids of tags already assigned to Pratilipi */
	ko.computed( function() {
		if(self.pratilipi.tags() == null)
			return null;
		
		self.pratilipi.tags().forEach(function(tag) {
			if (pratilipiTagIds.indexOf(tag.id()) == -1) {
			    /* add if not present already. */
                pratilipiTagIds.push(tag.id());
                selectedTagIds.push(tag.id());
                self.categories.push({
                    "name": tag.name(),
                    "type": 0
                });
			}
		});
		self.categoryInputActive(false);
		self.toolTipShown = ko.observable(false);
		
	}, this );
	
	ko.computed(function() {
		/* Default value for pratilipi type dropdown */
		if (self.pratilipi.type() && (self.pratilipi.type() == "ARTICLE" ||
										self.pratilipi.type() == "POEM" ||
										self.pratilipi.type() == "STORY")) {
			self.type(self.pratilipi.type());
			Array.prototype.forEach.call(radios, function(radio) {
				if (radio.value == self.pratilipi.type()) {
					radio.checked = true;
				}
			});
	        self.pratilipiTypeSelected(true);
		}
		return null;
		
	});
	
	ko.computed(function() {
		/* Initializing suggestedTags */
		if(self.pratilipi.suggestedTags() == null) 
			return;
		
		self.userSuggestedTags(self.pratilipi.suggestedTags().slice());
		suggestedTags = self.pratilipi.suggestedTags().slice();
		self.pratilipi.suggestedTags().forEach(function(tagName) {
			self.categories.push({
				"name": tagName,
				"type": 1
			});
		});
		self.categoryInputActive(false);
		self.toolTipShown = ko.observable(false);
	}, this);
		
	/* fetching tags based on selected pratilipi type */
	ko.computed(function() {
		if (self.type() == null)
			return;
		
		/* Show loading icon */
		self.tagsLoaded(false);
		/* FETCHING TAGS EVERYTIME self.type CHANGES */
		dataAccessor.getTags(self.pratilipi.language(), self.type(), 
				function(response) {
			if (response == null) {
				/* Server call fails */
				console.log('Tags not working');
				/* hide loading icon */
				self.tagsLoaded(true);
			} else {
				self.onSuccess(response);
			}
			
		});
	});
	
	self.onSuccess = function(data) {
		self.tagsLoaded(true);
		ko.mapping.fromJS(data.response[0].tags, {}, self.tags);
		/* marking already selected tags. */
		if(pratilipiTagIds != null) {
			pratilipiTagIds.forEach(function(tagId) {
				$('#' + tagId).addClass('checked');
				$('#' + tagId).attr("data-select", "1");
			});
		}
	};
	
	/* click event for help icon */
	self.helpClicked = function(data, event) {
		if (self.toolTipShown()) {
			self.toolTipShown(false);
		} else {
			event.stopPropagation();
			self.toolTipShown(true);
			/* Auto hide after 3 seconds */
			setTimeout(function() {
				self.toolTipShown(false);
			}, 3000);
			/* Google Analytics event */
			ga_CAL(TOOLTIP_LABEL, TOOLTIP_SHOW_ACTION, TOOLTIP_LOCATION);
		}
	};
	
	/* Toggle category input view visibility */
	self.toggleCatergoryInput = function() {
		if (self.categoryInputActive())
			self.categoryInputActive(false);
		else
			self.categoryInputActive(true);
	};
	
	/* when pratilipi type is updated */
	self.updateType = function() {
		console.log('#pratilipi_tags_type-'+self.pratilipi.pratilipiId());
		self.type( $( '#pratilipi_tags_type-'+self.pratilipi.pratilipiId() ).attr( "data-val" ) );
		self.pratilipiTypeSelected(true);
		/* GA - PRATILIPI TYPE UPDATED EVENT */
		if (self.type() != self.pratilipi.type()) {
			/* clearing selected tags of previous Pratilipi type */
			selectedTagIds = [];
			ga_CAL(CONTENT_CATEGORY_LABEL, UPDATE_PRATILIPI_TYPE, self.type() + "-" + self.pratilipi.type());
			FBEvent(SELECT_PRATILIPI_TYPE, OLD, self.type(), null, null, null, null, null);
			
		} else {
			if (pratilipiTagIds)
				selectedTagIds = pratilipiTagIds().slice();
		}
	};
	
	/* When cancel button is clicked */
	self.closeCategoryInput = function() {
		self.categoryInputActive(false);
		
		/* Close modal if present in modal */
		if (self.isShownInModal)
			$("#addCategory-" + self.pratilipi.pratilipiId()).modal('hide');
	};
	
	self.tagClick = function(data, event) {
		var id = data.id();
		var element = $("#" + id);
		element.toggleClass("checked");
		if (element.hasClass("checked")) {
			selectedTagIds.push(id);
			/* GA - CATEGORY SELECT EVENT */
			var selectCount = Number(element.attr("data-select"))+1;
			element.attr("data-select",selectCount);
			if (selectCount == 1) {		/* send GA event only first time */
				ga_CA(CATEGORY_TAGS_LABEL, SELECT_CATEGORY_ACTION);
				FBEvent(SELECT_CATEGORY, null, id.toString(), null, null, null, null, null);
			}
		} else {
			index = selectedTagIds.indexOf(id);
			if (index != -1)
				selectedTagIds.splice(index, 1);
			/* GA - CATEGORY DESELECT EVENT */
			var deselectCount = Number(element.attr("data-deselect"))+1;
			element.attr("data-deselect",deselectCount);
			if (deselectCount == 1) {	/* send GA and FB event only first time */
				ga_CA(CATEGORY_TAGS_LABEL, DESELECT_CATEGORY_ACTION);
				if (pratilipiTagIds.indexOf(Number(id)) != -1) /* send only if it is existing category */
					FBEvent(DESELECT_CATEGORY, null, id.toString(), null, null, null, null, null);
			} 
		}
	};
	
	self.suggestedTagClick = function(data, event) {
		var element = $(event.target);
		var userTag = element.text();
		element.toggleClass("checked");
		if (element.hasClass("checked")) {
			suggestedTags.push(userTag);
		} else {
			index = suggestedTags.indexOf(userTag);
			if (index != -1)
				suggestedTags.splice(index, 1);
			/* GA - USER TAG DESELECT EVENT */
			var deselectCount = Number(element.attr("data-deselect"))+1;
			element.attr("data-deselect",deselectCount);
			if (deselectCount == 1) {	/* send GA and FB event only first time */
				ga_CA(CATEGORY_TAGS_LABEL, DESELECT_USER_TAG_ACTION);
				if (self.pratilipi.suggestedTags().indexOf(userTag) != -1)	/* send only if it is existing user tag */
					FBEvent(DESELECT_TAG, null, userTag, null, null, null, null, null);
			} 
		}
	};
	
	self.addTag = function() {
		var inputBox = $("#pratilipi_suggested_tags_input-"+self.pratilipi.pratilipiId());
		var userTag = $.trim(inputBox.val());
		if (!userTag)
			return;
		
		/* clear input box. Add tag to the suggested list and to suggested selected list */
		inputBox.val("");
		inputBox.focus();
		self.userSuggestedTags.push(userTag);
		suggestedTags.push(userTag);
		/* GA - USER TAG SELECT EVENT */
		ga_CA(CATEGORY_TAGS_LABEL, SELECT_USER_TAG_ACTION);
		
		/* FB - USER TAG SELECT EVENT */
		FBEvent(SELECT_TAG, null, userTag, null, null, null, null, null);
	};
	
	self.saveTags = function() {
		var isTypeUpdated = false;
		var isCategoriesUpdated = false;
		var isUserTagsUpdated = false;
		
		/* Compare pratilipitype */
		if (self.pratilipi.type() != self.type())
			isTypeUpdated = true;
		/* Compare selectedTagIds with pratilipiTags */
		if (pratilipiTagIds && pratilipiTagIds.length) {	/* self.pratilipi.tags is not null and not empty */ 
			if (selectedTagIds.toString() != pratilipiTagIds.toString()) {
				isCategoriesUpdated = true;
			}
		} else {	/* self.pratilipi.tags is null or empty */
			if (selectedTagIds.length > 0)	/* selectedTagIds length is greater than 0 */
				isCategoriesUpdated = true;
		}
		
		/* existing suggested tags is not null and not equal to updated value */
		if (self.pratilipi.suggestedTags()) { /* self.pratilipi.suggestedTags is not null */ 
			if (suggestedTags.toString() != self.pratilipi.suggestedTags().toString()) {
				isUserTagsUpdated = true;
			}
		} else { /* self.pratilipi.suggestedTags is null  */
			if (suggestedTags.length > 0)  /* suggestedTags length is greater than 0 */
				isUserTagsUpdated = true;
		}
		
		if (isTypeUpdated || isCategoriesUpdated || isUserTagsUpdated) { 
			
			/* Google and FB Analytics */
			var gaAction, fbEventType, fbEventValue, isSuccess;
			if (isCategoriesUpdated && isUserTagsUpdated) {
				fbEventValue = CATEGORY_TAG;
				if ((pratilipiTagIds && pratilipiTagIds.length) || self.pratilipi.suggestedTags()) {
					gaAction =  UPDATE_CATEGORIES_TAGS_ACTION;
					fbEventType =  UPDATE;
				} else {
					gaAction =  ASSIGN_CATEGORIES_TAGS_ACTION;
					fbEventType =  NEW;
				}
			} else if (isCategoriesUpdated) {
				fbEventValue = CATEGORY;
				if (pratilipiTagIds && pratilipiTagIds.length) {
					gaAction =  UPDATE_CATEGORIES_ACTION;
					fbEventType =  UPDATE;
				} else {
					gaAction =  ASSIGN_CATEGORIES_ACTION;
					fbEventType =  NEW;
				}
			} else if (isUserTagsUpdated) {
				fbEventValue = TAG;
				if (self.pratilipi.suggestedTags() && self.pratilipi.suggestedTags().length) {
					gaAction =  UPDATE_TAGS_ACTION;
					fbEventType =  UPDATE;
				} else {
					gaAction =  ASSIGN_TAGS_ACTION;
					fbEventType =  NEW;
				}
			}
			
			/* disable submit button */
			$("#saveTags-" + self.pratilipi.pratilipiId()).prop('disabled', true);
			$("#closeCategoryInputView-" + self.pratilipi.pratilipiId()).prop('disabled', true);
			/* Make server call to update tags. */
			ToastUtil.toastUp( "${ _strings.working }" );
			dataAccessor.updatePratilipiTags(
					self.pratilipi.pratilipiId(),
					self.type(),
					selectedTagIds,
					suggestedTags,
					function(data) {
						$("#saveTags-" + self.pratilipi.pratilipiId()).prop('disabled', false);
						$("#closeCategoryInputView-" + self.pratilipi.pratilipiId()).prop('disabled', false);
						self.categoryInputActive(false);
						self.pratilipi.suggestedTags(suggestedTags.slice());
						pratilipiTagIds = selectedTagIds.slice();
						
						/* Close modal if present in modal */
						if (self.isShownInModal)
							$("#addCategory-" + self.pratilipi.pratilipiId()).modal('hide');
						
						if (self.type() && self.type() != self.pratilipi.type()) {
							self.pratilipi.type(self.type());
						}
						updateCategoies(selectedTagIds, suggestedTags);
						/* Send FB Event */
						FBEvent(
							SUBMIT_CATEGORY,
							fbEventType,
							fbEventValue,
							selectedTagIds ? selectedTagIds.length : 0,
							selectedTagIds ? selectedTagIds.toString() : null,
							suggestedTags ? suggestedTags.length : 0,
							suggestedTags ? suggestedTags.toString() : null,
							1 /* Successful request */
						);
						ToastUtil.toast( "${_strings.updated_pratilipi_info_success}", 3000 );
					},
					function(data) {
						$("#saveTags-" + self.pratilipi.pratilipiId()).prop('disabled', false);
						$("#closeCategoryInputView-" + self.pratilipi.pratilipiId()).prop('disabled', false);
						var message = "${ _strings.server_error_message }";
						if( data[ "message" ] != null )
							message = data[ "message" ];
						ToastUtil.toast( message, 3000 );
						/* Send FB Event */
						FBEvent(
							SUBMIT_CATEGORY,
							fbEventType,
							fbEventValue,
							selectedTagIds ? selectedTagIds.length : 0,
							selectedTagIds ? selectedTagIds.toString() : null,
							suggestedTags ? suggestedTags.length : 0,
							suggestedTags ? suggestedTags.toString() : null,
							0 /* failed request */
						);
					}
			);
			
			/* GA - ADD OR UPDATE CATEGORY OR/AND TAG EVENT */
			if (gaAction) {
				/* gaAction is null if only pratilipi type is updated */
				ga_CAV(CATEGORY_SUBMIT_LABEL, gaAction, Number(calculateGAValue(isCategoriesUpdated, isUserTagsUpdated)));
			}
		} else {
			/* close input view without making server call */
			self.categoryInputActive(false);
		}
		
	};
	
	updateCategoies = function(selectedTagIds, suggestedTags) {
		/* remove existing entries */
		self.categories([]);
		var list = self.tags();
		if (selectedTagIds) {
			selectedTagIds.forEach(function(id) {
				list.forEach(function(tag) {
					if (id == tag.id()) {
						self.categories.push({
							"name": tag.name(),
							"type": 0
						});
					}
				});
			});
		}
		if (suggestedTags) {
			suggestedTags.forEach(function(tagName) {
						self.categories.push({
							"name": tagName,
							"type": 1
						});
			});
		}
	};
	
	calculateGAValue = function(isCategoriesUpdated, isUserTagsUpdated) {
		var selectedTagIdsLength = selectedTagIds.length;
		var suggestedTagsLength = suggestedTags.length;
		
		if (selectedTagIdsLength == 0 || !isCategoriesUpdated)
			return suggestedTagsLength;
		
		if (suggestedTagsLength == 0 || !isUserTagsUpdated)
			return selectedTagIdsLength;
		
		if (suggestedTagsLength < 10)
			suggestedTagsLength = "0" + suggestedTagsLength;
		return selectedTagIdsLength + suggestedTagsLength;
	};
	
	FBEvent = function(event_name, entity_state, entity_value, category_count, category_string, tag_count, tag_string, success) {
		var params = {};
		params[ENVIRONMENT] = ENV_PROD;
		params[CONTENT_LANGUAGE] = self.pratilipi.language();
		params[SCREEN_NAME] = self.isShownInModal ? AUTHOR_PAGE : PRATILIPI_PAGE;
		params[USERID] = appViewModel.user.userId(); 
		if (entity_state)
			params[ENTITY_STATE] =  entity_state;
		if (entity_value)
			params[ENTITY_VALUE] =  entity_value;
		if (category_count)
			params[CATEGORY_COUNT] =  category_count;
		if (category_string)
			params[CATEGORY_STRING] =  category_string;
		if (tag_count)
			params[TAG_COUNT] =  tag_count;
		if (tag_string)
			params[TAG_STRING] =  tag_string;
		params[PRATILIPI_TYPE] =  self.pratilipi.type();
		params[CONTENT_ID] =  self.pratilipi.pratilipiId();
		params[AUTHOR_ID] =  self.pratilipi.author.authorId();
		params[ACCESS_LEVEL] = appViewModel.user.author.authorId() == self.pratilipi.author.authorId() ? ACCESS_LEVEL_SELF : ACCESS_LEVEL_ADMIN;
		if (success != null) /* because success == 0 for failed request */
			params[SUCCESS] =  success;
		
	    FB.AppEvents.logEvent(event_name, null, params);
	};
	
	
	/* Send tooltip event if it is visible by default. */
	if (self.toolTipShown()) {
		ga_CAL(TOOLTIP_LABEL, TOOLTIP_SHOW_ACTION, TOOLTIP_LOCATION);
	}
	
	/* Hide category tooltip when clicked anywhere */
	$(document).click(function() {
		self.toolTipShown(false);
	});
	
}
