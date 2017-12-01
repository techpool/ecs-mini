<#-- Load the Google Transliterate API -->
google.load( "elements", "1", {
	packages: "transliteration"
});

function onLoad() {
	var options = {
		sourceLanguage:
			google.elements.transliteration.LanguageCode.ENGLISH,
		destinationLanguage:
			[google.elements.transliteration.LanguageCode.${ language }],
		transliterationEnabled: true
	};

	var control = new google.elements.transliteration.TransliterationControl( options );
	control.makeTransliteratable( ['subtitle','chapter-content', 'title-vernacular', 'summary'] );

}

google.setOnLoadCallback( onLoad );
