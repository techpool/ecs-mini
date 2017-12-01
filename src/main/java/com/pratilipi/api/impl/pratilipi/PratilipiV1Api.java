package com.pratilipi.api.impl.pratilipi;

import java.util.List;

import com.google.gson.Gson;
import com.pratilipi.api.GenericApi;
import com.pratilipi.api.annotation.Bind;
import com.pratilipi.api.annotation.Get;
import com.pratilipi.api.annotation.Post;
import com.pratilipi.api.annotation.Validate;
import com.pratilipi.api.impl.author.AuthorApi;
import com.pratilipi.api.impl.init.InitV1Api;
import com.pratilipi.api.impl.init.InitV2Api;
import com.pratilipi.api.shared.GenericRequest;
import com.pratilipi.api.shared.GenericResponse;
import com.pratilipi.common.exception.InsufficientAccessException;
import com.pratilipi.common.exception.InvalidArgumentException;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.common.type.PratilipiContentType;
import com.pratilipi.common.type.PratilipiState;
import com.pratilipi.common.type.PratilipiType;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.client.PratilipiData;
import com.pratilipi.data.client.TagData;
import com.pratilipi.data.type.Author;
import com.pratilipi.data.type.Pratilipi;
import com.pratilipi.data.type.PratilipiContentDoc;
import com.pratilipi.data.util.PratilipiDataUtil;
import com.pratilipi.filter.AccessTokenFilter;
import com.pratilipi.filter.UxModeFilter;
import com.pratilipi.taskqueue.Task;
import com.pratilipi.taskqueue.TaskQueueFactory;

@SuppressWarnings("serial")
@Bind( uri = "/pratilipi", ver = "1" )
public class PratilipiV1Api extends GenericApi {
	
	public static class GetRequest extends GenericRequest {

		@Validate( required = true, minLong = 1L )
		protected Long pratilipiId;

		public void setPratilipiId( Long pratilipiId ) {
			this.pratilipiId = pratilipiId;
		}
	}


	public static class GetFromSlugRequest extends GenericRequest {

		protected String slug;
		public void setSlug(String slug) {
			this.slug = slug;
		}
	}
	
	@SuppressWarnings("unused")
	public static class PostRequest extends GenericRequest {

		@Validate( minLong = 1L )
		private Long pratilipiId;
		
		private String title;
		private boolean hasTitle;
		
		private String titleEn;
		private boolean hasTitleEn;
		
		private Language language;
		private boolean hasLanguage;
		
		private Long authorId;
		private boolean hasAuthorId;
		
		@Deprecated
		private String summary;
		@Deprecated
		private boolean hasSummary;
		
		private Integer publicationYear;
		private boolean hasPublicationYear;
		
		
		private PratilipiType type;
		private boolean hasType;

		private PratilipiContentType contentType;
		private boolean hasContentType;

		private PratilipiState state;
		private boolean hasState;

		private Boolean oldContent;
		private boolean hasOldContent;

	}
	
	public static class Response extends GenericResponse {
		
		private Long pratilipiId;
		
		private String title;
		private String titleEn;
		private String displayTitle;
		private Language language;
		private AuthorApi.Response author;
		@Deprecated
		private String summary;
		
		private String pageUrl;
		private String coverImageUrl;
		private String readPageUrl;
		private String writePageUrl;
		private Boolean oldContent;

		private PratilipiType type;
		private PratilipiContentType contentType;
		private PratilipiState state;
		
		private Long listingDateMillis;
		private Long lastUpdatedMillis;
		
		private Object index;
		private Integer pageCount;
		
		private Integer wordCount;
		private Long reviewCount;
		private Long ratingCount;
		private Float averageRating;
		private Long readCount;
		private Long fbLikeShareCount;
		
		private Boolean addedToLib;
		private Boolean hasAccessToUpdate;
		
		private List<TagData> tags;
		private List<String> suggestedTags;
		
		
		@SuppressWarnings("unused")
		protected Response() { }
		
		// TODO: change this to package level access ASAP
		public Response( PratilipiData pratilipiData ) {
			
			this.pratilipiId = pratilipiData.getId();
			
			this.title = pratilipiData.getTitle();
			this.titleEn = pratilipiData.getTitleEn();
			this.language = pratilipiData.getLanguage();
			this.author = new AuthorApi.Response( pratilipiData.getAuthor(), PratilipiV1Api.class );
			this.summary = pratilipiData.getSummary();
			
			this.pageUrl = pratilipiData.getPageUrl();
			this.coverImageUrl = pratilipiData.getCoverImageUrl();
			this.readPageUrl = pratilipiData.getReadPageUrl();
			this.writePageUrl = pratilipiData.getWritePageUrl();
			this.oldContent = pratilipiData.isOldContent();
			this.contentType = pratilipiData.getContentType();

			this.type = pratilipiData.getType();
			this.state = pratilipiData.getState();
			
			this.listingDateMillis = pratilipiData.getListingDate().getTime();
			if( ! UxModeFilter.isAndroidApp() ) {
				this.index = pratilipiData.getIndex();
				this.pageCount = pratilipiData.getPageCount();
			}

			if( pratilipiData.getLastUpdated() != null )
				this.lastUpdatedMillis = pratilipiData.getLastUpdated().getTime();

			this.wordCount = pratilipiData.getWordCount();
			this.reviewCount = pratilipiData.getReviewCount();
			this.ratingCount = pratilipiData.getRatingCount();
			this.averageRating = pratilipiData.getAverageRating();
			this.readCount = pratilipiData.getReadCount();
			this.fbLikeShareCount = pratilipiData.getFbLikeShareCount();
			
			this.hasAccessToUpdate = pratilipiData.hasAccessToUpdate();
			
			this.tags = pratilipiData.getTags();
			this.suggestedTags = pratilipiData.getSuggestedTags();
		}

		public Response( PratilipiData pratilipi, Class<? extends GenericApi> clazz ) {
			
			if( clazz == InitV1Api.class || clazz == InitV2Api.class
					|| clazz == PratilipiListV1Api.class || clazz == PratilipiListV2Api.class ) {
				
				this.pratilipiId = pratilipi.getId();
				this.title = pratilipi.getTitle() == null ? pratilipi.getTitleEn() : pratilipi.getTitle();
//				if( UxModeFilter.isAndroidApp() )	language information is required for categories.
					this.language = pratilipi.getLanguage();
				if( pratilipi.getAuthor() != null )
					this.author = new AuthorApi.Response( pratilipi.getAuthor(), PratilipiListV1Api.class );

				this.summary = pratilipi.getSummary();
				this.pageUrl = pratilipi.getPageUrl();
				this.coverImageUrl = pratilipi.getCoverImageUrl();
				this.readPageUrl = pratilipi.getReadPageUrl();
				this.writePageUrl = pratilipi.getWritePageUrl();
				if( UxModeFilter.isAndroidApp() )
					this.contentType = pratilipi.getContentType();
				if( UxModeFilter.isAndroidApp() )
					this.listingDateMillis = pratilipi.getListingDate().getTime();
				this.wordCount = pratilipi.getWordCount();
				this.ratingCount = pratilipi.getRatingCount();
				this.averageRating = pratilipi.getAverageRating();
				this.readCount = pratilipi.getReadCount();

				this.addedToLib = pratilipi.isAddedToLib();
				this.hasAccessToUpdate = pratilipi.hasAccessToUpdate();

				this.tags = pratilipi.getTags();
				this.suggestedTags = pratilipi.getSuggestedTags();
				
				/* Required for Add category and Edit category button on author profile page. */
				this.type = pratilipi.getType();

			} else if( clazz == PratilipiListV3Api.class ) {

				this.pratilipiId = pratilipi.getId();
				this.title = pratilipi.getTitle();
				this.titleEn = pratilipi.getTitleEn();
				this.displayTitle = pratilipi.getTitle() != null ? pratilipi.getTitle() : pratilipi.getTitleEn();
				this.language = pratilipi.getLanguage();
				if( pratilipi.getAuthor() != null )
					this.author = new AuthorApi.Response( pratilipi.getAuthor(), PratilipiListV3Api.class );
				if( UxModeFilter.isAndroidApp() )
					this.summary = pratilipi.getSummary();
				this.pageUrl = pratilipi.getPageUrl();
				this.coverImageUrl = pratilipi.getCoverImageUrl();
				this.readPageUrl = pratilipi.getReadPageUrl();
				this.writePageUrl = pratilipi.getWritePageUrl();
				if( UxModeFilter.isAndroidApp() )
					this.contentType = pratilipi.getContentType();
				if( UxModeFilter.isAndroidApp() )
					this.listingDateMillis = pratilipi.getListingDate().getTime();
				this.wordCount = pratilipi.getWordCount();
				this.ratingCount = pratilipi.getRatingCount();
				this.averageRating = pratilipi.getAverageRating();
				this.readCount = pratilipi.getReadCount();

				this.addedToLib = pratilipi.isAddedToLib();
				this.hasAccessToUpdate = pratilipi.hasAccessToUpdate();

				this.tags = pratilipi.getTags();
				this.suggestedTags = pratilipi.getSuggestedTags();

				/* Required for Add category and Edit category button on author profile page. */
				this.type = pratilipi.getType();

			}

		}
		
		@Deprecated
		public Response( PratilipiData pratilipi, boolean listItem ) {
			
			this.pratilipiId = pratilipi.getId();
			this.title = pratilipi.getTitle() == null ? pratilipi.getTitleEn() : pratilipi.getTitle();
			if( UxModeFilter.isAndroidApp() )
				this.language = pratilipi.getLanguage();
			if( pratilipi.getAuthor() != null )
				this.author = new AuthorApi.Response( pratilipi.getAuthor(), PratilipiListV1Api.class );
			if( UxModeFilter.isAndroidApp() )
				this.summary = pratilipi.getSummary();
			this.pageUrl = pratilipi.getPageUrl();
			this.coverImageUrl = pratilipi.getCoverImageUrl();
			this.readPageUrl = pratilipi.getReadPageUrl();
			this.writePageUrl = pratilipi.getWritePageUrl();
			if( UxModeFilter.isAndroidApp() )
				this.contentType = pratilipi.getContentType();
			this.ratingCount = pratilipi.getRatingCount();
			this.averageRating = pratilipi.getAverageRating();
			this.readCount = pratilipi.getReadCount();
			this.addedToLib = pratilipi.isAddedToLib();
			this.hasAccessToUpdate = pratilipi.hasAccessToUpdate();
			
			this.tags = pratilipi.getTags();
			this.suggestedTags = pratilipi.getSuggestedTags();
		}
		
		
		public Long getId() {
			return pratilipiId;
		}
		
		
		public String getTitle() {
			return title;
		}
		
		public String getTitleEn() {
			return titleEn;
		}

		public String getDisplayTitle() {
			return displayTitle;
		}

		public Language getLanguage() {
			return language;
		}
		
		public AuthorApi.Response getAuthor() {
			return author;
		}

		public String getSummary() {
			return summary;
		}

		
		public String getPageUrl() {
			return pageUrl;
		}
		
		public String getCoverImageUrl() {
			return coverImageUrl;
		}
		
		public String getCoverImageUrl( int width ) {
			return coverImageUrl.indexOf( '?' ) == -1
					? coverImageUrl + "?width=" + width
					: coverImageUrl + "&width=" + width;
		}

		public String getReadPageUrl() {
			return readPageUrl;
		}

		public String getWritePageUrl() {
			return writePageUrl;
		}

		public Boolean isOldContent() {
			return oldContent != null ? oldContent : true;
		}


		public PratilipiType getType() {
			return type;
		}

		public PratilipiContentType getContentType() {
			return contentType;
		}
		
		public PratilipiState getState() {
			return state;
		}

		
		public Long getListingDateMillis() {
			return listingDateMillis;
		}

		public Long getLastUpdatedMillis() {
			return lastUpdatedMillis;
		}


		public Object getIndex() {
			return index;
		}

		public Integer getPageCount() {
			return pageCount;
		}

		
		public Integer getWordCount() {
			return wordCount;
		}

		public Long getReviewCount() {
			return reviewCount;
		}

		public Long getRatingCount() {
			return ratingCount;
		}
		
		public Float getAverageRating() {
			return averageRating;
		}

		public Long getReadCount() {
			return readCount;
		}

		public Long getFbLikeShareCount() {
			return fbLikeShareCount;
		}

		
		public boolean isAddedToLib() {
			return addedToLib;
		}
		
		public Boolean hasAccessToUpdate() {
			return hasAccessToUpdate;
		}
		
		public List<TagData> getTags() {
			return tags;
		}
		
		public List<String> getSuggestedTags() {
			return suggestedTags;
		}
	}
	
	
	@Get
	public Response get( GetRequest request ) throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Pratilipi pratilipi = dataAccessor.getPratilipi( request.pratilipiId );
		Author author = pratilipi.getAuthorId() == null
				? null
				: dataAccessor.getAuthor( pratilipi.getAuthorId() );
		
		PratilipiData pratilipiData = PratilipiDataUtil.createPratilipiData( pratilipi, author );

		if( UxModeFilter.isAndroidApp() ) {
			DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
			PratilipiContentDoc pcDoc = docAccessor.getPratilipiContentDoc( request.pratilipiId );
			pratilipiData.setIndex( pcDoc == null ? null : pcDoc.getIndex() );
		}

		return new Response( pratilipiData );
		
	}

	@Post
	public Response post( PostRequest request )
			throws InvalidArgumentException, InsufficientAccessException, UnexpectedServerException {

		Gson gson = new Gson();

		// Creating PratilipiData object.
		PratilipiData pratilipiData = gson.fromJson( gson.toJson( request ), PratilipiData.class );

		// If not set already, setting AuthorId for new content.
		if( pratilipiData.getId() == null && ! pratilipiData.hasAuthorId() ) {
			DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
			Author author = dataAccessor.getAuthorByUserId( AccessTokenFilter.getAccessToken().getUserId() );
			pratilipiData.setAuthorId( author.getId() );
		}

		// Saving PratilipiData object.
		pratilipiData = PratilipiDataUtil.savePratilipiData( pratilipiData );

		// Creating PratilipiProcess task to process the data.
		Task task = TaskQueueFactory.newTask()
				.setUrl( "/pratilipi/process" )
				.addParam( "pratilipiId", pratilipiData.getId().toString() )
				.addParam( "processData", "true" );
		TaskQueueFactory.getPratilipiTaskQueue().add( task );

		// If PratilipiState has changed, creating AuthorProcess task to update Author stats.
		if( request.hasState && pratilipiData.getAuthorId() != null ) {
			Task authorTask = TaskQueueFactory.newTask()
					.setUrl( "/author/process" )
					.addParam( "authorId", pratilipiData.getAuthorId().toString() )
					.addParam( "updateStats", "true" );
			TaskQueueFactory.getAuthorTaskQueue().add( authorTask );
		}
		
		return new Response( pratilipiData );
		
	}		

}
