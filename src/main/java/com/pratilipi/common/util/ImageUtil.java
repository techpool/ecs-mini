package com.pratilipi.common.util;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesService.OutputEncoding;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.type.BlobEntry;

public class ImageUtil {

	private static final ImagesService imagesService = ImagesServiceFactory.getImagesService();
	

	@Deprecated
	public static int getHeight( byte[] imageData ) {
		return ImagesServiceFactory.makeImage( imageData ).getHeight();
	}
	
	@Deprecated
	public static int getWidth( byte[] imageData ) {
		return ImagesServiceFactory.makeImage( imageData ).getWidth();
	}
	
	@Deprecated
	public static byte[] resize( byte[] imageData, int width ) {
		Image image = ImagesServiceFactory.makeImage( imageData );
		Transform resize = ImagesServiceFactory.makeResize(
				width < 4000 ? width : 4000,
				4000,
				false );
		return imagesService.applyTransform( resize, image, OutputEncoding.JPEG ).getImageData();
	}

	@Deprecated
	public static byte[] resize( byte[] imageData, int width, int height ) {
		Image image = ImagesServiceFactory.makeImage( imageData );
		Transform resize = ImagesServiceFactory.makeResize(
				width < 4000 ? width : 4000,
				height < 4000 ? height : 4000,
				true );
		return imagesService.applyTransform( resize, image, OutputEncoding.JPEG ).getImageData();
	}
	
	
	public static int getHeight( BlobEntry blobEntry ) throws UnexpectedServerException {
		
		if( blobEntry.getMimeType().equalsIgnoreCase( "image/svg+xml" ) )
			return ImageSvgUtil.getHeight( blobEntry.getData() );
		
		return ImagesServiceFactory.makeImage( blobEntry.getData() ).getHeight();
		
	}
	
	public static int getWidth( BlobEntry blobEntry ) throws UnexpectedServerException {
		
		if( blobEntry.getMimeType().equalsIgnoreCase( "image/svg+xml" ) )
			return ImageSvgUtil.getWidth( blobEntry.getData() );
		
		return ImagesServiceFactory.makeImage( blobEntry.getData() ).getWidth();
		
	}
	
	public static BlobEntry resize( BlobEntry blobEntry, int width ) throws UnexpectedServerException {
		return resize( blobEntry, width, 0 );
	}
	
	public static BlobEntry resize( BlobEntry blobEntry, int width, int height ) throws UnexpectedServerException {
		
		if( blobEntry.getMimeType() != null && blobEntry.getMimeType().equalsIgnoreCase( "image/svg+xml" ) ) {
			blobEntry.setData( ImageSvgUtil.resizeSvg( blobEntry.getData(), width ) );
			return blobEntry;
		}
		
		Image image = ImagesServiceFactory.makeImage( blobEntry.getData() );
		Transform resize = ImagesServiceFactory.makeResize(
				width < 4000 ? width : 4000,
				height < 4000 && height != 0 ? height : 4000,
				height == 0 ? false : true );
		
		if( blobEntry.getMimeType() != null && blobEntry.getMimeType().equalsIgnoreCase( "image/png" ) ) {
			image = imagesService.applyTransform( resize, image, OutputEncoding.PNG );
		} else {
			image = imagesService.applyTransform( resize, image, OutputEncoding.JPEG );
			blobEntry.setMimeType( "image/jpeg" );
		}
		
		blobEntry.setData( image.getImageData() );
		return blobEntry;
		
	}
	
}