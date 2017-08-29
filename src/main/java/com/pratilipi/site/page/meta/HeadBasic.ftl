<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">

<#-- Page Title & Favicon -->
<title>${ title }</title>
<link rel="canonical" href="${ canonical_url }">
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />

<#-- DNS Prefetch -->
<#list 0..4 as i><link rel="dns-prefetch" href="//${i}.ptlp.co/"></#list>

<#-- Third-Party Library and Facebook OG Tags-->
<#list resourceList as resource>${ resource }</#list>

<#-- Load Facebook sdk -->
<script>
	window.fbAsyncInit = function() {
		FB.init({
			appId      : '293990794105516',
			cookie     : true,
			xfbml      : true,
			version    : 'v2.6' 
		});
	};
	(function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id)) return;
		js = d.createElement(s); js.id = id;
		js.src = "//connect.facebook.net/en_US/sdk.js";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
</script>

<#-- Load Clevertap -->

<script type="text/javascript">
     var clevertap = {event:[], profile:[], account:[], onUserLogin:[], notifications:[]};
     
	 clevertap.account.push({"id": "R8R-K59-K84Z"});
	 (function () {
			 var wzrk = document.createElement('script');
			 wzrk.type = 'text/javascript';
			 wzrk.async = true;
			 wzrk.src = ('https:' == document.location.protocol ? 'https://d2r1yp2w7bby2u.cloudfront.net' : 'http://static.clevertap.com') + '/js/a.js';
			 var s = document.getElementsByTagName('script')[0];
			 s.parentNode.insertBefore(wzrk, s);
	  })();
</script>

<#-- Google Analytics -->
<#include "GoogleAnalytics.ftl">

<#-- Font -->
<#include "Font.ftl">

<#-- Custom Stylesheet -->
<link type="text/css" rel="stylesheet" href="/resources/css/style-basic.css">

<#-- Firebase Script -->
<#include "Firebase.ftl">

<#-- Basic functions -->
<#include "Scripts.ftl">

<#-- Basic functions for mini-->
<#include "ScriptsMini.ftl">

<#if lang=="hi" && stage=="prod">
<#-- Facebook Pixel Code only on hindi pratilipi -->
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

<#-- Android Alerts on Website -->
<#include "AndroidAlertScript.ftl">
