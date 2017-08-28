package com.pratilipi.data.mock;

import static com.pratilipi.data.mock.AuthorMock.guAuthor_1;
import static com.pratilipi.data.mock.AuthorMock.hiAuthor_1;
import static com.pratilipi.data.mock.AuthorMock.taAuthor_1;
import static com.pratilipi.data.mock.EventMock.guEvent_1;
import static com.pratilipi.data.mock.EventMock.hiEvent_1;
import static com.pratilipi.data.mock.EventMock.taEvent_1;
import static com.pratilipi.data.mock.BlogMock.blog;
import static com.pratilipi.data.mock.BlogPostMock.guBlogPost_1;
import static com.pratilipi.data.mock.BlogPostMock.guBlogPost_2;
import static com.pratilipi.data.mock.BlogPostMock.guBlogPost_3;
import static com.pratilipi.data.mock.BlogPostMock.hiBlogPost_1;
import static com.pratilipi.data.mock.BlogPostMock.hiBlogPost_2;
import static com.pratilipi.data.mock.BlogPostMock.hiBlogPost_3;
import static com.pratilipi.data.mock.BlogPostMock.taBlogPost_1;
import static com.pratilipi.data.mock.BlogPostMock.taBlogPost_2;
import static com.pratilipi.data.mock.BlogPostMock.taBlogPost_3;
import static com.pratilipi.data.mock.PratilipiMock.guPratilipi_1;
import static com.pratilipi.data.mock.PratilipiMock.guPratilipi_2;
import static com.pratilipi.data.mock.PratilipiMock.guPratilipi_3;
import static com.pratilipi.data.mock.PratilipiMock.hiPratilipi_1;
import static com.pratilipi.data.mock.PratilipiMock.hiPratilipi_2;
import static com.pratilipi.data.mock.PratilipiMock.hiPratilipi_3;
import static com.pratilipi.data.mock.PratilipiMock.taPratilipi_1;
import static com.pratilipi.data.mock.PratilipiMock.taPratilipi_2;
import static com.pratilipi.data.mock.PratilipiMock.taPratilipi_3;

import java.util.LinkedList;
import java.util.List;

import com.pratilipi.common.type.PageType;
import com.pratilipi.data.type.Page;
import com.pratilipi.data.type.gae.PageEntity;

public class PageMock {

	public static final List<Page> PAGE_TABLE = new LinkedList<>();

	public static final Page home_Page = new PageEntity( 1L );

	public static final Page hiAuthor_1_Page = new PageEntity( 101L );
	public static final Page guAuthor_1_Page = new PageEntity( 102L );
	public static final Page taAuthor_1_Page = new PageEntity( 103L );
	
	public static final Page hiEvent_1_Page = new PageEntity( 201L );
	public static final Page guEvent_1_Page = new PageEntity( 202L );
	public static final Page taEvent_1_Page = new PageEntity( 203L );

	public static final Page blog_Page = new PageEntity( 300L );
	public static final Page hiBlogPost_1_Page = new PageEntity( 301L );
	public static final Page hiBlogPost_2_Page = new PageEntity( 302L );
	public static final Page hiBlogPost_3_Page = new PageEntity( 303L );
	public static final Page guBlogPost_1_Page = new PageEntity( 304L );
	public static final Page guBlogPost_2_Page = new PageEntity( 305L );
	public static final Page guBlogPost_3_Page = new PageEntity( 306L );
	public static final Page taBlogPost_1_Page = new PageEntity( 307L );
	public static final Page taBlogPost_2_Page = new PageEntity( 308L );
	public static final Page taBlogPost_3_Page = new PageEntity( 309L );

	public static final Page hiPratilipi_1_Page = new PageEntity( 10101L );
	public static final Page hiPratilipi_2_Page = new PageEntity( 10102L );
	public static final Page hiPratilipi_3_Page = new PageEntity( 10103L );
	
	public static final Page guPratilipi_1_Page = new PageEntity( 10201L );
	public static final Page guPratilipi_2_Page = new PageEntity( 10202L );
	public static final Page guPratilipi_3_Page = new PageEntity( 10203L );
	
	public static final Page taPratilipi_1_Page = new PageEntity( 10301L );
	public static final Page taPratilipi_2_Page = new PageEntity( 10302L );
	public static final Page taPratilipi_3_Page = new PageEntity( 10303L );

	public static final Page hiPratilipi_1_ReadPage = new PageEntity( 1010101L );
	public static final Page hiPratilipi_2_ReadPage = new PageEntity( 1010201L );
	public static final Page hiPratilipi_3_ReadPage = new PageEntity( 1010301L );
	
	public static final Page guPratilipi_1_ReadPage = new PageEntity( 1020101L );
	public static final Page guPratilipi_2_ReadPage = new PageEntity( 1020201L );
	public static final Page guPratilipi_3_ReadPage = new PageEntity( 1020301L );
	
	public static final Page taPratilipi_1_ReadPage = new PageEntity( 1030101L );
	public static final Page taPratilipi_2_ReadPage = new PageEntity( 1030201L );
	public static final Page taPratilipi_3_ReadPage = new PageEntity( 1030301L );


	static {
		PAGE_TABLE.add( home_Page );

		PAGE_TABLE.add( hiAuthor_1_Page );
		PAGE_TABLE.add( guAuthor_1_Page );
		PAGE_TABLE.add( taAuthor_1_Page );

		PAGE_TABLE.add( hiEvent_1_Page );
		PAGE_TABLE.add( guEvent_1_Page );
		PAGE_TABLE.add( taEvent_1_Page );

		PAGE_TABLE.add( blog_Page );
		PAGE_TABLE.add( hiBlogPost_1_Page );
		PAGE_TABLE.add( hiBlogPost_2_Page );
		PAGE_TABLE.add( hiBlogPost_3_Page );
		PAGE_TABLE.add( guBlogPost_1_Page );
		PAGE_TABLE.add( guBlogPost_2_Page );
		PAGE_TABLE.add( guBlogPost_3_Page );
		PAGE_TABLE.add( taBlogPost_1_Page );
		PAGE_TABLE.add( taBlogPost_2_Page );
		PAGE_TABLE.add( taBlogPost_3_Page );

		PAGE_TABLE.add( hiPratilipi_1_Page );
		PAGE_TABLE.add( hiPratilipi_2_Page );
		PAGE_TABLE.add( hiPratilipi_3_Page );

		PAGE_TABLE.add( guPratilipi_1_Page );
		PAGE_TABLE.add( guPratilipi_2_Page );
		PAGE_TABLE.add( guPratilipi_3_Page );

		PAGE_TABLE.add( taPratilipi_1_Page );
		PAGE_TABLE.add( taPratilipi_2_Page );
		PAGE_TABLE.add( taPratilipi_3_Page );

		PAGE_TABLE.add( hiPratilipi_1_ReadPage );
		PAGE_TABLE.add( hiPratilipi_2_ReadPage );
		PAGE_TABLE.add( hiPratilipi_3_ReadPage );

		PAGE_TABLE.add( guPratilipi_1_ReadPage );
		PAGE_TABLE.add( guPratilipi_2_ReadPage );
		PAGE_TABLE.add( guPratilipi_3_ReadPage );

		PAGE_TABLE.add( taPratilipi_1_ReadPage );
		PAGE_TABLE.add( taPratilipi_2_ReadPage );
		PAGE_TABLE.add( taPratilipi_3_ReadPage );

		
		home_Page.setType( PageType.GENERIC );
		home_Page.setUri( null );
		home_Page.setUriAlias( "/" );
		home_Page.setPrimaryContentId( null );

		
		hiAuthor_1_Page.setType( PageType.AUTHOR );
		hiAuthor_1_Page.setUri( PageType.AUTHOR.getUrlPrefix() + hiAuthor_1.getId() );
		hiAuthor_1_Page.setUriAlias( "/hindi-author-1" );
		hiAuthor_1_Page.setPrimaryContentId( hiAuthor_1.getId() );

		guAuthor_1_Page.setType( PageType.AUTHOR );
		guAuthor_1_Page.setUri( PageType.AUTHOR.getUrlPrefix() + guAuthor_1.getId() );
		guAuthor_1_Page.setUriAlias( "/gujarati-author-1" );
		guAuthor_1_Page.setPrimaryContentId( guAuthor_1.getId() );

		taAuthor_1_Page.setType( PageType.AUTHOR );
		taAuthor_1_Page.setUri( PageType.AUTHOR.getUrlPrefix() + taAuthor_1.getId() );
		taAuthor_1_Page.setUriAlias( "/tamil-author-1" );
		taAuthor_1_Page.setPrimaryContentId( taAuthor_1.getId() );

		
		hiEvent_1_Page.setType( PageType.EVENT );
		hiEvent_1_Page.setUri( PageType.EVENT.getUrlPrefix() + hiEvent_1.getId() );
		hiEvent_1_Page.setUriAlias( "/event/hindi-event-1" );
		hiEvent_1_Page.setPrimaryContentId( hiEvent_1.getId() );

		guEvent_1_Page.setType( PageType.EVENT );
		guEvent_1_Page.setUri( PageType.EVENT.getUrlPrefix() + guEvent_1.getId() );
		guEvent_1_Page.setUriAlias( "/event/gujarati-event-1" );
		guEvent_1_Page.setPrimaryContentId( guEvent_1.getId() );

		taEvent_1_Page.setType( PageType.EVENT );
		taEvent_1_Page.setUri( PageType.EVENT.getUrlPrefix() + taEvent_1.getId() );
		taEvent_1_Page.setUriAlias( "/event/tamil-event-1" );
		taEvent_1_Page.setPrimaryContentId( taEvent_1.getId() );


		blog_Page.setType( PageType.BLOG );
		blog_Page.setUri( PageType.BLOG.getUrlPrefix() + blog.getId() );
		blog_Page.setUriAlias( "/blog" );
		blog_Page.setPrimaryContentId( blog.getId() );



		hiBlogPost_1_Page.setType( PageType.BLOG_POST );
		hiBlogPost_1_Page.setUri( PageType.BLOG_POST.getUrlPrefix() + hiBlogPost_1.getId() );
		hiBlogPost_1_Page.setUriAlias( "/blog/hindi-blog-1" );
		hiBlogPost_1_Page.setPrimaryContentId( hiBlogPost_1.getId() );
		
		hiBlogPost_2_Page.setType( PageType.BLOG_POST );
		hiBlogPost_2_Page.setUri( PageType.BLOG_POST.getUrlPrefix() + hiBlogPost_2.getId() );
		hiBlogPost_2_Page.setUriAlias( "/blog/hindi-blog-2" );
		hiBlogPost_2_Page.setPrimaryContentId( hiBlogPost_2.getId() );
		
		hiBlogPost_3_Page.setType( PageType.BLOG_POST );
		hiBlogPost_3_Page.setUri( PageType.BLOG_POST.getUrlPrefix() + hiBlogPost_3.getId() );
		hiBlogPost_3_Page.setUriAlias( "/blog/hindi-blog-3" );
		hiBlogPost_3_Page.setPrimaryContentId( hiBlogPost_3.getId() );

		guBlogPost_1_Page.setType( PageType.BLOG_POST );
		guBlogPost_1_Page.setUri( PageType.BLOG_POST.getUrlPrefix() + guBlogPost_1.getId() );
		guBlogPost_1_Page.setUriAlias( "/blog/gujarati-blog-1" );
		guBlogPost_1_Page.setPrimaryContentId( guBlogPost_1.getId() );

		guBlogPost_2_Page.setType( PageType.BLOG_POST );
		guBlogPost_2_Page.setUri( PageType.BLOG_POST.getUrlPrefix() + guBlogPost_2.getId() );
		guBlogPost_2_Page.setUriAlias( "/blog/gujarati-blog-2" );
		guBlogPost_2_Page.setPrimaryContentId( guBlogPost_2.getId() );

		guBlogPost_3_Page.setType( PageType.BLOG_POST );
		guBlogPost_3_Page.setUri( PageType.BLOG_POST.getUrlPrefix() + guBlogPost_3.getId() );
		guBlogPost_3_Page.setUriAlias( "/blog/gujarati-blog-3" );
		guBlogPost_3_Page.setPrimaryContentId( guBlogPost_3.getId() );

		taBlogPost_1_Page.setType( PageType.BLOG_POST );
		taBlogPost_1_Page.setUri( PageType.BLOG_POST.getUrlPrefix() + taBlogPost_1.getId() );
		taBlogPost_1_Page.setUriAlias( "/blog/tamil-blog-1" );
		taBlogPost_1_Page.setPrimaryContentId( taBlogPost_1.getId() );

		taBlogPost_2_Page.setType( PageType.BLOG_POST );
		taBlogPost_2_Page.setUri( PageType.BLOG_POST.getUrlPrefix() + taBlogPost_2.getId() );
		taBlogPost_2_Page.setUriAlias( "/blog/tamil-blog-2" );
		taBlogPost_2_Page.setPrimaryContentId( taBlogPost_2.getId() );

		taBlogPost_3_Page.setType( PageType.BLOG_POST );
		taBlogPost_3_Page.setUri( PageType.BLOG_POST.getUrlPrefix() + taBlogPost_3.getId() );
		taBlogPost_3_Page.setUriAlias( "/blog/tamil-blog-3" );
		taBlogPost_3_Page.setPrimaryContentId( taBlogPost_3.getId() );

		
		hiPratilipi_1_Page.setType( PageType.PRATILIPI );
		hiPratilipi_1_Page.setUri( PageType.PRATILIPI.getUrlPrefix() + hiPratilipi_1.getId() );
		hiPratilipi_1_Page.setUriAlias( "/hindi-author-1/book-title-1" );
		hiPratilipi_1_Page.setPrimaryContentId( hiPratilipi_1.getId() );

		hiPratilipi_2_Page.setType( PageType.PRATILIPI );
		hiPratilipi_2_Page.setUri( PageType.PRATILIPI.getUrlPrefix() + hiPratilipi_2.getId() );
		hiPratilipi_2_Page.setUriAlias( "/hindi-author-1/book-title-2" );
		hiPratilipi_2_Page.setPrimaryContentId( hiPratilipi_2.getId() );

		hiPratilipi_3_Page.setType( PageType.PRATILIPI );
		hiPratilipi_3_Page.setUri( PageType.PRATILIPI.getUrlPrefix() + hiPratilipi_3.getId() );
		hiPratilipi_3_Page.setUriAlias( "/hindi-author-1/book-title-3" );
		hiPratilipi_3_Page.setPrimaryContentId( hiPratilipi_3.getId() );

		guPratilipi_1_Page.setType( PageType.PRATILIPI );
		guPratilipi_1_Page.setUri( PageType.PRATILIPI.getUrlPrefix() + guPratilipi_1.getId() );
		guPratilipi_1_Page.setUriAlias( "/gujarati-author-1/book-title-1" );
		guPratilipi_1_Page.setPrimaryContentId( guPratilipi_1.getId() );

		guPratilipi_2_Page.setType( PageType.PRATILIPI );
		guPratilipi_2_Page.setUri( PageType.PRATILIPI.getUrlPrefix() + guPratilipi_2.getId() );
		guPratilipi_2_Page.setUriAlias( "/gujarati-author-1/book-title-2" );
		guPratilipi_2_Page.setPrimaryContentId( guPratilipi_2.getId() );

		guPratilipi_3_Page.setType( PageType.PRATILIPI );
		guPratilipi_3_Page.setUri( PageType.PRATILIPI.getUrlPrefix() + guPratilipi_3.getId() );
		guPratilipi_3_Page.setUriAlias( "/gujarati-author-1/book-title-3" );
		guPratilipi_3_Page.setPrimaryContentId( guPratilipi_3.getId() );

		taPratilipi_1_Page.setType( PageType.PRATILIPI );
		taPratilipi_1_Page.setUri( PageType.PRATILIPI.getUrlPrefix() + taPratilipi_1.getId() );
		taPratilipi_1_Page.setUriAlias( "/tamil-author-1/book-title-1" );
		taPratilipi_1_Page.setPrimaryContentId( taPratilipi_1.getId() );
	
		taPratilipi_2_Page.setType( PageType.PRATILIPI );
		taPratilipi_2_Page.setUri( PageType.PRATILIPI.getUrlPrefix() + taPratilipi_2.getId() );
		taPratilipi_2_Page.setUriAlias( "/tamil-author-1/book-title-2" );
		taPratilipi_2_Page.setPrimaryContentId( taPratilipi_2.getId() );

		taPratilipi_3_Page.setType( PageType.PRATILIPI );
		taPratilipi_3_Page.setUri( PageType.PRATILIPI.getUrlPrefix() + taPratilipi_3.getId() );
		taPratilipi_3_Page.setUriAlias( "/tamil-author-1/book-title-3" );
		taPratilipi_3_Page.setPrimaryContentId( taPratilipi_3.getId() );
		
		
		hiPratilipi_1_ReadPage.setType( PageType.READ );
		hiPratilipi_1_ReadPage.setUri( PageType.READ.getUrlPrefix() + hiPratilipi_1.getId() );
		hiPratilipi_1_ReadPage.setUriAlias( "/hindi-author-1/book-title-1/read" );
		hiPratilipi_1_ReadPage.setPrimaryContentId( hiPratilipi_1.getId() );

		hiPratilipi_2_ReadPage.setType( PageType.READ );
		hiPratilipi_2_ReadPage.setUri( PageType.READ.getUrlPrefix() + hiPratilipi_2.getId() );
		hiPratilipi_2_ReadPage.setUriAlias( "/hindi-author-1/book-title-2/read" );
		hiPratilipi_2_ReadPage.setPrimaryContentId( hiPratilipi_2.getId() );

		hiPratilipi_3_ReadPage.setType( PageType.READ );
		hiPratilipi_3_ReadPage.setUri( PageType.READ.getUrlPrefix() + hiPratilipi_3.getId() );
		hiPratilipi_3_ReadPage.setUriAlias( "/hindi-author-1/book-title-3/read" );
		hiPratilipi_3_ReadPage.setPrimaryContentId( hiPratilipi_3.getId() );

		guPratilipi_1_ReadPage.setType( PageType.READ );
		guPratilipi_1_ReadPage.setUri( PageType.READ.getUrlPrefix() + guPratilipi_1.getId() );
		guPratilipi_1_ReadPage.setUriAlias( "/gujarati-author-1/book-title-1/read" );
		guPratilipi_1_ReadPage.setPrimaryContentId( guPratilipi_1.getId() );

		guPratilipi_2_ReadPage.setType( PageType.READ );
		guPratilipi_2_ReadPage.setUri( PageType.READ.getUrlPrefix() + guPratilipi_2.getId() );
		guPratilipi_2_ReadPage.setUriAlias( "/gujarati-author-1/book-title-2/read" );
		guPratilipi_2_ReadPage.setPrimaryContentId( guPratilipi_2.getId() );

		guPratilipi_3_ReadPage.setType( PageType.READ );
		guPratilipi_3_ReadPage.setUri( PageType.READ.getUrlPrefix() + guPratilipi_3.getId() );
		guPratilipi_3_ReadPage.setUriAlias( "/gujarati-author-1/book-title-3/read" );
		guPratilipi_3_ReadPage.setPrimaryContentId( guPratilipi_3.getId() );

		taPratilipi_1_ReadPage.setType( PageType.READ );
		taPratilipi_1_ReadPage.setUri( PageType.READ.getUrlPrefix() + taPratilipi_1.getId() );
		taPratilipi_1_ReadPage.setUriAlias( "/tamil-author-1/book-title-1/read" );
		taPratilipi_1_ReadPage.setPrimaryContentId( taPratilipi_1.getId() );
	
		taPratilipi_2_ReadPage.setType( PageType.READ );
		taPratilipi_2_ReadPage.setUri( PageType.READ.getUrlPrefix() + taPratilipi_2.getId() );
		taPratilipi_2_ReadPage.setUriAlias( "/tamil-author-1/book-title-2/read" );
		taPratilipi_2_ReadPage.setPrimaryContentId( taPratilipi_2.getId() );

		taPratilipi_3_ReadPage.setType( PageType.READ );
		taPratilipi_3_ReadPage.setUri( PageType.READ.getUrlPrefix() + taPratilipi_3.getId() );
		taPratilipi_3_ReadPage.setUriAlias( "/tamil-author-1/book-title-3/read" );
		taPratilipi_3_ReadPage.setPrimaryContentId( taPratilipi_3.getId() );
		
	}
	
}