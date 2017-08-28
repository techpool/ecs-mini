package com.pratilipi.common.util;

import org.jsoup.Jsoup;

public class HtmlUtil {
	
	public static String toPlainText( String html ) {
		return html == null ? null : Jsoup.parse( html.replaceAll( "(?i)<br[^>]*>|\\n", "LINE_BREAK" ) )
				.text()
				.replaceAll( "LINE_BREAK", "\n" )
				.trim();
	}

	public static String truncateText( String text, int length ) {
		if( text == null )
			return null;
		else if( text.length() <= length )
			return text;
		else
			return text.substring( 0, length ) + " ...";
	}

}
