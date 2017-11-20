<script type="text/javascript">
	google.load( "elements", "1", {
		packages: "transliteration"
	});
	function onLoad() {
		if( google.elements.transliteration.isBrowserCompatible() ) {
			var options = {
				sourceLanguage:
					google.elements.transliteration.LanguageCode.ENGLISH,
				destinationLanguage:
					[google.elements.transliteration.LanguageCode.${language}],
				shortcutKey: 'ctrl+g',
				transliterationEnabled: true
			};
	    	var control = new google.elements.transliteration.TransliterationControl( options );
	    	var allTextBoxes = $( "[name='pratilipiInputTransliterable'], textarea" ).toArray();
			control.makeTransliteratable( allTextBoxes );
		} 
	}
	google.setOnLoadCallback( onLoad );
</script>