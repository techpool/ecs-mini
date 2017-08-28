package com.pratilipi.i18n;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.pratilipi.common.type.Language;

public class I18n {
	
	private static final Logger logger = Logger.getLogger( I18n.class.getName() );
	
	private static Map<Language, Map<String, String>> LANG_STRINGS_MAP = new HashMap<>();
	
	
	public static String getString( String stringId, Language language ) {
		return getStrings( language ).get( stringId );
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getStrings( Language language ) {
		Map<String, String> strings = LANG_STRINGS_MAP.get( language );
		if( strings == null ) {
			strings = language == Language.ENGLISH
					? new HashMap<String, String>()
					: (Map<String, String>) ( (HashMap<String, String>) getStrings( Language.ENGLISH ) ).clone();
			try {
				File langFile = new File( I18n.class.getResource( "language." + language.getCode() ).toURI() );
				LineIterator it = FileUtils.lineIterator( langFile, "UTF-8" );
				while( it.hasNext() ) {
					String line = it.nextLine();
					if( line.indexOf( '=' ) != -1 ) {
						String key = line.substring( 0, line.indexOf( '=' ) ).trim();
						String value = line.substring( line.indexOf( '=' ) + 1 ).trim();
						if( language == Language.ENGLISH || strings.get( key ) != null )
							strings.put( key, value );
					}
				}
				LineIterator.closeQuietly( it );
				LANG_STRINGS_MAP.put( language, strings );
			} catch( NullPointerException | URISyntaxException | IOException e ) {
				logger.log( Level.SEVERE, "Exception while reading from " + language + " language file.", e );
			}
		}
		return strings;
	}

}
