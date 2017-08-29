package com.pratilipi.data.type;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface PratilipiContentDoc {

	enum PageletType {
		HEAD, @Deprecated HEAD_1, @Deprecated HEAD_2,
		TEXT, HTML, BLOCK_QUOTE, IMAGE,
		LIST_ORDERED, LIST_UNORDERED
	}

	enum AlignmentType {
		LEFT, CENTER, RIGHT, JUSTIFY
	}

	enum FrontendType {
		WEB, ANDROID
	}

	interface Pagelet {
		PageletType getType();
		void setType( PageletType type );
		JsonObject getData();
		String getDataAsString();
		void setData( Object data );
		AlignmentType getAlignment();
	}

	interface Page {
		List<Pagelet> getPageletList();
		void addPagelet( PageletType type, Object data );
		void addPagelet( PageletType type, Object data, AlignmentType alignmentType );
		void deleteAllPagelets();
		String getHtml();
		void setHtml( String html );
	}

	interface Chapter {
		String getId();
		void setId( String chapterId );
		Long getLastUpdated();
		FrontendType getLastUpdatedBy();
		void setLastUpdated( FrontendType lastUpdatedBy );
		String getTitle();
		void setTitle( String title );
		int getPageCount();
		Page getPage( int pageNo );
		List<Page> getPageList();
		Page addPage();
		Page addPage( int pageNo );
		void deletePage( int pageNo );
		int getNesting();
	}


	int getChapterCount();
	Chapter getChapter( int chapterNo );
	Chapter getChapter( String chapterId );
	List<Chapter> getChapterList();
	Chapter addChapter( String title );
	Chapter addChapter( Integer chapterNo, String title );
	Chapter addChapter( Integer chapterNo, String title, Integer nesting );
	void setChapter( Integer chapterNo, Chapter chapter );
	void deleteChapter( int chapterNo );
	void deleteChapter( String chapterId );
	JsonArray getIndex();

}
