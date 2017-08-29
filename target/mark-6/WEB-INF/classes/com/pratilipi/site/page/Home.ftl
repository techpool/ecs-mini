<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-home-page">
		<#include "meta/Head.ftl">
	</head>

	<body>
		<pratilipi-home-page 
			user-data='${ userJson }'
			pratilipi-types='${ pratilipiTypesJson }'
			navigation-list='${ navigationListJson }'
			language-map='${ languageMap }'></pratilipi-home-page>
			
		<#include "meta/Foot.ftl">
	</body>

</html>