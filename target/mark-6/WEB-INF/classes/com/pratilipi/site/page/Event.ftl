<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-event-page">
		<#include "meta/Head.ftl">
	</head>

	<body>
		<pratilipi-event-page 
			user-data='${ userJson }'
			event='${ eventJson }'
			pratilipi-types='${ pratilipiTypesJson }'
			navigation-list='${ navigationListJson }'
			language-map='${ languageMap }'></pratilipi-event-page>

		<#include "meta/Foot.ftl">

    </body>


</html>