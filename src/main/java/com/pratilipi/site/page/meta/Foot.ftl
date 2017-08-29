<#-- Deffered ResourceList -->
<#list deferredResourceList as resource>${ resource }</#list>

<#-- Style CSS -->
<#include "Font.ftl">
<link type="text/css" rel="stylesheet" href="/resources/css/style.css">

<#-- Scroll Handler -->
<#if mainPage??>
	<script> var didScroll; var lastScrollTop = 0; var delta = 10; var navbarHeight = 75; window.onscroll = function() { var st = $(this).scrollTop(); document.querySelector( '${ mainPage }' ).scrollHandler( st ); if( Math.abs( lastScrollTop - st ) <= delta ) return; if( st > lastScrollTop && st > navbarHeight ) $( 'header' ).removeClass( 'nav-down' ).addClass( 'nav-up' ); else if( st + $(window).height() < $(document).height() || st < navbarHeight ) $( 'header' ).removeClass( 'nav-up' ).addClass( 'nav-down' ); lastScrollTop = st; };</script>
</#if>

<#-- Google Transliterate Api -->
<#include "GoogleTransliterate.ftl">

<#-- Android Alerts on Website -->
<#include "AndroidAlertScript.ftl">
