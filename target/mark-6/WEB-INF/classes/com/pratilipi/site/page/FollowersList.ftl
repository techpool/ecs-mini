<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-followers-list-page">
		<#include "meta/Head.ftl">
	</head>

	<body>
		<pratilipi-followers-list-page 
			user-data='${ userJson }'
			author='${ authorJson! }'
			followers-object='${ followersObjectJson! }'
			pratilipi-types='${ pratilipiTypesJson }'
			navigation-list='${ navigationListJson }'
			language-map='${ languageMap }'></pratilipi-followers-list-page>

		<#include "meta/Foot.ftl">
	</body>
	
</html>