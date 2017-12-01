function(params) {
    var self = this;
    var dataAccessor = new DataAccessor();

    var RESULT_COUNT = 20;

    self.yesterdayTopAuthors = ko.observableArray([]);
    self.isLoaded = ko.observable(false);
    self.subHeading = ko.observable();

    self.listItemClicked = function(data, event) {
        console.log(data);
        window.open(data.pageUrl(),'_blank');
        self.FBEvent('CLICK_AUTHOR', data.authorId(), appViewModel.user.author.authorId() == data.authorId ? 'SELF' : 'OTHER');
    };

    self.sharePratilipi = function( data, event ) {
        console.log("Share button clicked");
        event.stopPropagation();
        var eventParams = {};
        eventParams['ENVIRONMENT'] = 'GROWTH';
        eventParams['PARENT'] = 'TOP AUTHOR';
        eventParams['LOCATION'] = 'DESKTOP_RIGHTBAR';
        eventParams['EXPERIMENT_ID'] = 'DIA070617A';
        var utmParameter = "utm_source=pratilipi_website&utm_medium=top_authors&utm_campaign=author_share";
        ShareUtil.shareAuthor( ko.mapping.toJS(data), utmParameter, eventParams );
    };

    self.FBEvent = function(event_name, author_id, access_level) {
        var eventParams = {};
        eventParams['ENVIRONMENT'] = 'GROWTH';
        eventParams['PARENT'] = 'TOP AUTHOR';
        eventParams['CONTENT_LANGUAGE'] = appViewModel.language;
        eventParams['LOCATION'] = 'DESKTOP_RIGHTBAR';
        eventParams['EXPERIMENT_ID'] = 'DIA070617A';
        eventParams['SCREEN_NAME'] = getLocation().fb_value;
        eventParams['USERID'] = appViewModel.user.userId();
        if (author_id)
            eventParams['AUTHOR_ID'] = author_id;
        if (access_level)
            eventParams['ACCESS_LEVEL'] = access_level;

        FB.AppEvents.logEvent(event_name, null, eventParams);

    };

    self.onSuccess = function(data) {
        /* setting author list. */
        ko.mapping.fromJS(data.authorDataList, {}, self.yesterdayTopAuthors);
        if (self.yesterdayTopAuthors().length > 0) {
            console.log(self.yesterdayTopAuthors().length);
            self.isLoaded(true);
            self.subHeading("( " + data.date + " ${ _strings.top_author_sub_heading }" + " *)");
            self.FBEvent('LANDED_PARENT');
        }
    };


    if (appViewModel.language == 'HINDI') {
        /* fetching yesterday's top 10 authors */
        dataAccessor.getYesterdayTopAuthors(
            appViewModel.language,
            RESULT_COUNT,
            function(response) {
                if (response != null) {
                    self.onSuccess(response);
                }

            }
        );
    }
}