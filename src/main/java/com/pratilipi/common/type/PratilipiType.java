package com.pratilipi.common.type;

public enum PratilipiType {

	BOOK	( "Book", "Books" ),
	POEM	( "Poem", "Poems" ),
	STORY	( "Story", "Stories" ),
	ARTICLE	( "Article", "Articles" ),
	MAGAZINE( "Magazine", "Magazines" ),
	;
	
	
	private final String name;
	private final String namePlural;
	
	
	private PratilipiType( String name, String namePlural ) {
		this.name = name;
		this.namePlural = namePlural;
	}
	
	
	public String getStringId() {
		return "_pratilipi_type_" + name.toLowerCase();
	}
	
	public String getPluralStringId() {
		return "_pratilipi_type_" + namePlural.toLowerCase();
	}

	
	@Deprecated
	public String getName() {
		return name;
	}
	
	@Deprecated
	public String getNamePlural() {
		return namePlural;
	}
	
}