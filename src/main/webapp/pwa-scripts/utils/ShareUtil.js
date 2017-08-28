/* Global Variables */
var ga_category = null;
var shareUrl = null;
var shareWhatsappText = null;
var pratilipiForEvent = null;
var authorForEvent = null;
var fbEventParams = null;
/* Share Modal */
function viewModel() {

	var self = this;

	self.shareOnFacebook = function() {
		ga_CA( ga_category, 'FB-Share' );
        self.sendFBEvent(ga_category, 'FB');
		window.open( "http://www.facebook.com/sharer.php?u=" + shareUrl, "share", "width=1100,height=500,left=70px,top=60px" );
	};

	self.shareOnTwitter = function() {
		ga_CA( ga_category, 'Twitter-Share' );
		self.sendFBEvent(ga_category, 'TWITTER');
		window.open( "http://twitter.com/share?url=" + shareUrl, "share", "width=1100,height=500,left=70px,top=60px" );
	};

	self.shareOnGplus = function() {
		ga_CA( ga_category, 'G+-Share' );
		self.sendFBEvent(ga_category, 'G+');
		window.open( "https://plus.google.com/share?url=" + shareUrl, "share", "width=1100,height=500,left=70px,top=60px" );
	};

	self.shareOnWhatsapp = function() {
		ga_CA( ga_category, 'Whatsapp-Share' );
		window.open( "whatsapp://send?text=" + shareWhatsappText );
	};

    self.sendFBEvent = function(ga_category, entity_value) {
        /* FB Event for Pratilipi Share from pratilipi page only */
        if (getLocation().ga_value == "PratilipiPage" && ga_category == "Pratilipi") {
            fbEvent('SHARE_CONTENT', entity_value);
        } else if (getLocation().fb_value && ga_category == 'Author') {
            fbAuthorEvent('SHARE_AUTHOR', entity_value);
        }
    };
}

ko.components.register( 'pratilipi-share-dialog', {
	viewModel: viewModel,
    template: <@add_backslashes><#include "../../pwa-elements/pratilipi-share-dialog/pratilipi-share-dialog.html"></@add_backslashes>
});

var shareDialog = document.createElement( 'pratilipi-share-dialog' );
shareDialog.style.zIndex = "1051";
shareDialog.style.display = "none";
document.body.appendChild( shareDialog );

var ShareUtil = (function() {
	return {
		sharePratilipi: function( pratilipi, utmParams ) {
		    pratilipiForEvent = pratilipi;
			var getShareUrl = function() {
				return window.location.origin + pratilipi.pageUrl;
			};
			var getWhatsappText = function() {
				return '"' + pratilipi.title + '"${ _strings.whatsapp_read_story } ' + getShareUrl() +" ${ _strings.whatsapp_read_unlimited_stories }";
			};
			MetaTagUtil.setMetaTagsForPratilipi( pratilipi );
			ga_category = "Pratilipi";
			if (getLocation().ga_value == "PratilipiPage") {
                fbEvent("LANDED_SHARE_CONTENT", null);
			}
			this.share( getShareUrl(), getWhatsappText(), utmParams );
		},
		shareAuthor: function( author, utmParams, eventParams ) {
			var getShareUrl = function() {
				return window.location.origin + author.pageUrl;
			};
			var getWhatsappText = function() {
				return '"' + author.name + '"${ _strings.whatsapp_read_story } ' + getShareUrl() +" ${ _strings.whatsapp_read_unlimited_stories }";
			};
			MetaTagUtil.setMetaTagsForAuthor( author );
			ga_category = "Author";
            authorForEvent = author;
            fbEventParams = eventParams;
			if (getLocation().fb_value)
			    fbAuthorEvent("LANDED_SHARE_AUTHOR", null);

			this.share( getShareUrl(), getWhatsappText(), utmParams );
		},
		share: function( url, whatsappText, utmParams ) {
			shareDialog.style.display = "block";
			shareUrl = encodeURIComponent( url + ( utmParams != null ? "?" + new HttpUtil().formatParams( utmParams ) : "" ) );
			shareWhatsappText = whatsappText != null ? encodeURIComponent( whatsappText ) : null;
			$( "#pratilipi-share-dialog" ).modal();
		}
	};
})();

fbEvent = function(event_name, entity_value) {
    var params = {};
    params['ENVIRONMENT'] = 'PROD';
    params['CONTENT_LANGUAGE'] = pratilipiForEvent.language;
    params['SCREEN_NAME'] = getLocation().fb_value;
    params['LOCATION'] = 'MAIN_SHARE_POPUP';
    params['USERID'] = appViewModel.user.userId();
    params['PRATILIPI_TYPE'] =  pratilipiForEvent.type;
    params['CONTENT_ID'] =  pratilipiForEvent.pratilipiId;
    params['AUTHOR_ID'] =  pratilipiForEvent.author.authorId;
    params['ACCESS_LEVEL'] = appViewModel.user.author.authorId() == pratilipiForEvent.author.authorId ? 'SELF' : 'OTHER';
    if (entity_value != null)
        params['ENTITY_VALUE'] =  entity_value;

    FB.AppEvents.logEvent(event_name, null, params);
};

fbAuthorEvent = function(event_name, entity_value) {
    var params = {};
    params['ENVIRONMENT'] = 'PROD';
    params['CONTENT_LANGUAGE'] = appViewModel.language;
    params['SCREEN_NAME'] = getLocation().fb_value;
    params['LOCATION'] = 'MAIN_SHARE_POPUP';
    params['USERID'] = appViewModel.user.userId();
    params['AUTHOR_ID'] =  authorForEvent.authorId;
    params['ACCESS_LEVEL'] = appViewModel.user.author.authorId() == authorForEvent.authorId ? 'SELF' : 'OTHER';
    if (entity_value != null)
        params['ENTITY_VALUE'] =  entity_value;

    if (fbEventParams) {
        var keys = Object.keys(fbEventParams);
        for (var i=0; i<keys.length; ++i) {
            var key = keys[i];
            params[key] = fbEventParams[key];
        }
    }
    FB.AppEvents.logEvent(event_name, null, params);
};
