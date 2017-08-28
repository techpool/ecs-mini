package com.pratilipi.data.type;

import com.pratilipi.common.type.I18nGroup;
import com.pratilipi.common.type.Language;

public interface I18n extends GenericOfyType {
	
	String getId();
	
	void setGroup( I18nGroup i18nGroup );
	
	String getI18nString( Language language );
	
	void setI18nString( Language language, String i18nString );
	
}
