<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-blog-page">
		<#include "meta/Head.ftl">
	</head>

	<body>
		<pratilipi-blog-page 
			user-data='${ userJson }'
			blogpost='${ blogPostJson }'
			pratilipi-types='${ pratilipiTypesJson }'
			navigation-list='${ navigationListJson }'
			language-map='${ languageMap }'></pratilipi-blog-page>

		<#include "meta/Foot.ftl">
    </body>

</html>