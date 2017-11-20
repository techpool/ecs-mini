var ChapterName = function( chapter_name_container, parent_object ) {
	this.$chapter_name_container = chapter_name_container;
	this.parent_object = parent_object;
};

ChapterName.prototype.init = function() {
};

ChapterName.prototype.change_name = function( name ) {
	this.$chapter_name_container.val( name );
};

ChapterName.prototype.reset = function() {
	this.$chapter_name_container.val( "" );
};

ChapterName.prototype.getTitle = function() {
	return this.$chapter_name_container.val();
};