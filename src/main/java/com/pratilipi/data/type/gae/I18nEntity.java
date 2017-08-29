package com.pratilipi.data.type.gae;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pratilipi.common.type.I18nGroup;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.type.I18n;

@Cache
@Entity( name = "I18N" )
public class I18nEntity implements I18n {

	@Id
	private String I18N_ID;

	@Index
	private I18nGroup GROUP;
	
	
	@Index
	private String HINDI;
	
	@Index
	private String GUJARATI;
	
	@Index
	private String TAMIL;
	
	@Index
	private String MARATHI;
	
	@Index
	private String MALAYALAM;
	
	@Index
	private String BENGALI;
	
	@Index
	private String TELUGU;
	
	@Index
	private String KANNADA;
	
	@Index
	private String ENGLISH;
	
	
	public I18nEntity() {}
	
	public I18nEntity( String i18nId ) {
		this.I18N_ID = i18nId;
	}
	
	
	@Override
	public String getId() {
		return I18N_ID;
	}

	public void setId( String id ) {
		this.I18N_ID = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.I18N_ID = key.getName();
	}

	
	@Override
	public void setGroup( I18nGroup i18nGroup ) {
		this.GROUP = i18nGroup;
	}
	
	@Override
	public String getI18nString( Language language ) {
		switch( language ) {
			case HINDI:
				return HINDI == null ? ENGLISH : HINDI;
			case GUJARATI:
				return GUJARATI == null ? ENGLISH : GUJARATI;
			case TAMIL:
				return TAMIL == null ? ENGLISH : TAMIL;
			case MARATHI:
				return MARATHI == null ? ENGLISH : MARATHI;
			case MALAYALAM:
				return MALAYALAM == null ? ENGLISH : MALAYALAM;
			case BENGALI:
				return BENGALI == null ? ENGLISH : BENGALI;
			case TELUGU:
				return TELUGU == null ? ENGLISH : TELUGU;
			case KANNADA:
				return KANNADA == null ? ENGLISH : KANNADA;
			default: return ENGLISH;
		}
	}
	
	@Override
	public void setI18nString( Language language, String i18nString ) {
		switch( language ) {
			case HINDI:
				HINDI = i18nString; return;
			case GUJARATI:
				GUJARATI = i18nString; return;
			case TAMIL:
				TAMIL = i18nString; return;
			case MARATHI:
				MARATHI = i18nString; return;
			case MALAYALAM:
				MALAYALAM = i18nString; return;
			case BENGALI:
				BENGALI = i18nString; return;
			case TELUGU:
				TELUGU = i18nString; return;
			case KANNADA:
				KANNADA = i18nString; return;
			case ENGLISH:
				ENGLISH = i18nString; return;
		}
	}
	
}
