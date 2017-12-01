<#assign pageUrl="read">

<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#include "meta/HeadBasic.ftl">
		<script>
			function gotoPage( pageNo ) {
				var redirectUrl = "${ pratilipi.getReadPageUrl() }" + ( "${ pratilipi.getReadPageUrl() }".indexOf( "?" ) == -1 ? "?" : "&" ) + "pageNo=" + pageNo;

				if( getUrlParameter( "ret" ) != null )
					redirectUrl = redirectUrl + "&" + "ret=" + getUrlParameter( "ret" );
					
				<#-- Set cookie -->
				setCookie( "reader_page_number_${ pratilipi.getId()?c }", pageNo, 30, "/${ pageUrl }" );

				window.location.href = redirectUrl;

			}
			function getUrl() {
				var url = "${ pratilipi.getReadPageUrl() }" + ( "${ pratilipi.getReadPageUrl() }".indexOf( "?" ) == -1 ? "?" : "&" ) + "pageNo=" + "${ pageNo }";
							
				if( getUrlParameter( "ret" ) != null )
					url = url + "&" + "ret=" + getUrlParameter( "ret" );

				return url;
			}
			<#if action != "index" && action != "share" && action != "setting">
				$( document ).ready(function() {
					if( getUrlParameter( "addToLib" ) == "true" )
						addToLibrary();
				});
				function gotoNavigation() {
					window.location.href = getUrl() + "&action=index";
				}
				function gotoShare() {
					window.location.href = getUrl() + "&action=share";
				}
				function gotoSetting() {
					window.location.href = getUrl() + "&action=setting";
				}
				function loadPrevious() {
					gotoPage( ${ pageNo - 1 } );
				}
				function loadNext() {
					gotoPage( ${ pageNo + 1 } );
				}
				function exitReader() {
					window.location.href = getUrlParameter( "ret" ) != null ? decodeURIComponent( getUrlParameter( "ret" ) ) : "${ pratilipi.getPageUrl() }";
				}
			</#if>
			
			<#if action == "setting">
				function increaseFontSize() {
					<#--  cutoff = 32 -->
					if( ${ fontSize + 2 } <= 32 ) {
						setCookie( "fontSize", ${ fontSize + 2 }, 30, "/${ pageUrl }" );
						window.location.href = getUrl();
					}
				}
				function decreaseFontSize() {
					<#--  cutoff = 12 -->
					if( ${ fontSize - 2 } >= 12 ) {
						setCookie( "fontSize", ${ fontSize - 2 }, 30, "/${ pageUrl }" );
						window.location.href = getUrl();
					}
				}
				function increaseImageSize() {
					<#--  cutoff = 1500 -->
					if( ${ imageSize + 50 } <= 1500 ) {
						setCookie( "imageSize", ${ imageSize + 50 }, 30, "/${ pageUrl }" );
						window.location.href = getUrl();
					}
				}
				function decreaseImageSize() {
					<#--  cutoff = 300 -->
					if( ${ imageSize - 50 } >= 300 ) {
						setCookie( "imageSize", ${ imageSize - 50 }, 30, "/${ pageUrl }" );
						window.location.href = getUrl();
					}
				}
				function addToOrRemoveFromLibrary( flag ) {
					$.ajax({
							
						type: 'post',
						url: '/api/userpratilipi/library',
			
						data: { 
							'pratilipiId': ${ pratilipi.getId()?c }, 
							'addedToLib': flag
						},
						
						success: function( response ) {
							if( flag )
								alert( "${ _strings.added_to_library }" );
							else
								alert( "${ _strings.removed_from_library }" );
							window.location.href = getUrl();
						},
						
						error: function( response ) {
							var message = jQuery.parseJSON( response.responseText );
							var status = response.status;
			
							if( message["message"] != null )
								alert( "Error " + status + " : " + message["message"] );
							else if( message["error"] != null )
								alert( "Error " + status + " : " + message["error"] ); 
							else
								alert( "Some exception occured at the server! Please try again." );
						}
					});
				}
				function addToLibrary() {
					addToOrRemoveFromLibrary( true );
				}
				function removeFromLibrary() {
					addToOrRemoveFromLibrary( false );
				}
			</#if>

			<#if action == "share" || pageNo == pageCount>
				function getShareUrl( utm_location, utm_source ) {
					if( utm_location == null || utm_source == null ) return;
					return encodeURIComponent( 
							"http://${ language?lower_case }.pratilipi.com" + "${ pratilipi.getPageUrl() }" 
							+ ( "${ pratilipi.getPageUrl() }".indexOf( '?' ) == -1 ? '?' : '&' )
							+ "utm_language=${ language?lower_case }" + "&"
							+ "utm_version=lite" + "&"
							+ "utm_device=mobile" + "&"
							+ "utm_parent=reader" + "&"
							+ "utm_location=" + utm_location + "&"
							+ "utm_action=share" + "&"
							+ "utm_source=" + utm_source
						);
				}
				function shareOnFacebook( utm_location ) {
					window.open( "http://www.facebook.com/sharer.php?u=" + getShareUrl( utm_location, "facebook" ),
						"share", "width=1100,height=500,left=70px,top=60px" );
				}
				function shareOnTwitter( utm_location ) {
					window.open( "http://twitter.com/share?url=" + getShareUrl( utm_location, "twitter" ),
						"share", "width=1100,height=500,left=70px,top=60px" );
				}
				function shareOnGplus( utm_location ) {
					window.open( "https://plus.google.com/share?url=" + getShareUrl( utm_location, "gplus" ),
						"share", "width=1100,height=500,left=70px,top=60px" );
				}
			</#if>
		</script>
		<style type="text/css" media="print"> * { visibility: hidden; display: none; } </style>
		<script language=JavaScript>
			$( document ).ready( function() { $( document ).on( "contextmenu",function() { return false; }); $( document ).mousedown( function(e) { if( e.button == 2 ) return false; else return true; }); }); $(document).keyup(function(e){ if(e.keyCode == 44) return false; }); document.onkeydown = function(e) { var isCtrl = false; if(e.which == 17) isCtrl = true; if(( ( e.which == 67 ) || ( e.which == 80 ) ) && isCtrl == true) return false; };
			function clickIE4(){ if (event.button==2){ return false; } } function clickNS4(e){ if (document.layers||document.getElementById&&!document.all){ if (e.which==2||e.which==3){ return false; } } } if (document.layers){ document.captureEvents(Event.MOUSEDOWN); document.onmousedown=clickNS4; } else if (document.all&&!document.getElementById){ document.onmousedown=clickIE4; } document.oncontextmenu=new Function( "return false" )
		</script>
	</head>

	<body ondragstart="return false" onselectstart="return false" onContextMenu="return false" onkeydown="if ((arguments[0] || window.event).ctrlKey) return false">
		<#if action != "index" && action != "share" && action != "setting">
			<#include "../element/basic/pratilipi-reader-header.ftl">
		<#else>
			<div class="secondary-500 pratilipi-shadow" style="display: block; padding: 5px; height: 64px;">
				<a style="cursor: pointer; position: absolute; right: 16px; top: 20px;" onClick="history.back();return false;">
					<div class="sprites-icon black-cross-icon"></div>
				</a>
			</div>
		</#if>
		<div style="padding-top: 0;" class="parent-container">
			<div class="container">
				<#if action == "index">
					<#include "../element/basic/pratilipi-reader-navigation.ftl">
				<#elseif action == "share">
					<#include "../element/basic/pratilipi-reader-social.ftl">
				<#elseif action == "setting">
					<#include "../element/basic/pratilipi-reader-setting.ftl">
				<#else>
					<#include "../element/basic/pratilipi-reader-content.ftl">
				</#if>
				
			</div>
		</div>
		<#if action != "index" && action != "share" && action != "setting">
			<#include "../element/basic/pratilipi-reader-footer.ftl">
		</#if>
		<#include "../element/basic/pratilipi-footer.ftl">
	</body>
	
</html>