package com.pratilipi.common.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.common.exception.UnexpectedServerException;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerUtil {

	private static final Logger logger =
			Logger.getLogger( FreeMarkerUtil.class.getName() );

	private static Configuration cfg;

	
	private static Configuration getConfiguration()
			throws UnexpectedServerException {
		
		if( cfg == null ) {
			FileTemplateLoader ftl;
			try {
				ftl = new FileTemplateLoader( new File( "." ) );
			} catch( IOException e ) {
				logger.log( Level.SEVERE, "Failed to set template directory.", e );
				throw new UnexpectedServerException();
			}
			ClassTemplateLoader ctl = new ClassTemplateLoader( FreeMarkerUtil.class.getClassLoader(), "" );
			MultiTemplateLoader mtl = new MultiTemplateLoader( new TemplateLoader[] { ftl, ctl } );				

			cfg = new Configuration( Configuration.VERSION_2_3_22 );
			cfg.setTemplateLoader( mtl );
			cfg.setDefaultEncoding( "UTF-8" );
			cfg.setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );
		}
		return cfg;
	}

	@SuppressWarnings("deprecation")
	public static String processString( Object model, String template ) 
			throws UnexpectedServerException {

		Configuration cfg = new Configuration();
		cfg.setObjectWrapper( new DefaultObjectWrapper() );

		try {
			Template t = new Template( "templateName", new StringReader( template ), cfg );
			Writer out = new StringWriter();
			t.process( model, out );
			return out.toString();
		} catch( TemplateException | IOException e ) {
			logger.log( Level.SEVERE, "Template processing failed.", e );
			throw new UnexpectedServerException();
		}

	}

	public static String processTemplate( Object dataModel, String templateName )
			throws UnexpectedServerException {
		
		Writer writer = new StringWriter();
		processTemplate( dataModel, templateName, writer );
		return writer.toString();
	}

	public static void processTemplate( Object dataModel, String templateName, Writer writer )
			throws UnexpectedServerException {
		
		try {
			getConfiguration().getTemplate( templateName ).process( dataModel, writer );
		} catch ( TemplateException | IOException e ) {
			logger.log( Level.SEVERE, "Template processing failed.", e );
			throw new UnexpectedServerException();
		}
	}

}
