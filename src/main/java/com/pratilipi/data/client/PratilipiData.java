package com.pratilipi.data.client;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiContentType;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.PratilipiType;

public class PratilipiData implements Serializable {

	private static final long serialVersionUID = -155635027192257511L;

	
	private Long pratilipiId;
	
	private String title;
	private boolean hasTitle;
	
	private String titleEn;
	private boolean hasTitleEn;
	
	private Language language;
	private boolean hasLanguage;
	
	private Long authorId;
	private boolean hasAuthorId;
	
	private AuthorData author;

	@Deprecated
	private String summary;
	@Deprecated
	private boolean hasSummary;
	
	
	private String pageUrl;
	private String coverImageUrl;
	private String readPageUrl;
	private String writePageUrl;

	
	private PratilipiType type;
	private boolean hasType;

	private PratilipiContentType contentType;
	private boolean hasContentType;

	private PratilipiState state;
	private boolean hasState;
	
	private Long listingDateMillis;
	private Long lastUpdatedMillis;


	@Deprecated
	private Object index;
	@Deprecated
	private Integer pageCount;

	private Integer wordCount;
	private Long reviewCount;
	private Long ratingCount;
	private Float averageRating;
	private Double relevance;
	private Long readCount;
	private Long fbLikeShareCount;
	
	
	private Boolean addedToLib;
	private Boolean hasAccessToUpdate;

	private Boolean oldContent;
	private boolean hasOldContent;

	private List<TagData> tags;
	private boolean hasTags;
	
	private List<String> suggestedTags;
	private boolean hasSuggestedTags;
	
	public PratilipiData() {}
	
	public PratilipiData( Long id ) {
		this.pratilipiId = id;
	}
	

	public Long getId() {
		return pratilipiId;
	}

	public void setId( Long id ) {
		this.pratilipiId = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
		this.hasTitle = true;
	}
	
	public boolean hasTitle() {
		return hasTitle;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn( String titleEn ) {
		this.titleEn = titleEn;
		this.hasTitleEn = true;
	}
	
	public boolean hasTitleEn() {
		return hasTitleEn;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage( Language language ) {
		this.language = language;
		this.hasLanguage = true;
	}

	public boolean hasLanguage() {
		return hasLanguage;
	}
	
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId( Long authorId ) {
		this.authorId = authorId;
		this.hasAuthorId = true;
	}

	public boolean hasAuthorId() {
		return hasAuthorId;
	}
	
	public AuthorData getAuthor() {
		return author;
	}

	public void setAuthor( AuthorData author ) {
		this.author = author;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary( String summary ) {
		this.summary = summary;
		this.hasSummary = true;
	}

	public boolean hasSummary() {
		return hasSummary;
	}
	
	
	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl( String pageUrl ) {
		this.pageUrl = pageUrl;
	}

	public String getCoverImageUrl() {
		return coverImageUrl;
	}

	public String getCoverImageUrl( int width ) {
		return coverImageUrl.indexOf( '?' ) == -1
				? coverImageUrl + "?width=" + width
				: coverImageUrl + "&width=" + width;
	}

	public void setCoverImageUrl( String coverImageUrl ) {
		this.coverImageUrl = coverImageUrl;
	}

	public String getReadPageUrl() {
		return readPageUrl;
	}

	public void setReadPageUrl( String readPageUrl ) {
		this.readPageUrl = readPageUrl;
	}
	
	public String getWritePageUrl(){
		return this.writePageUrl;
	}
	
	public void setWritePageUrl( String writePageUrl ){
		this.writePageUrl = writePageUrl;
	}


	public PratilipiType getType() {
		return this.type;
	}
	
	public void setType( PratilipiType type ) {
		this.type = type;
		this.hasType = true;
	}
	
	public boolean hasType() {
		return hasType;
	}
	
	public PratilipiContentType getContentType() {
		return this.contentType;
	}
	
	public void setContentType( PratilipiContentType contentType ) {
		this.contentType = contentType;
		this.hasContentType = true;
	}
	
	public boolean hasContentType() {
		return hasContentType;
	}
	
	public PratilipiState getState() {
		return state;
	}
	
	public void setState( PratilipiState state ) {
		this.state = state;
		this.hasState = true;
	}
	
	public boolean hasState() {
		return hasState;
	}
	
	public Date getListingDate() {
		return listingDateMillis == null ? null : new Date( this.listingDateMillis );
	}

	public void setListingDate( Date listingDate ) {
		this.listingDateMillis = listingDate == null ? null : listingDate.getTime();
	}

	public Date getLastUpdated() {
		return this.lastUpdatedMillis == null ? null : new Date( this.lastUpdatedMillis );
	}
	
	public void setLastUpdated( Date lastUpdated ){
		this.lastUpdatedMillis = lastUpdated == null ? null : lastUpdated.getTime();
	}


	public Object getIndex() {
		return index;
	}
	
	public void setIndex( Object index ) {
		this.index = index;
	}


	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount( Integer pageCount ) {
		this.pageCount = pageCount;
	}


	public Integer getWordCount() {
		return wordCount;
	}
	
	public void setWordCount( Integer wordCount ) {
		this.wordCount = wordCount;
	}

	public Long getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Long reviewCount) {
		this.reviewCount = reviewCount;
	}

	public Long getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Long ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating( Float averageRating ) {
		this.averageRating = averageRating;
	}
	
	public Double getRelevance() {
		return relevance;
	}
	
	public void setRelevance( Double relevance ) {
		this.relevance = relevance;
	}
	
	
	public Long getReadCount() {
		return readCount;
	}

	public void setReadCount( Long readCount ) {
		this.readCount = readCount;
	}
	
	public Long getFbLikeShareCount() {
		return fbLikeShareCount;
	}

	public void setFbLikeShareCount( Long fbLikeShareCount ) {
		this.fbLikeShareCount = fbLikeShareCount;
	}
	

	public boolean isAddedToLib() {
		return addedToLib == null ? false : addedToLib;
	}
	
	public void setAddedToLib( Boolean addedToLib ) {
		this.addedToLib = addedToLib;
	}
	
	public boolean hasAccessToUpdate() {
		return hasAccessToUpdate == null ? false : hasAccessToUpdate;
	}
	
	public void setAccessToUpdate( Boolean hasAccessToUpdate ) {
		this.hasAccessToUpdate = hasAccessToUpdate;
	}
	
	public boolean hasOldContentFlag() {
		return hasOldContent;
	} 

	public boolean isOldContent() {
		return oldContent;
	}

	public void setOldContent( Boolean oldContent ) {
		this.oldContent = oldContent;
		this.hasOldContent = true;
	}
	
	public List<TagData> getTags() {
		return this.tags;
	}
	
	public void setTags(List<TagData> tags) {
		this.tags = tags;
		this.hasTags = true;
	}
	
	public boolean hasTags() {
		return hasTags;
	}
	
	public List<String> getSuggestedTags() {
		return this.suggestedTags;
	}
	
	public void setSuggestedTags(List<String> suggestedTags) {
		this.suggestedTags = suggestedTags;
		this.hasSuggestedTags = true;
	}
	
	public boolean hasSuggestedTags() {
		return hasSuggestedTags;
	}

}
