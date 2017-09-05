package com.pratilipi.common.type;

public enum Website {

	ALL_LANGUAGE	(            "www.pratilipi.com",  "m.pratilipi.com", Language.ENGLISH,		null,               false ),
	HINDI			(          "hindi.pratilipi.com", "hi.pratilipi.com", Language.HINDI,		Language.HINDI,     false ),
	GUJARATI		(       "gujarati.pratilipi.com", "gu.pratilipi.com", Language.GUJARATI,	Language.GUJARATI,  false ),
	TAMIL			(          "tamil.pratilipi.com", "ta.pratilipi.com", Language.TAMIL,		Language.TAMIL,     false ),
	MARATHI			(        "marathi.pratilipi.com", "mr.pratilipi.com", Language.MARATHI,		Language.MARATHI,   false ),
	MALAYALAM		(      "malayalam.pratilipi.com", "ml.pratilipi.com", Language.MALAYALAM,	Language.MALAYALAM, false ),
	BENGALI			(        "bengali.pratilipi.com", "bn.pratilipi.com", Language.BENGALI,		Language.BENGALI,   false ),
	KANNADA			(        "kannada.pratilipi.com", "kn.pratilipi.com", Language.KANNADA,		Language.KANNADA,   false ),
	TELUGU			(         "telugu.pratilipi.com", "te.pratilipi.com", Language.TELUGU,		Language.TELUGU,    false ),

	GAMMA_ALL_LANGUAGE	(       "www-gamma.pratilipi.com",  "m-gamma.pratilipi.com", Language.ENGLISH,		null,               true ),
	GAMMA_HINDI			(     "hindi-gamma.pratilipi.com", "hi-gamma.pratilipi.com", Language.HINDI,		Language.HINDI,     true ),
	GAMMA_GUJARATI		(  "gujarati-gamma.pratilipi.com", "gu-gamma.pratilipi.com", Language.GUJARATI,		Language.GUJARATI,  true ),
	GAMMA_TAMIL			(     "tamil-gamma.pratilipi.com", "ta-gamma.pratilipi.com", Language.TAMIL,		Language.TAMIL,     true ),
	GAMMA_MARATHI		(   "marathi-gamma.pratilipi.com", "mr-gamma.pratilipi.com", Language.MARATHI,		Language.MARATHI,   true ),
	GAMMA_MALAYALAM		( "malayalam-gamma.pratilipi.com", "ml-gamma.pratilipi.com", Language.MALAYALAM,	Language.MALAYALAM, true ),
	GAMMA_BENGALI		(   "bengali-gamma.pratilipi.com", "bn-gamma.pratilipi.com", Language.BENGALI,		Language.BENGALI,   true ),
	GAMMA_KANNADA		(   "kannada-gamma.pratilipi.com", "kn-gamma.pratilipi.com", Language.KANNADA,		Language.KANNADA,   true ),
	GAMMA_TELUGU		(    "telugu-gamma.pratilipi.com", "te-gamma.pratilipi.com", Language.TELUGU,		Language.TELUGU,    true ),

	GAMMA_ALL_LANGUAGE_GR	(       "www-gamma-gr.pratilipi.com",  "m-gamma-gr.pratilipi.com", Language.ENGLISH,		null,               true ),
	GAMMA_HINDI_GR			(     "hindi-gamma-gr.pratilipi.com", "hi-gamma-gr.pratilipi.com", Language.HINDI,		Language.HINDI,     true ),
	GAMMA_GUJARATI_GR		(  "gujarati-gamma-gr.pratilipi.com", "gu-gamma-gr.pratilipi.com", Language.GUJARATI,		Language.GUJARATI,  true ),
	GAMMA_TAMIL_GR			(     "tamil-gamma-gr.pratilipi.com", "ta-gamma-gr.pratilipi.com", Language.TAMIL,		Language.TAMIL,     true ),
	GAMMA_MARATHI_GR		(   "marathi-gamma-gr.pratilipi.com", "mr-gamma-gr.pratilipi.com", Language.MARATHI,		Language.MARATHI,   true ),
	GAMMA_MALAYALAM_GR	    ( "malayalam-gamma-gr.pratilipi.com", "ml-gamma-gr.pratilipi.com", Language.MALAYALAM,	Language.MALAYALAM, true ),
	GAMMA_BENGALI_GR		(   "bengali-gamma-gr.pratilipi.com", "bn-gamma-gr.pratilipi.com", Language.BENGALI,		Language.BENGALI,   true ),
	GAMMA_KANNADA_GR		(   "kannada-gamma-gr.pratilipi.com", "kn-gamma-gr.pratilipi.com", Language.KANNADA,		Language.KANNADA,   true ),
	GAMMA_TELUGU_GR		    (    "telugu-gamma-gr.pratilipi.com", "te-gamma-gr.pratilipi.com", Language.TELUGU,		Language.TELUGU,    true ),

	DEVO_ALL_LANGUAGE( "www-devo.ptlp.co",       "m-devo.ptlp.co",  Language.ENGLISH,	null,               true ),
	DEVO_HINDI       ( "hindi-devo.ptlp.co",     "hi-devo.ptlp.co", Language.HINDI,		Language.HINDI,     true ),
	DEVO_GUJARATI    ( "gujarati-devo.ptlp.co",  "gu-devo.ptlp.co", Language.GUJARATI,	Language.GUJARATI,  true ),
	DEVO_TAMIL       ( "tamil-devo.ptlp.co",     "ta-devo.ptlp.co", Language.TAMIL,		Language.TAMIL,     true ),
	DEVO_MARATHI     ( "marathi-devo.ptlp.co",   "mr-devo.ptlp.co", Language.MARATHI,	Language.MARATHI,   true ),
	DEVO_MALAYALAM   ( "malayalam-devo.ptlp.co", "ml-devo.ptlp.co", Language.MALAYALAM,	Language.MALAYALAM, true ),
	DEVO_BENGALI     ( "bengali-devo.ptlp.co",   "bn-devo.ptlp.co", Language.BENGALI,	Language.BENGALI,   true ),
	DEVO_TELUGU      ( "telugu-devo.ptlp.co",    "te-devo.ptlp.co", Language.TELUGU,	Language.TELUGU,    true ),
	DEVO_KANNADA     ( "kannada-devo.ptlp.co",   "kn-devo.ptlp.co", Language.KANNADA,	Language.KANNADA,   true ),

	DEVO_ALL_LANGUAGE_GR ( "www-devo-gr.ptlp.co",       "m-devo-gr.ptlp.co",  Language.ENGLISH,	null,               true ),
	DEVO_HINDI_GR       ( "hindi-devo-gr.ptlp.co",     "hi-devo-gr.ptlp.co", Language.HINDI,		Language.HINDI,     true ),
	DEVO_GUJARATI_GR    ( "gujarati-devo-gr.ptlp.co",  "gu-devo-gr.ptlp.co", Language.GUJARATI,	Language.GUJARATI,  true ),
	DEVO_TAMIL_GR       ( "tamil-devo-gr.ptlp.co",     "ta-devo-gr.ptlp.co", Language.TAMIL,		Language.TAMIL,     true ),
	DEVO_MARATHI_GR     ( "marathi-devo-gr.ptlp.co",   "mr-devo-gr.ptlp.co", Language.MARATHI,	Language.MARATHI,   true ),
	DEVO_MALAYALAM_GR   ( "malayalam-devo-gr.ptlp.co", "ml-devo-gr.ptlp.co", Language.MALAYALAM,	Language.MALAYALAM, true ),
	DEVO_BENGALI_GR     ( "bengali-devo-gr.ptlp.co",   "bn-devo-gr.ptlp.co", Language.BENGALI,	Language.BENGALI,   true ),
	DEVO_TELUGU_GR      ( "telugu-devo-gr.ptlp.co",    "te-devo-gr.ptlp.co", Language.TELUGU,	Language.TELUGU,    true ),
	DEVO_KANNADA_GR     ( "kannada-devo-gr.ptlp.co",   "kn-devo-gr.ptlp.co", Language.KANNADA,	Language.KANNADA,   true ),

	ALPHA	( "localhost", "localhost", Language.HINDI, Language.HINDI, true ),
	;


	private String hostName;
	private String mobileHostName;
	private Language displayLanguage;
	private Language filterLanguage;
	private boolean isTestEnvironment;

	private Website( String hostName, String mobileHostName, Language displayLangauge, Language filterLanguage, boolean isTestEnvironment ) {
		this.hostName = hostName;
		this.mobileHostName = mobileHostName;
		this.displayLanguage = displayLangauge;
		this.filterLanguage = filterLanguage;
		this.isTestEnvironment = isTestEnvironment;
	}


	public String getHostName() {
		return hostName;
	}

	public String getMobileHostName() {
		return mobileHostName;
	}

	public Language getDisplayLanguage() {
		return displayLanguage;
	}

	public Language getFilterLanguage() {
		return filterLanguage;
	}

	public boolean isTestEnvironment() { return isTestEnvironment; }

}
