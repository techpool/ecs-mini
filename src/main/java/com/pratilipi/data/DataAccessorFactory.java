package com.pratilipi.data;

import java.util.Arrays;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.util.GoogleApi;
import com.pratilipi.common.util.SystemProperty;

public class DataAccessorFactory {

	private static final String datasource = SystemProperty.DATASOURCE;
	private static final String indexName = "GLOBAL_INDEX";
	private static final String gcsBucket = SystemProperty.BLOBSERVICE_GCS_BUCKET;
	private static final String gcsBucketBackup = SystemProperty.BLOBSERVICE_GCS_BUCKET_BACKUP;


	private static final Memcache cacheL1 = new MemcacheImpl();
	private static final Memcache cacheL2 = new MemcacheGaeImpl();
	
	private static final ThreadLocal<DataAccessor> threadLocalDataAccessor = new ThreadLocal<>();
	private static final SearchAccessor searchAccessor = datasource.equals( "gae" )	 
														? new SearchAccessorGaeImpl( indexName ) 
														: new SearchAccessorMockImpl( indexName );
	private static final ThreadLocal<BlobAccessor> threadLocalBlobAccessor = new ThreadLocal<>();
	private static final BlobAccessor blobAccessorBackup = new BlobAccessorGcsImpl2( gcsBucketBackup );
	private static final ThreadLocal<DocAccessor> threadLocalDocAccessor = new ThreadLocal<>();

	
	public static Memcache getL1CacheAccessor() {
		return cacheL1;
	}
	
	public static Memcache getL2CacheAccessor() {
		return cacheL2;
	}
	
	public static DataAccessor getDataAccessor() {
		DataAccessor dataAccessor = threadLocalDataAccessor.get();
		if( dataAccessor == null ) {
			dataAccessor = datasource.equals( "gae" )
					? new DataAccessorGaeImpl( new MemcacheWrapper( cacheL1, cacheL2 ) )
					: new DataAccessorMockImpl();
			threadLocalDataAccessor.set( dataAccessor );
		}
		return dataAccessor;
	}

	public static RtdbAccessor getRtdbAccessor() throws UnexpectedServerException {
		String googleApiAccessToken = GoogleApi.getAccessToken( Arrays.asList(
				"https://www.googleapis.com/auth/firebase.database",
				"https://www.googleapis.com/auth/userinfo.email" ) );
		return new RtdbAccessorWithMemcache(
				new RtdbAccessorFirebaseImpl( googleApiAccessToken ),
				new MemcacheWrapper( cacheL1, cacheL2 ) );
	}
	
	public static SearchAccessor getSearchAccessor() {
		return searchAccessor;
	}

	public static BlobAccessor getBlobAccessor() {
		BlobAccessor blobAccessor = threadLocalBlobAccessor.get();
		if( blobAccessor == null ) {
			blobAccessor = new BlobAccessorWithMemcache(
					new BlobAccessorGcsImpl2( gcsBucket ), 
					new MemcacheWrapper( cacheL1, cacheL2 ) );
			threadLocalBlobAccessor.set( blobAccessor );
		}
		return blobAccessor;
	}

	public static BlobAccessor getBlobAccessorBackup() {
		return blobAccessorBackup;
	}

	public static DocAccessor getDocAccessor() {
		DocAccessor docAccessor = threadLocalDocAccessor.get();
		if( docAccessor == null ) {
			docAccessor = new DocAccessorImpl( getBlobAccessor() );
			threadLocalDocAccessor.set( docAccessor );
		}
		return docAccessor;
	}
	
}
