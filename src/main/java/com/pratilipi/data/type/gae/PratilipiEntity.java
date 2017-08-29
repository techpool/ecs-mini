package com.pratilipi.data.type.gae;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
import com.googlecode.objectify.condition.IfTrue;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiContentType;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.data.type.Pratilipi;

@Cache
@Entity( name = "PRATILIPI" )
public class PratilipiEntity implements Pratilipi {

	// TODO: Required for backward compatibility with Mark-4
	private static final long serialVersionUID = -3740558387788310210L;
	
	
	@Id
	private Long PRATILIPI_ID;
	
	private String TITLE;
	
	private String TITLE_EN;
	
	@Index
	private Language LANGUAGE;

	@Index
	private Long AUTHOR_ID;
	
	@Deprecated
	@Index( IfNotNull.class )
	private Long PUBLISHER_ID;

	@Deprecated
	private String SUMMARY;

	
	@Deprecated
	@Index( { IfNotNull.class, IfTrue.class } )
	private Boolean PUBLIC_DOMAIN;
	
	@Index
	private PratilipiType PRATILIPI_TYPE;
	
	@Index
	private PratilipiContentType CONTENT_TYPE;
	
	@Index
	private PratilipiState STATE;
	
	@Index
	private String COVER_IMAGE;
	
	@Index
	private Date LISTING_DATE;

	@Index
	private Date LAST_UPDATED;

	
	@Deprecated
	private String INDEX;
	
	@Index
	private Integer WORD_COUNT;
	
	@Index
	private Integer IMAGE_COUNT;
	
	@Index
	private Integer PAGE_COUNT;
	
	@Index
	private Integer CHAPTER_COUNT;

	
	@Index
	private Long REVIEW_COUNT;
	
	@Index
	private Long RATING_COUNT;
	
	@Index
	private Long TOTAL_RATING;
	

	@Index
	private Long READ_COUNT_OFFSET;
	
	@Index
	private Long READ_COUNT;
	
	@Index
	private Long FB_LIKE_SHARE_COUNT_OFFSET;
	
	@Index
	private Long FB_LIKE_SHARE_COUNT;


	@Index
	private List<Long> TAG_IDS;

	@Index
	private List<String> SUGGESTED_TAGS;


	private Boolean OLD_CONTENT;
	
	@Index
	private Date _TIMESTAMP_;



	public PratilipiEntity() {}
	
	public PratilipiEntity( Long id ) {
		this.PRATILIPI_ID = id;
	}

	
	@Override
	public Long getId() {
		return PRATILIPI_ID;
	}

	public void setId( Long id ) {
		this.PRATILIPI_ID = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.PRATILIPI_ID = key.getId();
	}

	
	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public void setTitle( String title ) {
		this.TITLE = title;
	}

	@Override
	public String getTitleEn() {
		return this.TITLE_EN;
	}

	@Override
	public void setTitleEn(String titleEn) {
		this.TITLE_EN = titleEn;
	}
	
	@Override
	public Language getLanguage() {
		return LANGUAGE;
	}

	@Override
	public void setLanguage( Language language ) {
		this.LANGUAGE = language;
	}

	@Override
	public Long getAuthorId() {
		return AUTHOR_ID;
	}

	@Override
	public void setAuthorId( Long authorId ) {
		this.AUTHOR_ID = authorId;
	}
	
	@Override
	public String getSummary() {
		return SUMMARY;
	}

	@Override
	public void setSummary( String summary ) {
		this.SUMMARY = summary;
	}
	
	
	@Override
	public PratilipiType getType() {
		return PRATILIPI_TYPE;
	}
	
	@Override
	public void setType( PratilipiType pratilipiType ) {
		this.PRATILIPI_TYPE = pratilipiType;
	}

	@Override
	public PratilipiContentType getContentType() {
		return CONTENT_TYPE;
	}
	
	@Override
	public void setContentType( PratilipiContentType contentType ) {
		this.CONTENT_TYPE = contentType;
	}
	
	@Override
	public PratilipiState getState() {
		return STATE;
	}
	
	@Override
	public void setState( PratilipiState state ) {
		this.STATE = state;
	}

	@Override
	public String getCoverImage() {
		return COVER_IMAGE;
	}

	@Override
	public void setCoverImage( String coverImage ) {
		this.COVER_IMAGE = coverImage;
	}
	
	@Override
	public Date getListingDate() {
		return LISTING_DATE;
	}

	@Override
	public void setListingDate( Date listingDate ) {
		this.LISTING_DATE = listingDate;
	}

	@Override
	public Date getLastUpdated() {
		return LAST_UPDATED;
	}

	@Override
	public void setLastUpdated( Date lastUpdated ) {
		this.LAST_UPDATED = lastUpdated;
	}

	
	@Override
	public String getIndex() {
		return INDEX;
	}

	@Override
	public void setIndex( String index ) {
		this.INDEX = index;
	}
	
	@Override
	public Integer getWordCount() {
		return WORD_COUNT == null ? 0 : WORD_COUNT;
	}

	@Override
	public void setWordCount( Integer wordCount ) {
		this.WORD_COUNT = wordCount;
	}
	
	@Override
	public Integer getImageCount() {
		return IMAGE_COUNT == null ? 0 : IMAGE_COUNT;
	}

	@Override
	public void setImageCount( Integer imageCount ) {
		this.IMAGE_COUNT = imageCount;
	}
	
	@Override
	public Integer getPageCount() {
		return PAGE_COUNT == null ? 0 : PAGE_COUNT;
	}

	@Override
	public void setPageCount( Integer pageCount ) {
		this.PAGE_COUNT = pageCount;
	}
	
	@Override
	public Integer getChapterCount() {
		return CHAPTER_COUNT == null ? 0 : CHAPTER_COUNT;
	}

	@Override
	public void setChapterCount( Integer chapterCount ) {
		this.CHAPTER_COUNT = chapterCount;
	}

	
	@Override
	public Long getReviewCount() {
		return REVIEW_COUNT == null ? 0 : REVIEW_COUNT;
	}

	@Override
	public void setReviewCount( Long reviewCount ) {
		this.REVIEW_COUNT = reviewCount;
	}

	@Override
	public Long getRatingCount() {
		return RATING_COUNT == null ? 0 : RATING_COUNT;
	}

	@Override
	public void setRatingCount( Long ratingCount ) {
		this.RATING_COUNT = ratingCount;
	}
	
	@Override
	public Long getTotalRating() {
		return TOTAL_RATING == null ? 0 : TOTAL_RATING;
	}

	@Override
	public void setTotalRating( Long totalRating ) {
		this.TOTAL_RATING = totalRating;
	}
	
	
	@Override
	public Long getReadCountOffset() {
		return READ_COUNT_OFFSET == null ? 0L : READ_COUNT_OFFSET;
	}

	@Override
	public void setReadCountOffset( Long readCountOffset ) {
		this.READ_COUNT_OFFSET = readCountOffset;
	}
	
	@Override
	public Long getReadCount() {
		return READ_COUNT == null ? 0L : READ_COUNT;
	}

	@Override
	public void setReadCount( Long readCount ) {
		this.READ_COUNT = readCount;
	}
	
	@Override
	public Long getFbLikeShareCountOffset() {
		return FB_LIKE_SHARE_COUNT_OFFSET == null ? 0L : FB_LIKE_SHARE_COUNT_OFFSET;
	}

	@Override
	public void setFbLikeShareCountOffset( Long fbLikeShareCountOffset ) {
		this.FB_LIKE_SHARE_COUNT_OFFSET = fbLikeShareCountOffset;
	}
	
	@Override
	public Long getFbLikeShareCount() {
		return FB_LIKE_SHARE_COUNT == null ? 0L : FB_LIKE_SHARE_COUNT;
	}

	@Override
	public void setFbLikeShareCount( Long fbLikeShareCount ) {
		this.FB_LIKE_SHARE_COUNT = fbLikeShareCount;
	}


	@Override
	public List<Long> getTagIds() {
		return TAG_IDS;
	}

	@Override
	public void setTagIds( List<Long> tagIds ) {
		this.TAG_IDS = tagIds;
	}

	@Override
	public List<String> getSuggestedTags() {
		return SUGGESTED_TAGS;
	}

	@Override
	public void setSuggestedTags( List<String> suggestedTags ) {
		this.SUGGESTED_TAGS = suggestedTags;
	}


	@Override
	public Boolean isOldContent() {
		return OLD_CONTENT == null ? true : OLD_CONTENT;
	}

	@Override
	public void setOldContent( Boolean oldContent ) {
		this.OLD_CONTENT = oldContent;
	}
	
	@Override
	public Date getTimestamp() {
		return _TIMESTAMP_;
	}

	@Override
	public void setTimestamp( Date timestamp ) {
		this._TIMESTAMP_ = timestamp;
	}	

}