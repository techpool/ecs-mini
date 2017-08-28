package com.pratilipi.api.impl.page;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.site.PratilipiSite;

@SuppressWarnings( "serial" )
@Bind( uri = "/page/content" )
public class PageContentApi extends GenericApi {

	private static final Logger logger =
			Logger.getLogger( PageContentApi.class.getName() );

	
	public static class GetRequest extends GenericRequest {

		@Validate( required = true )
		private String pageName;

		@Validate( required = true )
		private Language language;

	}

	public static class Response extends GenericResponse {

		private String title;

		private String content;


		private Response() {}


		private Response( String title, String content ) {
			this.title = title;
			this.content = content;
		}


		public String getTitle() {
			return title;
		}

		public String getContent() {
			return content;
		}

	}


	@Get
	public Response get( GetRequest request ) throws UnexpectedServerException {

		String title = null;
		StringBuilder content = new StringBuilder();

		try {

			File file = _getFile( _getFileName( request.pageName, request.language ) );

			if( file == null && request.language != Language.ENGLISH ) // Fall-back to English
				file = _getFile( _getFileName( request.pageName, Language.ENGLISH ) );

			if( file == null ) // File doesn't exist in specified language and English
				return new Response();

			
			LineIterator it = FileUtils.lineIterator( file, "UTF-8" );
			if( it.hasNext() )
				title = it.nextLine().trim();
			while( it.hasNext() )
				content.append( it.nextLine() + "<br/>" );

			LineIterator.closeQuietly( it );

		} catch( IOException e ) {
			logger.log( Level.SEVERE, "Exception while reading from data file.", e );
			throw new UnexpectedServerException();

		}

		return new Response( title, content.toString() );

	}

	private File _getFile( String fileName ) throws UnexpectedServerException {

		try {
			File file = new File( PratilipiSite.class.getResource( fileName ).toURI() ); 
			if( file.exists() && ! file.isDirectory() )
				return file;
		} catch( URISyntaxException e ) {
			logger.log( Level.SEVERE, "Exception while reading data file.", e );
			throw new UnexpectedServerException();
		} catch( NullPointerException e ) {
			return null;
		}
		
		return null;

	}

	private String _getFileName( String pageName, Language language ) {
		return "page/data/static." + language.getCode() + "." + pageName;
	}

}
