package com.pratilipi.api;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Sensitive;
import com.pratilipi.api.shared.GenericFileDownloadResponse;
import com.pratilipi.api.shared.GenericFileUploadRequest;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Website;
import com.pratilipi.common.util.SystemProperty;
import com.pratilipi.filter.UxModeFilter;

@SuppressWarnings("serial")
public abstract class GenericApi extends HttpServlet {

	private static final Logger logger =
			Logger.getLogger( GenericApi.class.getName() );

	Method getMethod;
	Method postMethod;
	
	Class<? extends GenericRequest> getMethodParameterType;
	Class<? extends GenericRequest> postMethodParameterType;

	List<String> getRequestSensitiveFieldList = new LinkedList<>();
	List<String> postRequestSensitiveFieldList = new LinkedList<>();
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ServletException {
		
		for( Method method : this.getClass().getMethods() ) {
			if( getMethod == null && method.getAnnotation( Get.class ) != null ) {
				getMethod = method;
				getMethodParameterType = (Class<? extends GenericRequest>) method.getParameterTypes()[0];
				for( Field field : getMethodParameterType.getDeclaredFields() )
					if( field.getAnnotation( Sensitive.class ) != null )
						getRequestSensitiveFieldList.add( field.getName() );
			} else if( postMethod == null && method.getAnnotation( Post.class ) != null ) {
				postMethod = method;
				postMethodParameterType = (Class<? extends GenericRequest>) method.getParameterTypes()[0];
				for( Field field : postMethodParameterType.getDeclaredFields() )
					if( field.getAnnotation( Sensitive.class ) != null )
						postRequestSensitiveFieldList.add( field.getName() );
			}
		}
		
	}
	

	@Override
	protected void service( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException {
		
		// Logging
		if( SystemProperty.STAGE != SystemProperty.STAGE_PROD )
			logHeaders( request );
		logRequestParams( request );

		
		// Creating request payload json
		JsonObject requestPayloadJson = createRequestPayloadJson( request );

		
		String method = request.getMethod();
		Object apiResponse = null;

		// Invoking get/put method for API response
		if( method.equals( "GET" ) && getMethod != null )
			apiResponse = executeApi( this, getMethod, requestPayloadJson, getMethodParameterType, request );
		else if( method.equals( "POST" ) && postMethod != null )
			apiResponse = executeApi( this, postMethod, requestPayloadJson, postMethodParameterType, request );
		else
			apiResponse = new InvalidArgumentException( "Invalid resource or method." );
		
		// Dispatching API response
		dispatchApiResponse( apiResponse, request, response );
		
	}
	
	
	final void logHeaders( HttpServletRequest request ) {
		@SuppressWarnings("unchecked")
		Enumeration<String> headerNames = request.getHeaderNames();
		while( headerNames.hasMoreElements() ) {
			String headerName = headerNames.nextElement();
			logger.log( Level.INFO, "Header " + headerName + " = " + request.getHeader( headerName ) ) ;
		}
	}
	
	final void logRequestParams( HttpServletRequest request ) {
		
		String method = request.getMethod();
		List<String> sensitiveFieldList = null;
		if( method.equals( "GET" ) )
			sensitiveFieldList = getRequestSensitiveFieldList;
		else if( method.equals( "POST" ) )
			sensitiveFieldList = postRequestSensitiveFieldList;

		@SuppressWarnings("unchecked")
		Enumeration<String> requestParams = request.getParameterNames();
		while( requestParams.hasMoreElements() ) {
			String param = requestParams.nextElement();
			String value = sensitiveFieldList.contains( param )
					? "********"
					: request.getParameter( param ).trim();
			logger.log( Level.INFO, "Param " + param + " = " + value ) ;
		}
		
	}
	
	
	final JsonObject createRequestPayloadJson( HttpServletRequest request ) {
		
		Gson gson = new Gson();

		
		// Creating Json string from request parameters
		@SuppressWarnings("unchecked")
		Enumeration<String> requestParams = request.getParameterNames();
		StringBuilder requestParamsStr = new StringBuilder( "{" );
		while( requestParams.hasMoreElements() ) {
			String param = requestParams.nextElement();
			String value = request.getParameter( param ).trim();
			if( value.startsWith( "{" ) || value.startsWith( "[" ) ) { // Value could be a valid JSON string.
				requestParamsStr.append( "\"" + param + "\":" + value + "," );
			} else {
				if( value.isEmpty() || value.equals( "null" ) || value.equals( "undefined" ) ) {
					requestParamsStr.append( "\"" + param + "\":" + null + "," );
				} else {
					requestParamsStr.append( "\"" + param + "\":\"" + value.replace( "\\", "\\\\" ).replace( "\"", "\\\"" ) + "\"," );
				}
			}
		}
		
		if( requestParamsStr.length() > 1 )
			requestParamsStr.setCharAt( requestParamsStr.length() - 1, '}' );
		else
			requestParamsStr.append( "}" );

		
		// Creating JsonObject from Json string
		JsonObject requestParamsJson = gson.fromJson( requestParamsStr.toString(), JsonElement.class ).getAsJsonObject();
		JsonObject requestPayloadJson = new JsonObject();
		for( Entry<String, JsonElement> entry : requestParamsJson.entrySet() ) {
			requestPayloadJson.add( entry.getKey(), entry.getValue() );
			requestPayloadJson.addProperty( "has" + Character.toUpperCase( entry.getKey().charAt( 0 ) ) + entry.getKey().substring( 1 ), true );
		}
		
		
		return requestPayloadJson;
		
	}
	
	final Object executeApi( GenericApi api, Method apiMethod, JsonObject requestPayloadJson,
			Class<? extends GenericRequest> apiMethodParameterType, HttpServletRequest request ) {
		
		try {
			GenericRequest apiRequest = new Gson().fromJson( requestPayloadJson, apiMethodParameterType );
			
			if( apiRequest instanceof GenericFileUploadRequest ) {
				GenericFileUploadRequest gfuRequest = (GenericFileUploadRequest) apiRequest;
				try {
					ServletFileUpload upload = new ServletFileUpload();
					FileItemIterator iterator = upload.getItemIterator( request );
					while( iterator.hasNext() ) {
						FileItemStream fileItemStream = iterator.next();
						if( ! fileItemStream.isFormField() ) {
							gfuRequest.setName( fileItemStream.getName() );
							gfuRequest.setData( IOUtils.toByteArray( fileItemStream.openStream() ) );
							gfuRequest.setMimeType( fileItemStream.getContentType() );
							break;
						}
					}
				} catch( IOException | FileUploadException e ) {
					throw new UnexpectedServerException();
				}
			}

			JsonObject errorMessages = apiRequest.validate();
			if( errorMessages.entrySet().size() > 0 )
				return new InvalidArgumentException( errorMessages );
			else
				return apiMethod.invoke( api, apiRequest );

		} catch( JsonSyntaxException e ) {
			logger.log( Level.SEVERE, "Invalid JSON in request body.", e );
			return new InvalidArgumentException( "Invalid JSON in request body." );
		
		} catch( UnexpectedServerException e ) {
			return e;
		
		} catch( InvocationTargetException e ) {
			Throwable te = e.getTargetException();
			if( te instanceof InvalidArgumentException
					|| te instanceof InsufficientAccessException
					|| te instanceof UnexpectedServerException ) {
				return te;
			
			} else {
				logger.log( Level.SEVERE, "Failed to execute API.", te );
				return new UnexpectedServerException();
			}
			
		} catch( IllegalAccessException | IllegalArgumentException e ) {
			logger.log( Level.SEVERE, "Failed to execute API.", e );
			return new UnexpectedServerException();
		}
		
	}
	
	final void dispatchApiResponse( Object apiResponse, HttpServletRequest request,
			HttpServletResponse response ) throws IOException {
		
		if( apiResponse instanceof GenericFileDownloadResponse ) {
			
			GenericFileDownloadResponse gfdResponse = (GenericFileDownloadResponse) apiResponse;

			String eTag = request.getHeader( "If-None-Match" );
			if( eTag == null )
				logger.log( Level.INFO, "No eTag found !" );
				
			if( eTag != null && eTag.equals( gfdResponse.getETag() ) ) {
				response.setStatus( HttpServletResponse.SC_NOT_MODIFIED );
			
			} else {
				response.setContentType( gfdResponse.getMimeType() );
				response.setHeader( "Cache-Control", "max-age=315360000" );
				response.setHeader( "ETag", gfdResponse.getETag() );

				OutputStream out = response.getOutputStream();
				out.write( gfdResponse.getData() );
				out.close();
			}
			
		} else if( apiResponse instanceof GenericResponse ) {
			
			response.setContentType( "text/html" );
			response.setCharacterEncoding( "UTF-8" );

			if( SystemProperty.STAGE.equals( SystemProperty.STAGE_GAMMA ) ) {
//				response.setContentType( "application/json" );
				response.addHeader( "Access-Control-Allow-Origin", getAccessControlAllowOrigin() );
			}

			PrintWriter writer = response.getWriter();
			writer.println( new Gson().toJson( apiResponse ) );
			writer.close();
		
		} else if( apiResponse instanceof Throwable ) {
			
			response.setContentType( "text/html" );
			response.setCharacterEncoding( "UTF-8" );

			if( SystemProperty.STAGE.equals( SystemProperty.STAGE_GAMMA ) ) {
//				response.setContentType( "application/json" );
				response.addHeader( "Access-Control-Allow-Origin", getAccessControlAllowOrigin() );
			}

			PrintWriter writer = response.getWriter();

			if( apiResponse instanceof InvalidArgumentException ) {
				logger.log( Level.INFO, ((Throwable) apiResponse ).getMessage() );
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
			} else if( apiResponse instanceof InsufficientAccessException ) {
				logger.log( Level.INFO, ((Throwable) apiResponse ).getMessage() );
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
			} else if( apiResponse instanceof UnexpectedServerException )
				response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			else
				response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			
			writer.println( ((Throwable) apiResponse ).getMessage() );
			writer.close();
			
		}
		
	}
	
	protected String getAccessControlAllowOrigin() {
		return  "*";
//		String refererHost = UxModeFilter.getRefererHost();
//		for( Website website : Website.values() )
//			if( refererHost.equals( website.getHostName() ) || refererHost.equals( website.getMobileHostName() ) )
//				return website == Website.ALPHA ? "http://localhost:8080" : ( "https://" + refererHost );
//		return null;
	}

}
