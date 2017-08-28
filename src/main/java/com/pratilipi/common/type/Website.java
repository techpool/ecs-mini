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

	PROD_ALL_LANGUAGE	(       "www-prod.pratilipi.com",  "m-prod.pratilipi.com", Language.ENGLISH,		null,               false ),
	PROD_HINDI			(     "hindi-prod.pratilipi.com", "hi-prod.pratilipi.com", Language.HINDI,		Language.HINDI,     false ),
	PROD_GUJARATI		(  "gujarati-prod.pratilipi.com", "gu-prod.pratilipi.com", Language.GUJARATI,		Language.GUJARATI,  false ),
	PROD_TAMIL			(     "tamil-prod.pratilipi.com", "ta-prod.pratilipi.com", Language.TAMIL,		Language.TAMIL,     false ),
	PROD_MARATHI			(   "marathi-prod.pratilipi.com", "mr-prod.pratilipi.com", Language.MARATHI,		Language.MARATHI,   false ),
	PROD_MALAYALAM		( "malayalam-prod.pratilipi.com", "ml-prod.pratilipi.com", Language.MALAYALAM,	Language.MALAYALAM, false ),
	PROD_BENGALI			(   "bengali-prod.pratilipi.com", "bn-prod.pratilipi.com", Language.BENGALI,		Language.BENGALI,   false ),
	PROD_KANNADA			(   "kannada-prod.pratilipi.com", "kn-prod.pratilipi.com", Language.KANNADA,		Language.KANNADA,   false ),
	PROD_TELUGU			(    "telugu-prod.pratilipi.com", "te-prod.pratilipi.com", Language.TELUGU,		Language.TELUGU,    false ),

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

	DEVO_PR_HINDI       ( "pr-hindi.ptlp.co",     "pr-hi.ptlp.co", Language.HINDI,		Language.HINDI,     true ),
	DEVO_PR_GUJARATI    ( "pr-gujarati.ptlp.co",  "pr-gu.ptlp.co", Language.GUJARATI,	Language.GUJARATI,  true ),
	DEVO_PR_TAMIL       ( "pr-tamil.ptlp.co",     "pr-ta.ptlp.co", Language.TAMIL,		Language.TAMIL,     true ),
	DEVO_PR_MARATHI     ( "pr-marathi.ptlp.co",   "pr-mr.ptlp.co", Language.MARATHI,	Language.MARATHI,   true ),
	DEVO_PR_MALAYALAM   ( "pr-malayalam.ptlp.co", "pr-ml.ptlp.co", Language.MALAYALAM,	Language.MALAYALAM, true ),
	DEVO_PR_BENGALI     ( "pr-bengali.ptlp.co",   "pr-bn.ptlp.co", Language.BENGALI,	Language.BENGALI,   true ),
	DEVO_PR_TELUGU      ( "pr-telugu.ptlp.co",    "pr-te.ptlp.co", Language.TELUGU,	Language.TELUGU,    true ),
	DEVO_PR_KANNADA     ( "pr-kannada.ptlp.co",   "pr-kn.ptlp.co", Language.KANNADA,	Language.KANNADA,   true ),

	DEVO_GR_HINDI       ( "gr-hindi.ptlp.co",     "gr-hi.ptlp.co", Language.HINDI,		Language.HINDI,     true ),
	DEVO_GR_GUJARATI    ( "gr-gujarati.ptlp.co",  "gr-gu.ptlp.co", Language.GUJARATI,	Language.GUJARATI,  true ),
	DEVO_GR_TAMIL       ( "gr-tamil.ptlp.co",     "gr-ta.ptlp.co", Language.TAMIL,		Language.TAMIL,     true ),
	DEVO_GR_MARATHI     ( "gr-marathi.ptlp.co",   "gr-mr.ptlp.co", Language.MARATHI,	Language.MARATHI,   true ),
	DEVO_GR_MALAYALAM   ( "gr-malayalam.ptlp.co", "gr-ml.ptlp.co", Language.MALAYALAM,	Language.MALAYALAM, true ),
	DEVO_GR_BENGALI     ( "gr-bengali.ptlp.co",   "gr-bn.ptlp.co", Language.BENGALI,	Language.BENGALI,   true ),
	DEVO_GR_TELUGU      ( "gr-telugu.ptlp.co",    "gr-te.ptlp.co", Language.TELUGU,	Language.TELUGU,    true ),
	DEVO_GR_KANNADA     ( "gr-kannada.ptlp.co",   "gr-kn.ptlp.co", Language.KANNADA,	Language.KANNADA,   true ),

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
