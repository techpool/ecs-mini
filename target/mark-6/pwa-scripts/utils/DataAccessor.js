/* DataAccessor */
var DataAccessor = function() {

	var httpUtil = new HttpUtil();

	var API_PREFIX = window.location.origin.indexOf( "localhost:8080" ) > -1 ? "https://hindi-gamma.pratilipi.com/api" : "/api";

	/* Search */
	var SEARCH_PREFIX = "/search";
	var SEARCH_TRENDING_API = "/trending_search";
	var SEARCH_CORE_API = "/search";

	/* Recommendation Api */
	var RECOMMENDATION_PREFIX = "/recommendation";
	var RECOMMENDATION_PRATILIPI_API = "/pratilipis";

	var PAGE_API = "/page";
	var PAGE_CONTENT_API = "/page/content";
	var PRATILIPI_API = "/pratilipi?_apiVer=2";
	var PRATILIPI_LIST_API = "/pratilipi/list?_apiVer=3";
	var PRATILIPI_CONTENT_API = "/pratilipi/content";
	var PRATILIPI_CONTENT_INDEX_API = "/pratilipi/content/index";
	var PRATILIPI_TAGS_UPDATE_API = "/pratilipi/tags/update";
	var USER_PRATILIPI_API = "/userpratilipi";
	var USER_PRATILIPI_LIBRARY_LIST_API = "/userpratilipi/library/list";
	var AUTHOR_API = "/author";
	var USER_AUTHOR_FOLLOW_API = "/userauthor/follow?_apiVer=2";
	var USER_API = "/user?_apiVer=2";
	var USER_LOGIN_API = "/user/login";
	var USER_LOGIN_FACEBOOK_API = "/user/login/facebook";
	var USER_LOGIN_GOOGLE_API = "/user/login/google";
	var USER_REGISTER_API = "/user/register";
	var USER_PASSWORD_UPDATE_API = "/user/passwordupdate";
	var USER_LOGOUT_API = "/user/logout";
	var NOTIFICATION_API = "/notification";
	var NOTIFICATION_LIST_API = "/notification/list";
	var NAVIGATION_LIST_API = "/navigation/list";
	var USER_PRATILIPI_REVIEW_LIST_API = "/userpratilipi/review/list";
	var COMMENT_LIST_API = "/comment/list";
	var USER_PRATILIPI_REVIEW_API = "/userpratilipi/review";
	var USER_AUTHOR_FOLLOW_API = "/userauthor/follow?_apiVer=2";
	var USER_PRATILIPI_LIBRARY_API = "/userpratilipi/library";
	var COMMENT_API = "/comment";
	var VOTE_API = "/vote";
	var INIT_API = "/init?_apiVer=2";
	var INIT_BANNER_LIST_API = "/init/banner/list";
	var USER_AUTHOR_FOLLOW_LIST_API = "/userauthor/follow/list";
	var EVENT_API = "/event";
	var EVENT_LIST_API = "/event/list";
	var BLOG_POST_API = "/blogpost";
	var BLOG_POST_LIST_API = "/blogpost/list";
	var CONTACT_API = "/contact";
	var TAGS_API = "/pratilipi/tags";
	var USER_EMAIL_API = "/user/email";
	var TOP_AUTHORS_API = "/author/list/readcount";

	var request = function( name, api, params ) {
		return {
			"name": name,
			"api": api,
			"params": params != null ? encodeURIComponent( httpUtil.formatParams( params ) ) : null
		};
	};

	var processRequests = function( requests ) {
		var params = {};
		for( var i = 0; i < requests.length; i++ ) {
			var request = requests[i];
			params[ request.name ] = request.api;
			if( request.params != null )
				params[ request.name ] += encodeURIComponent( request.api.indexOf( "?" ) > -1 ? "&" : "?" ) + request.params;
		}
		return JSON.stringify( params );
	};

	var processGetResponse = function( response, status, aCallBack ) {
		if( aCallBack != null )
			aCallBack( status == 200 ? response : null );
	};

	var processPostResponse = function( response, status, successCallBack, errorCallBack ) {
		if( status == 200 && successCallBack != null )
			successCallBack( response );
		else if( status != 200 && errorCallBack != null )
			errorCallBack( response );
	};

	/* GET Methods */
	this.getPratilipiByUri = function( pageUri, includeUserPratilipi, aCallBack ) {

		var requests = [];
		requests.push( new request( "req1", PAGE_API, { "uri": pageUri } ) );
		requests.push( new request( "req2", PRATILIPI_API, { "pratilipiId": "$req1.primaryContentId" } ) );

		if( includeUserPratilipi )
			requests.push( new request( "req3", USER_PRATILIPI_API, { "pratilipiId": "$req1.primaryContentId" } ) );

		httpUtil.get( API_PREFIX, null, { "requests": processRequests( requests ) },
			function( response, status ) {
				if( aCallBack != null ) {
					var pratilipi = response.req2 && response.req2.status == 200 ? response.req2.response : null;
					var userpratilipi = includeUserPratilipi && response.req3 && response.req3.status == 200 ? response.req3.response : null;
					aCallBack( pratilipi, userpratilipi );
				}
		});
	};

	this.getPratilipiById = function( pratilipiId, includeUserPratilipi, aCallBack ) {

		var requests = [];
		requests.push( new request( "req1", PRATILIPI_API, { "pratilipiId": pratilipiId } ) );

		if( includeUserPratilipi )
			requests.push( new request( "req2", USER_PRATILIPI_API, { "pratilipiId": pratilipiId } ) );

		httpUtil.get( API_PREFIX, null, { "requests": processRequests( requests ) },
			function( response, status ) {
				if( aCallBack != null ) {
					var pratilipi = response.req1 && response.req1.status == 200 ? response.req1.response : null;
					var userpratilipi = includeUserPratilipi && response.req2 && response.req2.status == 200 ? response.req2.response : null;
					aCallBack( pratilipi, userpratilipi );
				}
		});
	};

	this.getPratilipiAndIndex = function( pratilipiId, aCallBack ) {
		if( pratilipiId == null ) return;

		var requests = [];
		requests.push( new request( "req1", PRATILIPI_API, { "pratilipiId": pratilipiId } ) );
		requests.push( new request( "req2", USER_PRATILIPI_API, { "pratilipiId": pratilipiId } ) );
		requests.push( new request( "req3", PRATILIPI_CONTENT_INDEX_API, { "pratilipiId": pratilipiId } ) );

		httpUtil.get( API_PREFIX, null, { "requests": processRequests( requests ) },
			function( response, status ) {
				if( aCallBack != null ) {
					var pratilipi = response.req1.status == 200 ? response.req1.response : null;
					var userpratilipi = response.req2.status == 200 ? response.req2.response : null;
					var index = response.req3.status == 200 ? response.req3.response : null;
					aCallBack( pratilipi, userpratilipi, index );
				}
		});
	};

	this.getAuthorByUri = function( pageUri, includeUserAuthor, aCallBack ) {

		var requests = [];
		requests.push( new request( "req1", PAGE_API, { "uri": pageUri } ) );
		requests.push( new request( "req2", AUTHOR_API, { "authorId": "$req1.primaryContentId" } ) );

		if( includeUserAuthor )
			requests.push( new request( "req3", USER_AUTHOR_FOLLOW_API, { "authorId": "$req1.primaryContentId" } ) );

		httpUtil.get( API_PREFIX, null, { "requests": processRequests( requests ) },
			function( response, status ) {
				if( aCallBack != null ) {
					var author = response.req2 && response.req2.status == 200 ? response.req2.response : null;
					var userauthor = includeUserAuthor && response.req3 && response.req3.status == 200 ? response.req3.response : null;
					aCallBack( author, userauthor );
				}
		});
	};

	this.getAuthorById = function( authorId, includeUserAuthor, aCallBack ) {

		var requests = [];
		requests.push( new request( "req1", AUTHOR_API, { "authorId": authorId } ) );

		if( includeUserAuthor )
			requests.push( new request( "req2", USER_AUTHOR_FOLLOW_API, { "authorId": authorId } ) );

		httpUtil.get( API_PREFIX, null, { "requests": processRequests( requests ) },
			function( response, status ) {
				if( aCallBack != null ) {
					var author = response.req1 && response.req1.status == 200 ? response.req1.response : null;
					var userauthor = includeUserAuthor && response.req2 && response.req2.status == 200 ? response.req2.response : null;
					aCallBack( author, userauthor );
				}
		});
	};

	this.getEventByUri = function( pageUri, aCallBack ) {

		var requests = [];
		requests.push( new request( "req1", PAGE_API, { "uri": pageUri } ) );
		requests.push( new request( "req2", EVENT_API, { "eventId": "$req1.primaryContentId" } ) );

		httpUtil.get( API_PREFIX, null, { "requests": processRequests( requests ) },
			function( response, status ) {
				if( aCallBack != null ) {
					var event = response.req2.status == 200 ? response.req2.response : null;
					aCallBack( event );
				}
		});
	};

	this.getEventById = function( eventId, aCallBack ) {
		if( eventId == null ) return;
		httpUtil.get( API_PREFIX + EVENT_API,
            null,
            { "eventId": eventId },
            function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getEventList = function( aCallBack ) {
		httpUtil.get( API_PREFIX + EVENT_LIST_API,
						null,
						{ "language": "${ language }" },
						function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getBlogPostByUri = function( pageUri, aCallBack ) {
		var requests = [];
		requests.push( new request( "req1", PAGE_API, { "uri": pageUri } ) );
		requests.push( new request( "req2", BLOG_POST_API, { "blogPostId": "$req1.primaryContentId" } ) );

		httpUtil.get( API_PREFIX, null, { "requests": processRequests( requests ) },
			function( response, status ) {
				if( aCallBack != null ) {
					var blogpost = response.req2.status == 200 ? response.req2.response : null;
					aCallBack( blogpost );
				}
		});
	};

	this.getBlogPostListByUri = function( pageUri, state, cursor, resultCount, aCallBack ) {
		var requests = [];
		requests.push( new request( "req1", PAGE_API, { "uri": pageUri } ) );

		var params = { "blogId": "$req1.primaryContentId",
					"language": "${ language }" };
		params[ "state" ] = state != null ? state : "PUBLISHED";
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;

		requests.push( new request( "req2", BLOG_POST_LIST_API, params ) );
		httpUtil.get( API_PREFIX, null, { "requests": processRequests( requests ) },
			function( response, status ) {
				if( aCallBack != null ) {
					var blogpost = response.req2.status == 200 ? response.req2.response : null;
					aCallBack( blogpost );
				}
		});
	};

	this.getUser = function( aCallBack ) {
		httpUtil.get( API_PREFIX + USER_API,
						null,
						null, 
						function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getUserById = function( userId, aCallBack ) {
		if( userId == null ) return;
		httpUtil.get( API_PREFIX + USER_API,
				null,
				{ "userId": userId }, 
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getNotificationList = function( cursor, resultCount, aCallBack ) {
		var params = {};
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;
		httpUtil.get( API_PREFIX + NOTIFICATION_LIST_API,
						null,
						params, 
						function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getNavigationList = function( aCallBack ) {
		httpUtil.get( API_PREFIX + NAVIGATION_LIST_API,
						null,
						{ "language": "${ language }" }, 
						function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getReviewList = function( pratilipiId, cursor, offset, resultCount, aCallBack ) {
		if( pratilipiId == null ) return;
		var params = { "pratilipiId": pratilipiId };
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( offset != null ) params[ "offset" ] = offset;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;
		httpUtil.get( API_PREFIX + USER_PRATILIPI_REVIEW_LIST_API,
						null,
						params, 
						function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getReviewCommentList = function( userPratilipiId, cursor, resultCount, aCallBack ) {
		if( userPratilipiId == null ) return;
		var params = { "parentType": "REVIEW", "parentId": userPratilipiId };
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;
		httpUtil.get( API_PREFIX + COMMENT_LIST_API,
						null,
						params, 
						function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getPratilipiListByListName = function( listName, cursor, offset, resultCount, aCallBack ) {
		if( listName == null ) return;
		var params = { "listName": listName, "state": "PUBLISHED", "language": "${ language }" };
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( offset != null ) params[ "offset" ] = offset;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;
		httpUtil.get( API_PREFIX + PRATILIPI_LIST_API,
			null,
			params,
			function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getPratilipiListBySearchQuery = function( searchQuery, cursor, offset, resultCount, aCallBack ) {
		if( searchQuery == null ) return;
		var params = { "searchQuery": searchQuery, "state": "PUBLISHED", "language": "${ language }" };
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( offset != null ) params[ "offset" ] = offset;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;
		httpUtil.get( API_PREFIX + PRATILIPI_LIST_API,
				null,
				params, 
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getPratilipiListByAuthor = function( authorId, state, cursor, offset, resultCount, aCallBack ) {
		if( authorId == null ) return;
		var params = { "authorId": authorId, "state": state != null ? state : "PUBLISHED" };
		if( state == "DRAFTED" ) params[ "orderByListingDate" ] = false;
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( offset != null ) params[ "offset" ] = offset;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;
		httpUtil.get( API_PREFIX + PRATILIPI_LIST_API,
				null,
				params, 
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getPratilipiListByEventId = function( eventId, cursor, offset, resultCount, aCallBack ) {
		if( eventId == null ) return;
		var params = { "eventId": eventId, "state": "PUBLISHED" };
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( offset != null ) params[ "offset" ] = offset;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;
		httpUtil.get( API_PREFIX + PRATILIPI_LIST_API,
				null,
				params,
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getUserLibraryList = function( cursor, resultCount, aCallBack ) {
		var params = {};
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;
		httpUtil.get( API_PREFIX + USER_PRATILIPI_LIBRARY_LIST_API,
				null,
				params, 
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getHomePageSections = function( aCallBack ) {
		httpUtil.get( API_PREFIX + INIT_API,
				null,
				{ "language": "${ language }" },
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getHomePageBanners = function( aCallBack ) {
		httpUtil.get( API_PREFIX + INIT_BANNER_LIST_API,
				null,
				{ "language": "${ language }" },
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getAuthorFollowers = function( authorId, cursor, offset, resultCount, aCallBack ) {
		if( authorId == null ) return;
		var params = { "authorId": authorId };
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( offset != null ) params[ "offset" ] = offset;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;
		httpUtil.get( API_PREFIX + USER_AUTHOR_FOLLOW_LIST_API,
				null,
				params,
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getUserFollowing = function( userId, cursor, offset, resultCount, aCallBack ) {
		if( userId == null ) return;
		var params = { "userId": userId };
		if( cursor != null ) params[ "cursor" ] = cursor;
		if( offset != null ) params[ "offset" ] = offset;
		if( resultCount != null ) params[ "resultCount" ] = resultCount;
		httpUtil.get( API_PREFIX + USER_AUTHOR_FOLLOW_LIST_API,
				null,
				params,
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getPageContent = function( pageName, aCallBack ) {
		if( pageName == null ) return;
		httpUtil.get( API_PREFIX + PAGE_CONTENT_API,
				null,
				{ "pageName": pageName, "language": "${ language }" },
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getPratilipiContent = function( pratilipiId, chapterNo, oldContent, aCallBack ) {
		if( pratilipiId == null ) return; /* chapterNo can be null for IMAGE contents */
		httpUtil.get( API_PREFIX + PRATILIPI_CONTENT_API,
				null,
				{ "pratilipiId": pratilipiId, "chapterNo": chapterNo, "_apiVer": oldContent ? 1 : 3 },
				function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	/* Recommendation Service */
	function getRecommendationHeaders() { return { "AccessToken": getCookie( "access_token" ), "Version": "1.0" }; };
	this.getPratilipiRecommendation = function( contextId, context, resultCount, aCallBack ) {
		if( contextId == null ) return;
		httpUtil.get( API_PREFIX + RECOMMENDATION_PREFIX + RECOMMENDATION_PRATILIPI_API,
			getRecommendationHeaders(),
			{ "contextId": contextId, "context": context, "resultCount": resultCount },
			function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};


	/* Search Service */
	function getSearchHeaders() { return { "AccessToken": getCookie( "access_token" ), "Version": "1.0" }; };
	this.getTrendingSearchKeywords = function( aCallBack ) {
		httpUtil.get( API_PREFIX + SEARCH_PREFIX + SEARCH_TRENDING_API,
			getSearchHeaders(),
			{ "language": "${ language }" },
			function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getInitialSearchResults = function( searchQuery, aCallBack ) {
		if( searchQuery == null ) return;
		httpUtil.get( API_PREFIX + SEARCH_PREFIX + SEARCH_CORE_API,
			getSearchHeaders(),
			{ "language": "${ language }", "text": searchQuery.trim() },
			function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getPratilipiSearchResults = function( searchQuery, cursor, resultCount, aCallBack ) {
		if( searchQuery == null || cursor == null ) return;
		httpUtil.get( API_PREFIX + SEARCH_PREFIX + SEARCH_CORE_API,
            getSearchHeaders(),
            { "language": "${ language }",
                "text": searchQuery.trim(),
                "pratilipiCursor": cursor,
                "pratilipiResultCount": resultCount != null ? resultCount : 20,
                "authorResultCount": 0 },
            function( response, status ) { processGetResponse( response, status, aCallBack ) } );
	};

	this.getAuthorSearchResults = function( searchQuery, cursor, resultCount, aCallBack ) {
		if( searchQuery == null || cursor == null ) return;
        httpUtil.get( API_PREFIX + SEARCH_PREFIX + SEARCH_CORE_API,
            getSearchHeaders(),
            { "language": "${ language }",
                "text": searchQuery.trim(),
                "authorCursor": cursor,
                "authorResultCount": resultCount != null ? resultCount : 10,
                "pratilipiResultCount": 0 },
            function( response, status ) { processGetResponse( response, status, aCallBack ) } );
    };

	/* POST Methods */
	this.createOrUpdateUser = function( userId, email, phone, successCallBack, errorCallBack ) {
		if( userId == null ) return;
		var params = { "userId": userId };
		if( email != null ) params[ "email" ] = email;
		if( phone != null ) params[ "phone" ] = phone;
		httpUtil.post( API_PREFIX + USER_API,
						null,
						params,
						function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.createOrUpdateAuthor = function( author, successCallBack, errorCallBack ) {
		if( author == null || isEmpty( author ) || author.authorId == null ) return;
		httpUtil.post( API_PREFIX + AUTHOR_API,
						null,
						author,
						function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.createOrUpdateReview = function( pratilipiId, rating, review, successCallBack, errorCallBack ) {
		if( pratilipiId == null ) return;
		var params = { "pratilipiId": pratilipiId };
		if( rating != null ) params[ "rating" ] = rating;
		if( review != null ) {
			params[ "review" ] = review; 
			params[ "reviewState" ]= "PUBLISHED";
		}
		httpUtil.post( API_PREFIX + USER_PRATILIPI_REVIEW_API,
				null,
				params, 
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.createOrUpdateReviewComment = function( userPratilipiId, commentId, content, successCallBack, errorCallBack ) {
		if( userPratilipiId == null && commentId == null ) return;
		var params = { "state": "ACTIVE" };
		if( userPratilipiId != null ) { params[ "parentId" ] = userPratilipiId; params[ "parentType" ] = "REVIEW"; }
		if( commentId != null ) params[ "commentId" ] = commentId;
		if( content != null ) params[ "content" ] = content;
		httpUtil.post( API_PREFIX + COMMENT_API,
				null,
				params, 
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	},

	this.followOrUnfollowAuthor = function( authorId, following, successCallBack, errorCallBack ) {
		if( authorId == null || following == null ) return;
		httpUtil.post( API_PREFIX + USER_AUTHOR_FOLLOW_API,
				null,
				{ "authorId": authorId, "state": following ? "FOLLOWING" : "UNFOLLOWED" }, 
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};
	
	this.addOrRemoveFromLibrary = function( pratilipiId, addedToLib, successCallBack, errorCallBack ) {
		if( pratilipiId == null || addedToLib == null ) return;
		httpUtil.post( API_PREFIX + USER_PRATILIPI_LIBRARY_API,
				null,
				{ "pratilipiId": pratilipiId, "addedToLib": addedToLib }, 
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.likeOrDislikeReview = function( userPratilipiId, isLiked, successCallBack, errorCallBack ) {
		if( userPratilipiId == null || isLiked == null ) return;
		httpUtil.post( API_PREFIX + VOTE_API,
				null,
				{ "parentId": userPratilipiId, "parentType": "REVIEW", "type": isLiked ? "LIKE" : "NONE" }, 
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.likeOrDislikeComment = function( commentId, isLiked, successCallBack, errorCallBack ) {
		if( commentId == null || isLiked == null ) return;
		httpUtil.post( API_PREFIX + VOTE_API,
				null,
				{ "parentId": commentId, "parentType": "COMMENT", "type": isLiked ? "LIKE" : "NONE" }, 
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.deleteReview = function( pratilipiId, successCallBack, errorCallBack ) {
		if( pratilipiId == null ) return;
		var params = { "pratilipiId": pratilipiId, "reviewState": "DELETED" };
		httpUtil.post( API_PREFIX + USER_PRATILIPI_REVIEW_API,
				null,
				params,
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.deleteComment = function( commentId, successCallBack, errorCallBack ) {
		if( commentId == null ) return;
		httpUtil.post( API_PREFIX + COMMENT_API,
				null,
				{ "commentId": commentId, "state": "DELETED" }, 
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.createOrUpdatePratilipi = function( pratilipi, successCallBack, errorCallBack ) {
		if( pratilipi == null ) return;
		if( pratilipi[ "pratilipiId" ] == null ) pratilipi[ "oldContent" ] = false;
		httpUtil.post( API_PREFIX + PRATILIPI_API,
				null,
				pratilipi, 
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.loginUser = function( email, password, successCallBack, errorCallBack ) {
		if( email == null || password == null ) return;
		httpUtil.post( API_PREFIX + USER_LOGIN_API,
				null,
				{ "email": email, "password": password },
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.loginGoogleUser = function( googleIdToken, successCallBack, errorCallBack ) {
		if( googleIdToken == null ) return;
		httpUtil.post( API_PREFIX + USER_LOGIN_GOOGLE_API,
				null,
				{ "googleIdToken": googleIdToken },
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.loginFacebookUser = function( fbUserAccessToken, successCallBack, errorCallBack ) {
		if( fbUserAccessToken == null ) return;
		httpUtil.post( API_PREFIX + USER_LOGIN_FACEBOOK_API,
				null,
				{ "fbUserAccessToken": fbUserAccessToken },
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.registerUser = function( name, email, password, successCallBack, errorCallBack ) {
		if( name == null || email == null || password == null ) return;
		httpUtil.post( API_PREFIX + USER_REGISTER_API,
				null,
				{ "name": name, "email": email, "password": password, "language": "${ language }" },
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.updateUserPassword = function( password, newPassword, successCallBack, errorCallBack ) {
		if( password == null || newPassword == null ) return;
		httpUtil.post( API_PREFIX + USER_PASSWORD_UPDATE_API,
				null,
				{ "password": password, "newPassword": newPassword },
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.forgotPassword = function( email, successCallBack, errorCallBack ) {
		if( email == null ) return;
		httpUtil.post( API_PREFIX + USER_EMAIL_API,
				null,
				{ "email": email, "sendPasswordResetMail": true },
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.resetUserPassword = function( email, verificationToken, newPassword, successCallBack, errorCallBack ) {
		if( email == null || verificationToken == null || newPassword == null ) return;
		httpUtil.post( API_PREFIX + USER_PASSWORD_UPDATE_API,
				null,
				{ "email": email, "verificationToken": verificationToken, "newPassword": newPassword },
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.logoutUser = function( successCallBack, errorCallBack ) {
		httpUtil.get( API_PREFIX + USER_LOGOUT_API,
				null,
				null,
				function( response, status ) {
					status == 200 ? successCallBack( response ) : errorCallBack( response ); /* Logout is different from all other cases */
		});
	};

	this.updateNotificationState = function( notificationId, state, successCallBack, errorCallBack ) {
		if( notificationId == null || state == null ) return;
		httpUtil.post( API_PREFIX + NOTIFICATION_API,
				null,
				{ "notificationId": notificationId, "state": state },
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};
	
	this.getTags = function( language, type, aCallBack ) {
		if( language == null || type == null ) return;
		httpUtil.get( API_PREFIX + TAGS_API,
				null,
				{ "type": type, "language": language },
				function( response, status ) { processGetResponse(response, status, aCallBack) });
	};
	
	this.updatePratilipiTags = function( pratilipiId, type, tagIds, suggestedTags, successCallBack, errorCallBack ) {
		if( pratilipiId == null ) return;
		httpUtil.post( API_PREFIX + PRATILIPI_TAGS_UPDATE_API,
				null,
				{ "pratilipiId": pratilipiId, "type": type, "tagIds": JSON.stringify( tagIds ), "suggestedTags": JSON.stringify( suggestedTags ) },
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

	this.reportContent = function( message, successCallBack, errorCallBack ) {
		if( message == null || message.trim() == "" ) return;
		httpUtil.post( API_PREFIX + CONTACT_API,
				null,
				{ "team": "AEE_${ language }", "message": message },
				function( response, status ) { processPostResponse( response, status, successCallBack, errorCallBack ) } );
	};

    this.getYesterdayTopAuthors = function(language, resultCount, aCallBack) {
        httpUtil.get(API_PREFIX + TOP_AUTHORS_API,
                null,
                {"language": language, "resultCount": resultCount},
                function(response, status) { processGetResponse(response, status, aCallBack) });
    };
};
