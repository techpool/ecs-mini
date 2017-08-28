package com.pratilipi.common.type;

public enum Language {

	// en.wikipedia.org/wiki/List_of_ISO_639-2_codes
	HINDI	 ( "hi", "हिंदी",		"Hindi",	     "hindi.pratilipi.com" ),
	GUJARATI ( "gu", "ગુજરાતી",	"Gujarati",	  "gujarati.pratilipi.com" ),
	TAMIL	 ( "ta", "தமிழ்",	"Tamil",	     "tamil.pratilipi.com" ),
	MARATHI	 ( "mr", "मराठी",		"Marathi",	   "marathi.pratilipi.com" ),
	MALAYALAM( "ml", "മലയാളം",	"Malayalam", "malayalam.pratilipi.com" ),
	BENGALI	 ( "bn", "বাংলা",		"Bengali",     "bengali.pratilipi.com" ),
	TELUGU	 ( "te", "తెలుగు",	"Telugu",       "telugu.pratilipi.com" ),
	KANNADA	 ( "kn", "ಕನ್ನಡ",	"Kannada",     "kannada.pratilipi.com" ),
	ENGLISH  ( "en", "English",	"English",	       "www.pratilipi.com" ),
	;
	
	
	private final String code;
	private final String name;
	private final String nameEn;
	@Deprecated
	private final String hostName;
	
	
	private Language( String code, String name, String nameEn, String hostName ) {
		this.code = code;
		this.name = name;
		this.nameEn = nameEn;
		this.hostName = hostName;
	}
	
	
	public String getStringId() {
		return "_language_" + code;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}

	public String getNameEn() {
		return nameEn;
	}
	
	@Deprecated
	public String getHostName() {
		return hostName;
	}

}