<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">

<#-- Page Title & Favicon -->
<title>${ title }</title>
<link rel="canonical" href="${ canonical_url }">
<link rel="alternate" media="only screen and (max-width: 640px)" href="${ alternate_url }">
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />

<#-- DNS Prefetch -->
<#list 0..4 as i><link rel="dns-prefetch" href="//${i}.ptlp.co/"></#list>

<#-- Third-Party Library and Facebook OG Tags-->
<#list resourceList as resource>${ resource }</#list>

<#-- Google Analytics -->
<#include "GoogleAnalytics.ftl">

<#-- Polymer Dependencies -->
<#if mainPage??><#include "PolymerDependencies.ftl"></#if>

<#-- Firebase Script -->
<#include "Firebase.ftl">

<#-- Basic functions -->
<#include "Scripts.ftl">

<#-- Google Login -->
<script src="https://apis.google.com/js/api:client.js"></script>

<#-- Facebook Pixel Code only on hindi pratilipi -->
<#if lang=="hi" && stage=="prod">
<script>
!function(f,b,e,v,n,t,s){if(f.fbq)return;n=f.fbq=function(){n.callMethod?
n.callMethod.apply(n,arguments):n.queue.push(arguments)};if(!f._fbq)f._fbq=n;
n.push=n;n.loaded=!0;n.version='2.0';n.queue=[];t=b.createElement(e);t.async=!0;
t.src=v;s=b.getElementsByTagName(e)[0];s.parentNode.insertBefore(t,s)}(window,
document,'script','https://connect.facebook.net/en_US/fbevents.js');

fbq('init', '1569748966613739');
fbq('track', "PageView");</script>
<noscript><img height="1" width="1" style="display:none"
src="https://www.facebook.com/tr?id=1569748966613739&ev=PageView&noscript=1"
/></noscript>
</#if>
<script>function trackPixelEvents(eventName){if(typeof(fbq)!='undefined')fbq('trackCustom',eventName);}</script>

<#-- Clevertap Script -->
<script type="text/javascript">
var clevertap = {event:[], profile:[], account:[], onUserLogin:[], notifications:[]};
clevertap.account.push({"id": "TEST-Z88-4ZZ-974Z"});
(function () {
 var wzrk = document.createElement('script');
 wzrk.type = 'text/javascript';
 wzrk.async = true;
 wzrk.src = ('https:' == document.location.protocol ? 'https://d2r1yp2w7bby2u.cloudfront.net' : 'http://static.clevertap.com') + '/js/a.js';
 var s = document.getElementsByTagName('script')[0];
 s.parentNode.insertBefore(wzrk, s);
})();
</script>

<#-- Custom events for cleartap -->
<script>
	<#if user.isGuest() == false>
	clevertap.profile.push({
		"site": {
			"name": "${ user.getDisplayName() }",
			"id": "${ user.getId()?c }",
			"email": "${ user.getEmail()! }",            
			"language": "${ language }"
		}
	});
	<#if isContentPage?? && isContentPage==true>
		clevertap.event.push( "content_landed", {
			"content_title": "${ pratilipi.getTitle() }",
			"content_id": "${ pratilipi.getId()?c }",
			"author_name": "<#if pratilipi.getAuthor() ??>${ pratilipi.getAuthor().getName() }</#if>",
			"author_id": "<#if pratilipi.getAuthor() ??>${ pratilipi.getAuthor().getId()?c }</#if>"
		});
	</#if>
	function clevertapShareContentOnFacebook( title, pratilipiId ) {
		clevertap.event.push( "content_shared", {
			"content_title": title,
			"content_id": pratilipiId,
			"share_app": "Facebook"
		});
	}
	<#else>
		function clevertapShareContentOnFacebook( title, pratilipiId ) {
			/* Do Nothing */
		}
	</#if>
</script>
