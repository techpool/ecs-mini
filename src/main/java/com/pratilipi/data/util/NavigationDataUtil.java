package com.pratilipi.data.util;

import java.util.ArrayList;
import java.util.List;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.common.type.Language;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.client.NavigationData;
import com.pratilipi.data.type.Navigation;

public class NavigationDataUtil {
	
	public static List<NavigationData> getNavigationDataList( Language language )
			throws UnexpectedServerException {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		List<Navigation> navigationList = dataAccessor.getNavigationList( language );
		List<NavigationData> navigationDataList = new ArrayList<>( navigationList.size() );
		for( Navigation navigation : navigationList )
			navigationDataList.add( new NavigationData( navigation.getTitle(), navigation.getLinkList() ) );
		return navigationDataList;
		
	}

}