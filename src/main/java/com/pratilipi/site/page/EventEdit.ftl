<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-edit-event">
		<#include "meta/Head.ftl">
	</head>

	<body>
		<link rel='import' href='/elements.${lang}/pratilipi-edit-event.html'>
		<pratilipi-edit-event event='<#if eventJson??>${ eventJson }<#else>{}</#if>'></pratilipi-edit-event>
		<#include "meta/Font.ftl">
    </body>

</html>