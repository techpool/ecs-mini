package com.pratilipi.api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pratilipi.common.type.RequestParameter;

@SuppressWarnings("serial")
public abstract class GenericService extends HttpServlet {
	
	static { }
	
	@Override
	protected final void service(
			HttpServletRequest request,
			HttpServletResponse response ) throws ServletException, IOException {

		String requestUri = request.getRequestURI();
		
		ServletConfig servletConfig = getServletConfig();
		String prefix = servletConfig.getInitParameter( "Prefix" );
		if( prefix != null && ! prefix.isEmpty() )
			requestUri = requestUri.substring( prefix.length() );
		if( requestUri.isEmpty() )
			requestUri = "/";

		String apiVer = request.getParameter( RequestParameter.API_VERSION.getName() );
		
		GenericApi api = apiVer == null
				? ApiRegistry.getApi( requestUri )
				: ApiRegistry.getApi( requestUri, apiVer );
		
		if( api == null )
			super.service( request, response );
		else
			api.service( request, response );
		
	}

}
