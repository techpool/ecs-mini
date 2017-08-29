package com.pratilipi.common.util;

import com.google.apphosting.api.ApiProxy;

public class SystemProperty {

	public static final Long SYSTEM_USER_ID = 5636632866717696L;

	public static final String DATASOURCE;
	public static final String BLOBSERVICE_GCS_BUCKET;
	public static final String BLOBSERVICE_GCS_BUCKET_BACKUP;
	public static final String CDN;
	public static final String STAGE;
	public static final String VERSION_ID;
	public static final String FIREBASE_DB_URL;
	public static final String ECS_PAG_ENDPOINT;

	public static final String STAGE_ALPHA	= "alpha";
	public static final String STAGE_BETA	= "beta";
	public static final String STAGE_GAMMA	= "gamma";
	public static final String STAGE_PROD	= "prod";

	static {
		boolean isGae = ApiProxy.getCurrentEnvironment() != null;
		String appId = isGae ? ApiProxy.getCurrentEnvironment().getAppId() : null;
		String moduleId = isGae ? ApiProxy.getCurrentEnvironment().getModuleId() : null;
		String versionId = isGae ? ApiProxy.getCurrentEnvironment().getVersionId() : null;

		if( isGae && ( appId.equals( "prod-pratilipi" ) || appId.equals( "s~prod-pratilipi" ) ) ) {
			DATASOURCE = "gae";
			BLOBSERVICE_GCS_BUCKET = "static.pratilipi.com";
			BLOBSERVICE_GCS_BUCKET_BACKUP = "backup.pratilipi.com";
			CDN = moduleId.equals( "android" ) ? "http://*.ptlp.co" : "https://*.ptlp.co";
			STAGE = moduleId.equals( "gamma" ) || moduleId.equals( "gamma-android" ) ? STAGE_GAMMA : STAGE_PROD;
			VERSION_ID = versionId;
			FIREBASE_DB_URL = "https://prod-pratilipi.firebaseio.com/";
			ECS_PAG_ENDPOINT = "https://prod.pratilipi.com";

		} else if( isGae && ( appId.equals( "devo-pratilipi" ) || appId.equals( "s~devo-pratilipi" ) ) ) {
			DATASOURCE = "gae";
			BLOBSERVICE_GCS_BUCKET = "devo-pratilipi.appspot.com";
			BLOBSERVICE_GCS_BUCKET_BACKUP = "devo-pratilipi.appspot.com";
			CDN = "https://*.ptlp.co";
			STAGE = STAGE_BETA;
			VERSION_ID = versionId;
			FIREBASE_DB_URL = "https://devo-pratilipi.firebaseio.com/";
			ECS_PAG_ENDPOINT = "https://pr-hi.ptlp.co";

		} else {
			DATASOURCE = "mock";
			BLOBSERVICE_GCS_BUCKET = "localhost";
			BLOBSERVICE_GCS_BUCKET_BACKUP = "localhost";
			CDN = null;
			STAGE = STAGE_ALPHA;
			VERSION_ID = null;
			FIREBASE_DB_URL = null;
			ECS_PAG_ENDPOINT = null;

		}
	}
	
}
