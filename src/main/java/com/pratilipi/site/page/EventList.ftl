<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-event-list-page">
		<#include "meta/Head.ftl">
	</head>

	<body>
		<pratilipi-event-list-page 
			user-data='${ userJson }'
			event-list='${ eventListJson }'
			has-access-to-add=${ hasAccessToAdd?c }
			pratilipi-types='${ pratilipiTypesJson }'
			navigation-list='${ navigationListJson }'
			language-map='${ languageMap }'></pratilipi-event-list-page>

		<#include "meta/Foot.ftl">

    </body>

</html>