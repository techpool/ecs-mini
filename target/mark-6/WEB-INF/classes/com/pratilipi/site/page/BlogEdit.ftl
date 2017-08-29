<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-edit-blog">
		<#include "meta/Head.ftl">
	</head>

	<body>
		<link rel='import' href='/elements.${lang}/pratilipi-edit-blog.html?2'>
		<pratilipi-edit-blog 
			blog='<#if blogPostJson??>${ blogPostJson }<#else>{}</#if>'
			blog-id="${ blogId?c }"></pratilipi-edit-blog>
		<#include "meta/Font.ftl">
    </body>

</html>