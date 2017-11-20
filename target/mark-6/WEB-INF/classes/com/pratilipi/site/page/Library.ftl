<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-library-page">
		<#include "meta/Head.ftl">
	</head>
	
	<body>
		<pratilipi-library-page 
			user-data='${ userJson }'
			pratilipi-list='${ pratilipiListJson! }'
			cursor='${ pratilipiListCursor! }'
			pratilipi-types='${ pratilipiTypesJson }'
			navigation-list='${ navigationListJson }'
			language-map='${ languageMap }'></pratilipi-library-page>

		<#include "meta/Foot.ftl">
	</body>

</html>