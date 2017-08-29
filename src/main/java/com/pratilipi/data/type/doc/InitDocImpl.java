package com.pratilipi.data.type.doc;

import java.util.ArrayList;
import java.util.List;

import com.pratilipi.data.type.InitBannerDoc;
import com.pratilipi.data.type.InitDoc;

public class InitDocImpl implements InitDoc {

	private List<InitBannerDocImpl> banners;
	
	
	public List<InitBannerDoc> getBanners() {
		return banners == null
				? new ArrayList<InitBannerDoc>( 0 )
				: new ArrayList<InitBannerDoc>( banners );
	}
	
	public void setBanners( List<InitBannerDoc> banners ) {
		if( banners == null || banners.size() == 0 ) {
			this.banners = null;
		} else {
			this.banners = new ArrayList<>( banners.size() );
			for( InitBannerDoc bannerDoc : banners )
				this.banners.add( (InitBannerDocImpl) bannerDoc );
		}
	}
	
}
