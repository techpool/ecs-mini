<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-author-list-page">
		<#include "meta/Head.ftl">
	</head>

	<body>
		<link rel='import' href='/elements.${lang}/pratilipi-author-list-page.html?3'>
		<pratilipi-author-list-page 
			user-data='${ userJson }'
			author-list='${ authorListJson }'
			filter='${ authorListFilterJson! }'
			cursor='${ authorListCursor! }'
			pratilipi-types='${ pratilipiTypesJson }'
			language-map='${ languageMap }'></pratilipi-author-list-page>

			<#include "meta/Foot.ftl">
	</body>
	
</html>