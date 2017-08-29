<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-following-list-page">
		<#include "meta/Head.ftl">
	</head>

	<body>
		<pratilipi-following-list-page 
			user-data='${ userJson }'
			author='${ authorJson! }'
			following-object='${ followingObjectJson! }'
			pratilipi-types='${ pratilipiTypesJson }'
			navigation-list='${ navigationListJson }'
			language-map='${ languageMap }'></pratilipi-following-list-page>

		<#include "meta/Foot.ftl">
	</body>
	
</html>