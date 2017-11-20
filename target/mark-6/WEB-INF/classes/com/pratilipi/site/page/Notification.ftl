<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-notification-page">
		<#include "meta/Head.ftl">
	</head>

	<body>
		<pratilipi-notification-page 
			user-data='${ userJson }'
			notification-object='${ notificationListJson }'
			pratilipi-types='${ pratilipiTypesJson }'
			navigation-list='${ navigationListJson }'
			language-map='${ languageMap }'></pratilipi-notification-page>

		<#include "meta/Foot.ftl">
	</body>

</html>