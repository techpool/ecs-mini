var FBEvents = function() {
	this.pratilipi_data = ${ pratilipiJson };
};

FBEvents.prototype.init = function() {};

FBEvents.prototype.logEvent = 
	function(event_name, entity_state, entity_value, category_count, category_string, tag_count, tag_string, success) {
		var params = {};
		params["ENVIRONMENT"] = "PROD";
		params["CONTENT_LANGUAGE"] = this.pratilipi_data.language;
		params["SCREEN_NAME"] = "WRITER_PANEL";
		params["USERID"] = '${user.getId()?c}'; 
		if (entity_state)
			params["ENTITY_STATE"] =  entity_state;
		if (entity_value)
			params["ENTITY_VALUE"] =  entity_value;
		if (category_count)
			params["CATEGORY_COUNT"] =  category_count;
		if (category_string)
			params["CATEGORY_STRING"] =  category_string;
		if (tag_count)
			params["TAG_COUNT"] =  tag_count;
		if (tag_string)
			params["TAG_STRING"] =  tag_string;
		params["PRATILIPI_TYPE"] =  this.pratilipi_data.type;
		params["CONTENT_ID"] =  '${ pratilipiId?c }';
		params["AUTHOR_ID"] =  this.pratilipi_data.author.authorId;
		params["ACCESS_LEVEL"] = this.isUserAdmin() ? "ADMIN" : "SELF";
		if (success != null) /* because success == 0 for failed request */
			params["SUCCESS"] =  success;
		
	    FB.AppEvents.logEvent(event_name, null, params);
	};

FBEvents.prototype.logGrowthEvent = 
	function(event_name, entity_value, screenName, location, action, experimentId, isAdmin) {
		var params = {};
		params["ENVIRONMENT"] = "GROWTH";
		params["CONTENT_LANGUAGE"] = this.pratilipi_data.language;
		params["SCREEN_NAME"] = screenName;
		params["LOCATION"] = location;
		params["SCREEN_LOCATION"] = screenName + '_' + location;
		params["ACTION"] = action;
		params["EXPERIMENT_ID"] = experimentId;

		params["USERID"] = '${user.getId()?c}'; 
		
		if (entity_value)
			params["ENTITY_VALUE"] =  entity_value;
		params["PRATILIPI_TYPE"] =  this.pratilipi_data.type;
		params["CONTENT_ID"] =  '${ pratilipiId?c }';
		params["AUTHOR_ID"] =  this.pratilipi_data.author.authorId;
		params["ACCESS_LEVEL"] = isAdmin ? "ADMIN" : "OTHER";
	    FB.AppEvents.logEvent(event_name, null, params);
		console.log(params);
	};
	
FBEvents.prototype.isUserAdmin = function() {
	var authorId = this.pratilipi_data.author.authorId;
	var userId = ${user.getAuthor().getId()?c};
	if (userId != authorId)
		return true;
	else
		return false;
};