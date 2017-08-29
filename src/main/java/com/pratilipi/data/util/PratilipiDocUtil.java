package com.pratilipi.data.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.AccessType;
import com.pratilipi.common.type.CommentParentType;
import com.pratilipi.common.type.CommentState;
import com.pratilipi.common.type.PageType;
import com.pratilipi.common.type.PratilipiContentType;
import com.pratilipi.common.type.ReferenceType;
import com.pratilipi.common.type.UserReviewState;
import com.pratilipi.common.type.VoteParentType;
import com.pratilipi.common.type.VoteType;
import com.pratilipi.common.util.GoogleAnalyticsApi;
import com.pratilipi.common.util.HtmlUtil;
import com.pratilipi.common.util.HttpUtil;
import com.pratilipi.common.util.ImageUtil;
import com.pratilipi.data.BlobAccessor;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.RtdbAccessor;
import com.pratilipi.data.type.AuditLog;
import com.pratilipi.data.type.BlobEntry;
import com.pratilipi.data.type.Comment;
import com.pratilipi.data.type.CommentDoc;
import com.pratilipi.data.type.Page;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.type.PratilipiContentDoc;
import com.pratilipi.data.type.PratilipiContentDoc.AlignmentType;
import com.pratilipi.data.type.PratilipiContentDoc.Chapter;
import com.pratilipi.data.type.PratilipiContentDoc.PageletType;
import com.pratilipi.data.type.PratilipiGoogleAnalyticsDoc;
import com.pratilipi.data.type.PratilipiMetaDoc;
import com.pratilipi.data.type.PratilipiReviewsDoc;
import com.pratilipi.data.type.UserPratilipi;
import com.pratilipi.data.type.UserPratilipiDoc;
import com.pratilipi.data.type.Vote;
import com.pratilipi.filter.AccessTokenFilter;
import com.pratilipi.filter.UxModeFilter;


public class PratilipiDocUtil {
	
	private static final Logger logger =
			Logger.getLogger( PratilipiDocUtil.class.getName() );
	
	private static final String nonKeywordsPattern = "&nbsp;|&lt;|&gt;|&amp;|&cent;|&pound;|&yen;|&euro;|&copy;|&reg;|<[^>]*>|[!-/:-@\\[-`{-~]|।";

	
	// Content doc

	public static JsonArray getContentIndex( Long pratilipiId ) 
			throws InsufficientAccessException, UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! PratilipiDataUtil.hasAccessToReadPratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		PratilipiContentDoc pcDoc = _validateChapterIdInContent( pratilipiId );
		return pcDoc == null ? null : pcDoc.getIndex();

	}

	
	@Deprecated
	public static Object getContent( Long pratilipiId, Integer chapterNo, Integer pageNo ) 
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! PratilipiDataUtil.hasAccessToReadPratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		PratilipiContentDoc pcDoc = docAccessor.getPratilipiContentDoc( pratilipiId );

		if( pcDoc == null )
			return null;
		else if( chapterNo == null )
			return _processContent( pratilipi, pcDoc );
		
		Chapter chapter = pcDoc.getChapter( chapterNo );
		
		if( chapter == null )
			return null;
		else if( pageNo == null )
			return _processContent( pratilipi, chapter );
		
		PratilipiContentDoc.Page page = chapter.getPage( pageNo );
		if( page == null )
			return null;
		else
			return _processContent( pratilipi, chapter.getPage( pageNo ) );

	}
	
	@Deprecated
	private static PratilipiContentDoc _processContent( Pratilipi pratilipi, PratilipiContentDoc pcDoc ) {
		for( PratilipiContentDoc.Chapter chapterDoc : pcDoc.getChapterList() )
			_processContent( pratilipi, chapterDoc );
		return pcDoc;
	}

	@Deprecated
	private static PratilipiContentDoc.Chapter _processContent( Pratilipi pratilipi, PratilipiContentDoc.Chapter chapterDoc ) {
		for( PratilipiContentDoc.Page pageDoc : chapterDoc.getPageList() )
			_processContent( pratilipi, pageDoc );
		return chapterDoc;
	}

	@Deprecated
	private static PratilipiContentDoc.Page _processContent( Pratilipi pratilipi, PratilipiContentDoc.Page pageDoc ) {
		if( UxModeFilter.isAndroidApp() || pratilipi.getContentType() == PratilipiContentType.IMAGE ) {
			for( PratilipiContentDoc.Pagelet pageletDoc : pageDoc.getPageletList() ) {
				if( pageletDoc.getType() == PageletType.HTML ) {
					pageletDoc.setType( PageletType.TEXT );
					pageletDoc.setData( HtmlUtil.toPlainText( pageletDoc.getDataAsString() ) );
				} else if( pageletDoc.getType() == PageletType.BLOCK_QUOTE ) {
					pageletDoc.setData( HtmlUtil.toPlainText( pageletDoc.getDataAsString() ) );
				}
			}
		} else {
			String html = "";
			for( PratilipiContentDoc.Pagelet pageletDoc : pageDoc.getPageletList() ) {
				if( ( pageletDoc.getType() == PageletType.TEXT || pageletDoc.getType() == PageletType.HTML ) && pageletDoc.getAlignment() == null )
					html += "<p>" + pageletDoc.getDataAsString() + "</p>";
				else if( ( pageletDoc.getType() == PageletType.TEXT || pageletDoc.getType() == PageletType.HTML ) && pageletDoc.getAlignment() != null )
					html += "<p style=\"text-align:" + pageletDoc.getAlignment() + "\">" + pageletDoc.getDataAsString() + "</p>";
				else if( pageletDoc.getType() == PageletType.BLOCK_QUOTE )
					html += "<blockquote>" + pageletDoc.getDataAsString() + "</blockquote>";
				else if( pageletDoc.getType() == PageletType.IMAGE )
					html += _processImagePagelet( pratilipi.getId(), pageletDoc.getData() );
			}
			pageDoc.setHtml( html );
			pageDoc.deleteAllPagelets();
		}
		return pageDoc;
	}

	public static Object getContent_v3( Long pratilipiId, String chapterId, Integer chapterNo, Integer pageNo )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! PratilipiDataUtil.hasAccessToReadPratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		PratilipiContentDoc pcDoc = _validateChapterIdInContent( pratilipiId );

		if( pcDoc == null )
			return null;
		else if( chapterId == null && chapterNo == null )
			return _processContent_v3( pratilipi, pcDoc );
		
		Chapter chapter = chapterId != null ? pcDoc.getChapter( chapterId ) : pcDoc.getChapter( chapterNo );
		
		if( chapter == null )
			return null;
		else if( pageNo == null )
			return _processContent_v3( pratilipi, chapter );
		
		PratilipiContentDoc.Page page = chapter.getPage( pageNo );
		if( page == null )
			return null;
		else
			return _processContent_v3( pratilipi, chapter.getPage( pageNo ) );

	}

	private static PratilipiContentDoc _processContent_v3( Pratilipi pratilipi, PratilipiContentDoc pcDoc ) {
		for( PratilipiContentDoc.Chapter chapterDoc : pcDoc.getChapterList() )
			_processContent_v3( pratilipi, chapterDoc );
		return pcDoc;
	}

	private static PratilipiContentDoc.Chapter _processContent_v3( Pratilipi pratilipi, PratilipiContentDoc.Chapter chapterDoc ) {
		for( PratilipiContentDoc.Page pageDoc : chapterDoc.getPageList() )
			_processContent_v3( pratilipi, pageDoc );
		return chapterDoc;
	}
	
	private static PratilipiContentDoc.Page _processContent_v3( Pratilipi pratilipi, PratilipiContentDoc.Page pageDoc ) {
		if( UxModeFilter.isAndroidApp() || pratilipi.getContentType() == PratilipiContentType.IMAGE ) {
			
			for( PratilipiContentDoc.Pagelet pageletDoc : pageDoc.getPageletList() ) {
				if( pageletDoc.getType() == PageletType.TEXT || pageletDoc.getType() == PageletType.HTML ) {
					if( pageletDoc.getAlignment() == AlignmentType.LEFT )
						pageletDoc.setData( "<p><left>" + pageletDoc.getDataAsString() + "</left></p>" );
					else if( pageletDoc.getAlignment() == AlignmentType.RIGHT )
						pageletDoc.setData( "<p><right>" + pageletDoc.getDataAsString() + "</right></p>" );
					else if( pageletDoc.getAlignment() == AlignmentType.CENTER )
						pageletDoc.setData( "<p><center>" + pageletDoc.getDataAsString() + "</center></p>" );
					else
						pageletDoc.setData( "<p>" + pageletDoc.getDataAsString() + "</p>" );
				} else if( pageletDoc.getType() == PageletType.BLOCK_QUOTE ) {
					pageletDoc.setData( "<blockquote>" + pageletDoc.getDataAsString() + "</blockquote>" );
				} else if( pageletDoc.getType() == PageletType.LIST_ORDERED ) {
					pageletDoc.setData( "<ol>" + pageletDoc.getDataAsString() + "</ol>" );
				} else if( pageletDoc.getType() == PageletType.LIST_UNORDERED ) {
					pageletDoc.setData( "<ul>" + pageletDoc.getDataAsString() + "</ul>" );
				}
			}
				
		} else {
			
			String html = "";
			for( PratilipiContentDoc.Pagelet pageletDoc : pageDoc.getPageletList() ) {
				if( ( pageletDoc.getType() == PageletType.TEXT || pageletDoc.getType() == PageletType.HTML ) && pageletDoc.getAlignment() == null )
					html += "<p>" + pageletDoc.getDataAsString() + "</p>";
				else if( ( pageletDoc.getType() == PageletType.TEXT || pageletDoc.getType() == PageletType.HTML ) && pageletDoc.getAlignment() != null )
					html += "<p style=\"text-align:" + pageletDoc.getAlignment() + "\">" + pageletDoc.getDataAsString() + "</p>";
				else if( pageletDoc.getType() == PageletType.BLOCK_QUOTE )
					html += "<blockquote>" + pageletDoc.getDataAsString() + "</blockquote>";
				else if( pageletDoc.getType() == PageletType.IMAGE )
					html += _processImagePagelet( pratilipi.getId(), pageletDoc.getData() );
				else if( pageletDoc.getType() == PageletType.LIST_ORDERED )
					html += "<ol>" + pageletDoc.getDataAsString() + "</ol>";
				else if( pageletDoc.getType() == PageletType.LIST_UNORDERED )
					html += "<ul>" + pageletDoc.getDataAsString() + "</ul>";
			}
			pageDoc.setHtml( html );
			pageDoc.deleteAllPagelets();
			
		}
		return pageDoc;
	}

	public static String _processImagePagelet( Long pratilipiId, JsonObject jsonObject ) {

		double widthSet = jsonObject.has( "ratio" )
				? jsonObject.get( "ratio" ).getAsDouble() * jsonObject.get( "width" ).getAsDouble()
				: jsonObject.get( "width" ).getAsDouble();
		double heightSet = jsonObject.has( "ratio" )
				? jsonObject.get( "ratio" ).getAsDouble() * jsonObject.get( "height" ).getAsDouble()
				: jsonObject.get( "height" ).getAsDouble();

		return "<img style=\"width: " + (int) widthSet + "px; height: " + (int) heightSet + "px;\" " +
				"src=\"/api/pratilipi/content/image?pratilipiId=" + pratilipiId +
				"&name=" + jsonObject.get( "name" ).getAsString() + "&width=" + (int) widthSet +"\"/>";

	}
	
	public static BlobEntry getContentImage( long pratilipiId, String name, Integer width )
			throws InsufficientAccessException, UnexpectedServerException {

		Pratilipi pratilipi = DataAccessorFactory.getDataAccessor().getPratilipi( pratilipiId );

		if( ! PratilipiDataUtil.hasAccessToReadPratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		BlobEntry blobEntry = DataAccessorFactory.getBlobAccessor()
				.getBlob( "pratilipi/" + pratilipiId + "/images/" + name );
	
		if( width != null )
			blobEntry = ImageUtil.resize( blobEntry, width );

		return blobEntry;

	}

	public static JsonArray addContentChapter( Long pratilipiId, Integer chapterNo ) 
			throws InsufficientAccessException, UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! PratilipiDataUtil.hasAccessToUpdatePratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		PratilipiContentDoc pcDoc = docAccessor.getPratilipiContentDoc( pratilipiId );

		if( pcDoc == null )
			pcDoc = docAccessor.newPratilipiContentDoc();

		pcDoc.addChapter( chapterNo, null );
		docAccessor.save( pratilipiId, pcDoc );

		return pcDoc.getIndex();

	}

	public static PratilipiContentDoc deleteContentChapter( Long pratilipiId, String chapterId, Integer chapterNo )
			throws InsufficientAccessException, UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! PratilipiDataUtil.hasAccessToUpdatePratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		PratilipiContentDoc pcDoc = docAccessor.getPratilipiContentDoc( pratilipiId );

		if( pcDoc == null )
			return docAccessor.newPratilipiContentDoc();

		if( chapterId != null )
			pcDoc.deleteChapter( chapterId );
		else if( chapterNo != null )
			pcDoc.deleteChapter( chapterNo );

		docAccessor.save( pratilipiId, pcDoc );

		return pcDoc;

	}

	private static void _setPage( Long pratilipiId, PratilipiContentDoc.Page page, String html )
			throws InvalidArgumentException, UnexpectedServerException {

		// Deleting Existing Pagelets
		page.deleteAllPagelets();

		// Adding Pagelets from HTML
		if( html != null && ! html.trim().isEmpty() ) {

			Node body = Jsoup.parse( html ).body();

			Node badNode = _validateContent( body );
			if( badNode != null ) {
				String errMsg = "";
				while( badNode != body ) {
					errMsg = " > " + badNode.nodeName() + errMsg;
					badNode = badNode.parent();
				}
				errMsg = "Invalid node " + errMsg;
				throw new InvalidArgumentException( errMsg );
			}

			for( Node node : body.childNodes() ) {

				if( node.nodeName().equals( "p" ) ) {

					if( node.childNodeSize() == 1 && node.childNode( 0 ).equals( "img" ) ) {

						JsonObject imgData = _createImageData( pratilipiId, node.childNode( 0 ) );
						if( imgData != null )
							page.addPagelet( PageletType.IMAGE, imgData );

					} else {

						page.addPagelet( PageletType.HTML, ( (Element) node ).html(), _getAlignment( node ) );

					}

				} else if( node.nodeName().equals( "img" ) ) {

					JsonObject imgData = _createImageData( pratilipiId, node );
					if( imgData != null )
						page.addPagelet( PageletType.IMAGE, imgData );

				} else if( node.nodeName().equals( "blockquote" ) ) {

					page.addPagelet( PageletType.BLOCK_QUOTE, ( (Element) node ).html() );

				} else if( node.nodeName().equals( "ol" ) ) {

					page.addPagelet( PageletType.LIST_ORDERED, ( (Element) node ).html() );

				} else if( node.nodeName().equals( "ul" ) ) {

					page.addPagelet( PageletType.LIST_UNORDERED, ( (Element) node ).html() );

				}
			}
		}
	}

	public static PratilipiContentDoc saveContentPage( Long pratilipiId, String chapterId, Integer chapterNo, String chapterTitle, Integer pageNo, String html )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! PratilipiDataUtil.hasAccessToUpdatePratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		// Doc
		PratilipiContentDoc pcDoc = _validateChapterIdInContent( pratilipiId );
		if( pcDoc == null )
			pcDoc = docAccessor.newPratilipiContentDoc();

		// Chapter
		Chapter chapter = null;
		if( chapterNo != null ) {
			chapter = pcDoc.getChapter( chapterNo );
			if( chapter == null ) { /* Add Chapter */
				Long aChapterId = new Date().getTime();
				while( ( chapter = pcDoc.getChapter( chapterNo ) ) == null ) {
					Chapter ch = pcDoc.addChapter( null );
					ch.setId( generateChapterId( pratilipiId, aChapterId++ ) );
					ch.setLastUpdated( UxModeFilter.isAndroidApp() ? PratilipiContentDoc.FrontendType.ANDROID : PratilipiContentDoc.FrontendType.WEB );
				}
			}
		} else if( chapterId != null ) {
			chapter =  pcDoc.getChapter( chapterId );
			if( chapter == null ) {
				chapter = pcDoc.addChapter( chapterTitle );
				chapter.setId( chapterId );
			}
		}

		chapter.setLastUpdated( UxModeFilter.isAndroidApp() ? PratilipiContentDoc.FrontendType.ANDROID : PratilipiContentDoc.FrontendType.WEB );

		if( chapterTitle != null )
			chapter.setTitle( chapterTitle );

		// Page
		PratilipiContentDoc.Page page = chapter.getPage( pageNo );
		if( page == null )
			page = chapter.addPage( pageNo );

		_setPage( pratilipiId, page, html );

		// Save
		docAccessor.save( pratilipiId, pcDoc );

		return pcDoc;

	}

	public static PratilipiContentDoc saveContentPageBatch( Long pratilipiId, JsonObject chapters )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! PratilipiDataUtil.hasAccessToUpdatePratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		// Doc
		PratilipiContentDoc pcDoc = docAccessor.getPratilipiContentDoc( pratilipiId );
		if( pcDoc == null )
			pcDoc = docAccessor.newPratilipiContentDoc();

		for( Map.Entry<String, JsonElement> chapterEntry : chapters.entrySet() ) {

			String chapterId = chapterEntry.getKey();

			// chapterTitle
			String chapterTitle = null;
			if( chapterEntry.getValue().getAsJsonObject().has( "chapterTitle" ) )
				chapterTitle = chapterEntry.getValue().getAsJsonObject().get( "chapterTitle" ).getAsString();

			// content
			String content = null;
			if( chapterEntry.getValue().getAsJsonObject().has( "content" ) )
				content = chapterEntry.getValue().getAsJsonObject().get( "content" ).getAsString();

			// pageNo
			if( ! chapterEntry.getValue().getAsJsonObject().has( "pageNo" ) )
				throw new InvalidArgumentException( GenericRequest.ERR_PRATILIPI_PAGE_NO_REQUIRED );
			Integer pageNo = chapterEntry.getValue().getAsJsonObject().get( "pageNo" ).getAsInt();


			// Chapter
			Chapter chapter = pcDoc.getChapter( chapterId );
			if( chapter == null ) {
				chapter = pcDoc.addChapter( pcDoc.getChapterCount() + 1, chapterTitle );
				chapter.setId( chapterId );
			} else if( chapterTitle != null ) {
				chapter.setTitle( chapterTitle );
			}

			chapter.setLastUpdated( UxModeFilter.isAndroidApp() ? PratilipiContentDoc.FrontendType.ANDROID : PratilipiContentDoc.FrontendType.WEB );

			// Page
			PratilipiContentDoc.Page page = chapter.getPage( pageNo );
			if( page == null )
				page = chapter.addPage( pageNo );

			_setPage( pratilipiId, page, content );

		}

		// Save
		docAccessor.save( pratilipiId, pcDoc );

		return pcDoc;

	}

	public static PratilipiContentDoc saveContentIndex( Long pratilipiId, List<String> chapterIdOrder )
			throws InsufficientAccessException, InvalidArgumentException, UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );

		if( ! PratilipiDataUtil.hasAccessToUpdatePratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();

		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		// Doc
		PratilipiContentDoc pcDoc = docAccessor.getPratilipiContentDoc( pratilipiId );
		if( pcDoc == null )
			throw new InvalidArgumentException( "Invalid PratilipiId!" );

		for( String chapterId : chapterIdOrder )
			if( pcDoc.getChapter( chapterId ) == null )
				throw new InvalidArgumentException( "Invalid chapterId: " + chapterId );

		PratilipiContentDoc newPcDoc = docAccessor.newPratilipiContentDoc();
		int chapterNo = 1;
		for( String chapterId : chapterIdOrder ) {
			Chapter chapter = pcDoc.getChapter( chapterId );
			newPcDoc.setChapter( chapterNo++, chapter );
		}

		// Save
		docAccessor.save( pratilipiId, newPcDoc );

		return newPcDoc;

	}

	public static String saveContentImage( Long pratilipiId, BlobEntry blobEntry )
			throws InsufficientAccessException, UnexpectedServerException {

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
		
		if( ! PratilipiDataUtil.hasAccessToUpdatePratilipiContent( pratilipi ) )
			throw new InsufficientAccessException();
		
		
		String contentImageName = new Date().getTime() + "";
		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		blobEntry.setName( _createImageFullName( pratilipiId, contentImageName ) );
		blobAccessor.createOrUpdateBlob( blobEntry );
		
		return contentImageName;

	}
	
	private static String _createImageFullName( Long pratilipiId, String imageName ) {
		return "pratilipi/" + pratilipiId + "/images/" + imageName;
	}

	private static Node _validateContent( Node node ) {

		if( node.nodeName().equals( "body" ) ) {
		
			for( Node childNode : node.childNodes() ) {
				if( childNode.nodeName().equals( "p" ) ) {
					if( childNode.childNodeSize() == 1 && childNode.childNode( 0 ).nodeName().equals( "img" ) ) {
						// Do Nothing
					} else {
						Node badNode = _validateContent( childNode );
						if( badNode != null )
							return badNode;
					}
				} else if( childNode.nodeName().equals( "blockquote" ) ) {
					Node badNode = _validateContent( childNode );
					if( badNode != null )
						return badNode;
				} else if( childNode.nodeName().equals( "img" ) ) {
					// Do Nothing
				} else if( childNode.nodeName().equals( "ol" ) || childNode.nodeName().equals( "ul" ) ) {
					Node badNode = _validateContent( childNode );
					if( badNode != null )
						return badNode;
				} else {
					return childNode; // Bad Node
				}
			}
					
		} else if( node.nodeName().equals( "p" ) || node.nodeName().equals( "blockquote" ) ) {
			
			for( Node childNode : node.childNodes() ) {
				if( childNode.getClass() == TextNode.class ) {
					// Do Nothing
				} else if( childNode.nodeName().equals( "br" )
						|| childNode.nodeName().equals( "b" )
						|| childNode.nodeName().equals( "i" )
						|| childNode.nodeName().equals( "u" )
						|| childNode.nodeName().equals( "a" ) ) {
					Node badNode = _validateContent( childNode );
					if( badNode != null )
						return badNode;
				} else {
					return childNode; // Bad Node
				}
			}
			
		} else if( node.nodeName().equals( "ol" ) || node.nodeName().equals( "ul" ) ) {

			for( Node childNode : node.childNodes() ) {
				if( childNode.nodeName().equals( "li" ) ) {
					Node badNode = _validateContent( childNode );
					if( badNode != null )
						return badNode;
				} else {
					return childNode; // Bad Node
				}
			}
			
		} else if( node.nodeName().equals( "li" ) ) {
			
			for( Node childNode : node.childNodes() ) {
				if( childNode.getClass() == TextNode.class ) {
					// Do Nothing
				} else if( childNode.nodeName().equals( "b" )
						|| childNode.nodeName().equals( "i" )
						|| childNode.nodeName().equals( "u" )
						|| childNode.nodeName().equals( "a" )
						|| childNode.nodeName().equals( "br" ) ) {
					Node badNode = _validateContent( childNode );
					if( badNode != null )
						return badNode;
				} else {
					return childNode; // Bad Node
				}
			}
			
		} else if( node.nodeName().equals( "b" ) ) {
			
			for( Node childNode : node.childNodes() ) {
				if( childNode.getClass() == TextNode.class ) {
					// Do Nothing
				} else if( childNode.nodeName().equals( "br" )
						|| childNode.nodeName().equals( "i" )
						|| childNode.nodeName().equals( "u" )
						|| childNode.nodeName().equals( "a" ) ) {
					Node badNode = _validateContent( childNode );
					if( badNode != null )
						return badNode;
				} else {
					return childNode; // Bad Node
				}
			}
			
		} else if( node.nodeName().equals( "i" ) ) {
			
			for( Node childNode : node.childNodes() ) {
				if( childNode.getClass() == TextNode.class ) {
					// Do Nothing
				} else if( childNode.nodeName().equals( "br" )
						|| childNode.nodeName().equals( "b" )
						|| childNode.nodeName().equals( "u" )
						|| childNode.nodeName().equals( "a" ) ) {
					Node badNode = _validateContent( childNode );
					if( badNode != null )
						return badNode;
				} else {
					return childNode; // Bad Node
				}
			}
			
		} else if( node.nodeName().equals( "u" ) ) {
			
			for( Node childNode : node.childNodes() ) {
				if( childNode.getClass() == TextNode.class ) {
					// Do Nothing
				} else if( childNode.nodeName().equals( "br" )
						|| childNode.nodeName().equals( "b" )
						|| childNode.nodeName().equals( "i" )
						|| childNode.nodeName().equals( "a" ) ) {
					Node badNode = _validateContent( childNode );
					if( badNode != null )
						return badNode;
				} else {
					return childNode; // Bad Node
				}
			}
			
		} else if( node.nodeName().equals( "a" ) ) {
			
			for( Node childNode : node.childNodes() ) {
				if( childNode.getClass() == TextNode.class ) {
					// Do Nothing
				} else if( childNode.nodeName().equals( "b" )
						|| childNode.nodeName().equals( "i" )
						|| childNode.nodeName().equals( "u" ) ) {
					Node badNode = _validateContent( childNode );
					if( badNode != null )
						return badNode;
				} else {
					return childNode; // Bad Node
				}
			}
			
		}
		
		return null;
	
	}

	private static AlignmentType _getAlignment( Node node ) {
		
		if( node.hasAttr( "style" ) && ! node.attr( "style" ).trim().isEmpty() ) {
			for( String style : node.attr( "style" ).split( ";" ) ) {
				if( ! style.trim().isEmpty() && style.substring( 0, style.indexOf( ":" ) ).trim().equals( "text-align" ) ) {
					String val = style.substring( style.indexOf( ":" ) + 1 ).trim(); 
					for( AlignmentType at : AlignmentType.values() ) {
						if( val.equalsIgnoreCase( at.name() ) ) {
							return at;
						}
					}
				}
			}
		}

		return null;

	}
	
	private static JsonObject _createImageData( Long pratilipiId, Node imageNode ) throws UnexpectedServerException {
		
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		
		String imageName = null;
		BlobEntry blobEntry = null;
		
		String imageUrl = imageNode.attr( "src" );
		if( imageUrl.indexOf( "pratilipiId=" + pratilipiId ) == -1 || imageUrl.indexOf( "name=" ) == -1 ) {
			imageName = imageUrl.replaceAll( "[:/.?=&+]+", "_" );
			String fileName = _createImageFullName( pratilipiId, imageName );
			blobEntry = blobAccessor.getBlob( fileName );
			if( blobEntry == null ) {
				blobEntry = HttpUtil.doGet( imageUrl );
				if( ! blobEntry.getMimeType().startsWith( "image/" ) ) {
					logger.log( Level.SEVERE, "Ignoring image " + imageUrl );
					return null;
				}
				blobEntry.setName( fileName );
				blobAccessor.createOrUpdateBlob( blobEntry );
			}
		} else {
			imageName = imageUrl.substring( imageUrl.indexOf( "name=" ) + 5 );
			if( imageName.indexOf( '&' ) != -1 )
				imageName = imageName.substring( 0, imageName.indexOf( '&' ) );
			imageName = imageName.replace( "%20", " " );
			blobEntry = blobAccessor.getBlob( _createImageFullName( pratilipiId, imageName ) );
		}

		
		int width = ImageUtil.getWidth( blobEntry );
		int height = ImageUtil.getHeight( blobEntry );
		
		int setWidth = width;
		if( imageNode.hasAttr( "width" ) && ! imageNode.attr( "width" ).trim().isEmpty() )
			setWidth = Integer.parseInt( imageNode.attr( "width" ).trim() );
		
		JsonObject imgData = new JsonObject();
		imgData.addProperty( "name", imageName );
		imgData.addProperty( "width", width );
		imgData.addProperty( "height", height );
		imgData.addProperty( "ratio", (double) setWidth/width );

		return imgData;

	}

	
	public static void updatePratilipiContent( Long pratilipiId ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		
		PratilipiContentDoc pcDoc = DataAccessorFactory.getDocAccessor().newPratilipiContentDoc();
		
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
		
		if( ! pratilipi.isOldContent() ) {

			return;
			
		} else if( pratilipi.getContentType() == PratilipiContentType.PRATILIPI ) {
		
			BlobEntry blobEntry = blobAccessor.getBlob( "pratilipi-content/pratilipi/" + pratilipiId );
			if( blobEntry == null )
				return;
			String contentHtml = new String( blobEntry.getData(), Charset.forName( "UTF-8" ) );
			
			List<Object[]> pageletList = _createPageletList( pratilipi, Jsoup.parse( contentHtml ).body() );
			
			if( pageletList.size() > 0 ) {
				
				PratilipiContentDoc.Chapter chapter = null;
				if( pageletList.get( 0 )[0] != PratilipiContentDoc.PageletType.HEAD )
					chapter = pcDoc.addChapter( pratilipi.getTitle() == null ? pratilipi.getTitleEn() : pratilipi.getTitle() );
				
				for( Object[] pagelet : pageletList ) {
					if( pagelet[0] == PratilipiContentDoc.PageletType.HEAD ) {
						chapter = pcDoc.addChapter( (String) pagelet[1] );
					} else {
						PratilipiContentDoc.Page page = chapter.getPage( 1 );
						if( page == null )
							page = chapter.addPage();
						page.addPagelet( (PratilipiContentDoc.PageletType) pagelet[0], pagelet[1], (PratilipiContentDoc.AlignmentType) pagelet[2] );
					}
				}
				
			}
			
		} else if( pratilipi.getContentType() == PratilipiContentType.IMAGE ) {
			
			for( int i = 1; i <= pratilipi.getPageCount(); i++ ) {
				
				BlobEntry blobEntry = blobAccessor.getBlob( "pratilipi/" + pratilipiId + "/images/" + i );
				
				if( pratilipi.getId() == 5639838220943360L && i <= 5 )
					continue; // Skipping first 5 pages as per Shally's request
				else if( pratilipi.getId() == 5749258686824448L && i <= 4 )
					continue; // Skipping first 4 pages as per Shally's request
				else if( pratilipi.getId() == 5486454792781824L && i <= 1 )
					continue; // Skipping first page as per Shally's request
				else if( blobEntry == null && pratilipi.getId() == 5768181499035648L )
					continue; // Skipping missing pages as per Dileepan's request
				
				JsonObject imgData = new JsonObject();
				imgData.addProperty( "name", i + "" );
				imgData.addProperty( "height", ImageUtil.getHeight( blobEntry ) );
				imgData.addProperty( "width", ImageUtil.getWidth( blobEntry ) );
				
				pcDoc.addChapter( null )
					.addPage()
					.addPagelet( PratilipiContentDoc.PageletType.IMAGE, imgData );
				
			}
			
		} else {
			
			throw new UnexpectedServerException( "ContentType " + pratilipi.getContentType() + " is not supported !" );
			
		}
		
		docAccessor.save( pratilipiId, pcDoc );
		
	}
	
	private static List<Object[]> _createPageletList( Pratilipi pratilipi, Node node ) throws UnexpectedServerException {
		
		List<Object[]> pageletList = new LinkedList<>();
		
		Object[] currPagelet = null;
		for( Node childNode : node.childNodes() ) {
			
			if( childNode.nodeName().equals( "body" )
					|| childNode.nodeName().equals( "div" )
					|| childNode.nodeName().equals( "p" ) ) {
				
				currPagelet = null;

				List<Object[]> pList = _createPageletList( pratilipi, childNode );
				
				if( pList.size() == 0 ) {
					pageletList.add( new Object[] { PratilipiContentDoc.PageletType.HTML, "<br/>", null } );
				} else {
					AlignmentType alignment = _getAlignment( childNode );
					if( alignment != null )
						for( Object[] pagelet : pList )
							if( pagelet[2] == null && ( pagelet[0] == PratilipiContentDoc.PageletType.TEXT || pagelet[0] == PratilipiContentDoc.PageletType.HTML ) )
								pagelet[2] = alignment;
					pageletList.addAll( pList );
				}
				
			} else if( childNode.nodeName().equals( "h1" ) || childNode.nodeName().equals( "h2" ) ) {
				
				String text = _extractText( childNode );
				if( text == null )
					continue;
				
				if( currPagelet != null && currPagelet[0] == PratilipiContentDoc.PageletType.HEAD ) {
					currPagelet[1] = currPagelet[1] + " - " + text;
				} else {
					currPagelet = new Object[] { PratilipiContentDoc.PageletType.HEAD, text, null };
					pageletList.add( currPagelet );
				}
				
			} else if( childNode.nodeName().equals( "img" ) ) {
				
				currPagelet = null;
				
				BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
				BlobEntry blobEntry = null;
				String imageUrl = childNode.attr( "src" );
				String imageName = null;
				if( imageUrl.indexOf( "name=" ) != -1 ) {
					imageName = imageUrl.substring( imageUrl.indexOf( "name=" ) + 5 );
					if( imageName.indexOf( '&' ) != -1 )
						imageName = imageName.substring( 0, imageName.indexOf( '&' ) );
					imageName = imageName.replace( "%20", " " );
					String fileName = _createImageFullName( pratilipi.getId(), imageName );
					blobEntry = blobAccessor.getBlob( fileName );
					if( blobEntry == null ) { // Copying from old resource location
						blobEntry = blobAccessor.getBlob( "pratilipi-resource/" + pratilipi.getId() + "/" + imageName );
						if( blobEntry != null ) {
							blobEntry.setName( fileName );
							blobAccessor.createOrUpdateBlob( blobEntry );
						}
					}
					if( blobEntry == null && imageUrl.indexOf( "pratilipiId=" ) != -1 ) { // Copying from old resource location of another Pratilipi
						String pratilipiIdStr = imageUrl.substring( imageUrl.indexOf( "pratilipiId=" ) + 12 );
						if( pratilipiIdStr.indexOf( '&' ) != -1 )
							pratilipiIdStr = pratilipiIdStr.substring( 0, pratilipiIdStr.indexOf( '&' ) );
						blobEntry = blobAccessor.getBlob( "pratilipi-resource/" + pratilipiIdStr + "/" + imageName );
						if( blobEntry != null ) {
							blobEntry.setName( fileName );
							blobAccessor.createOrUpdateBlob( blobEntry );
						}
					}
					if( blobEntry == null )
						continue;
				} else if( imageUrl.startsWith( "http" ) ) {
					imageName = imageUrl.replaceAll( "[:/.?=&+]+", "_" );
					String fileName = _createImageFullName( pratilipi.getId(), imageName );
					blobEntry = blobAccessor.getBlob( fileName );
					if( blobEntry == null ) {
						blobEntry = HttpUtil.doGet( imageUrl );
						if( ! blobEntry.getMimeType().startsWith( "image/" ) )
							continue;
						blobEntry.setName( fileName );
						blobAccessor.createOrUpdateBlob( blobEntry );
					}
				} else if( imageUrl.startsWith( "data:" ) && imageUrl.indexOf( "base64" ) != -1 ) {
					imageName = UUID.randomUUID().toString();
					String mimeType = imageUrl.substring( 5, imageUrl.indexOf( ';' ) );
					String base64String = imageUrl.substring( imageUrl.indexOf( "base64," ) + 7 );
					blobEntry = blobAccessor.newBlob(
							_createImageFullName( pratilipi.getId(), imageName ),
							Base64.decodeBase64( base64String ),
							mimeType );
					blobAccessor.createOrUpdateBlob( blobEntry );
				} else if( imageUrl.startsWith( "file:///" ) || imageUrl.startsWith( "C:" ) ) {
					continue;
				}
				
				JsonObject imgData = new JsonObject();
				imgData.addProperty( "name", imageName );
				imgData.addProperty( "height", ImageUtil.getHeight( blobEntry ) );
				imgData.addProperty( "width", ImageUtil.getWidth( blobEntry ) );
				
				pageletList.add( new Object[] { PratilipiContentDoc.PageletType.IMAGE, imgData, null } );
				
			} else if( childNode.nodeName().equals( "br" ) ) {
				
				if( currPagelet != null && currPagelet[0] == PratilipiContentDoc.PageletType.HTML )
					currPagelet[1] = currPagelet[1] + "<br/>";
				
			} else {
				
				String text = _extractText( childNode );
				if( text == null )
					continue;
				if( childNode.nodeName().equals( "b" )
						|| childNode.nodeName().equals( "strong" )
						|| childNode.nodeName().equals( "h3" )
						|| childNode.nodeName().equals( "h4" )
						|| childNode.nodeName().equals( "h5" )
						|| childNode.nodeName().equals( "h6" ) )
					text = "<b>" + text + "</b>";
				
				if( currPagelet == null || currPagelet[0] != PratilipiContentDoc.PageletType.HTML ) {
					currPagelet = new Object[] { PratilipiContentDoc.PageletType.HTML, text, null };
					pageletList.add( currPagelet );
				} else {
					currPagelet[1] = currPagelet[1] + " " + text;
				}
				
			}
			
		}
		
		return pageletList;
		
	}
	
	private static String _extractText( Node node ) {
		if( node.getClass() == org.jsoup.nodes.Comment.class )
			return null;
		String text = node.getClass() == TextNode.class
				? ( (TextNode) node ).text()
				: ( (Element) node ).text();
		text = text.replace( (char) 160, ' ' ).trim();
		return text.isEmpty() ? null : text;
	}
	
	
	public static boolean updateMeta( Long pratilipiId ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
		
		
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		PratilipiContentDoc pcDoc = docAccessor.getPratilipiContentDoc( pratilipiId );
		PratilipiMetaDoc pmDoc = docAccessor.getPratilipiMetaDoc( pratilipiId );
		if( pmDoc == null )
			pmDoc = docAccessor.newPratilipiMetaDoc();
		
		
		int wordCount = 0;
		int imageCount = 0;
		int chapterCount = 0;
		Map<String, Integer> wordCounts = new HashMap<>();
		
		for( PratilipiContentDoc.Chapter chapter : pcDoc.getChapterList() ) {
			
			String content = chapter.getTitle() == null ? "" : chapter.getTitle();
			
			for( PratilipiContentDoc.Page page : chapter.getPageList() ) {
				for( PratilipiContentDoc.Pagelet pagelet : page.getPageletList() ) {
					if( pagelet.getType() == PratilipiContentDoc.PageletType.IMAGE )
						imageCount++;
					else
						content += " " + pagelet.getDataAsString();
				}
			}

			content = content.trim();
			if( content.length() > 1 ) {
				String[] words = content.replaceAll( nonKeywordsPattern, " " ).split( "[\\s]+" );
				for( String word : words ) {
					Integer count = wordCounts.get( word );
					count = count == null ? 1 : count++;
					wordCounts.put( word, count );
				}
				wordCount += words.length;
			}
			
			chapterCount++;
			
		}

		
		boolean isChanged = wordCounts.size() != pmDoc.getWordCounts().size();
		if( ! isChanged ) {
			for( Entry<String, Integer> entry : pmDoc.getWordCounts().entrySet() ) {
				if( ! entry.getValue().equals( wordCounts.get( entry.getKey() ) ) ) {
					isChanged = true;
					break;
				}
			}
		}
		
		
		if( isChanged ) {
			// Update & Save Pratilipi Meta Doc
			pmDoc.setWordCounts( wordCounts );
			docAccessor.save( pratilipiId, pmDoc );
			// Update counts in Pratilipi Entity
			AuditLog auditLog = dataAccessor.newAuditLog(
					AccessTokenFilter.getAccessToken(),
					AccessType.PRATILIPI_UPDATE,
					pratilipi );
			pratilipi.setWordCount( wordCount );
			pratilipi.setImageCount( imageCount );
			pratilipi.setChapterCount( chapterCount );
			pratilipi = dataAccessor.createOrUpdatePratilipi( pratilipi, auditLog );
		}

		
		return isChanged;
		
	}

	
	public static void updatePratilipiReviews( Long pratilipiId ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		
		List<Comment> commentList = dataAccessor.getCommentListByReference( ReferenceType.PRATILIPI, pratilipiId );
		Map<String, List<Comment>> reviewIdCommentListMap = new HashMap<>();
		for( Comment comment : commentList ) {
			if( comment.getParentType() != CommentParentType.REVIEW )
				continue;
			List<Comment> reviewCommentList = reviewIdCommentListMap.get( comment.getParentId() );
			if( reviewCommentList == null ) {
				reviewCommentList = new LinkedList<>();
				reviewIdCommentListMap.put( comment.getParentId(), reviewCommentList );
			}
			reviewCommentList.add( comment );
		}
		
		List<Vote> voteList = dataAccessor.getVoteListByReference( ReferenceType.PRATILIPI, pratilipiId );
		Map<String, List<Vote>> reviewIdLikeVotesMap = new HashMap<>();
		Map<String, List<Long>> commentIdLikedByUserIdsMap = new HashMap<>();
		for( Vote vote : voteList ) {
			if( vote.getType() == VoteType.NONE ) {
				continue;
			} else if( vote.getParentType() == VoteParentType.REVIEW ) {
				List<Vote> reviewLikeVoteList = reviewIdLikeVotesMap.get( vote.getParentId() );
				if( reviewLikeVoteList == null ) {
					reviewLikeVoteList = new LinkedList<>();
					reviewIdLikeVotesMap.put( vote.getParentId(), reviewLikeVoteList );
				}
				reviewLikeVoteList.add( vote );
			} else if( vote.getParentType() == VoteParentType.COMMENT ) {
				List<Long> userIdList = commentIdLikedByUserIdsMap.get( vote.getParentId() );
				if( userIdList == null ) {
					userIdList = new LinkedList<>();
					commentIdLikedByUserIdsMap.put( vote.getParentId(), userIdList );
				}
				userIdList.add( vote.getUserId() );
			}
		}
		
		
		List<UserPratilipi> userPratilipiList = dataAccessor.getUserPratilipiList( null, pratilipiId, null, null, true ).getDataList();
		long ratingCount = 0;
		long totalRating = 0;
		List<UserPratilipiDoc> reviewDocList = new ArrayList<>();
		for( UserPratilipi userPratilipi : userPratilipiList ) {
			
			if( userPratilipi.getRating() != null && userPratilipi.getRating() > 0 ) {
				ratingCount++;
				totalRating += userPratilipi.getRating();
			}
			
			if( userPratilipi.getReviewState() != UserReviewState.PUBLISHED )
				continue;
			
			if( ( userPratilipi.getReviewTitle() == null || userPratilipi.getReviewTitle().trim().isEmpty() )
					&& ( userPratilipi.getReview() == null || userPratilipi.getReview().trim().isEmpty() ) )
				continue;
			
			UserPratilipiDoc reviewDoc = docAccessor.newUserPratilipiDoc();
			reviewDoc.setId( userPratilipi.getId() );
			reviewDoc.setUserId( userPratilipi.getUserId() );

			reviewDoc.setRating( userPratilipi.getRating() );
			reviewDoc.setReviewTitle( userPratilipi.getReviewTitle() == null || userPratilipi.getReviewTitle().trim().isEmpty() ? null : userPratilipi.getReviewTitle().trim() );
			reviewDoc.setReview( userPratilipi.getReview() == null || userPratilipi.getReview().trim().isEmpty() ? null : userPratilipi.getReview().trim() );
			
			reviewDoc.setReviewDate( userPratilipi.getReviewDate() );
			reviewDocList.add( reviewDoc );
			
			
			List<Vote> reviewLikeVoteList = reviewIdLikeVotesMap.get( userPratilipi.getId() );
			if( reviewLikeVoteList != null ) {
				List<Long> userIdList = new ArrayList<>( reviewLikeVoteList.size() );
				for( Vote vote : reviewLikeVoteList ) {
					if( vote.getLastUpdated().before( userPratilipi.getReviewDate() ) )
						continue;
					userIdList.add( vote.getUserId() );
				}
				reviewDoc.setLikedByUserIds( userIdList );
			}
			
			
			List<Comment> reviewCommentList = reviewIdCommentListMap.get( userPratilipi.getId() );
			if( reviewCommentList != null ) {
				List<CommentDoc> commentDocList = new ArrayList<>( reviewCommentList.size() );
				for( Comment comment : reviewCommentList ) {
					if( comment.getState() == CommentState.DELETED )
						continue;
					
					if( comment.getCreationDate().before( userPratilipi.getReviewDate() ) )
						continue;
					
					CommentDoc commentDoc = docAccessor.newCommentDoc();
					commentDoc.setId( comment.getId() );
					commentDoc.setUserId( comment.getUserId() );
					commentDoc.setContent( comment.getContent() );
					commentDoc.setCreationDate( comment.getCreationDate() );
					commentDoc.setLastUpdated( comment.getLastUpdated() );
					commentDoc.setLikedByUserIds( commentIdLikedByUserIdsMap.get( comment.getId().toString() ) );
					
					commentDocList.add( commentDoc );
				}
				reviewDoc.setComments( commentDocList );
			}
			
		}
		
		PratilipiReviewsDoc reviewsDoc = docAccessor.newPratilipiReviewsDoc();
		reviewsDoc.setRatingCount( ratingCount );
		reviewsDoc.setTotalRating( totalRating );
		reviewsDoc.setReviews( reviewDocList );
		docAccessor.save( pratilipiId, reviewsDoc );
		
	}
	
	
 	public static List<Long> updatePratilipiGoogleAnalyticsPageViews( int year, int month, int day )
			throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		
		Gson gson = new Gson();
		
		String dateStr = year
				+ ( month < 10 ? "-0" + month : "-" + month )
				+ ( day < 10 ? "-0" + day : "-" + day );
		
		String fileName = "pratilipi-google-analytics/page-views/" + dateStr;
		BlobEntry blobEntry = blobAccessor.getBlob( fileName );
		if( blobEntry == null ) {
			try {
				blobEntry = blobAccessor.newBlob( fileName, "{}".getBytes( "UTF-8" ), "application/json" );
			} catch( UnsupportedEncodingException e ) {
				logger.log( Level.SEVERE, e.getMessage() );
				throw new UnexpectedServerException();
			}
		}
		
		
		@SuppressWarnings("serial")
		Map<String, Integer> oldPageViewsMap = gson.fromJson(
				new String( blobEntry.getData(), Charset.forName( "UTF-8" ) ),
				new TypeToken<Map<String, Integer>>(){}.getType() );
		
		Map<String, Integer> newPageViewsMap =
				GoogleAnalyticsApi.getPageViews( dateStr );
		
		Map<String, Integer> diffPageViewsMap = new HashMap<>();
		for( Entry<String, Integer> entry : newPageViewsMap.entrySet() )
			if( ! entry.getValue().equals( oldPageViewsMap.get( entry.getKey() ) ) )
				diffPageViewsMap.put( entry.getKey(), entry.getValue() );
		
		
		Map<Long, Integer> pageViewsMap = new HashMap<>();
		Map<Long, Integer> readPageViewsMap = new HashMap<>();
		
		for( Entry<String, Integer> entry : diffPageViewsMap.entrySet() ) {
			
			String uri = entry.getKey();
			
			if( ! uri.startsWith( "/read?id=" ) ) { // Summary Page
				
				if( uri.indexOf( '?' ) != -1 )
					uri = uri.substring( 0, uri.indexOf( '?' ) );
				
				Page page = dataAccessor.getPage( uri );
				if( page != null && page.getType() == PageType.PRATILIPI ) {
					Long pratilpiId = page.getPrimaryContentId();
					if( pageViewsMap.get( pratilpiId ) == null )
						pageViewsMap.put( pratilpiId, entry.getValue() );
					else
						pageViewsMap.put( pratilpiId, pageViewsMap.get( pratilpiId ) + entry.getValue() );
				}
				
			} else { // Reader
				
				String patilipiIdStr = uri.indexOf( '&' ) == -1
						? uri.substring( "/read?id=".length() )
						: uri.substring( "/read?id=".length(), uri.indexOf( '&' ) );
						
				try {
					Long pratilpiId = Long.parseLong( patilipiIdStr );
					if( readPageViewsMap.get( pratilpiId ) == null )
						readPageViewsMap.put( pratilpiId, entry.getValue() );
					else
						readPageViewsMap.put( pratilpiId, readPageViewsMap.get( pratilpiId ) + entry.getValue() );
				} catch( NumberFormatException e ) {
					logger.log( Level.SEVERE, "Exception while processing reader uri " + uri, e );
				}
				
			}
			
		}
		
		
		for( Entry<Long, Integer> entry : pageViewsMap.entrySet() ) {
			if( readPageViewsMap.get( entry.getKey() ) == null ) {
				updatePratilipiGoogleAnalyticsPageViews( entry.getKey(), year, month, day, entry.getValue(), 0 );
			} else {
				updatePratilipiGoogleAnalyticsPageViews( entry.getKey(), year, month, day, entry.getValue(), readPageViewsMap.get( entry.getKey() ) );
				readPageViewsMap.remove( entry.getKey() );
			}
		}
		
		for( Entry<Long, Integer> entry : readPageViewsMap.entrySet() )
			updatePratilipiGoogleAnalyticsPageViews( entry.getKey(), year, month, day, 0, entry.getValue() );
		
		
		if( diffPageViewsMap.size() > 0 ) {
			try {
				blobEntry.setData( gson.toJson( newPageViewsMap ).getBytes( "UTF-8" ) );
				blobAccessor.createOrUpdateBlob( blobEntry );
			} catch( UnsupportedEncodingException e ) {
				logger.log( Level.SEVERE, e.getMessage() );
				throw new UnexpectedServerException();
			}
		}

		
		ArrayList<Long> updatedPratilipiIdList = new ArrayList<>( pageViewsMap.size() + readPageViewsMap.size() );
		updatedPratilipiIdList.addAll( pageViewsMap.keySet() );
		updatedPratilipiIdList.addAll( readPageViewsMap.keySet() );
		
		return updatedPratilipiIdList;
		
	}
	
	public static void updatePratilipiGoogleAnalyticsPageViews( Long pratilipiId, int year, int month, int day, int pageViews, int readPageViews )
			throws UnexpectedServerException {
		
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		
		PratilipiGoogleAnalyticsDoc gaDoc = docAccessor.getPratilipiGoogleAnalyticsDoc( pratilipiId );
		
		gaDoc.setPageViews( year, month, day, pageViews );
		gaDoc.setReadPageViews( year, month, day, readPageViews );

		docAccessor.save( pratilipiId, gaDoc );
		
	}

	public static void updateFirebaseIndex( Long pratilipiId, PratilipiContentDoc pcDoc )
			throws UnexpectedServerException {

		RtdbAccessor rtdbAccessor = DataAccessorFactory.getRtdbAccessor();

		JsonObject chapters = new JsonObject();
		for( Chapter chapter : pcDoc.getChapterList() ) {
			JsonObject meta = new JsonObject();
			meta.addProperty( "lastUpdated", chapter.getLastUpdated() );
			meta.addProperty( "lastUpdatedBy", chapter.getLastUpdatedBy().toString() );
			chapters.add( chapter.getId(), meta );
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.add( "index", chapters );

		rtdbAccessor.setPratilipiIndex( pratilipiId, jsonObject );

	}

	public static String generateChapterId( Long pratilipiId ) {
		return generateChapterId( pratilipiId, null );
	}

	public static String generateChapterId( Long pratilipiId, Long chapterId ) {
		return "ch-" + pratilipiId + "-" + ( chapterId != null ? chapterId : new Date().getTime() );
	}

	public static PratilipiContentDoc _validateChapterIdInContent( Long pratilipiId )
			throws UnexpectedServerException {

		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();

		// Doc
		PratilipiContentDoc pcDoc = docAccessor.getPratilipiContentDoc( pratilipiId );
		if( pcDoc == null )
			return null;

		boolean shouldBackfill = false;
		// chapters might be empty list -> chapters.get(0).getId() -> ArrayIndexOutOfBoundsException
		for( Chapter chapter : pcDoc.getChapterList() ) {
			if( chapter.getId() == null ) {
				shouldBackfill = true;
				break;
			}
		}

		if( shouldBackfill ) {
			Long chapterId = new Date().getTime();
			for( Chapter chapter : pcDoc.getChapterList() ) {
				if( chapter.getId() == null ) {
					chapter.setId( generateChapterId( pratilipiId, chapterId++ ) );
					chapter.setLastUpdated( UxModeFilter.isAndroidApp() ? PratilipiContentDoc.FrontendType.ANDROID : PratilipiContentDoc.FrontendType.WEB );
				}
			}
			docAccessor.save( pratilipiId, pcDoc );
			updateFirebaseIndex( pratilipiId, pcDoc );
		}

		return pcDoc;

	}

}
