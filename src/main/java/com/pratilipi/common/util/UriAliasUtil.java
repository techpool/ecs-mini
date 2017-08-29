package com.pratilipi.common.util;

import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Page;

public class UriAliasUtil {

	public static String generateUriAlias( String oldUriAlias, String uriPrefix, String... keywords ) {
		
		String uriAlias = uriPrefix;
		for( String keyword : keywords )
			if( keyword != null )
				uriAlias += keyword
								.trim()
								.toLowerCase()
								.replaceAll( "[^a-z0-9]+", "-" )
						+ "-";
		
		uriAlias = uriAlias.replaceAll( "[-]+", "-" );
		if( uriAlias.length() > uriPrefix.length() + 1 ) {
			if( uriAlias.charAt( uriPrefix.length() ) == '-' )
				uriAlias = uriPrefix + uriAlias.substring( uriPrefix.length() + 1, uriAlias.length() - 1 );
			else
				uriAlias = uriAlias.substring( 0, uriAlias.length() - 1 );
		} else {
			return null;
		}
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		for( int i = 0; ; i++ ) {
			String aUriAlias = i == 0 ? uriAlias : uriAlias + "-" + i;
			if( oldUriAlias != null && oldUriAlias.equals( aUriAlias ) )
				return aUriAlias;

			Page page = dataAccessor.getPage( aUriAlias );
			if( page == null )
				return aUriAlias;
		}

	}
	
}
