package com.pratilipi.common.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pratilipi.filter.AccessTokenFilter;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.JsonObject;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.BlobEntry;

public class HttpUtil {

	private static final Logger logger = Logger.getLogger( HttpUtil.class.getName() );
	
	
	public static class Request {

		private String targetUrl;

		private Map<String, String> headersMap;

		private Map<String, String> paramsMap;


		public Request( String targetUrl ) {
			this.targetUrl = targetUrl;
		}

		public Request( String targetUrl, Map<String, String> headersMap, Map<String, String> paramsMap ) {
			this.targetUrl = targetUrl;
			this.headersMap = headersMap;
			this.paramsMap = paramsMap;
		}

	}

	public static class Response {

		private String targetUrl;
		private Future<HTTPResponse> futureResponse;

		
		public Response( String targetUrl, Future<HTTPResponse> futureResponse ) {
			this.targetUrl = targetUrl;
			this.futureResponse = futureResponse;
		}

		
		public boolean isDone() {
			return futureResponse.isDone();
		}

		public BlobEntry get() throws UnexpectedServerException {
			return get( 60, TimeUnit.SECONDS );
		}

		public BlobEntry get( long timeout, TimeUnit unit ) throws UnexpectedServerException {

			try {
				
				return _toBlobEntry( targetUrl, futureResponse.get( timeout, unit ) );
				
			} catch( InterruptedException | ExecutionException | TimeoutException e ) {
				logger.log( Level.SEVERE, "Failed to execute Http Get call.", e );
				throw new UnexpectedServerException();
			}
			
		}

		public boolean cancel( boolean mayInterruptIfRunning ) {
			return futureResponse.cancel( mayInterruptIfRunning );
		}

		public boolean isCancelled() {
			return futureResponse.isCancelled();
		}

	}
	
	
	private static String createQueryString( Map<String, String> params )
			throws UnsupportedEncodingException {
	
		String queryStr = "";
		for( Map.Entry<String, String> entry : params.entrySet() )
			queryStr = queryStr + "&" + entry.getKey() + "=" + URLEncoder.encode( entry.getValue(), "UTF-8" );
		    
		return queryStr.substring( 1 );
	}


	public static BlobEntry doGet( String targetURL ) throws UnexpectedServerException {
		return doGet( targetURL, null, null );
	}
	
	public static BlobEntry doGet( String targetURL, Map<String, String> headersMap, Map<String, String> paramsMap ) 
			throws UnexpectedServerException {
		
		try {
			if( paramsMap != null && paramsMap.size() != 0 )
				targetURL = targetURL + "?" + createQueryString( paramsMap );
			logger.log( Level.INFO, "Http GET Request: " + targetURL );
			URLConnection urlConn = new URL( targetURL ).openConnection();
			if( headersMap != null )
				for( Entry<String, String> entry : headersMap.entrySet() )
					urlConn.setRequestProperty( entry.getKey(), entry.getValue() );
			String mimeType = urlConn.getContentType();
			int status = ( (HttpURLConnection) urlConn ).getResponseCode();
			byte[] data = IOUtils.toByteArray( urlConn );
			logger.log( Level.INFO, "Status: " + status );
			logger.log( Level.INFO, "Type: " + mimeType );
			logger.log( Level.INFO, "Length: "  + data.length );
			if( status != 200 ) {
				logger.log( Level.SEVERE, "Response: " + new String( data, "UTF-8" ) );
				throw new UnexpectedServerException();
			}
			return DataAccessorFactory.getBlobAccessor().newBlob( null, data, mimeType );
		} catch( IOException e ) {
			logger.log( Level.SEVERE, "Failed to execute Http Get call.", e );
			throw new UnexpectedServerException();
		}

	}

	// https://stackoverflow.com/questions/6067673/urldecoder-illegal-hex-characters-in-escape-pattern-for-input-string
	public static String _replacer( String data ) throws UnexpectedServerException {
		try {
			StringBuffer tempBuffer = new StringBuffer();
			int incrementor = 0;
			int dataLength = data.length();
			while( incrementor < dataLength ) {
				char characterAt = data.charAt( incrementor );
				if( characterAt == '%' ) {
					tempBuffer.append( "<percentage>" );
				} else if( characterAt == '+' ) {
					tempBuffer.append( "<plus>" );
				} else {
					tempBuffer.append( characterAt );
				}
				incrementor++;
			}
			data = tempBuffer.toString();
			data = URLDecoder.decode( data, "utf-8" );
			data = data.replaceAll( "<percentage>", "%" );
			data = data.replaceAll( "<plus>", "+" );
		} catch( IOException e ) {
			throw new UnexpectedServerException();
		}
		return data;
	}

	@Deprecated
	public static String doGet( String targetURL, Map<String, String> paramsMap ) 
			throws UnexpectedServerException {
		
		try {
			// Hack - AccessToken
			if( paramsMap == null )
				paramsMap = new HashMap<>();
			if( AccessTokenFilter.getAccessTokenId() != null )
				paramsMap.put( "accessToken", AccessTokenFilter.getAccessTokenId() );

			String requestUrl = paramsMap == null ? targetURL : targetURL + "?" + createQueryString( paramsMap );
			logger.log( Level.INFO, "Http GET Request: " + requestUrl );
			String response = _replacer( IOUtils.toString( new URL( requestUrl ).openStream(), "UTF-8" ) );
			logger.log( Level.INFO, "Http GET Response Length: "  + response.length() );
			return response;
		} catch( IOException e ) {
			logger.log( Level.SEVERE, "Failed to execute Http Get call.", e );
			throw new UnexpectedServerException();
		}

	}

	
	public static Map<String, BlobEntry> doGet( Collection<String> targetUrlList, Map<String, String> headersMap ) throws UnexpectedServerException {

		Map<String, Response> responses = new HashMap<>( targetUrlList.size() );
		for( String targetUrl : targetUrlList )
			responses.put( targetUrl, asyncGet( targetUrl, headersMap, null ) );
		
		Map<String, BlobEntry> blobEntries = new HashMap<>();
		for( String targetUrl : targetUrlList )
			blobEntries.put( targetUrl, responses.get( targetUrl ).get() );
	
		return blobEntries;
		
	}

	
	public static Response asyncGet( Request request ) throws UnexpectedServerException {
		return asyncGet( request.targetUrl, request.headersMap, request.paramsMap );
	}
	
	public static Response asyncGet( String targetURL, Map<String, String> headersMap, Map<String, String> paramsMap ) throws UnexpectedServerException {

		try {
			
			if( paramsMap != null && paramsMap.size() != 0 )
				targetURL = targetURL + "?" + createQueryString( paramsMap );
			
			HTTPRequest httpRequest = new HTTPRequest( new URL( targetURL ) );

			if( headersMap != null )
				for( Entry<String, String> entry : headersMap.entrySet() )
					httpRequest.setHeader( new HTTPHeader( entry.getKey(), entry.getValue() ) );

			return new Response(
					targetURL,
					URLFetchServiceFactory.getURLFetchService().fetchAsync( httpRequest ) );
			
		} catch( UnsupportedEncodingException | MalformedURLException e ) {
			throw new UnexpectedServerException();
		}

	}
	
	public static Map<Request, Response> asyncGet( List<Request> requestList ) throws UnexpectedServerException {
		Map<Request, Response> responses = new HashMap<>( requestList.size() );
		for( Request request : requestList )
			responses.put( request, asyncGet( request ) );
		return responses;
	}
	
	
	public static String doPost( String targetURL, Map<String, String> paramsMap )
			throws UnexpectedServerException {
		
		HttpURLConnection connection = null;
		
		try {
			
			// Forming content from request parameters
			byte[] content = createQueryString( paramsMap ).getBytes( Charset.forName( "UTF-8" ) );
			
			// Create connection
			connection = (HttpURLConnection) new URL( targetURL ).openConnection();
			connection.setRequestMethod( "POST" );
			connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
			connection.setRequestProperty( "Content-Length", content.length + "" );
			connection.setConnectTimeout( 60000 );	//60 Seconds
			connection.setReadTimeout( 60000 );		//60 Seconds
			connection.setUseCaches( false );
			connection.setDoInput( true );
			connection.setDoOutput( true );

			// Send request
			OutputStream outputStream = new DataOutputStream( connection.getOutputStream() );
			outputStream.write( content );
			outputStream.flush();
			outputStream.close();
			
			logger.log( Level.INFO, "Http POST Request: " + targetURL );

			// Response
			return _processPostResponse( connection );
		
		} catch( IOException e ) {
			logger.log( Level.SEVERE, "Failed to execute Http Post call.", e );
			throw new UnexpectedServerException();
			
		} finally {
			if( connection != null ) 
				connection.disconnect(); 
		}
		
	}
	
	public static String doPost( String targetURL, Map<String, String> headersMap, JsonObject jsonBody )
			throws UnexpectedServerException {
		
		HttpURLConnection connection = null;
		
		try {
			
			// Forming request parameters
			byte[] body = jsonBody.toString().getBytes( Charset.forName( "UTF-8" ) );
			
			// Create connection
			URL url = new URL( targetURL );
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod( "POST" );
			connection.setRequestProperty( "Content-Type", "application/json" );
			connection.setRequestProperty( "Content-Length", body.length + "" );
			for( Entry<String, String> entry : headersMap.entrySet() )
				connection.setRequestProperty( entry.getKey(), entry.getValue() );
			connection.setConnectTimeout( 60000 );	//60 Seconds
			connection.setReadTimeout( 60000 );		//60 Seconds
			connection.setUseCaches( false );
			connection.setDoInput( true );
			connection.setDoOutput( true );

			// Send request
			OutputStream outputStream = new DataOutputStream( connection.getOutputStream() );
			outputStream.write( body );
			outputStream.flush();
			outputStream.close();
			
			// Response
			logger.log( Level.INFO, "Http POST Request: " + targetURL );

			return _processPostResponse( connection );
		
		} catch( IOException e ) {
			logger.log( Level.SEVERE, "Failed to execute Http Post call.", e );
			throw new UnexpectedServerException();
			
		} finally {
			if( connection != null ) 
				connection.disconnect(); 
		}
		
	}

	public static String doPut( String targetURL, Map<String, String> headersMap, JsonObject jsonBody )
			throws UnexpectedServerException {

		HttpURLConnection connection = null;

		try {

			// Forming request parameters
			byte[] body = jsonBody.toString().getBytes( Charset.forName( "UTF-8" ) );

			// Create connection
			URL url = new URL( targetURL );
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod( "PUT" );
			connection.setRequestProperty( "Content-Type", "application/json" );
			connection.setRequestProperty( "Content-Length", body.length + "" );
			for( Entry<String, String> entry : headersMap.entrySet() )
				connection.setRequestProperty( entry.getKey(), entry.getValue() );
			connection.setConnectTimeout( 60000 );	//60 Seconds
			connection.setReadTimeout( 60000 );		//60 Seconds
			connection.setUseCaches( false );
			connection.setDoInput( true );
			connection.setDoOutput( true );

			// Send request
			OutputStream outputStream = new DataOutputStream( connection.getOutputStream() );
			outputStream.write( body );
			outputStream.flush();
			outputStream.close();

			// Response
			logger.log( Level.INFO, "Http POST Request: " + targetURL );

			return _processPostResponse( connection );

		} catch( IOException e ) {
			logger.log( Level.SEVERE, "Failed to execute Http Post call.", e );
			throw new UnexpectedServerException();

		} finally {
			if( connection != null )
				connection.disconnect();
		}

	}
	
	private static String _processPostResponse( HttpURLConnection connection ) throws IOException {
		String response = URLDecoder.decode( IOUtils.toString( connection.getInputStream(), "UTF-8" ), "UTF-8" );
		logger.log( Level.INFO, "Http POST Response Type: " + response + " & Length: "  + response.length() );
		return response;
	}

	
	private static BlobEntry _toBlobEntry( String targetUrl, HTTPResponse response ) throws UnexpectedServerException {
		
		String mimeType = null;
		int status = response.getResponseCode();
		byte[] data = response.getContent();

		for( HTTPHeader header : response.getHeadersUncombined() ) {
			if( header.getName().equals( "Content-Type" ) ) {
				mimeType = header.getValue();
				break;
			}
		}
		
		logger.log( Level.INFO, "Http GET Request: " + targetUrl );
		logger.log( Level.INFO, "Status: " + status );
		logger.log( Level.INFO, "Type: " + mimeType );
		logger.log( Level.INFO, "Length: "  + data.length );
		
		if( status != 200 ) {
			try {
				logger.log( Level.SEVERE, "Response: " + new String( data, "UTF-8" ) );
			} catch( IOException e ) {
				logger.log( Level.SEVERE, "Failed to parse error response.", e );
			}
			throw new UnexpectedServerException();
		}
		
		return DataAccessorFactory.getBlobAccessor().newBlob( null, data, mimeType );
		
	}
	
}
