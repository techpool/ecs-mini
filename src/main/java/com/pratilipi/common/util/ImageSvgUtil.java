package com.pratilipi.common.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pratilipi.common.exception.UnexpectedServerException;


public class ImageSvgUtil {

	private static final String heightPattern = "<svg.+?(height=\"[0-9]*\").+?>";
	private static final String widthPattern = "<svg.+?(width=\"[0-9]*\").+?>";

	
	public static int getHeight( byte[] svgData ) throws UnexpectedServerException {
	
		try {
			
			String svg = new String( svgData, "UTF-8" );
			
			Pattern pattern = Pattern.compile( heightPattern, Pattern.DOTALL );
			Matcher mHeight = pattern.matcher( svg );
			mHeight.find();
			
			String heightStr = mHeight.group( 1 );
			return Integer.parseInt( heightStr.substring( heightStr.indexOf( "\"" ) + 1, heightStr.lastIndexOf( "\"" ) ) );
			
		} catch( UnsupportedEncodingException e ) {
			throw new UnexpectedServerException();
		}
		
	}
	
	public static int getWidth( byte[] svgData ) throws UnexpectedServerException {

		try {
			
			String svgStr = new String( svgData, "UTF-8" );
			
			Pattern pattern = Pattern.compile( widthPattern, Pattern.DOTALL );
			Matcher mWidth = pattern.matcher( svgStr );
			mWidth.find();
			
			String widthStr = mWidth.group( 1 );
			return Integer.parseInt( widthStr.substring( widthStr.indexOf( "\"" ) + 1, widthStr.lastIndexOf( "\"" ) ) );
			
		} catch( UnsupportedEncodingException e ) {
			throw new UnexpectedServerException();
		}
		
	}

	public static byte[] resizeSvg( byte[] svgData, int width ) throws UnexpectedServerException {
		return resizeSvg( svgData, width, 0 );
	}

	public static byte[] resizeSvg( byte[] svgData, int width, int height ) throws UnexpectedServerException {

		try {
		
			String svgStr = new String( svgData, "UTF-8" );
			
			Pattern pWidth = Pattern.compile( widthPattern, Pattern.DOTALL );
			Matcher mWidth = pWidth.matcher( svgStr );
			mWidth.find();
			String widthStr = mWidth.group( 1 ).trim();
			
			Pattern pHeight = Pattern.compile( heightPattern, Pattern.DOTALL );
			Matcher mHeight = pHeight.matcher( svgStr );
			mHeight.find();
			String heightStr = mHeight.group( 1 ).trim();
			
			if( height == 0 )
				height = width
						* Integer.parseInt( heightStr.substring( heightStr.indexOf( "\"" ) + 1, heightStr.lastIndexOf( "\"" ) ) )
						/ Integer.parseInt( widthStr.substring( widthStr.indexOf( "\"" ) + 1, widthStr.lastIndexOf( "\"" ) ) );
			
			return svgStr.replace( widthStr, "width=\"" + width + "\"" )
					.replace( heightStr, "height=\"" + height + "\"" )
					.getBytes( "UTF-8" );
			
		} catch( UnsupportedEncodingException e ) {
			throw new UnexpectedServerException();
		}

	}

}