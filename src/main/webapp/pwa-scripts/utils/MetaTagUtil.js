var metaTag = function( property, content ) {
	return {
		"property": property,
		"content": content
	};
};

var MetaTagUtil = (function() {
	return {
		setMetaTagsForPratilipi: function( pratilipi ) {
			var metaTagArray = [];
			metaTagArray.push( new metaTag( "fb:app_id", "${ fbAppId }" ) );
			metaTagArray.push( new metaTag( "og:title", pratilipi.title ) );
			metaTagArray.push( new metaTag( "og:type", "books.book" ) );
			metaTagArray.push( new metaTag( "og:description", pratilipi.summary != null ? pratilipi.summary : "${ _strings.home_page_title }" ) );
			metaTagArray.push( new metaTag( "og:locale", "${ lang }_IN" ) );
			metaTagArray.push( new metaTag( "og:url", "https://" + ( pratilipi.language != null ? pratilipi.language.toLowerCase() : "${ language?lower_case }" ) + ".pratilipi.com" + pratilipi.pageUrl ) );
			metaTagArray.push( new metaTag( "og:image", getImageUrl( pratilipi.coverImageUrl, 256 ) ) );
			metaTagArray.push( new metaTag( "books:isbn", pratilipi.pratilipiId ) );
			metaTagArray.push( new metaTag( "books:author", "https://" + ( pratilipi.language != null ? pratilipi.language.toLowerCase() : "${ language?lower_case }" ) + ".pratilipi.com" + pratilipi.author.pageUrl ) );
			appViewModel.metaTags( metaTagArray );
		},
		setMetaTagsForAuthor: function( author ) {
			var metaTagArray = [];
			metaTagArray.push( new metaTag( "fb:app_id", "${ fbAppId }" ) );
			appViewModel.metaTags( metaTagArray );
		},
		setMetaTagsForEvent: function( event ) {
			var metaTagArray = [];
			metaTagArray.push( new metaTag( "fb:app_id", "${ fbAppId }" ) );
			appViewModel.metaTags( metaTagArray );
		}
	};
})();