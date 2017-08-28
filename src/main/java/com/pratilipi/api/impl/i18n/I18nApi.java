package com.pratilipi.api.impl.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.I18nGroup;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.util.UserAccessUtil;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.I18n;
import com.pratilipi.filter.AccessTokenFilter;

@SuppressWarnings( "serial" )
@Bind( uri = "/i18n" )
public class I18nApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		@Validate( required = true )
		private Language language;

		private String i18nId;

		private I18nGroup group;

		public void setLanguage( Language language ) {
			this.language = language;
		}

		public void setId( String i18nId ) {
			this.i18nId = i18nId;
		}

		public void setGroup( I18nGroup group ) {
			this.group = group;
		}

	}

	public static class PostRequest extends GenericRequest {

		@Validate( required = true )
		private I18nGroup group;

		@Validate( required = true )
		private Language language;

		private Map<String, String> keyValues;

	}

	public static class GetResponse extends GenericResponse {

		private String value;

		private Map<String, String> keyValues;

		public GetResponse( String value ) {
			this.value = value;
		}

		public GetResponse( Map<String, String> keyValues ) {
			this.keyValues = keyValues;
		}

		public String getValue() {
			return value;
		}

		public Map<String, String> getKeyValues() {
			return keyValues;
		}

	}



	@Get
	public static GetResponse get( GetRequest request ) {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();

		if( request.i18nId != null ) {
			I18n i18n = dataAccessor.getI18n( request.i18nId );
			return new GetResponse( i18n != null ? i18n.getI18nString( request.language ) : (String) null );
		}

		if( request.group != null )
			return new GetResponse( dataAccessor.getI18nStrings( request.group, request.language ) );

		return new GetResponse( (String) null );

	}

	@Post
	public static GenericResponse post( PostRequest request ) 
			throws InsufficientAccessException, UnexpectedServerException {

		if( ! UserAccessUtil.hasUserAccess( AccessTokenFilter.getAccessToken().getUserId(), request.language, AccessType.I18N_UPDATE ) )
			throw new InsufficientAccessException();

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		List<I18n> i18nList = new ArrayList<>();

		for( Entry<String, String> entry : request.keyValues.entrySet() ) {
			I18n i18n = dataAccessor.getI18n( entry.getKey() );
			if( i18n == null )
				i18n = dataAccessor.newI18n( entry.getKey() );

			// Resetting the group if its already set
			i18n.setGroup( request.group );
			i18n.setI18nString( request.language, entry.getValue() );

			i18nList.add( i18n );

		}

		i18nList = dataAccessor.createOrUpdateI18nList( i18nList );

		return new GenericResponse();

	}

}
