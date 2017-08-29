package com.pratilipi.data.type.doc;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pratilipi.data.type.PratilipiContentDoc;


public class PratilipiContentDocImpl implements PratilipiContentDoc {

	public static class PageletImpl implements PratilipiContentDoc.Pagelet {

		private PageletType type;

		private Object data;

		private AlignmentType alignment;


		@SuppressWarnings("unused")
		private PageletImpl() {}

		public PageletImpl( PageletType type, Object data, AlignmentType alignment ) {
			this.type = type;
			this.data = data;
			this.alignment = alignment;
		}


		@Override
		public PageletType getType() {
			return type;
		}

		@Override
		public void setType( PageletType type ) {
			this.type = type;
		}

		@Override
		public String getDataAsString() {
			return data.toString();
		}

		@Override
		public JsonObject getData() {
			if( data.getClass() != JsonObject.class )
				data = new Gson().toJsonTree( data ).getAsJsonObject();
			return (JsonObject) data;
		}

		@Override
		public void setData( Object data ) {
			this.data = data;
		}

		@Override
		public AlignmentType getAlignment() {
			return alignment;
		}

	}

	public static class PageImpl implements PratilipiContentDoc.Page {

		private List<PageletImpl> pagelets;

		private String html;


		public PageImpl() {}


		@Override
		public List<Pagelet> getPageletList() {
			return pagelets == null
					? new ArrayList<Pagelet>( 0 )
					: new ArrayList<Pagelet>( pagelets );
		}

		@Override
		public void addPagelet( PageletType type, Object data ) {
			addPagelet( type, data, null );
		}

		@Override
		public void addPagelet( PageletType type, Object data, AlignmentType alignment ) {
			if( pagelets == null )
				pagelets = new LinkedList<>();
			pagelets.add( new PageletImpl( type, data, alignment ) );
		}

		@Override
		public void deleteAllPagelets() {
			pagelets = null;
		}

		@Override
		public String getHtml() {
			return html;
		}

		@Override
		public void setHtml( String html ) {
			this.html = html;
		}



	}

	public static class ChapterImpl implements PratilipiContentDoc.Chapter {

		private String id;

		private String title;

		private List<PageImpl> pages;

		private Integer nesting;

		private Long lastUpdated;

		private FrontendType lastUpdatedBy;


		private ChapterImpl() {}


		public ChapterImpl( String title ) {
			this.title = title;
		}

		public ChapterImpl( String title, Integer nesting ) {
			this.title = title;
			this.nesting = nesting;
		}


		@Override
		public String getId() {
			return id;
		}

		@Override
		public void setId( String id ) {
			this.id = id;
		}

		@Override
		public Long getLastUpdated() { return lastUpdated; }

		@Override
		public FrontendType getLastUpdatedBy() { return lastUpdatedBy; }

		@Override
		public void setLastUpdated( FrontendType lastUpdatedBy ) { this.lastUpdated = new Date().getTime(); this.lastUpdatedBy = lastUpdatedBy; }

		@Override
		public String getTitle() {
			return title;
		}

		@Override
		public void setTitle( String title ) {
			this.title = title;
		}

		@Override
		public int getPageCount() {
			return pages == null ? 0 : pages.size();
		}

		@Override
		public Page getPage( int pageNo ) {
			return pages == null || pages.size() < pageNo ? null : pages.get( pageNo - 1 );
		}

		@Override
		public Page addPage() {
			if( pages == null )
				pages = new LinkedList<>();
			PageImpl page = new PageImpl();
			pages.add( page );
			return page;
		}

		@Override
		public Page addPage( int pageNo ) {
			if( pages == null )
				pages = new LinkedList<>();
			if( pageNo <= pages.size() + 1 )
				pages.add( pageNo - 1, new PageImpl() );
			else
				for( int i = pages.size(); i <= pageNo; i++ )
					pages.add( new PageImpl() );
			return pages.get( pageNo - 1 );
		}

		@Override
		public List<Page> getPageList() {
			return pages == null
					? new ArrayList<Page>( 0 )
					: new ArrayList<Page>( pages );
		}

		@Override
		public void deletePage( int pageNo ) {
			if( pages == null || pages.size() < pageNo )
				return;
			pages.remove( pageNo - 1 );
		}

		@Override
		public int getNesting() {
			return nesting == null ? 0 : nesting;
		}

	}



	public List<ChapterImpl> chapters;


	public PratilipiContentDocImpl() {}


	@Override
	public int getChapterCount() {
		return chapters == null ? 0 : chapters.size();
	}

	@Override
	public Chapter getChapter( int chapterNo ) {
		if( chapters != null && chapters.size() >= chapterNo )
			return chapters.get( chapterNo - 1 );
		return null;
	}

	@Override
	public Chapter getChapter( String chapterId ) {
		if( chapters != null ) {
			for( Chapter chapter : chapters )
				if( chapter.getId() != null && chapter.getId().equals( chapterId ) )
					return chapter;
		}
		return null;
	}

	@Override
	public List<Chapter> getChapterList() {
		return chapters == null
				? new ArrayList<Chapter>( 0 )
				: new ArrayList<Chapter>( chapters );
	}

	@Override
	public Chapter addChapter( String title ) {
		return addChapter( null, title, null );
	}

	@Override
	public Chapter addChapter( Integer chapterNo, String title ) {
		return addChapter( chapterNo, title, null );
	}

	@Override
	public Chapter addChapter( Integer chapterNo, String title, Integer nesting ) {

		if( chapters == null )
			chapters = new LinkedList<>();

		ChapterImpl chapter = nesting == null
				? new ChapterImpl( title )
				: new ChapterImpl( title, nesting );

		if( chapterNo == null )
			chapters.add( chapter );
		else if( chapterNo <= chapters.size() + 1 )
			chapters.add( chapterNo - 1, chapter );
		else
			for( int i = chapters.size(); i <= chapterNo; i++ )
				chapters.add( i - 1, i == chapterNo ? chapter : new ChapterImpl() );

		return chapter;

	}

	@Override
	public void setChapter( Integer chapterNo, Chapter chapter ) {
		if( chapters == null )
			chapters = new LinkedList<>();
		if( getChapter( chapterNo ) == null )
			addChapter( chapterNo, null, null );
		chapters.set( chapterNo - 1, (ChapterImpl) chapter );
	}

	@Override
	public void deleteChapter( int chapterNo ) {
		if( chapters != null && chapters.size() >= chapterNo )
			chapters.remove( chapterNo - 1 );
	}

	@Override
	public void deleteChapter( String chapterId ) {
		if( chapters != null ) {
			int index = -1;
			for( int i = 0; i < chapters.size(); i++ ) {
				if( chapters.get(i).getId() != null && chapters.get(i).getId().equals( chapterId ) ) {
					index = i;
					break;
				}
			}
			if( index != -1 )
				chapters.remove( index );
		}
	}

	@Override
	public JsonArray getIndex() {
		JsonArray index = new JsonArray();
		if( chapters != null ) {
			for( int chapterNo = 1; chapterNo <= chapters.size(); chapterNo++ ) {
				Chapter chapter = chapters.get( chapterNo - 1 );
				JsonObject indexItem = new JsonObject();
				indexItem.addProperty( "chapterId", chapter.getId() );
				indexItem.addProperty( "chapterNo", chapterNo );
				indexItem.addProperty( "title", chapter.getTitle() );
				indexItem.addProperty( "nesting", chapter.getNesting() );
				index.add( indexItem );
			}
		}
		return index;
	}

}
