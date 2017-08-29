package com.pratilipi.data.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.pratilipi.data.type.Blog;
import com.pratilipi.data.type.gae.BlogEntity;

public class BlogMock {

	public static final List<Blog> BLOG_TABLE = new LinkedList<>();

	public static final Blog blog = new BlogEntity( 1L );

	static {

		BLOG_TABLE.add( blog );

		blog.setCreationDate( new Date( 1 ) );
		blog.setLastUpdated( new Date() );
		blog.setTitle( "Blog" );

	}

}