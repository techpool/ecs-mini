package com.pratilipi.common.util;
import java.io.IOException;
import java.text.ParseException;

import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.googlecode.objectify.ObjectifyService;
import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.BlobAccessor;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.DocAccessor;
import com.pratilipi.data.Memcache;
import com.pratilipi.data.SearchAccessor;

public class DataStoreUtil {

	public static void main( String ... args ) throws IOException, UnexpectedServerException, InterruptedException, ParseException {
		
		RemoteApiOptions options = new RemoteApiOptions()
				.server( "m.gamma.pratilipi.com", 80 )
				.useServiceAccountCredential(
						"prod-pratilipi@appspot.gserviceaccount.com",
						"PrivateKey.p12" )
			    .remoteApiPath( "/remote_api" );
		RemoteApiInstaller installer = new RemoteApiInstaller();
		installer.install( options );
		
		ObjectifyService.begin();

		
		Memcache memcache = DataAccessorFactory.getL2CacheAccessor();
		GcsService gcsService = GcsServiceFactory.createGcsService( RetryParams.getDefaultInstance() );
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		SearchAccessor searchAccessor = DataAccessorFactory.getSearchAccessor();
		BlobAccessor blobAccessor = DataAccessorFactory.getBlobAccessor();
		DocAccessor docAccessor = DataAccessorFactory.getDocAccessor();
		
		
		// START
		
		
		// END
		
		installer.uninstall();
		
	}
	
}
