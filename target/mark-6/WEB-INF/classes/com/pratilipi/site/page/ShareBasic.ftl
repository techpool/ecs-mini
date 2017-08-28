<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#-- Page Description -->
		<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">
		
		<#include "meta/HeadBasic.ftl">
	</head>
	
	<body>
		<script>
			function shareOnFacebook() {
				var url = encodeURIComponent( getUrlParameter( "url" ) );
				url += encodeURIComponent( "&utm_source=facebook" );
				window.open( "http://www.facebook.com/sharer.php?u=" + url, 
				"share", "width=600,height=500,left=70px,top=60px" );
			}
			function shareOnTwitter() {
				var url = encodeURIComponent( getUrlParameter( "url" ) );
				url += encodeURIComponent( "&utm_source=twitter" );	
				window.open( "http://twitter.com/share?url=" + url ,
				"share", "width=500,height=600,left=70px,top=60px" );
			}
			function shareOnGplus() {
				var url = encodeURIComponent( getUrlParameter( "url" ) );
				url += encodeURIComponent( "&utm_source=gplus" );
				window.open( "https://plus.google.com/share?url=" + url, 
				"share", "width=500,height=600,left=70px,top=60px" );
			}
			function shareOnWhatsapp() {
				var url = encodeURIComponent( getUrlParameter( "url" ) );
				var type = getParameterByName( "utm_campaign", getUrlParameter( "url" ) );
				var name = encodeURIComponent( getUrlParameter( "name" ) );
				var text = "";
				if( type == "content_share" ) {
					text = "%22" + name + "%22${ _strings.whatsapp_read_story?url('UTF-8') }%20" + url +"%0A${ _strings.whatsapp_read_unlimited_stories }";
				} else {
					text = "%22" + name + "%22${ _strings.whatsapp_follow_author?url('UTF-8') }%20" + url +"%0A${ _strings.whatsapp_read_unlimited_stories }";
				}
				window.location.href = ( "whatsapp://send?text=" + text );
			}
		</script>
		<div class="secondary-500 pratilipi-shadow" style="display: block; padding: 5px; height: 64px;">
			<a style="cursor: pointer; position: absolute; right: 16px; top: 20px;" onClick="history.back();return false;">
				<div class="sprites-icon black-cross-icon"></div>
			</a>
		</div>		
		<div class="parent-container" style="padding-top: 0px;">
			<div class="container">
				<div class="secondary-500 pratilipi-shadow box">
					<a class="menu-item" style="cursor: pointer;" onclick="shareOnFacebook()" >
						<div class="sprites-icon reader-setting-icon fb-black-icon"></div>
						<span>${ _strings.share_on_facebook }</span>
					</a>
					<a class="menu-item" style="cursor: pointer;" onclick="shareOnTwitter()" >
						<div class="sprites-icon reader-setting-icon twitter-black-icon"></div>
						<span>${ _strings.share_on_twitter }</span>
					</a>
					<a class="menu-item" style="cursor: pointer;" onclick="shareOnGplus()">
						<div class="sprites-icon reader-setting-icon gplus-black-icon"></div>
						<span>${ _strings.share_on_gplus }</span>
					</a>
					<a class="menu-item" style="cursor: pointer;" onclick="shareOnWhatsapp()" >
						<div class="sprites-icon reader-setting-icon whatsapp-black-icon"></div>
						<span>${ _strings.share_on_whatsapp }</span>
					</a>				
				</div>	
			</div>
		</div>
		<#include "../element/basic/pratilipi-footer.ftl">
	</body>
	
</html>