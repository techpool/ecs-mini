var Pagination = function( pagination_container, parent_object ) {
    this.parent_object = parent_object;
    this.$pagination_container = pagination_container;
    this.$page_form = this.$pagination_container.find( "form" );
    this.$previous_page = this.$pagination_container.find( "[data-behaviour=previous-page]" );
    this.$next_page = this.$pagination_container.find( "[data-behaviour=next-page]" );
    this.$page_info_para = this.$pagination_container.find( "[data-behaviour=page-info]" );
    this.$curr_page_input = this.$pagination_container.find( "[data-behaviour=curr_page]" );
    this.$progress_line = this.$pagination_container.find( ".progress-bar" );
    this.$progress_ball = this.$pagination_container.find( "[data-behaviour=progress-ball]" );
};

Pagination.prototype.init = function () {
    this.attachPreviousPageListener();
    this.attachNextPageListener();
    this.attachChapterInputListener();
};

Pagination.prototype.getPreviousPage = function() {
    return ( this.parent_object.currChapter - 1 );
};

Pagination.prototype.getNextPage = function() {
    return ( this.parent_object.currChapter + 1 );
};

Pagination.prototype.attachPreviousPageListener = function() {
    var _this = this;
    this.$previous_page.on( 'click' , function( e ) {
        e.preventDefault();
        var prev_page = _this.getPreviousPage();
        if( prev_page >= 1 ) {
            _this.parent_object.setCurrentPage( prev_page );
        } 
    });
};

Pagination.prototype.attachNextPageListener = function() {
    var _this = this;
    this.$next_page.on( 'click' , function( e ) {
        e.preventDefault();
        var next_page = _this.getNextPage();
        if( next_page <= _this.parent_object.index.length ) {
            _this.parent_object.setCurrentPage( next_page );
        }
    });
};

Pagination.prototype.attachChapterInputListener = function() {
    var _this = this;
    var $page_input = this.$page_form.find( '[data-behaviour="curr_page"]' );
    this.$page_form.on( "submit" , function(e) {
        e.preventDefault();
        var page_val = $page_input.val();
        if( page_val > 0 && page_val <= _this.parent_object.index.length ) {
            _this.parent_object.setCurrentPage( page_val );
        } else {
            $page_input.val( _this.parent_object.currChapter );
        }
    });
};

Pagination.prototype.setProgressPage = function() {
    this.parent_object.table_of_contents_object.setCurrentChapterActive();

    if( this.parent_object.index.length == 1 ) {
        this.$pagination_container.hide();
    } else {
        this.$pagination_container.show();
    }
    this.setCurrentPageNo();
    this.setTotalPageNo();
    this.setProgressBar();
};

Pagination.prototype.setCurrentPageNo = function() {
    this.$curr_page_input.val( this.parent_object.currChapter );
};

Pagination.prototype.setTotalPageNo = function() {
    this.$page_info_para.get( 0 ).lastChild.nodeValue = ( "/" + this.parent_object.index.length );
};

Pagination.prototype.setProgressBar = function() {
    var progress = ( this.parent_object.currChapter /    this.parent_object.index.length )*100 ; 
    this.$progress_line.css( "width", progress + "%" );
    this.$progress_ball.css( "left", progress + "%" );
};