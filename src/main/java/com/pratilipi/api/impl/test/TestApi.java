package com.pratilipi.api.impl.test;

import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.UnexpectedServerException;

@SuppressWarnings( "serial" )
@Bind( uri = "/test" )
public class TestApi extends GenericApi {

	public static class GetRequest extends GenericRequest {

		@Validate( required = true )
		private Long wait;

	}

	@Get
	public static GenericResponse get( GetRequest request ) throws UnexpectedServerException {
		try {
			Thread.sleep( request.wait != null ? request.wait : 10000 );
		} catch ( InterruptedException e ) {
			throw new UnexpectedServerException();
		}
		return new GenericResponse();
	}
}
