package com.pratilipi.data.client;

import java.io.Serializable;

import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiType;

public class TagData  implements Serializable {
	
	private Long id;
	
	private String name;
	private boolean hasName;
	
	private String nameEn;
	private boolean hasNameEn;
	
	private Language language;
	private boolean hasLanguage;
	
	private PratilipiType type;
	private boolean hasType;

	private String pageUrl;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.hasName = true;
	}
	
	public boolean hasName() {
		return this.hasName;
	}
	
	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
		this.hasNameEn = true;
	}
	
	public boolean hasNameEn() {
		return this.hasNameEn;
	}
	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
		this.hasLanguage = true;
	}

	public boolean hasLanguage() {
		return this.hasLanguage;
	}
	
	public PratilipiType getPratilipiType() {
		return type;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPratilipiType(PratilipiType pratilipiType) {
		this.type = pratilipiType;
		this.hasType = true;
	}

	public boolean hasType() {
		return this.hasType;
	}
}
