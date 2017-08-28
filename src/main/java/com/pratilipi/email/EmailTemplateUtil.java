package com.pratilipi.email;

import java.util.HashMap;
import java.util.Map;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.EmailType;
import com.pratilipi.common.type.I18nGroup;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.FreeMarkerUtil;
import com.pratilipi.data.DataAccessorFactory;


public class EmailTemplateUtil {

	private final static String filePath = 
			EmailUtil.class.getName().substring( 0, EmailTemplateUtil.class.getName().lastIndexOf(".") ).replace( ".", "/" ) + "/template/";


	public static String getEmailTemplate( EmailType emailType, Language language ) 
			throws UnexpectedServerException {

		return FreeMarkerUtil.processTemplate( 
				DataAccessorFactory.getDataAccessor().getI18nStrings( I18nGroup.EMAIL, language ), 
				filePath + emailType.getTemplateName() );

	}

	public static String getEmailBody( String content, Language language ) 
			throws UnexpectedServerException {

		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put( "language", language );
		dataModel.put( "contact_email", language == Language.ENGLISH ? 
				"contact@pratilipi.com" : language.name().toLowerCase() + "@pratilipi.com" );
		dataModel.put( "emailBody", content );

		return FreeMarkerUtil.processTemplate( dataModel, filePath + "MainEmailTemplate.ftl" );

	}

}
