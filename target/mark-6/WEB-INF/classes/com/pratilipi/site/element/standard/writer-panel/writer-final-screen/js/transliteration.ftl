<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">

/* Load the Google Transliterate API */
google.load("elements", "1", {
      packages: "transliteration"
    });

function onLoad() {
  var options = {
      sourceLanguage:
          google.elements.transliteration.LanguageCode.ENGLISH,
      destinationLanguage:
          [google.elements.transliteration.LanguageCode.${ language }],
      /* shortcutKey: 'ctrl+g', */
      transliterationEnabled: true
  };

  var control =
      new google.elements.transliteration.TransliterationControl(options);

  control.makeTransliteratable(['summary']);

}
google.setOnLoadCallback(onLoad);
</script>