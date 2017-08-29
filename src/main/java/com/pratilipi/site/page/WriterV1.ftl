<!DOCTYPE html>
<html lang="${lang}">
	<head>
		<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>${ title }</title>
		<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
		<script src='https://0.ptlp.co/resource-all/jquery.bootstrap.polymer.firebase.jsapi.compressed.js'></script>
		<link rel='stylesheet' href='https://1.ptlp.co/third-party/bootstrap-3.3.4/css/bootstrap.min.css'>
		<#include "./meta/GoogleAnalytics.ftl">
		<#include "./meta/Scripts.ftl">
	</head>

	<#assign hasAccess = true>

	<#if user.isGuest() >
		<#assign hasAccess = false>
	<#elseif !pratilipiId?? >
		<#assign hasAccess = false>
	<#elseif !pratilipi.hasAccessToUpdate() >
		<#assign hasAccess = false>
	</#if>


	<#if hasAccess>
		<#include "../element/standard/writer-panel/writer-main-screen/index.html">
	<#else>
		<script>
			$( document ).ready(function() {
			    window.location.href = "/?action=start_writing";
			});
		</script>
	</#if>
	<#include "./meta/Font.ftl">
</html>