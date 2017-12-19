package com.pratilipi.data.client;

import java.io.Serializable;
import java.util.List;

public class CategoryData implements Serializable {

	String name;

	String nameEn;

	List<TagData> categories;

	public CategoryData() {}

	public CategoryData( String name, String nameEn, List<TagData> categories ) {
		this.name = name;
		this.nameEn = nameEn;
		this.categories = categories;
	}

	public String getName() {
		return name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public List<TagData> getTagList() {
		return categories;
	}

}