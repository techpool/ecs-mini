<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-pratilipi-page">
		<#include "meta/Head.ftl">
	</head>
	
	<body>
		<pratilipi-pratilipi-page 
				user-data='${ userJson }'
				pratilipi-id='${ pratilipi.getId()?c }'
				pratilipi-data='${ pratilipiJson }'
				userpratilipi-data='${ userpratilipiJson! }'
				pratilipi-types='${ pratilipiTypesJson }'
				navigation-list='${ navigationListJson }'
				language-map='${ languageMap }'
				stage="${ stage }"></pratilipi-pratilipi-page>

		<#include "meta/Foot.ftl">
	</body>
	
</html>