function( params ) {
	var self = this;

	this.index = params.index;
	this.chapterNo = params.chapterNo;
	this.setChapterNo = params.setChapterNo;

	this.changeChapter = function( vm, e ) {
		self.setChapterNo( vm.chapterNo );
	};

}