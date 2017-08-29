package com.pratilipi.common.util;

public enum ThirdPartyResource {

	JQUERY							( "<script src='//0.ptlp.co/third-party/compress/jquery_3_1_1.js'></script>" ),

	JQUERY_BOOTSTRAP				( "<script src='//0.ptlp.co/third-party/compress/jquery_3_1_1.bootstrap_3_3_7.js'></script>" ),

	JQUERY_BOOTSTRAP_POLYMER_JS		( "<script src='//0.ptlp.co/third-party/compress/jquery_3_1_1.bootstrap_3_3_7.webcomponents_lite.js'></script>" ),

	KNOCKOUT						( "<script src='//0.ptlp.co/third-party/compress/knockout_3_4_1.js'></script>" ),

	JQUERY_KNOCKOUT_BOOTSTRAP		( "<script src='//0.ptlp.co/third-party/compress/jquery_3_1_1.knockout_3_4_1.bootstrap_3_3_7.js'></script>" ),

	BOOTSTRAP_JS					( "<script defer src='//0.ptlp.co/third-party/bootstrap-3.3.4/js/bootstrap.min.js'></script>" ),

	BOOTSTRAP_CSS					( "<link rel='stylesheet' href='//0.ptlp.co/third-party/bootstrap-3.3.4/css/bootstrap.min.css'>" ),

	CKEDITOR						( "<script src='//0.ptlp.co/third-party/ckeditor-4.5.10-full/ckeditor.js'></script>" ),

	TINYMCE							( "<script src='//0.ptlp.co/third-party/tinymce-4.5.1/tinymce.min.js'></script>" ),

	POLYMER_ELEMENTS				( "<link rel='import' href='//0.ptlp.co/third-party/compress/pratilipi.polymer.elements.3.html'>" ),

	FIREBASE						( "<script src='//0.ptlp.co/third-party/compress/firebase_3_6_1.js'></script>" ),

	GOOGLE_TRANSLITERATION			( "<script src='//0.ptlp.co/third-party/compress/jsapi.js'></script>" )

	;
	
	
	private String tag;

	private ThirdPartyResource( String tag ) {

		if( SystemProperty.CDN != null ) {

			String domain = SystemProperty.CDN.replace( '*', '0' );

			if( tag.indexOf( "src='" ) != -1 )
				tag = tag.replace( "src='", "src='" + domain );
			if( tag.indexOf( "href='" ) != -1 )
				tag = tag.replace( "href='", "href='" + domain );

		}

		this.tag = tag;

	}
	
	public String getTag() {
		return this.tag;
	}
	
}
