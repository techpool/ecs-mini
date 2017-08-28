package com.pratilipi.data.type;

import java.util.Date;
import java.util.List;

import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiContentType;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.PratilipiType;

public interface Pratilipi extends GenericOfyType {

	Long getId();
	
	String getTitle();
	
	void setTitle( String title );
	
	String getTitleEn();
	
	void setTitleEn( String titleEn );
	
	Language getLanguage();
	
	void setLanguage( Language language );
	
	Long getAuthorId();
	
	void setAuthorId( Long authorId );
	
	@Deprecated
	String getSummary();
	
	@Deprecated
	void setSummary( String summary );


	PratilipiType getType();
	
	void setType( PratilipiType pratilipiType );
	
	PratilipiContentType getContentType();
	
	void setContentType( PratilipiContentType contentType );

	PratilipiState getState();
	
	void setState( PratilipiState state );
	
	String getCoverImage();

	void setCoverImage( String coverImage );

	Date getListingDate();
	
	void setListingDate( Date listingDate );

	Date getLastUpdated();
	
	void setLastUpdated( Date lastUpdated );


	@Deprecated
	String getIndex();
	
	@Deprecated
	void setIndex( String index );
	
	Integer getWordCount();

	void setWordCount( Integer wordCount );

	Integer getImageCount();

	void setImageCount( Integer imageCount );
	
	Integer getPageCount();
	
	void setPageCount( Integer pageCount );
	
	Integer getChapterCount();

	void setChapterCount( Integer chapterCount );

	
	Long getReviewCount();
	
	void setReviewCount( Long reviewCount );
	
	Long getRatingCount();
	
	void setRatingCount( Long ratingCount );
	
	Long getTotalRating();
	
	void setTotalRating( Long totalRating );

	
	Long getReadCountOffset();

	void setReadCountOffset( Long readCountOffset );
		
	Long getReadCount();

	void setReadCount( Long readCount );
	
	Long getFbLikeShareCountOffset();

	void setFbLikeShareCountOffset( Long fbLikeShareCountOffset );
	
	Long getFbLikeShareCount();

	void setFbLikeShareCount( Long fbLikeShareCount );


	List<Long> getTagIds();

	void setTagIds( List<Long> tagIds );

	List<String> getSuggestedTags();

	void setSuggestedTags( List<String> suggestedTags );


	Boolean isOldContent();

	void setOldContent( Boolean oldContent );

	
	Date getTimestamp();
	
	void setTimestamp( Date timestamp );

}